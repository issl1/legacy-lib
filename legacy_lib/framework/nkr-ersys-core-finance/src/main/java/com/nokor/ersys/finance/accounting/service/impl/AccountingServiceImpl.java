package com.nokor.ersys.finance.accounting.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.finance.accounting.dao.AccountDao;
import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.model.MJournalEventAccount;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;
import com.nokor.ersys.finance.accounting.service.AccountCategoryRestriction;
import com.nokor.ersys.finance.accounting.service.AccountLedgerRestriction;
import com.nokor.ersys.finance.accounting.service.AccountRestriction;
import com.nokor.ersys.finance.accounting.service.AccountingService;
import com.nokor.ersys.finance.accounting.service.JournalEntryRestriction;
import com.nokor.ersys.finance.accounting.service.JournalEventAccountRestriction;
import com.nokor.ersys.finance.accounting.workflow.JournalEntryWkfStatus;

/**
 * @author bunlong.taing
 */
@Service("accountingService")
public class AccountingServiceImpl extends MainEntityServiceImpl implements AccountingService {
	/** */
	private static final long serialVersionUID = -2789216076211683451L;
	
	@Autowired
	private AccountDao dao;
	
	/**
	 */
	public AccountingServiceImpl() {
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#getDao()
	 */
	@Override
	public AccountDao getDao() {
		return dao;
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#createProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		super.createProcess(mainEntity);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#updateProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		super.updateProcess(mainEntity);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#deleteProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}

	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#listAccountCategoryByParent(com.nokor.ersys.finance.accounting.model.AccountCategory)
	 */
	@Override
	public List<AccountCategory> listAccountCategoryByParent(AccountCategory parent) {
		if (parent == null) {
			return null;
		}
		AccountCategoryRestriction restrictions = new AccountCategoryRestriction();
		restrictions.setParent(parent);
		return list(restrictions);
	}

	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#listAccountCategoryParentByRoot(com.nokor.ersys.finance.accounting.model.ECategoryRoot)
	 */
	@Override
	public List<AccountCategory> listAccountCategoryParentByRoot(ECategoryRoot root) {
		AccountCategoryRestriction restrictions = new AccountCategoryRestriction();
		restrictions.addCriterion(Restrictions.isNull(AccountCategory.PARENT));
		restrictions.setRoot(root);
		return list(restrictions);
	}

	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#listAccountByAccountCategory(com.nokor.ersys.finance.accounting.model.AccountCategory)
	 */
	@Override
	public List<Account> listAccountByAccountCategory(AccountCategory category) {
		AccountRestriction restrictions = new AccountRestriction();
		restrictions.setCategory(category);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#listJournalEventAccount(com.nokor.ersys.finance.accounting.model.JournalEvent)
	 */
	@Override
	public List<JournalEventAccount> listJournalEventAccount(JournalEvent event) {
		JournalEventAccountRestriction restrictions = new JournalEventAccountRestriction();
		restrictions.setEventId(event.getId());
		restrictions.setJournalId(event.getJournal().getId());
		restrictions.addOrder(Order.asc(MJournalEventAccount.SORTINDEX));
		List<JournalEventAccount> eventAccounts = list(restrictions);
		
		if (eventAccounts == null || eventAccounts.isEmpty()) {
			throw new IllegalStateException(I18N.message("error.no.account.mapping.found", event.getCode()));
		} else if (eventAccounts.size() < 2) {
			throw new IllegalStateException(I18N.message("error.mapping.violate.double.entry.accounting"));
		}
		
		return eventAccounts;
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#getLastAccountLedgerByAccount(com.nokor.ersys.finance.accounting.model.Account)
	 */
	@Override
	public AccountLedger getLastAccountLedgerByAccount(Account account) {
		AccountLedgerRestriction restrictions = new AccountLedgerRestriction();
		restrictions.setAccount(account);
		restrictions.addOrder(Order.desc(AccountLedger.CREATE_DATE_PROPERTY));
		return getFirst(restrictions);
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#getJournalEntryValidated()
	 */
	@Override
	public List<JournalEntry> getJournalEntryValidated() {
		return getJournalEntryValidated(null, null);
	}

	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#getJournalEntryValidated(java.util.Date, java.util.Date)
	 */
	@Override
	public List<JournalEntry> getJournalEntryValidated(Date startDate, Date endDate) {
		JournalEntryRestriction restrictions = new JournalEntryRestriction();
		restrictions.setWkfStatus(JournalEntryWkfStatus.VALIDATED);
		restrictions.setStartDate(startDate);
		restrictions.setEndDate(endDate);
		return list(restrictions);
	}

	/**
	 * Only VALIDATED JournalEntry can be sent to the Ledger
	 * 
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#postJournalEntryIntoLedger(com.nokor.ersys.finance.accounting.model.JournalEntry)
	 */
	@Override
	public List<AccountLedger> postJournalEntryIntoLedger(JournalEntry entry) {
		if 	(!JournalEntryWkfStatus.VALIDATED.equals(entry.getWkfStatus())) {
			throw new IllegalStateException(I18N.message("error.journalentry.post.into.ledger.status.not.validated", JournalEntryWkfStatus.VALIDATED.getCode()));
		}
		
		List<JournalEventAccount> eventAccounts = listJournalEventAccount(entry.getJournalEvent());
		
		List<AccountLedger> ledgers = new ArrayList<AccountLedger>();
		// Create AccountLedger
		for (JournalEventAccount eventAccount : eventAccounts) {
			AccountLedger ledger = buildAccountLedger(entry, eventAccount);
			dao.saveOrUpdate(ledger);
			ledgers.add(ledger);
		}
		
		entry.setWkfStatus(JournalEntryWkfStatus.POSTED);
		dao.update(entry);
		
		return ledgers;
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#postJournalEntriesIntoLedger(java.util.List)
	 */
	@Override
	public List<AccountLedger> postJournalEntriesIntoLedger(List<JournalEntry> entries) {
		List<AccountLedger> ledgers = new ArrayList<AccountLedger>();
		for (JournalEntry entry : entries) {
			ledgers.addAll(postJournalEntryIntoLedger(entry));
		}
		return ledgers;
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#reconcileJournalEntry(com.nokor.ersys.finance.accounting.model.JournalEntry)
	 */
	@Override
	public void reconcileJournalEntry(JournalEntry entry) {
		entry.setWkfStatus(JournalEntryWkfStatus.VALIDATED);
		dao.update(entry);
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#reconcileJournalEntries(java.util.List)
	 */
	@Override
	public void reconcileJournalEntries(List<JournalEntry> entries) {
		for (JournalEntry entry : entries) {
			reconcileJournalEntry(entry);
		}
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#cancelJournalEntry(com.nokor.ersys.finance.accounting.model.JournalEntry)
	 */
	@Override
	public void cancelJournalEntry(JournalEntry entry) {
		entry.setWkfStatus(JournalEntryWkfStatus.CANCELLED);
		dao.update(entry);
	}
	
	/**
	 * @see com.nokor.ersys.finance.accounting.service.AccountingService#cancelJournalEntries(java.util.List)
	 */
	@Override
	public void cancelJournalEntries(List<JournalEntry> entries) {
		for (JournalEntry entry : entries) {
			cancelJournalEntry(entry);
		}
	}
	
	/**
	 * @param entry
	 * @param eventAccount
	 * @return
	 */
	private AccountLedger buildAccountLedger(JournalEntry entry, JournalEventAccount eventAccount) {
		AccountLedger ledger = new AccountLedger();
		ledger.setJournalEntry(entry);
		ledger.setAccount(eventAccount.getAccount());
		ledger.setWhen(entry.getWhen());
		
		BigDecimal balance = new BigDecimal(0);
		AccountLedger lastLedger = getLastAccountLedgerByAccount(eventAccount.getAccount());
		if (lastLedger != null) {
			BigDecimal lastBalance = lastLedger.getBalance();
			balance = balance.add(lastBalance);
		}
		if (eventAccount.getIsDebit()) {
			ledger.setDebit(entry.getAmounts().get(eventAccount.getSortIndex() - 1));
			balance = balance.add(ledger.getDebit());
		} else {
			ledger.setCredit(entry.getAmounts().get(eventAccount.getSortIndex() - 1));
			balance = balance.subtract(ledger.getCredit());
		}
		ledger.setBalance(balance);
		
		return ledger;
	}

	/**
	 * @param transactionEntry
	 */
	public void updateTransactionEntry(TransactionEntry transactionEntry) {
		saveOrUpdate(transactionEntry);
		for (JournalEntry journalEntry : transactionEntry.getJournalEntries()) {
			journalEntry.setTransactionEntry(transactionEntry);
			saveOrUpdate(journalEntry);
		}
	}
	
	/**
	 * @param entries
	 * @return
	 */
	@Override
	public Map<JournalEntry, List<AccountLedger>> getLedgersGroupFromJournalEntries(List<JournalEntry> entries) {		
		List<AccountLedger> ledgers = new ArrayList<>();		
		for (JournalEntry entry : entries) {					
			List<JournalEventAccount> eventAccounts = listJournalEventAccount(entry.getJournalEvent());		
			// Create AccountLedger
			for (JournalEventAccount eventAccount : eventAccounts) {
				ledgers.add(buildAccountLedger(entry, eventAccount));
			}
		}
		return getLedgersGroupFromLegers(ledgers);
	}
	

	
	/**
	 * @param
	 * @return
	 */
	@Override
	public Map<JournalEntry, List<AccountLedger>> getLedgersGroupFromLegers(List<AccountLedger> ledgers) {
		Map<JournalEntry, List<AccountLedger>> ledgersGroupByEntry = new LinkedHashMap<JournalEntry, List<AccountLedger>>();		
		for (AccountLedger ledger : ledgers) {
			JournalEntry entry = ledger.getJournalEntry();
			if (!ledgersGroupByEntry.containsKey(entry)) {
				ledgersGroupByEntry.put(entry, new ArrayList<AccountLedger>());
			}
			ledgersGroupByEntry.get(entry).add(ledger);
		}		
		return ledgersGroupByEntry;
	}
}
