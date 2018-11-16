package com.nokor.ersys.vaadin.ui.security;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Login with Multi-profiles management
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LoginPanel.NAME)
public class LoginPanel extends BaseLoginPanel {
	/** */
	private static final long serialVersionUID = -968811483008156094L;

	private VerticalLayout mainPanel;

	/**
	 * 
	 */
	public LoginPanel() {
		setSizeFull();
		setSpacing(true);
		setStyleName("login-bg");
		setImmediate(true);
		
		initView();
	}

	@PostConstruct
	public void PostConstruct() {
		super.PostConstruct();
	}


	/**
	 * @return the multiProfiles
	 */
	protected boolean isMultiProfiles() {
		return true;
	}
	
	/**
	 * @see com.nokor.ersys.vaadin.ui.security.BaseLoginPanel#getMainPanel()
	 */
	@Override
	protected AbstractLayout getMainPanel() {
		if (mainPanel == null) {
			createMainPanel();
		}
		return mainPanel;
	}
	
	/**
	 * 
	 */
	protected void initView() {
		setTxtUserName(ComponentFactory.getTextField(I18N.message("lbl.username"), true, 25, 200F));
		getTxtUserName().setImmediate(true);
		setTxtPassword(ComponentFactory.getPasswordField(I18N.message("lbl.password")));
		getTxtPassword().setImmediate(true);
		getTxtPassword().setRequired(true);
		getTxtPassword().setWidth(200F, Unit.PIXELS);
		
		Button btnLogin = new Button(I18N.message("login"));
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnLogin.setIcon(FontAwesome.SIGN_IN);
		}
		btnLogin.setClickShortcut(KeyCode.ENTER);
		btnLogin.addClickListener(this);
		setBtnLogin(btnLogin);
		
		addComponent(getMainPanel());
		setComponentAlignment(getMainPanel(), Alignment.MIDDLE_CENTER);
		
		addComponent(getProfileListLayout());
		setComponentAlignment(getProfileListLayout(), Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * 
	 */
	protected void createMainPanel() {
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setStyleName("fields");
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addComponent(getTxtUserName());
		formLayout.addComponent(getTxtPassword());
		formLayout.addComponent(getBtnLogin());
		formLayout.setComponentAlignment(getBtnLogin(), Alignment.BOTTOM_CENTER);
		
		mainPanel = new VerticalLayout();
		mainPanel.setStyleName("login-panel");
		mainPanel.setSpacing(true);
		mainPanel.setMargin(true);
		mainPanel.setWidth(MAIN_WIDTH);
		mainPanel.addComponent(getHeaderLayout());
		mainPanel.addComponent(formLayout);
		
	}
	
	/**
	 * @see com.nokor.ersys.vaadin.ui.security.BaseLoginPanel#createHeaderLayout()
	 */
	@Override
	protected void createHeaderLayout() {
		super.createHeaderLayout();
	}
	
	/**
	 * @see com.nokor.ersys.vaadin.ui.security.BaseLoginPanel#createProfileLayoutListView()
	 */
	@Override
	protected void createProfileLayoutListView() {
		super.createProfileLayoutListView();
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		super.buttonClick(event);
	}
	
	/**
	 * @see com.nokor.ersys.vaadin.ui.security.BaseLoginPanel#manageMultiProfiles()
	 */
	@Override
	protected void manageMultiProfiles() {
		super.manageMultiProfiles();
	}
	
	/**
	 * 
	 */
	@Override
	protected void gotoNextPanel(BaseMainUI mainUI) {
		super.gotoNextPanel(mainUI);
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		super.enter(event);
	}

}
