package com.nokor.efinance.gui.ui.panel.inbox.collection.oastaff;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.ColPhoneStaffDashboardTabPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColOAStaffPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2530991097528606026L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffDashboardTabPanel dashboardTabPanel;
	private ColOAStaffTaskTabPanel colOAStaffTaskTabPanel;
	
	public ColOAStaffPanel() {
		tabSheet = new TabSheet();
		
		dashboardTabPanel = new ColPhoneStaffDashboardTabPanel();
		dashboardTabPanel.assignValue();
		colOAStaffTaskTabPanel = new ColOAStaffTaskTabPanel();
		
		tabSheet.addTab(dashboardTabPanel, I18N.message("dashboard"));
		tabSheet.addTab(colOAStaffTaskTabPanel, I18N.message("task"));
		tabSheet.addTab(new Label(""), I18N.message("upload"));
		
		addComponent(tabSheet);
	}

}
