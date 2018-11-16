package com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.in;

import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * In Transaction Popup Panel
 * @author bunlong.taing
 */
public class InTransactionPopupPanel extends Window {
	/** */
	private static final long serialVersionUID = 2291182449328302614L;
	
	private Label lblReceiptGroup;
	private Label lblDate;
	private Label lblReceiptCode;
	private Label lblMethodOfPayment;
	private Label lblRefDue;
	private Label lblDescEn;
	private Label lblReceiptId;
	private Label lblAmount;
	private Label lblAmountVat;
	
	private TextArea txtBreakdown;

	/**
	 */
	public InTransactionPopupPanel() {
		setCaption(I18N.message("payment.details"));
		setModal(true);
		setResizable(false);
		initControls();
		setContent(createForm());
	}
	
	private void initControls() {
		lblReceiptGroup = ComponentFactory.getLabel();
		lblDate = ComponentFactory.getLabel();
		lblReceiptCode = ComponentFactory.getLabel();
		lblMethodOfPayment = ComponentFactory.getLabel();
		lblDescEn = ComponentFactory.getLabel();
		lblAmount = ComponentFactory.getLabel();
		lblRefDue = ComponentFactory.getLabel();
		lblReceiptId = ComponentFactory.getLabel();
		lblAmountVat = ComponentFactory.getLabel();
		txtBreakdown = ComponentFactory.getTextArea(false, 300, 100);
		txtBreakdown.setEnabled(false);
	}
	
	/**
	 * @return
	 */
	private Component createForm() {
		GridLayout content = new GridLayout(4, 5);
		content.setSpacing(true);
		
		content.addComponent(new Label(I18N.message("receipt.group") + " : "));
		content.addComponent(lblReceiptGroup);
		content.addComponent(new Label(I18N.message("desc") + " : "));
		content.addComponent(lblDescEn);
		
		content.addComponent(new Label(I18N.message("date") + " : "));
		content.addComponent(lblDate);
		content.addComponent(new Label(I18N.message("receipt.id") + " : "));
		content.addComponent(lblReceiptId);
		
		content.addComponent(new Label(I18N.message("receipt.code") + " : "));
		content.addComponent(lblReceiptCode);
		content.addComponent(new Label(I18N.message("amount") + " : "));
		content.addComponent(lblAmount);
		
		content.addComponent(new Label(I18N.message("method.of.payment") + " : "));
		content.addComponent(lblMethodOfPayment);
		content.addComponent(new Label(I18N.message("vat.amount") + " : "));
		content.addComponent(lblAmountVat);
		
		content.addComponent(new Label(I18N.message("ref.due") + " : "));
		content.addComponent(lblRefDue);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(ComponentFactory.getLabel("breakdown"));
		horizontalLayout.addComponent(txtBreakdown);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(content);
		verticalLayout.addComponent(horizontalLayout);
		
		return verticalLayout;
	}
	
	/**
	 * @param paymentId
	 */
	public void assignValues(Payment payment, List<Cashflow> cashflows) {
		reset();
		if (payment != null) {
			lblDate.setValue(DateUtils.getDateLabel(payment.getPaymentDate()));
			lblReceiptCode.setValue(payment.getReference());
			lblMethodOfPayment.setValue(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescLocale() : "");
			lblDescEn.setValue(payment.getDescEn());
			lblAmount.setValue(AmountUtils.format(payment.getTiPaidAmount()));
			lblReceiptId.setValue(payment.getId().toString());
			
			double tiPaidAmount = 0d;
			double vatPaidAmount = 0d;
			double principle = 0d;
			double interest = 0d;
			double vat = 0d;
			
			for (Cashflow cashflow : cashflows) {
				tiPaidAmount += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
				vatPaidAmount += MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
				
				if (ECashflowType.CAP.equals(cashflow.getCashflowType())) {
					principle += MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount());
					vat += MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
				} else if (ECashflowType.IAP.equals(cashflow.getCashflowType())) {					
					interest += MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount());
					vat += MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
				}
			}
			
			lblAmount.setValue(AmountUtils.format(tiPaidAmount));
			lblAmountVat.setValue(AmountUtils.format(vatPaidAmount));
			
			txtBreakdown.setValue(I18N.message("principle") + " : " + AmountUtils.format(principle) + "\n" +
					I18N.message("interest") + " : " + AmountUtils.format(interest) + "\n" + 
					I18N.message("vat : ") + " : " + AmountUtils.format(vat));
		}
	}
	
	/**
	 * Reset the controls
	 */
	private void reset() {
		lblReceiptGroup.setValue("");
		lblDate.setValue("");
		lblReceiptCode.setValue("");
		lblRefDue.setValue("");
		lblReceiptId.setValue("");
		lblAmountVat.setValue("");
		txtBreakdown.setValue("");
		lblMethodOfPayment.setValue("");
		lblDescEn.setValue("");
		lblAmount.setValue("");
	}

}
