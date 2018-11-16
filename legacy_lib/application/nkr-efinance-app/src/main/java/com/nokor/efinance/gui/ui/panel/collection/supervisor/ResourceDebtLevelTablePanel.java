package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.service.DebtLevelUtils;
import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class ResourceDebtLevelTablePanel extends VerticalLayout implements FinServicesHelper, FMEntityField {
	
	/**
	 */
	private static final long serialVersionUID = -8802734016504920617L;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<SecUserDeptLevel> pagedTable;
	private Item selectedItem = null;
	
	private SecUser secUser;
	private SecUserDeptLevel secUserDeptLevel;
	private VerticalLayout messagePanel;
	
	private ComboBox cbxDebt;
	private String profileCode;
	
	public ResourceDebtLevelTablePanel(String profileCode) {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		this.profileCode = profileCode;
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				secUserDeptLevel = null;
				getDebtLevelForm(secUserDeptLevel);
			}
		});		
		
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItem != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"),
							new ConfirmDialog.Listener() {
								/** */
								private static final long serialVersionUID = 2380193173874927880L;
								
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										Long debtLevelId = (Long) selectedItem.getItemProperty(ID).getValue();
										SecUserDeptLevel secUserDeptLevel = ENTITY_SRV.getById(SecUserDeptLevel.class, debtLevelId);
					            		ENTITY_SRV.delete(secUserDeptLevel);
					            		assignValues(secUser);
										Notification.show("",I18N.message("delete.successfully"),Notification.Type.HUMANIZED_MESSAGE);
										selectedItem = null;
									}
								}
						});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
            	} else {
            		MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"), MessageBox.Icon.WARN,
        					I18N.message("msg.info.edit.item.not.selected"),
        					Alignment.MIDDLE_RIGHT,
        					new MessageBox.ButtonConfig[] { new MessageBox.ButtonConfig(
        							MessageBox.ButtonType.OK, I18N.message("ok")) });

        			mb.show();
            	}
			}
		});		
		
		navigationPanel.addButton(btnAdd);	
		navigationPanel.addButton(btnDelete);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				 if (event.isDoubleClick()) {
					messagePanel.removeAllComponents();
					messagePanel.setVisible(false);
					Long debtLevelId = (Long) selectedItem.getItemProperty("id").getValue();
					secUserDeptLevel = ENTITY_SRV.getById(SecUserDeptLevel.class, debtLevelId);
					getDebtLevelForm(secUserDeptLevel);
				 }
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * @param teamId
	 */
	public void assignValues(SecUser secUser) {
		this.secUser = secUser;
		if (secUser != null) {
			pagedTable.setContainerDataSource(getIndexedContainer(COL_SRV.getSecUserDeptLevel(secUser.getId())));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<SecUserDeptLevel> secUserDeptLevels) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (SecUserDeptLevel secUserDeptLevel : secUserDeptLevels) {
			Item item = indexedContainer.addItem(secUserDeptLevel.getId());
			item.getItemProperty(ID).setValue(secUserDeptLevel.getId());
			item.getItemProperty("debt.level").setValue(secUserDeptLevel.getDebtLevel());
		}
		
		return indexedContainer;
	}
	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("debt.level", I18N.message("debt.level"), Integer.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param secUserDeptLevel
	 */
	public void getDebtLevelForm(SecUserDeptLevel secUserDeptLevel) {
		final Window winDebtLevel = new Window(I18N.message("debt.level"));
		winDebtLevel.setModal(true);
		winDebtLevel.setResizable(false);
		winDebtLevel.setWidth(360, Unit.PIXELS);
		winDebtLevel.setHeight(140, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        cbxDebt = getDebtComboBox(DebtLevelUtils.getDebtLevels(profileCode), "debt.level", true);
        
		if (secUserDeptLevel != null) {
			cbxDebt.setValue(String.valueOf(secUserDeptLevel.getDebtLevel()));
		}
		
        formLayout.addComponent(cbxDebt);  
        /**
         * 
         */
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (secUserDeptLevel == null) {
						SecUserDeptLevel secUserDeptLevel = new SecUserDeptLevel();
						secUserDeptLevel.setSecUser(secUser);
						secUserDeptLevel.setDebtLevel(Integer.valueOf((String) cbxDebt.getValue()));
						ENTITY_SRV.saveOrUpdate(secUserDeptLevel);
					} else {
						secUserDeptLevel.setDebtLevel(Integer.valueOf((String) cbxDebt.getValue()));
						ENTITY_SRV.saveOrUpdate(secUserDeptLevel);
					}	
					winDebtLevel.close();
					assignValues(secUser);	
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winDebtLevel.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        winDebtLevel.setContent(contentLayout);
        UI.getCurrent().addWindow(winDebtLevel);
	
	}
	
	/**
	 * Validation
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		Label messageLabel;
		if (cbxDebt.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("debt.level") }));
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
	 * 
	 * @param caption
	 * @return
	 */
	private ComboBox getDebtComboBox(Integer[] debLevels, String caption, boolean isRequried) {
		ComboBox cbx = new ComboBox(I18N.message(caption));
		cbx.setNullSelectionAllowed(false);
		cbx.setRequired(isRequried);
		cbx.setWidth("70px");
		for (int debLevel : debLevels) {
			cbx.addItem(String.valueOf(debLevel));
		}
		return cbx;
	}

}
