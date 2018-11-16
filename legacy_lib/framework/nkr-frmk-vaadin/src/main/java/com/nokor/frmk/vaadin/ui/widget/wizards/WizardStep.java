package com.nokor.frmk.vaadin.ui.widget.wizards;

import com.vaadin.ui.Component;

/**
 * @author ly.youhort
 *
 */
public interface WizardStep {

	/**
     * Returns the caption of this WizardStep.
     * 
     * @return the caption of this WizardStep.
     */
    String getCaption();
    
	/**
	* Returns the {@link Component} that is to be used as the actual content of
	* this WizardStep.
	*
	* @return the content of this WizardStep as a Component.
	*/
	Component getContent();

	/**
	 * Is step valid
	 * @return
	 */
	boolean isValid();
	
}
