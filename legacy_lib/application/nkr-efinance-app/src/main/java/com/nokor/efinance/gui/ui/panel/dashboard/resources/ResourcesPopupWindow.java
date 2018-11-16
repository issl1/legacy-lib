package com.nokor.efinance.gui.ui.panel.dashboard.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.ETimeUnit;
import com.nokor.ersys.collab.project.model.TaskEmployeeConfig;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ResourcesPopupWindow extends Window implements SeuksaServicesHelper {

	/** */
	private static final long serialVersionUID = 994513590728067994L;

	private TaskEmployeeConfig taskEmployeeConfig;
	protected List<String> errors;
	private VerticalLayout messagePanel;
	private TextField txtWeight;
	private TextField txtMaxNbTask;
	private ERefDataComboBox<ETimeUnit> cbxTimeUnit;
	private CheckBox cbActive;
	private ResourceTablePanel resourceTablePanel;
	
	/**
	 * 
	 * @param resourceTablePanel
	 * @param id
	 */
	public ResourcesPopupWindow(ResourceTablePanel resourceTablePanel, Long id) {
		this.resourceTablePanel = resourceTablePanel;
		Employee employee = ENTITY_SRV.getById(Employee.class, id);
		taskEmployeeConfig = resourceTablePanel.getTaskEmpConfigByEmpId(employee.getId());
		
		init();
	
		FormLayout frmLayout = resourceTablePanel.getFormLayout(null);
		frmLayout.addComponent(txtWeight);
		frmLayout.addComponent(txtMaxNbTask);
		frmLayout.addComponent(cbxTimeUnit);
		frmLayout.addComponent(cbActive);
		Panel settingPanel = getPanel("setting", frmLayout);
		
		if (taskEmployeeConfig == null) {
			taskEmployeeConfig = TaskEmployeeConfig.createInstance();
		} else {
			txtWeight.setValue(String.valueOf(taskEmployeeConfig.getWeight()));
			txtMaxNbTask.setValue(String.valueOf(taskEmployeeConfig.getMaxNumberTasks()));
			cbxTimeUnit.setSelectedEntity(taskEmployeeConfig.getUnit());
			cbActive.setValue(taskEmployeeConfig.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
		
		Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 7563146105852391977L;

			public void buttonClick(ClickEvent event) {
				close();
            }
        });
        btnCancel.setIcon(FontAwesome.TIMES);
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2180861060390083465L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					taskEmployeeConfig.setWeight(Double.parseDouble(txtWeight.getValue()));
					taskEmployeeConfig.setMaxNumberTasks(Double.parseDouble(txtMaxNbTask.getValue()));
					taskEmployeeConfig.setUnit(cbxTimeUnit.getSelectedEntity());
					taskEmployeeConfig.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
					taskEmployeeConfig.setEmployee(employee);
					// saveOrUpdateTaskEmployeeConfig
					ENTITY_SRV.saveOrUpdate(taskEmployeeConfig);
					resourceTablePanel.getSearchPanel().refreshTable();
					close();
				} else {
					displayErrors();
				}
			}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.addComponent(getGeneralSettingPanel(employee));
		contentLayout.addComponent(getPerformancePanel(employee));
		contentLayout.addComponent(settingPanel);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.addComponent(navigationPanel);
		verLayout.addComponent(messagePanel);
		verLayout.addComponent(contentLayout);
		setContent(verLayout);
	}
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	private Panel getGeneralSettingPanel(Employee employee) {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" >";
		template += "<tr>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblId\"></td>";
		template += "<td><div location =\"lblIdValue\"/></td>";
		template += "<td width=\"50\"></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblFullName\"></td>";
		template += "<td><div location =\"lblFullNameValue\"/></td>";
		template += "<td width=\"50\"></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblRole\"></td>";
		template += "<td><div location =\"lblRoleValue\"/></td>";
		template += "</tr>";
		template += "<tr>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblPosition\"></td>";
		template += "<td><div location =\"lblPositionValue\"/></td>";
		template += "<td width=\"50\"></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblGroup\"></td>";
		template += "<td><div location =\"lblGroupValue\"/></td>";
		template += "<td width=\"50\"></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblCurrentTask\"></td>";
		template += "<td><div location =\"lblCurrentTaskValue\"/></td>";
		template += "</tr>";
		template += "</table>";
		
		Label lblId = new Label();
		Label lblFullName = new Label();
		Label lblRole = new Label();
		Label lblPosition = new Label();
		Label lblGroup = new Label();
		Label lblCurrentTask = new Label();
		
		lblId.setValue(String.valueOf(employee.getId()));
		lblFullName.setValue(resourceTablePanel.getFullName(String.valueOf(employee.getLastNameEn()), 
				String.valueOf(employee.getFirstNameEn())));
		lblRole.setValue(resourceTablePanel.getEmpProfile(employee));
		lblPosition.setValue(employee.getJobPosition() != null ? employee.getJobPosition().getDescEn() : "");
		lblGroup.setValue(employee.getGender() != null ? employee.getGender().getDescEn() : "");
		lblCurrentTask.setValue(String.valueOf(resourceTablePanel.getNbTotalTaskAssigned(employee)));
		
		cusLayout.addComponent(new Label(I18N.message("id")), "lblId");
		cusLayout.addComponent(new Label(I18N.message("fullname")), "lblFullName");
		cusLayout.addComponent(new Label(I18N.message("role")), "lblRole");
		cusLayout.addComponent(new Label(I18N.message("position")), "lblPosition");
		cusLayout.addComponent(new Label(I18N.message("group")), "lblGroup");
		cusLayout.addComponent(new Label(I18N.message("current.tasks")), "lblCurrentTask");
		cusLayout.addComponent(lblId, "lblIdValue");
		cusLayout.addComponent(lblFullName, "lblFullNameValue");
		cusLayout.addComponent(lblRole, "lblRoleValue");
		cusLayout.addComponent(lblPosition, "lblPositionValue");
		cusLayout.addComponent(lblGroup, "lblGroupValue");
		cusLayout.addComponent(lblCurrentTask, "lblCurrentTaskValue");
		cusLayout.setTemplateContents(template);
		
		Panel mainPanel = getPanel("general.setting", cusLayout);
		return mainPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getPerformancePanel(Employee employee) {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" >";
		template += "<tr>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblTotalTaskAssigned\"></td>";
		template += "<td><div location =\"lblTotalTaskAssignedValue\"/></td>";
		template += "<td width=\"50\"></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblTotalTaskCompleted\"></td>";
		template += "<td><div location =\"lblTotalTaskCompletedValue\"/></td>";
		template += "</tr>";
		
		Label lblTotalTaskAssigned = new Label();
		Label lblTotalTaskCompleted = new Label();
		long totalTaskAssigned = resourceTablePanel.getNbTotalTaskAssigned(employee);
		lblTotalTaskAssigned.setValue(String.valueOf(resourceTablePanel.getNbTotalTaskAssigned(employee)));
		lblTotalTaskCompleted.setValue(resourceTablePanel.getNbPercentage(totalTaskAssigned, totalTaskAssigned) + "%");
	
		cusLayout.addComponent(new Label(I18N.message("total.task.assigned")), "lblTotalTaskAssigned");
		cusLayout.addComponent(new Label(I18N.message("total.task.completed")), "lblTotalTaskCompleted");
		cusLayout.addComponent(lblTotalTaskAssigned, "lblTotalTaskAssignedValue");
		cusLayout.addComponent(lblTotalTaskCompleted, "lblTotalTaskCompletedValue");
		
		List<ETaskType> taskTypes = ETaskType.values();
		int i = 0;
		if (taskTypes != null && !taskTypes.isEmpty()) {
			for (ETaskType taskType : taskTypes) {
				i++;
				template += "<tr>";
				template += "<td align=\"left\" width=\"120\"><div location=\"lblTotalTaskType" + i + "\"></td>";
				template += "<td><div location =\"lblTotalTaskTypeValue" + i + "\"></td>";
				template += "<td width=\"50\"></td>";
				template += "<td align=\"left\" width=\"120\"><div location=\"lblTotalTaskTypeCompleted" + i + "\"></td>";
				template += "<td><div location =\"lblTotalTaskTypeCompletedValue" + i + "\"></td>";
				template += "</tr>";
				long taskTypeValue = resourceTablePanel.getNbTaskTypeByType(employee, taskType);
				Label lblTotalTaskTypeCaption = new Label();
				Label lblTotalTaskTypeValue = new Label();
				Label lblTotalTaskTypeCompletedValue = new Label();
				lblTotalTaskTypeCaption.setValue(taskType.getDescEn());
				lblTotalTaskTypeValue.setValue(String.valueOf(taskTypeValue));
				lblTotalTaskTypeCompletedValue.setValue(resourceTablePanel.getNbPercentage(taskTypeValue, taskTypeValue) + "%");
				
				cusLayout.addComponent(lblTotalTaskTypeCaption, "lblTotalTaskType" + i);
				cusLayout.addComponent(new Label(I18N.message("total.task.completed")), "lblTotalTaskTypeCompleted" + i);
				cusLayout.addComponent(lblTotalTaskTypeValue, "lblTotalTaskTypeValue" + i);
				cusLayout.addComponent(lblTotalTaskTypeCompletedValue, "lblTotalTaskTypeCompletedValue" + i);
			}
		}
		template += "</table>";
		cusLayout.setTemplateContents(template);
		
		Panel mainPanel = getPanel("performance", cusLayout);
		return mainPanel;
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @return
	 */
	private Panel getPanel(String caption, Component component) {
		Panel mainPanel = new Panel(I18N.message(caption));
		mainPanel.setContent(component);
		return mainPanel;
	}
	
	/**
	 * 
	 */
	private void init() {
		setModal(true);
		setResizable(false);
		setWidth(800, Unit.PIXELS);
		setCaption(I18N.message("resources"));
		
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		txtWeight = ComponentFactory.getTextField("weight", false, 20, 150);
		txtMaxNbTask = ComponentFactory.getTextField("maximum.number.tasks", false, 20, 150);
		txtMaxNbTask.addStyleName("mytextfield-caption");
		cbxTimeUnit = resourceTablePanel.getERefDataComboBox("per", ETimeUnit.values());
		cbxTimeUnit.setRequired(true);
		cbxTimeUnit.setWidth(150, Unit.PIXELS);
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private List<String> checkDoubleTextField(AbstractTextField field, String messageKey) {
		if (StringUtils.isNotEmpty((String) field.getValue())) {
			try {
				Double.parseDouble((String) field.getValue());
			} catch (NumberFormatException e) {
				errors.add(I18N.message("field.value.incorrect.1", new String[] { I18N.message(messageKey) }));
			}
		} else {
			field.setValue("0");
		}
		return errors;
	}
	
	/**
	 * Validate the appointment form
	 * @return
	 */
	private boolean validate() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
		if (cbxTimeUnit.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("per") }));
		}
		checkDoubleTextField(txtWeight, "weight");
		checkDoubleTextField(txtMaxNbTask, "maximum.number.tasks");
		return errors.isEmpty();
	}
	
	/**
	 * Display Errors
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!(errors.isEmpty())) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}
}
