package com.nokor.efinance.gui.ui.panel.collection.filter;

import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.gui.ui.panel.collection.ColContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 */
public abstract class AbstractContractFilterPanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = 7345728438342272496L;
	
	protected final ColContractTablePanel contractTablePanel;
	
	/**
	 * Initialization
	 */
	public AbstractContractFilterPanel (final ColContractTablePanel contractTablePanel) {
		this.contractTablePanel = contractTablePanel;
	}
	
	public abstract ContractRestriction getRestrictions();
}
