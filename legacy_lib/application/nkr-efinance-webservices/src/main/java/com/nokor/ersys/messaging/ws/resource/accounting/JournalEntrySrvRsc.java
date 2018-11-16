package com.nokor.ersys.messaging.ws.resource.accounting;

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

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.service.JournalEntryRestriction;
import com.nokor.ersys.messaging.share.accounting.AccountLedgerDTO;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author bunlong.taing
 */
@Path(AccountingWsPath.ACCOUNTING + AccountingWsPath.JOURNALENTRIES)
public class JournalEntrySrvRsc extends BaseAccountingSrvRsc {
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(BaseWsPath.WKF_STATUS) String wkfStatusCode) {
		try {
			JournalEntryRestriction restrictions = new JournalEntryRestriction();
			if (StringUtils.isNotEmpty(wkfStatusCode)) {
				EWkfStatus wkfStatus = EWkfStatus.getByCode(wkfStatusCode);
				if (wkfStatus == null) {
					throw new EntityNotFoundException("WkfStatus [" + wkfStatusCode + "] not found");
				}
				restrictions.setWkfStatus(wkfStatus);
			}
			List<JournalEntry> entries = ENTITY_SRV.list(restrictions);
			List<JournalEntryDTO> entryDTOs = toJournalEntryDTOs(entries);
			
			return ResponseHelper.ok(entryDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching JournalEntry [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	
	/**
	 * 
	 * @param entryDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(JournalEntryDTO entryDTO) {
		try {
			if (entryDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("entryDTO"));
			}
			JournalEntry journalEntry = toJournalEntry(entryDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			} 
			ENTITY_SRV.saveOrUpdate(journalEntry);
			entryDTO.setId(journalEntry.getId());
			return ResponseHelper.ok(entryDTO);
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
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEntry - id. [" + id + "]");
		
			JournalEntry entry = ENTITY_SRV.getById(JournalEntry.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEntry [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			JournalEntryDTO entryDTO = toJournalEntryDTO(entry);
			
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
	@Path(BaseWsPath.PATH_ID)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEntry [" + (id != null ? id : NULL) + "]");
			
			JournalEntry entry = ENTITY_SRV.getById(JournalEntry.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEntry [" + id + "]";
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

	/**
	 * 
	 * @param id
	 * @return
	 */
	@POST
	@Path(BaseWsPath.PATH_ID + AccountingWsPath.POST)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response postIntoLedger(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEntry - id. [" + id + "]");
		
			JournalEntry entry = ENTITY_SRV.getById(JournalEntry.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEntry [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			List<AccountLedger> ledgers = ACCOUNTING_SRV.postJournalEntryIntoLedger(entry);
			List<AccountLedgerDTO> ledgerDTOs = toAccountLedgerDTOs(ledgers);
			
			return ResponseHelper.ok(ledgerDTOs);
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
	@POST
	@Path(BaseWsPath.PATH_ID + AccountingWsPath.RECONCILE)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response reconcile(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("JournalEntry - id. [" + id + "]");
		
			JournalEntry entry = ENTITY_SRV.getById(JournalEntry.class, id);
			
			if (entry == null) {
				String errMsg = "JournalEntry [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			ACCOUNTING_SRV.reconcileJournalEntry(entry);
			
			return ResponseHelper.ok("SUCCESS");
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
}
