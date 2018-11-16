package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class EGuarantorFlaws extends BaseERefData implements AttributeConverter<EGuarantorFlaws, Long> {
	/** */
	private static final long serialVersionUID = -4704135019387660678L;

	private EGuarantorFlawsCategory category;

	/**
	 */
	public EGuarantorFlaws() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EGuarantorFlaws(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EGuarantorFlaws guarantorFlaws) {
		return super.convertToDatabaseColumn(guarantorFlaws);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EGuarantorFlaws convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EGuarantorFlaws> values() {
		return getValues(EGuarantorFlaws.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EGuarantorFlaws getByCode(String code) {
		return getByCode(EGuarantorFlaws.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EGuarantorFlaws getById(long id) {
		return getById(EGuarantorFlaws.class, id);
	}
	

	/**
	 * @return the category
	 */
	public EGuarantorFlawsCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EGuarantorFlawsCategory category) {
		this.category = category;
	}

}
