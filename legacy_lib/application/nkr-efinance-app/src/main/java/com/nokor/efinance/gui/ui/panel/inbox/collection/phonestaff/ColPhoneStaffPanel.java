package com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneStaffPanel extends AbstractControlPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 181130211195458200L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffDashboardTabPanel dashboardTabPanel;
	private ColPhoneStaffTaskTabPanel taskTabPanel;
	
	public ColPhoneStaffPanel() {
		
		tabSheet = new TabSheet();
		
		dashboardTabPanel = new ColPhoneStaffDashboardTabPanel();
		dashboardTabPanel.assignValue();
		taskTabPanel = new ColPhoneStaffTaskTabPanel();
		
		tabSheet.addTab(dashboardTabPanel, I18N.message("dashboard"));
		tabSheet.addTab(taskTabPanel, I18N.message("task"));
		
		addComponent(tabSheet);
	}

}
