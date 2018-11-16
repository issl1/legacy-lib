package com.nokor.efinance.third.creditbureau.cbc.model;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MyDataId;

/**
 * @author ly.youhort
 */
public enum ProductStatus implements MyDataId {
	
	N("Normal"),
	S("Special Mention"),
	C("Closed"),
	W("Loss"),
	U("Substandard"),
	D("Doubtful");
	
	private final String code;

	/**
     * 
     */
	private ProductStatus(final String code) {
		this.code = code;
	}

	/**
	 * return code
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return I18N.value(code);
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return 0L;
	}

	
}