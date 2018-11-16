package com.nokor.efinance.ws.resource.config.insurance;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.insurancecampaign.InsuranceCampaignDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Insurance campaign web service
 * @author uhout.cheng
 */
@Path("/configs/insurances/campaigns")
public class InsuranceCampaignSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all insurance campaigns
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<InsuranceCampaign> insuranceCampaigns = ENTITY_SRV.list(InsuranceCampaign.class);
			List<InsuranceCampaignDTO> insuranceCampaignDTOs = new ArrayList<>();
			for (InsuranceCampaign insuranceCampaign : insuranceCampaigns) {
				insuranceCampaignDTOs.add(toInsuranceCampaignDTO(insuranceCampaign));
			}
			return ResponseHelper.ok(insuranceCampaignDTOs);
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
	 * List insurance campaign by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Insurance campaign - id. [" + id + "]");
		
			InsuranceCampaign insCampaign = ENTITY_SRV.getById(InsuranceCampaign.class, id);
			
			if (insCampaign == null) {
				String errMsg = "Insurance campaign [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			InsuranceCampaignDTO insCampaignDTO = toInsuranceCampaignDTO(insCampaign);
			
			return ResponseHelper.ok(insCampaignDTO);
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
	 * Create insurance campaign
	 * @param insCampaignDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(InsuranceCampaignDTO insCampaignDTO) {
		try {
			InsuranceCampaign insCampaign = toInsuranceCampaign(insCampaignDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(insCampaign);
			insCampaignDTO.setId(insCampaign.getId());
			
			return ResponseHelper.ok(insCampaignDTO);
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
	 * Update insurance campaign
	 * @param id
	 * @param insCampaignDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, InsuranceCampaignDTO insCampaignDTO) {
		try {
			LOG.debug("Insurance campaign - id. [" + id + "]");
			InsuranceCampaign insCampaign = toInsuranceCampaign(insCampaignDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(insCampaign);
			
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
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Delete insurance campaign
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Insurance campaign - id. [" + id + "]");
			
			InsuranceCampaign insCampaign = ENTITY_SRV.getById(InsuranceCampaign.class, id);
			
			if (insCampaign == null) {
				String errMsg = "Insurance campaign [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(insCampaign);
			
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
			String errMsg = "Insurance campaign id [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Convert from InsuranceCampaignDTO to InsuranceCampaign
	 * @param insCampaignDTO
	 * @param id
	 * @return
	 */
	private InsuranceCampaign toInsuranceCampaign(InsuranceCampaignDTO insCampaignDTO, Long id) {
		InsuranceCampaign insCampaign = null;
		if (id != null) {
			insCampaign = ENTITY_SRV.getById(InsuranceCampaign.class, id);
			if (insCampaign == null) {
				messages.add(FinWsMessage.INSURANCE_CAMPAIGN_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			insCampaign = new InsuranceCampaign();
		}
		
		if (StringUtils.isNotEmpty(insCampaignDTO.getCode())) {
			if (id == null && isAlreadyExist(insCampaignDTO.getCode())) {
				messages.add(FinWsMessage.DUPLICATE_INSURANCE_CAMPAIGN_CODE);
			} else {
				insCampaign.setCode(insCampaignDTO.getCode());
			}
		} else {
			messages.add(FinWsMessage.INSURANCE_CAMPAIGN_CODE_MANDATORY);
		}
		
		if (StringUtils.isNotEmpty(insCampaignDTO.getDescEn())) {
			insCampaign.setDescEn(insCampaignDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.INSURANCE_CAMPAIGN_DESC_EN_MANDATORY);
		}
		
		if (insCampaignDTO.getStartDate() != null) {
			insCampaign.setStartDate(insCampaignDTO.getStartDate());
		} else {
			messages.add(FinWsMessage.INSURANCE_CAMPAIGN_START_DATE_MANDATORY);
		}
		
		if (insCampaignDTO.getEndDate() != null) {
			insCampaign.setEndDate(insCampaignDTO.getEndDate());
		} else {
			messages.add(FinWsMessage.INSURANCE_CAMPAIGN_END_DATE_MANDATORY);
		}
		
		insCampaign.setDesc(insCampaignDTO.getDesc());
		insCampaign.setNbCoverageInYears(insCampaignDTO.getNbCoverageInYears());
		insCampaign.setInsuranceFee(insCampaignDTO.getInsuranceFee());
				
		if (insCampaignDTO.getInsuranceCompanyId() != null) {
			insCampaign.setInsuranceCompany(ENTITY_SRV.getById(Organization.class, insCampaignDTO.getInsuranceCompanyId()));
		}
		
		return insCampaign;
	}
		
	/**
	 * Convert to InsuranceCampaign data transfer
	 * @param insCampaign
	 * @return
	 */
	private InsuranceCampaignDTO toInsuranceCampaignDTO(InsuranceCampaign insCampaign) {
		InsuranceCampaignDTO insCampaignDTO = new InsuranceCampaignDTO();
		insCampaignDTO.setId(insCampaign.getId());
		insCampaignDTO.setCode(insCampaign.getCode());
		insCampaignDTO.setDescEn(insCampaign.getDescEn());
		insCampaignDTO.setDesc(insCampaign.getDesc());
		insCampaignDTO.setStartDate(insCampaign.getStartDate());
		insCampaignDTO.setEndDate(insCampaign.getEndDate());
		insCampaignDTO.setNbCoverageInYears(MyNumberUtils.getInteger(insCampaign.getNbCoverageInYears()));
		insCampaignDTO.setInsuranceFee(MyNumberUtils.getDouble(insCampaign.getInsuranceFee()));
			
		Organization company = insCampaign.getInsuranceCompany();
		insCampaignDTO.setInsuranceCompanyId(company != null ? company.getId() : null);
		
		return insCampaignDTO;
	}
	
	/**
	 * @param code
	 * @return
	 */
	private boolean isAlreadyExist(String code) {
		InsuranceCampaign insCampaign = ENTITY_SRV.getByCode(InsuranceCampaign.class, code);
		return insCampaign != null;
	}
}
