package com.nokor.efinance.gui.ui.panel.contract.dues.summary;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;


/**
 * 
 * @author uhout.cheng
 */
public class SummaryDuePanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 7207690139613239726L;
	
	private SummaryDueDetailPanel detailPanel;
	private SummaryDueARTablePanel arTablePanel;

	/**
	 * 
	 */
	public SummaryDuePanel() {
		detailPanel = new SummaryDueDetailPanel();
		arTablePanel = new SummaryDueARTablePanel();
		
		setMargin(true);
		setSpacing(true);
		addComponent(detailPanel);
		addComponent(arTablePanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		detailPanel.assignValues(contract);
		arTablePanel.assignValues(contract);
	}
	
}
