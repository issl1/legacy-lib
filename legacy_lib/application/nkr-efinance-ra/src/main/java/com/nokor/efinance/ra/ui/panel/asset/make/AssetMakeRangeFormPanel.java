package com.nokor.efinance.ra.ui.panel.asset.make;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetMakeRangeFormPanel extends AbstractFormPanel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = -5608252346337467935L;

	private AssetRange assetRange;
	
	private CheckBox cbActive;
	private TextField txtCode;
    private TextField txtDescEn;
    private EntityRefComboBox<AssetMake> cbxAssetMake;
    
    private Button btnSaveAdd;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("asset.make.create"));
        btnSaveAdd = new NativeButton(I18N.message("save.add"), this);
        btnSaveAdd.setIcon(FontAwesome.SAVE);
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnSaveAdd);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		assetRange.setCode(txtCode.getValue());
		assetRange.setDesc(txtDescEn.getValue());
		assetRange.setDescEn(txtDescEn.getValue());
		assetRange.setAssetMake(cbxAssetMake.getSelectedEntity());
		assetRange.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetRange;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		AssetRange assetRange = (AssetRange) getEntity();
		ASS_RANGE_SRV.saveOrUpdateAssetRange(assetRange);
		assignValues(cbxAssetMake.getSelectedEntity().getId(), assetRange.getId());
		displaySuccess();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		formPanel.setStyleName("myform-align-left");
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);
		txtCode.setEnabled(false);
		txtDescEn = ComponentFactory.getTextField("model.name", true, 60, 200);	   
        cbxAssetMake = new EntityRefComboBox<AssetMake>(I18N.message("asset.make"));
        cbxAssetMake.setRequired(true);
        cbxAssetMake.setWidth(200, Unit.PIXELS);
        cbxAssetMake.setEnabled(false);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(cbxAssetMake);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * 
	 * @param asmakeId
	 * @param asranId
	 */
	public void assignValues(Long asmakeId, Long asranId) {
		this.reset();
		if (asmakeId != null) {
			cbxAssetMake.setRestrictions(new BaseRestrictions<AssetMake>(AssetMake.class));
	        cbxAssetMake.renderer(); 
			cbxAssetMake.setSelectedEntity(ASS_MAKE_SRV.getById(AssetMake.class, asmakeId));
		}
		if (asranId != null) {
			assetRange = ENTITY_SRV.getById(AssetRange.class, asranId);
			txtCode.setValue(assetRange.getCode());
			txtDescEn.setValue(assetRange.getDescEn());
			cbActive.setValue(assetRange.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		assetRange = new AssetRange();
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbActive.setValue(true);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		errors.clear();
		checkMandatoryField(txtDescEn, "model.name");
		checkMandatorySelectField(cbxAssetMake, "asset.make");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSaveAdd) {
			if (validate()) {
				saveEntity();
				reset();
			} else {
				displayErrors();
			}
		}
	}
}
