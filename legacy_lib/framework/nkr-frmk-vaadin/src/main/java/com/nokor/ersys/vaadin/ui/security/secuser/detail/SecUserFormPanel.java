package com.nokor.ersys.vaadin.ui.security.secuser.detail;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.ersys.vaadin.ui.security.secuser.detail.profilegroup.SecProfileGroupPanel;
import com.nokor.ersys.vaadin.ui.security.secuser.list.SecUserHolderPanel;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * UserFormPanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SecUserFormPanel extends AbstractFormPanel implements VaadinServicesHelper {
	
	/**	 */
	private static final long serialVersionUID = -3669377571583826481L;
	private SecUser user;
	
	private SecUserHolderPanel mainPanel;
	protected FormLayout formDetailPanel;
	
	private CheckBox cbActive;
	private TextField txtDescLogin;
	private TextField txtLogin;
	private TextField txtEmail;
	private PasswordField txtPassword;
	private PasswordField txtConfirmPassword;
	
	private SecProfileGroupPanel profileGroupLayout; 
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("delear.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
				
		txtDescLogin = ComponentFactory.getTextField("name.en", true, 60, 200);
		txtLogin = ComponentFactory.getTextField("login", true, 60, 200);
		txtPassword = ComponentFactory.getPasswordField("password");
		txtPassword.setRequired(true);
		txtPassword.setMaxLength(60);
		txtPassword.setWidth(200, Unit.PIXELS);
		txtConfirmPassword = ComponentFactory.getPasswordField("confirm.password");
		txtConfirmPassword.setRequired(true);
		txtConfirmPassword.setMaxLength(60);
		txtConfirmPassword.setWidth(200, Unit.PIXELS);
		txtEmail = ComponentFactory.getTextField("email", false, 60, 200);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);

        formDetailPanel = new FormLayout();
        formDetailPanel.setMargin(true);
        formDetailPanel.setSizeFull();
        formDetailPanel.addComponent(txtDescLogin);
        formDetailPanel.addComponent(txtLogin);
        formDetailPanel.addComponent(txtPassword);
        formDetailPanel.addComponent(txtConfirmPassword);
        formDetailPanel.addComponent(txtEmail);
        formDetailPanel.addComponent(cbActive);    
        
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.addComponent(formDetailPanel);
        
        profileGroupLayout = new SecProfileGroupPanel();
        mainLayout.addComponent(profileGroupLayout);

        Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setContent(mainLayout);
		
		return mainPanel;
	}


	/**
	 * @param asmakId
	 */
	public void assignValues(Long id) {
		reset();
		if (id != null) {
			user = ENTITY_SRV.getById(SecUser.class, id);
			txtDescLogin.setValue(user.getDesc());
			txtLogin.setValue(user.getLogin());
			txtEmail.setValue(getDefaultString(user.getEmail()));
			cbActive.setValue(user.getStatusRecord() == EStatusRecord.ACTIV);

			profileGroupLayout.setSelectProfiles(user.getProfiles());
			txtLogin.addStyleName("blackdisabled");
			txtLogin.setEnabled(false);
		}
	}
	
	@Override
	protected SecUser getEntity() {
		user.setDesc(txtDescLogin.getValue());
		user.setLogin(txtLogin.getValue());
		user.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		user.setEmail(txtEmail.getValue());
		user.setProfiles(profileGroupLayout.getSelectedProfiles());
		return user;
	}
	
	@Override
	public void saveEntity() {
		SecUser secUser = getEntity();
		if (secUser != null && secUser.getId() != null) {
			SECURITY_SRV.update(secUser, txtPassword.getValue());
		} else {
			secUser = SECURITY_SRV.createUser(secUser, txtPassword.getValue());
			mainPanel.addSubTab(secUser.getId());
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();

		user = SecurityEntityFactory.createSecUserInstance(UserSessionManager.getCurrentUser().getLogin());
		txtDescLogin.setValue("");
		txtLogin.setValue("");
		txtLogin.removeStyleName("blackdisabled");
		txtLogin.setEnabled(true);
		txtEmail.setValue("");
		txtPassword.setValue("");
		txtConfirmPassword.setValue("");
		cbActive.setValue(true);
		
		profileGroupLayout.clearValues();
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescLogin, "name.en");
		if (user.getId() == null) {
			checkMandatoryField(txtPassword, "password");
		}
		
		if (StringUtils.isNotEmpty(txtPassword.getValue())) {
			checkMandatoryField(txtConfirmPassword, "confirm.password");
			if (StringUtils.isNotEmpty(txtConfirmPassword.getValue())
					&& !txtPassword.getValue().equals(txtConfirmPassword.getValue())) {
				errors.add("password.not.confirm");
			}
		}		

		if (user.getId() == null) {
			String login = txtLogin.getValue();
			boolean isUserAlreadyExist = isUserExist(login);
			if (isUserAlreadyExist) {
				errors.add(I18N.message("msg.info.user.already.exist"));
			}
		}
		if (errors.isEmpty()) {
			if (!profileGroupLayout.validate()) {
				errors.add(profileGroupLayout.getRequireMsg());
			}
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @param userLogin
	 * @return
	 */
	private boolean isUserExist(String login) {
		SecUser secUser = SECURITY_SRV.loadUserByUsername(login);
		return secUser != null;
	}
	
	/**
	 * Set Main Panel
	 * @param mainPanel
	 */
	public void setMainPanel(SecUserHolderPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
}
