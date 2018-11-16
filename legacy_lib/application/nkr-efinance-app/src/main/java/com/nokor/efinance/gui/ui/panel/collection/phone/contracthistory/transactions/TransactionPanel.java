package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.transactions;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.Transaction;
import com.nokor.efinance.core.contract.model.TransactionItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;


/**
 * @author uhout.cheng
 */
public class TransactionPanel extends AbstractControlPanel implements MCollection, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 4974175241513182306L;
	
	private TreeTable treeTable;
	
	
	/**
	 * 
	 */	
	public TransactionPanel() {
		setMargin(true);
		setSpacing(true);
		
		treeTable = new TreeTable();
		treeTable.setPageLength(10);
		treeTable.setSelectable(true);
		treeTable.setSizeFull();
		treeTable.setImmediate(true);
		treeTable.setColumnCollapsingAllowed(true);
		treeTable.setCaption(I18N.message("roadmap"));
		
		setUpColumnDefinitions(treeTable);
		addComponent(treeTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(BALANCE, I18N.message("balance"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 * @param parent
	 * @param index
	 */
	private void setIndexedContainer(Contract contract, int parent, int index) {
		List<TransactionVO> transactionVOs = CASHFLOW_SRV.getDueTransactions(contract.getId(), null);
		List<Transaction> transactions = null;
		if (transactionVOs != null && !transactionVOs.isEmpty()) {
			transactions = CONT_SRV.getTransaction(transactionVOs);
		}
		if (transactions != null && !transactions.isEmpty()) {
			int subParent = parent;
			for (Transaction transaction : transactions) {
				subParent = renderParentRow(DateUtils.getDateLabel(transaction.getTransactionDate(), "dd.MM.yyyy"), parent, index, transaction);
				index++;
				if (transaction.getItems() != null) {
					for (TransactionItem transactionItem : transaction.getItems()) {
						Item item = treeTable.addItem(index);
						treeTable.setParent(index, subParent);
						treeTable.setCollapsed(subParent, true);
						treeTable.setChildrenAllowed(index, false);
						index = renderRow(item, index, transactionItem);
						index++;
					}
				} else {
					treeTable.setChildrenAllowed(subParent, false);
				}	
				parent = index;
			}
		}
	}
	
	/**
	 * 
	 * @param item
	 * @param index
	 * @param lockSplitRecapVO
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderRow(Item item, int index, TransactionItem transactionItem) {
		if (transactionItem != null) {
			item.getItemProperty(DESC).setValue(transactionItem.getDescription());
			item.getItemProperty(AMOUNT).setValue(AmountUtils.format(transactionItem.getAmount()));
			item.getItemProperty(BALANCE).setValue(AmountUtils.format(transactionItem.getBalance()));
		}
		return index;
	}
	
	@SuppressWarnings("unchecked")
	private void setIndexLoanOrganization(Cashflow cashflow) {
		if (cashflow != null) {
			Item item = treeTable.addItem(0);
			item.getItemProperty(DATE).setValue(DateUtils.getDateLabel(cashflow.getInstallmentDate(), "dd.MM.yyyy"));
			item.getItemProperty(ID).setValue(I18N.message("none"));
			item.getItemProperty(DESC).setValue(I18N.message("loan.organization"));
			item.getItemProperty(AMOUNT).setValue(AmountUtils.format(cashflow.getTiInstallmentAmount()));
			item.getItemProperty(BALANCE).setValue(AmountUtils.format(cashflow.getTiInstallmentAmount()));
			item.getItemProperty(STATUS).setValue(cashflow.getPayment() != null ? I18N.message("paid") : I18N.message("unpaid"));
		}
	}
	
	
	/**
	 * 
	 * @param key
	 * @param parent
	 * @param index
	 * @param totalAmount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, int parent, int index, Transaction transaction) {
		Item item = treeTable.addItem(index);
		
		treeTable.setParent(index, parent);
		treeTable.setCollapsed(parent, true);
		
		item.getItemProperty(DATE).setValue(key);
		item.getItemProperty(ID).setValue(transaction.getTransactionId());
		item.getItemProperty(DESC).setValue(transaction.getDescription());
		item.getItemProperty(AMOUNT).setValue(AmountUtils.format(transaction.getAmount()));
		item.getItemProperty(BALANCE).setValue(AmountUtils.format(transaction.getBalance()));
		item.getItemProperty(STATUS).setValue(transaction.getWkfStatus().getDescLocale());
		return index;
	}
	
	
	/**
	 * Set Up Column Definitions
	 * @param table
	 */
	private void setUpColumnDefinitions(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.removeContainerProperty(columnDefinition.getPropertyId());
		}
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
	 * AssignValue
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		treeTable.removeAllItems();
		setIndexLoanOrganization(CASHFLOW_SRV.getLoanOrganization(contract));
		setIndexedContainer(contract, 1, 1);
		
	}
}
