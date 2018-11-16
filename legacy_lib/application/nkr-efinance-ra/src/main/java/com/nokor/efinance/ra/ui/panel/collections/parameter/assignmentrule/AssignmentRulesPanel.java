package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.staffarea.StaffAreaPanel;
import com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team.TeamsPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Assignment rule tab panel in RA
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssignmentRulesPanel.NAME)
public class AssignmentRulesPanel extends AbstractControlPanel implements View, SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -3334303166645531912L;
	
	public static final String NAME = "assignment.rules";
	
	/*private VerticalLayout debtLevelTab;
	private VerticalLayout groupsTab;
	private VerticalLayout teamGroupsTab;
	private VerticalLayout areasTab;
	private VerticalLayout groupStaffsTab;
	private VerticalLayout areaStaffsTab;
	private VerticalLayout weightTab;
	private VerticalLayout summaryTab;
	private VerticalLayout teamStaffTab;
	private VerticalLayout dashboardTab;*/
	private VerticalLayout teamsTab;
	private VerticalLayout staffAreaTab;
	
	/*private DebtLevelPanel debtLevelPanel;
	private GroupsPanel groupsPanel;
	private TeamGroupsPanel teamGroupsPanel;
	private AreasPanel areasPanel;
	private StaffGroupPanel staffGroupPanel;
	private AreaStaffPanel areaStaffPanel;
	private CollectionWeightPanel weightPanel;
	private SummaryTabPanel summaryTabPanel;
	private TeamStaffPanel teamStaffPanel;*/
	private TeamsPanel teamsPanel;
	private StaffAreaPanel staffAreaPanel;
	
	@PostConstruct
	public void PostConstruct() {
		addComponent(createForm());
	}

	/**
	 * 
	 * @return
	 */
	private com.vaadin.ui.Component createForm() {
		/*debtLevelTab = new VerticalLayout();
		groupsTab = new VerticalLayout();
		teamGroupsTab = new VerticalLayout();
		areasTab = new VerticalLayout();
		groupStaffsTab = new VerticalLayout();
		areaStaffsTab = new VerticalLayout();
		weightTab = new VerticalLayout();
		summaryTab = new VerticalLayout();
		teamStaffTab = new VerticalLayout();
		dashboardTab = new VerticalLayout();*/
		teamsTab = new VerticalLayout();
		staffAreaTab = new VerticalLayout();
		
		TabSheet tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		
		/*tabSheet.addTab(debtLevelTab, I18N.message("debt.levels"));
		tabSheet.addTab(teamStaffTab, I18N.message("team.staff"));
		tabSheet.addTab(groupsTab, I18N.message("groups"));
		tabSheet.addTab(teamGroupsTab, I18N.message("team.groups"));
		tabSheet.addTab(areasTab, I18N.message("areas"));
		tabSheet.addTab(groupStaffsTab, I18N.message("group.staffs"));
        tabSheet.addTab(areaStaffsTab, I18N.message("area.staffs"));
        tabSheet.addTab(weightTab, I18N.message("weight"));
        tabSheet.addTab(summaryTab, I18N.message("summaries"));
        tabSheet.addTab(dashboardTab, I18N.message("summary"));*/
        tabSheet.addTab(teamsTab, I18N.message("teams"));
        tabSheet.addTab(staffAreaTab, I18N.message("staff.area"));
        
        TabSheet mainTabSheet = new TabSheet();
        mainTabSheet.addTab(getVerticalLayout(tabSheet), I18N.message("assignment.rules"));
        
		return mainTabSheet;
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private VerticalLayout getVerticalLayout(TabSheet component) {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.addComponent(component);
		return verLayout;
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		com.vaadin.ui.Component selectedTab = event.getTabSheet().getSelectedTab();
		/*if (selectedTab == debtLevelTab) {
			if (debtLevelPanel == null) {
				debtLevelPanel = SpringUtils.getBean(DebtLevelPanel.class);
				debtLevelTab.addComponent(debtLevelPanel);
			}
		} else if (selectedTab == groupsTab) {
			if (groupsPanel == null) {
				groupsPanel = SpringUtils.getBean(GroupsPanel.class);
				groupsTab.addComponent(groupsPanel);
			}
		} else if (selectedTab == teamGroupsTab) {
			if (teamGroupsPanel == null) {
				teamGroupsPanel = SpringUtils.getBean(TeamGroupsPanel.class);
				teamGroupsTab.addComponent(teamGroupsPanel);
			}
		} else if (selectedTab == areasTab) {
			if (areasPanel == null) {
				areasPanel = SpringUtils.getBean(AreasPanel.class);
				areasTab.addComponent(areasPanel);
			}
		} else if (selectedTab == groupStaffsTab) {
			if (staffGroupPanel == null) {
				staffGroupPanel = SpringUtils.getBean(StaffGroupPanel.class);
				groupStaffsTab.addComponent(staffGroupPanel);
			}
		} else if (selectedTab == areaStaffsTab) {
			if (areaStaffPanel == null) {
				areaStaffPanel = SpringUtils.getBean(AreaStaffPanel.class);
				areaStaffsTab.addComponent(areaStaffPanel);
			}
		} else if (selectedTab == weightTab) {
			if (weightPanel == null) {
				weightPanel = SpringUtils.getBean(CollectionWeightPanel.class);
				weightTab.addComponent(weightPanel);
			}
		} else if (selectedTab == summaryTab) {
			if (summaryTabPanel == null) {
				summaryTabPanel = SpringUtils.getBean(SummaryTabPanel.class);
				summaryTab.addComponent(summaryTabPanel);
			}
		} else if (selectedTab == teamStaffTab) {
			teamStaffTab.removeAllComponents();
			teamStaffPanel = SpringUtils.getBean(TeamStaffPanel.class);
			teamStaffTab.addComponent(teamStaffPanel);
		}*/
		if (selectedTab == teamsTab) {
			if (teamsPanel == null) {
				teamsPanel = SpringUtils.getBean(TeamsPanel.class);
				teamsTab.addComponent(teamsPanel);
			} else {
				teamsPanel.getTabSheet().setSelectedTab(0);
			}
		} else if (selectedTab == staffAreaTab) {
			if (staffAreaPanel == null) {
				staffAreaTab.removeAllComponents();
				staffAreaPanel = SpringUtils.getBean(StaffAreaPanel.class);
				staffAreaTab.addComponent(staffAreaPanel);
			} else {
				staffAreaPanel.reset();
			} 
		}
	}

}
