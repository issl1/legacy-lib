package com.nokor.efinance.gui.ui.panel.marketing.team.employee;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.marketing.model.TeamEmployee;
import com.nokor.efinance.core.marketing.service.TeamEmployeeRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.marketing.team.employee.TeamEmployeePopupSelectPanel.SelectListener;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class TeamEmployeeFormPanel extends AbstractTabPanel implements SelectListener {
	
	/** */
	private static final long serialVersionUID = -6218699663409969577L;
	
	private Long teamId;
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<TeamEmployee> pagedTable;
	private TeamEmployeePopupSelectPanel selectPopup;
	private Item selectedItem = null;

	/**
	 * 
	 */
	public TeamEmployeeFormPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		selectPopup = new TeamEmployeePopupSelectPanel();
		selectPopup.setSelectListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -8124368013102914037L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				selectPopup.reset();
				selectPopup.setEntityId(teamId);
				selectPopup.show();
			}
		});
			
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -4500971864435265005L;

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
		pagedTable = new SimplePagedTable<TeamEmployee>(this.columnDefinitions);

		pagedTable.addItemClickListener(new ItemClickListener() {
	
			/** */
			private static final long serialVersionUID = -3233764286136286819L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
			
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * 
	 * @param teamId
	 */
	public void assignValues(Long teamId) {
		reset();
		if (teamId != null) {
			this.teamId = teamId;
			TeamEmployeeRestriction restrictions = new TeamEmployeeRestriction();
			restrictions.setTeamId(teamId);
			pagedTable.setContainerDataSource(getTeamEmployeeIndexedContainer(ENTITY_SRV.list(restrictions)));
		}
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(TeamEmployee.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(Employee.FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Employee.LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param teamEmployees
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getTeamEmployeeIndexedContainer(List<TeamEmployee> teamEmployees) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!teamEmployees.isEmpty()) {
			for (TeamEmployee teamEmp : teamEmployees) {
				Item item = indexedContainer.addItem(teamEmp.getId());
				item.getItemProperty(TeamEmployee.ID).setValue(teamEmp.getId());
				Employee employee = teamEmp.getEmployee();
				if (employee != null) {
					item.getItemProperty(Employee.FIRSTNAME).setValue(employee.getFirstNameLocale());
					item.getItemProperty(Employee.LASTNAME).setValue(getDefaultString(employee.getLastNameLocale()));
				}
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		teamId = null;
		selectedItem = null;
		pagedTable.removeAllItems();
	}
	
	/**
	 * @see com.nokor.efinance.gui.ui.panel.marketing.team.employee.TeamEmployeePopupSelectPanel.SelectListener#onSelected(java.util.Map)
	 */
	@Override
	public void onSelected(List<Long> selectedIds) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			List<TeamEmployee> teamEmployees = new ArrayList<>();
			Team team = ENTITY_SRV.getById(Team.class, teamId);
			for (Long empId : selectedIds) {
				TeamEmployee teamEmployee = TeamEmployee.createInstance();
				teamEmployee.setEmployee(ENTITY_SRV.getById(Employee.class, empId));
				teamEmployee.setTeam(team);
				teamEmployees.add(teamEmployee);
			}
			ENTITY_SRV.saveOrUpdateBulk(teamEmployees);
			assignValues(teamId);
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
			final Long id = (Long) selectedItem.getItemProperty(TeamEmployee.ID).getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {
				
				/** */
				private static final long serialVersionUID = 3471044778069351769L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[>> deleteTeamEmployee]");
						ENTITY_SRV.delete(TeamEmployee.class, id);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteTeamEmployee]");
					    dialog.close();
						ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
						assignValues(teamId);
						selectedItem = null;
					}
				}
			});
		}
	}

}
