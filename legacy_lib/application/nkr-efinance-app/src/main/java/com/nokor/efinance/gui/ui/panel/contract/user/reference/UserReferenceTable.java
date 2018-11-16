package com.nokor.efinance.gui.ui.panel.contract.user.reference;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.model.MIndividualReferenceInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
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
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * User reference table 
 * @author uhout.cheng
 */
public class UserReferenceTable extends VerticalLayout implements ItemClickListener, SelectedItem, ClickListener, FinServicesHelper, MIndividualReferenceInfo {

	/** */
	private static final long serialVersionUID = 8729489462481100904L;
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid gray; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"150px\" bgcolor=\"#ffffff\" "
			+ "style=\"border:1px solid gray;\" >";
	private static String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid gray;\" bgcolor=\"#ffffff\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	

	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private Item selectedItem;
	private ApplicantIndividualPanel delegate;
	private UserReferenceForm userReferenceForm;
	private Individual individual;
	private Long selectedId;
	
	/**
	 * 
	 * @param userPanel
	 */
	public UserReferenceTable(ApplicantIndividualPanel delegate) {
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
		simpleTable.setItemDescriptionGenerator(new ItemDescriptionGenerator() {

			private static final long serialVersionUID = -7746336444314592025L;

			@Override
			public String generateDescription(Component source, Object itemId, Object propertyId) {
				
 				return createContactInfoTooltip(itemId);
			}
		});
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		userReferenceForm = new UserReferenceForm();
		userReferenceForm.getBtnBack().addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -2626530780812690322L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				individual = INDIVI_SRV.getById(Individual.class, individual.getId());
				assignValues(individual);
				delegate.getUserReferenceLayout().removeComponent(userReferenceForm);
				delegate.getUserReferenceTable().setVisible(true);
			}
		});
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeUndefined();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setCaption(I18N.message("references"));
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
		columnDefinitions.add(new ColumnDefinition(REFERENCETYPE, I18N.message("type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(RELATIONSHIP, I18N.message("relationship"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(LASTNAMEEN + FIRSTNAMEEN, I18N.message("name"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PHONE, I18N.message("phone"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualReferenceInfos
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getUserReferenceIndexedContainer(List<IndividualReferenceInfo> individualReferenceInfos) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!individualReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfo indRefInfo : individualReferenceInfos) {
				if (indRefInfo != null && indRefInfo.getId() != null) {
					Item item = indexedContainer.addItem(indRefInfo.getId());
					item.getItemProperty(ID).setValue(indRefInfo.getId());
					item.getItemProperty(REFERENCETYPE).setValue(indRefInfo.getReferenceType() != null ? indRefInfo.getReferenceType().getDescLocale() : "");
					item.getItemProperty(RELATIONSHIP).setValue(indRefInfo.getRelationship() != null ? indRefInfo.getRelationship().getDescLocale() : "");
					item.getItemProperty(LASTNAMEEN + FIRSTNAMEEN).setValue(indRefInfo.getLastNameEn() + " " + indRefInfo.getFirstNameEn());
					List<String> refPhones = new ArrayList<String>();
					if (indRefInfo.getReferenceType() != null) {
						List<IndividualReferenceContactInfo> individualReferenceContactInfos = indRefInfo.getIndividualReferenceContactInfos();
						if (individualReferenceContactInfos != null && !individualReferenceContactInfos.isEmpty()) {
							for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
								ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
								if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
									refPhones.add(contactInfo.getValue() + "(M)");
								} else if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
									refPhones.add(contactInfo.getValue() + "(L | " + contactInfo.getTypeAddress().getDescLocale() + ")");
								}
							}
						}
					}
					item.getItemProperty(PHONE).setValue(StringUtils.join(refPhones, " , "));
				}
			}
		}
		return indexedContainer;
	}
	
	private String createContactInfoTooltip(Object itemId) {
		String contactInfoTooltip = "";
		Long indiRefId = (Long) itemId;
		IndividualReferenceInfo individualReferenceInfo = ENTITY_SRV.getById(IndividualReferenceInfo.class, indiRefId);
		
		contactInfoTooltip += OPEN_TABLE;
		
		contactInfoTooltip += OPEN_TR;
		contactInfoTooltip += OPEN_TH;
		contactInfoTooltip += I18N.message("contact.type");
		contactInfoTooltip += CLOSE_TH;
		contactInfoTooltip += OPEN_TH;
		contactInfoTooltip += I18N.message("contact.detail");
		contactInfoTooltip += CLOSE_TH;
		contactInfoTooltip += CLOSE_TR;
		
		for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceInfo.getIndividualReferenceContactInfos()) {
			ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
			String contactDetail = "";
			if (contactInfo != null) {
				contactInfoTooltip += OPEN_TR;
				
				contactInfoTooltip += OPEN_TD;
				contactInfoTooltip += contactInfo.getTypeInfo().getDescEn();
				contactInfoTooltip += CLOSE_TD;
				
				if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
					contactDetail = contactInfo.getValue() + " | " + contactInfo.getTypeAddress().getDescEn();
				} else {
					contactDetail = contactInfo.getValue();
				}
				
				contactInfoTooltip += OPEN_TD;
				contactInfoTooltip += contactDetail;
				contactInfoTooltip += CLOSE_TD;
				
				contactInfoTooltip += CLOSE_TR;
			}
		}
		contactInfoTooltip += CLOSE_TABLE;
		return contactInfoTooltip;
	}
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		this.individual = individual;
		simpleTable.setContainerDataSource(getUserReferenceIndexedContainer(INDIVI_SRV.getIndividualReferenceInfos(individual.getId())));
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			delegate.getUserReferenceTable().setVisible(false);
			delegate.getUserReferenceLayout().addComponent(userReferenceForm);
			userReferenceForm.assignValues(individual, null);
			userReferenceForm.reset();
		} else if (event.getButton().equals(btnEdit)) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				delegate.getUserReferenceTable().setVisible(false);
				delegate.getUserReferenceLayout().addComponent(userReferenceForm);
				userReferenceForm.removedMessagePanel();
				userReferenceForm.assignValues(individual, ENTITY_SRV.getById(IndividualReferenceInfo.class, selectedId));
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
					private static final long serialVersionUID = -6334521256548619902L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							INDIVI_SRV.deleteReference(INDIVI_SRV.getById(IndividualReferenceInfo.class, selectedId));
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
			delegate.getUserReferenceTable().setVisible(false);
			delegate.getUserReferenceLayout().addComponent(userReferenceForm);
			userReferenceForm.assignValues(individual, ENTITY_SRV.getById(IndividualReferenceInfo.class, selectedId));
			userReferenceForm.removedMessagePanel();
		}
	}	

}
