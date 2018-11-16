package com.nokor.efinance.third.creditbureau.cbc.model;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

/**
 * @author ly.youhort
 */
public enum EnquiryType implements RefDataId {
	NA("New Applicant"), 
	RV("Review");

	private final String code;

	/**
     * 
     */
	private EnquiryType(final String code) {
		this.code = code;
	}

	/**
	 * return code
	 */
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescEn() {
		return I18N.value(code);
	}
	
	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return I18N.value(code);
	}

	/**
	 * List of service type
	 * 
	 * @return
	 */
	public static List<EnquiryType> list() {
		List<EnquiryType> enquiryTypes = new ArrayList<EnquiryType>();
		enquiryTypes.add(NA);
		enquiryTypes.add(RV);
		return enquiryTypes;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return 0L;
	}
}