package com.nokor.efinance.gui.ui.panel.dashboard.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.action.model.ActionExecution;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.dashboard.vo.TaskGroupByVO;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.service.TaskRestriction;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;

/**
 * Task Form Panel
 * @author bunlong.taing
 */
public class TaskFormPanel extends AbstractControlPanel implements FinServicesHelper, SelectedItem, ItemClickListener {
	/** */
	private static final long serialVersionUID = -6660690110901098888L;
	
	private TaskSearchPanel searchPanel;
	private TreeTable treeTable;
	private boolean isMyTask;
	private ETaskType taskType;
	private Item selectedItem;
	
	/**
	 * 
	 * @param taskType
	 * @param isMyTask
	 */
	public TaskFormPanel(ETaskType taskType, boolean isMyTask) {
		this.isMyTask = isMyTask;
		this.taskType = taskType;
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		createForm();
		setCaption(taskType.getDescEn());
	}
	
	/**
	 * Create From
	 * @return
	 */
	private void createForm() {
		searchPanel = new TaskSearchPanel(this);
		treeTable = createTreeTable();
		
		addComponent(searchPanel);
		addComponent(treeTable);
		buildTableData(searchPanel.getRestrictions());
	}
	
	/**
	 * Create Tree table
	 * @return
	 */
	private TreeTable createTreeTable() {
		TreeTable table = new TreeTable(I18N.message(""));
		table.setPageLength(10);
		table.setSelectable(true);
		table.setSizeFull();
		table.setImmediate(true);
		table.setColumnCollapsingAllowed(true);
		table.addItemClickListener(this);
		setUpColumnDefinitions(table);
		return table;
	}
	
	
	/**
	 * Set Up Column Definitions
	 * @param table
	 */
	private void setUpColumnDefinitions(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.removeContainerProperty(columnDefinition.getPropertyId());
		}
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			if (Task.GROUPBY.equals(columnDefinition.getPropertyId())) {
				if (searchPanel.getGroupByFields() != null) {
					table.addContainerProperty(
							columnDefinition.getPropertyId(),
							columnDefinition.getPropertyType(),
							null,
							columnDefinition.getPropertyCaption(),
							null,
							columnDefinition.getPropertyAlignment());
					table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
				}
			} else {
				table.addContainerProperty(
						columnDefinition.getPropertyId(),
						columnDefinition.getPropertyType(),
						null,
						columnDefinition.getPropertyCaption(),
						null,
						columnDefinition.getPropertyAlignment());
				table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
			}
		}
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Task.GROUPBY, I18N.message("group.by"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(Task.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(Task.TITLEEN, I18N.message("name"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Task.ASSIGNEE, I18N.message("assignee"), String.class, Align.LEFT, 150));
		if (!isMyTask) {
			columnDefinitions.add(new ColumnDefinition(Task.ROLE, I18N.message("role"), String.class, Align.LEFT, 150));
			columnDefinitions.add(new ColumnDefinition(Task.GROUP, I18N.message("group"), String.class, Align.LEFT, 100));
			columnDefinitions.add(new ColumnDefinition(Task.POSITION, I18N.message("position"), String.class, Align.LEFT, 150));
		}
		columnDefinitions.add(new ColumnDefinition(Task.DEADLINE, I18N.message("deadline"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Task.ASSIGNMENT_DATE, I18N.message("assignment.date"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Task.DATEREMAINING, I18N.message("date.remaining"), Long.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(MTask.LINK_TO, I18N.message("link.to"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Task.STATUS, I18N.message("status"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Build Table Data
	 * @param restrictions
	 */
	public void buildTableData(TaskRestriction restrictions) {	
		treeTable.removeAllItems();
		setUpColumnDefinitions(treeTable);
		if (searchPanel.getGroupByFields() != null && searchPanel.getGroupByFields().length != 0) {
			restrictions.setGroupBy(true);
			List<Task> tasks = ENTITY_SRV.list(restrictions);
			TaskGroupByVO taskGroupVO = new TaskGroupByVO(tasks, searchPanel.getGroupByFields(), searchPanel.getGroupByFieldsCaption(), 0);
			renderRow(taskGroupVO, -1, 0);
		} else {
			restrictions.setGroupBy(false);
			List<Task> tasks = ENTITY_SRV.list(restrictions);
			if (!tasks.isEmpty()) {
				int index = 0;
				buildTaskTable(tasks, index);
			}
		}
	}
	
	/**
	 * 
	 * @param tasks
	 * @param index
	 * @return
	 */
	private int buildTaskTable(List<Task> tasks, int index) {
		int parentId = index;
		for (Task task : tasks) {
			index++;
			Item item = treeTable.addItem(index);
			renderRow(item, index, task, false);
			treeTable.setParent(index, parentId);

			List<Task> childTasks = task.getChildren();

			if ((childTasks == null || childTasks.isEmpty())) {
				treeTable.setChildrenAllowed(index, false);
			} else {
				index = buildTaskTable(childTasks, index);
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @param item
	 * @param index
	 * @param task
	 * @param isGroupBy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderRow(Item item, int index, Task task, boolean isGroupBy) {
		Employee assignee = task.getAssignee();
		SecUser secUser = assignee != null ? assignee.getSecUser() : null;
		EJobPosition jobPosition = assignee != null ? assignee.getJobPosition() : null;
	
		if (isGroupBy) {
			index++;
		} else {
			String conId = "";
			if (task.getParent() == null) {
				conId = task.getAction() != null ? "-" + task.getAction().getEntityId().toString() : "";
			} 
			item.getItemProperty(Task.GROUPBY).setValue(task.getDesc() + conId);
		}	
		item.getItemProperty(Task.ID).setValue(task.getId());
		item.getItemProperty(Task.TITLEEN).setValue(task.getDesc());
		item.getItemProperty(Task.ASSIGNEE).setValue(assignee != null ? assignee.getFullName() : "");
		if (!isMyTask) {
			item.getItemProperty(Task.ROLE).setValue(secUser != null ? secUser.getDefaultProfile().getDescEn() : "");
			item.getItemProperty(Task.GROUP).setValue("");
			item.getItemProperty(Task.POSITION).setValue(jobPosition != null ? jobPosition.getDescEn() : "");
		}
		item.getItemProperty(Task.DEADLINE).setValue(DateUtils.getDateLabel(task.getDeadline(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(Task.ASSIGNMENT_DATE).setValue(DateUtils.getDateLabel(TASK_SRV.getAssignmentDate(task), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(Task.DATEREMAINING).setValue(task.getRemainingDays());
		item.getItemProperty(Task.STATUS).setValue(task.getStatusRecord().getCode());
		return index;
	}
	
	/**
	 * Render Row
	 * @param taskGroupVO
	 * @param parent
	 * @param index
	 * @return
	 */
	private int renderRow(TaskGroupByVO taskGroupVO, int parent, int index) {
		if (taskGroupVO.getTaskGroupByVOs() != null) {
			Map<String, TaskGroupByVO> taskGroupBys = taskGroupVO.getTaskGroupByVOs();
			int subParent = parent;
			for (String key : taskGroupBys.keySet()) {
				subParent = renderParentRow(key, parent, index);
				index = renderRow(taskGroupBys.get(key), subParent, ++index);
			}
		} else if (taskGroupVO.getGroupByTasks() != null) {
			Map<String, List<Task>> groupByTasks = taskGroupVO.getGroupByTasks();
			int subParent = parent;
			for (String key : groupByTasks.keySet()) {
				if (searchPanel.getGroupByFields() != null) {
					subParent = renderParentRow(key, parent, index);
					index++;
				}
				
				for (Task task : groupByTasks.get(key)) {
					Item item = treeTable.addItem(index);
					if (subParent != -1) {
						treeTable.setParent(index, subParent);
						treeTable.setCollapsed(subParent, true);
					}
					treeTable.setChildrenAllowed(index, false);
					index = renderRow(item, index, task, true);
				}
			}
		}
		return index;
	}
	
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(Task.ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (getItemSelectedId() != null) {
			Task task = ENTITY_SRV.getById(Task.class, getItemSelectedId());
			if (task != null && task.getAction() != null) {
				ActionExecution actionExecution = task.getAction();
				if (actionExecution != null && actionExecution.getAction() != null) {
					try {
						Class<?> taskClass = Class.forName(actionExecution.getAction().getExecValue());
						Object obj = taskClass.newInstance();
						Method method = taskClass.getMethod("execute", Long.class);
						method.invoke(obj, task.getAction().getEntityId());
					} catch (ClassNotFoundException e) {
						logger.error(e.getMessage());
					} catch (InstantiationException e) {
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage());
					} catch (NoSuchMethodException e) {
						logger.error(e.getMessage());
					} catch (SecurityException e) {
						logger.error(e.getMessage());
					} catch (IllegalArgumentException e) {
						logger.error(e.getMessage());
					} catch (InvocationTargetException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * @param key
	 * @param parent
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, int parent, int index) {
		Item item = treeTable.addItem(index);
		item.getItemProperty(Task.GROUPBY).setValue(key);
		if (parent != -1) {
			treeTable.setParent(index, parent);
			treeTable.setCollapsed(parent, true);
		}
		return index;
	}
	
	/**
	 * Is My Task
	 * @return
	 */
	public boolean isMyTask() {
		return this.isMyTask;
	}

	/**
	 * @return the taskType
	 */
	public ETaskType getTaskType() {
		return taskType;
	}
}
