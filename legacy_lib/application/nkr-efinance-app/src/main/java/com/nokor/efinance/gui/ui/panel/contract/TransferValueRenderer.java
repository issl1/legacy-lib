package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;

public class TransferValueRenderer extends EntityColumnRenderer {
	
	/**
	 */
	private static final long serialVersionUID = -6994848391489757475L;

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
	 */
	@Override
	public Object getValue() {
		final Contract contract = (Contract) getEntity();
		return contract.isTransfered() ? "TRANSFER" : "NEW";
	}
}
