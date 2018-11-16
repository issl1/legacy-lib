package com.nokor.common.app.systools.model;

import org.seuksa.frmk.model.entity.RefDataId;

/**
 * 
 * @author prasnar
 *
 */
public enum EnSysTaskWhere implements RefDataId {
	PLACE_01(1L, "BEFORE_SPRING_INIT_CONTEXT"),
	PLACE_10(2L, "AFTER_SPRING_INIT_CONTEXT"),
	PLACE_20(3L, "AFTER_START_UP");
	
	private final long id;
	private final String code;

	/**
     * 
     */
	private EnSysTaskWhere(final long id, final String code) {
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