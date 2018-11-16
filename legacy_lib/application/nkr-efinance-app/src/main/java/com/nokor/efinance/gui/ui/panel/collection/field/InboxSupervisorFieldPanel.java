package com.nokor.efinance.gui.ui.panel.collection.field;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.field.supervisor.FieldUnassignedPanel;
import com.nokor.efinance.gui.ui.panel.collection.field.supervisor.assist.SupervisorColAssistContractsPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneAssignedPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * INBOX leader Phone Panel 
 * @author uhout.cheng
 */
public class InboxSupervisorFieldPanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private TabSheet tabSupervisor;
	
	private FieldUnassignedPanel unassignmentPanel;
	private PhoneAssignedPanel assignedPanel;
	//private SupervisorColFlagContractsPanel flagPanel;
	private SupervisorColAssistContractsPanel assistPanel;
	
	/** 
	 * @param dashboardPanel
	 */
	public InboxSupervisorFieldPanel() {
		
		tabSupervisor = new TabSheet();
		tabSupervisor.addSelectedTabChangeListener(this);
		
		unassignmentPanel = new FieldUnassignedPanel();
		assignedPanel = new PhoneAssignedPanel(IProfileCode.COL_FIE_STAFF);
		
		//flagPanel = new SupervisorColFlagContractsPanel();
		assistPanel = new SupervisorColAssistContractsPanel(IProfileCode.COL_FIE_STAFF);	
		
		tabSupervisor.addTab(unassignmentPanel, I18N.message("unassigned"));
		tabSupervisor.addTab(assignedPanel, I18N.message("assigned"));
		tabSupervisor.addTab(assistPanel, I18N.message("assist.request"));
		//tabSupervisor.addTab(flagPanel, I18N.message("flag.request"));
		
		addComponent(tabSupervisor);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSupervisor.getSelectedTab() == assignedPanel) {
			assignedPanel.refresh();
		} else if (tabSupervisor.getSelectedTab() == assistPanel) {
			assistPanel.refresh();
		}
	}
	
}
