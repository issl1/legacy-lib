package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;

/**
 * 
 * @author uhout.cheng
 */
public class ApplicationDateValueRenderer extends EntityColumnRenderer {
	
	/** */
	private static final long serialVersionUID = -7157268505129189634L;

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
	 */
	@Override
	public Object getValue() {
		final Contract contract = (Contract) getEntity();
		return ContractUtils.getApplicationDate(contract);
	}
}
