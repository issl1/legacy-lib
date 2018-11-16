package com.nokor.efinance.gui.ui.panel.contract.user.driver;

import com.nokor.efinance.core.applicant.model.Driver;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;


/**
 * 
 * @author uhout.cheng
 */
public class DriverInformationTab extends TabSheet implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = 4342371374618796833L;

	private DriverGeneralInfoPanel generalInfoPanel;
	private DriverAddressInfoPanel addressInfoPanel;
	
	/**
	 * 
	 */	
	public DriverInformationTab() {
		this.addSelectedTabChangeListener(this);
		generalInfoPanel = new DriverGeneralInfoPanel(this);
		addressInfoPanel = new DriverAddressInfoPanel();
		this.addTab(generalInfoPanel, I18N.message("general"));
	}
	
	/**
	 * 
	 */
	public void removedAddressTab() {
		this.setSelectedTab(generalInfoPanel);
		if (this.getComponentCount() > 1) {
			this.removeComponent(addressInfoPanel);
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param isSelected
	 */
	public void displayAddressTab(Driver driver, boolean isSelected) {
		this.addTab(addressInfoPanel, I18N.message("address"));
		if (isSelected) {
			this.setSelectedTab(addressInfoPanel);
		}
		addressInfoPanel.assignValues(driver);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.reset();
		generalInfoPanel.assignValues(contract);
	}
	
	/**
	 * 
	 */
	private void reset() {
		this.setSelectedTab(generalInfoPanel);
		generalInfoPanel.reset();
		addressInfoPanel.reset();
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (this.getSelectedTab().equals(generalInfoPanel)) {
			generalInfoPanel.removeErrorsPanel();
		} else if (this.getSelectedTab().equals(addressInfoPanel)) {
			addressInfoPanel.removeErrorsPanel();
		}
	}
}
