package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Campaign Form Panel
 * @author bunlong.taing
 */
public class CampaignAssetPanel extends AbstractTabPanel {
	/** */
	private static final long serialVersionUID = -7199618958188327308L;
	
	private TabSheet tabCampaign;

	private CampaignAssetFormPanel campaignAssetFormPanel;
	
	private Long campaignId;
		
	public CampaignAssetPanel() {
		super();
		setCaption(I18N.message("asset"));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		campaignAssetFormPanel = new CampaignAssetFormPanel();
		tabCampaign = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(tabCampaign);
		
		return contentLayout;
	}
	
	/**
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		this.campaignId = campaignId;
		displayTabs();
		campaignAssetFormPanel.assignValues(campaignId);
		tabCampaign.setSelectedTab(campaignAssetFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		campaignId = null;
		campaignAssetFormPanel.assignValues(null);
	}
	
	/** */
	private void displayTabs() {
			tabCampaign.addTab(campaignAssetFormPanel, I18N.message("asset.models"));
	}
}
