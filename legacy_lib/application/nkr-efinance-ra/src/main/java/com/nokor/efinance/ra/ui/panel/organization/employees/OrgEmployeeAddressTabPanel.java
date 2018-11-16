package com.nokor.efinance.ra.ui.panel.organization.employees;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class OrgEmployeeAddressTabPanel extends AbstractTabPanel implements FinServicesHelper, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 133285426617279649L;
	
	private Button btnSave;
	
	private AddressInfoPanel addressInfoPanel;
	private Employee employee;
	private Address address;
	private VerticalLayout messagePanel;

	@Override
	protected Component createForm() {
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		addressInfoPanel = new AddressInfoPanel();
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(new MarginInfo(false, false, true, false));
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(addressInfoPanel);
		
		return mainLayout;
	}
	
	/**
	 * AssignValue
	 * @param employee
	 */
	public void assignValue(Employee employee) {
		this.employee = employee;
		if (employee.getAddress() != null) {
			address = employee.getAddress();
			addressInfoPanel.assignValue(address, null);
		} 
	}
	
	/**
	 * Validate
	 * @return
	 */
	public boolean validate() {
		removeMessagePanel();
		messagePanel.removeAllComponents();
		errors = addressInfoPanel.fullValidate();
		Label messageLabel;
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * Reset 
	 */
	public void reset() {
		super.reset();
		addressInfoPanel.reset();
	}
	

	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate() && employee.getId() != null) {
				address = addressInfoPanel.getContactAddress(address);
				ENTITY_SRV.saveOrUpdate(address);
				ENTITY_SRV.saveOrUpdate(employee);
				Notification.show("", I18N.message("msg.info.save.successfully"), Type.HUMANIZED_MESSAGE);
			}
		}
		
	}

}
