package com.nokor.efinance.gui.ui.panel.collection.oa;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.oa.supervisor.OutsourceAgencyUnassignedPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneAssignedPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * 
 * @author uhout.cheng
 */
public class InboxSupervisorOAPanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 240584827441043326L;

	private TabSheet tabSupervisor;
	
	private OutsourceAgencyUnassignedPanel unassignmentPanel;
	private PhoneAssignedPanel assignedPanel;
//	private SupervisorColAssistContractsPanel assistPanel;
	
	/** 
	 * @param dashboardPanel
	 */
	public InboxSupervisorOAPanel() {
		
		tabSupervisor = new TabSheet();
		tabSupervisor.addSelectedTabChangeListener(this);
		
		unassignmentPanel = new OutsourceAgencyUnassignedPanel();
		assignedPanel = new PhoneAssignedPanel(IProfileCode.COL_OA_STAFF);
//		assistPanel = new SupervisorColAssistContractsPanel(IProfileCode.COL_OA_STAFF);	
		
		tabSupervisor.addTab(unassignmentPanel, I18N.message("unassigned"));
		tabSupervisor.addTab(assignedPanel, I18N.message("assigned"));
//		tabSupervisor.addTab(assistPanel, I18N.message("assist.request"));
		
		addComponent(tabSupervisor);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		/*if (tabSupervisor.getSelectedTab() == assignedPanel) {
			assignedPanel.refresh();
		} else if (tabSupervisor.getSelectedTab() == assistPanel) {
			assistPanel.refresh();
		}*/
	}
	
}
