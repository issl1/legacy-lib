package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;

public class StatusValueRenderer extends EntityColumnRenderer {
	
	/**
	 */
	private static final long serialVersionUID = 3633389774420510334L;

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
	 */
	@Override
	public Object getValue() {
		final Contract contract = (Contract) getEntity();
		return contract.getWkfSubStatus() != null ?  contract.getWkfSubStatus().getDescEn() : contract.getWkfStatus().getDescEn();
	}
}
