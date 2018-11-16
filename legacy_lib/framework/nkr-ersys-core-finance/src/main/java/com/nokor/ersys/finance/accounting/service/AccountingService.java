package com.nokor.ersys.finance.accounting.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;

/**
 * Accounting Service
 * @author bunlong.taing
 */
public interface AccountingService extends MainEntityService {
	
	List<AccountCategory> listAccountCategoryByParent(AccountCategory parent);
	
	List<AccountCategory> listAccountCategoryParentByRoot(ECategoryRoot root);
	
	List<Account> listAccountByAccountCategory(AccountCategory category);
	
	List<JournalEventAccount> listJournalEventAccount(JournalEvent event);
	
	AccountLedger getLastAccountLedgerByAccount(Account account);
	
	List<JournalEntry> getJournalEntryValidated();
	
	List<JournalEntry> getJournalEntryValidated(Date startDate, Date endDate);
	
	List<AccountLedger> postJournalEntryIntoLedger(JournalEntry entry);
	
	List<AccountLedger> postJournalEntriesIntoLedger(List<JournalEntry> entries);

	void reconcileJournalEntry(JournalEntry entry);
	
	void reconcileJournalEntries(List<JournalEntry> entries);
	
	void cancelJournalEntry(JournalEntry entry);
	
	void cancelJournalEntries(List<JournalEntry> entries);
	
	void updateTransactionEntry(TransactionEntry transactionEntry);
	
	Map<JournalEntry, List<AccountLedger>> getLedgersGroupFromJournalEntries(List<JournalEntry> entries);
	
	Map<JournalEntry, List<AccountLedger>> getLedgersGroupFromLegers(List<AccountLedger> ledgers);

}
