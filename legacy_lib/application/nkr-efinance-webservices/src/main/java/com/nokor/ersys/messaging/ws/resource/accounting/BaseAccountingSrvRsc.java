package com.nokor.ersys.messaging.ws.resource.accounting;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.messaging.ws.resource.BaseWsMessage;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.messaging.share.accounting.AccountDTO;
import com.nokor.ersys.messaging.share.accounting.AccountLedgerDTO;
import com.nokor.ersys.messaging.share.accounting.JournalDTO;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.ersys.messaging.share.accounting.JournalEventAccountDTO;
import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;
import com.nokor.frmk.messaging.ws.rest.BaseResource;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseAccountingSrvRsc extends BaseResource implements ErsysAccountingAppServicesHelper {
	protected List<BaseWsMessage> messages = new ArrayList<>();

	
	
	/**
	 * 
	 * @param acc
	 * @return
	 */
	protected AccountDTO toAccountDTO(Account acc) {
		AccountDTO accDTO = new AccountDTO();
		accDTO.setId(acc.getId());
		accDTO.setCode(acc.getCode());
		accDTO.setDesc(acc.getName());
		accDTO.setDescEn(acc.getNameEn());
		
		return accDTO;
	}
	

	/**
	 * 
	 * @param accounts
	 * @return
	 */
	protected List<AccountDTO> toAccountDTOs(List<Account> accounts) {
		List<AccountDTO> dtoLst = new ArrayList<>();
		for (Account acc : accounts) {
			dtoLst.add(toAccountDTO(acc));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param journals
	 * @return
	 */
	protected List<JournalDTO > toJournalDTOs(List<Journal> journals) {
		List<JournalDTO> dtoLst = new ArrayList<>();
		for (Journal jour : journals) {
			dtoLst.add(toJournalDTO(jour));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param jourDTO
	 * @return
	 */
	protected Journal toJournal(JournalDTO jourDTO) {
		Journal jour;
		if (jourDTO.getId() != null) {
			jour = ENTITY_SRV.getById(Journal.class, jourDTO.getId());
			if (jour == null) {
				messages.add(AccountingWsMessage.JOURNAL_NOT_FOUND);
				return null;
			}
		} else {
			jour = new Journal();
			jour.setId(jourDTO.getId());
			jour.setCode(jourDTO.getCode());
			jour.setDesc(jourDTO.getDesc());
			jour.setDescEn(jourDTO.getDescEn());
		}
		return jour;
	}
	
	/**
	 * 
	 * @param journal
	 * @return
	 */
	protected JournalDTO toJournalDTO(Journal journal) {
		JournalDTO jourDTO = new JournalDTO();
		jourDTO.setId(journal.getId());
		jourDTO.setCode(journal.getCode());
		jourDTO.setDesc(journal.getDesc());
		jourDTO.setDescEn(journal.getDescEn());
		
		return jourDTO;
	}
	
	/**
	 * 
	 * @param eventAccount
	 * @return
	 */
	protected JournalEventAccountDTO toJournalEventAccountDTO(JournalEventAccount eventAccount) {
		JournalEventAccountDTO evAccDTO = new JournalEventAccountDTO();
		evAccDTO.setId(eventAccount.getId());
		evAccDTO.setEventId(eventAccount.getEvent().getId());
		evAccDTO.setAccountId(eventAccount.getAccount().getId());
		evAccDTO.setIsDebit(eventAccount.getIsDebit());
		
		return evAccDTO;
	}
	
	/**
	 * 
	 * @param eventAccountDTO
	 * @return
	 */
	protected JournalEventAccount toJournalEventAccount(JournalEventAccountDTO eventAccountDTO) {
		JournalEventAccount evAcc = new JournalEventAccount();
		evAcc.setId(eventAccountDTO.getId());
		if (eventAccountDTO.getEventId() == null) {
			messages.add(AccountingWsMessage.EVENT_MANDATORY);
		} else {
			JournalEvent event = ENTITY_SRV.getById(JournalEvent.class, eventAccountDTO.getEventId());
			if (event == null) {
				messages.add(AccountingWsMessage.EVENT_MANDATORY);
			}
			evAcc.setEvent(event);
		}
		if (eventAccountDTO.getAccountId() == null) {
			messages.add(AccountingWsMessage.ACCOUNT_MANDATORY);
		} else {
			Account acc = ENTITY_SRV.getById(Account.class, eventAccountDTO.getAccountId());
			if (acc == null) {
				messages.add(AccountingWsMessage.ACCOUNT_NOT_FOUND);
			}
			evAcc.setAccount(acc);
		}
		evAcc.setIsDebit(eventAccountDTO.getIsDebit());
		
		return evAcc;
	}
	
	/**
	 * 
	 * @param eventAccounts
	 * @return
	 */
	protected List<JournalEventAccountDTO> toJournalEventAccountDTOs(List<JournalEventAccount> eventAccounts) {
		List<JournalEventAccountDTO> dtoLst = new ArrayList<>();
		for (JournalEventAccount evAcc : eventAccounts) {
			dtoLst.add(toJournalEventAccountDTO(evAcc));
		}
		return dtoLst;
	}
	
	
	/**
	 * 
	 * @param accDTO
	 * @return
	 */
	protected Account toAccount(AccountDTO accDTO, Long id) {
		Account acc = null;
		if (id != null) {
			acc = ENTITY_SRV.getById(Account.class, id);
			if (acc == null) {
				messages.add(AccountingWsMessage.ACCOUNT_NOT_FOUND);
				return null;
			}
		} else {
//			acc = Account.createInstance();
			
		}
		return acc;
	}
	

	/**
	 * 
	 * @param entries
	 * @return
	 */
	protected List<JournalEntryDTO> toJournalEntryDTOs(List<JournalEntry> entries) {
		List<JournalEntryDTO> dtoLst = new ArrayList<>();
		for (JournalEntry entry : entries) {
			dtoLst.add(toJournalEntryDTO(entry));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param entry
	 * @return
	 */
	protected JournalEntryDTO toJournalEntryDTO(JournalEntry entry) {
		JournalEntryDTO entryDTO = new JournalEntryDTO();
	
		entryDTO.setId(entry.getId());
		if (entry.getOrganization() != null) {
			entryDTO.setOrganizationId(entry.getOrganization().getId());
		}
		entryDTO.setDesc(entry.getDesc());
		entryDTO.setDescEn(entry.getDescEn());
		entryDTO.setReference(entry.getReference());
		entryDTO.setJournalEventCode(entry.getJournalEvent().getCode());
		entryDTO.setInfo(entry.getInfo());
		entryDTO.setOtherInfo(entry.getOtherInfo());
		entryDTO.setWhen(entry.getWhen());				
		entryDTO.setAmounts(entry.getAmounts());
		entryDTO.setWkfStatusCode(entry.getWkfStatus().getCode());
		
		return entryDTO;
	}
	
	/**
	 * 
	 * @param events
	 * @return
	 */
	protected List<JournalEventDTO> toJournalEventDTOs(List<JournalEvent> events) {
		List<JournalEventDTO> dtoLst = new ArrayList<>();
		for (JournalEvent contract : events) {
			dtoLst.add(toJournalEventDTO(contract));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	protected JournalEventDTO toJournalEventDTO(JournalEvent event) {
		JournalEventDTO eventDTO = new JournalEventDTO();
	
		eventDTO.setId(event.getId());
		eventDTO.setCode(event.getCode());
		eventDTO.setDesc(event.getDesc());
		eventDTO.setDescEn(event.getDescEn());
		eventDTO.setSortIndex(event.getSortIndex());
		eventDTO.setIsActive(event.isActive());
		eventDTO.setEventGroupId(event.getEventGroup() != null ? event.getEventGroup().getId() : null);
		
		return eventDTO;
	}
	
	/**
	 * 
	 * @param eventDTO
	 * @return
	 */
	protected JournalEvent toJournalEvent(JournalEventDTO eventDTO) {
		JournalEvent event = new JournalEvent();
	
		if (eventDTO.getJournalId() == null) {
			messages.add(AccountingWsMessage.JOURNAL_ID_MANDATORY);
		} else {
			Journal journal = ACCOUNTING_SRV.getById(Journal.class, eventDTO.getJournalId());
			if (journal == null) {
				messages.add(AccountingWsMessage.JOURNAL_NOT_FOUND);
			}
			event.setJournal(journal);
		}
		if (eventDTO.getEventGroupId() != null) {
			EJournalEventGroup eventGroup = EJournalEventGroup.getById(eventDTO.getEventGroupId());
			if (eventGroup == null) {
				messages.add(AccountingWsMessage.JOURNAL_EVENT_GROUP_NOT_FOUND);
			}
			event.setEventGroup(eventGroup);
		}
		
		event.setCode(eventDTO.getCode());
		event.setDesc(eventDTO.getDesc());
		event.setDescEn(eventDTO.getDescEn());
		event.setSortIndex(eventDTO.getSortIndex());
		event.setStatusRecord(eventDTO.getIsActive());
		
		return event;
	}
	
	
	/**
	 * @param entryDTO
	 * @param id
	 * @return
	 */
	protected JournalEntry toJournalEntry(JournalEntryDTO entryDTO) {
		JournalEntry journalEntry = new JournalEntry();

		// Validate
		if (entryDTO.getAmounts() == null || entryDTO.getAmounts().isEmpty()) {
			messages.add(AccountingWsMessage.AMOUNT_MANDATORY);
		}
		if (StringUtils.isEmpty(entryDTO.getJournalEventCode())) {
			messages.add(AccountingWsMessage.JOURNAL_EVENT_CODE_MANDATORY);
		} else {
			JournalEvent event = ENTITY_SRV.getByCode(JournalEvent.class, entryDTO.getJournalEventCode());
			if (event == null) {
				messages.add(AccountingWsMessage.JOURNAL_EVENT_NOT_FOUND);
			}
			journalEntry.setJournalEvent(event);
		}
		Organization org = null;
		if (entryDTO.getOrganizationId() != null) {
			messages.add(AccountingWsMessage.ORGANIZATION_MANDATORY);
			org = ENTITY_SRV.getById(Organization.class, entryDTO.getOrganizationId());
			if (org == null) {
				messages.add(AccountingWsMessage.ORGANIZATION_NOT_FOUND);
			}
		} else {
			org = Organization.getMainOrganization();
		}
		journalEntry.setOrganization(org);
		
		journalEntry.setDesc(entryDTO.getDesc());
		journalEntry.setDescEn(entryDTO.getDescEn());
		journalEntry.setReference(entryDTO.getReference());
		journalEntry.setInfo(entryDTO.getInfo());
		journalEntry.setOtherInfo(entryDTO.getOtherInfo());
		journalEntry.setWhen(entryDTO.getWhen() == null ? DateUtils.today() : entryDTO.getWhen());
		for (int i = 0; i < entryDTO.getAmounts().size(); i++) {
			if (i == 0) {
				journalEntry.setAmount(entryDTO.getAmounts().get(i));
			} else if (i == 1) {
				journalEntry.setAmount2(entryDTO.getAmounts().get(i));
			} else if (i == 2) {
				journalEntry.setAmount3(entryDTO.getAmounts().get(i));
			} else if (i == 3) {
				journalEntry.setAmount4(entryDTO.getAmounts().get(i));
			} else if (i == 4) {
				journalEntry.setAmount5(entryDTO.getAmounts().get(i));
			} else if (i == 5) {
				journalEntry.setAmount6(entryDTO.getAmounts().get(i));
			} else if (i == 6) {
				journalEntry.setAmount7(entryDTO.getAmounts().get(i));
			} else if (i == 7) {
				journalEntry.setAmount8(entryDTO.getAmounts().get(i));
			} else if (i == 8) {
				journalEntry.setAmount9(entryDTO.getAmounts().get(i));
			}
		}
		
		return journalEntry;
	}

	/**
	 * To AccountLedgerDTO
	 * @param accountLedger
	 * @return
	 */
	protected AccountLedgerDTO toAccountLedgerDTO(AccountLedger accountLedger) {
		AccountLedgerDTO accountLedgerDTO = new AccountLedgerDTO();
		
		accountLedgerDTO.setId(accountLedger.getId());
		accountLedgerDTO.setJournalEntryId(accountLedger.getJournalEntry().getId());
		accountLedgerDTO.setAccountId(accountLedger.getAccount().getId());
		accountLedgerDTO.setDebit(accountLedger.getDebit());
		accountLedgerDTO.setCredit(accountLedger.getCredit());
		accountLedgerDTO.setBalance(accountLedger.getBalance());
		accountLedgerDTO.setWhen(accountLedger.getWhen());
		
		return accountLedgerDTO;
	}
	
	/**
	 * To List of AccountLedgerDTOs
	 * @param accountLedgers
	 * @return
	 */
	protected List<AccountLedgerDTO> toAccountLedgerDTOs(List<AccountLedger> accountLedgers) {
		List<AccountLedgerDTO> accountLedgerDTOs = new ArrayList<>();
		for (AccountLedger accountLedger : accountLedgers) {
			accountLedgerDTOs.add(toAccountLedgerDTO(accountLedger));
		}
		return accountLedgerDTOs;
	}

}
