package com.nokor.ersys.messaging.ws.resource.accounting.cfg;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;
import com.nokor.ersys.messaging.ws.resource.accounting.AccountingWsPath;
import com.nokor.ersys.messaging.ws.resource.accounting.BaseAccountingSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_CONFIGS + AccountingWsPath.ACCOUNTING + AccountingWsPath.JOURNALS)
public class JournalEventSrvRsc extends BaseAccountingSrvRsc {
	
	/**	
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS)
	public Response list(@QueryParam(AccountingWsPath.EVENT_GROUP_ID) Long eventGroupId) {	
		return list((Long) null, eventGroupId);
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS + AccountingWsPath.JOURNAL_RECEIPTS)
	public Response listReceiptEvents(@QueryParam(AccountingWsPath.EVENT_GROUP_ID) Long eventGroupId) {
		return list(JournalEvent.RECEIPTS, eventGroupId);
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS + AccountingWsPath.JOURNAL_PAYMENTS)
	public Response listPaymentEvents(@QueryParam(AccountingWsPath.EVENT_GROUP_ID) Long eventGroupId) {
		return list(JournalEvent.PAYMENTS, eventGroupId);
	}
	
	/**
	 * 
	 * @param journalId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.PATH_JOURNAL_ID + AccountingWsPath.EVENTS)
	public Response list(@PathParam(AccountingWsPath.JOURNAL_ID) Long journalId,
			@QueryParam(AccountingWsPath.EVENT_GROUP_ID) Long eventGroupId) {
		try {
			EJournalEventGroup group = null;
			if (eventGroupId != null) { 
				group = EJournalEventGroup.getById(eventGroupId);
				if (group == null) {
					return ResponseHelper.ok(new ArrayList<JournalEventDTO>());
				}
			}
			JournalEventRestriction restrictions = new JournalEventRestriction();
			restrictions.setJournalId(journalId);
			restrictions.setEventGroup(group);
			List<JournalEvent> entries = ENTITY_SRV.list(restrictions);
			List<JournalEventDTO> eventDTOs = toJournalEventDTOs(entries);
			
			return ResponseHelper.ok(eventDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching JournalEvent [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	

	/**
	 * 
	 * @param eventDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS)
	public Response create(JournalEventDTO eventDTO) {
		return create((Long) null, eventDTO);
	}
	
	/**
	 * 
	 * @param eventDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.PATH_JOURNAL_ID + AccountingWsPath.EVENTS)
	public Response create(@PathParam(AccountingWsPath.JOURNAL_ID) Long journalId, JournalEventDTO eventDTO) {
		try {
			if (journalId != null) {
				eventDTO.setJournalId(journalId);
			}
			JournalEvent journalEvent = toJournalEvent(eventDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			} 
			ENTITY_SRV.saveOrUpdate(journalEvent);
			eventDTO.setId(journalEvent.getId());
			return ResponseHelper.ok(eventDTO);
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
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS + BaseWsPath.PATH_ID)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEvent - id. [" + id + "]");
		
			JournalEvent event = ENTITY_SRV.getById(JournalEvent.class, id);
			
			if (event == null) {
				String errMsg = "JournalEvent [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			JournalEventDTO eventDTO = toJournalEventDTO(event);
			
			return ResponseHelper.ok(eventDTO);
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
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTS + BaseWsPath.PATH_ID)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEvent [" + (id != null ? id : NULL) + "]");
			
			JournalEvent event = ENTITY_SRV.getById(JournalEvent.class, id);
			
			if (event == null) {
				String errMsg = "JournalEvent [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(event);
			
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
	
}
