package com.nokor.efinance.core.applicant.panel.address;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 *
 */
public class AddressPopupPanel extends Window {
	
	private static final long serialVersionUID = 1899042462222038683L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
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
    private Individual boIndividual;
    private Address address;
    private IndividualAddress boIndividualAddress;
    private AddressesPanel addressPanel;
    private CheckBox cbActive;
	/**
	 * @param AddressPanel the AddressPanel to set
	 */
	public void setAddressPanel(AddressesPanel addressPanel) {
		this.addressPanel = addressPanel;
	}

	public AddressPopupPanel(Individual individual, String caption) {
		this.boIndividual = individual;
		setModal(true);
		final Window winAddService = new Window(caption);
		winAddService.setModal(true);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
		txtHouseNo = ComponentFactory.getTextField(false, 10, 80);      
		txtStreet = ComponentFactory.getTextField(false, 100, 210);
		txtLine1 = ComponentFactory.getTextField(false, 100, 330);
		
		txtTimeAtAddressInYear = ComponentFactory.getTextField(false, 50, 50);
		txtTimeAtAddressInMonth = ComponentFactory.getTextField(false, 50, 50);
		cbxpropertyAddress = new ERefDataComboBox<EResidenceStatus>(EResidenceStatus.values());
		
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
        String template = "address";
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
        customLayout.addComponent(new Label(I18N.message("housing")), "lblpropertyAddress");
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
        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(customLayout);
        verticalLayout.addComponent(cbActive);
	    formLayout.addComponent(verticalLayout);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
        	
			private static final long serialVersionUID = 8088485001713740490L;

			public void buttonClick(ClickEvent event) {
				
				if(validate()){
					boolean isNewAddress = false;
					if (address == null){
					isNewAddress = true;
					address = new Address();
					}
					address.setHouseNo(txtHouseNo.getValue());
					address.setStreet(txtStreet.getValue());
					address.setLine1(txtLine1.getValue());		
					address.setTimeAtAddressInYear(getInteger(txtTimeAtAddressInYear));
					address.setTimeAtAddressInMonth(getInteger(txtTimeAtAddressInMonth));
					address.setResidenceStatus(cbxpropertyAddress.getSelectedEntity());
					address.setCountry(cbxCountry.getSelectedEntity());
					address.setVillage(cbxVillage.getSelectedEntity());
					address.setCommune(cbxCommune.getSelectedEntity());
					address.setDistrict(cbxDistrict.getSelectedEntity());
					address.setProvince(cbxProvince.getSelectedEntity());
					address.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
					entityService.saveOrUpdate(address);
					if (isNewAddress) {
						boIndividualAddress = new IndividualAddress();
						boIndividualAddress.setAddress(address);
						boIndividualAddress.setIndividual(boIndividual);
						boIndividualAddress.getAddress().setType(ETypeAddress.MAIN);					
						entityService.saveOrUpdate(boIndividualAddress);	
					}		
					winAddService.close();
					entityService.refresh(boIndividualAddress);
					addressPanel.assignValues(boIndividualAddress.getIndividual().getIndividualAddresses());
				} else {

					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
            }
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAddService.close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
        contentLayout.addComponent(formLayout);
        winAddService.setContent(contentLayout);
        winAddService.setWidth(600, Unit.PIXELS);
        winAddService.setHeight(400, Unit.PIXELS);
        UI.getCurrent().addWindow(winAddService);
	}
	
	/**
	 * @param Address
	 */
	public void assignValues(Address address, IndividualAddress boIndividualAddress) {
		this.boIndividualAddress = boIndividualAddress;
		this.address = address;
		txtHouseNo.setValue(address.getHouseNo() == null ? "" : address.getHouseNo());      
		txtStreet.setValue(address.getStreet() == null ? "" : address.getStreet());
		txtLine1.setValue(address.getLine1() == null ? "" : address.getLine1());
		txtTimeAtAddressInYear.setValue(address.getTimeAtAddressInYear() == null ? "0" : address.getTimeAtAddressInYear().toString());
		txtTimeAtAddressInMonth.setValue(address.getTimeAtAddressInMonth() == null ? "0" : address.getTimeAtAddressInMonth().toString());
		cbxpropertyAddress.setSelectedEntity(address.getResidenceStatus());
		cbxCountry.setSelectedEntity(address.getCountry());
		cbxProvince.setSelectedEntity(address.getProvince());
		cbxDistrict.setSelectedEntity(address.getDistrict());
		cbxCommune.setSelectedEntity(address.getCommune());
		cbxVillage.setSelectedEntity(address.getVillage());
		cbActive.setValue((address.getStatusRecord().equals(EStatusRecord.INACT))? false : true );
	}
	
	public void resetAddressPopupPanel() {
		txtHouseNo.setValue("");
		txtStreet.setValue("");
		txtLine1.setValue("");
		txtTimeAtAddressInYear.setValue("0");
		txtTimeAtAddressInMonth.setValue("0");
		cbxpropertyAddress.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxCommune.setSelectedEntity(null);
		cbxVillage.setSelectedEntity(null);
		cbActive.setValue(true);
		
	}
	
	private boolean validate() {
		boolean isValide = true;
		if(txtTimeAtAddressInYear.getValue() == null || txtTimeAtAddressInMonth.getValue() == null
				|| cbxpropertyAddress.getSelectedEntity() == null || cbxCountry.getSelectedEntity() == null
				|| cbxProvince.getSelectedEntity() == null || cbxVillage.getSelectedEntity() == null
				|| cbxDistrict.getSelectedEntity() == null || cbxCommune.getSelectedEntity() == null) {
			isValide = false;
			
		}
		return isValide;
	}
	
	protected Integer getInteger(AbstractTextField field) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
