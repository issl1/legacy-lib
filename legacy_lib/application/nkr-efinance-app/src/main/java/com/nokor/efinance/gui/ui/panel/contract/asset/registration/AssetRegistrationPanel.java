package com.nokor.efinance.gui.ui.panel.contract.asset.registration;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;


/**
 * Registration tab panel in Asset
 * @author uhout.cheng
 */
public class AssetRegistrationPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = 831944508899874665L;
	
	private RegistrationDetailPanel registrationDetailPanel;
	private RegistrationOperationTable registrationOperationTable;
	private RegistrationOperationDetailPanel registrationOperationDetailPanel;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		registrationDetailPanel = new RegistrationDetailPanel();
		registrationOperationTable = new RegistrationOperationTable();
		registrationOperationDetailPanel = new RegistrationOperationDetailPanel();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(registrationDetailPanel);
		contentLayout.addComponent(registrationOperationTable);
		contentLayout.addComponent(registrationOperationDetailPanel);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 * @param assetPanel
	 * @param loanSummaryPanel
	 */
	public void assignValues(Contract contract, AssetPanel assetPanel) {
		if (contract != null) {
			registrationDetailPanel.assignValues(contract, assetPanel);
		}
	}
	
	/**
	 * 
	 */
	public void reset() {
		registrationDetailPanel.reset();
		registrationOperationDetailPanel.reset();
	}

}
