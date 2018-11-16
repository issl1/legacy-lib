package com.nokor.ersys.collab.project.task.detail;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.collab.project.model.ETaskPriority;
import com.nokor.ersys.collab.project.model.ETaskSeverity;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskClassification;
import com.nokor.ersys.collab.project.model.TaskWkfHistoryItem;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Task Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TaskFormPanel extends AbstractFormPanel implements ErsysCollabAppServicesHelper {
	/** */
	private static final long serialVersionUID = 4067631463873129562L;
	private static final String _NULL = "NULL";
	
	private Long entityId;
	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	
	private EntityComboBox<Project> cbxProject;
	private EntityComboBox<Employee> cbxReporter;
	private EntityComboBox<Employee> cbxAssignee;
	
	private EntityRefComboBox<TaskClassification> cbxClassification;
	private EntityRefComboBox<ETaskType> cbxType;
	private EntityRefComboBox<ETaskPriority> cbxPriority;
	private EntityRefComboBox<ETaskSeverity> cbxSeverity;
	
	private AutoDateField dfDeadline;
	private TextArea txtComment;
	private NativeButton btnAddComment;
	
	private VerticalLayout commentLayout;
	private Panel commentPanel;
	
	/**
	 */
	public TaskFormPanel() {
        super.init();
        setCaption(I18N.message("task"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnAddComment = new NativeButton(I18N.message("add.comment"));
		btnAddComment.setIcon(FontAwesome.PLUS);
		btnAddComment.addStyleName("btn btn-default");
		btnAddComment.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 1097688927424076065L;
			@Override
			public void buttonClick(ClickEvent event) {
				txtComment.focus();
			}
		});
		
		txtCode = ComponentFactory.getTextField("code", false, 10, 200);
		txtDesc = ComponentFactory.getTextField("desc", true, 100, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 100, 200);
		
		cbxProject = new EntityComboBox<Project>(Project.class, Project.DESCEN);
		cbxReporter = new EntityComboBox<Employee>(Employee.class, Employee.FULLNAME);
		cbxAssignee = new EntityComboBox<Employee>(Employee.class, Employee.FULLNAME);
		cbxProject.setCaption(I18N.message("project"));
		cbxReporter.setCaption(I18N.message("reporter"));
		cbxAssignee.setCaption(I18N.message("assignee"));
		cbxProject.setWidth(200, Unit.PIXELS);
		cbxReporter.setWidth(200, Unit.PIXELS);
		cbxAssignee.setWidth(200, Unit.PIXELS);
		cbxProject.renderer();
		cbxReporter.renderer();
		cbxAssignee.renderer();
		
		cbxClassification = new EntityRefComboBox<TaskClassification>(I18N.message("classification"));
		cbxClassification.setRestrictions(new BaseRestrictions<TaskClassification>(TaskClassification.class));
		cbxClassification.setWidth(200, Unit.PIXELS);
		cbxClassification.setRequired(true);
		cbxClassification.renderer();
		
		cbxType = new EntityRefComboBox<ETaskType>(ETaskType.values());
		cbxType.setCaption(I18N.message("type"));
		cbxType.setWidth(200, Unit.PIXELS);
		cbxType.setRequired(true);
		
		cbxPriority = new EntityRefComboBox<ETaskPriority>(ETaskPriority.values());
		cbxPriority.setCaption(I18N.message("priority"));
		cbxPriority.setWidth(200, Unit.PIXELS);
		cbxPriority.setRequired(true);
		
		cbxSeverity = new EntityRefComboBox<ETaskSeverity>(ETaskSeverity.values());
		cbxSeverity.setCaption(I18N.message("severity"));
		cbxSeverity.setWidth(200, Unit.PIXELS);
		cbxSeverity.setRequired(true);

		dfDeadline = ComponentFactory.getAutoDateField();
		dfDeadline.setWidth(100, Unit.PIXELS);
		dfDeadline.setCaption(I18N.message("deadline"));
		
		txtComment = ComponentFactory.getTextArea("comment", false, 500, 150);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(false);
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(cbxReporter);
		formLayout.addComponent(cbxAssignee);
		formLayout.addComponent(dfDeadline);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.setMargin(false);
		formLayout.addComponent(cbxProject);
		formLayout.addComponent(cbxClassification);
		formLayout.addComponent(cbxType);
		formLayout.addComponent(cbxPriority);
		formLayout.addComponent(cbxSeverity);
		horizontalLayout.addComponent(formLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.addComponent(txtComment);
		
		Panel taskDetailPanel = new Panel();
		taskDetailPanel.setCaption(I18N.message("task.details"));
		taskDetailPanel.setContent(verticalLayout);
		
		commentLayout = new VerticalLayout();
		commentLayout.setSpacing(true);
		
		verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(commentLayout);
		
		commentPanel = new Panel();
		commentPanel.setCaption(I18N.message("comments"));
		commentPanel.setContent(verticalLayout);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(taskDetailPanel);
		contentLayout.addComponent(commentPanel);
		
		return contentLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		Task task = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			task = TASK_SRV.getById(Task.class, getEntityId());
		} else {
			task = Task.createInstance();
		}
		buildTaskFromControls(task);
		return task;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Task task= (Task) getEntity();
		boolean isUpdate = task.getId() != null && task.getId() > 0;
		if (isUpdate) {
			TASK_SRV.updateProcess(task);
        } else {
        	TASK_SRV.createProcess(task);
        	setEntityId(task.getId());
        }
		assignValues(task.getId());
	}
	
	/**
	 * 
	 * @param task
	 */
	private void buildTaskFromControls(Task task) {
		task.setForcedHistory(true);
		task.setCode(txtCode.getValue());
		task.setDesc(txtDesc.getValue());
		task.setDescEn(txtDescEn.getValue());
		task.setProject(cbxProject.getSelectedEntity());
		task.setReporter(cbxReporter.getSelectedEntity());
		task.setAssignee(cbxAssignee.getSelectedEntity());
		task.setClassification(cbxClassification.getSelectedEntity());
		task.setType(cbxType.getSelectedEntity());
		task.setPriority(cbxPriority.getSelectedEntity());
		task.setSeverity(cbxSeverity.getSelectedEntity());
		task.setDeadline(dfDeadline.getValue());
		if (StringUtils.isNotEmpty(txtComment.getValue())) {
			task.setComment(txtComment.getValue());
		}
	}
	
	/**
	 * Assign Values
	 * @param entityId
	 */
	public void assignValues(Long entityId) {
		reset();
		if (entityId != null) {
			setEntityId(entityId);
			Task task = TASK_SRV.getById(Task.class, entityId);
			
			txtCode.setValue(getDefaultString(task.getCode()));
			txtDesc.setValue(getDefaultString(task.getDesc()));
			txtDescEn.setValue(getDefaultString(task.getDescEn()));
			cbxProject.setSelectedEntity(task.getProject());
			cbxReporter.setSelectedEntity(task.getReporter());
			cbxAssignee.setSelectedEntity(task.getAssignee());
			cbxClassification.setSelectedEntity(task.getClassification());
			cbxType.setSelectedEntity(task.getType());
			cbxPriority.setSelectedEntity(task.getPriority());
			cbxSeverity.setSelectedEntity(task.getSeverity());
			dfDeadline.setValue(task.getDeadline());
			buildCommentsPanel(TASK_SRV.listComments(task));
		}
	}
	
	/**
	 * 
	 * @param comments
	 */
	private void buildCommentsPanel(List<TaskWkfHistoryItem> comments) {
		commentLayout.removeAllComponents();
		boolean showCommentPanel = false;
		if (comments != null) {
			for (TaskWkfHistoryItem comment : comments) {
				if (!_NULL.equals(comment.getNewValue())) {
					String caption = getDefaultString(comment.getCreateUser()) + " - " + DateUtils.getDateLabel(comment.getCreateDate(), DateUtils.FORMAT_YYYYMMDD_HHMMSS);
					TextArea commentControls = ComponentFactory.getTextArea(caption, false, 500, 150);
					commentControls.setEnabled(false);
					commentControls.setValue(getDefaultString(comment.getNewValue()));
					commentLayout.addComponent(commentControls);
					showCommentPanel = true;
				}
			}
		}
		if (showCommentPanel) {
			commentLayout.addComponent(btnAddComment);
		}
		commentPanel.setVisible(showCommentPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setEntityId(null);
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxProject.setSelectedEntity(null);
		cbxReporter.setSelectedEntity(null);
		cbxAssignee.setSelectedEntity(null);
		cbxClassification.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
		cbxPriority.setSelectedEntity(null);
		cbxSeverity.setSelectedEntity(null);
		dfDeadline.setValue(null);
		txtComment.setValue("");
		commentLayout.removeAllComponents();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDesc, "desc");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxClassification, "classification");
		checkMandatorySelectField(cbxType, "type");
		checkMandatorySelectField(cbxPriority, "priority");
		checkMandatorySelectField(cbxSeverity, "severity");
		return errors.isEmpty();
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

}
