package com.nokor.efinance.gui.ui.panel.dashboard;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionTaskTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.dashboard.ColPhoneDashboardPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class InboxCollectionPhoneStaff extends VerticalLayout implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1759530428425808079L;
	
	private TabSheet collectionPhoneStaffTabSheet;
	private CollectionTaskTabPanel colTaskTabPanel;
	//private CollectionPhoneStaffDashboardPanel collectionPhoneStaffDashboardPanel;
	private ColPhoneDashboardPanel colPhoneDashboardPanel;
	
	/**
	 * 
	 */
	public InboxCollectionPhoneStaff() {
		collectionPhoneStaffTabSheet = new TabSheet();
		collectionPhoneStaffTabSheet.addSelectedTabChangeListener(this);
		
		colTaskTabPanel = new CollectionTaskTabPanel();
		//collectionPhoneStaffDashboardPanel = new CollectionPhoneStaffDashboardPanel(this);
		colPhoneDashboardPanel = new ColPhoneDashboardPanel();
		
		collectionPhoneStaffTabSheet.addTab(colTaskTabPanel, I18N.message("tasks"));
		collectionPhoneStaffTabSheet.addTab(colPhoneDashboardPanel, I18N.message("dashboard"));
		
		
		collectionPhoneStaffTabSheet.setSelectedTab(colTaskTabPanel);
		addComponent(collectionPhoneStaffTabSheet);
	}

	/**
	 * 
	 * @param event
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (collectionPhoneStaffTabSheet.getSelectedTab() == colPhoneDashboardPanel) {
			colPhoneDashboardPanel.assignValue();
		} 
	}

	/**
	 * @return the collectionPhoneStaffTabSheet
	 */
	public TabSheet getTabSheet() {
		return collectionPhoneStaffTabSheet;
	}

	/**
	 * @return the colTaskTabPanel
	 */
	public CollectionTaskTabPanel getColTaskTabPanel() {
		return colTaskTabPanel;
	}
}
