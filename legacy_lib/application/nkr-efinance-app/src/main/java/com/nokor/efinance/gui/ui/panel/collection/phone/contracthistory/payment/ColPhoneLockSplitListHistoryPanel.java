package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitListHistoryPanel extends AbstractControlPanel{

	/** */
	private static final long serialVersionUID = -289678110697078757L;
	
	private ListHistoryDetailPaymentPanel historyDetailPaymentPanel;
	private ListHistoryTablePanel historyTablePanel;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitListHistoryPanel() {
		historyDetailPaymentPanel = new ListHistoryDetailPaymentPanel();
		historyTablePanel = new ListHistoryTablePanel(historyDetailPaymentPanel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(historyTablePanel);
		mainLayout.addComponent(historyDetailPaymentPanel);
		
		addComponent(mainLayout);
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		historyTablePanel.assignValues(contract);
	}

}
