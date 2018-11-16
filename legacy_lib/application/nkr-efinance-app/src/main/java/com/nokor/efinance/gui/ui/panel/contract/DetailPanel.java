package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Contract Detail Panel
 * @author sok.vina
 *
 */
public class DetailPanel extends AbstractTabPanel implements FMEntityField {
	
	private static final long serialVersionUID = 6849476207051876799L;

	public DetailPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		return new VerticalLayout();
	}
	
	/**
	 * Assign value to quotation 
	 * @param asset
	 */
	public void assignValues(Contract contract) {		 
	}
	
	/**
	 * Reset 
	 */
	public void reset() {
		assignValues(new Contract());
	}
}
