package com.nokor.efinance.gui.ui.panel.payment.integration.file.item;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * PaymentFileItem Popup Panel
 * @author bunlong.taing
 */
public class PaymentFileItemPopupPanel extends Window implements FinServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = 6344830303563831851L;
	
	private static final String FILE_INTEGRATION_PATH = "integration/file/";
	private static final String TEMPALTE_NAME = FILE_INTEGRATION_PATH + "paymentFileItemLayout";
	
	private TextField txtSequence;
	private TextField txtBankCode;
	private TextField txtCompanyAccount;
	private AutoDateField dfPaymentDate;
	private TextField txtCustomerId;
	private TextField txtCustomerName;
	private TextField txtCustomerRef1;
	private TextField txtCustomerRef2;
	private TextField txtCustomerRef3;
	private TextField txtBranchNo;
	private TextField txtTellerNo;
	private TextField txtTransactionKind;
	private TextField txtTransactionCode;
	private TextField txtChequeNo;
	private TextField txtAmount;
	private TextField txtChequeBankCode;
	private TextField txtChequeBranchCode;
	private TextField txtFilter1;
	private TextField txtChequeNoNew;
	private TextField txtFilter2;
	private TextField txtChequeNoNew1;
	private TextField txtFixCode;
	private TextField txtPostCode;
	private TextField txtReceiveNo;
	private TextField txtPayeeFeeSameZone;
	private TextField txtPayeeFeeDiffZone;
	
	private NavigationPanel navigationPanel;
	private NativeButton btnAllocate;
	private Long paymentFileItemId;
	
	private PaymentFileItemTablePanel tablePanel;
	
	/**
	 * @param caption
	 */
	public PaymentFileItemPopupPanel(String caption, PaymentFileItemTablePanel tablePanel) {
		super(caption);
		setModal(true);
		setResizable(true);
		this.tablePanel = tablePanel;
		
		btnAllocate = new NativeButton(I18N.message("allocate"));
		btnAllocate.addClickListener(this);
		btnAllocate.setIcon(FontAwesome.SHARE_SQUARE);
		
		navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAllocate);
		navigationPanel.setVisible(false);
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(createForm());
		setEnabledControls(false);
		
		setContent(content);
	}
	
	/**
	 * Create form
	 * @return
	 */
	private Component createForm() {
		txtSequence = ComponentFactory.getTextField(100, 200);
		txtBankCode = ComponentFactory.getTextField(100, 200);
		txtCompanyAccount = ComponentFactory.getTextField(100, 200);
		dfPaymentDate = ComponentFactory.getAutoDateField();
		txtCustomerId = ComponentFactory.getTextField(100, 200);
		txtCustomerName = ComponentFactory.getTextField(100, 200);
		txtCustomerRef1 = ComponentFactory.getTextField(100, 200);
		txtCustomerRef2 = ComponentFactory.getTextField(100, 200);
		txtCustomerRef3 = ComponentFactory.getTextField(100, 200);
		txtBranchNo = ComponentFactory.getTextField(100, 200);
		txtTellerNo = ComponentFactory.getTextField(100, 200);
		txtTransactionKind = ComponentFactory.getTextField(100, 200);
		txtTransactionCode = ComponentFactory.getTextField(100, 200);
		
		txtChequeNo = ComponentFactory.getTextField(100, 200);
		txtAmount = ComponentFactory.getTextField(100, 200);
		txtChequeBankCode = ComponentFactory.getTextField(100, 200);
		txtChequeBranchCode = ComponentFactory.getTextField(100, 200);
		txtFilter1 = ComponentFactory.getTextField(100, 200);
		txtChequeNoNew = ComponentFactory.getTextField(100, 200);
		txtFilter2 = ComponentFactory.getTextField(100, 200);
		txtChequeNoNew1 = ComponentFactory.getTextField(100, 200);
		txtFixCode = ComponentFactory.getTextField(100, 200);
		txtPostCode = ComponentFactory.getTextField(100, 200);
		txtReceiveNo = ComponentFactory.getTextField(100, 200);
		txtPayeeFeeSameZone = ComponentFactory.getTextField(100, 200);
		txtPayeeFeeDiffZone = ComponentFactory.getTextField(100, 200);
		
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPALTE_NAME);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPALTE_NAME), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(ComponentFactory.getLabel("sequence"), "lblSequence");
		customLayout.addComponent(txtSequence, "txtSequence");
		customLayout.addComponent(ComponentFactory.getLabel("bank.code"), "lblBankCode");
		customLayout.addComponent(txtBankCode, "txtBankCode");
		customLayout.addComponent(ComponentFactory.getLabel("company.account"), "lblCompanyAccount");
		customLayout.addComponent(txtCompanyAccount, "txtCompanyAccount");
		customLayout.addComponent(ComponentFactory.getLabel("payment.date"), "lblPaymentDate");
		customLayout.addComponent(dfPaymentDate, "dfPaymentDate");
		customLayout.addComponent(ComponentFactory.getLabel("customer.id"), "lblCustomerId");
		customLayout.addComponent(txtCustomerId, "txtCustomerId");
		customLayout.addComponent(ComponentFactory.getLabel("customer.name"), "lblCustomerName");
		customLayout.addComponent(txtCustomerName, "txtCustomerName");
		customLayout.addComponent(ComponentFactory.getLabel("customer.ref.1"), "lblCustomerRef1");
		customLayout.addComponent(txtCustomerRef1, "txtCustomerRef1");
		customLayout.addComponent(ComponentFactory.getLabel("customer.ref.2"), "lblCustomerRef2");
		customLayout.addComponent(txtCustomerRef2, "txtCustomerRef2");
		customLayout.addComponent(ComponentFactory.getLabel("customer.ref.3"), "lblCustomerRef3");
		customLayout.addComponent(txtCustomerRef3, "txtCustomerRef3");
		customLayout.addComponent(ComponentFactory.getLabel("branch.no"), "lblBranchNo");
		customLayout.addComponent(txtBranchNo, "txtBranchNo");
		customLayout.addComponent(ComponentFactory.getLabel("teller.no"), "lblTellerNo");
		customLayout.addComponent(txtTellerNo, "txtTellerNo");
		customLayout.addComponent(ComponentFactory.getLabel("transaction.kind"), "lblTransactionKind");
		customLayout.addComponent(txtTransactionKind, "txtTransactionKind");
		customLayout.addComponent(ComponentFactory.getLabel("transaction.code"), "lblTransactionCode");
		customLayout.addComponent(txtTransactionCode, "txtTransactionCode");
		
		customLayout.addComponent(ComponentFactory.getLabel("cheque.no"), "lblChequeNo");
		customLayout.addComponent(txtChequeNo, "txtChequeNo");
		customLayout.addComponent(ComponentFactory.getLabel("amount"), "lblAmount");
		customLayout.addComponent(txtAmount, "txtAmount");
		customLayout.addComponent(ComponentFactory.getLabel("cheque.bank.code"), "lblChequeBankCode");
		customLayout.addComponent(txtChequeBankCode, "txtChequeBankCode");
		customLayout.addComponent(ComponentFactory.getLabel("cheque.branch.code"), "lblChequeBranchCode");
		customLayout.addComponent(txtChequeBranchCode, "txtChequeBranchCode");
		customLayout.addComponent(ComponentFactory.getLabel("filter.1"), "lblFilter1");
		customLayout.addComponent(txtFilter1, "txtFilter1");
		customLayout.addComponent(ComponentFactory.getLabel("cheque.no.new"), "lblChequeNoNew");
		customLayout.addComponent(txtChequeNoNew, "txtChequeNoNew");
		customLayout.addComponent(ComponentFactory.getLabel("filter.2"), "lblFilter2");
		customLayout.addComponent(txtFilter2, "txtFilter2");
		customLayout.addComponent(ComponentFactory.getLabel("cheque.no.new.1"), "lblChequeNoNew1");
		customLayout.addComponent(txtChequeNoNew1, "txtChequeNoNew1");
		customLayout.addComponent(ComponentFactory.getLabel("fix.code"), "lblFixCode");
		customLayout.addComponent(txtFixCode, "txtFixCode");
		customLayout.addComponent(ComponentFactory.getLabel("post.code"), "lblPostCode");
		customLayout.addComponent(txtPostCode, "txtPostCode");
		customLayout.addComponent(ComponentFactory.getLabel("receive.no"), "lblReceiveNo");
		customLayout.addComponent(txtReceiveNo, "txtReceiveNo");
		customLayout.addComponent(ComponentFactory.getLabel("payee.fee.same.zone"), "lblPayeeFeeSameZone");
		customLayout.addComponent(txtPayeeFeeSameZone, "txtPayeeFeeSameZone");
		customLayout.addComponent(ComponentFactory.getLabel("payee.fee.diff.zone"), "lblPayeeFeeDiffZone");
		customLayout.addComponent(txtPayeeFeeDiffZone, "txtPayeeFeeDiffZone");
		
		return customLayout;
	}
	
	/**
	 * Assign values to controls
	 * @param id
	 */
	public void assignValues(Long id) {
		navigationPanel.setVisible(false);
		paymentFileItemId = null;
		if (id != null) {
			paymentFileItemId = id;
			PaymentFileItem item = FILE_INTEGRATION_SRV.getById(PaymentFileItem.class, id);
			
			txtSequence.setValue(getDefaultString(item.getSequence()));
			txtBankCode.setValue(item.getBankCode());
			txtCompanyAccount.setValue(item.getCompanyAccount());
			dfPaymentDate.setValue(item.getPaymentDate());
			txtCustomerId.setValue(item.getCustomerId());
			txtCustomerName.setValue(item.getCustomerName());
			txtCustomerRef1.setValue(item.getCustomerRef1());
			txtCustomerRef2.setValue(item.getCustomerRef2());
			txtCustomerRef3.setValue(item.getCustomerRef3());
			txtBranchNo.setValue(item.getBranchNo());
			txtTellerNo.setValue(item.getTellerNo());
			txtTransactionKind.setValue(item.getTransactionKind());
			txtTransactionCode.setValue(item.getTransactionCode());
			txtChequeNo.setValue(item.getChequeNo());
			txtAmount.setValue(AmountUtils.format(item.getAmount()));
			txtChequeBankCode.setValue(item.getChequeBankCode());
			txtChequeBranchCode.setValue(item.getChequeBranchCode());
			txtFilter1.setValue(item.getFilter1());
			txtChequeNoNew.setValue(item.getChequeNoNew());
			txtFilter2.setValue(item.getFilter2());
			txtChequeNoNew1.setValue(item.getChequeNoNew1());
			txtFixCode.setValue(item.getFixCode());
			txtPostCode.setValue(item.getPostCode());
			txtReceiveNo.setValue(item.getReceiveNo());
			txtPayeeFeeSameZone.setValue(item.getPayeeFeeSameZone());
			txtPayeeFeeDiffZone.setValue(item.getPayeeFeeDiffZone());
			
			if (PaymentFileWkfStatus.NEW.equals(item.getWkfStatus())) {
				navigationPanel.setVisible(true);
			}
		}
	}
	
	/**
	 * Set enabled controls
	 * @param enabled
	 */
	private void setEnabledControls(boolean enabled) {
		txtSequence.setEnabled(enabled);
		txtBankCode.setEnabled(enabled);
		txtCompanyAccount.setEnabled(enabled);
		dfPaymentDate.setEnabled(enabled);
		txtCustomerId.setEnabled(enabled);
		txtCustomerName.setEnabled(enabled);
		txtCustomerRef1.setEnabled(enabled);
		txtCustomerRef2.setEnabled(enabled);
		txtCustomerRef3.setEnabled(enabled);
		txtBranchNo.setEnabled(enabled);
		txtTellerNo.setEnabled(enabled);
		txtTransactionKind.setEnabled(enabled);
		txtTransactionCode.setEnabled(enabled);
		txtChequeNo.setEnabled(enabled);
		txtAmount.setEnabled(enabled);
		txtChequeBankCode.setEnabled(enabled);
		txtChequeBranchCode.setEnabled(enabled);
		txtFilter1.setEnabled(enabled);
		txtChequeNoNew.setEnabled(enabled);
		txtFilter2.setEnabled(enabled);
		txtChequeNoNew1.setEnabled(enabled);
		txtFixCode.setEnabled(enabled);
		txtPostCode.setEnabled(enabled);
		txtReceiveNo.setEnabled(enabled);
		txtPayeeFeeSameZone.setEnabled(enabled);
		txtPayeeFeeDiffZone.setEnabled(enabled);
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return value == null ? "" : value;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAllocate) {
			allocatePaymentFileItem();
		}
	}
	
	private void allocatePaymentFileItem() {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.allocate"), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = 8656750277645024262L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
			        if (dialog.isConfirmed()) {
			        	PaymentFileItem item = PAYMENT_ALLOCATION_SRV.getById(PaymentFileItem.class, paymentFileItemId);
						PAYMENT_ALLOCATION_SRV.allocatePaymentFileItem(item);
						assignValues(paymentFileItemId);
						tablePanel.refresh();
			    		Notification.show(I18N.message("msg.info.allocate.successfully"), Type.HUMANIZED_MESSAGE);
			        }
			    }
			});
	}

}
