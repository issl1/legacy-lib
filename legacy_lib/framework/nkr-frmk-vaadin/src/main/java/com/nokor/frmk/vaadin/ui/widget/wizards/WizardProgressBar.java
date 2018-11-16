package com.nokor.frmk.vaadin.ui.widget.wizards;

import java.util.List;

import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardCancelledEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardCompletedEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardProgressListener;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardStepActivationEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardStepSetChangedEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * WizardProgressBar displays the progress bar for a {@link Wizard}.
 */
public class WizardProgressBar extends VerticalLayout implements WizardProgressListener {

    /**	 */
	private static final long serialVersionUID = -8459683454005524406L;
	private final Wizard wizard;
    private boolean completed = false;
    private WizardStep activeWizardStep;
    
    private HorizontalLayout captions = null;
    private Label lbProgessbar = null;
    
    /**
     * @param wizard
     */
    public WizardProgressBar(Wizard wizard) {
        this.wizard = wizard;
        addStyleName("v-wizardprogressbar");
        setWidth("100%");
        
        captions = new HorizontalLayout();
        captions.setSizeFull();
        captions.addStyleName("bar-captions");
        
        HorizontalLayout progessbar = new HorizontalLayout();
        progessbar.setWidth("100%");
        progessbar.addStyleName("bar-wrapper");
        lbProgessbar = new Label("<div>&nbsp;</div>", ContentMode.HTML);
        lbProgessbar.addStyleName("bar");
        progessbar.addComponent(lbProgessbar);
        
        addComponent(captions);
        addComponent(progessbar);
        updateState();
    }
    
    /**
     * Update state
     */
    public void updateState() {    	
    	// int offsetWidth = (int) captions.getWidth();
        
		final List<WizardStep> wizardSteps = wizard.getWizardSteps();
        int numberOfSteps = wizardSteps.size();
        // double stepWidth = offsetWidth / (double) numberOfSteps;
        int indexActive = 0;
        for (int i = 0; i < numberOfSteps; i++) {
        	WizardStep wizardStep = wizardSteps.get(i);
        	
        	ProgressBarItem item;
            if (captions.getComponentCount() > i) {
                // get the existing widget for updating
                item = (ProgressBarItem) captions.getComponent(i);
            } else {
                // create new widget and add it to the layout
                item = new ProgressBarItem(i + 1);
                captions.addComponent(item);
            }
            item.setCaption(wizardStep.getCaption());
            
            boolean active = false;
            if (activeWizardStep == wizardStep) {
            	active = true;
            	indexActive = i;
            } else {
            	active = false;
            }
            // update the barElement width according to the current step
            if (!completed && active) {
            	int pc = ((i + 1) * 100 / numberOfSteps);
            	for (int j = 0; j < pc; j++) {
            		lbProgessbar.setWidth(j + "%");
            	}
            }
            
            boolean first = (i == 0);
            boolean last = (i == wizardSteps.size() - 1);
            updateStyleNames(item, active, i > indexActive, first, last);
        }

        if (completed) {
        	lbProgessbar.setWidth("100%");
        }
    }

    /**
     * Active step changed
     * @param event
     */
    public void activeStepChanged(WizardStepActivationEvent event) {
    	activeWizardStep = event.getActivatedStep();
    	updateState();
    }

    /**
     * Step set changed
     * @param event
     */
    public void stepSetChanged(WizardStepSetChangedEvent event) {
    	updateState();
    }

    /**
     * Wizard completed
     * @param event
     */
    public void wizardCompleted(WizardCompletedEvent event) {
        completed = true;
        updateState();
    }

    /**
     * Wizard cancelled
     * @param event
     */
    public void wizardCancelled(WizardCancelledEvent event) {
    }

    
    /**
     * Update wizard style
     * @param wizardStep
     * @param item
     * @param first
     * @param last
     */
    private void updateStyleNames(ProgressBarItem item, boolean active, boolean completed, boolean first, boolean last) {
        if (completed) {
            item.addStyleName("completed");
        } else {
            item.removeStyleName("completed");
        }
        if (active) {
            item.addStyleName("current");
        } else {
            item.removeStyleName("current");
        }
        if (first) {
            item.addStyleName("first");
        } else {
            item.removeStyleName("first");
        }
        if (last) {
            item.addStyleName("last");
        } else {
            item.removeStyleName("last");
        }
    }
    
    /**
     * @author ly.youhort
     */
    private static class ProgressBarItem extends HorizontalLayout {
        /**	 */
		private static final long serialVersionUID = 5560269521366717083L;
		
		private String caption;
        private final int index;
        
        /**
         * @param index
         */
        public ProgressBarItem(int index) {
        	this.index = index;
        	addStyleName("step");
        }
        
        /**
         * Set caption
         * @param caption
         */
        public void setCaption(String caption) {
            if (this.caption == null || !this.caption.equals(caption)) {
                this.caption = caption;
                Label lbCaption = new Label(index + "." + caption);
                lbCaption.addStyleName("step-caption");
                addComponent(lbCaption);
            }
        }
    }
}
