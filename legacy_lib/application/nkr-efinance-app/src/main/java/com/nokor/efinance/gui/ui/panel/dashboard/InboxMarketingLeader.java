package com.nokor.efinance.gui.ui.panel.dashboard;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.gui.ui.panel.collection.ColAreaTablePanel;
import com.nokor.efinance.gui.ui.panel.marketing.EmployeesMarketingPanel;
import com.nokor.efinance.gui.ui.panel.marketing.team.TeamMarketingHolderPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class InboxMarketingLeader extends VerticalLayout implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = 1241833524655142303L;

	private TabSheet tabSheet;
	private EmployeesMarketingPanel employeesMarketingPanel;
	private ColAreaTablePanel areaTablePanel;
	private VerticalLayout teamsTab;
	private TeamMarketingHolderPanel teamMarketingHolderPanel;
	
	/**
	 * 
	 */
	public InboxMarketingLeader() {
		tabSheet = new TabSheet();
		tabSheet.setNeedRefresh(true);
		tabSheet.addSelectedTabChangeListener(this);
		
		employeesMarketingPanel = new EmployeesMarketingPanel();
		areaTablePanel = new ColAreaTablePanel();
		
		teamsTab = new VerticalLayout();
		
		tabSheet.addTab(employeesMarketingPanel, I18N.message("employees"));
		tabSheet.addTab(areaTablePanel, I18N.message("areas"));
		tabSheet.addTab(teamsTab, I18N.message("teams"));
		
		tabSheet.setSelectedTab(employeesMarketingPanel);
		addComponent(tabSheet);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(employeesMarketingPanel)) {
			
		} else if (tabSheet.getSelectedTab().equals(areaTablePanel)) {
			if (tabSheet.isNeedRefresh()) {
				areaTablePanel.removeAllComponents();
				areaTablePanel.init();
				tabSheet.setNeedRefresh(false);
			} else {
				areaTablePanel.refresh();
			}
		} else if (tabSheet.getSelectedTab().equals(teamsTab)) {
			if (teamMarketingHolderPanel == null) {
				teamMarketingHolderPanel = SpringUtils.getBean(TeamMarketingHolderPanel.class);
				teamsTab.addComponent(teamMarketingHolderPanel);
			} else {
				teamMarketingHolderPanel.getTabSheet().setSelectedTab(0);
			}
		}
	}
	
}
