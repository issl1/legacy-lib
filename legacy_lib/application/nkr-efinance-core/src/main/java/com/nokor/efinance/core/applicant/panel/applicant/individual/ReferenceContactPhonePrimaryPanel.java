package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
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
public class ReferenceContactPhonePrimaryPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3442697884823515392L;
	private ContactInfo contactInfo;
	//private Individual individual;
	
	private IndividualReferenceInfo individualReferenceInfo;
	
	private ERefDataComboBox<ETypeContactInfo> cbxTypeContactInfo;
	private ERefDataComboBox<ETypeAddress> cbxTypeAddress;
	private TextField txtValue;
	private TextField txtRemark;
	private Button btnSavePrimery;
	
	private Button btnEditTypeContact;
	private Button btnEditTypeAddress;
	private Button btnEditValue;
	private Button btnEditRemark;
	
	public ReferenceContactPhonePrimaryPanel() {
		
		cbxTypeAddress = new ERefDataComboBox<>(ETypeAddress.values());
		cbxTypeAddress.setVisible(false);
		cbxTypeContactInfo = new ERefDataComboBox<>(getPhoneValues());
		cbxTypeContactInfo.setWidth("80px");
		cbxTypeContactInfo.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 1485410432507231312L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					cbxTypeAddress.setVisible(true);
				} else {
					cbxTypeAddress.setVisible(false);
					btnEditTypeContact.setVisible(false);
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
		
		HorizontalLayout primeryLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
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
	 * Assing Value
	 */
	public void assignValue(IndividualReferenceInfo individualReferenceInfo) {
		reset();
		this.individualReferenceInfo = individualReferenceInfo;
		if (individualReferenceInfo.getId() != null) {
			contactInfo = getContactInfo(individualReferenceInfo);
		}
		if (contactInfo != null) {
			cbxTypeContactInfo.setSelectedEntity(contactInfo.getTypeInfo());
			cbxTypeAddress.setSelectedEntity(contactInfo.getTypeAddress());
			txtValue.setValue(getDefaultString(contactInfo.getValue()));
			txtRemark.setValue(getDefaultString(contactInfo.getRemark()));
			disablePhoneForm(false);
		} else {
			contactInfo = ContactInfo.createInstance();
			contactInfo.setPrimary(true);
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		cbxTypeContactInfo.setSelectedEntity(null);
		cbxTypeAddress.setSelectedEntity(null);
		txtValue.setValue("");
		txtRemark.setValue("");
		contactInfo = null;
		
		cbxTypeContactInfo.setEnabled(true);
		cbxTypeAddress.setEnabled(true);
		txtValue.setEnabled(true);
		txtRemark.setEnabled(true);
		btnSavePrimery.setVisible(true);
		
		btnEditTypeContact.setVisible(false);
		btnEditValue.setVisible(false);
		btnEditRemark.setVisible(false);
		btnEditTypeAddress.setVisible(false);
	}
	
	/**
	 * 
	 * @param individual
	 * @return
	 */
	private ContactInfo getContactInfo(IndividualReferenceInfo individualReferenceInfo) {
		BaseRestrictions<IndividualReferenceContactInfo> restrictions = new BaseRestrictions<>(IndividualReferenceContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individualReferenceInfo", individualReferenceInfo));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("con.typeInfo", ETypeContactInfo.LANDLINE), Restrictions.eq("con.typeInfo", ETypeContactInfo.MOBILE)));
		restrictions.addCriterion(Restrictions.eq("con.primary", true));
		restrictions.addOrder(Order.desc("createDate"));
		List<IndividualReferenceContactInfo> individualReferenceContactInfos = ENTITY_SRV.list(restrictions);
		if (!individualReferenceContactInfos.isEmpty()) {
			return individualReferenceContactInfos.get(0).getContactInfo();
		}
		return null;
	}
	
	/**
	 * after click save phone
	 * @param isEnabel
	 */
	private void disablePhoneForm(boolean isEnabel) {
		
		cbxTypeContactInfo.setEnabled(isEnabel);
		txtValue.setEnabled(isEnabel);
		txtRemark.setEnabled(isEnabel);
		btnEditTypeContact.setVisible(true);
		btnEditValue.setVisible(true);
		btnEditRemark.setVisible(true);
		btnSavePrimery.setVisible(false);
		
		if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
			cbxTypeAddress.setEnabled(isEnabel);
			btnEditTypeAddress.setVisible(true);
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
		
		if (individualReferenceInfo.getId() != null) {
			contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
			contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
			contactInfo.setValue(txtValue.getValue());
			contactInfo.setRemark(txtRemark.getValue());
			
			IndividualReferenceContactInfo individualReferenceContactInfo = IndividualReferenceContactInfo.createInstance();
			individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
			individualReferenceContactInfo.setContactInfo(contactInfo);
			
			try {
				ENTITY_SRV.saveOrUpdate(contactInfo);
				ENTITY_SRV.saveOrUpdate(individualReferenceContactInfo);
				ComponentLayoutFactory.displaySuccessfullyMsg();
				disablePhoneForm(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ComponentLayoutFactory.displayErrorMsg("please.save.reference.before.save.contact.info");
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSavePrimery) {
			savePrimaryPhone();
		} else if (event.getButton() == btnEditTypeContact) {
			if (btnEditTypeContact.getIcon().equals(FontAwesome.EDIT)) {
				btnEditTypeContact.setIcon(FontAwesome.SAVE);
				cbxTypeContactInfo.setEnabled(true);
			} else {
				btnEditTypeContact.setIcon(FontAwesome.EDIT);
				contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				ComponentLayoutFactory.displaySuccessMsg("msg.type.contact.save.successfully");
				if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					cbxTypeAddress.setEnabled(false);
					btnEditTypeAddress.setVisible(true);
				}
			}
		} else if (event.getButton() == btnEditTypeAddress) {
			if (btnEditTypeAddress.getIcon().equals(FontAwesome.EDIT)) {
				btnEditTypeAddress.setIcon(FontAwesome.SAVE);
				cbxTypeAddress.setEnabled(true);
			} else {
				btnEditTypeAddress.setIcon(FontAwesome.EDIT);
				contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				ComponentLayoutFactory.displaySuccessMsg("msg.type.address.save.successfully");
			}
		} else if (event.getButton() == btnEditValue) {
			if (btnEditValue.getIcon().equals(FontAwesome.EDIT)) {
				btnEditValue.setIcon(FontAwesome.SAVE);
				txtValue.setEnabled(true);
			} else {
				btnEditValue.setIcon(FontAwesome.EDIT);
				contactInfo.setValue(txtValue.getValue()); 
				ENTITY_SRV.saveOrUpdate(contactInfo);
				ComponentLayoutFactory.displaySuccessMsg("msg.value.save.successfully");
			}
		} else if (event.getButton() == btnEditRemark) {
			if (btnEditRemark.getIcon().equals(FontAwesome.EDIT)) {
				btnEditRemark.setIcon(FontAwesome.SAVE);
				txtRemark.setEnabled(true);
			} else {
				btnEditRemark.setIcon(FontAwesome.EDIT);
				contactInfo.setRemark(txtRemark.getValue());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				ComponentLayoutFactory.displaySuccessMsg("msg.remark.save.successfully");
			}
		}
	}

}
