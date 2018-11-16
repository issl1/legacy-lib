package com.nokor.efinance.gui.ui.panel.collection.filter.callcenter;

import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign.CallCenterLeaderContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public abstract class AbstractCallCenterContractFilterPanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = 608679503795941081L;

	protected final CallCenterLeaderContractTablePanel callCenterLeaderContractTablePanel;
	
	/**
	 * 
	 * @param callCenterLeaderContractTablePanel
	 */
	public AbstractCallCenterContractFilterPanel(CallCenterLeaderContractTablePanel callCenterLeaderContractTablePanel) {
		this.callCenterLeaderContractTablePanel = callCenterLeaderContractTablePanel;
	}
	
	public abstract ContractRestriction getRestrictions();
}
