package com.nokor.efinance.gui.ui.panel.contract.promises;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;


/**
 * 
 * @author uhout.cheng
 */
public class PromisesPanel extends AbstractControlPanel {
	
	/** */
	private static final long serialVersionUID = 5478396427549489012L;
	
	private PromiseTablePanel promiseTablePanel;
	
	/**
	 * 
	 */
	public PromisesPanel() {
		promiseTablePanel = new PromiseTablePanel();
		addComponent(promiseTablePanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		promiseTablePanel.assignValues(contract);
	}
	
}
