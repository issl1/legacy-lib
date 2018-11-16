package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class EDecisionCategory extends BaseERefData implements AttributeConverter<EDecisionCategory, Long> {
	/** */
	private static final long serialVersionUID = -538128383680576830L;
	
	/**
	 */
	public EDecisionCategory() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EDecisionCategory(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EDecisionCategory category) {
		return super.convertToDatabaseColumn(category);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EDecisionCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EDecisionCategory> values() {
		return getValues(EDecisionCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDecisionCategory getByCode(String code) {
		return getByCode(EDecisionCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDecisionCategory getById(long id) {
		return getById(EDecisionCategory.class, id);
	}

}
