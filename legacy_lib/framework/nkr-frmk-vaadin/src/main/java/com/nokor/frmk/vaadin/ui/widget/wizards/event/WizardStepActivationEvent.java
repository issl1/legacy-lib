package com.nokor.frmk.vaadin.ui.widget.wizards.event;

import com.nokor.frmk.vaadin.ui.widget.wizards.Wizard;
import com.nokor.frmk.vaadin.ui.widget.wizards.WizardStep;

public class WizardStepActivationEvent extends AbstractWizardEvent {

    /**	 */
	private static final long serialVersionUID = -573232464446072781L;
	
	private final WizardStep activatedStep;

    /**
     * @param source
     * @param activatedStep
     */
    public WizardStepActivationEvent(Wizard source, WizardStep activatedStep) {
        super(source);
        this.activatedStep = activatedStep;
    }

    /**
     * Returns the {@link VWizardStep} that was the activated.
     * 
     * @return the activated {@link VWizardStep}.
     */
    public WizardStep getActivatedStep() {
        return activatedStep;
    }

}