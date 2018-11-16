package com.nokor.ersys.messaging.ws.resource.accounting;

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

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;
import com.nokor.ersys.finance.accounting.service.TransactionEntryRestriction;
import com.nokor.ersys.finance.accounting.workflow.JournalEntryWkfStatus;
import com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfStatus;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryStatus;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author bunlong.taing
 */
@Path(AccountingWsPath.ACCOUNTING + AccountingWsPath.TRANSACTIONENTRIES)
public class TransactionEntrySrvRsc extends BaseAccountingSrvRsc {
	
	/**
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(BaseWsPath.WKF_STATUS) String statusCode) {
		try {
			TransactionEntryRestriction restrictions = new TransactionEntryRestriction();
			if (StringUtils.isNotEmpty(statusCode)) {
				EWkfStatus wkfStatus = EWkfStatus.getByCode(statusCode);
				if (wkfStatus == null) {
					throw new EntityNotFoundException("WkfStatus [" + statusCode + "] not found");
				}
				restrictions.setWkfStatus(wkfStatus);
			}
			List<TransactionEntry> entries = ENTITY_SRV.list(restrictions);
			List<TransactionEntryDTO> entryDTOs = toTransactionEntryDTOs(entries);
			
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
	public Response create(TransactionEntryDTO entryDTO) {
		try {
			if (entryDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("entryDTO"));
			}
			
			LOG.debug("Create Transaction entry [" + entryDTO.getDesc() + "]");
			
			TransactionEntry transactionEntry = toTransactionEntry(entryDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			} 
			ACCOUNTING_SRV.updateTransactionEntry(transactionEntry);
			entryDTO.setId(transactionEntry.getId());
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
			LOG.debug("TransactionEntry - id. [" + id + "]");
		
			TransactionEntry entry = ENTITY_SRV.getById(TransactionEntry.class, id);
			
			if (entry == null) {
				String errMsg = "TransactionEntry [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			TransactionEntryDTO entryDTO = toTransactionEntryDTO(entry);
			
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
			LOG.debug("TransactionEntry [" + (id != null ? id : NULL) + "]");
			
			TransactionEntry entry = ENTITY_SRV.getById(TransactionEntry.class, id);
			
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
	public Response post(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Transaction - id. [" + id + "]");
		
			TransactionEntry transactionEntry = ENTITY_SRV.getById(TransactionEntry.class, id);
			
			if (transactionEntry == null) {
				String errMsg = "JournalEntry [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}			
			transactionEntry.setWkfStatus(TransactionEntryWkfStatus.POSTED);
			for (JournalEntry journalEntry : transactionEntry.getJournalEntries()) {
				journalEntry.setWkfStatus(JournalEntryWkfStatus.VALIDATED);
			}			
			ACCOUNTING_SRV.updateTransactionEntry(transactionEntry);					
			return ResponseHelper.ok();
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
	 * @param entries
	 * @return
	 */
	protected List<TransactionEntryDTO> toTransactionEntryDTOs(List<TransactionEntry> entries) {
		List<TransactionEntryDTO> dtoLst = new ArrayList<>();
		for (TransactionEntry entry : entries) {
			dtoLst.add(toTransactionEntryDTO(entry));
		}
		return dtoLst;
	}
	
	/**
	 * @param transactionEntryDTO
	 * @return
	 */
	private TransactionEntry toTransactionEntry(TransactionEntryDTO transactionEntryDTO) {
		TransactionEntry transactionEntry = new TransactionEntry();
		transactionEntry.setDesc(transactionEntryDTO.getDesc());
		transactionEntry.setDescEn(transactionEntryDTO.getDesc());		
		List<JournalEntry> journalEntries = new ArrayList<>();
		for (JournalEntryDTO journalEntryDTO : transactionEntryDTO.getJournalEntries()) {
			journalEntries.add(toJournalEntry(journalEntryDTO));
		}
		transactionEntry.setJournalEntries(journalEntries);
		if (transactionEntryDTO.getStatus() != null) {
			transactionEntry.setWkfStatus(EWkfStatus.getByCode(transactionEntryDTO.getStatus().name()));
		} else {
			transactionEntry.setWkfStatus(TransactionEntryWkfStatus.DRAFT);
		}
		
		List<String> callBackUrls = transactionEntryDTO.getCallBackUrls();
		if (callBackUrls != null && !callBackUrls.isEmpty()) {
			transactionEntry.setCallBackUrl(getCallBackUrl(callBackUrls));
		}
		return transactionEntry;
	}
	
	/**
	 * @param transactionEntry
	 * @return
	 */
	private TransactionEntryDTO toTransactionEntryDTO(TransactionEntry transactionEntry) {
		TransactionEntryDTO transactionEntryDTO = new TransactionEntryDTO();
		transactionEntryDTO.setId(transactionEntry.getId());
		transactionEntryDTO.setDesc(transactionEntry.getDesc());
		transactionEntryDTO.setStatus(TransactionEntryStatus.valueOf(transactionEntry.getWkfStatus().getCode()));
		transactionEntryDTO.setJournalEntries(toJournalEntryDTOs(transactionEntry.getJournalEntries()));
		List<String> callBackUrls = new ArrayList<>();
		String callBackUrl = transactionEntry.getCallBackUrl();
		if (StringUtils.isNotEmpty(callBackUrl)) {
			String[] callBackUrlParts = callBackUrl.split(";");
			for (int i = 0; i < callBackUrlParts.length; i++) {
				callBackUrls.add(callBackUrlParts[i]);
			}
		}
		transactionEntryDTO.setCallBackUrls(callBackUrls);
		return transactionEntryDTO;
	}
	
	/**
	 * 
	 * @param callBackUrls
	 * @return
	 */
	private String getCallBackUrl(List<String> callBackUrls) {
		StringBuffer url = new StringBuffer(); 
		for (String string : callBackUrls) {
			url.append(string);
			if (StringUtils.isNotEmpty(string)) {
				url.append(";");
			}
		}
		int lastIndex = url.lastIndexOf(";");
		url.replace(lastIndex, lastIndex + 1, "");
		return url.toString();
	}
}
