package com.nokor.efinance.gui.ui.panel.collection.filter;

import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.gui.ui.panel.collection.ColPhoLeadReqContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public abstract class AbstractColPhoLeadReqConFilterPanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = 4130550787409756789L;

	protected final ColPhoLeadReqContractTablePanel contractTablePanel;
	
	/**
	 * Initialization
	 */
	public AbstractColPhoLeadReqConFilterPanel (final ColPhoLeadReqContractTablePanel contractTablePanel) {
		this.contractTablePanel = contractTablePanel;
	}
	
	public abstract ContractRestriction getRestrictions();
}
