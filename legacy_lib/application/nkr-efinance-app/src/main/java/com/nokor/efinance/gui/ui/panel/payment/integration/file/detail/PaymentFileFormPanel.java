package com.nokor.efinance.gui.ui.panel.payment.integration.file.detail;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.integration.file.item.PaymentFileItemTablePanel;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Payment File Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentFileFormPanel extends AbstractFormPanel implements FinServicesHelper, ClickListener, SelectedTabChangeListener {
	/** */
	private static final long serialVersionUID = -7675050515660887913L;
	
	private static final String FILE_INTEGRATION_PATH = "integration/file/";
	private static final String HEADER_TEMPLATE_NAME = FILE_INTEGRATION_PATH + "paymentFileHeaderLayout";
	private static final String TOTAL_TEMPLATE_NAME = FILE_INTEGRATION_PATH + "paymentFileTotalLayout";
	
	private NavigationPanel navigationPanel;
	private NativeButton btnAllocate;
	
	// Header
	private TextField txtSequence;
	private TextField txtBankCode;
	private TextField txtCompanyAccount;
	private TextField txtCompanyAccountOptional;
	private TextField txtCompanyName;
	private AutoDateField dfEffectiveDate;
	private TextField txtServiceCode;
	private TextField txtFilter;
	
	// Total
	private TextField txtLastSequence;
	private TextField txtFooterBankCode;
	private TextField txtFooterCompanyAccount;
	private TextField txtTotalDebitAmount;
	private TextField txtTotalDebitTransaction;
	private TextField txtTotalCreditAmount;
	private TextField txtTotalCreditTransaction;
	private TextField txtTotalTransaction;
	private TextField txtFooterFilter;
	private TextField txtFixCode;
	private TextField txtTotalAmount;
	
	private Long paymentFileId;
	
	private VerticalLayout paymentDetailLayout;

	private PaymentFileItemTablePanel itemTablePanel;
	
	private TabSheet mainTabSheet;
	
	/**
	 * File Integration Form Panel
	 */
	public PaymentFileFormPanel() {
		super.init();
        setCaption(I18N.message("payment.file"));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		btnAllocate = new NativeButton(I18N.message("allocate"));
		btnAllocate.addClickListener(this);
		btnAllocate.setIcon(FontAwesome.SHARE_SQUARE);
		
		navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAllocate);
		navigationPanel.setVisible(false);
		
		Panel headerPanel = new Panel(createHeaderLayout());
		headerPanel.setCaption(I18N.message("header"));
		
		Panel totalPanel = new Panel(createTotalLayout());
		totalPanel.setCaption(I18N.message("total"));
		
		setEnableControls(false);
		
		paymentDetailLayout = new VerticalLayout();
		paymentDetailLayout.setMargin(true);
		paymentDetailLayout.setSpacing(true);
		paymentDetailLayout.addComponent(navigationPanel);
		paymentDetailLayout.addComponent(headerPanel);
		paymentDetailLayout.addComponent(totalPanel);
		
		itemTablePanel = SpringUtils.getBean(PaymentFileItemTablePanel.class);
		
		mainTabSheet = new TabSheet();
		mainTabSheet.addSelectedTabChangeListener(this);
		mainTabSheet.addTab(paymentDetailLayout, I18N.message("payment.details"));
		mainTabSheet.addTab(itemTablePanel);
		
		return mainTabSheet;
	}
	
	/**
	 * Create Header Layout
	 * @return
	 */
	private CustomLayout createHeaderLayout() {
		txtSequence = ComponentFactory.getTextField(100, 200);
		txtBankCode = ComponentFactory.getTextField(100, 200);
		txtCompanyAccount = ComponentFactory.getTextField(100, 200);
		txtCompanyAccountOptional = ComponentFactory.getTextField(100, 200);
		
		txtCompanyName = ComponentFactory.getTextField(100, 200);
		dfEffectiveDate = ComponentFactory.getAutoDateField();
		txtServiceCode = ComponentFactory.getTextField(100, 200);
		txtFilter = ComponentFactory.getTextField(200, 200);
		
		CustomLayout headerLayout = LayoutHelper.createCustomLayout(HEADER_TEMPLATE_NAME);
		if (headerLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(HEADER_TEMPLATE_NAME), Type.ERROR_MESSAGE);
		}
		
		headerLayout.addComponent(ComponentFactory.getLabel("sequence"), "lblSequence");
		headerLayout.addComponent(txtSequence, "txtSequence");
		headerLayout.addComponent(ComponentFactory.getLabel("bank.code"), "lblBankCode");
		headerLayout.addComponent(txtBankCode, "txtBankCode");
		headerLayout.addComponent(ComponentFactory.getLabel("company.account"), "lblCompanyAccount");
		headerLayout.addComponent(txtCompanyAccount, "txtCompanyAccount");
		headerLayout.addComponent(ComponentFactory.getLabel("company.account.optional"), "lblCompanyAccountOptional");
		headerLayout.addComponent(txtCompanyAccountOptional, "txtCompanyAccountOptional");
		
		headerLayout.addComponent(ComponentFactory.getLabel("company.name"), "lblCompanyName");
		headerLayout.addComponent(txtCompanyName, "txtCompanyName");
		headerLayout.addComponent(ComponentFactory.getLabel("effective.date"), "lblEffectiveDate");
		headerLayout.addComponent(dfEffectiveDate, "dfEffectiveDate");
		headerLayout.addComponent(ComponentFactory.getLabel("service.code"), "lblServiceCode");
		headerLayout.addComponent(txtServiceCode, "txtServiceCode");
		headerLayout.addComponent(ComponentFactory.getLabel("filter"), "lblFilter");
		headerLayout.addComponent(txtFilter, "txtFilter");
		
		return headerLayout;
	}
	
	/**
	 * Create Total Layout
	 * @return
	 */
	private CustomLayout createTotalLayout() {
		txtLastSequence = ComponentFactory.getTextField(100, 200);
		txtFooterBankCode = ComponentFactory.getTextField(100, 200);
		txtFooterCompanyAccount = ComponentFactory.getTextField(100, 200);
		txtTotalDebitAmount = ComponentFactory.getTextField(100, 200);
		txtTotalDebitTransaction = ComponentFactory.getTextField(100, 200);
		txtTotalCreditAmount = ComponentFactory.getTextField(100, 200);
		
		txtTotalCreditTransaction = ComponentFactory.getTextField(100, 200);
		txtTotalTransaction = ComponentFactory.getTextField(100, 200);
		txtFooterFilter = ComponentFactory.getTextField(100, 200);
		txtFixCode = ComponentFactory.getTextField(100, 200);
		txtTotalAmount = ComponentFactory.getTextField(200, 200);
		
		CustomLayout totalLayout = LayoutHelper.createCustomLayout(TOTAL_TEMPLATE_NAME);
		if (totalLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TOTAL_TEMPLATE_NAME), Type.ERROR_MESSAGE);
		}
		
		totalLayout.addComponent(ComponentFactory.getLabel("last.sequence"), "lblLastSequence");
		totalLayout.addComponent(txtLastSequence, "txtLastSequence");
		totalLayout.addComponent(ComponentFactory.getLabel("t.bank.code"), "lblFooterBankCode");
		totalLayout.addComponent(txtFooterBankCode, "txtFooterBankCode");
		totalLayout.addComponent(ComponentFactory.getLabel("t.company.account"), "lblFooterCompanyAccount");
		totalLayout.addComponent(txtFooterCompanyAccount, "txtFooterCompanyAccount");
		totalLayout.addComponent(ComponentFactory.getLabel("total.debit.amount"), "lblTotalDebitAmount");
		totalLayout.addComponent(txtTotalDebitAmount, "txtTotalDebitAmount");
		totalLayout.addComponent(ComponentFactory.getLabel("total.debit.transaction"), "lblTotalDebitTransaction");
		totalLayout.addComponent(txtTotalDebitTransaction, "txtTotalDebitTransaction");
		totalLayout.addComponent(ComponentFactory.getLabel("total.credit.amount"), "lblTotalCreditAmount");
		totalLayout.addComponent(txtTotalCreditAmount, "txtTotalCreditAmount");
		
		totalLayout.addComponent(ComponentFactory.getLabel("total.credit.transaction"), "lblTotalCreditTransaction");
		totalLayout.addComponent(txtTotalCreditTransaction, "txtTotalCreditTransaction");
		totalLayout.addComponent(ComponentFactory.getLabel("total.transaction"), "lblTotalTransaction");
		totalLayout.addComponent(txtTotalTransaction, "txtTotalTransaction");
		totalLayout.addComponent(ComponentFactory.getLabel("t.filter"), "lblFooterFilter");
		totalLayout.addComponent(txtFooterFilter, "txtFooterFilter");
		totalLayout.addComponent(ComponentFactory.getLabel("fix.code"), "lblFixCode");
		totalLayout.addComponent(txtFixCode, "txtFixCode");
		totalLayout.addComponent(ComponentFactory.getLabel("total.amount"), "lblTotalAmount");
		totalLayout.addComponent(txtTotalAmount, "txtTotalAmount");
		
		return totalLayout;
	}
	
	/**
	 * Assign value to controls
	 * @param id
	 */
	public void assignValues(Long id) {
		navigationPanel.setVisible(false);
		paymentFileId = null;
		mainTabSheet.setSelectedTab(paymentDetailLayout);
		if (id != null) {
			paymentFileId = id;
			PaymentFile paymentFile = FILE_INTEGRATION_SRV.getById(PaymentFile.class, id);
			
			txtSequence.setValue(getDefaultString(paymentFile.getSequence()));
			txtBankCode.setValue(getDefaultString(paymentFile.getBankCode()));
			txtCompanyAccount.setValue(getDefaultString(paymentFile.getCompanyAccount()));
			txtCompanyAccountOptional.setValue(getDefaultString(paymentFile.getCompanyAccountOptional()));
			txtCompanyName.setValue(getDefaultString(paymentFile.getCompanyName()));
			dfEffectiveDate.setValue(paymentFile.getEffectiveDate());
			txtServiceCode.setValue(getDefaultString(paymentFile.getServiceCode()));
			txtFilter.setValue(getDefaultString(paymentFile.getFilter()));
			
			txtLastSequence.setValue(getDefaultString(paymentFile.getLastSequence()));
			txtFooterBankCode.setValue(getDefaultString(paymentFile.getFooterBankCode()));
			txtFooterCompanyAccount.setValue(getDefaultString(paymentFile.getFooterCompanyAccount()));
			txtTotalDebitAmount.setValue(AmountUtils.format(paymentFile.getTotalDebitAmount()));
			txtTotalDebitTransaction.setValue(getDefaultString(paymentFile.getTotalDebitTransaction()));
			txtTotalCreditAmount.setValue(AmountUtils.format(paymentFile.getTotalCreditAmount()));
			txtTotalCreditTransaction.setValue(getDefaultString(paymentFile.getTotalCreditTransaction()));
			txtTotalTransaction.setValue(getDefaultString(paymentFile.getTotalTransaction()));
			txtFooterFilter.setValue(getDefaultString(paymentFile.getFooterFilter()));
			txtFixCode.setValue(getDefaultString(paymentFile.getFixCode()));
			txtTotalAmount.setValue(getDefaultString(paymentFile.getTotalAmount()));
			
			if (PaymentFileWkfStatus.NEW.equals(paymentFile.getWkfStatus())) {
				navigationPanel.setVisible(true);
			}
		}
	}
	
	/**
	 * Set Enable Controls
	 * @param enabled
	 */
	private void setEnableControls(boolean enabled) {
		txtSequence.setEnabled(enabled);
		txtBankCode.setEnabled(enabled);
		txtCompanyAccount.setEnabled(enabled);
		txtCompanyAccountOptional.setEnabled(enabled);
		txtCompanyName.setEnabled(enabled);
		dfEffectiveDate.setEnabled(enabled);
		txtServiceCode.setEnabled(enabled);
		txtFilter.setEnabled(enabled);
		
		txtLastSequence.setEnabled(enabled);
		txtFooterBankCode.setEnabled(enabled);
		txtFooterCompanyAccount.setEnabled(enabled);
		txtTotalDebitAmount.setEnabled(enabled);
		txtTotalDebitTransaction.setEnabled(enabled);
		txtTotalCreditAmount.setEnabled(enabled);
		txtTotalCreditTransaction.setEnabled(enabled);
		txtTotalTransaction.setEnabled(enabled);
		txtFooterFilter.setEnabled(enabled);
		txtFixCode.setEnabled(enabled);
		txtTotalAmount.setEnabled(enabled);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAllocate) {
			allocatePaymentFile();
		}
	}
	
	/**
	 */
	private void allocatePaymentFile() {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.allocate"), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = 8656750277645024262L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
			        if (dialog.isConfirmed()) {
			        	PaymentFile paymentFile = PAYMENT_ALLOCATION_SRV.getById(PaymentFile.class, paymentFileId);
			    		PAYMENT_ALLOCATION_SRV.allocatePayments(paymentFile);
			    		if (getParent() instanceof TabSheet) {
							((TabSheet) getParent()).setNeedRefresh(true);
						}
			    		if (itemTablePanel != null) {
			    			itemTablePanel.refresh();
			    		}
			    		assignValues(paymentFileId);
			    		Notification.show(I18N.message("msg.info.allocate.successfully"), Type.HUMANIZED_MESSAGE);
			        }
			    }
			});
	}

	/**
	 * @param itemTablePanel the itemTablePanel to set
	 */
	public void setItemTablePanel(PaymentFileItemTablePanel itemTablePanel) {
		this.itemTablePanel = itemTablePanel;
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (mainTabSheet.getSelectedTab().equals(itemTablePanel)) {
			itemTablePanel.assignValues(this.paymentFileId);
		}
	}
	
}
