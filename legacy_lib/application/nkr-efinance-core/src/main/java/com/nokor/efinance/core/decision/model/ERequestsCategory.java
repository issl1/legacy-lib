package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class ERequestsCategory extends BaseERefData implements AttributeConverter<ERequestsCategory, Long> {
	/** */
	private static final long serialVersionUID = 1491694925045202233L;
	
	/**
	 */
	public ERequestsCategory() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public ERequestsCategory(String code, long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ERequestsCategory category) {
		return super.convertToDatabaseColumn(category);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public ERequestsCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<ERequestsCategory> values() {
		return getValues(ERequestsCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERequestsCategory getByCode(String code) {
		return getByCode(ERequestsCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ERequestsCategory getById(long id) {
		return getById(ERequestsCategory.class, id);
	}

}
