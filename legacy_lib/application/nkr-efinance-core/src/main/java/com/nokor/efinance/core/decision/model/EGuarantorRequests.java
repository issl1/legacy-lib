package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EGuarantorRequests extends BaseERefData implements AttributeConverter<EGuarantorRequests, Long> {
	
	/** */
	private static final long serialVersionUID = 1066961532583633897L;
	
	private EGuarantorRequestsCategory category;
	
	/**
	 */
	public EGuarantorRequests() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EGuarantorRequests(String code, long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EGuarantorRequests requests) {
		return super.convertToDatabaseColumn(requests);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EGuarantorRequests convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EGuarantorRequests> values() {
		return getValues(EGuarantorRequests.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EGuarantorRequests getByCode(String code) {
		return getByCode(EGuarantorRequests.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EGuarantorRequests getById(long id) {
		return getById(EGuarantorRequests.class, id);
	}

	/**
	 * @return the category
	 */
	public EGuarantorRequestsCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EGuarantorRequestsCategory category) {
		this.category = category;
	}

}
