package com.nokor.ersys.core.partner.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EPartnerType extends BaseERefData implements AttributeConverter<EPartnerType, Long> {
	/** */
	private static final long serialVersionUID = -535677364108233708L;
	
	public final static EPartnerType DEFAULT = new EPartnerType("DEFAULT", 1);
	public final static EPartnerType PARTNERSHIP = new EPartnerType("PARTNERSHIP", 2);

	/**
	 */
	public EPartnerType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPartnerType(String code, long id) {
		super(code, id);
	}

	@Override
	public EPartnerType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPartnerType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPartnerType> values() {
		return getValues(EPartnerType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPartnerType getByCode(String code) {
		return getByCode(EPartnerType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPartnerType getById(long id) {
		return getById(EPartnerType.class, id);
	}
}
