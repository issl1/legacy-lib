package com.nokor.efinance.gui.ui.panel.accounting.detail;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.gui.ui.panel.accounting.tree.AccountingListPanel;
import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * AccountPopupPanel
 * @author bunlong.taing
 */
public class AccountPopupPanel extends Window implements ClickListener, ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = -6202333725589403225L;
	
	private TextField txtCode;
	private TextField txtName;
	private TextField txtNameEn;
	private TextField txtReference;
	private TextField txtInfo;
	private TextField txtOtherInfo;
	private TextField txtStartingBalance;
	
	private Button btnSave;
	private Button btnCancel;
	
	private VerticalLayout messagePanel;
	private AccountingListPanel mainPanel;
	
	private Long accountId;
	private AccountCategory category;
	
	/**
	 * Account Popup Panel
	 * @param mainPanel
	 */
	public AccountPopupPanel(AccountingListPanel mainPanel) {
		setModal(true);
		this.mainPanel = mainPanel;
		setCaption(I18N.message("account"));
		createForm();
	}
	
	/**
	 * Create Form
	 */
	private void createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		txtCode = ComponentFactory.getTextField("code", true, 100, 200);
		txtName = ComponentFactory.getTextField("name", false, 100, 200);
		txtNameEn = ComponentFactory.getTextField("name.en", false, 100, 200);
		txtStartingBalance = ComponentFactory.getTextField("starting.balance", true, 100, 200);
		txtReference = ComponentFactory.getTextField("reference", false, 100, 200);
		txtInfo = ComponentFactory.getTextField("information", false, 100, 200);
		txtOtherInfo = ComponentFactory.getTextField("other.info", false, 100, 200);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtNameEn);
		formLayout.addComponent(txtStartingBalance);
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtInfo);
		formLayout.addComponent(txtOtherInfo);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(formLayout);
			
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(messagePanel);
		content.addComponent(verticalLayout);
		
		setContent(content);
	}
	
	/**
	 * Assign Values
	 * @param accountId
	 */
	public void assignValues(Long accountId) {
		reset();
		if (accountId != null) {
			this.accountId = accountId;
			Account account = ACCOUNTING_SRV.getById(Account.class, accountId);
			txtCode.setValue(StringUtils.defaultString(account.getCode()));
			txtName.setValue(StringUtils.defaultString(account.getName()));
			txtNameEn.setValue(StringUtils.defaultString(account.getNameEn()));
		
			BigDecimal startingBalance = account.getStartingBalance();
			txtStartingBalance.setValue(startingBalance != null? startingBalance.toPlainString() : "");
			txtReference.setValue(StringUtils.defaultString(account.getReference()));
			txtInfo.setValue(StringUtils.defaultString(account.getInfo()));
			txtOtherInfo.setValue(StringUtils.defaultString(account.getOtherInfo()));
			// setCategory(account.getCategory());
		}
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (validate()) {
			ACCOUNTING_SRV.saveOrUpdate(getEntity());
			mainPanel.refreshAccountList();
			close();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Get Entity
	 * @return
	 */
	private Account getEntity() {
		Account account = null;
		boolean isUpdate = accountId != null && accountId > 0;
		if (isUpdate) {
			account = ACCOUNTING_SRV.getById(Account.class, accountId);
		} else {
			account = new Account();
		}
		buildAccountDetailsFromControls(account);
		return account;
	}
	
	/**
	 * Build Account Details From Controls
	 * @param account
	 */
	private void buildAccountDetailsFromControls(Account account) {
		account.setCode(txtCode.getValue());
		account.setName(txtName.getValue());
		account.setNameEn(txtNameEn.getValue());
		account.setReference(txtReference.getValue());
		account.setInfo(txtInfo.getValue());
		account.setOtherInfo(txtOtherInfo.getValue());
		account.setStartingBalance(MyNumberUtils.getBigDecimal(txtStartingBalance.getValue()));
		// account.setCategory(category);
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		boolean valid = true;
		
		ValidateUtil.clearErrors();
		if (!ValidateUtil.checkMandatoryField(txtCode, "code")) {
			valid = false;
		}
		if (!ValidateUtil.checkMandatoryField(txtStartingBalance, "starting.balance")) {
			valid = false;
		} else if(!ValidateUtil.checkBigDecimalField(txtStartingBalance, "starting.balance")) {
			valid = false;
		}
		if (category == null) {
			ValidateUtil.addError(I18N.message("field.required.1", I18N.message("account.category")));
			valid = false;
		}
		return valid;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		txtStartingBalance.setValue("");
		txtReference.setValue("");
		txtInfo.setValue("");
		txtOtherInfo.setValue("");
		accountId = null;
		category = null;
		messagePanel.setVisible(false);
	}
	
	/**
	 * Display Errors Panel
	 */
	private void displayErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = new Label(ValidateUtil.getErrorMessages(), ContentMode.HTML);
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Show the window
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}

	/**
	 * @return the category
	 */
	public AccountCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AccountCategory category) {
		this.category = category;
	}

}
