package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;

public class WizardCancelledEvent extends AbstractWizardEvent {

    /**	 */
	private static final long serialVersionUID = -6747264833520891248L;

	public WizardCancelledEvent(Wizard source) {
        super(source);
    }

}
