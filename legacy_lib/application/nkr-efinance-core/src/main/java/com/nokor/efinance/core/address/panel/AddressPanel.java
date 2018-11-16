package com.nokor.efinance.core.address.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Address Panel
 * @author youhort.ly
 *
 */
public class AddressPanel extends AbstractControlPanel {

	private static final long serialVersionUID = -765492115214802081L;
	
	private TextField txtBuilding;
	private TextField txtStreet;
    private TextField txtMoo;
    private TextField txtSoi;
   // private TextField txtLongValue;
   // private TextField txtLateValue;
    private ERefDataComboBox<ECountry> cbxCountry;
    // private EntityRefComboBox<Village> cbxVillage;

    private TextField txtTimeAtAddressInYear;
    private TextField txtTimeAtAddressInMonth;
    private ERefDataComboBox<EResidenceStatus> cbxpropertyAddress;
    private TextField txtPostCode;
    private ERefDataComboBox<ETypeAddress> cbxAddressType;
    
    private boolean required;
    
    private Address address;
    
    private EntityComboBox<Province> cbxProvince;
    private EntityComboBox<District> cbxDistrict;
    private EntityComboBox<Commune> cbxCommune;
    
    public AddressPanel(ETypeAddress addressType) {
    	this(false, addressType, "address");
    }
    
    public AddressPanel(boolean required, ETypeAddress addressType) {
    	this(required, addressType, "address");
    }
    
