package com.nokor.efinance.gui.ui.panel.payment.blocked.identify.dues;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractDuesTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class IdentifyPaymentDuesPanel extends VerticalLayout implements MLockSplit, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 1755612781582595896L;
	
	private ColContractDuesTablePanel duesTablePanel;

	/**
	 * 
	 */
	public IdentifyPaymentDuesPanel() {
		duesTablePanel = new ColContractDuesTablePanel();
		setMargin(true);
		addComponent(duesTablePanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		duesTablePanel.reset();
		if (contract != null) {
			duesTablePanel.assignValues(contract);
		}
	}
	
}
