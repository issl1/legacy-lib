package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class ELesseeFlawsCategory extends BaseERefData implements AttributeConverter<ELesseeFlawsCategory, Long> {
	
	/** */
	private static final long serialVersionUID = 2004206181867456868L;

	/**
	 */
	public ELesseeFlawsCategory() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public ELesseeFlawsCategory(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ELesseeFlawsCategory category) {
		return super.convertToDatabaseColumn(category);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ELesseeFlawsCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<ELesseeFlawsCategory> values() {
		return getValues(ELesseeFlawsCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELesseeFlawsCategory getByCode(String code) {
		return getByCode(ELesseeFlawsCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELesseeFlawsCategory getById(long id) {
		return getById(ELesseeFlawsCategory.class, id);
	}

}
