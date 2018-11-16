package com.nokor.efinance.gui.ui.panel.dashboard.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.gui.ui.panel.dashboard.vo.EmployeeGroupByVO;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.MTaskEmployeeConfig;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskEmployeeConfig;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;

/**
 * 
 * @author uhout.cheng
 */
public class ResourceTablePanel extends AbstractControlPanel implements MTaskEmployeeConfig, SelectedItem, ItemClickListener {
	
	/** */
	private static final long serialVersionUID = -4414441379996637394L;
	
	private ResourceSearchPanel searchPanel;
	private TreeTable treeTable;
	private Item selectedItem;
	
	/**
	 * @return the searchPanel
	 */
	public ResourceSearchPanel getSearchPanel() {
		return searchPanel;
	}
	
	/**
	 * 
	 */
	public ResourceTablePanel() {
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		createForm();
	}
	
	/**
	 * Create From
	 * @return
	 */
	private void createForm() {
		searchPanel = new ResourceSearchPanel(this);
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
			if (GROUPBY.equals(columnDefinition.getPropertyId())) {
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
		columnDefinitions.add(new ColumnDefinition(GROUPBY, I18N.message("group.by"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(FULLNAME, I18N.message("fullname"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(ROLE, I18N.message("role"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(POSITION, I18N.message("position"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(GROUP, I18N.message("group"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TOTALTASKASSIGNED, I18N.message("total.task.assigned"), Long.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(TOTALTASKCOMPLETED, I18N.message("total.task.completed"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(WEIGHT, I18N.message("weight"), Double.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Build Table Data
	 * @param restrictions
	 */
	public void buildTableData(BaseRestrictions<Employee> restrictions) {
		List<Employee> employees = ENTITY_SRV.list(restrictions);
		EmployeeGroupByVO taskGroupVO = new EmployeeGroupByVO(employees, searchPanel.getGroupByFields(), searchPanel.getGroupByFieldsCaption(), 0);
		treeTable.removeAllItems();
		setUpColumnDefinitions(treeTable);
		renderRow(taskGroupVO, -1, 0);
	}
	
	/**
	 * 
	 * @param empGroupVO
	 * @param parent
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderRow(EmployeeGroupByVO empGroupVO, int parent, int index) {
		if (empGroupVO.getEmpGroupByVos() != null) {
			Map<String, EmployeeGroupByVO> empGroupBys = empGroupVO.getEmpGroupByVos();
			int subParent = parent;
			for (String key : empGroupBys.keySet()) {
				subParent = renderParentRow(key, parent, index);
				index = renderRow(empGroupBys.get(key), subParent, ++index);
			}
		} else if (empGroupVO.getGroupByEmployees() != null) {
			Map<String, List<Employee>> groupByEmps = empGroupVO.getGroupByEmployees();
			int subParent = parent;
			for (String key : groupByEmps.keySet()) {
				if (searchPanel.getGroupByFields() != null) {
					subParent = renderParentRow(key, parent, index);
					index++;
				}
				
				for (Employee emp : groupByEmps.get(key)) {
					Item item = treeTable.addItem(index);
					if (subParent != -1) {
						treeTable.setParent(index, subParent);
						treeTable.setCollapsed(subParent, true);
					}
					treeTable.setChildrenAllowed(index, false);
					
					EJobPosition jobPosition = emp != null ? emp.getJobPosition() : null;
					
					item.getItemProperty(ID).setValue(emp.getId());
					item.getItemProperty(FULLNAME).setValue(getFullName(getDefaultString(emp.getLastNameEn()), 
							getDefaultString(emp.getFirstNameEn())));
					item.getItemProperty(ROLE).setValue(getEmpProfile(emp));
					item.getItemProperty(POSITION).setValue(jobPosition != null ? jobPosition.getDescEn() : "");
					item.getItemProperty(GROUP).setValue("");
					item.getItemProperty(TOTALTASKASSIGNED).setValue(getNbTotalTaskAssigned(emp));
					item.getItemProperty(TOTALTASKCOMPLETED).setValue(getNbPercentage(getNbTotalTaskAssigned(emp), 
							getNbTotalTaskAssigned(emp)) + "%");
					TaskEmployeeConfig taskEmployeeConfig = getTaskEmpConfigByEmpId(emp.getId());
					if (taskEmployeeConfig != null) {
						item.getItemProperty(WEIGHT).setValue(MyNumberUtils.getDouble(taskEmployeeConfig.getWeight()));
					}
					item.getItemProperty(STATUS).setValue(emp.getStatusRecord().getDescEn());
					index++;
				}
			}
		}
		return index;
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
		item.getItemProperty(GROUPBY).setValue(key);
		if (parent != -1) {
			treeTable.setParent(index, parent);
			treeTable.setCollapsed(parent, true);
		}
		return index;
	}
	
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
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
		if (event.isDoubleClick()) {
			if (getItemSelectedId() != null) {
				UI.getCurrent().addWindow(new ResourcesPopupWindow(this, getItemSelectedId()));
			}
		}
	}
	
	/**
	 * Get total task assigned
	 * @param employee
	 * @return
	 */
	public long getNbTotalTaskAssigned(Employee employee) {
		BaseRestrictions<Task> restrictions = new BaseRestrictions<>(Task.class);
		restrictions.addCriterion(Restrictions.eq(Task.ASSIGNEE, employee));
		return (long) ENTITY_SRV.count(restrictions);
	}
	
	/**
	 * Get number task type
	 * @param employee
	 * @param taskType
	 * @return
	 */
	public long getNbTaskTypeByType(Employee employee, ETaskType taskType) {
		BaseRestrictions<Task> restrictions = new BaseRestrictions<>(Task.class);
		restrictions.addCriterion(Restrictions.eq(Task.ASSIGNEE, employee));
		restrictions.addCriterion(Restrictions.eq(Task.TYPE, taskType));
		return (long) ENTITY_SRV.count(restrictions);
	}
	
	/**
	 * 
	 * @param firstValue
	 * @param secondValue
	 * @return
	 */
	public Double getNbPercentage(long firstValue, long secondValue) {
		if (firstValue > 0) {
			double total = ((double)(firstValue - secondValue) / firstValue);
			return MyMathUtils.roundTo(total * 100, 2);
		}
		return 0.00d;
	}
	
	/**
	 * 
	 * @param empId
	 * @return
	 */
	public TaskEmployeeConfig getTaskEmpConfigByEmpId(Long empId) {
		BaseRestrictions<TaskEmployeeConfig> restrictions = new BaseRestrictions<>(TaskEmployeeConfig.class);
		restrictions.addCriterion(Restrictions.eq("employee.id", empId));
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		List<TaskEmployeeConfig> taskEmployeeConfigs = ENTITY_SRV.list(restrictions);
		if (!taskEmployeeConfigs.isEmpty()) {
			for (TaskEmployeeConfig taskEmployeeConfig : taskEmployeeConfigs) {
				return taskEmployeeConfig;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param lastName
	 * @param firstName
	 * @return
	 */
	public String getFullName(String lastName, String firstName) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(lastName);
		stringBuffer.append(" ");
		stringBuffer.append(firstName);
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public FormLayout getFormLayout(String caption) {
		FormLayout formLayout = new FormLayout();
		formLayout.setCaption(I18N.message(caption));
		formLayout.setMargin(true);
		formLayout.setStyleName("myform-align-left");
		return formLayout;
	}
	

	/**
	 * 
	 * @param caption
	 * @param values
	 * @return
	 */
	public <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(String caption, List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(I18N.message(caption), values);
		comboBox.setWidth(170, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * Get employee profile description
	 * @param employee
	 * @return
	 */
	public String getEmpProfile(Employee employee) {
		String role = "";
		SecUser secUser = employee.getSecUser();
		if (secUser != null) {
			SecProfile  profile = secUser.getDefaultProfile();
			if (profile != null) {
				role = profile.getDescEn();
			}
		}
		return role;
	}
}
