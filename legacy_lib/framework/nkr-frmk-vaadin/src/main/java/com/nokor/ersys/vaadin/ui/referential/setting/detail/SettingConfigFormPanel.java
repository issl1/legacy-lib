/*
 * Created on 29/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.setting.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * SettingConfig Form Panel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SettingConfigFormPanel extends AbstractFormPanel implements AppServicesHelper {

	/**	 */
	private static final long serialVersionUID = 8581198351134582843L;

	private SettingConfig settingConfig;
    private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtValue;
	private TextField txtCode;
    private CheckBox cbActive;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		txtCode = ComponentFactory.getTextField("code", true, 200, 350);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		txtValue = ComponentFactory.getTextField("value", true, 100, 150);
		txtValue.setRequired(true);
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);
		formPanel.addComponent(txtCode);
	    formPanel.addComponent(txtDesc);
	    formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtValue);
        formPanel.addComponent(cbActive);
        
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(formPanel);
        
        Panel mainPanel = ComponentFactory.getPanel();        
		mainPanel.setContent(verticalLayout);        
		return mainPanel;
	}

	@Override
	protected Entity getEntity() {
		settingConfig.setCode(txtCode.getValue());
		settingConfig.setDescEn(txtDescEn.getValue());
		settingConfig.setDesc(txtDesc.getValue());
		settingConfig.setValue(txtValue.getValue());
		settingConfig.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return settingConfig;
	}
	
	/**
	 * 
	 * @param settingId
	 */
	public void assignValues(Long settingId) {
		super.reset();
		if (settingId != null) {
			settingConfig = ENTITY_SRV.getById(SettingConfig.class, settingId);
			txtCode.setValue(settingConfig.getCode());
			txtDescEn.setValue(settingConfig.getDescEn());
			txtDesc.setValue(settingConfig.getDesc());
			txtValue.setValue(settingConfig.getValue());
			txtCode.setEnabled(!settingConfig.isReadOnly());
			txtValue.setEnabled(!settingConfig.isReadOnly());
			cbActive.setValue(settingConfig.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Reset value
	 */
	@Override
	public void reset() {
		super.reset();
		settingConfig = new SettingConfig();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtValue.setValue("");
		cbActive.setValue(true);
		markAsDirty();
	}
	
	/**
	 * 
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDesc, "desc");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryField(txtValue, "value");
		return errors.isEmpty();
	}
}
