package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.EResidenceType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Address Information Panel
 * @author buntha.chea
 * */
public class AddressInfoPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	
	//Contract Information Path
	private ERefDataComboBox<ETypeAddress> cbxAddressType;
	private ERefDataComboBox<EResidenceStatus> cbxResidenceStatus;
	private ERefDataComboBox<EResidenceType> cbxResidenceType;
	private TextField txtTimeLiveInYear;
	private TextField txtTimeLiveInMonth;
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	private AddressPanel addressPanel;
	
	/** */
	public AddressInfoPanel() {
		super.setMargin(false);
		setSizeFull();
	}	
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(190, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxAddressType = getERefDataComboBox(ETypeAddress.valuesOfApplicants());
		cbxResidenceStatus = getERefDataComboBox(EResidenceStatus.values());
		cbxResidenceType = getERefDataComboBox(EResidenceType.values());
		txtTimeLiveInYear = ComponentFactory.getTextField(false, 60, 50);
		txtTimeLiveInMonth = ComponentFactory.getTextField(false, 60, 50);
		
		//address 
		addressPanel = new AddressPanel();
		addressPanel.setMargin(false);
		
		return createContactInformaionPanel();
	}
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout createContactInformaionPanel(){
		String template = "contactInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("address.type")), "lblAddressType");
		customLayout.addComponent(cbxAddressType, "cbxAddressType");
		customLayout.addComponent(new Label(I18N.message("living.period")), "lblTimeLive");
		customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
        customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
		customLayout.addComponent(txtTimeLiveInYear, "txtTimeLiveInYear");
		customLayout.addComponent(txtTimeLiveInMonth, "txtTimeLiveInMonth");
		customLayout.addComponent(new Label(I18N.message("residence.status")), "lblResidenceStatus");
		customLayout.addComponent(cbxResidenceStatus, "cbxResidenceStatus");
		customLayout.addComponent(new Label(I18N.message("residence.type")), "lblResidenceType");
		customLayout.addComponent(cbxResidenceType, "cbxResidenceType");
		
		VerticalLayout addressLayout = new VerticalLayout();
		addressLayout.setSpacing(true);
		addressLayout.addComponent(customLayout);
		
		VerticalLayout contactInfoLayout = new VerticalLayout();
		contactInfoLayout.addComponent(addressPanel);
		contactInfoLayout.addComponent(addressLayout);
		
		return contactInfoLayout;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	public Address getContactAddress(Address address) {
		address.setType(cbxAddressType.getSelectedEntity());
		address.setTimeAtAddressInYear(getInteger(txtTimeLiveInYear));
		address.setTimeAtAddressInMonth(getInteger(txtTimeLiveInMonth));
		address.setResidenceStatus(cbxResidenceStatus.getSelectedEntity());
		address.setResidenceType(cbxResidenceType.getSelectedEntity());
		addressPanel.getAddress(address);
		return address;
	}
	
	/**
	 * 
	 * @param addressInfoPanels
	 * @param applicant
	 * @return
	 */
	public Applicant getContactApplicant(List<AddressInfoPanel> addressInfoPanels, Applicant applicant) {
		if (applicant != null) {
			List<IndividualAddress> individualAddresses = applicant.getIndividual().getIndividualAddresses();
			List<IndividualAddress> indAddresses = new ArrayList<>();
			int numAddAddressInfo = addressInfoPanels.size() - individualAddresses.size();
			boolean isAddNewAddrsssInfo = false;
			int index = 0;
			for (int i=0; i<= numAddAddressInfo; i++) {
				if (!isAddNewAddrsssInfo) {
					//Update All Applicant Address
					for (IndividualAddress individualAddress : individualAddresses) {
						Address Address = individualAddress.getAddress();
						if (Address != null) {
							addressInfoPanels.get(index).addressPanel.getAddress(Address);
							Address.setType(addressInfoPanels.get(index).cbxAddressType.getSelectedEntity());
							Address.setTimeAtAddressInYear(getInteger(addressInfoPanels.get(index).txtTimeLiveInYear));
							Address.setTimeAtAddressInMonth(getInteger(addressInfoPanels.get(index).txtTimeLiveInMonth));
							Address.setResidenceStatus(addressInfoPanels.get(index).cbxResidenceStatus.getSelectedEntity());
							Address.setResidenceType(addressInfoPanels.get(index).cbxResidenceType.getSelectedEntity());
							entityService.saveOrUpdate(Address);
							individualAddress.setIndividual(applicant.getIndividual());
							individualAddress.setAddress(Address);
							indAddresses.add(individualAddress);
							index++;
							}
						}
						
						isAddNewAddrsssInfo = true;
					} else {
						//Add New Applicant Address After update if have
						Address Address = new Address();
						addressInfoPanels.get(index).addressPanel.getAddress(Address);
						Address.setType(addressInfoPanels.get(index).cbxAddressType.getSelectedEntity());
						Address.setTimeAtAddressInYear(getInteger(addressInfoPanels.get(index).txtTimeLiveInYear));
						Address.setTimeAtAddressInMonth(getInteger(addressInfoPanels.get(index).txtTimeLiveInMonth));
						Address.setResidenceStatus(addressInfoPanels.get(index).cbxResidenceStatus.getSelectedEntity());
						Address.setResidenceType(addressInfoPanels.get(index).cbxResidenceType.getSelectedEntity());
						
						IndividualAddress individualAddress = new IndividualAddress();
						individualAddress.setAddress(Address);
						individualAddress.setIndividual(applicant.getIndividual());
						//applicantAddresses.add(applicantAddress);
						
						if (Address.getType() != null) {
							entityService.saveOrUpdate(Address);
							entityService.saveOrUpdate(individualAddress);
						}
						indAddresses.add(individualAddress);
						index++;
					}
				}
			applicant.getIndividual().setIndividualAddresses(indAddresses);
			} 
		return applicant;
	}
	
	/**
	 * 
	 * @param address
	 * @param applicant
	 */
	public void assignValue(Address address, Applicant applicant) {
		if (applicant != null) {
			addressPanel.setIndividual(applicant.getIndividual());
		}
		if (address != null) {
			cbxAddressType.setSelectedEntity(address.getType());
			cbxResidenceStatus.setSelectedEntity(address.getResidenceStatus());
			cbxResidenceType.setSelectedEntity(address.getResidenceType());
			txtTimeLiveInMonth.setValue(getDefaultString(address.getTimeAtAddressInMonth()));
			txtTimeLiveInYear.setValue(getDefaultString(address.getTimeAtAddressInYear()));
		}
		addressPanel.assignValue(address);
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxAddressType, "address.type");
		checkMandatorySelectField(cbxResidenceStatus, "residence.status");
		checkMandatoryField(txtTimeLiveInMonth, "months");
		checkMandatoryField(txtTimeLiveInYear, "years");
		checkIntegerField(txtTimeLiveInYear, "years");
		checkIntegerField(txtTimeLiveInMonth, "months");
		addressPanel.isValid();
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		removeErrorsPanel();
		errors.addAll(addressPanel.fullValidate());
		checkMandatorySelectField(cbxAddressType, "address.type");
		checkMandatorySelectField(cbxResidenceStatus, "residence.status");
		checkMandatoryField(txtTimeLiveInMonth, "months");
		checkMandatoryField(txtTimeLiveInYear, "years");
		checkIntegerField(txtTimeLiveInYear, "years");
		checkIntegerField(txtTimeLiveInMonth, "months");
		return errors;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxAddressType.setSelectedEntity(null);
		cbxResidenceStatus.setSelectedEntity(null);
		cbxResidenceType.setSelectedEntity(null);
		txtTimeLiveInYear.setValue("");
		txtTimeLiveInMonth.setValue("");
		addressPanel.reset();
	}
}
