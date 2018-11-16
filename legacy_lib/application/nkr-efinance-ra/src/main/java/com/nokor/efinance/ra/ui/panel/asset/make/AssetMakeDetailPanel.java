package com.nokor.efinance.ra.ui.panel.asset.make;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.EFinAssetType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class AssetMakeDetailPanel extends AbstractTabPanel implements SaveClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -1150346906107328187L;

	private AssetMake assetMake;

	private CheckBox cbActive;
	private TextField txtCode;
    private TextField txtDescEn;
    
    private AssetMakeFormPanel delegate;   
    
    /**
     * @param delegate
     */
	public AssetMakeDetailPanel(AssetMakeFormPanel delegate) {
        super();
        this.delegate = delegate;
        NavigationPanel navigationPanel = new NavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   		addComponent(navigationPanel, 0);
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	private AssetMake getEntity() {
		assetMake.setCode(txtCode.getValue());
		assetMake.setDesc(txtDescEn.getValue());
		assetMake.setDescEn(txtDescEn.getValue());
		assetMake.setAssetType(EFinAssetType.MOTO);
		assetMake.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetMake;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		formPanel.setStyleName("myform-align-left");
		formPanel.setMargin(new MarginInfo(false, false, true, true));
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);	
		txtCode.setEnabled(false);
		txtDescEn = ComponentFactory.getTextField("brand.name", true, 60, 200);    	
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(cbActive);
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(new Panel(formPanel));

		return content;
	}

	/**
	 * 
	 * @param assetMake
	 */
	public void assignValues(AssetMake assetMake) {
		super.removeMessagePanel();
		if (assetMake != null) {
			this.assetMake = assetMake;
			txtCode.setValue(assetMake.getCode());
			txtDescEn.setValue(assetMake.getDescEn());
			cbActive.setValue(assetMake.getStatusRecord().equals(EStatusRecord.ACTIV));
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
		this.assetMake = AssetMake.createInstance();
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	private boolean validate() {
		errors.clear();
		checkMandatoryField(txtDescEn, "brand.name");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		if (validate()) {
			ASS_MAKE_SRV.saveOrUpdateAssetMake(getEntity());
			delegate.assignValues(this.assetMake.getId());
			delegate.setNeedRefresh(true);
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
	}
}
