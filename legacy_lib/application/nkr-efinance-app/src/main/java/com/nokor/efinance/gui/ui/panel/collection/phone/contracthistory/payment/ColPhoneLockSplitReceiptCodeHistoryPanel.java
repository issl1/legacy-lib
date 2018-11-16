package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitReceiptCodeHistoryPanel extends AbstractControlPanel implements MPayment, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -6695469098599400764L;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitReceiptCodeHistoryPanel() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		setMargin(true);
		setSpacing(true);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(RECEIPTCODE, I18N.message("receipt.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<Payment> payments) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		Map<JournalEvent, Double> mapReceiptCodeAmounts = new HashMap<JournalEvent, Double>();
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				List<Cashflow> cashflows = payment.getCashflows();
				if (cashflows != null && !cashflows.isEmpty()) {
					for (Cashflow cashflow : cashflows) {
						JournalEvent journalEvent = cashflow.getJournalEvent();
						double cfwAmount = MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						if (journalEvent != null) {
							if (!mapReceiptCodeAmounts.containsKey(journalEvent)) {
								mapReceiptCodeAmounts.put(journalEvent, cfwAmount);
							} else {
								double oldAmount = mapReceiptCodeAmounts.get(journalEvent);
								oldAmount += cfwAmount;
								mapReceiptCodeAmounts.replace(journalEvent, oldAmount);
							}
						}
					}
				}
			}
		}
		if (!mapReceiptCodeAmounts.isEmpty()) {
			int index = 0;
			for (JournalEvent key : mapReceiptCodeAmounts.keySet()) {
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(RECEIPTCODE).setValue(key.getCode() + " - " + key.getDescLocale());
				item.getItemProperty(AMOUNT).setValue(MyNumberUtils.formatDoubleToString(Math.abs(mapReceiptCodeAmounts.get(key)), "###,##0.00"));
				index++;
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		setTableIndexedContainer(PAYMENT_SRV.getListPaymentsByContractID(contract.getId(), new EPaymentType[] {EPaymentType.IRC}));
	}
}
