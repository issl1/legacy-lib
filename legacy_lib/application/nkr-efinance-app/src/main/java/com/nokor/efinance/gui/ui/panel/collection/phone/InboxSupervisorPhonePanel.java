package com.nokor.efinance.gui.ui.panel.collection.phone;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneAssignedPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneUnassignedPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * INBOX leader Phone Panel 
 * @author uhout.cheng
 */
public class InboxSupervisorPhonePanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private TabSheet tabSupervisor;
	
	private PhoneUnassignedPanel unassignmentPanel;
	private PhoneAssignedPanel assignedPanel;
	//private AssistPanel assistPanel;
	//private FlagPanel flagPanel;
	
	/** 
	 * @param dashboardPanel
	 */
	public InboxSupervisorPhonePanel() {
		
		tabSupervisor = new TabSheet();
		tabSupervisor.addSelectedTabChangeListener(this);
		
		unassignmentPanel = new PhoneUnassignedPanel();
		assignedPanel = new PhoneAssignedPanel(IProfileCode.COL_PHO_STAFF);
		
	//	assistPanel = new AssistPanel();
	//	flagPanel = new FlagPanel();			
		
		
		tabSupervisor.addTab(unassignmentPanel, I18N.message("unassigned"));
		tabSupervisor.addTab(assignedPanel, I18N.message("assigned"));
	//	tabSupervisor.addTab(assistPanel);
	//	tabSupervisor.addTab(flagPanel);
		
		addComponent(tabSupervisor);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSupervisor.getSelectedTab() == assignedPanel) {
			assignedPanel.refresh();
		}
	}
	
}
