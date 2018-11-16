package com.nokor.efinance.gui.ui.panel.collection.callcenter.staff;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * INBOX call center staff panel
 * @author uhout.cheng
 */
public class InboxCallCenterStaff extends VerticalLayout {

	/** */
	private static final long serialVersionUID = -1820908455470515445L;

	private TabSheet mainTab;
	private CallCenterStaffPanel callCenterStaffPanel;
	private DashboardCallCenterStaff dashboardPanel;
	
	/**
	 * 
	 */
	public InboxCallCenterStaff() {
		callCenterStaffPanel = new CallCenterStaffPanel();
		dashboardPanel = new DashboardCallCenterStaff();
		
		mainTab = new TabSheet();
		mainTab.addTab(callCenterStaffPanel, I18N.message("tasks"));
		mainTab.addTab(dashboardPanel, I18N.message("dashboard"));
		addComponent(mainTab);
	}

}
