package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class ELesseeFlaws extends BaseERefData implements AttributeConverter<ELesseeFlaws, Long> {
	
	/** */
	private static final long serialVersionUID = 6373944273269604733L;

	private ELesseeFlawsCategory category;
	
	/**
	 */
	public ELesseeFlaws() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ELesseeFlaws(String code, long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ELesseeFlaws lesseeFlaws) {
		return super.convertToDatabaseColumn(lesseeFlaws);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ELesseeFlaws convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<ELesseeFlaws> values() {
		return getValues(ELesseeFlaws.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELesseeFlaws getByCode(String code) {
		return getByCode(ELesseeFlaws.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELesseeFlaws getById(long id) {
		return getById(ELesseeFlaws.class, id);
	}

	/**
	 * @return the category
	 */
	public ELesseeFlawsCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ELesseeFlawsCategory category) {
		this.category = category;
	}

}
