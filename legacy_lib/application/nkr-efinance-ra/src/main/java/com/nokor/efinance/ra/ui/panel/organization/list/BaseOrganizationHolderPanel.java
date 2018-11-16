package com.nokor.efinance.ra.ui.panel.organization.list;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;

import com.nokor.efinance.ra.ui.panel.agent.company.detail.AgentCompanyFormPanel;
import com.nokor.efinance.ra.ui.panel.auction.location.detail.LocationFormPanel;
import com.nokor.efinance.ra.ui.panel.collections.warehouses.detail.WarehouseFormPanel;
import com.nokor.efinance.ra.ui.panel.insurance.company.detail.InsuranceCompanyFormPanel;
import com.nokor.efinance.ra.ui.panel.organization.branch.list.BranchTablePanel;
import com.nokor.efinance.ra.ui.panel.organization.detail.OrganizationFormPanel;
import com.nokor.efinance.ra.ui.panel.organization.employees.OrgEmployeesTablePanel;
import com.nokor.efinance.ra.ui.panel.organization.subsidiairy.detail.SubsidiaryFormPanel;
import com.nokor.efinance.ra.ui.panel.organization.subsidiairy.list.SubsidiairyTablePanel;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseOrganizationHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = -4565847982423995252L;
	
	private ETypeOrganization typeOrganization;
	
	private ESubTypeOrganization subTypeOrganization;
	
	@Autowired
	private OrganizationTablePanel tablePanel;
	
	@Autowired
	private OrganizationFormPanel orgFormPanel;
	
	@Autowired
	private InsuranceCompanyFormPanel insuranceFormPanel;
	
	@Autowired
	private AgentCompanyFormPanel agentFormPanel;
	
	@Autowired
	private SubsidiairyTablePanel subsidiairyTablePanel;
	
	@Autowired
	private SubsidiaryFormPanel subsidiaryFormPanel;

	@Autowired
	private BranchTablePanel branchTablePanel;
	
	@Autowired
	private WarehouseFormPanel warehouseFormPanel;
	
	@Autowired
	private BranchTablePanel areaTablePanel;
	
	@Autowired
	private LocationFormPanel locationFormPanel;
	
	@Autowired
	private BranchTablePanel departmentTabelPanel;
	
	@Autowired
	private OrgEmployeesTablePanel employeesTablePanel;
	

	/**
	 * 
	 */
	public void init() {
		super.init();
		tablePanel.setMainPanel(this);
		tablePanel.init(I18N.message(getOrgCaption()));
		orgFormPanel.setMainPanel(this);
		branchTablePanel.init("offices", "office", EOrgLevel.BRANCH);
		areaTablePanel.init("areas", "area", EOrgLevel.BRANCH);
		departmentTabelPanel.init("departments", "department", EOrgLevel.DEPARTMENT);
		insuranceFormPanel.setMainPanel(this);
		insuranceFormPanel.init();
		agentFormPanel.setMainPanel(this);
		agentFormPanel.init();
		subsidiairyTablePanel.setMainPanel(this);
		subsidiaryFormPanel.setMainPanel(this);
		subsidiaryFormPanel.init();
		warehouseFormPanel.setMainPanel(this);
		locationFormPanel.setMainPanel(this);
		employeesTablePanel.init("employee");
		getTabSheet().setTablePanel(tablePanel);
		getTabSheet().addSelectedTabChangeListener(new SelectedTabChangeListener() {
			/** */
			private static final long serialVersionUID = -4126241648387297538L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component selectedTab = event.getTabSheet().getSelectedTab();
				if (selectedTab == subsidiairyTablePanel) {
					getTabSheet().removeFormPanel(subsidiaryFormPanel);
					subsidiairyTablePanel.refresh();
				}
			}
		});
	}
	
	/**
	 * Get Orgization Caption
	 * @return
	 */
	protected abstract String getOrgCaption();

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
		if (OrganizationTypes.MAIN.equals(typeOrganization)) {
			orgFormPanel.reset();
			getTabSheet().addFormPanel(orgFormPanel);
			getTabSheet().setSelectedTab(orgFormPanel);
		} else if (OrganizationTypes.INSURANCE.equals(typeOrganization)) {
			insuranceFormPanel.reset();
			getTabSheet().addFormPanel(insuranceFormPanel);
			getTabSheet().setSelectedTab(insuranceFormPanel);
		} else if (OrganizationTypes.AGENT.equals(typeOrganization)) {
			agentFormPanel.reset();
			getTabSheet().addFormPanel(agentFormPanel);
			getTabSheet().setSelectedTab(agentFormPanel);
		} else if (OrganizationTypes.LOCATION.equals(typeOrganization)) {
			ESubTypeOrganization warehouse = ESubTypeOrganization.getById(4l);// id 4 = Warehouse
			ESubTypeOrganization location = ESubTypeOrganization.getById(5l);// id 5 = Auction location
			if (warehouse != null && warehouse.equals(subTypeOrganization)) {
				warehouseFormPanel.reset();
				getTabSheet().addFormPanel(warehouseFormPanel);
				getTabSheet().setSelectedTab(warehouseFormPanel);
			} else if (location != null && location.equals(subTypeOrganization)) {
				locationFormPanel.reset();
				getTabSheet().addFormPanel(locationFormPanel);
				getTabSheet().setSelectedTab(locationFormPanel);
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		if (OrganizationTypes.MAIN.equals(typeOrganization)) {
			getTabSheet().addFormPanel(orgFormPanel);
			addBranchSubTab(tablePanel.getItemSelectedId());
			initSelectedTab(orgFormPanel);
		} else if (OrganizationTypes.INSURANCE.equals(typeOrganization)) {
			getTabSheet().addFormPanel(insuranceFormPanel);
			addSubsidiairySubTab(tablePanel.getItemSelectedId());
			initSelectedTab(insuranceFormPanel);
		} else if (OrganizationTypes.AGENT.equals(typeOrganization)) {
			getTabSheet().addFormPanel(agentFormPanel);
			initSelectedTab(agentFormPanel);
		} else if (OrganizationTypes.LOCATION.equals(typeOrganization)) {
			ESubTypeOrganization warehouse = ESubTypeOrganization.getById(4l);// id 4 = Warehouse
			ESubTypeOrganization location = ESubTypeOrganization.getById(5l);// id 5 = Auction location
			if (warehouse != null && warehouse.equals(subTypeOrganization)) {
				getTabSheet().addFormPanel(warehouseFormPanel);
				addAreaSubTab(tablePanel.getItemSelectedId());
				initSelectedTab(warehouseFormPanel);
			} else if (location != null && location.equals(subTypeOrganization)) {
				getTabSheet().addFormPanel(locationFormPanel);
				initSelectedTab(locationFormPanel);
			}
		}
	}
	
	/**
	 * Add Branch Sub Tab
	 */
	public void addBranchSubTab(Long selectedId) {
		branchTablePanel.assignValues(selectedId);
		departmentTabelPanel.assignValues(selectedId);
		employeesTablePanel.assignValues(selectedId);
		branchTablePanel.refresh();
		departmentTabelPanel.refresh();
		employeesTablePanel.refresh();
		getTabSheet().addFormPanel(branchTablePanel);
		getTabSheet().addFormPanel(departmentTabelPanel);
		getTabSheet().addFormPanel(employeesTablePanel);
	}
	
	/**
	 * Add Area Sub Tab
	 * @param selectedId
	 */
	public void addAreaSubTab(Long selectedId) {
		areaTablePanel.assignValues(selectedId);
		areaTablePanel.refresh();
		getTabSheet().addFormPanel(areaTablePanel);
	}
	
	/**
	 * Add Subsidiairy Sub Tab
	 * @param selectedId
	 */
	public void addSubsidiairySubTab(Long selectedId) {
		subsidiairyTablePanel.setParentOrganization(selectedId);
		subsidiairyTablePanel.refresh();
		getTabSheet().addFormPanel(subsidiairyTablePanel);
	}
	
	/**
	 * Add Subsidiary Form Panel
	 * @param selectedId
	 */
	public void addSubsidiaryFormPanel(Long parentOrganizationId, Long selectedId) {
		subsidiaryFormPanel.reset();
		subsidiaryFormPanel.setParentOrganizationId(parentOrganizationId);
		subsidiaryFormPanel.assignValues(selectedId);
		getTabSheet().addFormPanel(subsidiaryFormPanel);
		getTabSheet().setSelectedTab(subsidiaryFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == orgFormPanel) {
			orgFormPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == insuranceFormPanel) {
			insuranceFormPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == agentFormPanel) {
			agentFormPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == warehouseFormPanel) {
			warehouseFormPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == locationFormPanel) {
			locationFormPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @return the typeOrganization
	 */
	public ETypeOrganization getTypeOrganization() {
		return typeOrganization;
	}

	/**
	 * @param typeOrganization the typeOrganization to set
	 */
	public void setTypeOrganization(ETypeOrganization typeOrganization) {
		this.typeOrganization = typeOrganization;
	}

	/**
	 * @return the subTypeOrganization
	 */
	public ESubTypeOrganization getSubTypeOrganization() {
		return subTypeOrganization;
	}

	/**
	 * @param subTypeOrganization the subTypeOrganization to set
	 */
	public void setSubTypeOrganization(ESubTypeOrganization subTypeOrganization) {
		this.subTypeOrganization = subTypeOrganization;
	}
	
}
