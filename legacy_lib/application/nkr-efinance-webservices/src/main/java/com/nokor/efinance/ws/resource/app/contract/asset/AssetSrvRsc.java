package com.nokor.efinance.ws.resource.app.contract.asset;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetRestriction;
import com.nokor.efinance.share.asset.AssetCriteriaDTO;
import com.nokor.efinance.share.asset.AssetDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Asset web service
 * @author uhout.cheng
 */
@Path("/assets")
public class AssetSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * 
	 * List all asset models
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam("registrationNumber") String registrationNumber, 
			@QueryParam("chassisNumber") String chassisNumber, 
			@QueryParam("engineNumber") String engineNumber, @QueryParam("contractId") String contractId) {		
		try {	
			
			if (StringUtils.isBlank(registrationNumber)
					&& StringUtils.isBlank(chassisNumber)
					&& StringUtils.isBlank(engineNumber)
					&& StringUtils.isBlank(contractId)) {
				return ResponseHelper.badRequest("You should provide at least one filter among registrationNumber, chassisNumber, engineNumber, contractId");
			} else {
			
				AssetRestriction restrictions = new AssetRestriction();
				if (StringUtils.isNotBlank(registrationNumber)) {
					restrictions.setRegistrationNumber(registrationNumber);
				}
				if (StringUtils.isNotBlank(chassisNumber)) {
					restrictions.setChassisNumber(chassisNumber);
				}
				if (StringUtils.isNotBlank(engineNumber)) {
					restrictions.setEngineNumber(engineNumber);	
				}	
				if (StringUtils.isNotBlank(contractId)) {
					restrictions.setContractId(contractId);	
				}
				restrictions.setMaxResults(1);
				
				List<Asset> assets = ENTITY_SRV.list(restrictions);
				if (assets == null || assets.isEmpty()) {
					String errMsg = "Asset [registrationNumber = " +  registrationNumber + ", chassisNumber = " + chassisNumber + ", engineNumber = " + engineNumber + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
				}
				return ResponseHelper.ok(toAssetDTO(assets.get(0)));
			}
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * Get asset by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Asset - id. [" + id + "]");
		
			Asset asset = ENTITY_SRV.getById(Asset.class, id);
			
			if (asset == null) {
				String errMsg = "Asset [" + id + "]";
				throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);			
			}
			
			AssetDTO assetDTO = toAssetDTO(asset);
			
			return ResponseHelper.ok(assetDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * Update asset by registration number
	 * @param id
	 * @param assetDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, AssetDTO assetDTO) {
		try {
			LOG.debug("Asset - id. [" + id + "]");
			assetDTO.setId(id);
			Asset asset = toAsset(assetDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(asset);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityUpdateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Update asset by registration number
	 * @param id
	 * @param assetDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@QueryParam("registrationNumber") String registrationNumber, AssetDTO assetDTO) {
		try {
			LOG.debug("Asset Registration - No. [" + registrationNumber + "]");
			Asset asset = toUpdateAsset(assetDTO, registrationNumber);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(asset);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Search asset 
	 * @param AssetCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response search(AssetCriteriaDTO assetCriteriaDTO) {
		try {
			LOG.debug("Asset [" + assetCriteriaDTO + "]");
			
			AssetRestriction restrictions = toAssetRestriction(assetCriteriaDTO);
			List<Asset> assets = ENTITY_SRV.list(restrictions);
			List<AssetDTO> assetDTOs = new ArrayList<>();
			for (Asset asset : assets) {
				assetDTOs.add(toAssetDTO(asset));
			}
			
			return ResponseHelper.ok(assetDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	
	/**
	 * @param criteria
	 * @return
	 */
	private AssetRestriction toAssetRestriction(AssetCriteriaDTO criteria) {
		AssetRestriction restrictions = new AssetRestriction();
		restrictions.setChassisNumber(criteria.getChassisNumber());
		restrictions.setEngineNumber(criteria.getEngineNumber());
		return restrictions;
	}
	
	/**
	 * Convert from AssetDTO to Asset
	 * @param assetDTO
	 * @param registrationNumber
	 * @return
	 */
	private Asset toUpdateAsset(AssetDTO assetDTO, String registrationNumber) {
		Asset asset = null;
		if (StringUtils.isNotEmpty(registrationNumber)) {
			asset = ASS_SRV.getAssetByPlateNumber(registrationNumber);
			if (asset == null) {
				messages.add(FinWsMessage.ASSET_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} else {
				if (assetDTO.getColor() != null) {
					asset.setColor(EColor.getById(assetDTO.getColor().getId()));
				} else {
					if (asset.getColor() == null) {
						messages.add(FinWsMessage.ASSET_COLOR_ID_MANDATORY);
					}
				}
				asset.setChassisNumber(assetDTO.getChassisNumber());
				asset.setEngineNumber(assetDTO.getEngineNumber());
			}
		}
		return asset;
	}
	
}
