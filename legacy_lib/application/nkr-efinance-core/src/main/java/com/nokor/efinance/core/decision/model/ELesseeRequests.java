package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class ELesseeRequests extends BaseERefData implements AttributeConverter<ELesseeRequests, Long> {
	
	/** */
	private static final long serialVersionUID = 6198092829908651894L;
	
	private ERequestsCategory category;
	
	/**
	 */
	public ELesseeRequests() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public ELesseeRequests(String code, long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ELesseeRequests requests) {
		return super.convertToDatabaseColumn(requests);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ELesseeRequests convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<ELesseeRequests> values() {
		return getValues(ELesseeRequests.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELesseeRequests getByCode(String code) {
		return getByCode(ELesseeRequests.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELesseeRequests getById(long id) {
		return getById(ELesseeRequests.class, id);
	}

	/**
	 * @return the category
	 */
	public ERequestsCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ERequestsCategory category) {
		this.category = category;
	}

}
