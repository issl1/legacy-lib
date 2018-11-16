package com.nokor.efinance.ra.ui.panel.asset.range;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetRangeFormPanel extends AbstractFormPanel implements FinServicesHelper, SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -6978374607842167163L;

	private AssetRange assetRange;
	
	private TabSheet tabAssetRange;

	private AssetRangesPanel mainPanel;
	
	private AssetRangeDetailPanel assetRangeDetailPanel;
	private AssetRangeModelTablePanel assetRangeModelTablePanel;
	
	@Autowired
	private AssetRangeModelFormPanel formPanel;
    
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
		tabAssetRange = new TabSheet();		
		tabAssetRange.addSelectedTabChangeListener(this);
		assetRangeDetailPanel = new AssetRangeDetailPanel(this);
		
		tabAssetRange.addTab(assetRangeDetailPanel, I18N.message("detail"));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(tabAssetRange);
		
		return contentLayout;
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabAssetRange.getSelectedTab().equals(assetRangeDetailPanel) || tabAssetRange.getSelectedTab().equals(assetRangeModelTablePanel)) {
			if (tabAssetRange.getComponentCount() == 3) {
				tabAssetRange.removeComponent(formPanel);
				assetRangeModelTablePanel.assignValues(this.assetRange.getId());
			} 
		}
	}
	
	/**
	 * @param asranId
	 */
	public void assignValues(Long asranId) {
		super.reset();
		if (asranId != null) {
			this.assetRange = ASS_RANGE_SRV.getById(AssetRange.class, asranId);
			assetRangeDetailPanel.assignValues(this.assetRange);
			
			if (tabAssetRange.getComponentCount() <= 1) {
				assetRangeModelTablePanel = new AssetRangeModelTablePanel(this);
				tabAssetRange.addTab(assetRangeModelTablePanel, I18N.message("asset.models"));
			}
		}
		if (assetRangeModelTablePanel != null) {
			assetRangeModelTablePanel.assignValues(this.assetRange.getId());		
			tabAssetRange.setSelectedTab(assetRangeDetailPanel);
		}
	}
	
	/**
	 * 
	 * @param assModelId
	 * @param assModelId
	 */
	public void addForm(Long assRangeId, Long assModelId) {
		formPanel.assignValues(assRangeId, assModelId);
		tabAssetRange.addTab(formPanel, I18N.message("asset.model"));
		tabAssetRange.setSelectedTab(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.assetRange = AssetRange.createInstance();
		tabAssetRange.setSelectedTab(assetRangeDetailPanel);
		if (tabAssetRange.getComponentCount() == 2 && this.assetRange.getId() == null) {
			tabAssetRange.removeTab(tabAssetRange.getTab(1));
		}
		assetRangeDetailPanel.reset();
	}
	
	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(AssetRangesPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * 
	 * @param needRefresh
	 */
	public void setNeedRefresh(boolean needRefresh) {
		mainPanel.getTabSheet().setNeedRefresh(needRefresh);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}
	
}
