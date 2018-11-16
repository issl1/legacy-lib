package com.nokor.ersys.vaadin.ui.security.sys;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.security.LoginPanel;
import com.nokor.frmk.security.service.AuthenticationServiceAware;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SysAdminLoginPanel.SA_NAME)
public class SysAdminLoginPanel extends LoginPanel {
	/** */
	private static final long serialVersionUID = -775759766916297713L;

	public static final String SA_NAME = "salogin";

	/**
	 * 
	 */
	public SysAdminLoginPanel() {
		super();
	}

	@PostConstruct
	public void PostConstruct() {
		super.PostConstruct();
	}
	
	/**
	 * 
	 */
	protected void initView() {
		super.initView();
	}
	
	@Override
	protected void createMainPanel() {
//		if (!UserSessionManager.isAuthenticated() && !UserSessionManager.getCurrentUser().isAdmin()) {
//			VerticalLayout mainPanel = new VerticalLayout();
//			mainPanel.setStyleName("login-panel");
//			mainPanel.setSpacing(true);
//			mainPanel.setMargin(true);
//			mainPanel.setWidth(MAIN_WIDTH);
//			mainPanel.addComponent(getHeaderLayout());
//			
//			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
//					MessageBox.Icon.INFO, I18N.message("please.select.profile"),
//					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
//			mb.setWidth("250px");
//			mb.setHeight("145px");
//			mb.show();
//			return;
//		}
		
		super.createMainPanel();
	}

	/**
	 * @return the multiProfiles
	 */
	protected boolean isMultiProfiles() {
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	protected AuthenticationServiceAware getAuthenticationService() {
		return SYS_AUTH_SRV;
	}
	
}
