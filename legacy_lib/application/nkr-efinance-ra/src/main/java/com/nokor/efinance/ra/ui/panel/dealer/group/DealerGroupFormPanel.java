package com.nokor.efinance.ra.ui.panel.dealer.group;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;

/**
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DealerGroupFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 3009097491814863392L;
	
	private TabSheet tabDealerGroup;
	private DealerGroupsPanel mainPanel;
	
	private DealerGroupGeneralPanel dealerGroupGeneralPanel;
	private DealerGroupTypePanel dealerGroupTypeBranchPanel;
	private DealerGroupTypePanel dealerGroupTypeHeadPanel;

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
		tabDealerGroup = new TabSheet();
		dealerGroupGeneralPanel = new DealerGroupGeneralPanel(this);
		dealerGroupTypeBranchPanel = new DealerGroupTypePanel(EDealerType.BRANCH);
		dealerGroupTypeHeadPanel = new DealerGroupTypePanel(EDealerType.HEAD);
		
		tabDealerGroup.addTab(dealerGroupGeneralPanel, I18N.message("general"));
		
		return tabDealerGroup;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long deaGrpId) {
		super.reset();
		dealerGroupGeneralPanel.assignValues(deaGrpId);
		dealerGroupTypeBranchPanel.assignValues(deaGrpId);
		dealerGroupTypeHeadPanel.assignValues(deaGrpId);
		
		tabDealerGroup.setSelectedTab(dealerGroupGeneralPanel);
		if (deaGrpId != null) {
			tabDealerGroup.addTab(dealerGroupTypeBranchPanel, I18N.message("dealer.branch"));
			tabDealerGroup.addTab(dealerGroupTypeHeadPanel, I18N.message("dealer.head"));
		} else {
			tabDealerGroup.removeComponent(dealerGroupTypeBranchPanel);
			tabDealerGroup.removeComponent(dealerGroupTypeHeadPanel);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		dealerGroupGeneralPanel.assignValues(null);
		tabDealerGroup.removeComponent(dealerGroupTypeBranchPanel);
		tabDealerGroup.removeComponent(dealerGroupTypeHeadPanel);
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
	public void setMainPanel(DealerGroupsPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**
	 * @param needRefresh
	 */
	public void setNeedRefresh(boolean needRefresh) {
		mainPanel.getTabSheet().setNeedRefresh(needRefresh);
	}

}
