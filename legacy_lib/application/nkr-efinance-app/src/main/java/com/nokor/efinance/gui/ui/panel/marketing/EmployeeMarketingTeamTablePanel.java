package com.nokor.efinance.gui.ui.panel.marketing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.marketing.model.TeamEmployee;
import com.nokor.efinance.core.marketing.service.TeamEmployeeRestriction;
import com.nokor.efinance.core.marketing.service.TeamRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.organization.Employee;
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
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EmployeeMarketingTeamTablePanel extends VerticalLayout implements FinServicesHelper, FMEntityField {
	
	/** */
	private static final long serialVersionUID = 8227935410633404933L;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<SecUserDeptLevel> pagedTable;
	private Item selectedItem = null;
	
	private Employee employee;
	private TeamEmployee teamEmployee;
	private VerticalLayout messagePanel;
	
	private ComboBox cbxTeam;
	
	/**
	 * 
	 */
	public EmployeeMarketingTeamTablePanel() {
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

			/** */
			private static final long serialVersionUID = -2466166720788744912L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				teamEmployee = null;
				getTeamForm(teamEmployee);
			}
		});		
		
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItem != null) {
					ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"),	new ConfirmDialog.Listener() {
								
						/** */
						private static final long serialVersionUID = 3667629902085773622L;
								
						/**
						 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
						 */
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								Long teamId = (Long) selectedItem.getItemProperty(ID).getValue();
								TeamEmployee teamEmployee = ENTITY_SRV.getById(TeamEmployee.class, teamId);
								try {
									ENTITY_SRV.delete(teamEmployee);
									assignValues(employee);
									ComponentLayoutFactory.displaySuccessMsg("delete.successfully");
									selectedItem = null;
								} catch (Exception e) {
									ComponentLayoutFactory.displayErrorMsg(e.getMessage());
								}
							}
						}
					});
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
			
			/** */
			private static final long serialVersionUID = 6468384353849439753L;
			
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				 if (event.isDoubleClick()) {
					messagePanel.removeAllComponents();
					messagePanel.setVisible(false);
					Long teamEmpId = (Long) selectedItem.getItemProperty(TeamEmployee.ID).getValue();
					teamEmployee = ENTITY_SRV.getById(TeamEmployee.class, teamEmpId);
					getTeamForm(teamEmployee);
				 }
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @param employee
	 */
	public void assignValues(Employee employee) {
		this.employee = employee;
		if (employee != null) {
			TeamEmployeeRestriction restrictions = new TeamEmployeeRestriction();
			restrictions.setEmpId(employee.getId());
			pagedTable.setContainerDataSource(getIndexedContainer(ENTITY_SRV.list(restrictions)));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * 
	 * @param teamEmployees
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<TeamEmployee> teamEmployees) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (TeamEmployee teamEmployee : teamEmployees) {
			Item item = indexedContainer.addItem(teamEmployee.getId());
			item.getItemProperty(TeamEmployee.ID).setValue(teamEmployee.getId());
			item.getItemProperty(TeamEmployee.TEAM).setValue(teamEmployee.getTeam() != null ? teamEmployee.getTeam().getDescription() : StringUtils.EMPTY);
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
		columnDefinitions.add(new ColumnDefinition(TeamEmployee.TEAM, I18N.message("team"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param teamEmployee
	 */
	public void getTeamForm(TeamEmployee teamEmployee) {
		final Window window = new Window(I18N.message("employee"));
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(360, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
						
        FormLayout formLayout = new FormLayout();
        
        cbxTeam = getTeamComboBox(ENTITY_SRV.list(new TeamRestriction()), "team", true);
        
		if (teamEmployee != null) {
			cbxTeam.setValue(teamEmployee.getTeam());
		}
		
        formLayout.addComponent(cbxTeam);  
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 789010292605543044L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (teamEmployee == null) {
						TeamEmployee teamEmployee = TeamEmployee.createInstance();
						teamEmployee.setEmployee(employee);
						teamEmployee.setTeam((Team) cbxTeam.getValue());
						ENTITY_SRV.saveOrUpdate(teamEmployee);
					} else {
						teamEmployee.setTeam((Team) cbxTeam.getValue());
						ENTITY_SRV.saveOrUpdate(teamEmployee);
					}	
					window.close();
					assignValues(employee);	
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
		   
			/** */
			private static final long serialVersionUID = -1736925832884882213L;

			/**
			*@see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			*/
			@Override
			public void buttonClick(ClickEvent event) {
            	window.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        window.setContent(contentLayout);
        UI.getCurrent().addWindow(window);
	
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
		if (cbxTeam.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("team") }));
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
	private ComboBox getTeamComboBox(List<Team> teams, String caption, boolean isRequried) {
		ComboBox cbx = new ComboBox(I18N.message(caption));
		cbx.setNullSelectionAllowed(false);
		cbx.setRequired(isRequried);
		cbx.setWidth(150, Unit.PIXELS);
		for (Team team : teams) {
			cbx.addItem(team);
			cbx.setItemCaption(team, String.valueOf(team.getDescription()));
		}
		return cbx;
	}

}
