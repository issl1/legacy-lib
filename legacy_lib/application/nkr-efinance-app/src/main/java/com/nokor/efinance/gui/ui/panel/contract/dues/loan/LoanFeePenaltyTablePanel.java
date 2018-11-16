package com.nokor.efinance.gui.ui.panel.contract.dues.loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO.Type;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;


/**
 * 
 * @author uhout.cheng
 */
public class LoanFeePenaltyTablePanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -8520300933046374086L;

	private static final String OPTIONS = "options";
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Button btnAdd;
	
	private Contract contract;

	/**
	 * 
	 */
	public LoanFeePenaltyTablePanel() {
		this.columnDefinitions = getColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setCaption(I18N.message("fees.penalties"));
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		addComponent(btnAdd);
		addComponent(simpleTable);
		
		setComponentAlignment(btnAdd, Alignment.BOTTOM_LEFT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(TransactionVO.ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DATE, I18N.message("due.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.TYPE, I18N.message("type"), Type.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIINSTALLMENTAMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIPAIDAMOUNT, I18N.message("amount.paid"), Double.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.DETAILS, I18N.message("details"), Label.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TransactionVO.STATUS, I18N.message("status"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(OPTIONS, StringUtils.EMPTY, HorizontalLayout.class, Align.CENTER, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param transactionVOs
	 */
	protected void assignValues(Contract contract) {
		this.contract = contract;
		setIndexedContainer(CASHFLOW_SRV.getFeePenaltyTransaction(contract.getId(), DateUtils.today()));
	}
	
	/**
	 * 
	 * @param mapCashflows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<TransactionVO> transactionVOs) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (transactionVOs != null && !transactionVOs.isEmpty()) {
			for (TransactionVO transaction : transactionVOs) {
				Item item = indexedContainer.addItem(transaction);
				
				Button btnUpdate = ComponentLayoutFactory.getDefaultButton("edit", null, 60);
				Button btnDelete = ComponentLayoutFactory.getDefaultButton("waive", null, 60);
				btnUpdate.setData(transaction.getCashflowId());
				btnDelete.setData(transaction.getCashflowId());
				
				item.getItemProperty(TransactionVO.ID).setValue(transaction.getCashflowId());
				item.getItemProperty(TransactionVO.DATE).setValue(transaction.getDate());
				item.getItemProperty(TransactionVO.TYPE).setValue(transaction.getType());
				item.getItemProperty(Cashflow.TIINSTALLMENTAMOUNT).setValue(transaction.getPaidAmount() != null ?
						transaction.getBalanceAmount().getTiAmount() : 0d);
				item.getItemProperty(Cashflow.TIPAIDAMOUNT).setValue(transaction.getPaidAmount() != null ?
						transaction.getPaidAmount().getTiAmount() : 0d);
				item.getItemProperty(TransactionVO.DETAILS).setValue(ComponentFactory.getLabel("Fee for " + transaction.getReference()));
				item.getItemProperty(TransactionVO.PAYMENTDATE).setValue(transaction.getPaymentDate());
				item.getItemProperty(TransactionVO.STATUS).setValue(transaction.getWkfStatus() != null ? transaction.getWkfStatus().getDescEn() : "");
				item.getItemProperty(OPTIONS).setValue(getButtonLayouts(btnUpdate, btnDelete));
				
				btnUpdate.addClickListener(new ClickListener() {

					/** */
					private static final long serialVersionUID = 2780227583706666673L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnUpdate.getData();
						Cashflow cashflow = CASHFLOW_SRV.getById(Cashflow.class, id);
						UI.getCurrent().addWindow(new EditFeeWindowFormPanel(LoanFeePenaltyTablePanel.this, cashflow, false));
					}
				});
				
				btnDelete.addClickListener(new ClickListener() {

					/** */
					private static final long serialVersionUID = 6242930996647197406L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnDelete.getData();
						Cashflow cashflow = CASHFLOW_SRV.getById(Cashflow.class, id);
						UI.getCurrent().addWindow(new EditFeeWindowFormPanel(LoanFeePenaltyTablePanel.this, cashflow, true));
					}
				});
			}
		}
	}
	
	/**
	 * 
	 * @param btnUpdate
	 * @param btnDelete
	 * @return
	 */
	private HorizontalLayout getButtonLayouts(Button btnUpdate, Button btnDelete) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(btnUpdate);
		layout.addComponent(btnDelete);
		return layout;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			UI.getCurrent().addWindow(new FeeWindowFormPanel(this, this.contract));
		}
	}
}
