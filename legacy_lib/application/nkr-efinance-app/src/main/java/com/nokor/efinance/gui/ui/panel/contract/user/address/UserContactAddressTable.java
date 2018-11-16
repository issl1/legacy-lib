package com.nokor.efinance.gui.ui.panel.contract.user.address;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * User contact address table 
 * @author uhout.cheng
 */
public class UserContactAddressTable extends VerticalLayout implements ItemClickListener, SelectedItem, ClickListener, FinServicesHelper, MEntityA, MBaseAddress {

	/** */
	private static final long serialVersionUID = 310400265192799426L;

	private final static String ADDRESS = "address";
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private Item selectedItem;
	private ApplicantIndividualPanel delegate;
	private UserContactAddressForm userContactAddressForm;
	private Individual individual;
	private Long selectedId;
	
	/**
	 * 
	 * @param userPanel
	 */
	public UserContactAddressTable(ApplicantIndividualPanel delegate) {
		this.delegate = delegate;
		init();
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(this);
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		userContactAddressForm = new UserContactAddressForm();
		userContactAddressForm.getBtnBack().addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 6257275540975996731L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				assignValues(individual);
				delegate.getUserContactLayout().removeComponent(userContactAddressForm);
				delegate.setVisibleContactTable(true);
			}
		});
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeUndefined();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setCaption(I18N.message("addresses"));
		tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		setSpacing(true);
		addComponent(navigationPanel);
		addComponent(tablePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(RESIDENCESTATUS, I18N.message("residence.status"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(RESIDENCETYPE, I18N.message("residence.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ADDRESS, I18N.message("address"), String.class, Align.LEFT, 470));
		columnDefinitions.add(new ColumnDefinition(TIMEATADDRESSINYEAR + TIMEATADDRESSINMONTH, I18N.message("living.period"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		this.individual = individual;
		simpleTable.setContainerDataSource(getUserContactAddressIndexedContainer(INDIVI_SRV.getIndividualAddresses(individual.getId())));
		userContactAddressForm.assignValues(this.individual);
	}
	
	/**
	 * 
	 * @param individualAddresses
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getUserContactAddressIndexedContainer(List<IndividualAddress> individualAddresses) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			for (IndividualAddress indAdrInfo : individualAddresses) {
				Address address = indAdrInfo.getAddress();
				if (address != null && address.getId() != null) {
					Item item = indexedContainer.addItem(address.getId());
					item.getItemProperty(ID).setValue(address.getId());
					item.getItemProperty(TYPE).setValue(address.getType() != null ? address.getType().getDescLocale() : "");
					item.getItemProperty(RESIDENCESTATUS).setValue(address.getResidenceStatus() != null ? address.getResidenceStatus().getDescLocale() : "");
					item.getItemProperty(RESIDENCETYPE).setValue(address.getResidenceType() != null ? address.getResidenceType().getDescLocale() : "");
					item.getItemProperty(ADDRESS).setValue(ADDRESS_SRV.getDetailAddress(address));
					item.getItemProperty(TIMEATADDRESSINYEAR + TIMEATADDRESSINMONTH).setValue(getLivingPeriod(address));
				}
			}
		}
		return indexedContainer;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			delegate.setVisibleContactTable(false);
			delegate.getUserContactLayout().addComponent(userContactAddressForm);
			userContactAddressForm.assignValues(individual);
			userContactAddressForm.reset();
			userContactAddressForm.assignValuesToControls(null);
		} else if (event.getButton().equals(btnEdit)) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				delegate.setVisibleContactTable(false);
				delegate.getUserContactLayout().addComponent(userContactAddressForm);
				userContactAddressForm.removedMessagePanel();
				userContactAddressForm.assignValuesToControls(ENTITY_SRV.getById(Address.class, selectedId));
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {selectedId.toString()}),
						new ConfirmDialog.Listener() {

					/** */
					private static final long serialVersionUID = 5308254087977800337L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							INDIVI_SRV.deleteContactAddress(individual.getId(), selectedId);
							getNotificationDesc("item.deleted.successfully");
							assignValues(individual);
							selectedItem = null;
						}
					}
				});
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (this.selectedItem != null) {
			selectedId = (Long) selectedItem.getItemProperty(MEntityA.ID).getValue();
		}	
		if (event.isDoubleClick()) {
			delegate.setVisibleContactTable(false);
			delegate.getUserContactLayout().addComponent(userContactAddressForm);
			userContactAddressForm.assignValuesToControls(ENTITY_SRV.getById(Address.class, selectedId));
			userContactAddressForm.removedMessagePanel();
		}
	}
	
	/**
	 * 
	 * @param description
	 * @return
	 */
	private Notification getNotificationDesc(String description) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ selectedId.toString() }));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		return notification;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private String getLivingPeriod(Address address) {
		String SPACE = StringUtils.SPACE;
		StringBuffer stringBuffer = new StringBuffer(); 
		stringBuffer.append(String.valueOf(MyNumberUtils.getInteger(address.getTimeAtAddressInYear())));
		stringBuffer.append(SPACE);
		stringBuffer.append(I18N.message("year"));
		stringBuffer.append(SPACE);
		stringBuffer.append(String.valueOf(MyNumberUtils.getInteger(address.getTimeAtAddressInMonth())));
		stringBuffer.append(SPACE);
		stringBuffer.append(I18N.message("month"));
		return stringBuffer.toString();
	}

}
