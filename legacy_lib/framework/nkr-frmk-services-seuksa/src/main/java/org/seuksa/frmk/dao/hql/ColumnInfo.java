package org.seuksa.frmk.dao.hql;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author prasnar
 *
 */
public class ColumnInfo implements Serializable {
	/** */
	private static final long serialVersionUID = 7635446172954256268L;
	
	private String parentPath;
	private String name;
	private Class<?> type;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public ColumnInfo(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * 
	 * @param parentPath
	 * @param name
	 * @param type
	 */
	public ColumnInfo(String parentPath, String name, Class<?> type) {
		this.parentPath = parentPath;
		this.name = name;
		this.type = type;
	}
	
	/**
	 * @return the full name
	 */
	public String getFullName() {
		return StringUtils.isNotEmpty(parentPath) ? parentPath + "." + name : name;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentPath
	 */
	public String getParentPath() {
		return parentPath;
	}

	/**
	 * @param parentPath the parentPath to set
	 */
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	
}
