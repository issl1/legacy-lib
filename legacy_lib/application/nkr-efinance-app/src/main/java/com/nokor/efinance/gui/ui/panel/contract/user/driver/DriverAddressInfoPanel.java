package com.nokor.efinance.gui.ui.panel.contract.user.driver;

import com.nokor.efinance.core.applicant.model.Driver;
import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * Driver information address
 * @author uhout.cheng
 */
public class DriverAddressInfoPanel extends AbstractTabPanel implements FinServicesHelper, SaveClickListener, EditClickListener {
	
	/** */
	private static final long serialVersionUID = 3238304619455332006L;

	private AddressInfoPanel addressInfoPanel;
	
	private Driver driver;
	private Address address;
	
	private NavigationPanel navigationPanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addEditClickListener(this);
		addComponent(navigationPanel, 0);
		
		navigationPanel.getSaveClickButton().setVisible(false);
		
		addressInfoPanel = new AddressInfoPanel();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.addComponent(addressInfoPanel);
		Panel addressPanel = new Panel(mainLayout);
		
		return addressPanel;
	}
	
	/**
	 * 
	 * @param driver
	 */
	public void assignValues(Driver driver) {
		reset();
		this.driver = driver;
		this.address = driver.getAddress();
		if (this.address == null) {
			this.address = Address.createInstance();
		} 
		Contract contra = driver.getContract();
		addressInfoPanel.assignValue(this.address, contra == null ? null : contra.getApplicant());
		setEnableControls(navigationPanel.getSaveClickButton().isVisible());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		super.removeErrorsPanel();
		this.driver = null;
	}

	/**
	 * Save Entity
	 */
	private void saveEntity() {
		this.driver.setAddress(addressInfoPanel.getContactAddress(this.address));
		DRIVER_SRV.saveOrUpdateDriverAddress(this.driver);
	}

	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		errors.clear();
		errors.addAll(addressInfoPanel.fullValidate());
		return errors.isEmpty();
	}
	
	/**
	 * Set enable
	 * @param isEnable
	 */
	private void setEnableControls(boolean isEnable) {
		addressInfoPanel.setEnabled(isEnable);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		setEnableControls(true);
		navigationPanel.getEditClickButton().setVisible(false);
		navigationPanel.getSaveClickButton().setVisible(true);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		if (validate()) {
			try {
				saveEntity();
				displaySuccess();
				
				setEnableControls(false);
				navigationPanel.getEditClickButton().setVisible(true);
				navigationPanel.getSaveClickButton().setVisible(false);
			} catch (Exception ex) {
				errors.add(I18N.message("msg.error.technical"));
				errors.add(ex.getMessage());
			}
			if (!errors.isEmpty()) {
				displayErrorsPanel();
			}
		} else {
			displayErrorsPanel();
		}
	}
	
}
