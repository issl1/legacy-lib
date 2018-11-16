package com.nokor.efinance.gui.ui.panel.contract.dues.loan;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;


/**
 * 
 * @author uhout.cheng
 */
public class ChargesTabPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 8363227738950901272L;

	private LoanFeePenaltyTablePanel feePenaltyPanel;

	/**
	 * 
	 */
	public ChargesTabPanel() {
		feePenaltyPanel = new LoanFeePenaltyTablePanel();
		setMargin(true);
		setSpacing(true);
		addComponent(feePenaltyPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		feePenaltyPanel.assignValues(contract);
	}
	
}
