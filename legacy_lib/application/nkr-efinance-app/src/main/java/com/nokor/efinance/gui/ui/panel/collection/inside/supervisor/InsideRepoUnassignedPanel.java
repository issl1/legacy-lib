package com.nokor.efinance.gui.ui.panel.collection.inside.supervisor;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.DebtLevelUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.ColAreaTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.field.supervisor.UnmatchedContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneUnassignedOdmPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.ResourcesCollectionPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * InsideRepo Unassigned Panel
 * @author buntha.chea
 */
public class InsideRepoUnassignedPanel extends AbstractTabPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 1671696293586468320L;
	
	private Label lblNbUnassignedOdm1;
	private Label lblNbUnassignedOdm2;
	private Label lblNbUnassignedOdm3;
	private Label lblNbUnassignedOdm4;
	private Label lblNbUnassignedOdm5;
	private Label lblNbUnassignedOdm6;

	private Label lblNbAssignedOdm1;
	private Label lblNbAssignedOdm2;
	private Label lblNbAssignedOdm3;
	private Label lblNbAssignedOdm4;
	private Label lblNbAssignedOdm5;
	private Label lblNbAssignedOdm6;
	
	private Label lblNbStaffAllocatedOdm1;
	private Label lblNbStaffAllocatedOdm2;
	private Label lblNbStaffAllocatedOdm3;
	private Label lblNbStaffAllocatedOdm4;
	private Label lblNbStaffAllocatedOdm5;
	private Label lblNbStaffAllocatedOdm6;
	
	private Label lblNbAveragePerAreaOdm1;
	private Label lblNbAveragePerAreaOdm2;
	private Label lblNbAveragePerAreaOdm3;
	private Label lblNbAveragePerAreaOdm4;
	private Label lblNbAveragePerAreaOdm5;
	private Label lblNbAveragePerAreaOdm6;
	
	private PhoneUnassignedOdmPanel odm1Tab;
	private PhoneUnassignedOdmPanel odm2Tab;
	private PhoneUnassignedOdmPanel odm3Tab;
	private PhoneUnassignedOdmPanel odm4Tab;
	private PhoneUnassignedOdmPanel odm5Tab;
	private PhoneUnassignedOdmPanel odm6Tab;
	
	private UnmatchedContractTablePanel unmatchedContractTab;
	
	private TabSheet tabSheet;
	
	private ResourcesCollectionPanel resourcesPanel;
	
	private Button btnReassign;
	
	private ColAreaTablePanel areaTablePanel;
	//private ResultsTablePanel resultsTablePanel;
	
	
	/**
	 */
	public InsideRepoUnassignedPanel() {
		
		btnReassign = new Button();
		btnReassign.setStyleName(Reindeer.BUTTON_LINK);
		btnReassign.setIcon(FontAwesome.REFRESH);
		
		btnReassign.addClickListener(new ClickListener() {			
			private static final long serialVersionUID = 5805902137955307069L;
			@Override
			public void buttonClick(ClickEvent event) {
				COL_SRV.reassignInsideRepoContracts();
				odm1Tab.assignValues();
				odm2Tab.assignValues();
				odm3Tab.assignValues();
				odm4Tab.assignValues();
				odm5Tab.assignValues();
				odm6Tab.assignValues();
			}
		});
		
		addComponent(btnReassign);
		addComponent(createTabLayout());
		
		calculateDetailData();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		return createAssignmentTableDetailLayout();
	}
	
	/**
	 * Create Assignment Table Detail Layout
	 * @return
	 */
	private Panel createAssignmentTableDetailLayout() {
		lblNbUnassignedOdm1 = ComponentFactory.getLabel();
		lblNbUnassignedOdm2 = ComponentFactory.getLabel();
		lblNbUnassignedOdm3 = ComponentFactory.getLabel();
		lblNbUnassignedOdm4 = ComponentFactory.getLabel();
		lblNbUnassignedOdm5 = ComponentFactory.getLabel();
		lblNbUnassignedOdm6 = ComponentFactory.getLabel();

		lblNbAssignedOdm1 = ComponentFactory.getLabel();
		lblNbAssignedOdm2 = ComponentFactory.getLabel();
		lblNbAssignedOdm3 = ComponentFactory.getLabel();
		lblNbAssignedOdm4 = ComponentFactory.getLabel();
		lblNbAssignedOdm5 = ComponentFactory.getLabel();
		lblNbAssignedOdm6 = ComponentFactory.getLabel();

		lblNbStaffAllocatedOdm1 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm2 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm3 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm4 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm5 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm6 = ComponentFactory.getLabel();
		
		lblNbAveragePerAreaOdm1 = ComponentFactory.getLabel();
		lblNbAveragePerAreaOdm2 = ComponentFactory.getLabel();
		lblNbAveragePerAreaOdm3 = ComponentFactory.getLabel();
		lblNbAveragePerAreaOdm4 = ComponentFactory.getLabel();
		lblNbAveragePerAreaOdm5 = ComponentFactory.getLabel();
		lblNbAveragePerAreaOdm6 = ComponentFactory.getLabel();
		
		String[] types = {
				I18N.message("unassigned"),
				I18N.message("assigned"),
				I18N.message("staffs.allocated"),
				I18N.message("average.per.area")
		};
		String template = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\"><th></th>"
				+ "<th><b>" + I18N.message("odm1") + "</b></th>"
				+ "<th><b>" + I18N.message("odm2") + "</b></th>"
				+ "<th><b>" + I18N.message("odm3") + "</b></th>"		
				+ "<th><b>" + I18N.message("odm4") + "</b></th>"
				+ "<th><b>" + I18N.message("odm5") + "</b></th>"
				+ "<th><b>" + I18N.message("odm6") + "</b></th>"
				+ "</tr>";
		for (String type : types) {
			String location1 = " location=\"lbl" + type + "odm1";
			String location2 = " location=\"lbl" + type + "odm2";
			String location3 = " location=\"lbl" + type + "odm3";
			String location4 = " location=\"lbl" + type + "odm4";
			String location5 = " location=\"lbl" + type + "odm5";
			String location6 = " location=\"lbl" + type + "odm6";
			template += "<tr><td>" + type + "</td><td " + location1 + "\"></td><td " + location2 + "\"></td><td " + location3 + "\"></td><td " + location4 + "\"></td><td " + location5 + "\"></td><td " + location6 + "\"></td></tr>";
		}
		template += "</table>";

		CustomLayout tableLayout = new CustomLayout();
		tableLayout.setTemplateContents(template);
		
		tableLayout.addComponent(lblNbUnassignedOdm1, "lbl" + types[0] + "odm1");
		tableLayout.addComponent(lblNbUnassignedOdm2, "lbl" + types[0] + "odm2");
		tableLayout.addComponent(lblNbUnassignedOdm3, "lbl" + types[0] + "odm3");
		tableLayout.addComponent(lblNbUnassignedOdm4, "lbl" + types[0] + "odm4");
		tableLayout.addComponent(lblNbUnassignedOdm5, "lbl" + types[0] + "odm5");
		tableLayout.addComponent(lblNbUnassignedOdm6, "lbl" + types[0] + "odm6");
		
		tableLayout.addComponent(lblNbAssignedOdm1, "lbl" + types[1] + "odm1");
		tableLayout.addComponent(lblNbAssignedOdm2, "lbl" + types[1] + "odm2");
		tableLayout.addComponent(lblNbAssignedOdm3, "lbl" + types[1] + "odm3");
		tableLayout.addComponent(lblNbAssignedOdm4, "lbl" + types[1] + "odm4");
		tableLayout.addComponent(lblNbAssignedOdm5, "lbl" + types[1] + "odm5");
		tableLayout.addComponent(lblNbAssignedOdm6, "lbl" + types[1] + "odm6");
		
		tableLayout.addComponent(lblNbStaffAllocatedOdm1, "lbl" + types[2] + "odm1");
		tableLayout.addComponent(lblNbStaffAllocatedOdm2, "lbl" + types[2] + "odm2");
		tableLayout.addComponent(lblNbStaffAllocatedOdm3, "lbl" + types[2] + "odm3");
		tableLayout.addComponent(lblNbStaffAllocatedOdm4, "lbl" + types[2] + "odm4");
		tableLayout.addComponent(lblNbStaffAllocatedOdm5, "lbl" + types[2] + "odm5");
		tableLayout.addComponent(lblNbStaffAllocatedOdm6, "lbl" + types[2] + "odm6");
		
		tableLayout.addComponent(lblNbAveragePerAreaOdm1, "lbl" + types[3] + "odm1");
		tableLayout.addComponent(lblNbAveragePerAreaOdm2, "lbl" + types[3] + "odm2");
		tableLayout.addComponent(lblNbAveragePerAreaOdm3, "lbl" + types[3] + "odm3");
		tableLayout.addComponent(lblNbAveragePerAreaOdm4, "lbl" + types[3] + "odm4");
		tableLayout.addComponent(lblNbAveragePerAreaOdm5, "lbl" + types[3] + "odm5");
		tableLayout.addComponent(lblNbAveragePerAreaOdm6, "lbl" + types[3] + "odm6");
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		return new Panel(content);
	}
	
	/**
	 * Calculate Detail Data
	 */
	public void calculateDetailData() {
		int odmsUnassigned[] = new int[6];
		int odmsAssigned[] = new int[6];
		int odmsStaffAllocated[] = new int[6];
		
		int nbAveragePerAreaOdm1 = odm1Tab.getNbContractPerArea();
		int nbAveragePerAreaOdm2 = odm2Tab.getNbContractPerArea();
		int nbAveragePerAreaOdm3 = odm3Tab.getNbContractPerArea();
		int nbAveragePerAreaOdm4 = odm4Tab.getNbContractPerArea();
		int nbAveragePerAreaOdm5 = odm5Tab.getNbContractPerArea();
		int nbAveragePerAreaOdm6 = odm6Tab.getNbContractPerArea();
		
		lblNbUnassignedOdm1.setValue(String.valueOf(odmsUnassigned[0]));
		lblNbUnassignedOdm2.setValue(String.valueOf(odmsUnassigned[1]));
		lblNbUnassignedOdm3.setValue(String.valueOf(odmsUnassigned[2]));
		lblNbUnassignedOdm4.setValue(String.valueOf(odmsUnassigned[3]));
		lblNbUnassignedOdm5.setValue(String.valueOf(odmsUnassigned[4]));
		lblNbUnassignedOdm6.setValue(String.valueOf(odmsUnassigned[5]));
		
		lblNbAssignedOdm1.setValue(String.valueOf(odmsAssigned[0]));
		lblNbAssignedOdm2.setValue(String.valueOf(odmsAssigned[1]));
		lblNbAssignedOdm3.setValue(String.valueOf(odmsAssigned[2]));
		lblNbAssignedOdm4.setValue(String.valueOf(odmsAssigned[3]));
		lblNbAssignedOdm5.setValue(String.valueOf(odmsAssigned[4]));
		lblNbAssignedOdm6.setValue(String.valueOf(odmsAssigned[5]));
		
		lblNbStaffAllocatedOdm1.setValue(String.valueOf(odmsStaffAllocated[0]));
		lblNbStaffAllocatedOdm2.setValue(String.valueOf(odmsStaffAllocated[1]));
		lblNbStaffAllocatedOdm3.setValue(String.valueOf(odmsStaffAllocated[2]));
		lblNbStaffAllocatedOdm4.setValue(String.valueOf(odmsStaffAllocated[3]));
		lblNbStaffAllocatedOdm5.setValue(String.valueOf(odmsStaffAllocated[4]));
		lblNbStaffAllocatedOdm6.setValue(String.valueOf(odmsStaffAllocated[5]));
		
		lblNbAveragePerAreaOdm1.setValue(String.valueOf(nbAveragePerAreaOdm1));
		lblNbAveragePerAreaOdm2.setValue(String.valueOf(nbAveragePerAreaOdm2));
		lblNbAveragePerAreaOdm3.setValue(String.valueOf(nbAveragePerAreaOdm3));
		lblNbAveragePerAreaOdm4.setValue(String.valueOf(nbAveragePerAreaOdm4));
		lblNbAveragePerAreaOdm5.setValue(String.valueOf(nbAveragePerAreaOdm5));
		lblNbAveragePerAreaOdm6.setValue(String.valueOf(nbAveragePerAreaOdm6));
	}

	/**
	 * 
	 * @return
	 */
	private TabSheet createTabLayout() {
		tabSheet = new TabSheet();
		tabSheet.setNeedRefresh(true);
		
		resourcesPanel = new ResourcesCollectionPanel(EColType.INSIDE_REPO);
		
		odm1Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_1, EColType.INSIDE_REPO);
		odm2Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_2, EColType.INSIDE_REPO);
		odm3Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_3, EColType.INSIDE_REPO);
		odm4Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_4, EColType.INSIDE_REPO);
		odm5Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_5, EColType.INSIDE_REPO);
		odm6Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_6, EColType.INSIDE_REPO);
		
		unmatchedContractTab = new UnmatchedContractTablePanel();
		areaTablePanel = new ColAreaTablePanel();
		//resultsTablePanel = new ResultsTablePanel();
		
		tabSheet.addTab(resourcesPanel, I18N.message("resources"));
		//tabSheet.addTab(resultsTablePanel, I18N.message("result"));
		tabSheet.addTab(areaTablePanel, I18N.message("area"));
		tabSheet.addTab(odm1Tab, I18N.message("odm1"));
		tabSheet.addTab(odm2Tab, I18N.message("odm2"));
		tabSheet.addTab(odm3Tab, I18N.message("odm3"));
		tabSheet.addTab(odm4Tab, I18N.message("odm4"));
		tabSheet.addTab(odm5Tab, I18N.message("odm5"));
		tabSheet.addTab(odm6Tab, I18N.message("odm6"));
		tabSheet.addTab(unmatchedContractTab, I18N.message("unmatched"));
				
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {	
		private static final long serialVersionUID = -538160108371234253L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (tabSheet.getSelectedTab() == odm1Tab) {
					odm1Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm2Tab) {
					odm2Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm3Tab) {
					odm3Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm4Tab) {
					odm4Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm5Tab) {
					odm5Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm6Tab) {
					odm6Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == unmatchedContractTab) {
					unmatchedContractTab.assignValues();
				} else if (tabSheet.getSelectedTab() == areaTablePanel) {
					if (tabSheet.isNeedRefresh()) {
						areaTablePanel.removeAllComponents();
						areaTablePanel.init();
						tabSheet.setNeedRefresh(false);
					} else {
						areaTablePanel.refresh();
					}
				} 
			}
		});

		return tabSheet;
	}

}
