package com.nokor.efinance.gui.ui.panel.collection.field.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author youhort.ly
 *
 */
public class AreasSelectPanel extends Window implements FMEntityField, FinServicesHelper, ClickListener {

	private static final long serialVersionUID = 5489374367808132695L;
	
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict;
	
	protected Button btnSearch;
	protected Button btnReset;
	
	private SimpleTable<Area> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Contract contract;
	private EColType colType;
	private Listener selectListener = null;
	
	private Area selectArea;
	
	public interface Listener extends Serializable {
        void onClose(AreasSelectPanel dialog);
    }

    /**
     * Show a modal ConfirmDialog in a window.
     * @param contractUserSimulInbox
     * @param selectListener
     */
    public void show(final Contract contract, final EColType colType, final Listener selectListener) {   
    	this.contract = contract;
    	this.colType = colType;
    	this.selectListener = selectListener;
    	initSearchCriteria(contract);
    	UI.getCurrent().addWindow(this);
    }
    
	/**
	 * 
	 */
	public AreasSelectPanel() {
		super.center();
		setModal(true);
		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setMargin(true);
		containLayout.setSpacing(true);
	
		simpleTable = new SimpleTable<>(createColumnDefinition());
		simpleTable.setCaption(null);		
		containLayout.addComponent(createSearchForm());
		containLayout.addComponent(simpleTable);
		setContent(containLayout);
	}
	
	private void initSearchCriteria(Contract contract) {
		List<Address> addresses = contract.getApplicant().getAddresses();
		Address selectAddress = null;
		for (Address address : addresses) {
			if (address.getStatusRecord() == EStatusRecord.ACTIV) {
				selectAddress = address;
				break;
			}
		}
		if (selectAddress != null) {
			cbxProvince.setSelectedEntity(selectAddress.getProvince());
			cbxDistrict.setSelectedEntity(selectAddress.getDistrict());
			cbxSubDistrict.setSelectedEntity(selectAddress.getCommune());
			assignValues();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	private void reset() {
		cbxProvince.setSelectedEntity(null);
	}

	/**
	 * 
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox() {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(190, Unit.PIXELS);
		comboBox.setImmediate(true);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	private Component createSearchForm() {		
		Panel searchPanel = new Panel(I18N.message("search"));		
		cbxProvince = getEntityRefComboBox();
		cbxProvince.setRestrictions(new BaseRestrictions<>(Province.class));
		cbxProvince.renderer();
		cbxDistrict = getEntityRefComboBox();
		cbxSubDistrict = getEntityRefComboBox();
		
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
					cbxDistrict.setRestrictions(restrictions);
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
					cbxSubDistrict.setRestrictions(restrictions);
					cbxSubDistrict.renderer();
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setStyleName("panel-search-center");
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);

		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		int iCol = 0;
		int iRow = 0;
		
		Label lblProvince = ComponentFactory.getLabel("province");
		Label lblDistrict = ComponentFactory.getLabel("district");
		Label lblSubDistrict = ComponentFactory.getLabel("commune");
		
		gridLayout.addComponent(lblProvince, iCol++, iRow);
		gridLayout.addComponent(cbxProvince, iCol++, iRow);
		gridLayout.addComponent(lblDistrict, iCol++, iRow);
		gridLayout.addComponent(cbxDistrict, iCol++, iRow);
		gridLayout.addComponent(lblSubDistrict, iCol++, iRow);
		gridLayout.addComponent(cbxSubDistrict, iCol++, iRow);
		
		gridLayout.setComponentAlignment(lblProvince, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblDistrict, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblSubDistrict, Alignment.MIDDLE_CENTER);
		
		VerticalLayout searchLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		searchLayout.addComponent(gridLayout);
		searchLayout.addComponent(buttonsLayout);
		
		searchPanel.setContent(searchLayout);
		
		return searchPanel;
	}
	
	/**
	 * 
	 * @param entityId
	 * @return
	 */
	private BaseRestrictions<Area> getRestrictions() {
		BaseRestrictions<Area> restrictions = new BaseRestrictions<>(Area.class);
		
		restrictions.addCriterion(Restrictions.eq("colType", colType));
		
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("province", cbxProvince.getSelectedEntity()));
		} 		
		if (cbxDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("district", cbxDistrict.getSelectedEntity()));
		}		
		if (cbxSubDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("commune", cbxSubDistrict.getSelectedEntity()));
		}			
		return restrictions;
	}
	
	/**
	 * @param dealers
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Area> areas) {
		Container indexedContainer = simpleTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (Area area : areas) {
			Item item = indexedContainer.addItem(area.getId());
			item.getItemProperty("select").setValue(new SelectButton(area));
			item.getItemProperty(ID).setValue(area.getId());
			item.getItemProperty(CODE).setValue(area.getCode());
			item.getItemProperty(PROVINCE).setValue(area.getProvince() != null ? area.getProvince().getDescEn() : "");
			item.getItemProperty(DISTRICT).setValue(area.getDistrict() != null ? area.getDistrict().getDescEn() : "");
			item.getItemProperty(COMMUNE).setValue(area.getCommune() != null ? area.getCommune().getDescEn() : "");
			item.getItemProperty("detail").setValue(area.getDescEn());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition("select", I18N.message("select"), Button.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(PROVINCE, I18N.message("province"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DISTRICT, I18N.message("district"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMMUNE, I18N.message("commune"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("detail", I18N.message("detail"), String.class, Align.LEFT, 300));	
		return columnDefinitions;
	}
	
	/**
	 */
	private void assignValues() {
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			assignValues();
		} else if (event.getButton() == btnReset) {
			reset();
		}
	}
	
	/**
	 * @return
	 */
	public Area getSelectArea() {
		return selectArea;
	}
	
	/**
	 * @author buntha.chea
	 */
	private class SelectButton extends NativeButton {		
		/**
		 */
		private static final long serialVersionUID = 1585961357565634055L;
		public SelectButton(final Area area) {
			super("");
			//setStyleName(Reindeer.BUTTON_LINK);
			setCaption(I18N.message("select"));
			setStyleName("btn btn-success button-small");
			addClickListener(new ClickListener() {				
				private static final long serialVersionUID = 494854605358694744L;
				@Override
				public void buttonClick(ClickEvent event) {
					selectArea = area;
					if (selectListener != null) {
	                    selectListener.onClose(AreasSelectPanel.this);
	                }
	                UI.getCurrent().removeWindow(AreasSelectPanel.this);
				}
			});
		}
	}
}
