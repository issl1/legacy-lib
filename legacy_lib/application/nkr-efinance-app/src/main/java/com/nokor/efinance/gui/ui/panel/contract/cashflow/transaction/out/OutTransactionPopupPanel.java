package com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.out;

import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * Out Transaction Popup Panel
 * @author bunlong.taing
 */
public class OutTransactionPopupPanel extends Window {
	/** */
	private static final long serialVersionUID = 2291182449328302614L;
	
	private Label lblPaymentCode;
	private Label lblRequestDate;
	private Label lblUserFullName;
	private Label lblMethodOfPayment;
	private Label lblBankName;
	private Label lblSubmittedDate;
	private Label lblSubmittingUser;
	private Label lblBreakdown;
	private Label lblDescEn;
	private Label lblUserId;
	private Label lblAmount;
	private Label lblPayeeName;
	private Label lblAccountNo;
	private Label lblPostedDate;
	private Label lblPostingUser;

	/**
	 */
	public OutTransactionPopupPanel() {
		setCaption(I18N.message("payment.details"));
		setModal(true);
		setResizable(false);
		initControls();
		setContent(createForm());
	}
	
	private void initControls() {
		lblPaymentCode = ComponentFactory.getLabel();
		lblRequestDate = ComponentFactory.getLabel();
		lblUserFullName = ComponentFactory.getLabel();
		lblMethodOfPayment = ComponentFactory.getLabel();
		lblBankName = ComponentFactory.getLabel();
		lblSubmittedDate = ComponentFactory.getLabel();
		lblSubmittingUser = ComponentFactory.getLabel();
		lblBreakdown = ComponentFactory.getLabel();
		lblDescEn = ComponentFactory.getLabel();
		lblUserId = ComponentFactory.getLabel();
		lblAmount = ComponentFactory.getLabel();
		lblPayeeName = ComponentFactory.getLabel();
		lblAccountNo = ComponentFactory.getLabel();
		lblPostedDate = ComponentFactory.getLabel();
		lblPostingUser = ComponentFactory.getLabel();
	}
	
	/**
	 * @return
	 */
	private Component createForm() {
		GridLayout content = new GridLayout(4, 8);
		content.setMargin(true);
		content.setSpacing(true);
		
		content.addComponent(new Label(I18N.message("payment.code") + " : "));
		content.addComponent(lblPaymentCode);
		content.addComponent(new Label(I18N.message("desc") + " : "));
		content.addComponent(lblDescEn);
		
		content.addComponent(new Label(I18N.message("request.date") + " : "));
		content.addComponent(lblRequestDate);
		content.addComponent(new Label(I18N.message("user.id") + " : "));
		content.addComponent(lblUserId);
		
		content.addComponent(new Label(I18N.message("user.full.name") + " : "));
		content.addComponent(lblUserFullName);
		content.addComponent(new Label(I18N.message("amount") + " : "));
		content.addComponent(lblAmount);
		
		content.addComponent(new Label(I18N.message("method.of.payment") + " : "));
		content.addComponent(lblMethodOfPayment);
		content.addComponent(new Label(I18N.message("payee.name") + " : "));
		content.addComponent(lblPayeeName);
		
		content.addComponent(new Label(I18N.message("bank.name") + " : "));
		content.addComponent(lblBankName);
		content.addComponent(new Label(I18N.message("account.no") + " : "));
		content.addComponent(lblAccountNo);
		
		content.addComponent(new Label(I18N.message("submitted.date") + " : "));
		content.addComponent(lblSubmittedDate);
		content.addComponent(new Label(I18N.message("posted.date") + " : "));
		content.addComponent(lblPostedDate);
		
		content.addComponent(new Label(I18N.message("submitted.user") + " : "));
		content.addComponent(lblSubmittingUser);
		content.addComponent(new Label(I18N.message("posting.user") + " : "));
		content.addComponent(lblPostingUser);
		
		content.addComponent(new Label(I18N.message("breakdown") + " : "));
		content.addComponent(lblBreakdown);
		
		return content;
	}
	
	/**
	 * @param paymentId
	 */
	public void assignValues(Payment payment) {
		reset();
		if (payment != null) {
			lblPaymentCode.setValue(payment.getReference());
			lblRequestDate.setValue("");
			
			SecUser user = payment.getReceivedUser();
			lblUserFullName.setValue(user != null ? user.getUsername() : "");
			lblMethodOfPayment.setValue(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescLocale() : "");
			lblBankName.setValue("");
			lblSubmittedDate.setValue("");
			lblSubmittingUser.setValue("");
			lblBreakdown.setValue("");
			lblDescEn.setValue(payment.getDescEn());
			lblUserId.setValue(user != null ? user.getLogin() : "");
			lblAmount.setValue(AmountUtils.format(payment.getTiPaidAmount()));
			lblPayeeName.setValue("");
			lblAccountNo.setValue("");
			lblPostedDate.setValue("");
			lblPostingUser.setValue("");
		}
	}
	
	/**
	 * Reset the controls
	 */
	private void reset() {
		lblPaymentCode.setValue("");
		lblRequestDate.setValue("");
		lblUserFullName.setValue("");
		lblMethodOfPayment.setValue("");
		lblBankName.setValue("");
		lblSubmittedDate.setValue("");
		lblSubmittingUser.setValue("");
		lblBreakdown.setValue("");
		lblDescEn.setValue("");
		lblUserId.setValue("");
		lblAmount.setValue("");
		lblPayeeName.setValue("");
		lblAccountNo.setValue("");
		lblPostedDate.setValue("");
		lblPostingUser.setValue("");
	}

}
