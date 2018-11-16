package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;



/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AddressTablePanel extends AbstractControlPanel implements FinServicesHelper, MBaseAddress {

	/** */
	private static final long serialVersionUID = 1719066647835004646L;

	private final static String ADDRESS = "address";
	private final static String ID = "id";
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Item selectedItem;
	
	private Button btnAdd;
	
	private Individual individual;
	
	/**
	 * 
	 * @param caption
	 * @param colContactAddressPanel
	 */
	public AddressTablePanel(String caption, ColContactAddressPanel colContactAddressPanel) {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(new ItemClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 426962237607915178L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					Long addressId = (Long) selectedItem.getItemProperty(ID).getValue();
					Address address = ENTITY_SRV.getById(Address.class, addressId);
					
					ColPhoneAddressPopupPanel.show(address, individual, new ColPhoneAddressPopupPanel.Listener() {
						
						private static final long serialVersionUID = 1261478473862844711L;
						
						/**
						 * @see com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address.ColPhoneAddressPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address.ColPhoneAddressPopupPanel)
						 */
						@Override
						public void onClose(ColPhoneAddressPopupPanel dialog) {
							colContactAddressPanel.refresh();
						}
					});
				}
				
			}
		});
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 5402413804350533034L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				ColPhoneAddressPopupPanel.show(null, individual, new ColPhoneAddressPopupPanel.Listener() {
					
					/** */
					private static final long serialVersionUID = 4208099997864727382L;

					/**
					 * @see com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address.ColPhoneAddressPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address.ColPhoneAddressPopupPanel)
					 */
					@Override
					public void onClose(ColPhoneAddressPopupPanel dialog) {
						colContactAddressPanel.refresh();
					}
				});
			}
		});
		
		VerticalLayout layout = new VerticalLayout(btnAdd, simpleTable);
		layout.setComponentAlignment(btnAdd, Alignment.TOP_RIGHT);
		
		Panel tableCustomerPanel = new Panel(layout);
		
		simpleTable.setCaption(I18N.message(caption));
		tableCustomerPanel.setStyleName(Reindeer.PANEL_LIGHT);

		addComponent(tableCustomerPanel);
	}

	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ADDRESS, I18N.message("address"), String.class, Align.LEFT, 500));
		return columnDefinitions;
		
	}
	
	/**
	 * 
	 * @param addresses
	 * @param individual
	 */
	public void assignValues(List<Address> addresses, Individual individual) {	
		this.individual = individual;
		simpleTable.setContainerDataSource(getAddressesIndexedContainer(addresses));
	}
	
	/**
	 * 
	 * @param addresses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getAddressesIndexedContainer(List<Address> addresses) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (addresses != null && !addresses.isEmpty()) {
			for (int i = 0; i < addresses.size(); i++) {
				Item item = indexedContainer.addItem(i);
				item.getItemProperty(ID).setValue(addresses.get(i).getId());
				item.getItemProperty(TYPE).setValue(addresses.get(i).getType() != null ? 
						addresses.get(i).getType().getDescLocale() : StringUtils.EMPTY);
				item.getItemProperty(ADDRESS).setValue(ADDRESS_SRV.getDetailAddress(addresses.get(i)));
			}
		}
		return indexedContainer;
	}

}
