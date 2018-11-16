package com.nokor.ersys.collab.project.task.list;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskClassification;
import com.nokor.ersys.collab.project.service.TaskRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Task Search Panel
 * @author bunlong.taing
 */
public class TaskSearchPanel extends AbstractSearchPanel<Task> {
	/** */
	private static final long serialVersionUID = -4869992331827935268L;
	
	private TextField txtSearchText;
	private EntityComboBox<Project> cbxProject;
	private EntityRefComboBox<TaskClassification> cbxClassification;
	private EntityRefComboBox<ETaskType> cbxType;
	
	/**
	 */
	public TaskSearchPanel(TaskTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtSearchText = ComponentFactory.getTextField(100, 200);
		
		cbxProject = new EntityComboBox<Project>(Project.class, Project.DESCEN);
		cbxProject.setWidth(200, Unit.PIXELS);
		cbxProject.renderer();
		
		cbxClassification = new EntityRefComboBox<TaskClassification>();
		cbxClassification.setRestrictions(new BaseRestrictions<TaskClassification>(TaskClassification.class));
		cbxClassification.setWidth(200, Unit.PIXELS);
		cbxClassification.renderer();
		
		cbxType = new EntityRefComboBox<ETaskType>(ETaskType.values());
		cbxType.setWidth(200, Unit.PIXELS);
		
		Label lblSearchText = ComponentFactory.getLabel("search.text");
		Label lblProject = ComponentFactory.getLabel("project");
		Label lblClassification = ComponentFactory.getLabel("classification");
		Label lblType = ComponentFactory.getLabel("type");
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblSearchText);
		gridLayout.addComponent(txtSearchText);
		gridLayout.addComponent(lblProject);
		gridLayout.addComponent(cbxProject);
		gridLayout.addComponent(lblClassification);
		gridLayout.addComponent(cbxClassification);
		gridLayout.addComponent(lblType);
		gridLayout.addComponent(cbxType);
		
		gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblProject, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblClassification, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblType, Alignment.MIDDLE_LEFT);
		
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public TaskRestriction getRestrictions() {
		TaskRestriction restrictions = new TaskRestriction();
		restrictions.setText(txtSearchText.getValue());
		restrictions.setProject(cbxProject.getSelectedEntity());
		restrictions.setClassification(cbxClassification.getSelectedEntity());
		restrictions.setTaskType(cbxType.getSelectedEntity());
		restrictions.addOrder(Order.asc(Task.ID));
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtSearchText.setValue("");
		cbxProject.setSelectedEntity(null);
		cbxClassification.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
	}

}
