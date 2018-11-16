package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Call Center Unassigned Panel
 * @author uhout.cheng
 */
public class CallCenterUnassignedPanel extends AbstractTabPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 6368333108336961223L;
	
	private Label lblNbUnassignedContract;
	private Label lblNbAssignedContract;
	private Label lblNbStaffAllocatedContract;
	private CallCenterUnassignedOdmPanel contractTab;
	private TabSheet tabSheet;
	private CalCenterResourcesPanel resourcesPanel;
	
	private Button btnReassign;
	private Button btnRunAssign;
	private AutoDateField dfProcess;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		btnReassign = new Button();
		btnReassign.setStyleName(Reindeer.BUTTON_LINK);
		btnReassign.setIcon(FontAwesome.REFRESH);		
		btnReassign.addClickListener(new ClickListener() {			
			
			/** */
			private static final long serialVersionUID = -2463390376748055826L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				CALL_CTR_SRV.reassignContracts();
				contractTab.setStaffTableContainerDataSource(true);
				contractTab.calculateDetailData();
			}
		});
		
		btnRunAssign = new Button();
		btnRunAssign.setStyleName(Reindeer.BUTTON_LINK);
		btnRunAssign.setIcon(FontAwesome.ADJUST);
		btnRunAssign.addClickListener(new ClickListener() {
			/**
			 */
			private static final long serialVersionUID = 8486155449059664384L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (dfProcess.getValue() != null) {
					CALL_CTR_SRV.assignContracts(dfProcess.getValue());
					contractTab.setStaffTableContainerDataSource(true);
					contractTab.calculateDetailData();
				}
			}
		});
		
		dfProcess = ComponentFactory.getAutoDateField();
		
		HorizontalLayout assignLayout = new HorizontalLayout();
		assignLayout.setSpacing(true);
		assignLayout.addComponent(btnReassign);
		assignLayout.addComponent(btnRunAssign);
		assignLayout.addComponent(dfProcess);
		assignLayout.setComponentAlignment(btnReassign, Alignment.MIDDLE_LEFT);
		assignLayout.setComponentAlignment(btnRunAssign, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.addComponent(createAssignmentTableDetailLayout());
		verLayout.addComponent(assignLayout);
		verLayout.addComponent(createTabLayout());
		
		calculateDetailData();
		
		return verLayout;
	}
	
	/**
	 * Create Assignment Table Detail Layout
	 * @return
	 */
	private Panel createAssignmentTableDetailLayout() {
		lblNbUnassignedContract = ComponentFactory.getLabel();
		lblNbAssignedContract = ComponentFactory.getLabel();
		lblNbStaffAllocatedContract = ComponentFactory.getLabel();
		
		String table = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\">"
				+ "<th><b>" + I18N.message("unassigned") + "</b></th>"
				+ "<th><b>" + I18N.message("assigned") + "</b></th>"
				+ "<th><b>" + I18N.message("staffs.allocated") + "</b></th>"
				+ "</tr>";
		
		table += "<tr>"
				+ "<td location=\"lblNbUnassignedContract\"></td>"
				+ "<td location=\"lblNbAssignedContract\"></td>"
				+ "<td location=\"lblNbStaffAllocatedContract\"></td>"
				+ "</tr></table>";
		
		CustomLayout tableLayout = null;
		try {
			tableLayout = new CustomLayout(new ByteArrayInputStream(table.getBytes()));
		} catch (IOException e) {
			Notification.show("Could not create custom layout", e.getMessage(), Type.ERROR_MESSAGE);
		}
		tableLayout.addComponent(lblNbUnassignedContract, "lblNbUnassignedContract");
		tableLayout.addComponent(lblNbAssignedContract, "lblNbAssignedContract");
		tableLayout.addComponent(lblNbStaffAllocatedContract, "lblNbStaffAllocatedContract");
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		return new Panel(content);
	}
	
	/**
	 * Calculate Detail Data
	 */
	public void calculateDetailData() {
		int contractsUnassigned[] = new int[4];
		int contractsAssigned[] = new int[4];
		int contractsStaffAllocated[] = new int[4];
		
		lblNbUnassignedContract.setValue(String.valueOf(contractsUnassigned[0]));
		lblNbAssignedContract.setValue(String.valueOf(contractsAssigned[0]));
		lblNbStaffAllocatedContract.setValue(String.valueOf(contractsStaffAllocated[0]));
	}

	/**
	 * Create Filter Layout
	 * @param profileCode
	 * @return
	 */
	private TabSheet createTabLayout() {
		tabSheet = new TabSheet();
		
		resourcesPanel = new CalCenterResourcesPanel();
		contractTab = new CallCenterUnassignedOdmPanel();
		
		tabSheet.addTab(resourcesPanel, I18N.message("resources"));
		tabSheet.addTab(contractTab, I18N.message("contracts"));
		
		return tabSheet;
	}
}
