package com.nokor.efinance.gui.ui.panel.payment.blocked.identify.locksplit;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment.ColPhoneLockSplitTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;

/**
 * 
 * @author uhout.cheng
 */
public class IdentifyPaymentLockSplitPanel extends AbstractControlPanel implements MLockSplit, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -108841344010619817L;

	private ColPhoneLockSplitTablePanel colPhoneLockSplitTablePanel;
	
	/**
	 * 
	 */
	public IdentifyPaymentLockSplitPanel() {
		colPhoneLockSplitTablePanel = new ColPhoneLockSplitTablePanel(true);
		addComponent(colPhoneLockSplitTablePanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		colPhoneLockSplitTablePanel.assignValue(contract);
	}
	
}
