package com.nokor.efinance.core.contact.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerContactInfo;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class UserContactPhonePrimaryPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3442697884823515392L;
	private ContactInfo contactInfo;
	private Individual individual;
	private Dealer dealer;

	private ERefDataComboBox<ETypeContactInfo> cbxTypeContactInfo;
	private ERefDataComboBox<ETypeAddress> cbxTypeAddress;
	private TextField txtValue;
	private TextField txtRemark;
	private Button btnSavePrimery;
	
	private Button btnEditTypeContact;
	private Button btnEditTypeAddress;
	private Button btnEditValue;
	private Button btnEditRemark;
	
	public UserContactPhonePrimaryPanel() {
		
		cbxTypeAddress = new ERefDataComboBox<>(ETypeAddress.values());
		cbxTypeAddress.setVisible(false);
		cbxTypeAddress.setWidth(150, Unit.PIXELS);
		cbxTypeContactInfo = new ERefDataComboBox<>(getPhoneValues());
		cbxTypeContactInfo.setNullSelectionAllowed(false);
		cbxTypeContactInfo.setSelectedEntity(ETypeContactInfo.MOBILE);
		cbxTypeContactInfo.setWidth("80px");
		cbxTypeContactInfo.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 1485410432507231312L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					cbxTypeAddress.setVisible(true);
					cbxTypeAddress.setEnabled(true);
				} else {
					cbxTypeAddress.setVisible(false);
					btnEditTypeAddress.setVisible(false);
				}
			}
		});
		
		txtValue = ComponentFactory.getTextField();
		txtRemark = ComponentFactory.getTextField();
		btnSavePrimery = ComponentLayoutFactory.getButtonSave();
		btnSavePrimery.addClickListener(this);
		
		Label lblPrimeryPhoneNo = ComponentLayoutFactory.getLabelCaptionRequired("primary.phone.no");
		btnEditTypeContact = new Button();
		btnEditTypeContact.setIcon(FontAwesome.EDIT);
		btnEditTypeContact.setStyleName(Reindeer.BUTTON_LINK);
		btnEditTypeContact.setVisible(false);
		btnEditTypeContact.addClickListener(this);
		
		btnEditTypeAddress = new Button();
		btnEditTypeAddress.setIcon(FontAwesome.EDIT);
		btnEditTypeAddress.setStyleName(Reindeer.BUTTON_LINK);
		btnEditTypeAddress.setVisible(false);
		btnEditTypeAddress.addClickListener(this);
		
		btnEditValue = new Button();
		btnEditValue.setIcon(FontAwesome.EDIT);
		btnEditValue.setStyleName(Reindeer.BUTTON_LINK);
		btnEditValue.setVisible(false);
		btnEditValue.addClickListener(this);
		
		btnEditRemark =  new Button();
		btnEditRemark.setIcon(FontAwesome.EDIT);
		btnEditRemark.setStyleName(Reindeer.BUTTON_LINK);
		btnEditRemark.setVisible(false);
		btnEditRemark.addClickListener(this);
		
		HorizontalLayout primeryLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		primeryLayout.addComponent(lblPrimeryPhoneNo);
		primeryLayout.addComponent(cbxTypeContactInfo);
		primeryLayout.addComponent(btnEditTypeContact);
		primeryLayout.addComponent(cbxTypeAddress);
		primeryLayout.addComponent(btnEditTypeAddress);
		primeryLayout.addComponent(txtValue);
		primeryLayout.addComponent(btnEditValue);
		primeryLayout.addComponent(txtRemark);
		primeryLayout.addComponent(btnEditRemark);
		primeryLayout.addComponent(btnSavePrimery);
		
		primeryLayout.setComponentAlignment(btnEditTypeContact, Alignment.MIDDLE_LEFT);
		primeryLayout.setComponentAlignment(btnEditTypeAddress, Alignment.MIDDLE_LEFT);
		primeryLayout.setComponentAlignment(btnEditValue, Alignment.MIDDLE_LEFT);
		primeryLayout.setComponentAlignment(btnEditRemark, Alignment.MIDDLE_LEFT);
		
		addComponent(primeryLayout);	
	}
	
	/**
	 * 
	 * @param contactInfo
	 */
	public void assignValue(ContactInfo contactInfo) {
		reset();
		this.contactInfo = contactInfo;
		if (contactInfo != null) {
			cbxTypeContactInfo.setSelectedEntity(contactInfo.getTypeInfo());
			cbxTypeAddress.setSelectedEntity(contactInfo.getTypeAddress());
			txtValue.setValue(getDefaultString(contactInfo.getValue()));
			txtRemark.setValue(getDefaultString(contactInfo.getRemark()));
			disablePhoneForm(false);
		} else {
			disablePhoneForm(true);
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		this.contactInfo = null;
		this.individual = null;
		this.dealer = null;
		cbxTypeContactInfo.setSelectedEntity(ETypeContactInfo.MOBILE);
		cbxTypeAddress.setSelectedEntity(null);
		txtValue.setValue(StringUtils.EMPTY);
		txtRemark.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * after click save phone
	 * @param isEnable
	 */
	private void disablePhoneForm(boolean isEnable) {
		cbxTypeContactInfo.setEnabled(isEnable);
		txtValue.setEnabled(isEnable);
		txtRemark.setEnabled(isEnable);
		btnSavePrimery.setVisible(isEnable);
		btnEditTypeContact.setVisible(!isEnable);
		btnEditValue.setVisible(!isEnable);
		btnEditRemark.setVisible(!isEnable);
		if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
			cbxTypeAddress.setEnabled(isEnable);
			btnEditTypeAddress.setVisible(!isEnable);
		}
	}
	
	/**
	 * Get Mobile and Landline to combobox
	 * @return
	 */
	private List<ETypeContactInfo> getPhoneValues() {
		List<ETypeContactInfo> typeContactInfos = new ArrayList<ETypeContactInfo>();
		typeContactInfos.add(ETypeContactInfo.MOBILE);
		typeContactInfos.add(ETypeContactInfo.LANDLINE);
		return typeContactInfos;
	}
	
	/**
	 * Save New PrimaryPhone
	 */
	private void savePrimaryPhone() {
		if (this.contactInfo == null) {
			this.contactInfo = ContactInfo.createInstance();
			this.contactInfo.setPrimary(true);
			this.contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
			this.contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
			this.contactInfo.setValue(txtValue.getValue());
			this.contactInfo.setRemark(txtRemark.getValue());
		}
		
		IndividualContactInfo individualContactInfo = null;
		DealerContactInfo dealerContactInfo = null;
		if (individual != null) {
			individualContactInfo = IndividualContactInfo.createInstance();
			individualContactInfo.setIndividual(individual);
			individualContactInfo.setContactInfo(contactInfo);
		} else if (dealer != null) {
			dealerContactInfo = DealerContactInfo.createInstance();
			dealerContactInfo.setDealer(dealer);
			dealerContactInfo.setContactInfo(contactInfo);
		}
		
		try {
			ENTITY_SRV.saveOrUpdate(contactInfo);
			if (individualContactInfo != null) {
				ENTITY_SRV.saveOrUpdate(individualContactInfo);
			} else if (dealerContactInfo != null) {
				ENTITY_SRV.saveOrUpdate(dealerContactInfo);
			}
			ComponentLayoutFactory.displaySuccessfullyMsg();
			disablePhoneForm(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSavePrimery) {
			savePrimaryPhone();
		} else if (event.getButton() == btnEditTypeContact) {
			if (btnEditTypeContact.getIcon().equals(FontAwesome.EDIT)) {
				btnEditTypeContact.setIcon(FontAwesome.SAVE);
				cbxTypeContactInfo.setEnabled(true);
			} else {
				cbxTypeContactInfo.setEnabled(false);
				btnEditTypeContact.setIcon(FontAwesome.EDIT);
				contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
				if (ETypeContactInfo.MOBILE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					contactInfo.setTypeAddress(null);
				} else {
					contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
				}
				ENTITY_SRV.saveOrUpdate(contactInfo);
				displaySuccessMsg("msg.type.contact.save.successfully");
			}
		} else if (event.getButton() == btnEditTypeAddress) {
			if (btnEditTypeAddress.getIcon().equals(FontAwesome.EDIT)) {
				btnEditTypeAddress.setIcon(FontAwesome.SAVE);
				cbxTypeAddress.setEnabled(true);
			} else {
				btnEditTypeAddress.setIcon(FontAwesome.EDIT);
				contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				displaySuccessMsg("msg.type.address.save.successfully");
			}
		} else if (event.getButton() == btnEditValue) {
			if (btnEditValue.getIcon().equals(FontAwesome.EDIT)) {
				btnEditValue.setIcon(FontAwesome.SAVE);
				txtValue.setEnabled(true);
			} else {
				btnEditValue.setIcon(FontAwesome.EDIT);
				contactInfo.setValue(txtValue.getValue()); 
				ENTITY_SRV.saveOrUpdate(contactInfo);
				displaySuccessMsg("msg.value.save.successfully");
			}
		} else if (event.getButton() == btnEditRemark) {
			if (btnEditRemark.getIcon().equals(FontAwesome.EDIT)) {
				btnEditRemark.setIcon(FontAwesome.SAVE);
				txtRemark.setEnabled(true);
			} else {
				btnEditRemark.setIcon(FontAwesome.EDIT);
				contactInfo.setRemark(txtRemark.getValue());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				displaySuccessMsg("msg.remark.save.successfully");
			}
		}
	}
	
	/**
	 * 
	 * @param desc
	 */
	private void displaySuccessMsg(String desc) {
		ComponentLayoutFactory.displaySuccessMsg(desc);
		disablePhoneForm(false);
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}
	
	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
}
