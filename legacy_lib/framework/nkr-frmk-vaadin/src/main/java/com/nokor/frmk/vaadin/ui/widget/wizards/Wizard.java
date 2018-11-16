package com.nokor.frmk.vaadin.ui.widget.wizards;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardCancelledEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardCompletedEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardProgressListener;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardStepActivationEvent;
import com.nokor.frmk.vaadin.ui.widget.wizards.event.WizardStepSetChangedEvent;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for displaying multi-step wizard style user interface.
 * 
 * <p>
 * The steps of the wizard must be implementations of the {@link WizardStep}
 * interface. Use the {@link #addStep(WizardStep)} method to add these steps in
 * the same order they are supposed to be displayed.
 * </p>
 * 
 * <p>
 * The wizard also supports navigation through URI fragments. This feature is
 * disabled by default, but you can enable it using
 * {@link #setUriFragmentEnabled(boolean)} method. Each step will get a
 * generated identifier that is used as the URI fragment. If you wish to
 * override these with your own identifiers, you can add the steps using the
 * overloaded {@link #addStep(WizardStep, String)} method.
 * </p>
 * 
 * <p>
 * To react on the progress, cancellation or completion of this {@code Wizard}
 * you should add one or more listeners that implement the
 * {@link WizardProgressListener} interface. These listeners are added using the
 * {@link #addListener(WizardProgressListener)} method and removed with the
 * {@link #removeListener(WizardProgressListener)}.
 * </p>
 */
public class Wizard extends CustomComponent implements UriFragmentChangedListener {

    /**	 */
	private static final long serialVersionUID = -2460862097936198983L;

    protected final List<WizardStep> wizardSteps = new ArrayList<WizardStep>();
    protected final Map<String, WizardStep> idMap = new HashMap<String, WizardStep>();

    protected WizardStep currentStep;
    protected WizardStep lastCompletedStep;

    private int stepIndex = 1;

    protected VerticalLayout mainLayout;
    protected HorizontalLayout footer;
    private Panel contentPanel;

    private Button nextButton;
    private Button backButton;
    private Button finishButton;
    private Button cancelButton;

    private Component header;
    private boolean uriFragmentEnabled = false;

    private static final Method WIZARD_ACTIVE_STEP_CHANGED_METHOD;
    private static final Method WIZARD_STEP_SET_CHANGED_METHOD;
    private static final Method WIZARD_COMPLETED_METHOD;
    private static final Method WIZARD_CANCELLED_METHOD;

    static {
        try {
            WIZARD_COMPLETED_METHOD = WizardProgressListener.class
                    .getDeclaredMethod("wizardCompleted",
                            new Class[] { WizardCompletedEvent.class });
            WIZARD_STEP_SET_CHANGED_METHOD = WizardProgressListener.class
                    .getDeclaredMethod("stepSetChanged",
                            new Class[] { WizardStepSetChangedEvent.class });
            WIZARD_ACTIVE_STEP_CHANGED_METHOD = WizardProgressListener.class
                    .getDeclaredMethod("activeStepChanged",
                            new Class[] { WizardStepActivationEvent.class });
            WIZARD_CANCELLED_METHOD = WizardProgressListener.class
                    .getDeclaredMethod("wizardCancelled",
                            new Class[] { WizardCancelledEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in Wizard", e);
        }
    }

    /**
     * 
     */
    public Wizard() {
        setStyleName("wizard");
        init();
    }

    /**
     * Initialization
     */
    private void init() {
        mainLayout = new VerticalLayout();
        setCompositionRoot(mainLayout);
        setSizeFull();

        contentPanel = new Panel();
        contentPanel.setSizeFull();

        initControlButtons();

        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(cancelButton);
        footer.addComponent(backButton);
        footer.addComponent(nextButton);
        footer.addComponent(finishButton);

        mainLayout.addComponent(contentPanel);
        mainLayout.addComponent(footer);
        mainLayout.setComponentAlignment(footer, Alignment.BOTTOM_RIGHT);

        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(contentPanel, 1.0f);

        initDefaultHeader();
    }

    /**
     * Init control buttons
     */
    private void initControlButtons() {
        nextButton = new Button(I18N.message("next"));
        nextButton.addClickListener(new Button.ClickListener() {
            /**	 */
			private static final long serialVersionUID = 5921160956978649923L;

			public void buttonClick(ClickEvent event) {
                next();
            }
        });

        backButton = new Button(I18N.message("back"));
        backButton.addClickListener(new Button.ClickListener() {
            /**	 */
			private static final long serialVersionUID = -7066322526367207552L;

			public void buttonClick(ClickEvent event) {
                back();
            }
        });

        finishButton = new Button(I18N.message("finish"));
        finishButton.addClickListener(new Button.ClickListener() {
            /**	 */
			private static final long serialVersionUID = -3808872950174610125L;

			public void buttonClick(ClickEvent event) {
                finish();
            }
        });
        finishButton.setEnabled(false);

        cancelButton = new Button(I18N.message("cancel"));
        cancelButton.addClickListener(new Button.ClickListener() {
            /**	 */
			private static final long serialVersionUID = -7808376690983625464L;

			public void buttonClick(ClickEvent event) {
                cancel();
            }
        });
    }

    /**
     * Init default header
     */
    private void initDefaultHeader() {
        WizardProgressBar progressBar = new WizardProgressBar(this);
        addListener(progressBar);
        setHeader(progressBar);
    }

    /**
     * Set uriFragmentEnabled
     * @param uriFragmentEnabled
     */
    public void setUriFragmentEnabled(boolean uriFragmentEnabled) {
        if (uriFragmentEnabled) {
            Page.getCurrent().addUriFragmentChangedListener(this);
        }
        this.uriFragmentEnabled = uriFragmentEnabled;
    }

    /**
     * Is uri fragment enabled
     * @return uriFragmentEnabled
     */
    public boolean isUriFragmentEnabled() {
        return uriFragmentEnabled;
    }

    /**
     * Sets a {@link Component} that is displayed on top of the actual content.
     * Set to {@code null} to remove the header altogether.
     * 
     * @param newHeader {@link Component} to be displayed on top of the actual content or {@code null} to remove the header.
     */
    public void setHeader(Component newHeader) {
        if (header != null) {
            if (newHeader == null) {
                mainLayout.removeComponent(header);
            } else {
                mainLayout.replaceComponent(header, newHeader);
            }
        } else {
            if (newHeader != null) {
                mainLayout.addComponentAsFirst(newHeader);
            }
        }
        this.header = newHeader;
    }

    /**
     * Returns a {@link Component} that is displayed on top of the actual
     * content or {@code null} if no header is specified.
     * 
     * <p>
     * By default the header is a {@link WizardProgressBar} component that is
     * also registered as a {@link WizardProgressListener} to this Wizard.
     * </p>
     * 
     * @return {@link Component} that is displayed on top of the actual content or {@code null}.
     */
    public Component getHeader() {
        return header;
    }

    /**
     * Adds a step to this Wizard with the given identifier. The used {@code id}
     * must be unique or an {@link IllegalArgumentException} is thrown. If you
     * don't wish to explicitly provide an identifier, you can use the
     * {@link #addStep(WizardStep)} method.
     * 
     * @param step
     * @param id
     * @throws IllegalStateException if the given {@code id} already exists.
     */
    public void addStep(WizardStep step, String id) {
        if (idMap.containsKey(id)) {
            throw new IllegalArgumentException(String.format("A step with given id %s already exists. You must use unique identifiers for the steps.", id));
        }
        wizardSteps.add(step);
        idMap.put(id, step);
        updateButtons();
        
        if (currentStep == null && !wizardSteps.isEmpty()) {
            // activate the first step
            activateStep(wizardSteps.get(0));
        }
        // notify listeners
        fireEvent(new WizardStepSetChangedEvent(this));
    }
    
    /**
     * Adds a step to this Wizard. The WizardStep will be assigned an identifier
     * automatically. If you wish to provide an explicit identifier for your
     * WizardStep, you can use the {@link #addStep(WizardStep, String)} method
     * instead.
     * 
     * @param step
     */
    public void addStep(WizardStep step) {
        addStep(step, "wizard-step-" + stepIndex++);
    }

    /**
     * Add Listener
     * @param listener
     */
    public void addListener(WizardProgressListener listener) {
        addListener(WizardCompletedEvent.class, listener,
                WIZARD_COMPLETED_METHOD);
        addListener(WizardStepActivationEvent.class, listener,
                WIZARD_ACTIVE_STEP_CHANGED_METHOD);
        addListener(WizardStepSetChangedEvent.class, listener,
                WIZARD_STEP_SET_CHANGED_METHOD);
        addListener(WizardCancelledEvent.class, listener,
                WIZARD_CANCELLED_METHOD);
    }

    /**
     * Remove listener
     * @param listener
     */
    public void removeListener(WizardProgressListener listener) {
        removeListener(WizardCompletedEvent.class, listener,
                WIZARD_COMPLETED_METHOD);
        removeListener(WizardStepActivationEvent.class, listener,
                WIZARD_ACTIVE_STEP_CHANGED_METHOD);
        removeListener(WizardStepSetChangedEvent.class, listener,
                WIZARD_STEP_SET_CHANGED_METHOD);
        removeListener(WizardCancelledEvent.class, listener,
                WIZARD_CANCELLED_METHOD);
    }

    /**
     * Get wizard steps list
     * @return
     */
    public List<WizardStep> getWizardSteps() {
        return Collections.unmodifiableList(wizardSteps);
    }

    /**
     * Returns {@code true} if the given step is already completed by the user.
     * @param step step to check for completion.
     * @return {@code true} if the given step is already completed.
     */
    public boolean isCompleted(WizardStep step) {
        return wizardSteps.indexOf(step) < wizardSteps.indexOf(currentStep);
    }

    /**
     * Returns {@code true} if the given step is the currently active step.
     * @param step step to check for.
     * @return {@code true} if the given step is the currently active step.
     */
    public boolean isActive(WizardStep step) {
        return (step == currentStep);
    }

    /**
     * Update buttons
     */
    private void updateButtons() {
        if (isLastStep(currentStep)) {
            finishButton.setEnabled(true);
            nextButton.setEnabled(false);
        } else {
            finishButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
        backButton.setEnabled(!isFirstStep(currentStep));
    }

    /**
     * Get next button
     * @return
     */
    public Button getNextButton() {
        return nextButton;
    }

    /**
     * Get back button
     * @return
     */
    public Button getBackButton() {
        return backButton;
    }

    /**
     * Get finish button
     * @return
     */
    public Button getFinishButton() {
        return finishButton;
    }

    /**
     * Get cancel button
     * @return
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * @param step
     */
    protected void activateStep(WizardStep step) {
        if (step == null) {
            return;
        }
        if (currentStep != null) {
            if (currentStep.equals(step)) {
                // already active
                return;
            }
            // ask if we're allowed to move
            /*boolean advancing = wizardSteps.indexOf(step) > wizardSteps.indexOf(currentStep);
            if (advancing) {
                if (!currentStep.onNext()) {
                    return;
                }
            } else {
                if (!currentStep.onBack()) {
                    return;
                }
            }*/
            // keep track of the last step that was completed
            int currentIndex = wizardSteps.indexOf(currentStep);
            if (lastCompletedStep == null
                    || wizardSteps.indexOf(lastCompletedStep) < currentIndex) {
                lastCompletedStep = currentStep;
            }
        }

        contentPanel.setContent(step.getContent());
        currentStep = step;

        updateUriFragment();
        updateButtons();
        fireEvent(new WizardStepActivationEvent(this, step));
    }

    /**
     * Active step
     * @param id
     */
    protected void activateStep(String id) {
    	WizardStep step = idMap.get(id);
        if (step != null) {
            // check that we don't go past the lastCompletedStep by using the id
            int lastCompletedIndex = lastCompletedStep == null ? -1 : wizardSteps.indexOf(lastCompletedStep);
            int stepIndex = wizardSteps.indexOf(step);
            if (lastCompletedIndex < stepIndex) {
                activateStep(lastCompletedStep);
            } else {
                activateStep(step);
            }
        }
    }

    /**
     * Get Id
     * @param step
     * @return
     */
    protected String getId(WizardStep step) {
        for (Map.Entry<String, WizardStep> entry : idMap.entrySet()) {
            if (entry.getValue().equals(step)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Update Uri fragment
     */
    private void updateUriFragment() {
        if (isUriFragmentEnabled()) {
            String currentStepId = getId(currentStep);
            if (currentStepId != null && currentStepId.length() > 0) {
            	Page.getCurrent().setUriFragment(currentStepId, false);
            } else {
            	Page.getCurrent().setUriFragment(null, false);
            }
        }
    }

    /**
     * Is first step ?
     * @param step
     * @return
     */
    protected boolean isFirstStep(WizardStep step) {
        if (step != null) {
            return wizardSteps.indexOf(step) == 0;
        }
        return false;
    }

    /**
     * Is last step ?
     * @param step
     * @return
     */
    protected boolean isLastStep(WizardStep step) {
        if (step != null && !wizardSteps.isEmpty()) {
            return wizardSteps.indexOf(step) == (wizardSteps.size() - 1);
        }
        return false;
    }

    /**
     * Cancels this Wizard triggering a {@link WizardCancelledEvent}. This
     * method is called when user clicks the cancel button.
     */
    public void cancel() {
        fireEvent(new WizardCancelledEvent(this));
    }

    /**
     * Triggers a {@link WizardCompletedEvent} if the current step is the last
     * step and it allows advancing (see {@link WizardStep#onNext()}). This
     * method is called when user clicks the finish button.
     */
    public void finish() {
        if (isLastStep(currentStep)) { //&& currentStep.onNext()) {
            // next (finish) allowed -> fire complete event
            fireEvent(new WizardCompletedEvent(this));
        }
    }

    /**
     * Activates the next {@link WizardStep} if the current step allows
     * advancing (see {@link WizardStep#onNext()}) or calls the
     * {@link #finish()} method the current step is the last step. This method
     * is called when user clicks the next button.
     */
    public void next() {
    	if (currentStep.isValid()) {
	        if (isLastStep(currentStep)) {
	            finish();
	        } else {
	            int currentIndex = wizardSteps.indexOf(currentStep);
	            activateStep(wizardSteps.get(currentIndex + 1));
	        }
    	}
    }

    /**
     * Activates the previous {@link WizardStep} if the current step allows
     * going back (see {@link WizardStep#onBack()}) and the current step is not
     * the first step. This method is called when user clicks the back button.
     */
    public void back() {
    	if (currentStep.isValid()) {
	        int currentIndex = wizardSteps.indexOf(currentStep);
	        if (currentIndex > 0) {
	            activateStep(wizardSteps.get(currentIndex - 1));
	        }
    	}
    }

    /**
     * Removes the given step from this Wizard. An {@link IllegalStateException}
     * is thrown if the given step is already completed or is the currently
     * active step.
     * @param stepToRemove the step to remove.
     * @see #isCompleted(WizardStep)
     * @see #isActive(WizardStep)
     */
    public void removeStep(WizardStep stepToRemove) {
        if (idMap.containsValue(stepToRemove)) {
            for (Map.Entry<String, WizardStep> entry : idMap.entrySet()) {
                if (entry.getValue().equals(stepToRemove)) {
                    // delegate the actual removal to the overloaded method
                    removeStep(entry.getKey());
                    return;
                }
            }
        }
    }

    /**
     * Removes the step with given id from this Wizard. An
     * {@link IllegalStateException} is thrown if the given step is already
     * completed or is the currently active step.
     * @param id identifier of the step to remove.
     * @see #isCompleted(WizardStep)
     * @see #isActive(WizardStep)
     */
    public void removeStep(String id) {
        if (idMap.containsKey(id)) {
        	WizardStep stepToRemove = idMap.get(id);
            if (isCompleted(stepToRemove)) {
                throw new IllegalStateException("Already completed step cannot be removed.");
            }
            if (isActive(stepToRemove)) {
                throw new IllegalStateException(
                        "Currently active step cannot be removed.");
            }

            idMap.remove(id);
            wizardSteps.remove(stepToRemove);
            // notify listeners
            fireEvent(new WizardStepSetChangedEvent(this));
        }
    }

	@Override
	public void uriFragmentChanged(UriFragmentChangedEvent event) {
		if (isUriFragmentEnabled()) {
            String fragment = event.getUriFragment();
            if (fragment.equals("") && !wizardSteps.isEmpty()) {
                // empty fragment -> set the fragment of first step
                Page.getCurrent().setUriFragment(getId(wizardSteps.get(0)));
            } else {
                activateStep(fragment);
            }
        }
	}

}
