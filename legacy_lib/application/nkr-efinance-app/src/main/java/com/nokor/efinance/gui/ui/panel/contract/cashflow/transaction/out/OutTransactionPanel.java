package com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.MPayment;
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
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.UI;

/**
 * Out Transaction Panel
 * @author bunlong.taing
 */
public class OutTransactionPanel extends AbstractControlPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 4875831867306459568L;
	
	private static final String ACTIONS = "actions";
	
	private SimplePagedTable<Entity> table;
	private OutTransactionPopupPanel outPopupPanel;
	
	/**
	 */
	public OutTransactionPanel() {
		setMargin(true);
		setSpacing(true);
		outPopupPanel = new OutTransactionPopupPanel();
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
		columnDefinitions.add(new ColumnDefinition("payment.group", I18N.message("payment.group"), String.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(Payment.REFERENCE, I18N.message("payment.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.ID, I18N.message("payment.id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(Payment.DESCEN, I18N.message("desc"), String.class, Align.LEFT, 100));
		
		// TODO Request date
		columnDefinitions.add(new ColumnDefinition("requested.date", I18N.message("requested.date"), Date.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(Payment.RECEIVEDUSER, I18N.message("user"), String.class, Align.LEFT, 100));
		
		// TODO Submitted date
		columnDefinitions.add(new ColumnDefinition("submitted.date", I18N.message("submitted.date"), Date.class, Align.LEFT, 100));
		// TODO Posting date
		columnDefinitions.add(new ColumnDefinition("posting.date", I18N.message("posting.date"), Date.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTMETHOD, I18N.message("method.of.payment"), String.class, Align.RIGHT, 100));
		
		// TODO Payee name
		columnDefinitions.add(new ColumnDefinition("payee.name", I18N.message("payee.name"), String.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition(MPayment.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 100));
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
			restriction.setPaymentTypes(new EPaymentType[] {EPaymentType.ORC, EPaymentType.DCO});
			List<Payment> payments = PAYMENT_SRV.list(restriction);
			setTableIndexedContainer(payments);
		}
	}
	
	/**
	 * @param payments
	 */
	private void setTableIndexedContainer(List<Payment> payments) {
		Container indexedContainer = table.getContainerDataSource();
		for (Payment payment : payments) {
			Item item = indexedContainer.addItem(payment.getId());
			item.getItemProperty(Payment.REFERENCE).setValue(payment.getReference());
			item.getItemProperty(Payment.ID).setValue(payment.getId());
			item.getItemProperty(Payment.DESCEN).setValue(payment.getDescEn());
			SecUser user = payment.getReceivedUser();
			item.getItemProperty(Payment.RECEIVEDUSER).setValue(user != null ? user.getLogin() : "");
			item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(payment.getTiPaidAmount());
			EPaymentMethod paymentMethod = payment.getPaymentMethod();
			item.getItemProperty(Payment.PAYMENTMETHOD).setValue(paymentMethod != null ? paymentMethod.getDescLocale() : "");
			item.getItemProperty(MPayment.WKFSTATUS).setValue(payment.getWkfStatus().getDescLocale());
			
			Button btnView = new NativeButton();
			btnView.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = 7397242216961821446L;
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					outPopupPanel.assignValues(payment);
					UI.getCurrent().addWindow(outPopupPanel);
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
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		table.removeAllItems();
	}

}
