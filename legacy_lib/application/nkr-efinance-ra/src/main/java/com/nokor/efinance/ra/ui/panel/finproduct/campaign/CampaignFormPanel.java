package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * Campaign Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CampaignFormPanel extends AbstractFormPanel implements SelectedTabChangeListener {
	/** */
	private static final long serialVersionUID = -7199618958188327308L;
	
	private TabSheet tabCampaign;
	private CampaignsPanel mainPanel;
	
	private CampaignGeneralFormPanel campaignGeneralFormPanel;
	private CampaignDealerFormPanel campaignDealerFormPanel;
	private CampaignAssetPanel campaignAssetPanel;
	
	private Long campaignId;
	
	/**
	 */
	@PostConstruct
	public void postContruct() {
		super.init();
		setCaption(I18N.message("marketing.campaign"));
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		campaignGeneralFormPanel = new CampaignGeneralFormPanel(this);
		campaignDealerFormPanel = new CampaignDealerFormPanel();
		campaignAssetPanel = new CampaignAssetPanel();
				
		/*campaignTermFormPanel.setCaption();
		campaignGradeFormPanel.setCaption();
		campaignDocumentTablePanel.setCaption();
		campaignDealerFormPanel.setCaption();
		campaignAssetFormPanel.setCaption();
		campaignAssetMakeFormPanel.setCaption(I18N.message("brand"));
		campaignAssetRangeFormPanel.setCaption(I18N.message("model"));*/
		
		tabCampaign = new TabSheet();
		tabCampaign.addSelectedTabChangeListener(this);
		tabCampaign.addTab(campaignGeneralFormPanel, I18N.message("general"));
		
		return tabCampaign;
	}
	
	/**
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		this.campaignId = campaignId;
		campaignGeneralFormPanel.assignValues(campaignId);
		
		tabCampaign.setSelectedTab(campaignGeneralFormPanel);
		if (campaignId != null) {
			tabCampaign.addTab(campaignDealerFormPanel, I18N.message("dealers"));
			tabCampaign.addTab(campaignAssetPanel, I18N.message("assets"));
		} else {
			tabCampaign.removeComponent(campaignDealerFormPanel);
			tabCampaign.removeComponent(campaignAssetPanel);
		}
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabCampaign.getSelectedTab().equals(campaignGeneralFormPanel)) {
			campaignGeneralFormPanel.assignValues(this.campaignId);
		} else if (tabCampaign.getSelectedTab().equals(campaignDealerFormPanel)) {
			campaignDealerFormPanel.assignValues(this.campaignId);
		} else if (tabCampaign.getSelectedTab().equals(campaignAssetPanel)) {
			campaignAssetPanel.assignValues(this.campaignId);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.campaignId = null;
		campaignGeneralFormPanel.assignValues(null);
		campaignDealerFormPanel.assignValues(null);
		campaignAssetPanel.reset();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(CampaignsPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**
	 * @param needRefresh
	 */
	public void setNeedRefresh(boolean needRefresh) {
		mainPanel.getTabSheet().setNeedRefresh(needRefresh);
	}

}
