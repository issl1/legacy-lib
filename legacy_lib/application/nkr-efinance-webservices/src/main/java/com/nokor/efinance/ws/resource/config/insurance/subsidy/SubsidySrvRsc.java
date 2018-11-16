package com.nokor.efinance.ws.resource.config.insurance.subsidy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.efinance.core.financial.service.SubsidyRestriction;
import com.nokor.efinance.share.subsidy.SubsidyDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Model Web Service
 * @author seanglay.chhoeurn
 *
 */
@Path("/configs/insurances/subsidies")
public class SubsidySrvRsc extends FinResourceSrvRsc {

	/**
	 * 
	 * @param brandId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ConfigWsPath.MODEL_ID) Long modelId, @QueryParam(ConfigWsPath.BRAND_ID) Long brandId) {		
		try {					
			SubsidyRestriction restrictions = new SubsidyRestriction();
			restrictions.setBrandId(brandId);
			restrictions.setModelId(modelId);
			
			List<ManufacturerSubsidy> manuFacturerSubsidies = ENTITY_SRV.list(restrictions);
			List<SubsidyDTO> subsidyDTO = new ArrayList<>();
			for (ManufacturerSubsidy manuFacturerSubsidy : manuFacturerSubsidies) {
				subsidyDTO.add(toSubsidyDTO(manuFacturerSubsidy));
			}
			return ResponseHelper.ok(subsidyDTO);
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
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Subsidy - id. [" + id + "]");
		
			ManufacturerSubsidy manuFacturerSubsidy = ENTITY_SRV.getById(ManufacturerSubsidy.class, id);
			
			if (manuFacturerSubsidy == null) {
				String errMsg = "ManufacturerSubsidy-ID [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			SubsidyDTO subsidyDTO = toSubsidyDTO(manuFacturerSubsidy);
			
			return ResponseHelper.ok(subsidyDTO);
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
	 * 
	 * @param subsidyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(SubsidyDTO subsidyDTO) {
		try {
			ManufacturerSubsidy manufacturerSubsidy = toManuFacturerSubsidy(subsidyDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			SUBSIDY_SRV.saveOrUpdateSubsidy(manufacturerSubsidy);

			return ResponseHelper.ok(toSubsidyDTO(manufacturerSubsidy));
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param assetRangeDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, SubsidyDTO assetRangeDTO) {
		try {
			LOG.debug("AssetRange - id. [" + id + "]");
			ManufacturerSubsidy manufacturerSubsidy = toManuFacturerSubsidy(assetRangeDTO);
			manufacturerSubsidy.setId(id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			SUBSIDY_SRV.saveOrUpdateSubsidy(manufacturerSubsidy);
			
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
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Subsidy - id. [" + id + "]");
			
			ManufacturerSubsidy manuFacturerSubsidy = ENTITY_SRV.getById(ManufacturerSubsidy.class, id);
			
			if (manuFacturerSubsidy == null) {
				String errMsg = "Subsidy param [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(manuFacturerSubsidy);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Subsidy id param [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);	
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}

}
