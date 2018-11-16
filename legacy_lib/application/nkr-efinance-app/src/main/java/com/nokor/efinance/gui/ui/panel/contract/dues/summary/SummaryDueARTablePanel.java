package com.nokor.efinance.gui.ui.panel.contract.dues.summary;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.Summary;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author uhout.cheng
 */
public class SummaryDueARTablePanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 6818053323325572989L;
	
	private final static String ITEM = "item";
	private final static String AMOUNT = "amount";
	
	private SimpleTable<Entity> simpleTable;

	/**
	 * 
	 */
	public SummaryDueARTablePanel() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(10);
		simpleTable.setCaption(I18N.message("ar"));
		
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		setIndexedContainer(contract);
	}
	
	/**
	 * 
	 * @param contract
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(Contract contract) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		Summary summary = CONT_SRV.getContractSummary(contract);		
		for (int i = 0; i < 8; i++) {
			Item item = container.addItem(i);
			if (i == 0) {
				item.getItemProperty(ITEM).setValue(I18N.message("installment.outstanding"));
				item.getItemProperty(AMOUNT).setValue(summary.getBalanceInstallment().getTiAmount());
			} else if (i == 1) {
				item.getItemProperty(ITEM).setValue(I18N.message("installment.due.date"));
				item.getItemProperty(AMOUNT).setValue(summary.getDueInstallmentToDate().getTiAmount());
			} else if (i == 2) {
				item.getItemProperty(ITEM).setValue(I18N.message("installment.due.30.day"));
				item.getItemProperty(AMOUNT).setValue(summary.getDueInstallmentIn30Days().getTiAmount());
			} else if (i == 3) {
				item.getItemProperty(ITEM).setValue(I18N.message("penalty.other.fee.outstanding"));
				item.getItemProperty(AMOUNT).setValue(summary.getBalanceOthers().getTiAmount());
			} else if (i == 4) {
				item.getItemProperty(ITEM).setValue(I18N.message("total.due.date"));
				item.getItemProperty(AMOUNT).setValue(summary.getBalanceDueToDate().getTiAmount());
			} else if (i == 5) {
				item.getItemProperty(ITEM).setValue(I18N.message("total.outstanding"));
				item.getItemProperty(AMOUNT).setValue(summary.getBalanceInstallment().getTiAmount() + summary.getBalanceOthers().getTiAmount());
			} else if (i == 6) {
				item.getItemProperty(ITEM).setValue(I18N.message("advanced"));
				item.getItemProperty(AMOUNT).setValue(0d);
			} else if (i == 7) {
				item.getItemProperty(ITEM).setValue(I18N.message("net.outstanding"));
				item.getItemProperty(AMOUNT).setValue(summary.getBalanceInstallment().getTiAmount() + summary.getBalanceOthers().getTiAmount());
			} 
		}
	}
	
}
