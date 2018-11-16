package com.nokor.efinance.gui.ui.panel.contract.user.reference;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.panel.applicant.individual.ReferenceInfoPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;

/**
 * Contact reference form panel
 * @author uhout.cheng
 */
public class UserReferenceForm extends AbstractTabPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 471117801249245988L;
	
	private ReferenceInfoPanel referenceInfoPanel;
	
	private Individual individual;
	private IndividualReferenceInfo individualReferenceInfo;
 	
	private Button btnSave;
	private Button btnBack;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}
	
	/** */
	public UserReferenceForm() {
		super.setMargin(false);
		setSpacing(true);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		addComponent(navigationPanel, 0);
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		referenceInfoPanel = new ReferenceInfoPanel();
		Panel panel = new Panel();
		panel.setContent(referenceInfoPanel);
		return panel;
    }
	
	/**
	 * 
	 * @param individual
	 * @param individualReferenceInfo
	 */
	public void assignValues(Individual individual, IndividualReferenceInfo individualReferenceInfo) {
		this.individual = individual;
		if (individualReferenceInfo == null) {
			individualReferenceInfo = IndividualReferenceInfo.createInstance();
		} 
		referenceInfoPanel.assignValues(individualReferenceInfo);
		this.individualReferenceInfo = individualReferenceInfo;
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		errors.addAll(referenceInfoPanel.isValid());
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}

	/**
	 * 
	 */
	public void removedMessagePanel() {
		super.removeErrorsPanel();
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		super.removeErrorsPanel();
		referenceInfoPanel.reset();
	}
	
	/**
	 * Event to Click Add button Address,Contact,Reference
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (isValid()) {
				referenceInfoPanel.setReferenceInfo(individual, individualReferenceInfo);
				displaySuccess();
			} 
		}
	}
	
}
