package com.nokor.efinance.ra.ui.panel.insurance.campaign;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsuranceCampaignsFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = -2742220260490298569L;

	private InsuranceCampaign insuranceCampaign;
	
	private TabSheet tabInsCampaigns;
	
	private InsuranceCampaignDetailPanel detailPanel;
	private InsuranceCampaignDealerPanel dealersPanel;

	/**
	 */
	public InsuranceCampaignsFormPanel() {
		super.init();
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected Component createForm() {
		
		tabInsCampaigns = new TabSheet();
		
		detailPanel = new InsuranceCampaignDetailPanel();
		dealersPanel = new InsuranceCampaignDealerPanel();
		
		tabInsCampaigns.addTab(detailPanel, I18N.message("detail"));
		tabInsCampaigns.addTab(dealersPanel, I18N.message("dealers"));
		
	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(tabInsCampaigns);
		
		return contentLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/** 
	 * @param insCamId
	 */
	public void assignValues(Long insCamId) {
		super.reset();
		if (insCamId != null) {
			insuranceCampaign = ENTITY_SRV.getById(InsuranceCampaign.class, insCamId);
			detailPanel.assignValues(insuranceCampaign);
			dealersPanel.assignValues(insuranceCampaign);
		}
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
     */
	@Override
	public void reset() {
		super.reset();
		insuranceCampaign = new InsuranceCampaign();
		detailPanel.reset();
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
     */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}
}
