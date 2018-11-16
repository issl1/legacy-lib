package com.nokor.ersys.vaadin.ui.security.secuser;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.security.authentication.BadCredentialsException;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author peng.leng
 *
 */
public class UserProfileFormPopupPanel extends Window implements FrmkServicesHelper {
	/** */
	private static final long serialVersionUID = 63310696565790577L;

	private TabSheet mainTabSheet;

	private TextField txtName;
	private TextField txtLogin;
	private TextField txtEmail;
	private TextField txtLastUpdate;
	
	private PasswordField txtPassword;
	private PasswordField txtNewPassword;
	private PasswordField txtConfirmPassword;
	
	private SecUser secUser;
	
	/**
	 * 
	 */
	public UserProfileFormPopupPanel() {
		super(I18N.message("my.account"));
		
		setModal(true);
		setResizable(false);
		setWidth("450px");
		setHeight("257px");
		
		initMainTabSheet();
	}
	
	/**
	 * 
	 */
	public void open() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		setSecUser(secUser);
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 * 
	 */
	protected void initMainTabSheet() {
		mainTabSheet = new TabSheet();
		mainTabSheet.setSizeFull();
		
		mainTabSheet.addTab(createSecUserInfo(), I18N.message("user.info"));
		mainTabSheet.addTab(createSecUserPassword(), I18N.message("change.password"));
		
		setContent(mainTabSheet);
	}
	
	/**
	 * 
	 * @param secUser
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
		
		if (secUser != null) {
			txtLogin.setValue(secUser.getLogin());
			txtName.setValue(secUser.getDesc());
			txtEmail.setValue(secUser.getEmail());
			txtLastUpdate.setValue(DateUtils.getDateLabel(secUser.getUpdateDate(), "dd-MM-yyyy hh:mm:ss"));
		}
		txtNewPassword.setValue("");
		txtPassword.setValue("");
		txtConfirmPassword.setValue("");
	}
	
	/**
	 * 
	 * @return
	 */
	protected VerticalLayout createSecUserInfo() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		txtLogin = ComponentFactory.getTextField("login", true, 60, 200);
		txtLogin.setEnabled(false);
		txtName = ComponentFactory.getTextField("name.en", true, 60, 200);
		txtEmail = ComponentFactory.getTextField("email", false, 60, 200);
		txtLastUpdate = ComponentFactory.getTextField("date.last.modify", false, 60, 200);
		txtLastUpdate.setEnabled(false);

        final FormLayout formDetailPanel = new FormLayout();
        formDetailPanel.setWidth("100%");
        formDetailPanel.addComponent(txtLogin);
        formDetailPanel.addComponent(txtName);
        formDetailPanel.addComponent(txtEmail);
        formDetailPanel.addComponent(txtLastUpdate);        
        
        Button btnSave = new NativeButton(I18N.message("save"));
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	btnSave.setIcon(FontAwesome.SAVE);
        }
        else {
        	btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/disk.png"));
        }
        
        /**
         * On save
         */
        btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1421820964698562975L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (StringUtils.isEmpty(txtName.getValue())) {
					showMessageBox(I18N.message("field.required.1", I18N.message("name.en")));
				}
				else {
					secUser.setDesc(txtName.getValue());
					secUser.setEmail(txtEmail.getValue());
					ENTITY_SRV.saveOrUpdate(secUser);					
					showSuccessMessage(I18N.message("msg.info.save.successfully"));
				}
			}
		});
        NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		mainLayout.addComponent(navigationPanel);
        mainLayout.addComponent(formDetailPanel);
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	protected VerticalLayout createSecUserPassword() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		txtPassword = ComponentFactory.getPasswordField("old.password");
		txtPassword.setRequired(true);
		txtPassword.setMaxLength(60);
		txtPassword.setWidth(200, Unit.PIXELS);
		txtNewPassword = ComponentFactory.getPasswordField("new.password");
		txtNewPassword.setRequired(true);
		txtNewPassword.setMaxLength(60);
		txtNewPassword.setWidth(200, Unit.PIXELS);
		txtConfirmPassword = ComponentFactory.getPasswordField("confirm.password");
		txtConfirmPassword.setRequired(true);
		txtConfirmPassword.setMaxLength(60);
		txtConfirmPassword.setWidth(200, Unit.PIXELS);

        final FormLayout formDetailPanel = new FormLayout();        
        formDetailPanel.setWidth("100%");
        formDetailPanel.addComponent(txtPassword);
        formDetailPanel.addComponent(txtNewPassword);
        formDetailPanel.addComponent(txtConfirmPassword);
        
        Button btnSave = new NativeButton(I18N.message("change"));
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	btnSave.setIcon(FontAwesome.SAVE);
        }
        else {
        	btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/disk.png"));
        }
        btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1421820964698562975L;

			@Override
			public void buttonClick(ClickEvent event) {
				String msg = "";
				
				if (StringUtils.isEmpty(txtPassword.getValue())) {
					msg = I18N.message("field.required.1", I18N.message("old.password"));
				}
				if (StringUtils.isEmpty(txtNewPassword.getValue())) {
					if (!StringUtils.isEmpty(msg)) {
						msg += "<br/>";
					}
					msg += I18N.message("field.required.1", I18N.message("new.password"));
				}
				if (StringUtils.isEmpty(msg)) {
					if (!txtNewPassword.getValue().equals(txtConfirmPassword.getValue())) {
						msg = I18N.message("msg.error.password.not.match");
					}
				}
				
				if (StringUtils.isEmpty(msg)) {
					try {
						SecurityService securityService = FrmkServicesHelper.SECURITY_SRV;
						securityService.changePassword(secUser, txtPassword.getValue(), txtNewPassword.getValue());
						
						txtNewPassword.setValue("");
						txtPassword.setValue("");
						txtConfirmPassword.setValue("");
						showSuccessMessage(I18N.message("msg.info.change.successfully"));
					}
					catch (BadCredentialsException ex) {
						showMessageBox(ex.getMessage());
					}
				}
				else {
					showMessageBox(msg);
				}
			}
		});
        
        NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		mainLayout.addComponent(navigationPanel);
        mainLayout.addComponent(formDetailPanel);
		return mainLayout;
	}
	
	/**
	 * 
	 * @param message
	 */
	private void showSuccessMessage(String message) {
		MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
				MessageBox.Icon.INFO, message,
				new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.setWidth("210px");
		mb.setHeight("145px");
		mb.show();
	}
	
	/**
	 * 
	 * @param message
	 */
	private void showMessageBox(String message) {
		MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("warning"),
				MessageBox.Icon.WARN, message,
				new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.setWidth("300px");
		mb.setHeight("145px");
		mb.show();
	}
}
