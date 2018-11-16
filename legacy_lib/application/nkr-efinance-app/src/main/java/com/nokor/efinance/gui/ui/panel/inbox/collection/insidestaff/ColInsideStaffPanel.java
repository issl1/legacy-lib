package com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks.ColInsideStaffTaskTabPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.ColPhoneStaffDashboardTabPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideStaffPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1808733105187341522L;
	
	private TabSheet tabSheet;
	
	private ColInsideStaffTaskTabPanel taskTabPanel;
	private ColPhoneStaffDashboardTabPanel dashboardTabPanel;
	
	public ColInsideStaffPanel() {
		tabSheet = new TabSheet();
		
		dashboardTabPanel = new ColPhoneStaffDashboardTabPanel();
		dashboardTabPanel.assignValue();
		taskTabPanel = new ColInsideStaffTaskTabPanel();
		
		tabSheet.addTab(dashboardTabPanel, I18N.message("dashboard"));
		tabSheet.addTab(taskTabPanel, I18N.message("task"));
		tabSheet.addTab(new Label(""), I18N.message("upload"));
		
		addComponent(tabSheet);
	}

}
