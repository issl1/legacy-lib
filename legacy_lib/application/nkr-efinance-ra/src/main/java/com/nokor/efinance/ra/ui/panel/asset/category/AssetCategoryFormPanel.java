package com.nokor.efinance.ra.ui.panel.asset.category;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetCategoryFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = -4276155516661087487L;

	private AssetCategory assetCategory;
	
	private CheckBox cbActive;
    private TextField txtDescEn;
    
    /**
     * 
     */
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
		assetCategory.setDesc(txtDescEn.getValue());
		assetCategory.setDescEn(txtDescEn.getValue());
		assetCategory.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetCategory;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtDescEn = ComponentFactory.getTextField("name", true, 60, 180);        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        
        FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
        frmLayout.setMargin(new MarginInfo(false, false, false, true));
        frmLayout.addComponent(txtDescEn);
        frmLayout.addComponent(cbActive);
        
        return frmLayout;
	}
	
	/**
	 * @param assCatId
	 */
	public void assignValues(Long assCatId) {
		super.reset();
		if (assCatId != null) {
			assetCategory = ENTITY_SRV.getById(AssetCategory.class, assCatId);
			txtDescEn.setValue(assetCategory.getDescEn());
			cbActive.setValue(assetCategory.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		assetCategory = new AssetCategory();
		txtDescEn.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "name.en");		
		return errors.isEmpty();
	}
}
