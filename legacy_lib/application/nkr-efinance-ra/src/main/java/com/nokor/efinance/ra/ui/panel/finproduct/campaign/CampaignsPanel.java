package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CampaignsPanel.NAME)
public class CampaignsPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -761674883032107240L;

	public static final String NAME = "campaigns";
	
	@Autowired
	private CampaignTablePanel campaignTablePanel;
	
	@Autowired
	private CampaignFormPanel formPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		campaignTablePanel.setMainPanel(this);
		formPanel.setMainPanel(this);
		getTabSheet().setTablePanel(campaignTablePanel);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		formPanel.assignValues(null);
		getTabSheet().addFormPanel(formPanel);
		getTabSheet().setSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(formPanel);
		initSelectedTab(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(campaignTablePanel.getItemSelectedId());
		} else if (selectedTab == campaignTablePanel && getTabSheet().isNeedRefresh()) {
			campaignTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
}
