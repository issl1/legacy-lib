package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamDeptLevel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamDebtLevelTablePanel extends VerticalLayout implements FinServicesHelper, FMEntityField {

	/** */
	private static final long serialVersionUID = 7990845162527925604L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<EColTeamDeptLevel> pagedTable;
	private Item selectedItem = null;
	
	private TextField txtDebtLevel;
	
	private EColTeam colTeam;
	private EColTeamDeptLevel colTeamDebtLevel;
	private VerticalLayout messagePanel;
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
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
				colTeamDebtLevel = null;
				getDebtLevelForm(colTeamDebtLevel);
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
					            		EColTeamDeptLevel colTeamDebtLevel = ENTITY_SRV.getById(EColTeamDeptLevel.class, debtLevelId);
					            		ENTITY_SRV.delete(colTeamDebtLevel);
					            	
					            		assignValues(colTeam.getId());
										Notification.show("",I18N.message("delete.successfully"),Notification.Type.HUMANIZED_MESSAGE);
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
		pagedTable = new SimplePagedTable<EColTeamDeptLevel>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				 if (event.isDoubleClick()) {
					messagePanel.removeAllComponents();
					messagePanel.setVisible(false);
					Long debtLevelId = (Long) selectedItem.getItemProperty("id").getValue();
					colTeamDebtLevel = ENTITY_SRV.getById(EColTeamDeptLevel.class, debtLevelId);
					getDebtLevelForm(colTeamDebtLevel);
				 }
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @param teamId
	 */
	public void assignValues(Long teamId) {
		if (teamId != null) {
			colTeam =  ENTITY_SRV.getById(EColTeam.class, teamId);
			pagedTable.setContainerDataSource(getIndexedContainer(colTeam.getDebtLevels()));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<EColTeamDeptLevel> teamDebtLevels) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
					
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			for (EColTeamDeptLevel teamDebtLevel : teamDebtLevels) {
				Item item = indexedContainer.addItem(teamDebtLevel.getId());
				item.getItemProperty(ID).setValue(teamDebtLevel.getId());
				item.getItemProperty("debt.level").setValue(teamDebtLevel.getDebtLevel());
			}
						
		} catch (DaoException e) {
			logger.error("DaoException", e);
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
	 * @param colTeamDebtLevel
	 */
	public void getDebtLevelForm(EColTeamDeptLevel colTeamDebtLevel) {
		final Window winDebtLevel = new Window(I18N.message("debt.level"));
		winDebtLevel.setModal(true);
		winDebtLevel.setResizable(false);
		winDebtLevel.setWidth(420, Unit.PIXELS);
		winDebtLevel.setHeight(140, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        txtDebtLevel = ComponentFactory.getTextField("debt.level", true, 20, 150);
        
		if (colTeamDebtLevel != null) {
			txtDebtLevel.setValue(String.valueOf(colTeamDebtLevel.getDebtLevel()));
		}
		
        formLayout.addComponent(txtDebtLevel);  
        /**
         * 
         */
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (colTeamDebtLevel == null) {
						EColTeamDeptLevel colTeamDebtLevel = new EColTeamDeptLevel();
						colTeamDebtLevel.setColTeam(colTeam);
						colTeamDebtLevel.setDebtLevel(Integer.valueOf(txtDebtLevel.getValue()));
						ENTITY_SRV.saveOrUpdate(colTeamDebtLevel);
					} else {
						colTeamDebtLevel.setDebtLevel(Integer.valueOf(txtDebtLevel.getValue()));
						ENTITY_SRV.saveOrUpdate(colTeamDebtLevel);
					}	
					winDebtLevel.close();
					assignValues(colTeam.getId());	
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
		if (StringUtils.isEmpty(txtDebtLevel.getValue())) {
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

}
