package com.nokor.efinance.gui.ui.panel.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author ly.youhort
 */
public class PasswordPanel extends Window {

	private static final long serialVersionUID = -3257822869747884791L;

	protected SecurityService securityService = (SecurityService) SecApplicationContextHolder.getContext().getBean("securityService");
	
	private PasswordField txtOldPassword;
	private PasswordField txtNewPassword;
	private PasswordField txtConfirmPassword;
	
	public PasswordPanel() {
		setModal(true);		
		setCaption(I18N.message("change.password"));
		txtOldPassword = ComponentFactory.getPasswordField("old.password");
		txtOldPassword.setRequired(true);
		txtOldPassword.setMaxLength(60);
		txtOldPassword.setWidth(200, Unit.PIXELS);
		
		txtNewPassword = ComponentFactory.getPasswordField("new.password");
		txtNewPassword.setRequired(true);
		txtNewPassword.setMaxLength(60);
		txtNewPassword.setWidth(200, Unit.PIXELS);
		
		txtConfirmPassword = ComponentFactory.getPasswordField("confirm.password");
		txtConfirmPassword.setRequired(true);
		txtConfirmPassword.setMaxLength(60);
		txtConfirmPassword.setWidth(200, Unit.PIXELS);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		final VerticalLayout messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			private static final long serialVersionUID = 7657693632881547084L;

			public void buttonClick(ClickEvent event) {
				List<String> errors = new ArrayList<String>();
				
				if (StringUtils.isEmpty(txtOldPassword.getValue())) {
					errors.add(I18N.message("field.required.1", I18N.message("old.password")));
				}
				
				if (StringUtils.isEmpty(txtNewPassword.getValue())) {
					errors.add(I18N.message("field.required.1", I18N.message("new.password")));
				}
				
				if (StringUtils.isEmpty(txtConfirmPassword.getValue())) {
					errors.add(I18N.message("field.required.1", I18N.message("confirm.password")));
				}
				
				if (errors.isEmpty() && !txtNewPassword.getValue().equals(txtConfirmPassword.getValue())) {
					errors.add(I18N.message("password.not.confirm"));
				}
				
				if (errors.isEmpty()) {
					SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					try {
						securityService.changePassword(secUser, txtOldPassword.getValue(), txtNewPassword.getValue());
					} catch (BadCredentialsException e) {
						errors.add(I18N.message("message.old.password.not.match"));
					}
				}
				
				if (!errors.isEmpty()) {
					messagePanel.removeAllComponents();
					for (String error : errors) {
						Label messageLabel = new Label(error);
						messageLabel.addStyleName("error");
						messagePanel.addComponent(messageLabel);
					}
					messagePanel.setVisible(true);
				} else {
					Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
					notification.setDescription(I18N.message("success.change.password"));
					notification.setDelayMsec(3000);
					notification.show(Page.getCurrent());
					close();
				}
            }
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
            	close();
            }
		});
		
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addComponent(txtOldPassword);
		formLayout.addComponent(txtNewPassword);
		formLayout.addComponent(txtConfirmPassword);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(formLayout);
		setContent(contentLayout);
	}
}
