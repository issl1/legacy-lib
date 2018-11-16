package com.nokor.efinance.gui.ui.panel.accounting.detail;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.accounting.tree.AccountingListPanel;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Account Category Popup Panel
 * @author bunlong.taing
 */
public class AccountCategoryPopupPanel extends Window implements ClickListener, ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = 4999778287187483724L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private TextField txtName;
	private TextField txtNameEn;
	private TextArea txtComment;
	
	private Button btnSave;
	private Button btnCancel;
	
	private VerticalLayout messagePanel;
	private AccountingListPanel mainPanel;
	
	private Long categoryId;
	private AccountCategory categoryParent;
	private ECategoryRoot root;
	
	/**
	 * Account Category Popup Panel
	 */
	public AccountCategoryPopupPanel(AccountingListPanel mainPanel) {
		setModal(true);
		this.mainPanel = mainPanel;
		setCaption(I18N.message("account.category"));
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
		txtDesc = ComponentFactory.getTextField("desc", false, 100, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 100, 200);
		
		txtComment = ComponentFactory.getTextArea("comment", false, 300, 100);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtNameEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtComment);
		
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
	 * @param categoryId
	 */
	public void assignValues(Long categoryId) {
		reset();
		if (categoryId != null) {
			this.categoryId = categoryId;
			AccountCategory category = ACCOUNTING_SRV.getById(AccountCategory.class, categoryId);
			txtCode.setValue(StringUtils.defaultString(category.getCode()));
			txtName.setValue(StringUtils.defaultString(category.getName()));
			txtNameEn.setValue(StringUtils.defaultString(category.getNameEn()));
			txtDesc.setValue(StringUtils.defaultString(category.getDesc()));
			txtDescEn.setValue(StringUtils.defaultString(category.getDescEn()));
			txtComment.setValue(StringUtils.defaultString(category.getComment()));
			categoryParent = category.getParent();
			root = category.getRoot();
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
	private AccountCategory getEntity() {
		AccountCategory category = null;
		boolean isUpdate = categoryId != null && categoryId > 0;
		if (isUpdate) {
			category = ACCOUNTING_SRV.getById(AccountCategory.class, categoryId);
		} else {
			category = new AccountCategory();
		}
		buildCategoryDetailsFromControls(category);
		return category;
	}
	
	/**
	 * Build Category Details From Controls
	 * @param category
	 */
	private void buildCategoryDetailsFromControls(AccountCategory category) {
		category.setCode(txtCode.getValue());
		category.setName(txtName.getValue());
		category.setNameEn(txtNameEn.getValue());
		category.setDesc(txtDesc.getValue());
		category.setDescEn(txtDescEn.getValue());
		category.setComment(txtComment.getValue());
		category.setParent(categoryParent);
		category.setRoot(root);
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
		if (root == null) {
			ValidateUtil.addError(I18N.message("field.required.1", I18N.message("category.root")));
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * Show the window
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		txtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		txtComment.setValue("");
		messagePanel.setVisible(false);
		categoryParent = null;
		categoryId = null;
		root = null;
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
	 * @return the categoryParent
	 */
	public AccountCategory getCategoryParent() {
		return categoryParent;
	}

	/**
	 * @param categoryParent the categoryParent to set
	 */
	public void setCategoryParent(AccountCategory categoryParent) {
		this.categoryParent = categoryParent;
	}

	/**
	 * @return the root
	 */
	public ECategoryRoot getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(ECategoryRoot root) {
		this.root = root;
	}

}
