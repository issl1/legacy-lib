package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EGuarantorRequestsCategory extends BaseERefData implements AttributeConverter<EGuarantorRequestsCategory, Long> {
	
	/** */
	private static final long serialVersionUID = 6307955157376829481L;

	/**
	 */
	public EGuarantorRequestsCategory() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EGuarantorRequestsCategory(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EGuarantorRequestsCategory category) {
		return super.convertToDatabaseColumn(category);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EGuarantorRequestsCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EGuarantorRequestsCategory> values() {
		return getValues(EGuarantorRequestsCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EGuarantorRequestsCategory getByCode(String code) {
		return getByCode(EGuarantorRequestsCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EGuarantorRequestsCategory getById(long id) {
		return getById(EGuarantorRequestsCategory.class, id);
	}

}
