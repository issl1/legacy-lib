package com.nokor.efinance.gui.ui.panel.contract.user.phone;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.contact.panel.UserContactPhonePrimaryPanel;
import com.nokor.efinance.core.contact.panel.UserOtherContactPanel;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class UserContactPhonePanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1351287878005010288L;
	
	private Individual individual;
	
	private UserContactPhonePrimaryPanel userContactPhonePrimaryPanel;
	private UserOtherContactPanel userOtherContactPanel;
	
	private Button btnAddOtherPhone;
	private VerticalLayout otherPhoneLayout;
	
	public UserContactPhonePanel() {
		setSpacing(true);
		init();	
		VerticalLayout phoneLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(true, true, false, true), false);
		phoneLayout.addComponent(userContactPhonePrimaryPanel);
		phoneLayout.addComponent(otherPhoneLayout);
		phoneLayout.addComponent(btnAddOtherPhone);
		
		FieldSet phoneFieldSet = new FieldSet();
		phoneFieldSet.setLegend(I18N.message("phone"));
		phoneFieldSet.setContent(phoneLayout);
		
		Panel phonePanel = new Panel(phoneFieldSet);
		phonePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		FieldSet otherFieldSet = new FieldSet();
		otherFieldSet.setLegend(I18N.message("other.contact"));
		otherFieldSet.setContent(userOtherContactPanel);
		
		Panel otherPanel = new Panel(otherFieldSet);
		otherPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(phonePanel);
		addComponent(otherPanel);
	}
	
	/**
	 * init
	 */
	private void init() {
		userContactPhonePrimaryPanel = new UserContactPhonePrimaryPanel();
		userOtherContactPanel = new UserOtherContactPanel();
		otherPhoneLayout = ComponentLayoutFactory.getVerticalLayout(false, false);

		btnAddOtherPhone = ComponentLayoutFactory.getButtonAdd();
		btnAddOtherPhone.addClickListener(this);
	}
	
	/**
	 * Assign Value
	 */
	public void assignValue(Individual individual) {
		this.individual = individual;
		otherPhoneLayout.removeAllComponents();
		
		userContactPhonePrimaryPanel.assignValue(getContactInfo(individual));
		userContactPhonePrimaryPanel.setIndividual(individual);
		
		userOtherContactPanel.setIndividual(individual);
		userOtherContactPanel.assignValue();
		
		btnAddOtherPhone.setEnabled(true);
		List<IndividualContactInfo> individualContactInfos = getIndividualContactInfos(individual);
		for (IndividualContactInfo individualContactInfo : individualContactInfos) {
			ContactInfo contactInfo = individualContactInfo.getContactInfo();
			otherPhoneLayout.addComponent(createOtherPhoneFormLayout(contactInfo));
		}
	}
	
	/**
	 * 
	 * @param individual
	 * @return
	 */
	private ContactInfo getContactInfo(Individual individual) {
		BaseRestrictions<IndividualContactInfo> restrictions = new BaseRestrictions<>(IndividualContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individual", individual));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("con.typeInfo", ETypeContactInfo.LANDLINE), Restrictions.eq("con.typeInfo", ETypeContactInfo.MOBILE)));
		restrictions.addCriterion(Restrictions.eq("con.primary", true));
		restrictions.addOrder(Order.desc("createDate"));
		List<IndividualContactInfo> individualContactInfos = ENTITY_SRV.list(restrictions);
		if (!individualContactInfos.isEmpty()) {
			return individualContactInfos.get(0).getContactInfo();
		}
		return null;
	}
	
	/**
	 * 
	 * @param individual
	 * @return
	 */
	private List<IndividualContactInfo> getIndividualContactInfos(Individual individual) {
		BaseRestrictions<IndividualContactInfo> restrictions = new BaseRestrictions<>(IndividualContactInfo.class);
		restrictions.addAssociation("contactInfo", "con", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individual", individual));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("con.typeInfo", ETypeContactInfo.LANDLINE), Restrictions.eq("con.typeInfo", ETypeContactInfo.MOBILE)));
		restrictions.addCriterion(Restrictions.eq("con.primary", false));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Create Dynamic Other Phone Form
	 * @return
	 */
	private HorizontalLayout createOtherPhoneFormLayout(ContactInfo contactInfo) {
		
		ERefDataComboBox<ETypeAddress> cbxTypeAddress = new ERefDataComboBox<>(ETypeAddress.values());
		cbxTypeAddress.setWidth(150, Unit.PIXELS);
		cbxTypeAddress.setVisible(false);
		
		Button btnEditTypeAddress = new Button(FontAwesome.EDIT);
		btnEditTypeAddress.setStyleName(Reindeer.BUTTON_LINK);
		btnEditTypeAddress.setVisible(false);
		btnEditTypeAddress.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -8308922331304228978L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (btnEditTypeAddress.getIcon().equals(FontAwesome.EDIT)) {
					btnEditTypeAddress.setIcon(FontAwesome.SAVE);
					cbxTypeAddress.setEnabled(true);
				} else {
					btnEditTypeAddress.setIcon(FontAwesome.EDIT);
					cbxTypeAddress.setEnabled(false);
					contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(contactInfo);
					ComponentLayoutFactory.displaySuccessMsg("msg.type.address.save.successfully");
				}
			}
		});
		
		ERefDataComboBox<ETypeContactInfo> cbxTypeContactInfo = new ERefDataComboBox<>(getPhoneValues());
		cbxTypeContactInfo.setWidth("80px");
		cbxTypeContactInfo.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 1485410432507231312L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					cbxTypeAddress.setVisible(true);
				} else {
					cbxTypeAddress.setVisible(false);
					btnEditTypeAddress.setVisible(false);
				}
			}
		});
		
		TextField txtValue = ComponentFactory.getTextField();
		TextField txtRemark = ComponentFactory.getTextField();
		
		Button btnEditTypeContact = new Button(FontAwesome.EDIT);
		btnEditTypeContact.setVisible(false);
		btnEditTypeContact.setStyleName(Reindeer.BUTTON_LINK);
		btnEditTypeContact.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 5505750802585275309L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (btnEditTypeContact.getIcon().equals(FontAwesome.EDIT)) {
					btnEditTypeContact.setIcon(FontAwesome.SAVE);
					cbxTypeContactInfo.setEnabled(true);
				} else {
					btnEditTypeContact.setIcon(FontAwesome.EDIT);
					cbxTypeContactInfo.setEnabled(false);
					contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
					if (ETypeContactInfo.MOBILE.equals(cbxTypeContactInfo.getSelectedEntity())) {
						contactInfo.setTypeAddress(null);
					} else {
						contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
					}
					ENTITY_SRV.saveOrUpdate(contactInfo);
					ComponentLayoutFactory.displaySuccessMsg("msg.type.contact.save.successfully");
					if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
						btnEditTypeAddress.setVisible(true);
					}
				}
			}
		});
		
		
		
		Button btnEditValue = new Button(FontAwesome.EDIT);
		btnEditValue.setStyleName(Reindeer.BUTTON_LINK);
		btnEditValue.setVisible(false);
		btnEditValue.addClickListener(new ClickListener() {

		private static final long serialVersionUID = -6197819506282699526L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (btnEditValue.getIcon().equals(FontAwesome.EDIT)) {
					btnEditValue.setIcon(FontAwesome.SAVE);
					txtValue.setEnabled(true);
				} else {
					btnEditValue.setIcon(FontAwesome.EDIT);
					txtValue.setEnabled(false);
					contactInfo.setValue(txtValue.getValue());
					ENTITY_SRV.saveOrUpdate(contactInfo);
					ComponentLayoutFactory.displaySuccessMsg("msg.value.save.successfully");
				}
			}
		});
		
		Button btnEditRemark = new Button(FontAwesome.EDIT);
		btnEditRemark.setStyleName(Reindeer.BUTTON_LINK);
		btnEditRemark.setVisible(false);
		btnEditRemark.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5121720860303828806L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (btnEditRemark.getIcon().equals(FontAwesome.EDIT)) {
					txtRemark.setEnabled(true);
					btnEditRemark.setIcon(FontAwesome.SAVE);
				} else {
					txtRemark.setEnabled(false);
					btnEditRemark.setIcon(FontAwesome.EDIT);
					contactInfo.setRemark(txtRemark.getValue());
					ENTITY_SRV.saveOrUpdate(contactInfo);
					ComponentLayoutFactory.displaySuccessMsg("msg.remark.save.successfully");
				}
			}
		});
		
		Button btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4455767673238937032L;

			@Override
			public void buttonClick(ClickEvent event) {
				btnSave.setVisible(false);
				cbxTypeContactInfo.setEnabled(false);
				btnEditTypeContact.setVisible(true);
				
				if (ETypeContactInfo.LANDLINE.equals(cbxTypeContactInfo.getSelectedEntity())) {
					cbxTypeAddress.setEnabled(false);
					btnEditTypeAddress.setVisible(true);
				}
				txtValue.setEnabled(false);
				btnEditValue.setVisible(true);
				txtRemark.setEnabled(false);
				btnEditRemark.setVisible(true);
				btnAddOtherPhone.setEnabled(true);
				
				contactInfo.setTypeInfo(cbxTypeContactInfo.getSelectedEntity());
				contactInfo.setTypeAddress(cbxTypeAddress.getSelectedEntity());
				contactInfo.setValue(txtValue.getValue());
				contactInfo.setRemark(txtRemark.getValue());
				ENTITY_SRV.saveOrUpdate(contactInfo);
				
				IndividualContactInfo individualContactInfo = IndividualContactInfo.createInstance();
				individualContactInfo.setIndividual(individual);
				individualContactInfo.setContactInfo(contactInfo);
				ENTITY_SRV.saveOrUpdate(individualContactInfo);
				
				ComponentLayoutFactory.displaySuccessfullyMsg();
				assignValue(individual);
			}
		});
		
		if (contactInfo.getId() != null) {
			btnSave.setVisible(false);
			cbxTypeContactInfo.setEnabled(false);
			btnEditTypeContact.setVisible(true);
			
			if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
				cbxTypeAddress.setEnabled(false);
				btnEditTypeAddress.setVisible(true);
			}
			txtValue.setEnabled(false);
			btnEditValue.setVisible(true);
			txtRemark.setEnabled(false);
			btnEditRemark.setVisible(true);
			btnAddOtherPhone.setEnabled(true);
			
			cbxTypeContactInfo.setSelectedEntity(contactInfo.getTypeInfo());
			cbxTypeAddress.setSelectedEntity(contactInfo.getTypeAddress());
			txtValue.setValue(getDefaultString(contactInfo.getValue()));
			txtRemark.setValue(getDefaultString(contactInfo.getRemark()));
		}
		
		Label lblOtherPhoneNo = ComponentLayoutFactory.getLabelCaption("other.phone.no");
		HorizontalLayout otherLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(true, false, false, false), true);
		otherLayout.addComponent(lblOtherPhoneNo);
		otherLayout.addComponent(ComponentFactory.getSpaceLayout(12, Unit.PIXELS));
		otherLayout.addComponent(cbxTypeContactInfo);
		otherLayout.addComponent(btnEditTypeContact);
		otherLayout.addComponent(cbxTypeAddress);
		otherLayout.addComponent(btnEditTypeAddress);
		otherLayout.addComponent(txtValue);
		otherLayout.addComponent(btnEditValue);
		otherLayout.addComponent(txtRemark);
		otherLayout.addComponent(btnEditRemark);
		otherLayout.addComponent(btnSave);
		
		otherLayout.setComponentAlignment(btnEditTypeContact, Alignment.MIDDLE_LEFT);
		otherLayout.setComponentAlignment(btnEditTypeAddress, Alignment.MIDDLE_LEFT);
		otherLayout.setComponentAlignment(btnEditValue, Alignment.MIDDLE_LEFT);
		otherLayout.setComponentAlignment(btnEditRemark, Alignment.MIDDLE_LEFT);
		
		return otherLayout;
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAddOtherPhone) {
			ContactInfo contactInfo = ContactInfo.createInstance();
			contactInfo.setPrimary(false);
			otherPhoneLayout.addComponent(createOtherPhoneFormLayout(contactInfo));
			btnAddOtherPhone.setEnabled(false);
		}
	}
}