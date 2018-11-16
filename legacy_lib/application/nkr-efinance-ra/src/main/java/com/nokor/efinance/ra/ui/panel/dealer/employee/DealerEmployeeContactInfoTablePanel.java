package com.nokor.efinance.ra.ui.panel.dealer.employee;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.efinance.ra.ui.panel.dealer.employee.DealerEmployeeContactInfoFormPanel.BackListener;
import com.nokor.efinance.ra.ui.panel.dealer.employee.DealerEmployeeContactInfoFormPanel.SaveListener;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Dealer Employee Contact Info Table Panel
 * @author bunlong.taing
 */
public class DealerEmployeeContactInfoTablePanel extends AbstractTabPanel implements ClickListener, BackListener, SaveListener {
	/** */
	private static final long serialVersionUID = -5181815615815475175L;
	
	private final static String CONTACTDETAIL = "contact.detail";
	private final static String PRIMARY = "primary";
	
	private SimpleTable<Entity> contactTable;
	
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	
	private VerticalLayout contactTableLayout;
	private VerticalLayout contentLayout;
	private DealerEmployeeContactInfoFormPanel contactFormPanel;
	
	private List<DealerEmployeeContactInfo> dealerEmployeeContactInfos;
	private DealerEmployee dealerEmployee;
	private boolean isAdd;
	private Integer selectedIndex;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#init()
	 */
	@Override
	public void init() {
		super.init();
		setMargin(false);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		contactFormPanel = new DealerEmployeeContactInfoFormPanel();
		contactFormPanel.setBackListener(this);
		contactFormPanel.setSaveListener(this);
		
		btnAdd = new NativeButton(I18N.message("add"));
		btnEdit = new NativeButton(I18N.message("edit"));
		btnDelete = new NativeButton(I18N.message("delete"));
		
		btnAdd.addClickListener(this);
		btnEdit.addClickListener(this);
		btnDelete.addClickListener(this);
		
		btnAdd.setIcon(FontAwesome.PLUS);
		btnEdit.setIcon(FontAwesome.EDIT);
		btnDelete.setIcon(FontAwesome.TRASH_O);
		
		contactTable = getSimpleTable(createColumnDefinitions());
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeUndefined();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		contactTableLayout = new VerticalLayout();
		contactTableLayout.setSpacing(true);
		contactTableLayout.addComponent(navigationPanel);
		contactTableLayout.addComponent(contactTable);
		
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(contactTableLayout);
		
		return contentLayout;
	}
	
	/**
	 * Get Simple Table
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 7644319947000216161L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedIndex = (Integer) event.getItemId();
				if (event.isDoubleClick()) {
					isAdd = false;
					edit();
				}
			}
		});
		return simpleTable;
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MBaseAddress.TYPE, I18N.message("contact.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CONTACTDETAIL, I18N.message("contact.detail"), String.class, Align.LEFT, 300));
		columnDefinitions.add(new ColumnDefinition(PRIMARY, I18N.message("primary"), CheckBox.class, Align.LEFT, 75));
		return columnDefinitions;
	}
	
	/**
	 * @param contactInfo
	 * @return
	 */
	private String getContactDetailByType(DealerEmployeeContactInfo contactInfo) {
		StringBuffer stringBuffer = new StringBuffer();
		if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
			stringBuffer.append(contactInfo.getValue());
			stringBuffer.append(" | ");
			stringBuffer.append(contactInfo.getTypeAddress() != null ? contactInfo.getTypeAddress().getDescLocale() : "");
		} else if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
			stringBuffer.append(contactInfo.getValue());
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Set Table Container Data Source
	 */
	@SuppressWarnings("unchecked")
	private void setTableContainerDataSource() {
		selectedIndex = null;
		contactTable.removeAllItems();
		int index = 0;
		for (DealerEmployeeContactInfo contactInfo : dealerEmployeeContactInfos) {
			Item item = contactTable.addItem(index++);
			CheckBox cb = new CheckBox();
			cb.setValue(contactInfo.isPrimary());
			cb.setEnabled(false);
			item.getItemProperty(MBaseAddress.TYPE).setValue(contactInfo.getTypeInfo() != null ? contactInfo.getTypeInfo().getDescLocale() : "");
			item.getItemProperty(CONTACTDETAIL).setValue(getContactDetailByType(contactInfo));
			item.getItemProperty(PRIMARY).setValue(cb);
		}
	}
	
	/**
	 * Assign Values
	 * @param dealerEmployeeContactInfo
	 */
	public void assignValues(DealerEmployee dealerEmployee) {
		this.dealerEmployee = dealerEmployee;
		List<DealerEmployeeContactInfo> dealerEmployeeContactInfos = dealerEmployee.getDealerEmployeeContactInfos();
		if (dealerEmployeeContactInfos == null) {
			dealerEmployeeContactInfos = new ArrayList<DealerEmployeeContactInfo>();
			dealerEmployee.setDealerEmployeeContactInfos(dealerEmployeeContactInfos);
		}
		this.dealerEmployeeContactInfos = dealerEmployeeContactInfos;
		setTableContainerDataSource();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			isAdd = true;
			contactFormPanel.reset();
			contactFormPanel.assignValues(new DealerEmployeeContactInfo());
			contentLayout.removeComponent(contactTableLayout);
			contentLayout.addComponent(contactFormPanel);
		} else if (event.getButton() == btnEdit) {
			isAdd = false;
			edit();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * Edit
	 */
	private void edit() {
		if (selectedIndex == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			contactFormPanel.reset();
			contactFormPanel.assignValues(dealerEmployeeContactInfos.get(selectedIndex));
			contentLayout.removeComponent(contactTableLayout);
			contentLayout.addComponent(contactFormPanel);
		}
	}
	
	/**
	 * Delete
	 */
	private void delete() {
		if (selectedIndex == null) {
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
						DealerEmployeeContactInfo contactInfo = dealerEmployeeContactInfos.get(selectedIndex);
						if (contactInfo.getId() == null || contactInfo.getId() <= 0) {
							dealerEmployeeContactInfos.remove(selectedIndex.intValue());
						} else {
							ENTITY_SRV.delete(contactInfo);
							dealerEmployeeContactInfos.remove(selectedIndex.intValue());
						}
						Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
						notification.setDescription(I18N.message("item.deleted.successfully", I18N.message("contact.info")));
						notification.setDelayMsec(3000);
						notification.show(Page.getCurrent());
						setTableContainerDataSource();
					}
				}
			});
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		dealerEmployee = null;
		selectedIndex = null;
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.dealer.employee.DealerEmployeeContactInfoFormPanel.BackListener#onBack()
	 */
	@Override
	public void onBack() {
		contentLayout.removeComponent(contactFormPanel);
		contentLayout.addComponent(contactTableLayout);
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.dealer.employee.DealerEmployeeContactInfoFormPanel.SaveListener#onSaveSuccess(com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo)
	 */
	@Override
	public void onSaveSuccess(DealerEmployeeContactInfo contactInfo) {
		if (isAdd) {
			dealerEmployeeContactInfos.add(contactInfo);
		}
		if (dealerEmployee != null && dealerEmployee.getId() != null) {
			contactInfo.setDealerEmployee(dealerEmployee);
			ENTITY_SRV.saveOrUpdate(contactInfo);
		}
		setTableContainerDataSource();
		onBack();
	}

}
