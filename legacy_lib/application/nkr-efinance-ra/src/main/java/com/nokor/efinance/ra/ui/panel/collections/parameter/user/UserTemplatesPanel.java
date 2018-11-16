package com.nokor.efinance.ra.ui.panel.collections.parameter.user;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Set User tab panel in collection RA
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UserTemplatesPanel.NAME)
public class UserTemplatesPanel extends AbstractTabPanel implements View, ClickListener {
	
	/** */
	private static final long serialVersionUID = 4371292459186056554L;

	public static final String NAME = "user.templates";
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"120px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private Button btnAdd;
	
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	private Button getButton(String caption, ThemeResource themeResource) {
		Button button = ComponentFactory.getButton(caption);
		button.setIcon(themeResource);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = ComponentFactory.getLabel();
		return label;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		super.setMargin(false);
		btnAdd = getButton("add", new ThemeResource("../nkr-default/icons/16/add.png"));
		btnAdd.addClickListener(this);
		
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		
        contentLayout.addComponent(createLetterStatusTable());
        
        TabSheet mainTab = new TabSheet();
        mainTab.addTab(contentLayout, I18N.message("user.templates"));
		
		return mainTab;
	}

	/**
	 * 
	 * @return
	 */
	private CustomLayout createLetterStatusTable(){
		CustomLayout userCustomLayout = new CustomLayout("xxx");
		String userTemplate = OPEN_TABLE;
		userTemplate += OPEN_TR;
		userTemplate += "<th class=\"align-center\" width=\"60px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		userTemplate += "<div location =\"lblUserID\" />";
		userTemplate += CLOSE_TH;
		userTemplate += "<th class=\"align-center\" width=\"60px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		userTemplate += "<div location =\"lblLogin\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblPassword\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblBranch\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblDivision\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblCompanyName\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblFirstName\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblLastName\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblDOB\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblPhoneNumber\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblEmail\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblAddress\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblBankName\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblBankAccount\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"lblDateAdded\" />";
		userTemplate += CLOSE_TH;
		userTemplate += OPEN_TH;
		userTemplate += "<div location =\"btnAdd\" />";
		userTemplate += CLOSE_TH;
		userTemplate += CLOSE_TR;
		userCustomLayout.addComponent(new Label(I18N.message("user.id")), "lblUserID");
		userCustomLayout.addComponent(new Label(I18N.message("login")), "lblLogin");
		userCustomLayout.addComponent(new Label(I18N.message("password")), "lblPassword");
		userCustomLayout.addComponent(new Label(I18N.message("branch")), "lblBranch");
		userCustomLayout.addComponent(new Label(I18N.message("division")), "lblDivision");
		userCustomLayout.addComponent(new Label(I18N.message("company.name")), "lblCompanyName");
		userCustomLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		userCustomLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		userCustomLayout.addComponent(new Label(I18N.message("dob")), "lblDOB");
		userCustomLayout.addComponent(new Label(I18N.message("phone.number")), "lblPhoneNumber");
		userCustomLayout.addComponent(new Label(I18N.message("email")), "lblEmail");
		userCustomLayout.addComponent(new Label(I18N.message("address")), "lblAddress");
		userCustomLayout.addComponent(new Label(I18N.message("bank.name")), "lblBankName");
		userCustomLayout.addComponent(new Label(I18N.message("bank.account")), "lblBankAccount");
		userCustomLayout.addComponent(new Label(I18N.message("date.added")), "lblDateAdded");
		userCustomLayout.addComponent(btnAdd, "btnAdd");
		
		for (int i = 0; i < 3; i++) {
			userTemplate += OPEN_TR;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblUserIDValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblLoginValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblPasswordValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblBranchValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblDivisionValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblCompanyNameValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblFirstNameValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblLastNameValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblDOBValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblPhoneNumberValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblEmailValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblAddressValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblBankNameValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblBankAccountValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"lblDateAddedValue" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += OPEN_TD;
			userTemplate += "<div location =\"btnLayout" + i + "\" />";
			userTemplate += CLOSE_TD;
			userTemplate += CLOSE_TR;
			
			Label lblUserIDValue = getLabel();
			Label lblLoginValue = getLabel();
			Label lblPasswordValue = getLabel();
			Label lblBranchValue = getLabel();
			Label lblDivisionValue = getLabel();
			Label lblCompanyNameValue = getLabel();
			Label lblFirstNameValue = getLabel();
			Label lblLastNameValue = getLabel();
			Label lblDOBValue = getLabel();
			Label lblPhoneNumberValue = getLabel();
			Label lblEmailValue = getLabel();
			Label lblAddressValue = getLabel();
			Label lblBankNameValue = getLabel();
			Label lblBankAccountValue = getLabel();
			Label lblDateAddedValue = getLabel();
			
			Button btnEdit = getButton("edit", new ThemeResource("../nkr-default/icons/16/edit.png"));
			Button btnDelete = getButton("delete", new ThemeResource("../nkr-default/icons/16/close.png"));
			btnEdit.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = -747493974595740677L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().addWindow(windowUserStatus());
				}
			});
			btnDelete.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = -2348888263578641153L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
							new String[] {}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = -4220564108032494542L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									dialog.close();
					            }
							}
						});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
				}
			});
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSpacing(true);
			buttonLayout.addComponent(btnEdit);
			buttonLayout.addComponent(btnDelete);
			
			userCustomLayout.addComponent(lblUserIDValue, "lblLetterCodeValue" + i);
			userCustomLayout.addComponent(lblLoginValue, "lblPassDueDateValue" + i);
			userCustomLayout.addComponent(lblPasswordValue, "lblResultValue" + i);
			userCustomLayout.addComponent(lblBranchValue, "lblDetailValue" + i);
			userCustomLayout.addComponent(lblDivisionValue, "lblDivisionValue" + i);
			userCustomLayout.addComponent(lblCompanyNameValue, "lblCompanyNameValue" + i);
			userCustomLayout.addComponent(lblFirstNameValue, "lblFirstNameValue" + i);
			userCustomLayout.addComponent(lblLastNameValue, "lblLastNameValue" + i);
			userCustomLayout.addComponent(lblDOBValue, "lblDOBValue" + i);
			userCustomLayout.addComponent(lblPhoneNumberValue, "lblPhoneNumberValue" + i);
			userCustomLayout.addComponent(lblEmailValue, "lblEmailValue" + i);
			userCustomLayout.addComponent(lblAddressValue, "lblAddressValue" + i);
			userCustomLayout.addComponent(lblBankNameValue, "lblBankNameValue" + i);
			userCustomLayout.addComponent(lblBankAccountValue, "lblBankAccountValue" + i);
			userCustomLayout.addComponent(lblDateAddedValue, "lblDateAddedValue" + i);
			userCustomLayout.addComponent(buttonLayout, "btnLayout" + i);
		}
		
		userTemplate += CLOSE_TABLE;
		userCustomLayout.setTemplateContents(userTemplate);
		
		return userCustomLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Window windowUserStatus() {
		final Window windowUserStatus = new Window();
		windowUserStatus.setModal(true);
		windowUserStatus.setResizable(false);
	    windowUserStatus.setWidth(800, Unit.PIXELS);
	    windowUserStatus.setHeight(600, Unit.PIXELS);
	    windowUserStatus.setCaption(I18N.message("user"));
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -6084334480090455821L;

			public void buttonClick(ClickEvent event) {
				windowUserStatus.close();
            }
        });
        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -4516850702053533588L;

			public void buttonClick(ClickEvent event) {
				windowUserStatus.close();
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));	
		
		TextField txtOA = ComponentFactory.getTextField("oa", false, 60, 150);
		TextField txtCompanyName = ComponentFactory.getTextField("company.name", false, 60, 150);
		TextField txtEmail = ComponentFactory.getTextField("email", false, 60, 150);
		TextField txtAddress = ComponentFactory.getTextField("address", false, 60, 150);
		TextField txtPhoneNumber = ComponentFactory.getTextField("phone.number", false, 60, 150);
		ComboBox cbxStatus = ComponentFactory.getComboBox("status", null);
		TextArea txtDetail = ComponentFactory.getTextArea("remark", false, 230, 100);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(txtOA);
		formLayout.addComponent(txtCompanyName);
		formLayout.addComponent(txtEmail);
		formLayout.addComponent(txtAddress);
		formLayout.addComponent(txtPhoneNumber);
		formLayout.addComponent(cbxStatus);
		formLayout.addComponent(txtDetail);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		VerticalLayout tabLayout = new VerticalLayout();
		tabLayout.setSpacing(true);
		tabLayout.setMargin(true);
		tabLayout.addComponent(createTabsheet());
		
		verticalLayout.addComponent(tabLayout);
		
		windowUserStatus.setContent(verticalLayout);
		return windowUserStatus;
	}
	
	/**
	 * 
	 * @return
	 */
	private TabSheet createTabsheet() {
		TextField txtBankBook = ComponentFactory.getTextField("bank", false, 60, 150);
		TextField txtBranchBook = ComponentFactory.getTextField("branch", false, 60, 150);
		TextField txtAccountCode = ComponentFactory.getTextField("account.code", false, 60, 150);
		TextField txtAccountName = ComponentFactory.getTextField("account.name", false, 60, 150);
		AutoDateField dfApprovalDate = ComponentFactory.getAutoDateField("approval.date", false);
		AutoDateField dfExpiryDate = ComponentFactory.getAutoDateField("expire.date", false);
		TextField txtBank = ComponentFactory.getTextField("bank", false, 60, 150);
		TextField txtBranch = ComponentFactory.getTextField("branch", false, 60, 150);
		
		final TabSheet tabSheet = new TabSheet();
		
		final FormLayout bookFormLayout = new FormLayout();
		bookFormLayout.setSpacing(true);
		bookFormLayout.addComponent(txtBankBook);
		bookFormLayout.addComponent(txtBranchBook);
		bookFormLayout.addComponent(txtAccountCode);
		bookFormLayout.addComponent(txtAccountName);
		
		final FormLayout bankGuaranteeLayout = new FormLayout();
		bankGuaranteeLayout.setSpacing(true);
		bankGuaranteeLayout.addComponent(dfApprovalDate);
		bankGuaranteeLayout.addComponent(dfExpiryDate);
		bankGuaranteeLayout.addComponent(txtBank);
		bankGuaranteeLayout.addComponent(txtBranch);
		
		tabSheet.addTab(bookFormLayout, I18N.message("book"));
		tabSheet.addTab(bankGuaranteeLayout, I18N.message("bank.guarantee"));
		
		return tabSheet;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			UI.getCurrent().addWindow(windowUserStatus());
		} 
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
