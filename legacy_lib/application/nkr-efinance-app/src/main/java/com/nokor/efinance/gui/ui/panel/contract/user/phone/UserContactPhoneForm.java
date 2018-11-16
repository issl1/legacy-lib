package com.nokor.efinance.gui.ui.panel.contract.user.phone;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;

/**
 * Contact phone form panel
 * @author uhout.cheng
 */
public class UserContactPhoneForm extends AbstractTabPanel implements ClickListener {
	
	/** */
	private static final long serialVersionUID = 3883315434533548549L;
	
	private Button btnSave;
	private Button btnBack;
	
	private ContactInfoPanel contactInfoPanel;
	private Individual individual;
	private ContactInfo contactInfo;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}
	
	/** */
	public UserContactPhoneForm() {
		setMargin(false);
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
		contactInfoPanel = new ContactInfoPanel();
		Panel panel = new Panel();
		panel.setContent(contactInfoPanel);
		return panel;
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		this.individual = individual;
	}
	
	/**
	 * Event to Click Add button Address,Contact,Reference
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			if (isValid()) {
				ContactInfo contactInfo = this.contactInfo;
				if (contactInfo != null) {
					ENTITY_SRV.saveOrUpdate(contactInfoPanel.getContactInfomation(contactInfo));
				} else {
					contactInfo = ContactInfo.createInstance();
					ENTITY_SRV.saveOrUpdate(contactInfoPanel.getContactInfomation(contactInfo));
					IndividualContactInfo newIndividualContactInfo = IndividualContactInfo.createInstance();
					newIndividualContactInfo.setIndividual(individual);
					newIndividualContactInfo.setContactInfo(contactInfo);
					ENTITY_SRV.saveOrUpdate(contactInfo);
					ENTITY_SRV.saveOrUpdate(newIndividualContactInfo);
					this.contactInfo = contactInfo;
				}
				displaySuccess();
			}
		}
	}
	
	/**
	 * 
	 * @param contactInfo
	 */
	public void assignValuesToControls(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
		contactInfoPanel.assignValue(contactInfo);
	}
	
	/**
	 * 
	 */
	public void removedMessagePanel() {
		super.removeErrorsPanel();
	}
	
	/**
	 * 
	 */
	public void reset() {
		this.contactInfo = null;
		super.removeErrorsPanel();
		contactInfoPanel.reset();
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isValid() {
		super.removeErrorsPanel();
		errors.addAll(contactInfoPanel.isValid());
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
}
