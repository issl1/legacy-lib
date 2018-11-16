package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * addressPanel
 * @author buntha.chea
 * */
public class AddressPanel extends AbstractTabPanel implements FMEntityField, ValueChangeListener {
	
	private static final long serialVersionUID = 710425425958548975L;
	
	private EntityComboBox<Province> cbxProvince;
	private EntityComboBox<District> cbxDistrict;
	private EntityComboBox<Commune> cbxSubDistrict;
	
	private TextField txtBuilding;
	private TextField txtMoo;
	private TextField txtSoi;
	private TextField txtSubSoi;
	private TextField txtStreet;
	private TextField txtPostalCode;
	//private AddressComboBox cbxAddress;
	private Address address;
	private Individual individual;
	
	private ERefDataComboBox<ETypeAddress> cbxCopyFromAddressType;
	
//	/**
//	 * @param addresses the addresses to set
//	 */
//	public void setAddresses(List<Address> addresses) {
//		cbxAddress.setEntities(addresses);
//	}

	/** */
	public AddressPanel() {
		super();
		setSizeFull();
	}	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField txtField = ComponentFactory.getTextField(false, 60, 190);
		return txtField;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.renderer();
		cbxProvince.setWidth(190, Unit.PIXELS);
		
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setWidth(190, Unit.PIXELS);
		cbxSubDistrict = new EntityComboBox<>(Commune.class, "desc");
		cbxSubDistrict.setWidth(190, Unit.PIXELS);
		
	    txtPostalCode = getTextField();
		txtBuilding = getTextField();
		txtMoo = getTextField();
		txtSoi = getTextField();
		txtSubSoi = getTextField();
		txtStreet = getTextField();
		
