package com.nokor.efinance.gui.ui.panel.collection.inside.staff;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoStaffPanel extends AbstractControlPanel implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957614054427076872L;
	
	private TabSheet staffTabSheet;
	private ColInsideRepoStaffDashboardTabPanel dashboardTabPanel;
	private ColInsideRepoStaffTaskTabPanel taskTabPanel;
	
	public ColInsideRepoStaffPanel() {
		staffTabSheet = new TabSheet();
		
		dashboardTabPanel = new ColInsideRepoStaffDashboardTabPanel(this);
		taskTabPanel = new ColInsideRepoStaffTaskTabPanel();
		
		staffTabSheet.addTab(taskTabPanel, I18N.message("task"));
		staffTabSheet.addTab(dashboardTabPanel, I18N.message("dashboard"));
		
		
		addComponent(staffTabSheet);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (staffTabSheet.getSelectedTab() == dashboardTabPanel) {
			dashboardTabPanel.assignValue();
		}
		
	}
	/**
	 * @return the taskTabPanel
	 */
	public ColInsideRepoStaffTaskTabPanel getTaskTabPanel() {
		return taskTabPanel;
	}

	/**
	 * @return the staffTabSheet
	 */
	public TabSheet getStaffTabSheet() {
		return staffTabSheet;
	}

}
