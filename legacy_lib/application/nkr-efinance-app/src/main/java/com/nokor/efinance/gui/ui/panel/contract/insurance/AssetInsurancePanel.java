package com.nokor.efinance.gui.ui.panel.contract.insurance;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.ui.VerticalLayout;


/**
 * Insurance panel in asset
 * @author uhout.cheng
 */
public class AssetInsurancePanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7304654582544844906L;
	
	private InsurancesTablePanel insurancesTablePanel;
	private ClaimsTablePanel claimsTablePanel;
	private HistoryTablePanel historyTablePanel;
	
	/**
	 * 
	 */
	public AssetInsurancePanel() {
		setMargin(true);
		setSpacing(true);
		
		insurancesTablePanel = new InsurancesTablePanel("insurances");
		claimsTablePanel = new ClaimsTablePanel();
		historyTablePanel = new HistoryTablePanel();
		
        addComponent(insurancesTablePanel);
        addComponent(claimsTablePanel);
        addComponent(historyTablePanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		insurancesTablePanel.assignValues(contract);
		claimsTablePanel.assignValues(contract);
		historyTablePanel.assignValues(contract);
	}
	
}
