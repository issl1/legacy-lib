package com.nokor.efinance.gui.ui.panel.contract.user.phone;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * User contact phone table 
 * @author uhout.cheng
 */
public class UserContactPhoneTable extends VerticalLayout implements ItemClickListener, ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 6266946435480360095L;
	
	private final static String CONTACTDETAIL = "contact.detail";
	private final static String PRIMARY = "primary";
	
	private SimpleTable<Entity> phonesSimpleTable;
	private SimpleTable<Entity> otherContactsSimpleTable;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private Item phonesSelectedItem;
	private Item otherContactSelectedItem;
	private ApplicantIndividualPanel delegate;
	private UserContactPhoneForm userContactPhoneForm;
	private Individual individual;
	private Long selectedId;
	
	private CheckBox cbPrimary;
	
	/**
	 * 
	 * @param userPanel
	 */
	public UserContactPhoneTable(ApplicantIndividualPanel delegate) {
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
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		phonesSimpleTable = getSimpleTable(getColumnDefinitions());
		otherContactsSimpleTable = getSimpleTable(getColumnDefinitionsOthersContactInfo());
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		userContactPhoneForm = new UserContactPhoneForm();
		userContactPhoneForm.getBtnBack().addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 6395257387229157270L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				individual = INDIVI_SRV.getById(Individual.class, individual.getId());
				assignValues(individual);
				delegate.getUserContactLayout().removeComponent(userContactPhoneForm);
				delegate.setVisibleContactTable(true);
			}
		});
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeUndefined();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		Panel phonesPanel = new Panel(phonesSimpleTable);
		phonesPanel.setCaption(I18N.message("phones"));
		phonesPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		Panel otherContactsPanel = new Panel(otherContactsSimpleTable);
		otherContactsPanel.setCaption(I18N.message("other.contacts"));
		otherContactsPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		setSpacing(true);
		addComponent(navigationPanel);
		addComponent(phonesPanel);
		addComponent(otherContactsPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MEntityA.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(MBaseAddress.TYPE, I18N.message("contact.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CONTACTDETAIL, I18N.message("contact.detail"), String.class, Align.LEFT, 300));
		columnDefinitions.add(new ColumnDefinition(PRIMARY, I18N.message("primary"), CheckBox.class, Align.LEFT, 75));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitionsOthersContactInfo() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MEntityA.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(MBaseAddress.TYPE, I18N.message("contact.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CONTACTDETAIL, I18N.message("contact.detail"), String.class, Align.LEFT, 300));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		this.individual = individual;
		setUserContactPhonesValueIndexedContainer(INDIVI_SRV.getIndividualContactInfos(individual.getId()));
		setUserOtherContactsValueIndexedContainer(INDIVI_SRV.getIndividualContactInfos(individual.getId()));
		userContactPhoneForm.assignValues(individual);
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setUserContactPhonesValueIndexedContainer(List<IndividualContactInfo> individualContactInfos) {
		phonesSimpleTable.removeAllItems();
		phonesSelectedItem = null;
		Container indexedContainer = phonesSimpleTable.getContainerDataSource();
		if (!individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indConInfo : individualContactInfos) {
				ContactInfo contactInfo = INDIVI_SRV.getById(ContactInfo.class, indConInfo.getContactInfo().getId());
				cbPrimary = new CheckBox();
				cbPrimary.setEnabled(false);
				if (contactInfo != null && contactInfo.getId() != null) {
					if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo()) 
							|| ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
						cbPrimary.setValue(contactInfo.isPrimary());
						Item item = indexedContainer.addItem(contactInfo.getId());
						item.getItemProperty(MEntityA.ID).setValue(contactInfo.getId());
						item.getItemProperty(MBaseAddress.TYPE).setValue(contactInfo.getTypeInfo() != null ? contactInfo.getTypeInfo().getDescLocale() : "");
						item.getItemProperty(CONTACTDETAIL).setValue(getContactDetailByType(contactInfo));
						item.getItemProperty(PRIMARY).setValue(cbPrimary);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setUserOtherContactsValueIndexedContainer(List<IndividualContactInfo> individualContactInfos) {
		otherContactsSimpleTable.removeAllItems();
		otherContactSelectedItem = null;
		Container indexedContainer = otherContactsSimpleTable.getContainerDataSource();
		if (!individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indConInfo : individualContactInfos) {
				ContactInfo contactInfo = INDIVI_SRV.getById(ContactInfo.class, indConInfo.getContactInfo().getId());
				cbPrimary = new CheckBox();
				cbPrimary.setEnabled(false);
				if (contactInfo != null && contactInfo.getId() != null) {
					if (!ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo()) 
							&& !ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
						cbPrimary.setValue(contactInfo.isPrimary());
						Item item = indexedContainer.addItem(contactInfo.getId());
						item.getItemProperty(MEntityA.ID).setValue(contactInfo.getId());
						item.getItemProperty(MBaseAddress.TYPE).setValue(contactInfo.getTypeInfo() != null ? contactInfo.getTypeInfo().getDescLocale() : "");
						item.getItemProperty(CONTACTDETAIL).setValue(contactInfo.getValue());
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param contactInfo
	 * @return
	 */
	private String getContactDetailByType(ContactInfo contactInfo) {
		StringBuffer stringBuffer = new StringBuffer();
		if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
			stringBuffer.append(contactInfo.getValue());
			stringBuffer.append(" | ");
			stringBuffer.append(contactInfo.getTypeAddress() != null ? contactInfo.getTypeAddress().getDescLocale() : "");
			stringBuffer.append(" | ");
			stringBuffer.append(contactInfo.getRemark() != null ? contactInfo.getRemark() : "");
		} else if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
			stringBuffer.append(contactInfo.getValue());
			stringBuffer.append(" | ");
			stringBuffer.append(contactInfo.getRemark() != null ? contactInfo.getRemark() : "");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			delegate.setVisibleContactTable(false);
			delegate.getUserContactLayout().addComponent(userContactPhoneForm);
			userContactPhoneForm.reset();
		} else if (event.getButton().equals(btnEdit)) {
			if (phonesSelectedItem == null && otherContactSelectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				delegate.setVisibleContactTable(false);
				delegate.getUserContactLayout().addComponent(userContactPhoneForm);
				userContactPhoneForm.removedMessagePanel();
				userContactPhoneForm.assignValuesToControls(ENTITY_SRV.getById(ContactInfo.class, selectedId));
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (phonesSelectedItem == null && otherContactSelectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {selectedId.toString()}),
						new ConfirmDialog.Listener() {

					/** */
					private static final long serialVersionUID = -1278300263633872114L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							INDIVI_SRV.deleteContactInfo(individual.getId(), selectedId);
							getNotificationDesc("item.deleted.successfully");
							assignValues(individual);
						}
					}
				});
			}
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
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		phonesSelectedItem = null;
		otherContactSelectedItem = null;
		if (phonesSimpleTable == event.getComponent()) {
			phonesSelectedItem = event.getItem();
		} else {
			otherContactSelectedItem = event.getItem();
		}
		if (phonesSelectedItem != null) {
			selectedId = (Long) phonesSelectedItem.getItemProperty(MEntityA.ID).getValue();
			setUserOtherContactsValueIndexedContainer(INDIVI_SRV.getIndividualContactInfos(individual.getId()));
		} else if (otherContactSelectedItem != null) {
			selectedId = (Long) otherContactSelectedItem.getItemProperty(MEntityA.ID).getValue();
			setUserContactPhonesValueIndexedContainer(INDIVI_SRV.getIndividualContactInfos(individual.getId()));
		}
		if (event.isDoubleClick()) {
			delegate.setVisibleContactTable(false);
			delegate.getUserContactLayout().addComponent(userContactPhoneForm);
			userContactPhoneForm.assignValuesToControls(ENTITY_SRV.getById(ContactInfo.class, selectedId));
			userContactPhoneForm.removedMessagePanel();
		}
	}

}
