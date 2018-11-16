package com.nokor.efinance.ra.ui.panel.dealer.contact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.eref.ECountry;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
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
 * @author uhout.cheng
 */
public class ManagerOwnerContactAddressPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 544745658140504080L;
	
	private VerticalLayout addressLayout;
	private DealerEmployee dealerEmployee;
	private Address address;
   
	/**
	 * 
	 */
	public ManagerOwnerContactAddressPanel() {
		addressLayout = ComponentLayoutFactory.getVerticalLayout(false, false);

		setSpacing(true);
		addComponent(addressLayout);
	}
	
	/**
	 * 
	 * @param dealerEmployee
	 * @param typeContact
	 */
	public void assignValue(DealerEmployee dealerEmployee, ETypeContact typeContact) {
		this.address = null;
		this.dealerEmployee = dealerEmployee;
		if (dealerEmployee != null) {
			this.address = dealerEmployee.getAddress(); 
		}
		addressLayout.removeAllComponents();
		if (this.address != null) {
			addressLayout.addComponent(createAddressGridLayout(address, address.getType()));
		} else {
			addressLayout.addComponent(createAddressGridLayout(Address.createInstance(), ETypeAddress.MAIN));
		}
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
		Label lblFloorRoom = ComponentLayoutFactory.getLabelCaption("floor.room");
		Label lblSoi = ComponentLayoutFactory.getLabelCaption("soi");
		Label lblPostCodes = ComponentLayoutFactory.getLabelCaption("postal.code");
		Label lblProvince = ComponentLayoutFactory.getLabelCaption("province");
		Label lblDistrict = ComponentLayoutFactory.getLabelCaption("district");
		Label lblSubDistrict = ComponentLayoutFactory.getLabelCaption("sub.district");
		
		Label lblBuilding = ComponentLayoutFactory.getLabelCaption("number.building");
		Label lblMoo = ComponentLayoutFactory.getLabelCaption("moo");
		Label lblStreet = ComponentLayoutFactory.getLabelCaption("street");
		Label lblCountry = ComponentLayoutFactory.getLabelCaption("country");
		
		List<ETypeAddress> typeAddresses = new ArrayList<ETypeAddress>();
		typeAddresses.add(ETypeAddress.MAIN);
		
		ERefDataComboBox<ETypeAddress> cbxTypeAddress = new ERefDataComboBox<>(typeAddresses);
		cbxTypeAddress.setWidth("110px");
		cbxTypeAddress.setNullSelectionAllowed(false);
		if (addressType != null) {
			cbxTypeAddress.setSelectedEntity(addressType);
			cbxTypeAddress.setEnabled(false);
		} else {
			cbxTypeAddress.setSelectedEntity(ETypeAddress.MAIN);
		}

		TextField txtNo = ComponentFactory.getTextField(60, 70);
		TextField txtBuilding = ComponentFactory.getTextField(60, 70);
		TextField txtFloor = ComponentFactory.getTextField(60, 70);
		TextField txtRoom = ComponentFactory.getTextField(60, 70);
		TextField txtMoo = ComponentFactory.getTextField(60, 145);
		TextField txtStreet = ComponentFactory.getTextField(60, 145);
		ERefDataComboBox<ECountry> cbxCountry = new ERefDataComboBox<>(ECountry.values());
		cbxCountry.setWidth(155, Unit.PIXELS);
		
		TextField txtSoiRoad = ComponentFactory.getTextField(60, 145);
		TextField txtPostalCode = ComponentFactory.getTextField(60, 145);
		
		EntityComboBox<Province> cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth(155, Unit.PIXELS);
		cbxProvince.renderer();
		
		EntityComboBox<District> cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setImmediate(true);
		cbxDistrict.setWidth(145, Unit.PIXELS);
		
		EntityComboBox<Commune> cbxCommune = new EntityComboBox<>(Commune.class, "desc");
	    cbxCommune.setImmediate(true);
	    cbxCommune.setWidth(145, Unit.PIXELS);
 
	    cbxProvince.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = 4330202332326762518L;

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
			private static final long serialVersionUID = 5475450179040440142L;

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
			private static final long serialVersionUID = 6504344048111579213L;

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
	    	private static final long serialVersionUID = -2511928110440420523L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditBuilding.getIcon())) {
					txtBuilding.setEnabled(true);
					txtNo.setEnabled(true);
					btnEditBuilding.setIcon(FontAwesome.SAVE);
				} else {
					btnEditBuilding.setIcon(FontAwesome.EDIT);
					txtBuilding.setEnabled(false);
					txtNo.setEnabled(false);
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
			private static final long serialVersionUID = -8562933263638531555L;

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
			private static final long serialVersionUID = -7891312487861841670L;

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
			private static final long serialVersionUID = -9011001340687870447L;

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
			private static final long serialVersionUID = -5647264555306015662L;

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
	    
	    Button btnEditSoiRoad = new Button(FontAwesome.EDIT);
	    btnEditSoiRoad.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditSoiRoad.setVisible(false);
	    btnEditSoiRoad.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 7750420115241289443L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (FontAwesome.EDIT.equals(btnEditSoiRoad.getIcon())) {
					txtSoiRoad.setEnabled(true);
					btnEditSoiRoad.setIcon(FontAwesome.SAVE);
				} else {
					btnEditSoiRoad.setIcon(FontAwesome.EDIT);
					txtSoiRoad.setEnabled(false);
					address.setLine2(txtSoiRoad.getValue());
					ENTITY_SRV.saveOrUpdate(address);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				}
			}
		});
	    
	    Button btnEditProvince = new Button(FontAwesome.EDIT);
	    btnEditProvince.setStyleName(Reindeer.BUTTON_LINK);
	    btnEditProvince.setVisible(false);
	    btnEditProvince.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -9027938825594157931L;

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
			
			/** */
			private static final long serialVersionUID = -1487649333704762001L;

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
			
			/** */
			private static final long serialVersionUID = -7929450605050409965L;

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
			
			/** */
			private static final long serialVersionUID = 7779304968718101087L;

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
	    
	    Button btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -1288003167185685268L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				address.setHouseNo(txtNo.getValue());
				address.setBuildingName(txtBuilding.getValue());
				address.setFloor(txtFloor.getValue());
				address.setRoomNumber(txtFloor.getValue());
				address.setType(cbxTypeAddress.getSelectedEntity());
				address.setLine1(txtMoo.getValue());
				address.setLine2(txtSoiRoad.getValue());
				address.setStreet(txtStreet.getValue());
				address.setCountry(cbxCountry.getSelectedEntity());
				address.setProvince(cbxProvince.getSelectedEntity());
				address.setDistrict(cbxDistrict.getSelectedEntity());
				address.setCommune(cbxCommune.getSelectedEntity());
				address.setPostalCode(txtPostalCode.getValue());
				
				btnSave.setVisible(false);
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
				
				cbxTypeAddress.setEnabled(false);
				txtNo.setEnabled(false);
				txtBuilding.setEnabled(false);
				txtFloor.setEnabled(false);
				txtRoom.setEnabled(false);
				txtMoo.setEnabled(false);
				txtStreet.setEnabled(false);
				cbxCountry.setEnabled(false);
				txtSoiRoad.setEnabled(false);
				cbxProvince.setEnabled(false);
				cbxDistrict.setEnabled(false);
				cbxCommune.setEnabled(false);
				txtPostalCode.setEnabled(false);
	
				if (dealerEmployee == null) {
					dealerEmployee = DealerEmployee.createInstance();
				}
				dealerEmployee.setAddress(address);
				try {
					DEA_SRV.saveOrUpdateDealerEmployeeAddress(dealerEmployee);
					ComponentLayoutFactory.displaySuccessfullyMsg();
				} catch (Exception e) {
					ComponentLayoutFactory.displayErrorMsg(e.getMessage());
				}
			}
		});
		
		if (address.getId() != null) {
			txtNo.setValue(getDefaultString(address.getBuildingName()));
			txtBuilding.setValue(getDefaultString(address.getHouseNo()));
			txtFloor.setValue(getDefaultString(address.getFloor()));
			txtRoom.setValue(getDefaultString(address.getRoomNumber()));
			txtMoo.setValue(address.getLine1());
			txtStreet.setValue(address.getStreet());
			cbxCountry.setSelectedEntity(address.getCountry());
			txtSoiRoad.setValue(getDefaultString(address.getLine2()));
			cbxProvince.setSelectedEntity(address.getProvince());
			cbxDistrict.setSelectedEntity(address.getDistrict());
			cbxCommune.setSelectedEntity(address.getCommune());
			
			btnSave.setVisible(false);
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
			
			cbxTypeAddress.setEnabled(false);
			txtNo.setEnabled(false);
			txtBuilding.setEnabled(false);
			txtFloor.setEnabled(false);
			txtRoom.setEnabled(false);
			txtMoo.setEnabled(false);
			txtStreet.setEnabled(false);
			cbxCountry.setEnabled(false);
			txtSoiRoad.setEnabled(false);
			cbxProvince.setEnabled(false);
			cbxDistrict.setEnabled(false);
			cbxCommune.setEnabled(false);
			txtPostalCode.setEnabled(false);
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
	    addressGridLayout.addComponent(txtSoiRoad, iCol++, 0);
	    addressGridLayout.addComponent(btnEditSoiRoad, iCol++, 0);
	    addressGridLayout.addComponent(lblFloorRoom, iCol++, 0);
	    addressGridLayout.addComponent(floorRoomLayout, iCol++, 0);
	    addressGridLayout.addComponent(btnEditFloorRoom, iCol++, 0);
	    
	    iCol = 2;
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
	    addressGridLayout.addComponent(lblCountry, iCol++, 1);
	    addressGridLayout.addComponent(cbxCountry, iCol++, 1);
	    addressGridLayout.addComponent(btnEditCountry, iCol++, 1);
	    
	    addressGridLayout.addComponent(btnSave, 0, 2);
	    
	    addressGridLayout.setComponentAlignment(btnEditBuilding, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditMoo, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditStreet, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditCountry, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditFloorRoom, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditSoiRoad, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditProvince, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditDistrict, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditSubDistrict, Alignment.MIDDLE_LEFT);
	    addressGridLayout.setComponentAlignment(btnEditPostalCode, Alignment.MIDDLE_LEFT);
	    
	    FieldSet fieldSet = new FieldSet();
	    fieldSet.setLegend(I18N.message("address"));
	    fieldSet.setContent(addressGridLayout);
	    
	    Panel panel = new Panel(fieldSet);
	    panel.setStyleName(Reindeer.PANEL_LIGHT);
	   
		return panel;
	}

}
