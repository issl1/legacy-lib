package com.nokor.efinance.ws.resource.app.contract.sms;

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

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.contract.service.ContractSmsRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ContractSmsDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.SMS)
public class ContractSmsSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all SMS by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LOG.debug("Contract - Reference. [" + contractNo + "]");
			ContractSmsRestriction restrictions = new ContractSmsRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setConId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			List<ContractSms> contractSms = ENTITY_SRV.list(restrictions);
			List<ContractSmsDTO> smsDTOs= toContractSmsDTOs(contractSms);
			
			return ResponseHelper.ok(smsDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract sms [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List SMS by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractSms - id. [" + id + "]");
			
			ContractSms sms = ENTITY_SRV.getById(ContractSms.class, id);
			if (sms == null) {
				String errMsg = "Contract-SMS [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			ContractSmsDTO smsDTO = toContractSmsDTO(sms);
			
			return ResponseHelper.ok(smsDTO);
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
	 * Create a SMS
	 * @param smsDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ContractSmsDTO smsDTO) {
		try {
			if (smsDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("ContractSms"));
			}
			ContractSms sms = toContractSms(smsDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			
//			ClientSms.sendSms(sms.getPhoneNumber(), sms.getMessage(), sms.getUserLogin());
			
			NOTE_SRV.saveOrUpdateSMS(sms);
			
			return ResponseHelper.ok(toContractSmsDTO(sms));
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
	 * Update a SMS by id
	 * @param id
	 * @param smsDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ContractSmsDTO smsDTO) {
		try {
			LOG.debug("SMS - id. [" + id + "]");
			smsDTO.setId(id);
			ContractSms sms = toContractSms(smsDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(sms);
			
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
	 * Delete a SMS by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractSms [" + (id != null ? id : NULL) + "]");
			
			ContractSms sms = ENTITY_SRV.getById(ContractSms.class, id);
			if (sms == null) {
				String errMsg = "ContractSms [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(sms);
			
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
			String errMsg = I18N.messageObjectCanNotDelete("" + id);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * @param sms
	 * @return
	 */
	private ContractSmsDTO toContractSmsDTO(ContractSms sms) {
		ContractSmsDTO smsDTO = new ContractSmsDTO();
		smsDTO.setId(sms.getId());
		smsDTO.setCreationDate(sms.getCreateDate());
		smsDTO.setContractNo(sms.getContract() != null ? sms.getContract().getReference() : StringUtils.EMPTY);
		smsDTO.setUserLogin(sms.getUserLogin());
		smsDTO.setMessage(sms.getMessage());
		smsDTO.setPhoneNumber(sms.getPhoneNumber());
		smsDTO.setType(sms.getType());
		smsDTO.setSendTo(sms.getSendTo());
		return smsDTO;
	}
	
	/**
	 * @param sms
	 * @return
	 */
	private List<ContractSmsDTO> toContractSmsDTOs(List<ContractSms> sms) {
		List<ContractSmsDTO> smsDTOs = new ArrayList<ContractSmsDTO>();
		for (ContractSms contractSms : sms) {
			smsDTOs.add(toContractSmsDTO(contractSms));
		}
		return smsDTOs;
	}
	
	/**
	 * @param smsDTO
	 * @return
	 */
	private ContractSms toContractSms(ContractSmsDTO smsDTO) {
		ContractSms sms = null;
		if (smsDTO.getId() != null) {
			sms = ENTITY_SRV.getById(ContractSms.class, smsDTO.getId());
			if (sms == null) {
				messages.add(FinWsMessage.SMS_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 sms = ContractSms.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(smsDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(smsDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		sms.setContract(contract);
		
		sms.setType(smsDTO.getType());
		if (smsDTO.getType() == 1) {
			sms.setSendTo(EApplicantType.C.getDescLocale());
		} else if (smsDTO.getType() == 2) {
			sms.setSendTo(EApplicantType.G.getDescLocale());
		} else {
			sms.setSendTo(EApplicantType.R.getDescLocale());
		}
	
		sms.setUserLogin(smsDTO.getUserLogin());
		
		if (StringUtils.isEmpty(smsDTO.getMessage())) {
			messages.add(FinWsMessage.MESSAGE_MANDATORY);
		} else {
			sms.setMessage(smsDTO.getMessage());
		}
		
		if (StringUtils.isEmpty(smsDTO.getPhoneNumber())) {
			messages.add(FinWsMessage.PHONE_NUMBER_MANDATORY);
		} else {
			sms.setPhoneNumber(smsDTO.getPhoneNumber());
		}
		
		return sms;
	}

}
