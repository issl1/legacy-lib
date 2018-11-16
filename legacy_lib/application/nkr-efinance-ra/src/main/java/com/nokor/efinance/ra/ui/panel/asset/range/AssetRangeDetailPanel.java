package com.nokor.efinance.ra.ui.panel.asset.range;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class AssetRangeDetailPanel extends AbstractTabPanel implements SaveClickListener, FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = -5605977022271804664L;

	private AssetRange assetRange;

	private CheckBox cbActive;
	private TextField txtCode;
    private TextField txtDescEn;
    private EntityRefComboBox<AssetMake> cbxAssetMake;
    
    private Button btnSaveAdd;
    
    private AssetRangeFormPanel delegate;   
    
    /**
     * @param delegate
     */
	public AssetRangeDetailPanel(AssetRangeFormPanel delegate) {
        super();
        this.delegate = delegate;
   	 	btnSaveAdd = new NativeButton(I18N.message("save.add"), this);
   	 	btnSaveAdd.setIcon(FontAwesome.SAVE);
   	 	NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnSaveAdd);
		addComponent(navigationPanel, 0);
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	private AssetRange getEntity() {
		assetRange.setCode(txtCode.getValue());
		assetRange.setDesc(txtDescEn.getValue());
		assetRange.setDescEn(txtDescEn.getValue());
		assetRange.setAssetMake(cbxAssetMake.getSelectedEntity());
		assetRange.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetRange;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		FormLayout formPanel = new FormLayout();	
		formPanel.setMargin(new MarginInfo(false, false, true, true));
		formPanel.setStyleName("myform-align-left");
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);
		txtCode.setEnabled(false);
		txtDescEn = ComponentFactory.getTextField("model.name", true, 60, 200);	  
        cbxAssetMake = new EntityRefComboBox<AssetMake>(I18N.message("asset.make"));
        cbxAssetMake.setRestrictions(new BaseRestrictions<AssetMake>(AssetMake.class));
        cbxAssetMake.setRequired(true);
        cbxAssetMake.renderer(); 
        cbxAssetMake.setWidth(200, Unit.PIXELS);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(cbxAssetMake);
        formPanel.addComponent(cbActive);
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(new Panel(formPanel));

		return content;
	}

	/**
	 * 
	 * @param assetRange
	 */
	public void assignValues(AssetRange assetRange) {
		super.removeMessagePanel();
		if (assetRange != null) {
			this.assetRange = assetRange;
			txtCode.setValue(assetRange.getCode());
			txtDescEn.setValue(assetRange.getDescEn());
			cbActive.setValue(assetRange.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbxAssetMake.setSelectedEntity(assetRange.getAssetMake());
		}
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		removeErrorsPanel();
		this.assetRange = AssetRange.createInstance();
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbActive.setValue(true);
		cbxAssetMake.setSelectedEntity(null);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	private boolean validate() {
		errors.clear();
		checkMandatoryField(txtDescEn, "model.name");
		checkMandatorySelectField(cbxAssetMake, "asset.make");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		if (validate()) {
			ASS_RANGE_SRV.saveOrUpdateAssetRange(getEntity());
			delegate.assignValues(this.assetRange.getId());
			delegate.setNeedRefresh(true);
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSaveAdd) {
			if (validate()) {
				ASS_RANGE_SRV.saveOrUpdateAssetRange(getEntity());
				delegate.setNeedRefresh(true);
				delegate.reset();
				displaySuccess();
			} else {
				displayErrorsPanel();
			}
		}
	}
}
