package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign.CallCenterAssignedPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * INBOX leader call center Panel 
 * @author uhout.cheng
 */
public class InboxCallCenterLeaderPanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = -4224366191427860134L;

	private TabSheet tabCallCenterLeader;
	
	private CallCenterUnassignedPanel unassignmentPanel;
	private CallCenterAssignedPanel assignedPanel;
	
	/**
	 * 
	 */
	public InboxCallCenterLeaderPanel() {
		
		tabCallCenterLeader = new TabSheet();
		tabCallCenterLeader.addSelectedTabChangeListener(this);
		
		unassignmentPanel = new CallCenterUnassignedPanel();
		assignedPanel = new CallCenterAssignedPanel();
		
		tabCallCenterLeader.addTab(unassignmentPanel, I18N.message("unassigned"));
		tabCallCenterLeader.addTab(assignedPanel, I18N.message("assigned"));
		
		addComponent(tabCallCenterLeader);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabCallCenterLeader.getSelectedTab() == assignedPanel) {
			assignedPanel.refresh();
		}
	}
	
}
