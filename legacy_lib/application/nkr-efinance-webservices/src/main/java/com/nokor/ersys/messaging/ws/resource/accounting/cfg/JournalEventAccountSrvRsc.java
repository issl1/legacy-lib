package com.nokor.ersys.messaging.ws.resource.accounting.cfg;

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
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.service.JournalEventAccountRestriction;
import com.nokor.ersys.messaging.share.accounting.JournalEventAccountDTO;
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
public class JournalEventAccountSrvRsc extends BaseAccountingSrvRsc {

	/**
	 * 
	 * @param eventId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTACCOUNTS)
	public Response list(
						@QueryParam(AccountingWsPath.EVENT_ID) Long eventId,
						@QueryParam(AccountingWsPath.ACCOUNT_ID) Long accountId) {
		return list((Long) null, eventId, accountId);
	}
	
	/**
	 * 
	 * @param journalId
	 * @param eventId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.PATH_JOURNAL_ID + AccountingWsPath.EVENTACCOUNTS)
	public Response list(@PathParam(AccountingWsPath.JOURNAL_ID) Long journalId, 
						@QueryParam(AccountingWsPath.EVENT_ID) Long eventId,
						@QueryParam(AccountingWsPath.ACCOUNT_ID) Long accountId) {
		try {
			JournalEventAccountRestriction restrictions = new JournalEventAccountRestriction();
			restrictions.setJournalId(journalId);
			restrictions.setEventId(eventId);
			restrictions.setAccountId(accountId);
			List<JournalEventAccount> eventAccounts = ENTITY_SRV.list(restrictions);
			List<JournalEventAccountDTO> eventAccountDTOs = toJournalEventAccountDTOs(eventAccounts);
			
			return ResponseHelper.ok(eventAccountDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching JournalEventAccount [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * 
	 * @param eventAccountDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(AccountingWsPath.EVENTACCOUNTS)
	public Response create(JournalEventAccountDTO eventAccountDTO) {
		try {
			JournalEventAccount evAcc = toJournalEventAccount(eventAccountDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			} 
			ENTITY_SRV.saveOrUpdate(evAcc);
			eventAccountDTO.setId(evAcc.getId());
			return ResponseHelper.ok(eventAccountDTO);
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
	@Path(AccountingWsPath.EVENTACCOUNTS + BaseWsPath.PATH_ID)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEventAccount - id. [" + id + "]");
		
			JournalEventAccount entry = ENTITY_SRV.getById(JournalEventAccount.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEventAccount [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			JournalEventAccountDTO entryDTO = toJournalEventAccountDTO(entry);
			
			return ResponseHelper.ok(entryDTO);
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
	@Path(AccountingWsPath.EVENTACCOUNTS + BaseWsPath.PATH_ID)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEventAccount [" + (id != null ? id : NULL) + "]");
			
			JournalEventAccount entry = ENTITY_SRV.getById(JournalEventAccount.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEventAccount [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(entry);
			
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
