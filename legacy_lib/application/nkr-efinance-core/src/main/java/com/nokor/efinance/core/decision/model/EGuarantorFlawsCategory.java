package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EGuarantorFlawsCategory extends BaseERefData implements AttributeConverter<EGuarantorFlawsCategory, Long> {
	
	/** */
	private static final long serialVersionUID = -5471835632386516204L;

	/**
	 */
	public EGuarantorFlawsCategory() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EGuarantorFlawsCategory(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EGuarantorFlawsCategory category) {
		return super.convertToDatabaseColumn(category);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EGuarantorFlawsCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EGuarantorFlawsCategory> values() {
		return getValues(EGuarantorFlawsCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EGuarantorFlawsCategory getByCode(String code) {
		return getByCode(EGuarantorFlawsCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EGuarantorFlawsCategory getById(long id) {
		return getById(EGuarantorFlawsCategory.class, id);
	}

}
