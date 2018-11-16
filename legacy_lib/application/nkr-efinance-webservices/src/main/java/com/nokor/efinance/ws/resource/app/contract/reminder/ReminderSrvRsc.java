package com.nokor.efinance.ws.resource.app.contract.reminder;

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
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.collection.service.ReminderRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ReminderDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.REMINDERS)
public class ReminderSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all reminders by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			ReminderRestriction restrictions = new ReminderRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setConId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			List<Reminder> reminders = ENTITY_SRV.list(restrictions);
			List<ReminderDTO> reminderDTOs= toReminderDTOs(reminders);
			
			return ResponseHelper.ok(reminderDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching reminders [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List reminder by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Reminder - id. [" + id + "]");
			
			Reminder reminder = ENTITY_SRV.getById(Reminder.class, id);
			if (reminder == null) {
				String errMsg = "Reminder [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			ReminderDTO reminderDTO = toReminderDTO(reminder);
			
			return ResponseHelper.ok(reminderDTO);
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
	 * Create a reminder
	 * @param reminderDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ReminderDTO reminderDTO) {
		try {
			if (reminderDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("ReminderDTO"));
			}
			Reminder reminder = toReminder(reminderDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			REMINDER_SRV.saveOrUpdateReminder(reminder);
			
			return ResponseHelper.ok(toReminderDTO(reminder));
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
	 * Update a reminder by id
	 * @param id
	 * @param reminderDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ReminderDTO reminderDTO) {
		try {
			LOG.debug("Reminder - id. [" + id + "]");
			reminderDTO.setId(id);
			Reminder reminder = toReminder(reminderDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			REMINDER_SRV.saveOrUpdateReminder(reminder);
			
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
	 * Delete a reminder by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Reminder [" + (id != null ? id : NULL) + "]");
			
			Reminder reminder = ENTITY_SRV.getById(Reminder.class, id);
			if (reminder == null) {
				String errMsg = "Reminder [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(reminder);
			
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
	 * @param reminder
	 * @return
	 */
	private ReminderDTO toReminderDTO(Reminder reminder) {
		ReminderDTO reminderDTO = new ReminderDTO();
		reminderDTO.setId(reminder.getId());
		reminderDTO.setContractNo(reminder.getContract() != null ? reminder.getContract().getReference() : StringUtils.EMPTY);
		reminderDTO.setComment(reminder.getComment());
		reminderDTO.setDate(reminder.getDate());
		reminderDTO.setDismiss(reminder.isDismiss());
		return reminderDTO;
	}
	
	/**
	 * @param reminders
	 * @return
	 */
	private List<ReminderDTO> toReminderDTOs(List<Reminder> reminders) {
		List<ReminderDTO> reminderDTOs = new ArrayList<ReminderDTO>();
		for (Reminder reminder : reminders) {
			reminderDTOs.add(toReminderDTO(reminder));
		}
		return reminderDTOs;
	}
	
	/**
	 * @param reminderDTO
	 * @return
	 */
	private Reminder toReminder(ReminderDTO reminderDTO) {
		Reminder reminder = null;
		if (reminderDTO.getId() != null) {
			reminder = ENTITY_SRV.getById(Reminder.class, reminderDTO.getId());
			if (reminder == null) {
				messages.add(FinWsMessage.REMINDER_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 reminder = Reminder.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(reminderDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(reminderDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		reminder.setContract(contract);
		
		if (StringUtils.isEmpty(reminderDTO.getComment())) {
			messages.add(FinWsMessage.COMMENT_MANDATORY);
		} else {
			reminder.setComment(reminderDTO.getComment());
		}
		
		if (reminderDTO.getDate() == null) {
			messages.add(FinWsMessage.DATE_MANDATORY);
		} else {
			reminder.setDate(reminderDTO.getDate());
		}
		
		reminder.setDismiss(reminderDTO.isDismiss());
		
		return reminder;
	}

}