	public AddressPanel(boolean required, ETypeAddress addressType, String template) {
		this.required = required;
		setSizeFull();
		txtBuilding = ComponentFactory.getTextField(false, 20, 150);      
		txtStreet = ComponentFactory.getTextField(false, 100, 200);
		txtMoo = ComponentFactory.getTextField(false, 100, 150);
		txtSoi = ComponentFactory.getTextField(false, 100, 150);
		txtPostCode = ComponentFactory.getTextField(false, 100, 150);
	//txtLongValue = ComponentFactory.getTextField(false, 100, 150);
	//	txtLateValue = ComponentFactory.getTextField(false, 100, 150);
		
		txtTimeAtAddressInYear = ComponentFactory.getTextField(false, 50, 50);
		txtTimeAtAddressInMonth = ComponentFactory.getTextField(false, 50, 50);
		cbxpropertyAddress = new ERefDataComboBox<EResidenceStatus>(EResidenceStatus.values());
		if (addressType == ETypeAddress.WORK) {
			cbxpropertyAddress.setVisible(false);
		}
		
		cbxCountry = new ERefDataComboBox<ECountry>(ECountry.values());
		cbxCountry.setImmediate(true);
		cbxCountry.setWidth(150, Unit.PIXELS);
		
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth(150, Unit.PIXELS);
		cbxProvince.renderer();
		
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setImmediate(true);
		cbxDistrict.setWidth(150, Unit.PIXELS);
		
	    cbxCommune = new EntityComboBox<>(Commune.class, "desc");
	    cbxCommune.setImmediate(true);
	    cbxCommune.setWidth(150, Unit.PIXELS);
	        
	    cbxAddressType = new ERefDataComboBox<>(ETypeAddress.class);
	  
	    // cbxVillage = new EntityRefComboBox<Village>();
	    // cbxVillage.setRestrictions(new BaseRestrictions<Village>(Village.class));
	    // cbxVillage.setImmediate(true);
	    // cbxVillage.setWidth(150, Unit.PIXELS);
	    
		cbxCountry.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3023654283916642525L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCountry.getSelectedEntity() != null) {
					BaseRestrictions<Province> restrictions = new BaseRestrictions<>(Province.class);
					restrictions.addCriterion(Restrictions.eq("country", cbxCountry.getSelectedEntity()));
					cbxProvince.setEntities(ENTITY_SRV.list(restrictions));
				} else {
					cbxProvince.clear();
				}
				cbxDistrict.clear();
				cbxCommune.clear();
			}
		});
					    
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -78822726610125810L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq("province", cbxProvince.getSelectedEntity()));
					cbxDistrict.setEntities(ENTITY_SRV.list(restrictions));
				} else {
					cbxDistrict.clear();
				}
				cbxCommune.clear();
				// cbxVillage.clear();
			}
		});
			    
	    cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1761539763125185708L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq("district", cbxDistrict.getSelectedEntity()));
					cbxCommune.setEntities(ENTITY_SRV.list(restrictions));
				} else {
					cbxCommune.clear();
				}
				// cbxVillage.clear();
			}
		});
	    
	    cbxCommune.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4700289434066242228L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCommune.getSelectedEntity() != null) {
					Commune commune = ENTITY_SRV.getById(Commune.class, cbxCommune.getSelectedEntity().getId());
					txtPostCode.setValue(commune.getPostalCode());
				} else {
					txtPostCode.setValue("");
				}
			}
		});
	    
	    InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
			customLayout.setSizeFull();
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("address.type")), "lblAddressType");
        customLayout.addComponent(cbxAddressType, "cbxAddressType");
        customLayout.addComponent(new Label(I18N.message("number.building")), "lblBuilding");
        customLayout.addComponent(txtBuilding, "txtBuilding");
        customLayout.addComponent(new Label(I18N.message("moo")), "lblMoo");
        customLayout.addComponent(txtMoo, "txtMoo");
        customLayout.addComponent(new Label(I18N.message("soi")), "lblSoi");
        customLayout.addComponent(txtSoi, "txtSoi");
        customLayout.addComponent(new Label(I18N.message("street")), "lblStreet");
        customLayout.addComponent(txtStreet, "txtStreet");
       
        
        customLayout.addComponent(new Label(I18N.message("time.at.address")), "lblTimeAtAddress");
        customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
        customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
        customLayout.addComponent(txtTimeAtAddressInYear, "txtTimeAtAddressInYear");
        customLayout.addComponent(txtTimeAtAddressInMonth, "txtTimeAtAddressInMonth");
        if (addressType == ETypeAddress.WORK) {
        	customLayout.addComponent(new Label(), "lblpropertyAddress");
        } else {
        	customLayout.addComponent(new Label(I18N.message("housing")), "lblpropertyAddress");
        }
        customLayout.addComponent(cbxpropertyAddress, "cbxpropertyAddress");     
        customLayout.addComponent(new Label(I18N.message("country")), "lblCountry");
        customLayout.addComponent(cbxCountry, "cbxCountry");
        customLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
        customLayout.addComponent(cbxProvince, "cbxProvince");
        customLayout.addComponent(new Label(I18N.message("district")), "lblDistrict");
        customLayout.addComponent(cbxDistrict, "cbxDistrict");
        customLayout.addComponent(new Label(I18N.message("commune")), "lblCommune");
        customLayout.addComponent(cbxCommune, "cbxCommune");
        customLayout.addComponent(new Label(I18N.message("postal.code")), "lblPostCode");
        customLayout.addComponent(txtPostCode, "txtPostCode");
		addComponent(customLayout);
	}
	
	/**
	 *@param enabled 
	 */
	public void setAddressEnabled(boolean enabled) {
		if (enabled) {
	    	cbxCountry.removeStyleName("v-textfield-blackdisabled");
	    	cbxProvince.removeStyleName("v-textfield-blackdisabled");
	    	cbxDistrict.removeStyleName("v-textfield-blackdisabled");
	    	cbxCommune.removeStyleName("v-textfield-blackdisabled");
	    	cbxAddressType.removeStyleName("v-textfield-blackdisabled");
	    	// cbxVillage.removeStyleName("v-textfield-blackdisabled");
	    	txtBuilding.removeStyleName("blackdisabled");
			txtStreet.removeStyleName("blackdisabled");
			txtMoo.removeStyleName("blackdisabled");
			txtSoi.removeStyleName("blackdisabled");
			txtPostCode.removeStyleName("blackdisabled");
		    //txtTimeAtAddressInYear.removeStyleName("blackdisabled");
		    //txtTimeAtAddressInMonth.removeStyleName("blackdisabled");
	    } else {
	    	cbxCountry.addStyleName("v-textfield-blackdisabled");
	    	cbxProvince.addStyleName("v-textfield-blackdisabled");
	    	cbxDistrict.addStyleName("v-textfield-blackdisabled");
	    	cbxCommune.addStyleName("v-textfield-blackdisabled");
	    	cbxAddressType.addStyleName("v-textfield-blackdisabled");
	    	// cbxVillage.addStyleName("v-textfield-blackdisabled");
			txtBuilding.addStyleName("blackdisabled");
			txtStreet.addStyleName("blackdisabled");
			txtMoo.addStyleName("blackdisabled");
			txtSoi.addStyleName("blackdisabled");
			txtPostCode.addStyleName("blackdisabled");
		    //txtTimeAtAddressInYear.addStyleName("blackdisabled");
		    //txtTimeAtAddressInMonth.addStyleName("blackdisabled");  
	    }
	    
	    txtBuilding.setEnabled(enabled);
	    txtStreet.setEnabled(enabled);
	    txtMoo.setEnabled(enabled);
	    txtSoi.setEnabled(enabled);
	    txtPostCode.setEnabled(enabled);
	    cbxCountry.setEnabled(enabled);
	    cbxProvince.setEnabled(enabled);
	    cbxDistrict.setEnabled(enabled);
	    cbxCommune.setEnabled(enabled);
	    cbxAddressType.setEnabled(enabled);
	    // cbxVillage.setEnabled(enabled);
	    //txtTimeAtAddressInYear.setEnabled(enabled);
	    //txtTimeAtAddressInMonth.setEnabled(enabled);
	    //cbxpropertyAddress.setEnabled(enabled);
	}
	
	/**
	 * @param address
	 * @return
	 */
	public Address getAddress(Address address) {
		address.setHouseNo(txtBuilding.getValue());
		address.setStreet(txtStreet.getValue());
		address.setLine1(txtMoo.getValue());
		address.setLine2(txtSoi.getValue());
		address.setTimeAtAddressInYear(getInteger(txtTimeAtAddressInYear));
		address.setTimeAtAddressInMonth(getInteger(txtTimeAtAddressInMonth));
		address.setPostalCode(txtPostCode.getValue());
		address.setResidenceStatus(cbxpropertyAddress.getSelectedEntity());
		//address.setLongitude(getDouble(txtLongValue));
		//address.setLatitude(getDouble(txtLateValue));
		address.setCountry(cbxCountry.getSelectedEntity());
		// address.setVillage(cbxVillage.getSelectedEntity());
		address.setCommune(cbxCommune.getSelectedEntity());
		address.setDistrict(cbxDistrict.getSelectedEntity());
		address.setProvince(cbxProvince.getSelectedEntity());	
		address.setType(cbxAddressType.getSelectedEntity());
		return address;
	}
	
	public void assignValues(Address address) {
		txtBuilding.setValue(getDefaultString(address.getHouseNo()));      
		txtStreet.setValue(getDefaultString(address.getStreet()));
		txtMoo.setValue(getDefaultString(address.getLine1()));
		txtSoi.setValue(getDefaultString(address.getLine2()));
		txtTimeAtAddressInYear.setValue(getDefaultString(address.getTimeAtAddressInYear()));
		txtTimeAtAddressInMonth.setValue(getDefaultString(address.getTimeAtAddressInMonth()));
		txtPostCode.setValue(getDefaultString(address.getPostalCode()));
		cbxpropertyAddress.setSelectedEntity(address.getResidenceStatus());
		//txtLongValue.setValue(getDefaultString(address.getLongitude()));
		//txtLateValue.setValue(getDefaultString(address.getLatitude()));
		cbxCountry.setSelectedEntity(address.getCountry());
		cbxProvince.setSelectedEntity(address.getProvince());
		cbxDistrict.setSelectedEntity(address.getDistrict());
		cbxCommune.setSelectedEntity(address.getCommune());
		cbxAddressType.setSelectedEntity(address.getType());
		// cbxVillage.setSelectedEntity(address.getVillage());
		setAddress(address);
		
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Address());
		cbxCountry.setSelectedEntity(ECountry.THA);
	}
	
	/**
	 * @return
	 */	
	public List<String> fullValidate() {
		super.reset();
		if (required) {
			// checkMandatoryField(txtHouseNo, "house.no");
			// checkMandatoryField(txtStreet, "street");
			checkMandatoryField(txtBuilding, "number.building");
			checkMandatoryField(txtTimeAtAddressInYear, "time.at.address");
			checkMandatoryField(txtTimeAtAddressInMonth, "time.at.address");
			checkMandatoryField(txtStreet, "street");
			checkMandatorySelectField(cbxCountry, "country");
			checkMandatorySelectField(cbxProvince, "province");
			checkMandatorySelectField(cbxDistrict, "district");
			checkMandatorySelectField(cbxCommune, "commune");
			checkMandatorySelectField(cbxAddressType, "address.type");
			// checkMandatorySelectField(cbxVillage, "village");
		}
		return errors;
	}
	
	/**
	 * Validate Dealer Address
	 * @return
	 */
	public List<String> validateDealerAddress() {
		super.reset();
		if (required) {
			// checkMandatoryField(txtHouseNo, "house.no");
			// checkMandatoryField(txtStreet, "street");
			checkMandatoryField(txtBuilding, "number.building");
			checkMandatoryField(txtStreet, "street");
			checkMandatorySelectField(cbxCountry, "country");
			checkMandatorySelectField(cbxProvince, "province");
			checkMandatorySelectField(cbxDistrict, "district");
			checkMandatorySelectField(cbxCommune, "commune");
			checkMandatorySelectField(cbxAddressType, "address.type");
			// checkMandatorySelectField(cbxVillage, "village");
		}
		return errors;
	}
	/**
	 * @return
	 */	
	public List<String> partialValidate() {
		super.reset();
		if (required) {
			checkMandatorySelectField(cbxCountry, "country");
			checkMandatorySelectField(cbxProvince, "province");
			checkMandatorySelectField(cbxDistrict, "district");
			checkMandatorySelectField(cbxCommune, "commune");
			// checkMandatorySelectField(cbxVillage, "village");
		}
		return errors;
	}
	
	/**
	 * Validate address
	 * @param address
	 * @return
	 */
	public boolean validateAddress() {
		boolean isValid = false;
		
		if (StringUtils.isNotEmpty(txtBuilding.getValue())
				|| StringUtils.isNotEmpty(txtMoo.getValue())
				|| StringUtils.isNotEmpty(txtSoi.getValue())
				|| StringUtils.isNotEmpty(txtStreet.getValue())
				|| StringUtils.isNotEmpty(txtPostCode.getValue())
				|| cbxProvince.getSelectedEntity() != null
				|| cbxDistrict.getSelectedEntity() != null
				|| cbxCommune.getSelectedEntity() != null) {
			isValid = true;
		}
		
		return isValid;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
