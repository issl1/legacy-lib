package com.nokor.efinance.ra.ui.panel.dealer.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.ra.ui.panel.dealer.contact.DealerEmployeeContactFormPanel.BackContactFormListener;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class DealerEmployeeFormPanel extends AbstractTabPanel implements ClickListener, ItemClickListener, FinServicesHelper, BackContactFormListener {

	private static final long serialVersionUID = -7060526331365945498L;
	
	private final static String CONTACTDETAIL = "contact.detail";
	private final static String PRIMARY = "primary";
	
	private TextField txtFirstName;
	private TextField txtLastName;
	private Button btnBack;
	private Button btnSave;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private BackListener backListener;
	private DealerEmployee dealerEmployee;
	private Dealer dealer;
	private VerticalLayout messagePanel;
	private SimpleTable<Entity> phoneTable;
	private SimpleTable<Entity> otherTable;
	private Item phoneSelectedItem;
	private Item otherSelectedItem;
	private Long selectedId;
	
	private ManagerOwnerContactAddressPanel mgtOwnerAddressPanel;
	
	private DealerEmployeeContactFormPanel dealerEmployeeContactFormPanel;
	private VerticalLayout contractInfoLayout;
	private NavigationPanel contactInfoNavigationPanel;
	

	@Override
	protected Component createForm() {
		setMargin(false);
		
//		Panel phoneTablePanel = new Panel(I18N.message());
		phoneTable = getSimpleTable(getColumnDefinitions(), "phone");
//		phoneTablePanel.setContent(phoneTable);
		
//		Panel otherTablePanel = new Panel(I18N.message("other"));
		otherTable = getSimpleTable(getColumnDefinitionsOthersContactInfo(), "other");
//		otherTablePanel.setContent(otherTable);
		
		txtFirstName = ComponentFactory.getTextField("firstname", true, 50, 200);
		txtLastName = ComponentFactory.getTextField("lastname", true, 50, 200);
		btnBack = new NativeButton(I18N.message("back"), this);
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		
		btnAdd = new NativeButton(I18N.message("add"), this);
		btnAdd.setIcon(FontAwesome.PLUS);
		btnEdit = new NativeButton(I18N.message("edit"), this);
		btnEdit.setIcon(FontAwesome.EDIT);
		btnDelete = new NativeButton(I18N.message("delete"), this);
		btnDelete.setIcon(FontAwesome.TRASH_O);
	
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtFirstName);
		formLayout.addComponent(txtLastName);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		
		contactInfoNavigationPanel = new NavigationPanel();
		contactInfoNavigationPanel.setSizeUndefined();
		contactInfoNavigationPanel.addButton(btnAdd);
		contactInfoNavigationPanel.addButton(btnEdit);
		contactInfoNavigationPanel.addButton(btnDelete);
		
		contractInfoLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		contractInfoLayout.addComponent(contactInfoNavigationPanel);
		contractInfoLayout.addComponent(phoneTable);
		contractInfoLayout.addComponent(otherTable);
	
		mgtOwnerAddressPanel = new ManagerOwnerContactAddressPanel();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(formLayout);
		mainLayout.addComponent(mgtOwnerAddressPanel);
		mainLayout.addComponent(contractInfoLayout);
		
		dealerEmployeeContactFormPanel = new DealerEmployeeContactFormPanel();
		dealerEmployeeContactFormPanel.setBackListener(this);
		
		return mainLayout;
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
	 * @param dealerEmployeeContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setUserContactPhonesValueIndexedContainer(List<DealerEmployeeContactInfo> dealerEmployeeContactInfos) {
		phoneTable.removeAllItems();
		phoneSelectedItem = null;
		Container indexedContainer = phoneTable.getContainerDataSource();
		if (dealerEmployeeContactInfos != null && !dealerEmployeeContactInfos.isEmpty()) {
			for (DealerEmployeeContactInfo dealerEmployeeContactInfo : dealerEmployeeContactInfos) {
				CheckBox cbPrimary = new CheckBox();
			    cbPrimary.setEnabled(false);
			    cbPrimary.setValue(dealerEmployeeContactInfo.isPrimary());
				if (ETypeContactInfo.LANDLINE.equals(dealerEmployeeContactInfo.getTypeInfo()) 
						|| ETypeContactInfo.MOBILE.equals(dealerEmployeeContactInfo.getTypeInfo())) {
					Item item = indexedContainer.addItem(dealerEmployeeContactInfo.getId());
					item.getItemProperty(MEntityA.ID).setValue(dealerEmployeeContactInfo.getId());
					item.getItemProperty(MBaseAddress.TYPE).setValue(dealerEmployeeContactInfo.getTypeInfo() != null ? dealerEmployeeContactInfo.getTypeInfo().getDescEn() : "");
					item.getItemProperty(CONTACTDETAIL).setValue(getContactDetailByType(dealerEmployeeContactInfo));
					item.getItemProperty(PRIMARY).setValue(cbPrimary);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setUserOtherContactsValueIndexedContainer(List<DealerEmployeeContactInfo> dealerEmployeeContactInfos) {
		otherTable.removeAllItems();
		otherSelectedItem = null;
		Container indexedContainer = otherTable.getContainerDataSource();
		if (dealerEmployeeContactInfos != null && !dealerEmployeeContactInfos.isEmpty()) {
			for (DealerEmployeeContactInfo dealerEmployeeContactInfo : dealerEmployeeContactInfos) {
				
				CheckBox cbPrimary = new CheckBox();
				cbPrimary.setEnabled(false);
				cbPrimary.setValue(dealerEmployeeContactInfo.isPrimary());
				if (!ETypeContactInfo.LANDLINE.equals(dealerEmployeeContactInfo.getTypeInfo()) 
						&& !ETypeContactInfo.MOBILE.equals(dealerEmployeeContactInfo.getTypeInfo())) {
					Item item = indexedContainer.addItem(dealerEmployeeContactInfo.getId());
					item.getItemProperty(MEntityA.ID).setValue(dealerEmployeeContactInfo.getId());
					item.getItemProperty(MBaseAddress.TYPE).setValue(dealerEmployeeContactInfo.getTypeInfo() != null ? dealerEmployeeContactInfo.getTypeInfo().getDescLocale() : "");
					item.getItemProperty(CONTACTDETAIL).setValue(dealerEmployeeContactInfo.getValue());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param contactInfo
	 * @return
	 */
	private String getContactDetailByType(DealerEmployeeContactInfo dealerEmployeeContactInfo) {
		StringBuffer stringBuffer = new StringBuffer();
		if (ETypeContactInfo.LANDLINE.equals(dealerEmployeeContactInfo.getTypeInfo())) {
			stringBuffer.append(dealerEmployeeContactInfo.getValue());
			stringBuffer.append(" | ");
			stringBuffer.append(dealerEmployeeContactInfo.getTypeAddress() != null ? dealerEmployeeContactInfo.getTypeAddress().getDescLocale() : "");
		} else if (ETypeContactInfo.MOBILE.equals(dealerEmployeeContactInfo.getTypeInfo())) {
			stringBuffer.append(dealerEmployeeContactInfo.getValue());
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @param caption
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions, String caption) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		simpleTable.setCaption(I18N.message(caption));
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * assignValue
	 */
	public void assignValue(Dealer dealer, DealerEmployee dealerEmployee, ETypeContact typeContact) {
		reset();
		if (dealerEmployee == null) {
			dealerEmployee = DealerEmployee.createInstance();
			dealerEmployee.setCompany(dealer);
			dealerEmployee.setTypeContact(typeContact);
		}
		this.dealerEmployee = dealerEmployee;
		this.dealer = dealer;
		
		txtFirstName.setValue(dealerEmployee.getFirstName());
		txtLastName.setValue(dealerEmployee.getLastName());
		mgtOwnerAddressPanel.assignValue(dealerEmployee, typeContact);
		refresh();
	}
	
	/**
	 * 
	 */
	private void refresh() {
		List<DealerEmployeeContactInfo> dealerEmployeeContactInfos = dealerEmployee.getDealerEmployeeContactInfos();
		setUserContactPhonesValueIndexedContainer(dealerEmployeeContactInfos);
		setUserOtherContactsValueIndexedContainer(dealerEmployeeContactInfos);
	}
	
	/**
	 * Save Dealer Employee and dealer employee contact info
	 */
	private void save() {
		try {
			dealerEmployee.setFirstName(txtFirstName.getValue());
			dealerEmployee.setLastName(txtLastName.getValue());
			dealerEmployee.setCompany(dealer);
			ENTITY_SRV.saveOrUpdate(dealerEmployee);
			ENTITY_SRV.refresh(dealer);
			Notification.show("", I18N.message("msg.info.save.successfully"), Type.HUMANIZED_MESSAGE);
		} catch (Exception e) {
			Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Validate Dealer Employee Contact Info
	 * @return
	 */
	private boolean validate() {
		errors.clear();
		Label messageLabel;
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		
		checkMandatoryField(txtFirstName, "firstname");
		checkMandatoryField(txtLastName, "lastname");
		
		if (dealer == null) {
			errors.add(I18N.message("please.save.dealer.before.save.dealer.employee"));
		}
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		errors.clear();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		txtFirstName.setValue("");
		txtLastName.setValue("");
		
		phoneTable.removeAllItems();
		otherTable.removeAllItems();
		
		dealerEmployeeContactFormPanel.reset();
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnBack) {
			if (backListener != null) {
				backListener.onBack();
			}
		} else if (event.getButton() == btnSave) {
			if (validate()) {
				save();
			}
		} else if (event.getButton() == btnAdd) {
			contractInfoLayout.removeAllComponents();
			contractInfoLayout.addComponent(dealerEmployeeContactFormPanel);
			DealerEmployeeContactInfo dealerEmployeeContactInfo = new DealerEmployeeContactInfo();
			dealerEmployeeContactInfo.setDealerEmployee(dealerEmployee);
			dealerEmployeeContactFormPanel.assignValue(dealerEmployeeContactInfo);
		} else if (event.getButton() == btnEdit) {
			edit();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * @param backListener the backListener to set
	 */
	public void setBackListener(BackListener backListener) {
		this.backListener = backListener;
	}

	
	/**
	 * @author buntha.chea
	 */
	public interface BackListener extends Serializable {
		/** */
		void onBack();
	}
	
	/**
	 * click edit button for edit DealerEmployeeContactInfo
	 */
	private void edit() {
		if (phoneSelectedItem == null && otherSelectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			contractInfoLayout.removeAllComponents();
			contractInfoLayout.addComponent(dealerEmployeeContactFormPanel);
			dealerEmployeeContactFormPanel.assignValue(ENTITY_SRV.getById(DealerEmployeeContactInfo.class, selectedId));
		}
	}
	
	private void delete() {
		if (phoneSelectedItem == null && otherSelectedItem == null) {
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
						try {
							DEA_SRV.delete(DEA_SRV.getById(DealerEmployeeContactInfo.class, selectedId));
							Notification.show("", I18N.message("item.deleted.successfully"), Type.HUMANIZED_MESSAGE);
							refresh();
							selectedId = null;
						} catch (Exception e) {
							e.printStackTrace();
							Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				}
			});
		}
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		
		if (phoneTable == event.getComponent()) {
			phoneSelectedItem = event.getItem();
		} else {
			otherSelectedItem = event.getItem();
		}
		if (phoneSelectedItem != null) {
			selectedId = (Long) phoneSelectedItem.getItemProperty(MEntityA.ID).getValue();
		} else if (otherSelectedItem != null) {
			selectedId = (Long) otherSelectedItem.getItemProperty(MEntityA.ID).getValue();
		}
		if (event.isDoubleClick()) {
			contractInfoLayout.removeAllComponents();
			contractInfoLayout.addComponent(dealerEmployeeContactFormPanel);
			DealerEmployeeContactInfo dealerEmployeeContactInfo = ENTITY_SRV.getById(DealerEmployeeContactInfo.class, selectedId);
			dealerEmployeeContactFormPanel.assignValue(dealerEmployeeContactInfo);
		}
	}

	@Override
	public void onBack() {
		contractInfoLayout.removeAllComponents();
		contractInfoLayout.addComponent(contactInfoNavigationPanel);
		contractInfoLayout.addComponent(phoneTable);
		contractInfoLayout.addComponent(otherTable);
		refresh();
	}
}
