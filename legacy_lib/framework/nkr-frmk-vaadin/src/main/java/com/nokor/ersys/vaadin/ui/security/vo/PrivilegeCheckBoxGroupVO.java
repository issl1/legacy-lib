package com.nokor.ersys.vaadin.ui.security.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;

/**
 * Privilege CheckBox Group VO
 * @author bunlong.taing
 */
public class PrivilegeCheckBoxGroupVO implements Serializable {
	/** */
	private static final long serialVersionUID = -9124556247902526001L;
	
	private Map<Long, CheckBox> checkBoxs;
	private PrivilegeCheckBoxGroupVO parent;
	private List<PrivilegeCheckBoxGroupVO> children;
	private Map<Long, ValueChangeListener> listeners;
	
	/**
	 * Set Value
	 * @param value
	 */
	public void setValue(Long privilegeId, boolean value, boolean isParentChange) {
		if (checkBoxs.get(privilegeId) != null) {
			checkBoxs.get(privilegeId).removeValueChangeListener(listeners.get(privilegeId));
			checkBoxs.get(privilegeId).setValue(value);
			checkBoxs.get(privilegeId).addValueChangeListener(listeners.get(privilegeId));
		}
		if (parent != null && value == true) {
			parent.setValue(privilegeId, value, false);
		}
		if (children != null && isParentChange) {
			for (PrivilegeCheckBoxGroupVO privilegeCheckBoxGroupVO : children) {
				privilegeCheckBoxGroupVO.setValue(privilegeId, value, isParentChange);
			}
		}
	}
	
	/**
	 * @return the checkBoxs
	 */
	public Map<Long, CheckBox> getCheckBoxs() {
		return checkBoxs;
	}
	
	/**
	 * @param checkBoxs the checkBoxs to set
	 */
	public void setCheckBoxs(Map<Long, CheckBox> checkBoxs) {
		this.checkBoxs = checkBoxs;
	}
	
	/**
	 * Add CheckBoxs
	 * @param privilegeId
	 * @param checkBox
	 */
	public void addCheckBoxs(Long privilegeId, CheckBox checkBox) {
		if (checkBoxs == null) {
			checkBoxs = new HashMap<Long, CheckBox>();
		}
		checkBoxs.put(privilegeId, checkBox);
	}
	
	/**
	 * @return the parent
	 */
	public PrivilegeCheckBoxGroupVO getParent() {
		return parent;
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(PrivilegeCheckBoxGroupVO parent) {
		this.parent = parent;
	}
	
	/**
	 * @return the children
	 */
	public List<PrivilegeCheckBoxGroupVO> getChildren() {
		return children;
	}
	
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<PrivilegeCheckBoxGroupVO> children) {
		this.children = children;
	}
	
	/**
	 * @param child the child to add
	 */
	public void addChildren(PrivilegeCheckBoxGroupVO child) {
		if (this.children == null) {
			this.children = new ArrayList<PrivilegeCheckBoxGroupVO>();
		}
		this.children.add(child);
	}

	/**
	 * @return the listeners
	 */
	public Map<Long, ValueChangeListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listeners the listeners to set
	 */
	public void setListeners(Map<Long, ValueChangeListener> listeners) {
		this.listeners = listeners;
	}
	
	/**
	 * Add Listeners
	 * @param privilegeId
	 * @param value
	 */
	public void addListeners(Long privilegeId, ValueChangeListener value) {
		if (listeners == null) {
			listeners = new HashMap<Long, ValueChangeListener>();
		}
		listeners.put(privilegeId, value);
	}

}
