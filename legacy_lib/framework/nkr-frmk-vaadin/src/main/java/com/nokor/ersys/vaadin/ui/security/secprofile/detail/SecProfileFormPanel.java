package com.nokor.ersys.vaadin.ui.security.secprofile.detail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.ersys.vaadin.ui.security.secprofile.detail.applicationgroup.SecApplicationGroupLayout;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * ProfileFormPanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SecProfileFormPanel extends AbstractFormPanel implements VaadinServicesHelper {
	/**	 */
	private static final long serialVersionUID = 56171179622435972L;

	private SecProfile profile;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private SecApplicationGroupLayout appGroupLayout;

    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		profile.setCode(txtCode.getValue());
		profile.setDesc(txtDesc.getValue());
		profile.setDescEn(txtDescEn.getValue());
		profile.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		profile.setApplications(appGroupLayout.getSelectedApplication());
		profile.setControlProfilePrivileges(appGroupLayout.getSelectedControlPrivileges());
		return profile;
	}
	
	@Override
	public void saveEntity() {
		SecProfile secProfile = (SecProfile) getEntity();
		if (secProfile != null && secProfile.getId() != null) {
			SECURITY_SRV.updateProfile(secProfile);
		}
		else {
			List<SecManagedProfile> managedProfiles = new ArrayList<SecManagedProfile>();
			SecManagedProfile managedProfile = SecManagedProfile.createInstance();
			managedProfile.setParent(UserSessionManager.getCurrentUser().getDefaultProfile());
			managedProfile.setChild(secProfile);
			managedProfiles.add(managedProfile);
			
			secProfile.setManagedProfiles(managedProfiles);
			SECURITY_SRV.createProfile(secProfile);
		}
		displaySuccess();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);      
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
        
		cbActive = new CheckBox(I18N.message("active"));
		
        cbActive.setValue(true);        

        final FormLayout formPanel = new FormLayout();
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(cbActive);

        VerticalLayout mainLayout = new VerticalLayout();        
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.addComponent(formPanel);
        
        appGroupLayout = new SecApplicationGroupLayout();
        mainLayout.addComponent(appGroupLayout);

        Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setContent(mainLayout);
		return mainPanel;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long proId) {
		reset();
		if (proId != null) {
			profile = ENTITY_SRV.getById(SecProfile.class, proId);
			txtCode.setValue(profile.getCode());
			txtDescEn.setValue(profile.getDescEn());
			txtDesc.setValue(profile.getDesc());
			cbActive.setValue(profile.getStatusRecord() == EStatusRecord.ACTIV);
			appGroupLayout.setSelectByProfile(profile);
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		profile = EntityFactory.createInstance(SecProfile.class);
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbActive.setValue(true);
		appGroupLayout.clearValues();
		
		markAsDirty();		
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		
		if (errors.isEmpty()) {
			boolean isCodeAlreadyExist = ENTITY_SRV.checkByCodeExcept(SecProfile.class, txtCode.getValue(), profile.getId());
			if (isCodeAlreadyExist) {
				errors.add(I18N.message("msg.info.code.already.exist"));
			}
		}
		if (errors.isEmpty()) {
			if (!appGroupLayout.validate()) {
				errors.addAll(appGroupLayout.getRequireMsg());
			}
		}
		return errors.isEmpty();
	}	
}
