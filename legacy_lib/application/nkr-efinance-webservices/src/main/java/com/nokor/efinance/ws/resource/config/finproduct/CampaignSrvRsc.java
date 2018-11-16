package com.nokor.efinance.ws.resource.config.finproduct;

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

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.AreaRestriction;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignAssetModel;
import com.nokor.efinance.core.financial.model.CampaignDealer;
import com.nokor.efinance.core.financial.model.CreditControl;
import com.nokor.efinance.core.financial.model.CreditControlItem;
import com.nokor.efinance.core.financial.service.CampaignAssetModelRestriction;
import com.nokor.efinance.core.financial.service.CampaignRestriction;
import com.nokor.efinance.share.campaign.CampaignDTO;
import com.nokor.efinance.share.campaign.CampaignSerieDTO;
import com.nokor.efinance.share.campaign.CreditControlDTO;
import com.nokor.efinance.share.campaign.CreditControlItemDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Marketing campaign web service
 * @author uhout.cheng
 */
@Path("/configs/campaigns")
public class CampaignSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all campaigns
	 * @param deaId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ConfigWsPath.DEALER_ID) Long deaId) {		
		try {				
			CampaignRestriction restrictions = new CampaignRestriction();
			if (deaId != null) {
				restrictions.setDealerId(deaId);
			}
			List<Campaign> campaigns = CAM_SRV.list(restrictions);
			List<CampaignDTO> campaignsDTO = new ArrayList<>();
			for (Campaign campaign : campaigns) {
				campaignsDTO.add(toCampaignDTO(campaign));
			}
			return ResponseHelper.ok(campaignsDTO);
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
	 * List campaign by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Campaign - id. [" + id + "]");
		
			Campaign campaign = CAM_SRV.getById(Campaign.class, id);
			
			if (campaign == null) {
				String errMsg = "Campaign [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			CampaignDTO campaignDTO = toCampaignDTO(campaign);
			
			return ResponseHelper.ok(campaignDTO);
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
	 * Get campaign series by series id in Campaign id
	 * @param camId
	 * @param serId
	 * @return
	 */
	@GET
	@Path("/{camId}/series/{serId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getCampaignAssetModel(@PathParam("camId") Long camId, @PathParam("serId") Long serId) {
		try {
			LOG.debug("Campaign - id. [" + camId + "]");
			LOG.debug("Series - id. [" + serId + "]");
		
			Campaign campaign = ENTITY_SRV.getById(Campaign.class, camId);
			AssetModel serie = ENTITY_SRV.getById(AssetModel.class, serId);
			
			if (campaign == null) {
				String errMsg = "Campaign [" + camId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				if (serie == null) {
					String errMsg = "Series [" + serId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			CampaignAssetModelRestriction restrictions = new CampaignAssetModelRestriction();
			restrictions.setCampaignId(camId);
			restrictions.setSerieId(serId);
			List<CampaignAssetModel> campaignAssetModels = ENTITY_SRV.list(restrictions);
			CampaignSerieDTO campaignSerieDTO = null;
			if (campaignAssetModels != null && !campaignAssetModels.isEmpty()) {
				campaignSerieDTO = toCampaignSerieDTO(campaignAssetModels.get(0));
			}
			
			return ResponseHelper.ok(campaignSerieDTO);
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
	 * Create Campaign
	 * @param campaignDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(CampaignDTO campaignDTO) {
		try {
			Campaign campaign = toCampaign(campaignDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			CAM_SRV.saveOrUpdateCampagin(campaign);
			
			return ResponseHelper.ok(toCampaignDTO(campaign));
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
	 * Update campaign
	 * @param id
	 * @param campaignDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, CampaignDTO campaignDTO) {
		try {
			LOG.debug("Campaign - id. [" + id + "]");
			Campaign campaign = toCampaign(campaignDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			CAM_SRV.saveOrUpdateCampagin(campaign);
			
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
		} catch (DataIntegrityViolationException e) {
			String errMsg = I18N.messageObjectAlreadyExists("Campaign code [" + campaignDTO.getCode() + "]");
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Delete campaign
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Campaign - id. [" + id + "]");
			
			Campaign campaign = ENTITY_SRV.getById(Campaign.class, id);
			
			if (campaign == null) {
				String errMsg = "Campaign [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
	
			CAM_SRV.deleteCampaign(campaign);
			
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
			String errMsg = "Campaign id [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Convert from CampaginDTO to Campaign
	 * @param campaignDTO
	 * @param id
	 * @return
	 */
	private Campaign toCampaign(CampaignDTO campaignDTO, Long id) {
		Campaign campaign = null;
		if (id != null) {
			campaign = ENTITY_SRV.getById(Campaign.class, id);
			if (campaign == null) {
				messages.add(FinWsMessage.CAMPAIGN_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			campaign = new Campaign();
		}
		
		/*if (StringUtils.isNotEmpty(campaignDTO.getCode())) {
			if (id == null && isAlreadyExist(campaignDTO.getCode())) {
				messages.add(FinWsMessage.DUPLICATE_CAMPAIGN_CODE);
			} else {
				campaign.setCode(campaignDTO.getCode());
			}
		} else {
			messages.add(FinWsMessage.CAMPAIGN_CODE_MANDATORY);
		}*/
		
		if (StringUtils.isNotEmpty(campaignDTO.getDescEn())) {
			campaign.setDescEn(campaignDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.CAMPAIGN_DESC_EN_MANDATORY);
		}
		
		if (campaignDTO.getStartDate() != null) {
			campaign.setStartDate(campaignDTO.getStartDate());
		} else {
			messages.add(FinWsMessage.CAMPAIGN_START_DATE_MANDATORY);
		}
		
		if (campaignDTO.getEndDate() != null) {
			campaign.setEndDate(campaignDTO.getEndDate());
		} else {
			messages.add(FinWsMessage.CAMPAIGN_END_DATE_MANDATORY);
		}
		
		campaign.setFlatRate(campaignDTO.getFlatRate());
		campaign.setMaxFlatRate(campaignDTO.getMaxFlatRate());
		campaign.setValidForAllDealers(campaignDTO.isValidForAllDealers());
		
		if (campaignDTO.getArea() != null) {
			AreaRestriction restrictions = new AreaRestriction();
			restrictions.setAreaId(campaignDTO.getArea().getId());
			restrictions.setColType(EColType.MKT);
			List<Area> areas = ENTITY_SRV.list(restrictions);
			Area area = null;
			if (areas != null && !areas.isEmpty()) {
				area = areas.get(0);
			}
			campaign.setArea(area);
		}
		
		return campaign;
	}
		
	/**
	 * Convert to Campaign data transfer
	 * @param campaign
	 * @return
	 */
	private CampaignDTO toCampaignDTO(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO();
		campaignDTO.setId(campaign.getId());
		campaignDTO.setCode(campaign.getCode());
		campaignDTO.setDescEn(campaign.getDescEn());
		campaignDTO.setStartDate(campaign.getStartDate());
		campaignDTO.setEndDate(campaign.getEndDate());
		campaignDTO.setFlatRate(MyNumberUtils.getDouble(campaign.getFlatRate()));
		campaignDTO.setMaxFlatRate(MyNumberUtils.getDouble(campaign.getMaxFlatRate()));
		campaignDTO.setValidForAllDealers(campaign.isValidForAllDealers());
		campaignDTO.setArea(campaign.getArea() != null ? getAreasDTO(campaign.getArea().getId()) : null);
		
		List<CampaignSerieDTO> campaignSerieDTOs = new ArrayList<CampaignSerieDTO>();
		for (CampaignAssetModel campaignSerie : campaign.getAssetModels()) {
			campaignSerieDTOs.add(toCampaignSerieDTO(campaignSerie));
		}
		
		List<UriDTO> dealerUriDTOs = new ArrayList<>();
		if (campaign.getDealers() != null) {
			for (CampaignDealer campaignDealer : campaign.getDealers()) {
				dealerUriDTOs.add(getDealersDTO(campaignDealer.getDealer().getId()));
			}
		}
		
		if (campaign.getCreditControl() != null) {
			campaignDTO.setCreditControl(toCreditControlDTO(campaign.getCreditControl()));
		}
		campaignDTO.setSeries(campaignSerieDTOs);
		campaignDTO.setDealers(dealerUriDTOs);
		
		return campaignDTO;
	}
	
	/**
	 * 
	 * @param campaignSerie
	 * @return
	 */
	private CampaignSerieDTO toCampaignSerieDTO(CampaignAssetModel campaignSerie) {
		CampaignSerieDTO campaignSerieDTO = new CampaignSerieDTO();
		campaignSerieDTO.setId(campaignSerie.getId());
		AssetModel serie = campaignSerie.getAssetModel();
		AssetRange model = null;
		AssetMake brand = null;
		if (serie != null) {
			model = serie.getAssetRange();
			if (model != null) {
				brand = model.getAssetMake();
			}
		}
		campaignSerieDTO.setBrand(brand != null ? getAssetMakesDTO(brand.getId()) : null);
		campaignSerieDTO.setModel(model != null ? getAssetRangesDTO(model.getId()) : null);
		campaignSerieDTO.setSerie(serie != null ? getAssetModelsDTO(serie.getId()) : null);
		campaignSerieDTO.setStandardFinanceAmount(MyNumberUtils.getDouble(campaignSerie.getStandardFinanceAmount()));
		return campaignSerieDTO;
	}
	
	/**
	 * @param creditControl
	 * @return
	 */
	private CreditControlDTO toCreditControlDTO(CreditControl creditControl) {
		CreditControlDTO creditControlDTO = new CreditControlDTO();
		creditControlDTO.setId(creditControl.getId());
		creditControlDTO.setDesc(creditControl.getDesc());
		creditControlDTO.setDescEn(creditControl.getDescEn());
		
		List<CreditControlItemDTO> creditControlItemDTOs = new ArrayList<>();
		if (creditControl.getCreditControlItems() != null) {
			for (CreditControlItem creditControlItem : creditControl.getCreditControlItems()) {
				creditControlItemDTOs.add(toCreditControlItemDTO(creditControlItem));
			}
		}
		creditControlDTO.setCreditControlItems(creditControlItemDTOs);
		return creditControlDTO;
	}
	
	/**
	 * Convert to CampaignCreditControl data transfer
	 * @param creditControlItem
	 * @return
	 */
	private CreditControlItemDTO toCreditControlItemDTO(CreditControlItem creditControlItem) {
		CreditControlItemDTO creditControlDTO = new CreditControlItemDTO();
		creditControlDTO.setId(creditControlItem.getId());
		creditControlDTO.setCode(creditControlItem.getCode());
		creditControlDTO.setValue1(creditControlItem.getValue1());
		creditControlDTO.setValue2(creditControlItem.getValue2());
		creditControlDTO.setValue3(creditControlItem.getValue3());
		return creditControlDTO;
	}
		
	/**
	 * @param code
	 * @return
	 */
	/*private boolean isAlreadyExist(String code) {
		Campaign campaign = CAM_SRV.getByCode(Campaign.class, code);
		return campaign != null;
	}*/
}
