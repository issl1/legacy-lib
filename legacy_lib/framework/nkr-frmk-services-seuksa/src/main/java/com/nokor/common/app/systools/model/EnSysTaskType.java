package com.nokor.common.app.systools.model;

import org.seuksa.frmk.model.entity.RefDataId;

/**
 * 
 * @author prasnar
 *
 */
public enum EnSysTaskType implements RefDataId {
	JAVA(1L, "JAVA"),
	SQL_FILE(2L, "SQL_FILE"),
	SQL(3L, "SQL");
	
	private final long id;
	private final String code;

	/**
     * 
     */
	private EnSysTaskType(final long id, final String code) {
		this.id = id;
		this.code = code;
	}

	/**
	 * return id
	 */
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getCode() {
		return code;
	}

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return getCode();
	}

	@Override
	public String getDescEn() {
		return getDesc();
	}
	
}