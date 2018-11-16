package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 551543145668095424L;
	
	private ColPhoneLockSplitTopPanel colPhoneLockSplitTopPanel;
	private ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitPanel() {
		colPhoneLockSplitTabPanel = new ColPhoneLockSplitTabPanel();
		colPhoneLockSplitTopPanel = new ColPhoneLockSplitTopPanel(colPhoneLockSplitTabPanel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(colPhoneLockSplitTopPanel);
		mainLayout.addComponent(colPhoneLockSplitTabPanel);
		
		addComponent(mainLayout);
	}
	
	/**
	 * AssignValues
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		colPhoneLockSplitTopPanel.assignValues(contract);
		colPhoneLockSplitTabPanel.assignValue(contract);
	}

}
