package com.nokor.efinance.ra.ui.panel.dealer.contact;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.efinance.ra.ui.panel.dealer.contact.DealerEmployeeFormPanel.BackListener;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class DelaerEmployeeContactInfoPanel extends AbstractTabPanel implements ClickListener, BackListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7971970672964801439L;
	
	private SimplePagedTable<DealerEmployee> pageTable;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	
	private Dealer dealer;
	private ETypeContact typeContact;
	
	private List<ColumnDefinition> columnDefinitions;
	private VerticalLayout mainLayout;
	private VerticalLayout contactTableLayout;
	private DealerEmployeeFormPanel dealerEmployeeFormPanel;
	
	private Item selectedItem;

	@Override
	protected Component createForm() {
		
		dealerEmployeeFormPanel = new DealerEmployeeFormPanel();
		dealerEmployeeFormPanel.setBackListener(this);
		
		pageTable = new SimplePagedTable<>(createColumnDefinitions());
		pageTable.addItemClickListener(new ItemClickListener() {
		
			private static final long serialVersionUID = -6741010601233647427L;

			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					Long dealerEmployeeId = (Long) selectedItem.getItemProperty("id").getValue();
					dealerEmployeeFormPanel.reset();
					mainLayout.removeComponent(contactTableLayout);
					mainLayout.addComponent(dealerEmployeeFormPanel);
					dealerEmployeeFormPanel.assignValue(dealer, ENTITY_SRV.getById(DealerEmployee.class, dealerEmployeeId), typeContact);
				}
			}
		});
		btnAdd = new NativeButton(I18N.message("add"), this);
		btnEdit = new NativeButton(I18N.message("edit"), this);
		btnDelete = new NativeButton(I18N.message("delete"), this);
		
		btnAdd.setIcon(FontAwesome.PLUS);
		btnEdit.setIcon(FontAwesome.EDIT);
		btnDelete.setIcon(FontAwesome.TRASH_O);

		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		contactTableLayout = new VerticalLayout();
		contactTableLayout.setSpacing(true);
		contactTableLayout.addComponent(navigationPanel);
		contactTableLayout.addComponent(pageTable);
		contactTableLayout.addComponent(pageTable.createControls());
		pageTable.setPageLength(5);
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(contactTableLayout);
		
		return mainLayout;
	}
	
	public void reset() {
		pageTable.removeAllItems();
	}
	
	/**
	 * 
	 * @param dealer
	 * @param typeContact
	 */
	public void assignValue(Dealer dealer, ETypeContact typeContact) {
		List<DealerEmployee> dealerEmployees = new ArrayList<DealerEmployee>();
		this.dealer = dealer;
		if (dealer.getId() != null) {
			dealerEmployees = getDealerEmployeeByType(typeContact);
		} 
		this.typeContact = typeContact;
		pageTable.setContainerDataSource(getIndexedContainer(dealerEmployees));
		dealerEmployeeFormPanel.reset();
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("firstname", I18N.message("firstname"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("lastname", I18N.message("lastname"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param schedules
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<DealerEmployee> dealerEmployees) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (DealerEmployee dealerEmployee : dealerEmployees) {
			Item item = indexedContainer.addItem(dealerEmployee.getId());
			item.getItemProperty("id").setValue(dealerEmployee.getId());
			item.getItemProperty("firstname").setValue(dealerEmployee.getFirstName());
			item.getItemProperty("lastname").setValue(dealerEmployee.getLastName());
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param typeContact
	 * @return
	 */
	private List<DealerEmployee> getDealerEmployeeByType(ETypeContact typeContact) {
		BaseRestrictions<DealerEmployee> restrictions = new BaseRestrictions<>(DealerEmployee.class);
		restrictions.addCriterion(Restrictions.eq("company", dealer));
		restrictions.addCriterion(Restrictions.eq("typeContact", typeContact));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Edit
	 */
	private void edit() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			Long dealerEmployeeId = (Long) selectedItem.getItemProperty("id").getValue();
			dealerEmployeeFormPanel.reset();
			mainLayout.removeComponent(contactTableLayout);
			mainLayout.addComponent(dealerEmployeeFormPanel);
			dealerEmployeeFormPanel.assignValue(dealer, ENTITY_SRV.getById(DealerEmployee.class, dealerEmployeeId), typeContact);
		}
	}
	
	/**
	 * Delete
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -2479159124880797413L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
							Long dealerEmployeeId = (Long) selectedItem.getItemProperty("id").getValue();
							DealerEmployee dealerEmployee = ENTITY_SRV.getById(DealerEmployee.class, dealerEmployeeId);
							for (DealerEmployeeContactInfo dealerEmployeeContactInfo : dealerEmployee.getDealerEmployeeContactInfos()) {
								ENTITY_SRV.delete(dealerEmployeeContactInfo);
							}	
							ENTITY_SRV.delete(dealerEmployee);
							Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
							notification.setDescription(I18N.message("item.deleted.successfully", I18N.message("dealer.employee")));
							notification.show(Page.getCurrent());
							selectedItem = null;
							assignValue(dealer, typeContact);
						} catch (Exception e) {
							Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
							logger.error(e.getMessage());
							e.printStackTrace();
						}
						
					}
				}
			});
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			dealerEmployeeFormPanel.reset();
			mainLayout.removeComponent(contactTableLayout);
			mainLayout.addComponent(dealerEmployeeFormPanel);
			dealerEmployeeFormPanel.assignValue(dealer, null, typeContact);
		} else if (event.getButton() == btnEdit) {
			edit();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}

	@Override
	public void onBack() {
		selectedItem = null;
		mainLayout.removeComponent(dealerEmployeeFormPanel);
		mainLayout.addComponent(contactTableLayout);
		assignValue(dealer, typeContact);
	}	
}