		cbxCopyFromAddressType = new ERefDataComboBox<>(ETypeAddress.valuesOfApplicants());
		cbxCopyFromAddressType.setWidth("110px");
		cbxCopyFromAddressType.addValueChangeListener(this);
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -572281953646438700L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, cbxProvince.getSelectedEntity().getId()));
					cbxDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
				}
				cbxSubDistrict.clear();
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -470992549634354195L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, cbxDistrict.getSelectedEntity().getId()));
					cbxSubDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxSubDistrict.renderer();
					
					
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		cbxSubDistrict.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = -4700289434066242228L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (cbxSubDistrict.getSelectedEntity() != null) {
					    Commune commune = ENTITY_SRV.getById(Commune.class, cbxSubDistrict.getSelectedEntity().getId());
						txtPostalCode.setValue(commune.getPostalCode());
					} else {
						txtPostalCode.setValue("");
					}
				}
		});
	
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(createAddressPanel());
		return verticalLayout;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	public Address getAddress(Address address) {
		address.setHouseNo(txtBuilding.getValue());
		address.setLine1(txtMoo.getValue());
		address.setLine2(txtSoi.getValue());
		address.setLine3(txtSubSoi.getValue());
		address.setStreet(txtStreet.getValue());
		address.setProvince(cbxProvince.getSelectedEntity());
		address.setDistrict(cbxDistrict.getSelectedEntity());
		address.setCommune(cbxSubDistrict.getSelectedEntity());
		address.setPostalCode(txtPostalCode.getValue());
		return address;
	}
	
	/**
	 * 
	 * @param address
	 */
	public void assignValue(Address address){
		this.address = address;
		this.reset();
		if (address != null) {
			txtBuilding.setValue(getDefaultString(address.getHouseNo()));
			txtMoo.setValue(getDefaultString(address.getLine1()));
			txtSoi.setValue(getDefaultString(address.getLine2()));
			txtSubSoi.setValue(getDefaultString(address.getLine3()));
			txtStreet.setValue(getDefaultString(address.getStreet()));
			Commune subDistrict = address.getCommune();
			District district = subDistrict == null ? null : subDistrict.getDistrict();
			cbxProvince.setSelectedEntity(district == null ? null : district.getProvince());
			cbxDistrict.setSelectedEntity(district);
			cbxSubDistrict.setSelectedEntity(subDistrict);
			txtPostalCode.setValue(getDefaultString(address.getPostalCode()));
		}
	}
	
	/**
	 * 
	 * @param address
	 */
	private void copyValueToControls() {
		if (individual != null) {
			reset();
			List<IndividualAddress> individualAddresses = getIndividualAddress(individual);
			for (IndividualAddress individualAddress : individualAddresses) {
				Address address = individualAddress.getAddress();
				if (cbxCopyFromAddressType.getSelectedEntity().equals(address.getType())) {
					txtBuilding.setValue(getDefaultString(address.getHouseNo()));
					txtMoo.setValue(getDefaultString(address.getLine1()));
					txtSoi.setValue(getDefaultString(address.getLine2()));
					txtSubSoi.setValue(getDefaultString(address.getLine3()));
					txtStreet.setValue(getDefaultString(address.getStreet()));
					Commune subDistrict = address.getCommune();
					District district = subDistrict == null ? null : subDistrict.getDistrict();
					cbxProvince.setSelectedEntity(district == null ? null : district.getProvince());
					cbxDistrict.setSelectedEntity(district);
					cbxSubDistrict.setSelectedEntity(subDistrict);
					txtPostalCode.setValue(getDefaultString(address.getPostalCode()));
					return;
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public CustomLayout createAddressPanel(){
		String template = "addressInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout addressCustomLayout = null;
		try {
			addressCustomLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		addressCustomLayout.addComponent(new Label(I18N.message("number.building")), "lblBuilding");
		addressCustomLayout.addComponent(txtBuilding, "txtBuilding");
		addressCustomLayout.addComponent(new Label(I18N.message("copy.from")), "lblCopyFrom");
		addressCustomLayout.addComponent(cbxCopyFromAddressType, "cbxAddress");
		addressCustomLayout.addComponent(new Label(I18N.message("moo")), "lblMoo");
		addressCustomLayout.addComponent(txtMoo, "txtMoo");
		addressCustomLayout.addComponent(new Label(I18N.message("soi")), "lblSoi");
		addressCustomLayout.addComponent(txtSoi, "txtSoi");
		addressCustomLayout.addComponent(new Label(I18N.message("sub.soi")), "lblSubSoi");
		addressCustomLayout.addComponent(txtSubSoi, "txtSubSoi");
		addressCustomLayout.addComponent(new Label(I18N.message("street")), "lblStreet");
		addressCustomLayout.addComponent(txtStreet, "txtStreet");
		addressCustomLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
		addressCustomLayout.addComponent(cbxProvince, "cbxProvince");
		addressCustomLayout.addComponent(new Label(I18N.message("district")), "lblDistrict");
		addressCustomLayout.addComponent(cbxDistrict, "cbxDistrict");
		addressCustomLayout.addComponent(new Label(I18N.message("subdistrict")), "lblSubDistrict");
		addressCustomLayout.addComponent(cbxSubDistrict, "cbxSubDistrict");
		addressCustomLayout.addComponent(new Label(I18N.message("postal.code")), "lblPostalCode");
		addressCustomLayout.addComponent(txtPostalCode, "txtPostalCode");
		
		return addressCustomLayout;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		checkMandatoryField(txtBuilding, "number.building");
		checkMandatoryField(txtStreet, "street");
		checkMandatorySelectField(cbxProvince, "province");
		checkMandatorySelectField(cbxDistrict, "district");
		checkMandatorySelectField(cbxSubDistrict, "subdistrict");
		checkMandatoryField(txtPostalCode, "postal.code");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		super.removeErrorsPanel();
		checkMandatoryField(txtBuilding, "number.building");
		checkMandatoryField(txtStreet, "street");
		checkMandatorySelectField(cbxProvince, "province");
		checkMandatorySelectField(cbxDistrict, "district");
		checkMandatorySelectField(cbxSubDistrict, "subdistrict");
		checkMandatoryField(txtPostalCode, "postal.code");
		return errors;
	}
	
	/**
	 * 
	 * @param isEnabled
	 */
	public void setEnabledControls(boolean isEnabled) {
		cbxCopyFromAddressType.setEnabled(isEnabled);
		cbxProvince.setEnabled(isEnabled);
		cbxDistrict.setEnabled(isEnabled);
		cbxSubDistrict.setEnabled(isEnabled);
		txtBuilding.setEnabled(isEnabled);
		txtMoo.setEnabled(isEnabled);
		txtSoi.setEnabled(isEnabled);
		txtSubSoi.setEnabled(isEnabled);
		txtStreet.setEnabled(isEnabled);
		txtPostalCode.setEnabled(isEnabled);
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxSubDistrict.setSelectedEntity(null);
		txtBuilding.setValue(StringUtils.EMPTY);
		txtMoo.setValue(StringUtils.EMPTY);
		txtSoi.setValue(StringUtils.EMPTY);
		txtSubSoi.setValue(StringUtils.EMPTY);
		txtStreet.setValue(StringUtils.EMPTY);
		txtPostalCode.setValue(StringUtils.EMPTY);
	}
		
	/**
	 * @return the cbxProvince
	 */
	public EntityComboBox<Province> getCbxProvince() {
		return cbxProvince;
	}

	/**
	 * @param cbxProvince the cbxProvince to set
	 */
	public void setCbxProvince(EntityComboBox<Province> cbxProvince) {
		this.cbxProvince = cbxProvince;
	}

	/**
	 * @return the cbxDistrict
	 */
	public EntityComboBox<District> getCbxDistrict() {
		return cbxDistrict;
	}

	/**
	 * @param cbxDistrict the cbxDistrict to set
	 */
	public void setCbxDistrict(EntityComboBox<District> cbxDistrict) {
		this.cbxDistrict = cbxDistrict;
	}

	/**
	 * @return the cbxSubDistrict
	 */
	public EntityComboBox<Commune> getCbxSubDistrict() {
		return cbxSubDistrict;
	}

	/**
	 * @param cbxSubDistrict the cbxSubDistrict to set
	 */
	public void setCbxSubDistrict(EntityComboBox<Commune> cbxSubDistrict) {
		this.cbxSubDistrict = cbxSubDistrict;
	}

	/**
	 * @return the txtBuilding
	 */
	public TextField getTxtBuilding() {
		return txtBuilding;
	}
	
	/**
	 * @return the txtStreet
	 */
	public TextField getTxtStreet() {
		return txtStreet;
	}
	
	/**
	 * @return the txtPostalCode
	 */
	public TextField getTxtPostalCode() {
		return txtPostalCode;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}
	
	/**
	 * 
	 * @param individual
	 * @return
	 */
	private List<IndividualAddress> getIndividualAddress(Individual individual) {
		BaseRestrictions<IndividualAddress> restrictions = new BaseRestrictions<>(IndividualAddress.class);
		restrictions.addAssociation("address", "add", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individual", individual));
		restrictions.addCriterion(Restrictions.in("add.type", ETypeAddress.valuesOfApplicants()));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbxCopyFromAddressType)) {
			if (cbxCopyFromAddressType.getSelectedEntity() != null) {
				copyValueToControls();
			} else {
				if (this.address != null && this.address.getId() != null) {
					this.assignValue(this.address);
				} else {
					this.reset();
				}
			}
		}
	}
	
}
