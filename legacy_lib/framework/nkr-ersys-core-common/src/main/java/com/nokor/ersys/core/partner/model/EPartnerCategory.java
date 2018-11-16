package com.nokor.ersys.core.partner.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EPartnerCategory extends BaseERefData implements AttributeConverter<EPartnerCategory, Long> {
	/** */
	private static final long serialVersionUID = -8287670996363962017L;

	public final static EPartnerType COMPANY = new EPartnerType("COMPANY", 1);
	public final static EPartnerType ASSOCIATION = new EPartnerType("ASSOCIATION", 2);
	public final static EPartnerType INDIVIDUAL = new EPartnerType("INDIVIDUAL", 3);

	/**
	 */
	public EPartnerCategory() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPartnerCategory(String code, long id) {
		super(code, id);
	}

	@Override
	public EPartnerCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPartnerCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPartnerCategory> values() {
		return getValues(EPartnerCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPartnerCategory getByCode(String code) {
		return getByCode(EPartnerCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPartnerCategory getById(long id) {
		return getById(EPartnerCategory.class, id);
	}
}
