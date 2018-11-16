package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.widget.SecUserListSelect;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Staff table of team in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamStaffTablePanel extends AbstractControlPanel implements SeuksaServicesHelper {

	/** */
	private static final long serialVersionUID = 2823328285655364499L;
	
	private EColTeam colTeam;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<Entity> pagedTable;
	private Item selectedItem = null;
	private SecUserListSelect<SecUser> lstStaff;
	protected List<String> errors;
	private VerticalLayout messagePanel;
	private Window windowAssetModel;
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 3978107434809078246L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				createFromAsset();
			}
		});
			
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 2496292912705985359L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
					
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
			
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Entity>(this.columnDefinitions);

		pagedTable.addItemClickListener(new ItemClickListener() {
	
			/** */
			private static final long serialVersionUID = -6037925438334976912L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
			
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * saveOrUpdate ColTeamStaff
	 */
	private void save() {
		if (validate()) {
			colTeam.setStaffs(lstStaff.getSelectedEntities());
			ENTITY_SRV.saveOrUpdate(colTeam);
			windowAssetModel.close();
			assignValues(colTeam.getId());
			reset();
			selectedItem = null;
		} else {
			displayErrors();
		}
	}
	
	/**
	 * 
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = 3091105769256269186L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[>> deleteColTeamStaff]");
					    SecUser staff = ENTITY_SRV.getById(SecUser.class, id);
					    List<SecUser> secUsers = colTeam.getStaffs();
					    if (secUsers != null && !secUsers.isEmpty()) {
					    	for (SecUser secUser : secUsers) {
								if (staff.getId().equals(secUser.getId())) {
									secUsers.remove(secUser);
								}
							}
					    }
					    colTeam.setStaffs(secUsers);
					    ENTITY_SRV.update(colTeam);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteColTeamStaff]");
					    dialog.close();
					    getNotificationDesc("item.deleted.successfully", id.toString());
						assignValues(colTeam.getId());
						selectedItem = null;
					}
				}
			});
		}
	}
	
	/**
	 * 
	 * @param description
	 * @param id
	 * @return
	 */
	private Notification getNotificationDesc(String description, String id) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ id }));
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
		return notification;
	}
	
	/**
	 * 
	 * @param colTeamId
	 */
	public void assignValues(Long colTeamId) {
		if (colTeamId != null) {
			colTeam = ENTITY_SRV.getById(EColTeam.class, new Long(colTeamId));
			pagedTable.setContainerDataSource(getColTeamStaffIndexedContainer(colTeam));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Display Errors
	 */
	private void displayErrors() {
		this.messagePanel.removeAllComponents();
		if (!(this.errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}
	
	/**
	 * 
	 * @param colTeam
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getColTeamStaffIndexedContainer(EColTeam colTeam) {
		List<SecUser> colTeamStaffs = colTeam.getStaffs();
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!colTeamStaffs.isEmpty()) {
			for (SecUser colTeamStaff : colTeamStaffs) {
				Item item = indexedContainer.addItem(colTeamStaff.getId());
				
				SecUser secUser = colTeamStaff;
				if (secUser != null) {
					item.getItemProperty("id").setValue(colTeamStaff.getId());
					item.getItemProperty("staff").setValue(secUser.getDesc());
				}
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 */
	private void removeErrorComponent() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}
	
	/**
	 * Reset
	 */
	protected void reset() {
		removeErrorComponent();
		lstStaff.setSelectedEntities(null);
	}
	
	/**
	 * Validate the asset model form
	 * @return
	 */
	private boolean validate() {
		removeErrorComponent();
		if (lstStaff.getSelectedEntities().isEmpty()) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("staff") }));
		}
		return errors.isEmpty();
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("staff", I18N.message("staff"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * Create Form Asset
	 */
	private void createFromAsset() {
		windowAssetModel = new Window(I18N.message("staff"));
		windowAssetModel.setModal(true);
		windowAssetModel.setResizable(false);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		
		lstStaff = getSecUserListSelect("staff", getRestrictions());
		reset();
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addStyleName("myform-align-left");
		formLayout.addComponent(lstStaff);
	        
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2999400046284478712L;

			public void buttonClick(ClickEvent event) {
				save();
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
	     
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 865544438425730124L;

			public void buttonClick(ClickEvent event) {
				windowAssetModel.close();
			}
		});
		btnCancel.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(formLayout);
	    
		windowAssetModel.setContent(contentLayout);
		UI.getCurrent().addWindow(windowAssetModel);
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<SecUser> getRestrictions() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addCriterion(Restrictions.gt("id", 0l));
		return restrictions;
	}
	
	/**
	 * Get SecUser ListSelect with a caption and the data from the restriction.
	 * @param caption
	 * @param restrictions
	 * @return
	 */
	private <T extends SecUser> SecUserListSelect<T> getSecUserListSelect(String caption, BaseRestrictions<T> restrictions) {
		SecUserListSelect<T> listSelect = new SecUserListSelect<T>(I18N.message(caption));
		listSelect.setRows(10);
		listSelect.setNullSelectionAllowed(false);
		listSelect.setImmediate(true);
		listSelect.setWidth(180, Unit.PIXELS);
		listSelect.setRestrictions(restrictions);
		listSelect.setRequired(true);
		listSelect.setMultiSelect(true);
		listSelect.renderer();
		return listSelect;
	}
	
}
