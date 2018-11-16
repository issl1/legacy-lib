package com.nokor.efinance.gui.ui.panel.contract.user.address;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;

/**
 * Contact address form panel
 * @author uhout.cheng
 */
public class UserContactAddressForm extends AbstractTabPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -2592205212893916591L;
	
	private Button btnSave;
	private Button btnBack;
	
	private AddressInfoPanel addressInfoPanel;
	private Individual individual;
	private Address address;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}
	
	/** */
	public UserContactAddressForm() {
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
		addressInfoPanel = new AddressInfoPanel();
		Panel panel = new Panel();
		panel.setContent(addressInfoPanel);
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
				Address address = this.address;
				if (address != null) {
					ENTITY_SRV.saveOrUpdate(addressInfoPanel.getContactAddress(address));
				} else {
					address = Address.createInstance();
					ENTITY_SRV.saveOrUpdate(addressInfoPanel.getContactAddress(address));
					IndividualAddress individualAddress = EntityFactory.createInstance(IndividualAddress.class);
					individualAddress.setIndividual(individual);
					individualAddress.setAddress(address);
					ENTITY_SRV.saveOrUpdate(address);
					ENTITY_SRV.saveOrUpdate(individualAddress);
				}
				displaySuccess();
			}
		}
	}
	
	/**
	 * 
	 * @param address
	 */
	public void assignValuesToControls(Address address) {
		this.address = address;
		Applicant app = APP_SRV.getApplicantCategory(EApplicantCategory.INDIVIDUAL, individual.getId());
		addressInfoPanel.assignValue(address, app);
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
		this.address = null;
		super.removeErrorsPanel();
		addressInfoPanel.reset();
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isValid() {
		super.removeErrorsPanel();
		errors.addAll(addressInfoPanel.fullValidate());
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
}
