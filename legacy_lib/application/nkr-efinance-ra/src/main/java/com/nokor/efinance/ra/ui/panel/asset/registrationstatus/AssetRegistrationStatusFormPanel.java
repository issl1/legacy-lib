package com.nokor.efinance.ra.ui.panel.asset.registrationstatus;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetRegistrationStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Asset registration status form panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetRegistrationStatusFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 3009853062906198411L;

	private AssetRegistrationStatus assetRegistrationStatus;
	
	private CheckBox cbActive;
	private TextField txtDesc;
    private TextField txtDescEn;
    
    /** */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		assetRegistrationStatus.setDesc(txtDesc.getValue());
		assetRegistrationStatus.setDescEn(txtDescEn.getValue());
		assetRegistrationStatus.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetRegistrationStatus;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();			
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);		
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);      
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * @param assetRegistrationStatusId
	 */
	public void assignValues(Long assetRegistrationStatusId) {
		super.reset();
		if (assetRegistrationStatusId != null) {
			assetRegistrationStatus = ENTITY_SRV.getById(AssetRegistrationStatus.class, assetRegistrationStatusId);
			txtDescEn.setValue(assetRegistrationStatus.getDescEn());
			txtDesc.setValue(assetRegistrationStatus.getDesc());
			cbActive.setValue(assetRegistrationStatus.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		assetRegistrationStatus = new AssetRegistrationStatus();
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbActive.setValue(true);
		markAsDirty();
	}
	
	/**
	 * Validate control
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
		
	}
}
