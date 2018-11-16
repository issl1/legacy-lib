package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.teamgroup;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
/**
 * Team and Group tab panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamGroupsPanel extends AbstractTabsheetPanel {
	
	/** */
	private static final long serialVersionUID = -8388470467383046505L;
	
	@Autowired
	private TeamGroupTablePanel teamGroupTablePanel;
	@Autowired
	private TeamGroupFormPanel teamGroupFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		setMargin(true);
		teamGroupTablePanel.setMainPanel(this);
		teamGroupFormPanel.setCaption(I18N.message("team.group"));
		getTabSheet().setTablePanel(teamGroupTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		teamGroupFormPanel.reset();
		getTabSheet().addFormPanel(teamGroupFormPanel);
		getTabSheet().setSelectedTab(teamGroupFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(teamGroupFormPanel);
		initSelectedTab(teamGroupFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == teamGroupFormPanel) {
			teamGroupFormPanel.assignValues(teamGroupTablePanel.getItemSelectedId());
		} else if (selectedTab == teamGroupTablePanel && getTabSheet().isNeedRefresh()) {
			teamGroupTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
