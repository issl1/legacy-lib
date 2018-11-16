package com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.in;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * In Transaction Panel
 * @author bunlong.taing
 */
public class InTransactionPanel extends AbstractControlPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 4875831867306459568L;
	
	private static final String ACTIONS = "actions";
	
	private SimplePagedTable<Entity> table;
	private InTransactionPopupPanel inPopupPanel;
	
	/**
	 */
	public InTransactionPanel() {
		setMargin(true);
		setSpacing(true);
		inPopupPanel = new InTransactionPopupPanel();
		table = new SimplePagedTable<Entity>(createColumnDefinitions());
		addComponent(table);
		addComponent(table.createControls());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		// TODO Payment Group
		columnDefinitions.add(new ColumnDefinition("receipt.group", I18N.message("receipt.group"), String.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(Payment.REFERENCE, I18N.message("receipt.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.ID, I18N.message("receipt.id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(Payment.DESCEN, I18N.message("desc"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTDATE, I18N.message("date"), Date.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTMETHOD, I18N.message("method.of.payment"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.VATPAIDAMOUNT, I18N.message("vat.amount"), Double.class, Align.RIGHT, 100));
		
		// TODO ref due
		columnDefinitions.add(new ColumnDefinition("ref.due", I18N.message("ref.due"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), HorizontalLayout.class, Align.CENTER, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param contraId
	 */
	public void assignValues(Long contraId) {
		reset();
		if (contraId != null) {
			PaymentRestriction restriction = new PaymentRestriction();
			restriction.setContractId(contraId);
			restriction.setPaymentTypes(new EPaymentType[] {EPaymentType.IRC});
			List<Payment> payments = PAYMENT_SRV.list(restriction);
			setTableIndexedContainer(payments);
		}
	}
	
	/**
	 * @param payments
	 */
	private void setTableIndexedContainer(List<Payment> payments) {
		Container indexedContainer = table.getContainerDataSource();
		int index = 0;
		for (Payment payment : payments) {
			
			List<Cashflow> cashflows = payment.getCashflows();
			Collections.sort(cashflows, new CashflowComparatorByNumInstallment());
			Map<Integer, List<Cashflow>> mapCashflow = groupCashflowByNumInstallment(cashflows);
			
			for (Integer numInstallment : mapCashflow.keySet()) {
				double tiPaidAmount = 0d;
				double vatPaidAmount = 0d;
				for (Cashflow cashflow : mapCashflow.get(numInstallment)) {
					tiPaidAmount += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
					vatPaidAmount += MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
				}
				
				Item item = indexedContainer.addItem(index++);
				item.getItemProperty(Payment.REFERENCE).setValue(payment.getReference());
				item.getItemProperty(Payment.ID).setValue(payment.getId());
				item.getItemProperty(Payment.DESCEN).setValue(payment.getDescEn());
				item.getItemProperty(Payment.PAYMENTDATE).setValue(payment.getPaymentDate());
				
				SecUser user = payment.getReceivedUser();
				EPaymentMethod paymentMethod = payment.getPaymentMethod();
				item.getItemProperty(Payment.PAYMENTMETHOD).setValue(paymentMethod != null ? paymentMethod.getDescLocale() : "");
				item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(tiPaidAmount);
				item.getItemProperty(Payment.VATPAIDAMOUNT).setValue(vatPaidAmount);
				
				Button btnView = new NativeButton();
				btnView.addClickListener(new ClickListener() {
					/** */
					private static final long serialVersionUID = 7397242216961821446L;
					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						inPopupPanel.assignValues(payment, mapCashflow.get(numInstallment));
						UI.getCurrent().addWindow(inPopupPanel);
					}
				});
				btnView.setIcon(FontAwesome.EYE);
				btnView.addStyleName("btn btn-success");
				btnView.addStyleName(Reindeer.BUTTON_SMALL);
				
				Button btnPrint = new NativeButton();
				btnPrint.setIcon(FontAwesome.PRINT);
				btnPrint.addStyleName("btn btn-success");
				btnPrint.addStyleName(Reindeer.BUTTON_SMALL);
				
				HorizontalLayout actionsLayout = new HorizontalLayout();
				actionsLayout.setSpacing(true);
				actionsLayout.addComponent(btnView);
				actionsLayout.addComponent(btnPrint);
				
				item.getItemProperty(ACTIONS).setValue(actionsLayout);
			}
		}
	}
	
	/**
	 * @param cashflow
	 * @return
	 */
	private Map<Integer, List<Cashflow>> groupCashflowByNumInstallment(List<Cashflow> cashflows) {
		Map<Integer, List<Cashflow>> map = new LinkedHashMap<Integer, List<Cashflow>>();
		for (Cashflow cashflow : cashflows) {
			if (!map.containsKey(cashflow.getNumInstallment())) {
				map.put(cashflow.getNumInstallment(), new ArrayList<Cashflow>());
			}
			map.get(cashflow.getNumInstallment()).add(cashflow);
		}
		return map;
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class CashflowComparatorByNumInstallment implements Comparator<Cashflow> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Cashflow c1, Cashflow c2) {
			if (c1 == null || c1.getNumInstallment() == null) {
				if (c2 == null || c2.getNumInstallment() == null) {
					return 0;
				}
				return -1;
			}
			if (c2 == null || c2.getNumInstallment() == null) {
				return 1;
			}
			return c1.getNumInstallment().compareTo(c2.getNumInstallment());
		}
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		table.removeAllItems();
	}

}
