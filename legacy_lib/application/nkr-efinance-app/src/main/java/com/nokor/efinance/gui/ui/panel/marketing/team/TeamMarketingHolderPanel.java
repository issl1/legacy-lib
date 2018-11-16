package com.nokor.efinance.gui.ui.panel.marketing.team;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;


/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamMarketingHolderPanel extends AbstractTabsheetPanel {
	
	/** */
	private static final long serialVersionUID = 7189936741538404351L;
	
	@Autowired
	private TeamMarketingTablePanel teamTablePanel;
	@Autowired
	private TeamMarketingFormPanel teamFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		setMargin(true);
		teamTablePanel.setMainPanel(this);
		teamFormPanel.setCaption(I18N.message("team"));
		getTabSheet().setTablePanel(teamTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		teamFormPanel.reset();
		getTabSheet().addFormPanel(teamFormPanel);
		getTabSheet().setSelectedTab(teamFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(teamFormPanel);
		initSelectedTab(teamFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == teamFormPanel) {
			teamFormPanel.assignValues(teamTablePanel.getItemSelectedId());
		} else if (selectedTab == teamTablePanel) {
			teamTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
