package com.nokor.efinance.gui.ui.panel.contract.dues.loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author uhout.cheng
 */
public class LoanInstallmentTablePanel extends AbstractControlPanel implements MCollection {

	/** */
	private static final long serialVersionUID = 7182398161517294949L;

	private SimplePagedTable<Entity> simplePagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Contract contract;

	/**
	 * 
	 */
	public LoanInstallmentTablePanel() {
		this.columnDefinitions = getColumnDefinitions();
		simplePagedTable = new SimplePagedTable<Entity>(this.columnDefinitions);
		simplePagedTable.setCaption(I18N.message("installments"));
		
		setSpacing(true);
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DUEDATE, I18N.message("due.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), String.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIINSTALLMENTAMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIPAIDAMOUNT, I18N.message("amount.paid"), Double.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DETAILS, I18N.message("details"), Label.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), Double.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.STATUS, I18N.message("status"), String.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param transactionVOs
	 */
	public void assignValues(List<TransactionVO> transactionVOs, Contract contract) {
		this.contract = contract;
		simplePagedTable.setContainerDataSource(getIndexedContainer(transactionVOs));
	}
	
	/**
	 * 
	 * @param mapCashflows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<TransactionVO> transactionVOs) {
		String FORMAT = "###,##0.00";
		simplePagedTable.removeAllItems();
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (transactionVOs != null && !transactionVOs.isEmpty()) {
			for (TransactionVO transaction : transactionVOs) {
				Item item = indexedContainer.addItem(transaction);
				item.getItemProperty(DUEDATE).setValue(transaction.getDate());
				item.getItemProperty(ID).setValue(transaction.getReference());
				item.getItemProperty(Cashflow.TIINSTALLMENTAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
						transaction.getDueAmount(), FORMAT));
				item.getItemProperty(Cashflow.TIPAIDAMOUNT).setValue(transaction.getPaidAmount() != null ?
						transaction.getPaidAmount().getTiAmount() : 0d);
				item.getItemProperty(TransactionVO.DETAILS).setValue(ComponentFactory.getHtmlLabel(transaction.getDueDetails()));
				if (contract != null) {
					Collection collection = contract.getCollection();
					item.getItemProperty(APD).setValue(collection != null ? collection.getTiTotalAmountInOverdue() : 0d);
				}
				item.getItemProperty(DPD).setValue(transaction.getNbOverdueDay());
				item.getItemProperty(TransactionVO.PAYMENTDATE).setValue(transaction.getPaymentDate());
				item.getItemProperty(TransactionVO.STATUS).setValue(transaction.getWkfStatus() != null ? transaction.getWkfStatus().getDescEn() : "");
			}
		}
		return indexedContainer;
	}
	
}
