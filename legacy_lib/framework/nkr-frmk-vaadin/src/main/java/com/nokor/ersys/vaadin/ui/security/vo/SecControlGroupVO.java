package com.nokor.ersys.vaadin.ui.security.vo;

import java.io.Serializable;
import java.util.List;

import com.nokor.frmk.security.model.SecControl;

/**
 * 
 * @author phirun.kong
 *
 */
public class SecControlGroupVO implements Serializable {

	/**	 */
	private static final long serialVersionUID = 3481509237637936535L;
	
	private int level;
	private SecControl control;
	private List<SecControlGroupVO> subGroup;	
	
	public SecControlGroupVO() {

	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the control
	 */
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}

	/**
	 * @return the subGroup
	 */
	public List<SecControlGroupVO> getSubGroup() {
		return subGroup;
	}

	/**
	 * @param subGroup the subGroup to set
	 */
	public void setSubGroup(List<SecControlGroupVO> subGroup) {
		this.subGroup = subGroup;
	}

}
