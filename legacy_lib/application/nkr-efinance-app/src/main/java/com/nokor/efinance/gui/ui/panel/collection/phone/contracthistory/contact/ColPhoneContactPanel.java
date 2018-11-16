package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import com.nokor.efinance.core.contract.model.Contract;
import com.vaadin.ui.VerticalLayout;


/**
 * Collection phone contact panel
 * @author uhout.cheng
 */
public class ColPhoneContactPanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = 4073127940116997870L;
	
//	private ColPhoneContactHistoryPanel historyPanel;
	private ColContactFollowUpHistoryPanel historyPanel;
//	private ColPhoneContactResultPanel colPhoneResultPanel;
//	private CallCenterResultFormPanel callCenterResultPanel;
	
	/**
	 * 
	 * @param isCallIn
	 */
	public ColPhoneContactPanel(boolean isCallIn) {
		historyPanel = new ColContactFollowUpHistoryPanel(isCallIn);
//		colPhoneResultPanel = new ColPhoneContactResultPanel();
//		callCenterResultPanel = new CallCenterResultFormPanel();
		addComponent(historyPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assingValues(Contract contract) {
		historyPanel.assingValues(contract);
//		colPhoneResultPanel.assingValues(contract);
//		callCenterResultPanel.assignValues(contract);
	}
	
	/**
	 * 
	 */
	public void reset() {
		historyPanel.reset();
//		colPhoneResultPanel.reset();
	}
	
}
