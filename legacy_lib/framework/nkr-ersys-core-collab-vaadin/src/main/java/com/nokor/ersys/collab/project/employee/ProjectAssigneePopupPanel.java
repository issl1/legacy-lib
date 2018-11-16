package com.nokor.ersys.collab.project.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;

import com.nokor.ersys.collab.project.model.EProjectRole;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.ProjectAssignee;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.help.vaadin.ui.EmployeeComboBox;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.CloseClickButton;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.SaveClickButton;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.CloseClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Assignment Assignee Popup Panel
 * @author bunlong.taing
 *
 */
public class ProjectAssigneePopupPanel extends Window implements SaveClickListener, CloseClickListener, ErsysCollabAppServicesHelper {

	/** */
	private static final long serialVersionUID = 4123869751665793044L;
	
	private ProjectAssigneeTablePanel mainPanel;
	
	private EmployeeComboBox cbxAssignee;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ERefDataComboBox<EProjectRole> cbxRole;
	
	private Long proId;
	private Long empProjectId;
	
	/**
	 * Assignment Assignee Popup Panel constructor
	 * @param mainPanel
	 * @param caption
	 */
	public ProjectAssigneePopupPanel(final ProjectAssigneeTablePanel mainPanel, String caption) {
		this.mainPanel = mainPanel;
		setModal(true);
		setCaption(I18N.message(caption));
		setContent(createForm());
		setWidth(650, Unit.PIXELS);
		setHeight(250, Unit.PIXELS);
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 * Create Form pop up
	 * @return
	 */
	private Component createForm() {
		Button btnSave = new SaveClickButton(this);
		Button btnCancel = new CloseClickButton(this);
		
		cbxAssignee = new EmployeeComboBox("assignee");
		cbxAssignee.setRestrictions(new BaseRestrictions<Employee>(Employee.class));
		cbxAssignee.setWidth(200, Unit.PIXELS);
		cbxAssignee.setRequired(true);
		cbxAssignee.renderer();
		
		cbxRole = new ERefDataComboBox<EProjectRole>(I18N.message("role"), EProjectRole.values());
		cbxRole.setWidth(200, Unit.PIXELS);
		cbxRole.setRequired(true);
		
		dfStartDate = ComponentFactory.getAutoDateField("date.start", true);
		dfStartDate.setWidth(95, Unit.PIXELS);
		dfEndDate = ComponentFactory.getAutoDateField("date.end", true);
		dfEndDate.setWidth(95, Unit.PIXELS);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxAssignee);
		formLayout.addComponent(cbxRole);
		formLayout.addComponent(dfStartDate);
		formLayout.addComponent(dfEndDate);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(formLayout);
		
		return contentLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent paramClickEvent) {
		if (this.proId != null) {
			saveEntity();
		}
	}
	
	/**
	 * Save Entity
	 */
	private void saveEntity() {
		if (validate()) {
			ProjectAssignee empProject = buildEmpProjectFromControls();
			boolean isUpdate = empProject.getId() != null && empProject.getId() > 0;
			Project project = PROJECT_SRV.getById(Project.class, this.proId);
			if (isUpdate) {
				setCascadeAtUpdate(project, empProject);
	        } else {
	        	setCascadeAtCreation(project, empProject);
	        }
			PROJECT_SRV.updateProcess(project);
			mainPanel.refreshTable();
			close();
		}
	}
	
	/**
	 * Build EmpProject From Controls
	 * @return
	 */
	private ProjectAssignee buildEmpProjectFromControls() {
		ProjectAssignee empProject = null;
		boolean isUpdate = this.empProjectId != null && this.empProjectId > 0;
		if (isUpdate) {
			empProject = PROJECT_SRV.getById(ProjectAssignee.class, this.empProjectId);
		} else {
			empProject = ProjectAssignee.createInstance();
		}
		buildEmpProjectDetailFromControls(empProject);
		return empProject;
	}
	
	/**
	 * Build EmpProject Detail From Controls
	 * @param empProject
	 */
	private void buildEmpProjectDetailFromControls(ProjectAssignee empProject) {
		empProject.setEmployee(cbxAssignee.getSelectedEntity());
		empProject.setStartDate(dfStartDate.getValue());
		empProject.setEndDate(dfEndDate.getValue());
		empProject.setRole(cbxRole.getSelectedEntity());
	}
	
	/**
	 * Set Cascade At Creation
	 * @param project
	 * @param empProject
	 */
	private void setCascadeAtCreation(Project project, ProjectAssignee empProject) {
		List<ProjectAssignee> empProjects = project.getAssignees();
		if (empProjects == null) {
			empProjects = new ArrayList<ProjectAssignee>();
		}
		empProjects.add(empProject);
		empProject.setCrudAction(CrudAction.CREATE);
		empProject.setProject(project);
		project.addSubListEntityToCascade(empProjects);
    }
	
	/**
	 * Set Cascade At Update
	 * @param project
	 * @param empProject
	 */
	private void setCascadeAtUpdate(Project project, ProjectAssignee empProject) {
		List<ProjectAssignee> empProjects = project.getAssignees();
		empProject.setCrudAction(CrudAction.UPDATE);
		project.addSubListEntityToCascade(empProjects);
    }
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		boolean isValid = true;
		
		if (cbxAssignee.getSelectedEntity() == null
				|| cbxRole.getSelectedEntity() == null
				|| dfEndDate.getValue() == null
				|| dfStartDate.getValue() == null) {
			isValid = false;
			MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (((Date) dfEndDate.getValue()).before((Date) dfStartDate.getValue())) {
			isValid = false;
			MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("field.range.date.incorrect"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		}
		
		return isValid;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.CloseClickListener#closeButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void closeButtonClick(ClickEvent paramClickEvent) {
		close();
	}
	
	/**
	 * Reset popup form
	 */
	public void reset() {
		this.proId = null;
		this.empProjectId = null;
		cbxAssignee.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxRole.setSelectedEntity(null);
	}
	
	/**
	 * Assign Values
	 * @param empProject
	 */
	public void assignValues(ProjectAssignee empProject) {
		cbxAssignee.setSelectedEntity(empProject.getEmployee());
		dfStartDate.setValue(empProject.getStartDate());
		dfEndDate.setValue(empProject.getEndDate());
		cbxRole.setSelectedEntity(empProject.getRole());
	}
	
	/**
	 * Set Project Id
	 * @param projectId
	 */
	public void setProjectId(Long projectId) {
		this.proId = projectId;
	}
	
	/**
	 * Set EmpProject Id
	 * @param empPorjectId
	 */
	public void setEmpProjectId(Long empPorjectId) {
		this.empProjectId = empPorjectId;
	}

}
