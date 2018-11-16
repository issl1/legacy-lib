package com.nokor.efinance.core.applicant.panel.address;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Addresses panel
 * @author sok.vina
 */
public class AddressesPanel extends AbstractTabPanel implements AddClickListener, FMEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Address> pagedTable;
	private IndividualAddress boApplicantAddress;
	private AddressesPanel addressesPanel;
	/**
	 */
	public AddressesPanel() {
		super();
		setSizeFull();	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		addComponent(navigationPanel, 0);
		setAddressesPanel(this);
	}
	
	/**
	 * @return the addressesPanel
	 */
	AddressesPanel getAddressesPanel() {
		return addressesPanel;
	}

	/**
	 * @param addressesPanel the addressesPanel to set
	 */
	void setAddressesPanel(AddressesPanel addressesPanel) {
		this.addressesPanel = addressesPanel;
	}

	
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Address>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Long itemId = (Long) event.getItemId();
					AddressPopupPanel addressPanel = new AddressPopupPanel(boApplicantAddress.getIndividual(),I18N.message("edit.address"));
					addressPanel.resetAddressPopupPanel();
					Address address = entityService.getById(Address.class, itemId.intValue());
					addressPanel.assignValues(address, boApplicantAddress);
					addressPanel.setAddressPanel(getAddressesPanel());
				}
			}
		});
				
		contentLayout.addComponent(pagedTable);
		contentLayout.setSpacing(true);
		contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
				
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<IndividualAddress> individualAddresses) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (individualAddresses != null && !individualAddresses.isEmpty()) {	
			for (IndividualAddress addressIndividual : individualAddresses) {
				Address address = addressIndividual.getAddress();
					Item item = indexedContainer.addItem(address.getId());
					item.getItemProperty(ID).setValue(address.getId());
					item.getItemProperty(HOUSE_NO).setValue((address.getHouseNo() != null ? address.getHouseNo() : ""));
					item.getItemProperty(STREET).setValue((address.getStreet() != null ? address.getStreet() : ""));
					item.getItemProperty(PROVINCE).setValue((address.getProvince() != null ? address.getProvince().getDescEn() : ""));
					item.getItemProperty(DISTRICT).setValue((address.getDistrict() != null ? address.getDistrict().getDescEn() : ""));
					item.getItemProperty(COMMUNE).setValue((address.getCommune() != null ? address.getCommune().getDescEn() : ""));
					item.getItemProperty(VILLAGE).setValue((address.getVillage() != null ? address.getVillage().getDescEn() : ""));
					item.getItemProperty(STATUS_RECORD).setValue((address.getStatusRecord().equals(EStatusRecord.INACT) ? "inactive" : "active"));
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(HOUSE_NO, I18N.message("house.no"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STREET, I18N.message("streed"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(VILLAGE, I18N.message("village"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(COMMUNE, I18N.message("commune"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DISTRICT, I18N.message("district"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PROVINCE, I18N.message("provice"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(STATUS_RECORD, I18N.message("status"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * @param boApplicantAddresses
	 */
	public void assignValues(List<IndividualAddress> boApplicantAddresses) {
		if (boApplicantAddresses.size() > 0) {
			boApplicantAddress = boApplicantAddresses.get(0);
			pagedTable.setContainerDataSource(getIndexedContainer(boApplicantAddresses));
		} else {
			pagedTable.removeAllItems();
		}
	}

	@Override
	public void addButtonClick(ClickEvent event) {
		AddressPopupPanel AddressPanel = new AddressPopupPanel(boApplicantAddress.getIndividual(), I18N.message("address"));
		AddressPanel.setAddressPanel(getAddressesPanel());
	}


}
