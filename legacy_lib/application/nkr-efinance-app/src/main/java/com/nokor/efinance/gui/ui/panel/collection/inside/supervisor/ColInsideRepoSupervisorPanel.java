package com.nokor.efinance.gui.ui.panel.collection.inside.supervisor;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.AssistPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneAssignedPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoSupervisorPanel extends VerticalLayout implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2664814268889711642L;
	
	private TabSheet tabSupervisor;
	
	private InsideRepoUnassignedPanel unassignmentPanel;
	private PhoneAssignedPanel assignedPanel;
	private AssistPanel assistPanel;
	//private FlagPanel flagPanel;
	
	public ColInsideRepoSupervisorPanel() {
		tabSupervisor = new TabSheet();
		tabSupervisor.addSelectedTabChangeListener(this);
		
		unassignmentPanel = new InsideRepoUnassignedPanel();
		assignedPanel = new PhoneAssignedPanel(IProfileCode.COL_INS_STAFF);
		
		assistPanel = new AssistPanel();
		//flagPanel = new FlagPanel();			
		
		
		tabSupervisor.addTab(unassignmentPanel, I18N.message("unassigned"));
		tabSupervisor.addTab(assignedPanel, I18N.message("assigned"));
		tabSupervisor.addTab(assistPanel);
		//tabSupervisor.addTab(flagPanel);
		
		addComponent(tabSupervisor);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSupervisor.getSelectedTab() == assignedPanel) {
			assignedPanel.refresh();
		}
	}

}
