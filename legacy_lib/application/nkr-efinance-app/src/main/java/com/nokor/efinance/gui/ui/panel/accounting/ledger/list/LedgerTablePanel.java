package com.nokor.efinance.gui.ui.panel.accounting.ledger.list;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.MJournalEntry;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;

/**
 * Ledger Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LedgerTablePanel extends AbstractTablePanel<AccountLedger> implements ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = 4175808625567170637L;
	
	private TreeTable ledgerTable;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("ledgers"));
		init(I18N.message("ledgers"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#init(java.lang.String)
	 */
	@Override
	protected void init(String caption) {
		searchPanel = createSearchPanel();
		if (searchPanel != null) {
			addComponent(searchPanel);
		}
		
		ledgerTable = createLedgerTable(caption);
		biuldTableColumnDefinition(ledgerTable);
		addComponent(ledgerTable);
	}
	
	/**
	 * @param caption
	 * @return
	 */
	private TreeTable createLedgerTable(String caption) {
		TreeTable table = new TreeTable(caption) {
			/** */
			private static final long serialVersionUID = 9052765495964629399L;
			
			/**
			 * @see com.vaadin.ui.Table#formatPropertyValue(java.lang.Object, java.lang.Object, com.vaadin.data.Property)
			 */
			@Override
			protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
				Object value = property.getValue();
				if (value instanceof Date) {
					Date dateValue = (Date) value;
		            return new SimpleDateFormat(DateUtils.FORMAT_DDMMYYYY_SLASH).format(dateValue);
				}
				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		table.setPageLength(20);
		table.setSelectable(true);
		table.setSizeFull();
		table.setImmediate(true);
		table.setColumnCollapsingAllowed(true);
		return table;
	}
	
	/**
	 * @param table
	 */
	private void biuldTableColumnDefinition(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.addContainerProperty(
					columnDefinition.getPropertyId(),
					columnDefinition.getPropertyType(),
					null,
					columnDefinition.getPropertyCaption(),
					null,
					columnDefinition.getPropertyAlignment());
			table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
		}
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(JournalEntry.DESCLOCALE, I18N.message("journal.entry"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.ID, I18N.message("id"), Long.class, Align.RIGHT, 50));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.ACCOUNT + "." + Account.NAME, I18N.message("account.name"), String.class, Align.LEFT, 300));
		columnDefinitions.add(new ColumnDefinition(MJournalEntry.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.DEBIT, I18N.message("debit"), BigDecimal.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.CREDIT, I18N.message("credit"), BigDecimal.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.BALANCE, I18N.message("balance"), BigDecimal.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(AccountLedger.WHEN, I18N.message("when"), Date.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		selectedItem = null;
		Map<JournalEntry, List<AccountLedger>> ledgersGroupByEntry =  getLedgersGroupByEntry();
		
		int index = 0;
		int parent = -1;
		ledgerTable.removeAllItems();
		for (JournalEntry entry : ledgersGroupByEntry.keySet()) {
			parent = index;
			Item item = ledgerTable.addItem(index++);
			ledgerTable.setCollapsed(parent, false);
			String entryCaption = entry.getDescLocale() != null ? entry.getDescLocale() : I18N.message("no.desc");
			item.getItemProperty(JournalEntry.DESCLOCALE).setValue(entryCaption);
			item.getItemProperty(AccountLedger.ID).setValue(entry.getId());
			item.getItemProperty(AccountLedger.WHEN).setValue(entry.getWhen());
			item.getItemProperty(JournalEntry.REFERENCE).setValue(entry.getReference());
			
			List<AccountLedger> ledgers = ledgersGroupByEntry.get(entry);
			BigDecimal totalDebit = new BigDecimal(0);
			BigDecimal totalCredit = new BigDecimal(0);
			for (AccountLedger ledger : ledgers) {
				Item ledgerItem = ledgerTable.addItem(index);
				ledgerTable.setParent(index, parent);
				Account account = ledger.getAccount();
				
				if (ledger.getDebit() != null) {
					totalDebit = totalDebit.add(ledger.getDebit());
				}
				if (ledger.getCredit() != null) {
					totalCredit = totalCredit.add(ledger.getCredit());
				}
				
				ledgerItem.getItemProperty(AccountLedger.ID).setValue(ledger.getId());
				ledgerItem.getItemProperty(AccountLedger.ACCOUNT + "." + Account.NAME).setValue(account != null ?  account.getCode() + " - " + account.getNameLocale() : "");
				ledgerItem.getItemProperty(AccountLedger.DEBIT).setValue(ledger.getDebit());
				ledgerItem.getItemProperty(AccountLedger.CREDIT).setValue(ledger.getCredit());
				ledgerItem.getItemProperty(AccountLedger.BALANCE).setValue(ledger.getBalance());
				ledgerItem.getItemProperty(AccountLedger.WHEN).setValue(ledger.getWhen());
				index++;
			}
			
			Item ledgerItem = ledgerTable.addItem(index);
			ledgerTable.setParent(index, parent);
			item.getItemProperty(AccountLedger.DEBIT).setValue(totalDebit);
			item.getItemProperty(AccountLedger.CREDIT).setValue(totalCredit);
			item.getItemProperty(AccountLedger.BALANCE).setValue(totalDebit.subtract(totalCredit));
		}
	}
	
	/**
	 * @return
	 */
	private Map<JournalEntry, List<AccountLedger>> getLedgersGroupByEntry() {	
		List<AccountLedger> ledgers = ACCOUNTING_SRV.list(searchPanel.getRestrictions());
		Map<JournalEntry, List<AccountLedger>> ledgersGroupByEntry = ACCOUNTING_SRV.getLedgersGroupFromLegers(ledgers);		
		return ledgersGroupByEntry;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AccountLedger getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ACCOUNTING_SRV.getById(AccountLedger.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected LedgerSearchPanel createSearchPanel() {
		return new LedgerSearchPanel(this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<AccountLedger> createPagedDataProvider() {
		return null;
	}

}
