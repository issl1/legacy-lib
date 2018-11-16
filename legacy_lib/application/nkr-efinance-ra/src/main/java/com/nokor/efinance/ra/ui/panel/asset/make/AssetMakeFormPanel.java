package com.nokor.efinance.ra.ui.panel.asset.make;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetMakeFormPanel extends AbstractFormPanel implements FinServicesHelper, SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = 3009097491814863392L;
	
	private AssetMake assetMake;
	
	private TabSheet tabAssetMake;

	private AssetMakesPanel mainPanel;
	
	private AssetMakeDetailPanel assetMakeDetailPanel;
	private AssetMakeRangeTablePanel assetMakeRangeTablePanel;
	
	@Autowired
	private AssetMakeRangeFormPanel formPanel;

	/**
	 * @return the formPanel
	 */
	public AssetMakeRangeFormPanel getFormPanel() {
		return formPanel;
	}

	/**
	 * @return the tabAssetMake
	 */
	public TabSheet getTabAssetMake() {
		return tabAssetMake;
	}
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabAssetMake = new TabSheet();		
		tabAssetMake.addSelectedTabChangeListener(this);
		assetMakeDetailPanel = new AssetMakeDetailPanel(this);
		
		tabAssetMake.addTab(assetMakeDetailPanel, I18N.message("detail"));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(tabAssetMake);
		
		return contentLayout;
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabAssetMake.getSelectedTab().equals(assetMakeDetailPanel) || tabAssetMake.getSelectedTab().equals(assetMakeRangeTablePanel)) {
			if (tabAssetMake.getComponentCount() == 3) {
				tabAssetMake.removeComponent(formPanel);
				assetMakeRangeTablePanel.assignValues(this.assetMake.getId());
			} 
		}
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long asmakId) {
		super.reset();
		if (asmakId != null) {
			this.assetMake = ENTITY_SRV.getById(AssetMake.class, asmakId);
			assetMakeDetailPanel.assignValues(this.assetMake);
			
			if (tabAssetMake.getComponentCount() <= 1) {
				assetMakeRangeTablePanel = new AssetMakeRangeTablePanel(this);
				tabAssetMake.addTab(assetMakeRangeTablePanel, I18N.message("asset.ranges"));
			}
		}
		if (assetMakeRangeTablePanel != null) {
			assetMakeRangeTablePanel.assignValues(this.assetMake.getId());		
			tabAssetMake.setSelectedTab(assetMakeDetailPanel);
		}
	}
	
	/**
	 * 
	 * @param assMakeId
	 * @param assRangeId
	 */
	public void addForm(Long assMakeId, Long assRangeId) {
		formPanel.assignValues(assMakeId, assRangeId);
		tabAssetMake.addTab(formPanel, I18N.message("asset.range"));
		tabAssetMake.setSelectedTab(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.assetMake = AssetMake.createInstance();
		tabAssetMake.setSelectedTab(assetMakeDetailPanel);
		if (tabAssetMake.getComponentCount() == 2 && this.assetMake.getId() == null) {
			tabAssetMake.removeTab(tabAssetMake.getTab(1));
		}
		assetMakeDetailPanel.reset();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {	
		return errors.isEmpty();
	}
	
	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(AssetMakesPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * 
	 * @param needRefresh
	 */
	public void setNeedRefresh(boolean needRefresh) {
		mainPanel.getTabSheet().setNeedRefresh(needRefresh);
	}
}
