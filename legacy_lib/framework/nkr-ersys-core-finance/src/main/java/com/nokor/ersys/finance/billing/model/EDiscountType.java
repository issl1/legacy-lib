package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EDiscountType extends BaseERefData implements AttributeConverter<EDiscountType, Long> {
	/** */
	private static final long serialVersionUID = -5137295082325186724L;
	
	public final static EDiscountType RATE = new EDiscountType("RATE", 1);
	public final static EDiscountType AMOUNT = new EDiscountType("AMOUNT", 2);

	/**
	 */
	public EDiscountType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EDiscountType(String code, long id) {
		super(code, id);
	}

	@Override
	public EDiscountType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDiscountType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EDiscountType> values() {
		return getValues(EDiscountType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDiscountType getByCode(String code) {
		return getByCode(EDiscountType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDiscountType getById(long id) {
		return getById(EDiscountType.class, id);
	}
}
