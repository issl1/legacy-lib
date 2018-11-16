package com.nokor.frmk.auditlog;

import java.io.Serializable;

/**
 * 
 * @author prasnar
 *
 */
public class FieldProperty implements Serializable {
	/** */
	private static final long serialVersionUID = -7793069211858065772L;
	
	private String name;
	private String className;
	private boolean isLazy; // if true, should be load in WorkflowInterceptor.onLoad()
	
	/**
	 * 
	 * @param name
	 */
	public FieldProperty(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param name
	 * @param className
	 */
	public FieldProperty(String name, String className) {
		this.name = name;
		this.className = className;
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	
	
}