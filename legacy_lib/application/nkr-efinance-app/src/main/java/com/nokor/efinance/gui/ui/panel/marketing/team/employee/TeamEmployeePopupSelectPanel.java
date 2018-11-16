package com.nokor.efinance.gui.ui.panel.marketing.team.employee;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.marketing.model.TeamEmployee;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class TeamEmployeePopupSelectPanel extends Window implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -6478920625744382421L;

	private static final String SELECT = "select";
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	private TextField txtFirstName;
	private TextField txtLastName;
	
	private SimpleTable<Employee> simpleTable;
	private SelectListener selectListener;
	
	private Long entityId;
	
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	/**
	 */
	public TeamEmployeePopupSelectPanel() {
		setCaption(I18N.message("employee"));
		setModal(true);
		setReadOnly(false);
		setWidth(750, Unit.PIXELS);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(createSearchPanel());
		content.addComponent(createForm());
		setContent(content);
	}
	
	/**
	 * @return
	 */
	private Component createSearchPanel() {
		
		txtFirstName = ComponentFactory.getTextField(false, 60, 150);
		txtLastName = ComponentFactory.getTextField(false, 60, 150);
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("firstname"));
		searchLayout.addComponent(txtFirstName);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("lastname"));
		searchLayout.addComponent(txtLastName);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnSearch);
		horizontalLayout.addComponent(btnReset);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(searchLayout);
		content.addComponent(horizontalLayout);
		content.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel(I18N.message("filters"));
		panel.setContent(content);
		return panel;
	}
	
	/**
	 * @return
	 */
	private Component createForm() {
		btnSelect = ComponentFactory.getButton("select");
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);
		btnSelect.addClickListener(this);
		
		createTable();
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(btnSelect);
		content.addComponent(simpleTable);
		return content;
	}
	
	/**
	 */
	private void createTable() {
		simpleTable = new SimpleTable<Employee>(createColumnDefinition());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(Employee.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(Employee.FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Employee.LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * @param employees
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<Employee> employees) {
		if (employees != null) {
			for (Employee employee : employees) {
				Item item = simpleTable.addItem(employee.getId());
				CheckBox cbSelect = new CheckBox();
				cbSelect.setValue(true);
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty(Employee.ID).setValue(employee.getId());
				item.getItemProperty(Employee.FIRSTNAME).setValue(employee.getFirstName());
				item.getItemProperty(Employee.LASTNAME).setValue(employee.getLastName());
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		simpleTable.removeAllItems();
	}
	
	/**
	 * Search
	 */
	private void search() {
		simpleTable.removeAllItems();
		setTableDataSource(ENTITY_SRV.list(getRestrictions()));
	}

	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Employee> getRestrictions() {
		String DOT = ".";
		String TEAM_EMP = "teamemp";
		BaseRestrictions<Employee> restrictions = new BaseRestrictions<>(Employee.class);
		
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(Employee.FIRSTNAME, txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtLastName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(Employee.LASTNAME, txtLastName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (entityId != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(TeamEmployee.class, TEAM_EMP);
			subCriteria.add(Restrictions.eq(TEAM_EMP + DOT + TeamEmployee.TEAM + DOT + TeamEmployee.ID, entityId));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property(TEAM_EMP + DOT + 
					TeamEmployee.EMPLOYEE + DOT + TeamEmployee.ID)));
			restrictions.addCriterion(Property.forName(TeamEmployee.ID).notIn(subCriteria));
		}
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			search();
		} else if (event.getButton() == btnReset) {
			txtFirstName.setValue(StringUtils.EMPTY);
			txtLastName.setValue(StringUtils.EMPTY);
		} else if (event.getButton() == btnSelect) {
			select();
		} 
	}
	
	/**
	 * Show the popup
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 */
	private void select() {
		if (selectListener != null) {
			selectListener.onSelected(getSelectedIds());
			close();
		}
	}
	
	private List<Long> getSelectedIds() {
		List<Long> ids = new ArrayList<Long>();
		for (Object i : simpleTable.getItemIds()) {
		    Long id = (Long) i;
		    Item item = simpleTable.getItem(id);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(id);
		    }
		}
		return ids;
	}
	
	/**
	 * @param selectListener
	 */
	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	public interface SelectListener {
		/** */
		void onSelected(List<Long> selectedIds);
	}

}
