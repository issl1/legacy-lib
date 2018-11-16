package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Past dues table in collection right panel
 * @author uhout.cheng
 */
public class CollectionPastDuesTablePanel extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 6829774548289332084L;

	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public CollectionPastDuesTablePanel() {
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setWidth(528, Unit.PIXELS);
		simpleTable.setPageLength(3);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(new VerticalLayout(simpleTable));
		setContent(horLayout);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DATE, I18N.message("due.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.TYPE, I18N.message("description"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PASTDUEAMOUNT, I18N.message("amount.past.due"), Double.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.STATUS, I18N.message("status"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param transactionVOs
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<TransactionVO> transactionVOs) {
		Container indexedContainer = simpleTable.getContainerDataSource();
		for (TransactionVO transaction : transactionVOs) {
			Item item = indexedContainer.addItem(transaction);
			item.getItemProperty(TransactionVO.DATE).setValue(transaction.getDate());
			item.getItemProperty(TransactionVO.TYPE).setValue(I18N.message("installment"));
			item.getItemProperty(TransactionVO.REFERENCE).setValue(transaction.getReference());
			item.getItemProperty(TransactionVO.PASTDUEAMOUNT).setValue(transaction.getPastDueAmount() != null ? transaction.getPastDueAmount().getTiAmount() : 0d);
			item.getItemProperty(TransactionVO.STATUS).setValue(transaction.getWkfStatus() != null ? transaction.getWkfStatus().getDescEn() : "");
			item.getItemProperty(TransactionVO.PAYMENTDATE).setValue(transaction.getPaymentDate());
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		simpleTable.removeAllItems();
		if (contract.getId() != null) {
			List<TransactionVO> transanctions = CASHFLOW_SRV.getDueTransactions(contract.getId(), null);
			setTableIndexedContainer(transanctions);
		}
	}

}
