package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.DebtLevelUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
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
 * Phone Unassigned Panel
 * @author bunlong.taing
 */
public class PhoneUnassignedPanel extends AbstractTabPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 1671696293586468320L;
	
	private Label lblNbUnassignedOdm0;
	private Label lblNbUnassignedOdm1;
	private Label lblNbUnassignedOdm2;
	private Label lblNbUnassignedOdm3;
	
	private Label lblNbAssignedOdm0;
	private Label lblNbAssignedOdm1;
	private Label lblNbAssignedOdm2;
	private Label lblNbAssignedOdm3;
	
	private Label lblNbStaffAllocatedOdm0;
	private Label lblNbStaffAllocatedOdm1;
	private Label lblNbStaffAllocatedOdm2;
	private Label lblNbStaffAllocatedOdm3;
		
	private PhoneUnassignedOdmPanel odm0Tab;
	private PhoneUnassignedOdmPanel odm1Tab;
	private PhoneUnassignedOdmPanel odm2Tab;
	private PhoneUnassignedOdmPanel odm3Tab;
	
	private TabSheet tabSheet;
	
	private ResourcesCollectionPanel resourcesPanel;
	
	//private ResultsTablePanel resultsTablePanel;
	
	private Button btnReassign;
	
	/**
	 * 
	 */
	public PhoneUnassignedPanel() {
		
		btnReassign = new Button();
		btnReassign.setStyleName(Reindeer.BUTTON_LINK);
		btnReassign.setIcon(FontAwesome.REFRESH);
		
		btnReassign.addClickListener(new ClickListener() {			
			private static final long serialVersionUID = 5805902137955307069L;
			@Override
			public void buttonClick(ClickEvent event) {
				COL_SRV.reassignPhoneContracts();
				odm0Tab.assignValues();
				odm1Tab.assignValues();
				odm2Tab.assignValues();
				odm3Tab.assignValues();
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
		lblNbUnassignedOdm0 = ComponentFactory.getLabel();
		lblNbUnassignedOdm1 = ComponentFactory.getLabel();
		lblNbUnassignedOdm2 = ComponentFactory.getLabel();
		lblNbUnassignedOdm3 = ComponentFactory.getLabel();
		
		lblNbAssignedOdm0 = ComponentFactory.getLabel();
		lblNbAssignedOdm1 = ComponentFactory.getLabel();
		lblNbAssignedOdm2 = ComponentFactory.getLabel();
		lblNbAssignedOdm3 = ComponentFactory.getLabel();
		
		lblNbStaffAllocatedOdm0 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm1 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm2 = ComponentFactory.getLabel();
		lblNbStaffAllocatedOdm3 = ComponentFactory.getLabel();
		
		String[] types = {
				I18N.message("unassigned"),
				I18N.message("assigned"),
				I18N.message("staffs.allocated")
		};
		String template = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\"><th></th>"
				+ "<th><b>" + I18N.message("odm0") + "</b></th>"
				+ "<th><b>" + I18N.message("odm1") + "</b></th>"
				+ "<th><b>" + I18N.message("odm2") + "</b></th>"
				+ "<th><b>" + I18N.message("odm3") + "</b></th>"
				+ "</tr>";
		for (String type : types) {
			String location0 = " location=\"lbl" + type + "odm0";
			String location1 = " location=\"lbl" + type + "odm1";
			String location2 = " location=\"lbl" + type + "odm2";
			String location3 = " location=\"lbl" + type + "odm3";
			template += "<tr><td>" + type + "</td><td " + location0 + "\"></td><td " + location1 + "\"></td><td " + location2 + "\"></td><td " + location3 + "\"></td></tr>";
		}
		template += "</table>";

		CustomLayout tableLayout = new CustomLayout();
		tableLayout.setTemplateContents(template);
		
		tableLayout.addComponent(lblNbUnassignedOdm0, "lbl" + types[0] + "odm0");
		tableLayout.addComponent(lblNbUnassignedOdm1, "lbl" + types[0] + "odm1");
		tableLayout.addComponent(lblNbUnassignedOdm2, "lbl" + types[0] + "odm2");
		tableLayout.addComponent(lblNbUnassignedOdm3, "lbl" + types[0] + "odm3");
		
		tableLayout.addComponent(lblNbAssignedOdm0, "lbl" + types[1] + "odm0");
		tableLayout.addComponent(lblNbAssignedOdm1, "lbl" + types[1] + "odm1");
		tableLayout.addComponent(lblNbAssignedOdm2, "lbl" + types[1] + "odm2");
		tableLayout.addComponent(lblNbAssignedOdm3, "lbl" + types[1] + "odm3");
		
		tableLayout.addComponent(lblNbStaffAllocatedOdm0, "lbl" + types[2] + "odm0");
		tableLayout.addComponent(lblNbStaffAllocatedOdm1, "lbl" + types[2] + "odm1");
		tableLayout.addComponent(lblNbStaffAllocatedOdm2, "lbl" + types[2] + "odm2");
		tableLayout.addComponent(lblNbStaffAllocatedOdm3, "lbl" + types[2] + "odm3");
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		return new Panel(content);
	}
	
	/**
	 * Calculate Detail Data
	 */
	public void calculateDetailData() {
		int odmsUnassigned[] = new int[4];
		int odmsAssigned[] = new int[4];
		int odmsStaffAllocated[] = new int[4];
		
		lblNbUnassignedOdm0.setValue(String.valueOf(odmsUnassigned[0]));
		lblNbUnassignedOdm1.setValue(String.valueOf(odmsUnassigned[1]));
		lblNbUnassignedOdm2.setValue(String.valueOf(odmsUnassigned[2]));
		lblNbUnassignedOdm3.setValue(String.valueOf(odmsUnassigned[3]));
		
		lblNbAssignedOdm0.setValue(String.valueOf(odmsAssigned[0]));
		lblNbAssignedOdm1.setValue(String.valueOf(odmsAssigned[1]));
		lblNbAssignedOdm2.setValue(String.valueOf(odmsAssigned[2]));
		lblNbAssignedOdm3.setValue(String.valueOf(odmsAssigned[3]));
		
		lblNbStaffAllocatedOdm0.setValue(String.valueOf(odmsStaffAllocated[0]));
		lblNbStaffAllocatedOdm1.setValue(String.valueOf(odmsStaffAllocated[1]));
		lblNbStaffAllocatedOdm2.setValue(String.valueOf(odmsStaffAllocated[2]));
		lblNbStaffAllocatedOdm3.setValue(String.valueOf(odmsStaffAllocated[3]));
	}

	/**
	 * 
	 * @return
	 */
	private TabSheet createTabLayout() {
		tabSheet = new TabSheet();
		tabSheet.setNeedRefresh(true);
		
		resourcesPanel = new ResourcesCollectionPanel(EColType.PHONE);
		odm0Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_0, EColType.PHONE);
		odm1Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_1, EColType.PHONE);
		odm2Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_2, EColType.PHONE);
		odm3Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_3, EColType.PHONE);
		
		//resultsTablePanel = new ResultsTablePanel();
		
		tabSheet.addTab(resourcesPanel, I18N.message("resources"));
		//tabSheet.addTab(resultsTablePanel, I18N.message("result"));
		tabSheet.addTab(odm0Tab, I18N.message("odm0"));
		tabSheet.addTab(odm1Tab, I18N.message("odm1"));
		tabSheet.addTab(odm2Tab, I18N.message("odm2"));
		tabSheet.addTab(odm3Tab, I18N.message("odm3"));
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {	

			private static final long serialVersionUID = -538160108371234253L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (tabSheet.getSelectedTab() == odm0Tab) {
					odm0Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm1Tab) {
					odm1Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm2Tab) {
					odm2Tab.assignValues();
				} else if (tabSheet.getSelectedTab() == odm3Tab) {
					odm3Tab.assignValues();
				} 
			}
		});
		
		return tabSheet;
	}

}
