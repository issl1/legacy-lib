package com.nokor.efinance.ra.ui.panel.asset.exteriorstatus;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetExteriorStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Asset exterior status form panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetExteriorStatusFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 7716137197520816548L;

	private AssetExteriorStatus assetExteriorStatus;
	
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
		assetExteriorStatus.setDesc(txtDesc.getValue());
		assetExteriorStatus.setDescEn(txtDescEn.getValue());
		assetExteriorStatus.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetExteriorStatus;
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
	 * @param assetExteriorStatusId
	 */
	public void assignValues(Long assetExteriorStatusId) {
		super.reset();
		if (assetExteriorStatusId != null) {
			assetExteriorStatus = ENTITY_SRV.getById(AssetExteriorStatus.class, assetExteriorStatusId);
			txtDescEn.setValue(assetExteriorStatus.getDescEn());
			txtDesc.setValue(assetExteriorStatus.getDesc());
			cbActive.setValue(assetExteriorStatus.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		assetExteriorStatus = new AssetExteriorStatus();
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
