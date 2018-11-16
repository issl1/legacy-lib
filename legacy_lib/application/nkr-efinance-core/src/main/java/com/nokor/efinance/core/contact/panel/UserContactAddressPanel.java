package com.nokor.efinance.core.contact.panel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.eref.ECountry;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.EResidenceType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
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
public class UserContactAddressPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	private static final long serialVersionUID = 7226393003541863208L;
	private Individual individual;
	
	private Button btnAdd;
	private VerticalLayout addressLayout;
	private List<Address> indAddresses;
   
	/**
	 * 
	 */
	public UserContactAddressPanel() {
		addressLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(addressLayout);
		mainLayout.addComponent(btnAdd);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("addresses"));
		fieldSet.setContent(mainLayout);
		
		Panel mainPanel = new Panel(fieldSet);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * AssignValue
	 * @param individual
	 */
	public void assignValue() {
		if (individual != null) {
			Applicant applicant = APP_SRV.getApplicantCategory(EApplicantCategory.INDIVIDUAL, individual.getId());
			this.indAddresses = AddressUtils.getAddressesByApplicant(applicant);
		}
		addressLayout.removeAllComponents();
		if (this.indAddresses != null && !this.indAddresses.isEmpty()) {
			List<IndividualAddress> individualAddresses = getIndividualAddress(individual);
			List<ETypeAddress> typeAddresses = ETypeAddress.valuesOfApplicants();
			if (individualAddresses != null && !individualAddresses.isEmpty()) {
				for (IndividualAddress individualAddress : individualAddresses) {
					Address address = individualAddress.getAddress();
					addressLayout.addComponent(createAddressGridLayout(address, address.getType()));
					typeAddresses.remove(address.getType());
				}
				for (ETypeAddress typeAddress : typeAddresses) {
					addressLayout.addComponent(createAddressGridLayout(new Address(), typeAddress));
				}
			} else {
				addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.MAIN));
				addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.WORK));
				addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.IDCARDADDRESS));
				addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.HRADDRESS));
				addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.MAILADDRESS));
			}
		} else {
			addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.MAIN));
			addressLayout.addComponent(createAddressGridLayout(new Address(), ETypeAddress.MAILADDRESS));
		}
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
	 * createAddressGridLayout
	 * @return
	 */
	private Panel createAddressGridLayout(Address address, ETypeAddress addressType) {
		GridLayout addressGridLayout = ComponentLayoutFactory.getGridLayout(23, 3);
		addressGridLayout.setSpacing(true);
		addressGridLayout.setMargin(new MarginInfo(true, false, false, false));
		
		Label lblTypeAddress = ComponentLayoutFactory.getLabelCaptionRequired("address.type");
		Label lblResidenceType = ComponentLayoutFactory.getLabelCaption("residence.type");
		Label lblResidenceStatus = ComponentLayoutFactory.getLabelCaption("residence.status");
		Label lblFloorRoom = ComponentLayoutFactory.getLabelCaption("floor.room");
		Label lblSoi = ComponentLayoutFactory.getLabelCaption("soi");
		Label lblPostCodes = ComponentLayoutFactory.getLabelCaption("postal.code");
		Label lblYear = ComponentLayoutFactory.getLabelCaption("year");
		Label lblMonth = ComponentLayoutFactory.getLabelCaption("month");
		Label lblProvince = ComponentLayoutFactory.getLabelCaption("province");
		Label lblDistrict = ComponentLayoutFactory.getLabelCaption("district");
		Label lblSubDistrict = ComponentLayoutFactory.getLabelCaption("sub.district");
		
		Label lblBuilding = ComponentLayoutFactory.getLabelCaption("number.building");
		Label lblMoo = ComponentLayoutFactory.getLabelCaption("moo");
		Label lblStreet = ComponentLayoutFactory.getLabelCaption("street");
		Label lblCountry = ComponentLayoutFactory.getLabelCaption("country");
		
		ERefDataComboBox<EResidenceType> cbxResidesceType = new ERefDataComboBox<>(EResidenceType.values());
		cbxResidesceType.setWidth("145px");
		ERefDataComboBox<EResidenceStatus> cbxResidesceStatus = new ERefDataComboBox<>(EResidenceStatus.values());
		cbxResidesceStatus.setWidth("145px");
		ERefDataComboBox<ETypeAddress> cbxTypeAddress = new ERefDataComboBox<>(ETypeAddress.valuesOfApplicants());
		cbxTypeAddress.setWidth("110px");
		cbxTypeAddress.setNullSelectionAllowed(false);
		if (addressType != null) {
			cbxTypeAddress.setSelectedEntity(addressType);
			cbxTypeAddress.setEnabled(false);
		} else {
			cbxTypeAddress.setSelectedEntity(ETypeAddress.MAIN);
		}
	
		ERefDataComboBox<ETypeAddress> cbxCopyFrom = new ERefDataComboBox<>(ETypeAddress.valuesOfApplicants());
		cbxCopyFrom.setWidth("110px");
		
		TextField txtNo = ComponentFactory.getTextField(60, 70);
		TextField txtBuilding = ComponentFactory.getTextField(60, 70);
		TextField txtFloor = ComponentFactory.getTextField(60, 70);
		TextField txtRoom = ComponentFactory.getTextField(60, 70);
		TextField txtMoo = ComponentFactory.getTextField(60, 145);
		TextField txtStreet = ComponentFactory.getTextField(60, 145);
		ERefDataComboBox<ECountry> cbxCountry = new ERefDataComboBox<>(ECountry.values());
		cbxCountry.setWidth(145, Unit.PIXELS);
		
		TextField txtSoi = ComponentFactory.getTextField(60, 145);
		TextField txtPostalCode = ComponentFactory.getTextField(60, 145);
		TextField txtYear = ComponentFactory.getTextField(60, 145);
		TextField txtMonth = ComponentFactory.getTextField(60, 155);
		
		EntityComboBox<Province> cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth(145, Unit.PIXELS);
		cbxProvince.renderer();
		
		EntityComboBox<District> cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setImmediate(true);
		cbxDistrict.setWidth(145, Unit.PIXELS);
		
		EntityComboBox<Commune> cbxCommune = new EntityComboBox<>(Commune.class, "desc");
	    cbxCommune.setImmediate(true);
	    cbxCommune.setWidth(155, Unit.PIXELS);
	    
	    cbxCopyFrom.addValueChangeListener(new ValueChangeListener() {

	    	/** */
			private static final long serialVersionUID = 3628553745892929213L;

			/**
	    	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	    	 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCopyFrom.getSelectedEntity() != null) {
					List<IndividualAddress> individualAddresses = getIndividualAddress(individual);
					for (IndividualAddress individualAddress : individualAddresses) {
						Address address = individualAddress.getAddress();
						if (cbxCopyFrom.getSelectedEntity().equals(address.getType())) {
							txtNo.setValue(getDefaultString(address.getHouseNo()));
							txtBuilding.setValue(getDefaultString(address.getBuildingName()));
							
							txtFloor.setValue(getDefaultString(address.getFloor()));
							txtRoom.setValue(getDefaultString(address.getRoomNumber()));
							txtMoo.setValue(getDefaultString(address.getLine1()));
							txtSoi.setValue(getDefaultString(address.getLine2()));
							txtStreet.setValue(getDefaultString(address.getStreet()));
							cbxCountry.setSelectedEntity(address.getCountry());
							txtMonth.setValue(getDefaultString(MyNumberUtils.getInteger(address.getTimeAtAddressInMonth())));
							txtYear.setValue(getDefaultString(MyNumberUtils.getInteger(address.getTimeAtAddressInYear())));
							cbxResidesceStatus.setSelectedEntity(address.getResidenceStatus());
							cbxResidesceType.setSelectedEntity(address.getResidenceType());
							Commune subDistrict = address.getCommune();
							District district = subDistrict == null ? null : subDistrict.getDistrict();
							cbxProvince.setSelectedEntity(district == null ? null : district.getProvince());
							cbxDistrict.setSelectedEntity(district);
							cbxCommune.setSelectedEntity(subDistrict);
							txtPostalCode.setValue(getDefaultString(address.getPostalCode()));
							return;
						}
					}
				} else {
					txtNo.setValue(StringUtils.EMPTY);
					txtBuilding.setValue(StringUtils.EMPTY);
					txtMoo.setValue(StringUtils.EMPTY);
					txtSoi.setValue(StringUtils.EMPTY);
					txtFloor.setValue(StringUtils.EMPTY);
					txtRoom.setValue(StringUtils.EMPTY);
					txtStreet.setValue(StringUtils.EMPTY);
					cbxCountry.setSelectedEntity(null);
					txtMonth.setValue(StringUtils.EMPTY);
					txtYear.setValue(StringUtils.EMPTY);
					cbxResidesceStatus.setSelectedEntity(null);
					cbxResidesceType.setSelectedEntity(null);
					cbxProvince.setSelectedEntity(null);
					cbxDistrict.setSelectedEntity(null);
					cbxCommune.setSelectedEntity(null);
					txtPostalCode.setValue(StringUtils.EMPTY);
				}
			}
		});
 
	    cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -7299339921904658556L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
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
			}
		});
	    
	    cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			
	    	/** */
			private static final long serialVersionUID = 5384028525883581970L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq("district", cbxDistrict.getSelectedEntity()));
					cbxCommune.setEntities(ENTITY_SRV.list(restrictions));
				} else {
					cbxCommune.clear();
				}
			}
		});
	    
	    cbxCommune.addValueChangeListener(new ValueChangeListener() {
	    	
	    	/** */
			private static final long serialVersionUID = 3638832089146922125L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCommune.getSelectedEntity() != null) {
					Commune commune = ENTITY_SRV.getById(Commune.class, cbxCommune.getSelectedEntity().getId());
					txtPostalCode.setValue(commune.getPostalCode());
				} else {
					txtPostalCode.setValue(StringUtils.EMPTY);
				}
			}
		});
	    
	    Button btnEditBuilding = new Button(FontAwesome.EDIT);
	    btnEditBuilding.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditBuilding.setVisible(false);
	    btnEditBuilding.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -1262729577629008133L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditBuilding.getIcon())) {
					txtNo.setEnabled(true);
					txtBuilding.setEnabled(true);
					
					btnEditBuilding.setIcon(FontAwesome.SAVE);
				} else {
					btnEditBuilding.setIcon(FontAwesome.EDIT);
					txtNo.setEnabled(false);
					txtBuilding.setEnabled(false);
					address.setHouseNo(txtNo.getValue());
					address.setBuildingName(txtBuilding.getValue());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditFloorRoom = new Button(FontAwesome.EDIT);
	    btnEditFloorRoom.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditFloorRoom.setVisible(false);
	    btnEditFloorRoom.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8753244193330290895L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditFloorRoom.getIcon())) {
					txtFloor.setEnabled(true);
					txtRoom.setEnabled(true);
					btnEditFloorRoom.setIcon(FontAwesome.SAVE);
				} else {
					btnEditFloorRoom.setIcon(FontAwesome.EDIT);
					txtFloor.setEnabled(false);
					txtRoom.setEnabled(false);
					address.setFloor(txtFloor.getValue());
					address.setRoomNumber(txtRoom.getValue());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});

	    Button btnEditMoo = new Button(FontAwesome.EDIT);
	    btnEditMoo.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditMoo.setVisible(false);
	    btnEditMoo.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8753244193330290895L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditMoo.getIcon())) {
					txtMoo.setEnabled(true);
					btnEditMoo.setIcon(FontAwesome.SAVE);
				} else {
					btnEditMoo.setIcon(FontAwesome.EDIT);
					txtMoo.setEnabled(false);
					address.setLine1(getDefaultString(txtMoo.getValue()));
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	   
	    
	    Button btnEditStreet = new Button(FontAwesome.EDIT);
	    btnEditStreet.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditStreet.setVisible(false);
	    btnEditStreet.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -1559220901538731597L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditStreet.getIcon())) {
					txtStreet.setEnabled(true);
					btnEditStreet.setIcon(FontAwesome.SAVE);
				} else {
					btnEditStreet.setIcon(FontAwesome.EDIT);
					txtStreet.setEnabled(false);
					address.setStreet(getDefaultString(txtStreet.getValue()));
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditCountry = new Button(FontAwesome.EDIT);
	    btnEditCountry.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditCountry.setVisible(false);
	    btnEditCountry.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = 6613197658083748522L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditCountry.getIcon())) {
					cbxCountry.setEnabled(true);
					btnEditCountry.setIcon(FontAwesome.SAVE);
				} else {
					btnEditCountry.setIcon(FontAwesome.EDIT);
					cbxCountry.setEnabled(false);
					address.setCountry(cbxCountry.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditResidenceType = new Button(FontAwesome.EDIT);
	    btnEditResidenceType.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditResidenceType.setVisible(false);
	    btnEditResidenceType.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 4632276864169179693L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditResidenceType.getIcon())) {
					cbxResidesceType.setEnabled(true);
					btnEditResidenceType.setIcon(FontAwesome.SAVE);
				} else {
					btnEditResidenceType.setIcon(FontAwesome.EDIT);
					cbxResidesceType.setEnabled(false);
					address.setResidenceType(cbxResidesceType.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditSoiRoad = new Button(FontAwesome.EDIT);
	    btnEditSoiRoad.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditSoiRoad.setVisible(false);
	    btnEditSoiRoad.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -2220695648944862856L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditSoiRoad.getIcon())) {
					txtSoi.setEnabled(true);
					btnEditSoiRoad.setIcon(FontAwesome.SAVE);
				} else {
					btnEditSoiRoad.setIcon(FontAwesome.EDIT);
					txtSoi.setEnabled(false);
					address.setLine2(txtSoi.getValue());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditProvince = new Button(FontAwesome.EDIT);
	    btnEditProvince.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditProvince.setVisible(false);
	    btnEditProvince.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 3723244984036778037L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditProvince.getIcon())) {
					cbxProvince.setEnabled(true);
					btnEditProvince.setIcon(FontAwesome.SAVE);
				} else {
					btnEditProvince.setIcon(FontAwesome.EDIT);
					cbxProvince.setEnabled(false);
					address.setProvince(cbxProvince.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditDistrict = new Button(FontAwesome.EDIT);
	    btnEditDistrict.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditDistrict.setVisible(false);
	    btnEditDistrict.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -3772221626407969033L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditDistrict.getIcon())) {
					cbxDistrict.setEnabled(true);
					btnEditDistrict.setIcon(FontAwesome.SAVE);
				} else {
					btnEditDistrict.setIcon(FontAwesome.EDIT);
					cbxDistrict.setEnabled(false);
					address.setDistrict(cbxDistrict.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditSubDistrict = new Button(FontAwesome.EDIT);
	    btnEditSubDistrict.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditSubDistrict.setVisible(false);
	    btnEditSubDistrict.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -6999121894958303475L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditSubDistrict.getIcon())) {
					cbxCommune.setEnabled(true);
					btnEditSubDistrict.setIcon(FontAwesome.SAVE);
				} else {
					btnEditSubDistrict.setIcon(FontAwesome.EDIT);
					cbxCommune.setEnabled(false);
					address.setCommune(cbxCommune.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditPostalCode = new Button(FontAwesome.EDIT);
	    btnEditPostalCode.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditPostalCode.setVisible(false);
	    btnEditPostalCode.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7075451397230023881L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditPostalCode.getIcon())) {
					txtPostalCode.setEnabled(true);
					btnEditPostalCode.setIcon(FontAwesome.SAVE);
				} else {
					btnEditPostalCode.setIcon(FontAwesome.EDIT);
					txtPostalCode.setEnabled(false);
					address.setPostalCode(txtPostalCode.getValue());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditResidenceStatus = new Button(FontAwesome.EDIT);
	    btnEditResidenceStatus.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditResidenceStatus.setVisible(false);
	    btnEditResidenceStatus.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7769929031284563915L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditResidenceStatus.getIcon())) {
					cbxResidesceStatus.setEnabled(true);
					btnEditResidenceStatus.setIcon(FontAwesome.SAVE);
				} else {
					btnEditResidenceStatus.setIcon(FontAwesome.EDIT);
					cbxResidesceStatus.setEnabled(false);
					address.setResidenceStatus(cbxResidesceStatus.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditYear = new Button(FontAwesome.EDIT);
	    btnEditYear.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditYear.setVisible(false);
	    btnEditYear.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -5455373322216143129L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditYear.getIcon())) {
					txtYear.setEnabled(true);
					btnEditYear.setIcon(FontAwesome.SAVE);
				} else {
					btnEditYear.setIcon(FontAwesome.EDIT);
					txtYear.setEnabled(false);
					address.setTimeAtAddressInYear(getInteger(txtYear));
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditMonth = new Button(FontAwesome.EDIT);
	    btnEditMonth.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditMonth.setVisible(false);
	    btnEditMonth.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 2378551652817340001L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditMonth.getIcon())) {
					txtMonth.setEnabled(true);
					btnEditMonth.setIcon(FontAwesome.SAVE);
				} else {
					btnEditMonth.setIcon(FontAwesome.EDIT);
					txtMonth.setEnabled(false);
					address.setTimeAtAddressInMonth(getInteger(txtMonth));
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 1533454299623884640L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				address.setHouseNo(txtNo.getValue());
				address.setBuildingName(txtBuilding.getValue());
				address.setFloor(txtFloor.getValue());
				address.setRoomNumber(txtRoom.getValue());
				address.setType(cbxTypeAddress.getSelectedEntity());
				address.setResidenceType(cbxResidesceType.getSelectedEntity());
				address.setLine1(txtMoo.getValue());
				address.setLine2(txtSoi.getValue());
				address.setStreet(txtStreet.getValue());
				address.setCountry(cbxCountry.getSelectedEntity());
				address.setProvince(cbxProvince.getSelectedEntity());
				address.setDistrict(cbxDistrict.getSelectedEntity());
				address.setCommune(cbxCommune.getSelectedEntity());
				address.setPostalCode(txtPostalCode.getValue());
				address.setResidenceStatus(cbxResidesceStatus.getSelectedEntity());
				address.setTimeAtAddressInYear(getInteger(txtYear));
				address.setTimeAtAddressInMonth(getInteger(txtMonth));
				
				btnSave.setVisible(false);
				btnEditBuilding.setVisible(true);
				btnEditFloorRoom.setVisible(true);
				btnEditMoo.setVisible(true);
				btnEditStreet.setVisible(true);
				btnEditCountry.setVisible(true);
				btnEditResidenceType.setVisible(true);
				btnEditSoiRoad.setVisible(true);
				btnEditProvince.setVisible(true);
				btnEditDistrict.setVisible(true);
				btnEditSubDistrict.setVisible(true);	
				btnEditPostalCode.setVisible(true);
				btnEditResidenceStatus.setVisible(true);
				btnEditYear.setVisible(true);
				btnEditMonth.setVisible(true);
				
				cbxTypeAddress.setEnabled(false);
				txtNo.setEnabled(false);
				txtBuilding.setEnabled(false);
				txtFloor.setEnabled(false);
				txtRoom.setEnabled(false);
				txtMoo.setEnabled(false);
				txtStreet.setEnabled(false);
				cbxCountry.setEnabled(false);
				cbxResidesceType.setEnabled(false);
				txtSoi.setEnabled(false);
				cbxProvince.setEnabled(false);
				cbxDistrict.setEnabled(false);
				cbxCommune.setEnabled(false);
				txtPostalCode.setEnabled(false);
				cbxResidesceStatus.setEnabled(false);
				txtYear.setEnabled(false);
				txtMonth.setEnabled(false);
				
				IndividualAddress individualAddress = null;
				if (individual != null && individual.getId() != null) {
					individualAddress = IndividualAddress.createInstance();
					individualAddress.setIndividual(individual);
					individualAddress.setAddress(address);
				}
	
				try {
					if (individualAddress != null) {
						INDIVI_SRV.saveOrUpdateIndividualAddress(individualAddress);
					}
					ComponentLayoutFactory.displaySuccessfullyMsg();
					assignValue();
				} catch (Exception e) {
					ComponentLayoutFactory.displayErrorMsg(e.getMessage());
				}
			}
		});
		
		if (address.getId() != null) {
			txtNo.setValue(getDefaultString(address.getHouseNo()));
			txtBuilding.setValue(getDefaultString(address.getBuildingName()));
			txtFloor.setValue(getDefaultString(address.getFloor()));
			txtRoom.setValue(getDefaultString(address.getRoomNumber()));
			txtMoo.setValue(address.getLine1());
			txtStreet.setValue(address.getStreet());
			cbxCountry.setSelectedEntity(address.getCountry());
			cbxResidesceType.setSelectedEntity(address.getResidenceType());
			txtSoi.setValue(getDefaultString(address.getLine2()));
			cbxProvince.setSelectedEntity(address.getProvince());
			cbxDistrict.setSelectedEntity(address.getDistrict());
			cbxCommune.setSelectedEntity(address.getCommune());
			cbxResidesceStatus.setSelectedEntity(address.getResidenceStatus());
			txtYear.setValue(getDefaultString(address.getTimeAtAddressInYear()));
			txtMonth.setValue(getDefaultString(address.getTimeAtAddressInMonth()));
			
			btnSave.setVisible(false);
			btnEditResidenceType.setVisible(true);
			btnEditBuilding.setVisible(true);
			btnEditFloorRoom.setVisible(true);
			btnEditMoo.setVisible(true);
			btnEditStreet.setVisible(true);
			btnEditCountry.setVisible(true);
			btnEditSoiRoad.setVisible(true);
			btnEditProvince.setVisible(true);
			btnEditDistrict.setVisible(true);
			btnEditSubDistrict.setVisible(true);	
			btnEditPostalCode.setVisible(true);
			btnEditResidenceStatus.setVisible(true);
			btnEditYear.setVisible(true);
			btnEditMonth.setVisible(true);
			
			cbxTypeAddress.setEnabled(false);
			txtNo.setEnabled(false);
			txtBuilding.setEnabled(false);
			txtFloor.setEnabled(false);
			txtRoom.setEnabled(false);
			txtMoo.setEnabled(false);
			txtStreet.setEnabled(false);
			cbxCountry.setEnabled(false);
			cbxResidesceType.setEnabled(false);
			txtSoi.setEnabled(false);
			cbxProvince.setEnabled(false);
			cbxDistrict.setEnabled(false);
			cbxCommune.setEnabled(false);
			txtPostalCode.setEnabled(false);
			cbxResidesceStatus.setEnabled(false);
			txtYear.setEnabled(false);
			txtMonth.setEnabled(false);
		}
		
		HorizontalLayout noBulidingLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		noBulidingLayout.addComponent(txtNo);
		noBulidingLayout.addComponent(ComponentLayoutFactory.getLabelCaption("/"));
		noBulidingLayout.addComponent(txtBuilding);
		
		HorizontalLayout floorRoomLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		floorRoomLayout.addComponent(txtFloor);
		floorRoomLayout.addComponent(ComponentLayoutFactory.getLabelCaption("/"));
		floorRoomLayout.addComponent(txtRoom);
	    
	    int iCol = 0;
	    addressGridLayout.addComponent(lblTypeAddress, iCol++, 0);
	    addressGridLayout.addComponent(cbxTypeAddress, iCol++, 0);
	    addressGridLayout.addComponent(lblResidenceType, iCol++, 0);
	    addressGridLayout.addComponent(cbxResidesceType, iCol++, 0);
	    addressGridLayout.addComponent(btnEditResidenceType, iCol++, 0);
	    addressGridLayout.addComponent(lblBuilding, iCol++, 0);
	    addressGridLayout.addComponent(noBulidingLayout, iCol++, 0);
	    addressGridLayout.addComponent(btnEditBuilding, iCol++, 0);
	    addressGridLayout.addComponent(lblStreet, iCol++, 0);
	    addressGridLayout.addComponent(txtStreet, iCol++, 0);
	    addressGridLayout.addComponent(btnEditStreet, iCol++, 0);
	    addressGridLayout.addComponent(lblMoo, iCol++, 0);
	    addressGridLayout.addComponent(txtMoo, iCol++, 0);
	    addressGridLayout.addComponent(btnEditMoo, iCol++, 0);
	    addressGridLayout.addComponent(lblSoi, iCol++, 0);
	    addressGridLayout.addComponent(txtSoi, iCol++, 0);
	    addressGridLayout.addComponent(btnEditSoiRoad, iCol++, 0);
	    addressGridLayout.addComponent(lblFloorRoom, iCol++, 0);
	    addressGridLayout.addComponent(floorRoomLayout, iCol++, 0);
	    addressGridLayout.addComponent(btnEditFloorRoom, iCol++, 0);
	    addressGridLayout.addComponent(lblCountry, iCol++, 0);
	    addressGridLayout.addComponent(cbxCountry, iCol++, 0);
	    addressGridLayout.addComponent(btnEditCountry, iCol++, 0);
	    
	    iCol = 0;
	    addressGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("copy.from"), iCol++, 1);
	    addressGridLayout.addComponent(cbxCopyFrom, iCol++, 1);
	    addressGridLayout.addComponent(lblResidenceStatus, iCol++, 1);
	    addressGridLayout.addComponent(cbxResidesceStatus, iCol++, 1);
	    addressGridLayout.addComponent(btnEditResidenceStatus, iCol++, 1);
	    addressGridLayout.addComponent(lblMonth, iCol++, 1);
	    addressGridLayout.addComponent(txtMonth, iCol++, 1);
	    addressGridLayout.addComponent(btnEditMonth, iCol++, 1);
	    addressGridLayout.addComponent(lblYear, iCol++, 1);
	    addressGridLayout.addComponent(txtYear, iCol++, 1);
	    addressGridLayout.addComponent(btnEditYear, iCol++, 1);
	    
	    addressGridLayout.addComponent(lblProvince, iCol++, 1);
	    addressGridLayout.addComponent(cbxProvince, iCol++, 1);
	    addressGridLayout.addComponent(btnEditProvince, iCol++, 1);
	    addressGridLayout.addComponent(lblDistrict, iCol++, 1);
	    addressGridLayout.addComponent(cbxDistrict, iCol++, 1);
	    addressGridLayout.addComponent(btnEditDistrict, iCol++, 1);
	    addressGridLayout.addComponent(lblSubDistrict, iCol++, 1);
	    addressGridLayout.addComponent(cbxCommune, iCol++, 1);
	    addressGridLayout.addComponent(btnEditSubDistrict, iCol++, 1);
	    addressGridLayout.addComponent(lblPostCodes, iCol++, 1);
	    addressGridLayout.addComponent(txtPostalCode, iCol++, 1);
	    addressGridLayout.addComponent(btnEditPostalCode, iCol++, 1);
	    
	    addressGridLayout.addComponent(btnSave, 0, 2);
	    
	    addressGridLayout.setComponentAlignment(btnEditBuilding, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditMoo, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditStreet, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditCountry, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditResidenceType, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditFloorRoom, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditSoiRoad, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditProvince, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditDistrict, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditSubDistrict, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditPostalCode, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditResidenceStatus, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditYear, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditMonth, Alignment.MIDDLE_LEFT);
	    
	    FieldSet fieldSet = new FieldSet();
	    fieldSet.setLegend(I18N.message("address"));
	    fieldSet.setContent(addressGridLayout);
	    
	    Panel panel = new Panel(fieldSet);
	    panel.setStyleName(Reindeer.PANEL_LIGHT);
	   
		return panel;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			addressLayout.addComponent(createAddressGridLayout(new Address(), null));
		}
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}
}
