package com.nokor.efinance.ws.resource.app.contract.email;

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
import com.nokor.efinance.core.contract.model.Email;
import com.nokor.efinance.core.contract.service.EmailRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.EmailDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.EMAILS)
public class EmailSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all email by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LOG.debug("Contract - Reference. [" + contractNo + "]");
			EmailRestriction restrictions = new EmailRestriction();
			restrictions.setContractNo(contractNo);
			
			List<Email> emails = ENTITY_SRV.list(restrictions);
			List<EmailDTO> emailDTOs= toEmailDTOs(emails);
			
			return ResponseHelper.ok(emailDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract email [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List email by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Email - id. [" + id + "]");
			
			Email email = ENTITY_SRV.getById(Email.class, id);
			if (email == null) {
				String errMsg = "Contract-Email [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			EmailDTO emailDTO = toEmailDTO(email);
			
			return ResponseHelper.ok(emailDTO);
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
	 * Create an email
	 * @param emailDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(EmailDTO emailDTO) {
		try {
			if (emailDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("Email"));
			}
			Email email = toEmail(emailDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(email);
			
			return ResponseHelper.ok(toEmailDTO(email));
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
	 * Update an email by id
	 * @param id
	 * @param emailDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, EmailDTO emailDTO) {
		try {
			LOG.debug("Email - id. [" + id + "]");
			emailDTO.setId(id);
			Email email = toEmail(emailDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(email);
			
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
	 * Delete an email by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Email [" + (id != null ? id : NULL) + "]");
			
			Email email = ENTITY_SRV.getById(Email.class, id);
			if (email == null) {
				String errMsg = "Email [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(email);
			
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
	 * @param email
	 * @return
	 */
	private EmailDTO toEmailDTO(Email email) {
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setId(email.getId());
		emailDTO.setCreationDate(email.getCreateDate());
		emailDTO.setContractNo(email.getContract() != null ? email.getContract().getReference() : StringUtils.EMPTY);
		emailDTO.setApplicantType(email.getSendTo() != null ? toRefDataDTO(email.getSendTo()) : null);
		emailDTO.setUserLogin(email.getUserLogin());
		emailDTO.setSubject(email.getSubject());
		emailDTO.setMessage(email.getMessage());
		emailDTO.setSendEmail(email.getSendEmail());
		emailDTO.setSendDate(email.getSendDate());
		return emailDTO;
	}
	
	/**
	 * @param emails
	 * @return
	 */
	private List<EmailDTO> toEmailDTOs(List<Email> emails) {
		List<EmailDTO> emailDTOs = new ArrayList<EmailDTO>();
		for (Email email : emails) {
			emailDTOs.add(toEmailDTO(email));
		}
		return emailDTOs;
	}
	
	/**
	 * @param emailDTO
	 * @return
	 */
	private Email toEmail(EmailDTO emailDTO) {
		Email email = null;
		if (emailDTO.getId() != null) {
			email = ENTITY_SRV.getById(Email.class, emailDTO.getId());
			if (email == null) {
				messages.add(FinWsMessage.EMAIL_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 email = Email.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(emailDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(emailDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		email.setContract(contract);
		
		if (emailDTO.getApplicantType() != null) {
			email.setSendTo(EApplicantType.getById(emailDTO.getApplicantType().getId()));
			if (email.getSendTo() == null) {
				email.setSendTo(EApplicantType.getById(1l));
			}
		} else {
			email.setSendTo(EApplicantType.getById(1l));
		}
	
		email.setUserLogin(emailDTO.getUserLogin());
		email.setSubject(emailDTO.getSubject());
		email.setMessage(emailDTO.getMessage());
		email.setSendEmail(emailDTO.getSendEmail());
		email.setSendDate(emailDTO.getSendDate());
		return email;
	}

}
