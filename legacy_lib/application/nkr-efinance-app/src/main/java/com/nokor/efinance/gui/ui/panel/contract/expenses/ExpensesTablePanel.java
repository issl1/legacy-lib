package com.nokor.efinance.gui.ui.panel.contract.expenses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractPromise;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;


/**
 * 
 * @author uhout.cheng
 */
public class ExpensesTablePanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = 2730066911648428684L;

	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Item selectedItem;
	
	private Button btnAdd;
	
	private Contract contract;

	/**
	 * 
	 */
	public ExpensesTablePanel() {
		this.columnDefinitions = getColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setCaption(I18N.message("histories"));
		simpleTable.addItemClickListener(this);
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		setMargin(true);
		addComponent(btnAdd);
		addComponent(simpleTable);
		
		setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Payment.ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(Payment.CREATEDATE, I18N.message("requested.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(Payment.RECEIPTCODE, I18N.message("payment.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.DESCEN, I18N.message("desc"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTMETHOD, I18N.message("payment.method"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(Payment.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.FULLNAME, I18N.message("payee.fullname"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.STATUS, I18N.message("payment.status"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.ACTIONS, StringUtils.EMPTY, HorizontalLayout.class, Align.CENTER, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		this.contract = contract;
		PaymentRestriction restrictions = new PaymentRestriction();
		restrictions.setContractId(contract.getId());
		restrictions.setPaymentTypes(new EPaymentType[] { EPaymentType.DCO });
		setIndexedContainer(PAYMENT_SRV.list(restrictions));
	}
	
	/**
	 * 
	 * @param mapCashflows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Payment> payments) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				Item item = indexedContainer.addItem(payment.getId());
				
				Button btnUpdate = ComponentLayoutFactory.getDefaultButton("edit", FontAwesome.EDIT, 70);
				Button btnDelete = ComponentLayoutFactory.getDefaultButton("delete", FontAwesome.TRASH_O, 70);
				btnUpdate.setData(payment.getId());
				btnDelete.setData(payment.getId());
				
				item.getItemProperty(ContractPromise.ID).setValue(payment.getId());
				item.getItemProperty(ContractPromise.CREATEDATE).setValue(payment.getCreateDate());
				item.getItemProperty(Payment.RECEIPTCODE).setValue(payment.getPaymentType().getCode());
				item.getItemProperty(Payment.DESCEN).setValue(payment.getDescEn());
				item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
				item.getItemProperty(Payment.PAYMENTMETHOD).setValue(payment.getPaymentMethod().getDescLocale());
				item.getItemProperty(Payment.REFERENCE).setValue(payment.getReference());
				// TODO payee full name
				item.getItemProperty(Payment.FULLNAME).setValue(StringUtils.EMPTY);
				item.getItemProperty(Payment.PAYMENTDATE).setValue(payment.getPaymentDate());
				item.getItemProperty(Payment.STATUS).setValue(payment.getWkfStatus().getDescLocale());
				item.getItemProperty(Payment.ACTIONS).setValue(getButtonLayouts(btnUpdate, btnDelete));
				
				btnUpdate.addClickListener(new ClickListener() {
				
					/** */
					private static final long serialVersionUID = -2092901211741491156L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnUpdate.getData();
						Payment payment = ENTITY_SRV.getById(Payment.class, id);
						UI.getCurrent().addWindow(new ExpensesWindowFormPanel(ExpensesTablePanel.this, payment));
					}
				});
				
				btnDelete.addClickListener(new ClickListener() {

					/** */
					private static final long serialVersionUID = -6994585149308456922L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnDelete.getData();
						ContractPromise promise = ENTITY_SRV.getById(ContractPromise.class, id);
						ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
								new String[] {promise.getId().toString()}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = -7498331469373375409L;

							/**
							 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
							 */
							@Override
							public void onClose(ConfirmDialog dialog) {
								
							}
						});
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
			Payment payment = new Payment();
			payment.setContract(this.contract);
			UI.getCurrent().addWindow(new ExpensesWindowFormPanel(this, payment));
		}
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			if (getItemSelectedId() != null) {
				Payment payment = ENTITY_SRV.getById(Payment.class, getItemSelectedId());
				UI.getCurrent().addWindow(new ExpensesWindowFormPanel(ExpensesTablePanel.this, payment));
			}
		}
	}
		
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ContractPromise.ID).getValue());
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
}
