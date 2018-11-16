package com.nokor.efinance.gui.ui.panel.collection.field.staff;

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
public class ColFieldStaffPanel extends AbstractControlPanel implements SelectedTabChangeListener {

	private static final long serialVersionUID = -4161146581587306068L;
	
	private TabSheet fieldStaffTabSheet;
	private ColFieldStaffDashboardTabPanel dashboardPanel;
	private ColFieldStaffTaskTabPanel taskPanel;
	
	public ColFieldStaffPanel() {
		
		fieldStaffTabSheet = new TabSheet();
		fieldStaffTabSheet.addSelectedTabChangeListener(this);
		
		dashboardPanel = new ColFieldStaffDashboardTabPanel(this);
		taskPanel = new ColFieldStaffTaskTabPanel();
		
		fieldStaffTabSheet.addTab(taskPanel, I18N.message("task"));	
		fieldStaffTabSheet.addTab(dashboardPanel, I18N.message("dashboard"));
		
		
		addComponent(fieldStaffTabSheet);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (fieldStaffTabSheet.getSelectedTab() == dashboardPanel) {
			dashboardPanel.assignValue();
		} 
	}

	/**
	 * @return the fieldStaffTabSheet
	 */
	public TabSheet getFieldStaffTabSheet() {
		return fieldStaffTabSheet;
	}

	/**
	 * @return the taskPanel
	 */
	public ColFieldStaffTaskTabPanel getTaskPanel() {
		return taskPanel;
	}

}
