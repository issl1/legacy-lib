package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.transactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * Transaction panel in collection phone
 * @author uhout.cheng
 */
public class ColPhoneTransactionPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 3011579201537569396L;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ColPhoneTransactionPanel() {
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeFull();
		simpleTable.setPageLength(10);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		setMargin(true);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Payment.RECEIPTGROUP, I18N.message("receipt.group"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(Payment.RECEIPTCODE, I18N.message("receipt.code"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(Payment.REFERENCE, I18N.message("receipt.id"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTTYPE, I18N.message("desc"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTDATE, I18N.message("date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTMETHOD, I18N.message("payment.method"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), Amount.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(Payment.VATPAIDAMOUNT, I18N.message("amount.vat"), Amount.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		setTableIndexedContainer(PAYMENT_SRV.getListPaymentsByContractID(contract.getId(), new EPaymentType[] {EPaymentType.IRC}));
	}
	
	/**
	 * 
	 * @param payments
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<Payment> payments) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				if (payment != null && !EPaymentType.ORC.equals(payment.getPaymentType())) {
					Item item = indexedContainer.addItem(payment.getId());
					item.getItemProperty(Payment.RECEIPTGROUP).setValue("");
					item.getItemProperty(Payment.RECEIPTCODE).setValue("");
					item.getItemProperty(Payment.REFERENCE).setValue(payment.getReference());
					item.getItemProperty(Payment.PAYMENTTYPE).setValue(payment.getPaymentType().getDescLocale());
					item.getItemProperty(Payment.PAYMENTDATE).setValue(payment.getPaymentDate());
					item.getItemProperty(Payment.PAYMENTMETHOD).setValue(payment.getPaymentMethod().getDescLocale());
					item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
					item.getItemProperty(Payment.VATPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getVatPaidAmount())));
				}
			}
		}
	}
}
