package com.nokor.ersys.collab.project.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.ProjectAssignee;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Assignment Assignee Table Panel
 * @author bunlong.taing
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectAssigneeTablePanel extends AbstractTabPanel implements AddClickListener, DeleteClickListener, ErsysCollabAppServicesHelper {
	/** */
	private static final long serialVersionUID = -822827963487737381L;
	
	private Long proId;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<ProjectAssignee> simpleTable;
	private Item selectedItem = null;
	
	/**
	 * Assignment Assignee Table Panel
	 */
	public ProjectAssigneeTablePanel() {
		super();
		setSizeFull();
		setMargin(true);
        NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setMargin(true);
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<ProjectAssignee>(this.columnDefinitions);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/**	 */
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					Long empProjectId = (Long) event.getItemId();
					String strEdit = I18N.message("edit") + " " + I18N.message("assignee");
					ProjectAssigneePopupPanel popupForm = new ProjectAssigneePopupPanel(ProjectAssigneeTablePanel.this, strEdit);
					popupForm.setEmpProjectId(empProjectId);
					popupForm.setProjectId(proId);
					ProjectAssignee empProject = PROJECT_SRV.getById(ProjectAssignee.class, empProjectId);
					popupForm.assignValues(empProject);
				}
			}
		});
				
		contentLayout.addComponent(simpleTable);
        return contentLayout;
	}
	
	/**
	 * Get Indexed Container
	 * @param empProjects
	 * @return
	 */
	private IndexedContainer getIndexedContainer(List<ProjectAssignee> empProjects) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (empProjects != null && !empProjects.isEmpty()) {	
			for (ProjectAssignee empProject : empProjects) {
				Item item = indexedContainer.addItem(empProject.getId());
				item.getItemProperty(ProjectAssignee.ID).setValue(empProject.getId());
				Employee employee = empProject.getEmployee();
				item.getItemProperty(ProjectAssignee.EMPLOYEE + "." + Employee.LASTNAMELOCALE).setValue(employee != null ? employee.getLastNameEn() : "");
				item.getItemProperty(ProjectAssignee.EMPLOYEE + "." + Employee.FIRSTNAMELOCALE).setValue(employee != null ? employee.getFirstNameEn() : "");
				item.getItemProperty(ProjectAssignee.ROLE).setValue(empProject.getRole().getDescEn());
				item.getItemProperty(ProjectAssignee.STARTDATE).setValue(empProject.getStartDate());
				item.getItemProperty(ProjectAssignee.ENDDATE).setValue(empProject.getEndDate());
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.EMPLOYEE + "." + Employee.LASTNAMELOCALE, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.EMPLOYEE + "." + Employee.FIRSTNAMELOCALE, I18N.message("firstname.en"),String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.ROLE, I18N.message("role"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.STARTDATE, I18N.message("date.start"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ProjectAssignee.ENDDATE, I18N.message("date.end"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Refresh Table after save action
	 */
	public void refreshTable() {
		assignValuesToControls(proId);
	}
	
	/**
	 * Assign Values To Controls
	 * @param entityId
	 */
	public void assignValuesToControls(Long entityId) {
		reset();
		if (entityId != null) {
			selectedItem = null;
			this.proId = entityId;
			// Must to reload to refresh session
			Project project = PROJECT_SRV.getById(Project.class, entityId);
			List<ProjectAssignee> empProjects = project.getAssignees();
			if (empProjects != null && !empProjects.isEmpty()) {
				simpleTable.setContainerDataSource(getIndexedContainer(empProjects));
			} else {
				simpleTable.removeAllItems();
				// Must use this code to clear table
				IndexedContainer indexedContainer = new IndexedContainer();
				for (ColumnDefinition column : this.columnDefinitions) {				
					indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
				}
				simpleTable.setContainerDataSource(indexedContainer);
			}
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent paramClickEvent) {
		if (proId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("assignment.msg.assignment.should.be.created.first"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();		
		} else {
			ProjectAssigneePopupPanel popupForm = new ProjectAssigneePopupPanel(this, I18N.message("assignee"));
			popupForm.reset();
			popupForm.setProjectId(proId);
		}
	}
	
	/**
	 * @see DeleteClickListener#deleteButtonClick(ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long empProId = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(empProId)),
		        new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -2203757872162548422L;

					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	ENTITY_SRV.delete(ProjectAssignee.class, empProId);
		                	assignValuesToControls(proId);
		                }
		            }
		        });
		}
	}

}
