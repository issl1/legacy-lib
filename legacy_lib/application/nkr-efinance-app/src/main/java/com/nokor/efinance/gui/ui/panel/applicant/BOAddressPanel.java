package com.nokor.efinance.gui.ui.panel.applicant;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
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
 * @author sok.vina
 *
 */
public class BOAddressPanel extends AbstractControlPanel {

	private static final long serialVersionUID = -765492115214802081L;
	
	private TextField txtHouseNo;
	private TextField txtStreet;
    private TextField txtLine1;
    private ERefDataComboBox<ECountry> cbxCountry;
    private EntityRefComboBox<Province> cbxProvince;
    private EntityRefComboBox<District> cbxDistrict;
    private EntityRefComboBox<Commune> cbxCommune;
    private EntityRefComboBox<Village> cbxVillage;

    private TextField txtTimeAtAddressInYear;
    private TextField txtTimeAtAddressInMonth;
    private ERefDataComboBox<EResidenceStatus> cbxpropertyAddress;
    
    public BOAddressPanel(ETypeAddress addressType) {
    	this(false, addressType, "address");
    }
    
    public BOAddressPanel(boolean required, ETypeAddress addressType) {
    	this(required, addressType, "address");
    }
    
	public BOAddressPanel(boolean required, ETypeAddress addressType, String template) {
		setSizeFull();
		txtHouseNo = ComponentFactory.getTextField(false, 10, 80);      
		txtStreet = ComponentFactory.getTextField(false, 100, 210);
		txtLine1 = ComponentFactory.getTextField(false, 100, 330);
		
		txtTimeAtAddressInYear = ComponentFactory.getTextField(false, 50, 50);
		txtTimeAtAddressInMonth = ComponentFactory.getTextField(false, 50, 50);
		cbxpropertyAddress = new ERefDataComboBox<EResidenceStatus>(EResidenceStatus.values());
		if (addressType == ETypeAddress.WORK) {
			cbxpropertyAddress.setVisible(false);
		}
		
		cbxCountry = new ERefDataComboBox<ECountry>(ECountry.values());
		cbxCountry.setImmediate(true);
		cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setImmediate(true);
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxDistrict = new EntityRefComboBox<District>();
		cbxDistrict.setImmediate(true);
		cbxDistrict.setRestrictions(new BaseRestrictions<District>(District.class));
	    cbxCommune = new EntityRefComboBox<Commune>();
	    cbxCommune.setImmediate(true);
	    cbxCommune.setRestrictions(new BaseRestrictions<Commune>(Commune.class));
	    cbxVillage = new EntityRefComboBox<Village>();
	    cbxVillage.setRestrictions(new BaseRestrictions<Village>(Village.class));
	    cbxVillage.setImmediate(true);    
	    
		cbxCountry.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3023654283916642525L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCountry.getSelectedEntity() != null) {
					BaseRestrictions<Province> restrictions = cbxProvince.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("country", cbxCountry.getSelectedEntity()));
					restrictions.setCriterions(criterions);
					cbxProvince.renderer();
				} else {
					cbxProvince.clear();
				}
				cbxDistrict.clear();
				cbxCommune.clear();
				cbxVillage.clear();
			}
		});
					    
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -78822726610125810L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = cbxDistrict.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("province.id", cbxProvince.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
				}
				cbxCommune.clear();
				cbxVillage.clear();
			}
		});
			    
	    cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1761539763125185708L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = cbxCommune.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("district.id", cbxDistrict.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					cbxCommune.renderer();
				} else {
					cbxCommune.clear();
				}
				cbxVillage.clear();
			}
		});
	    
	    cbxCommune.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4700289434066242228L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCommune.getSelectedEntity() != null) {
					BaseRestrictions<Village> restrictions = cbxVillage.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("commune.id", cbxCommune.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					cbxVillage.renderer();
				} else {
					cbxVillage.clear();
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

        customLayout.addComponent(new Label(I18N.message("house.no")), "lblHouseNo");
        customLayout.addComponent(txtHouseNo, "txtHouseNo");
        customLayout.addComponent(new Label(I18N.message("street")), "lblStreet");
        customLayout.addComponent(txtStreet, "txtStreet");
        customLayout.addComponent(txtLine1, "txtLine1");
        
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
        customLayout.addComponent(new Label(I18N.message("village")), "lblVillage");
        customLayout.addComponent(cbxVillage, "cbxVillage");
		addComponent(customLayout);
	}
	
	public void assignValues(Address address) {
		txtHouseNo.setValue(getDefaultString(address.getHouseNo()));      
		txtStreet.setValue(getDefaultString(address.getStreet()));
		txtLine1.setValue(getDefaultString(address.getLine1()));
		txtTimeAtAddressInYear.setValue(getDefaultString(address.getTimeAtAddressInYear()));
		txtTimeAtAddressInMonth.setValue(getDefaultString(address.getTimeAtAddressInMonth()));
		cbxpropertyAddress.setSelectedEntity(address.getResidenceStatus());
		cbxCountry.setSelectedEntity(address.getCountry());
		cbxProvince.setSelectedEntity(address.getProvince());
		cbxDistrict.setSelectedEntity(address.getDistrict());
		cbxCommune.setSelectedEntity(address.getCommune());
		cbxVillage.setSelectedEntity(address.getVillage());
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Address());
		cbxCountry.setSelectedEntity(ECountry.KHM);
	}
}
