package com.nokor.ersys.core.help.vaadin.ui;

import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.vaadin.ui.ComboBox;

/**
 * Employee ComboBox
 * @author bunlong.taing
 */
public class EmployeeComboBox extends ComboBox implements AppServicesHelper {

	/** */
	private static final long serialVersionUID = 277996464373701773L;
	private BaseRestrictions<Employee> restrictions;
	private boolean onlyActive;
	
	protected LinkedHashMap<String, Employee> valueMap = new LinkedHashMap<String, Employee>();
	
	/**
	 * Constructor
	 */
	public EmployeeComboBox() {
		this("");
	}
	
	/**
	 * Constructor with caption
	 * @param caption
	 */
	public EmployeeComboBox(String caption) {
		this(caption, true);
	}
	
	/**
	 * Constructor with caption and only Active employee
	 * @param caption
	 * @param onlyActive
	 */
	public EmployeeComboBox(String caption, boolean onlyActive) {
		super(I18N.message(caption));
		this.onlyActive = onlyActive;
	}
	
	/**
	 * Set Restrictions
	 * @param restrictions
	 */
	public void setRestrictions(BaseRestrictions<Employee> restrictions) {
		this.restrictions = restrictions;
		if (onlyActive) {
			this.restrictions.addCriterion("statusRecord", FilterMode.EQUALS, EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Get Selected Entity
	 * @return
	 */
	public Employee getSelectedEntity () {
		return valueMap.get(getValue());
	}
	
	/**
	 * Set Selected Entity
	 * @param employee
	 */
	public void setSelectedEntity (Employee employee) {
		if (employee != null) {
    		setValue(employee.getId().toString());
    	} else {
    		setValue(null);
    	}
	}
	
	/**
	 * Render data
	 */
	public void renderer() {
		List<Employee> employees = ENTITY_SRV.list(restrictions);
    	renderer(employees);
    	setValue(null);
    }
	
	/**
	 * Render data
	 * @param employees
	 */
	public void renderer(List<Employee> employees) {
		valueMap.clear();
    	removeAllItems();
		for (Employee employee : employees) {
    		addItem(employee.getId().toString());
    		setItemCaption(employee.getId().toString(), employee.getFullNameLocale());
    		valueMap.put(employee.getId().toString(), employee);                
    	}
	}
	
	/**
	 * Clear the ComboBox
	 */
	public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }

}
