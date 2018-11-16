package com.nokor.efinance.gui.ui.panel.collection.delinquencies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 *
 */
public class DelinquenciesPanel extends AbstractControlPanel implements MCollection, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6556341407237390871L;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Cashflow> simplePagedTable;
	
	private Label lblPaymentPatternValue;
	
	public DelinquenciesPanel() {
		setSpacing(true);
		
		lblPaymentPatternValue = new Label();
		Label lblPaymentPattern = getLabel("payment.pattern");
		HorizontalLayout paymentPatternLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		paymentPatternLayout.addComponent(lblPaymentPattern);
		paymentPatternLayout.addComponent(lblPaymentPatternValue);
		
		this.columnDefinitions = createdefinitions();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
		
		addComponent(paymentPatternLayout);
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
	}
	

	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createdefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(DUEDATE, I18N.message("due.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DUEAMOUNT, I18N.message("due.amount"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PERCENTAGAMOUNT, I18N.message("percentag.amount"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 90));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<TransactionVO> transactionVOs) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		if (transactionVOs != null && !transactionVOs.isEmpty()) {
			int index = 0;
			for (TransactionVO transactionVO : transactionVOs) {
				Item item = indexedContainer.addItem(index++);
				item.getItemProperty(ITEM).setValue(I18N.message("installment"));
				item.getItemProperty(ID).setValue("#ins" + transactionVO.getNumInstallment());
				item.getItemProperty(DUEDATE).setValue(transactionVO.getDate());
				item.getItemProperty(DUEAMOUNT).setValue(AmountUtils.format(transactionVO.getDueAmount()));
				item.getItemProperty(APD).setValue(AmountUtils.format(transactionVO.getPastDueAmount().getTiAmount()));
				double percentageAmount = 0;
				if (transactionVO.getPastDueAmount().getTiAmount() > 0) {
					percentageAmount = (100 * transactionVO.getPastDueAmount().getTiAmount()) / transactionVO.getDueAmount();
				}
				item.getItemProperty(PERCENTAGAMOUNT).setValue(AmountUtils.format(percentageAmount));
				item.getItemProperty(DPD).setValue(transactionVO.getNbOverdueDay());
				item.getItemProperty(STATUS).setValue(transactionVO.getWkfStatus().getDescEn());
			}
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		simplePagedTable.setContainerDataSource(getIndexedContainer(CASHFLOW_SRV.getDueTransactions(contract.getId(), DateUtils.today())));
	}
}
