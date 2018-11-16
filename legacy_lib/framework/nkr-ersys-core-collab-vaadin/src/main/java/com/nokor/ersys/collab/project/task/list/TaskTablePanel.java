package com.nokor.ersys.collab.project.task.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.collab.project.model.ETaskPriority;
import com.nokor.ersys.collab.project.model.ETaskSeverity;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskClassification;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Task Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TaskTablePanel extends AbstractTablePanel<Task> implements ErsysCollabAppServicesHelper {
	/** */
	private static final long serialVersionUID = 1925914542625180262L;

	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("tasks"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("tasks"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Task> createPagedDataProvider() {
		PagedDefinition<Task> pagedDefinition = new PagedDefinition<Task>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(Task.ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(Task.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(Task.PROJECT + "." + Project.DESCEN, I18N.message("project"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.CLASSIFICATION + "." + TaskClassification.DESCEN, I18N.message("classification"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.ASSIGNEE + "." + Employee.FULLNAME, I18N.message("assignee"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Task.TYPE + "." + ETaskType.DESCEN, I18N.message("type"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.PRIORITY + "." + ETaskPriority.DESCEN, I18N.message("priority"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Task.SEVERITY + "." + ETaskSeverity.DESCEN, I18N.message("severity"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Task> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Task getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return TASK_SRV.getById(Task.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Task> createSearchPanel() {
		return new TaskSearchPanel(this);
	}

}
