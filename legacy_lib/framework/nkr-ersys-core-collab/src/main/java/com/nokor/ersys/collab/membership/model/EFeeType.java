package com.nokor.ersys.collab.membership.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EFeeType extends BaseERefData implements AttributeConverter<EFeeType, Long> {
	/** */
	private static final long serialVersionUID = 4351409262717176566L;

	public final static EFeeType PRICING_VARIES = new EFeeType("PRICING_VARIES", 1);
	public final static EFeeType PER_PRICING = new EFeeType("PER_PRICING", 2);
	public final static EFeeType SINGLE_PRICING = new EFeeType("SINGLE_PRICING", 3);
	public final static EFeeType NO_PRICE = new EFeeType("NO_PRICE", 4);

	/**
	 */
	public EFeeType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EFeeType(String code, long id) {
		super(code, id);
	}

	@Override
	public EFeeType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFeeType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EFeeType> values() {
		return getValues(EFeeType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EFeeType getByCode(String code) {
		return getByCode(EFeeType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EFeeType getById(long id) {
		return getById(EFeeType.class, id);
	}
}
