package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author ly.youhort
 *
 */

public class EChargePoint extends BaseERefData implements AttributeConverter<EChargePoint, Long> {
	
	/**
	 */
	private static final long serialVersionUID = 3321655070882421001L;
	
	public final static EChargePoint PRI_ASS = new EChargePoint("PRI_ASS", 1);
	public final static EChargePoint MONTH_END = new EChargePoint("MONTH_END", 2);
	
	/**
	 */
	public EChargePoint() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EChargePoint(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EChargePoint convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EChargePoint arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * @return
	 */
	public static List<EChargePoint> values() {
		return getValues(EChargePoint.class);
	}

	/**
	 * @param code
	 * @return
	 */
	public static EChargePoint getByCode(String code) {
		return getByCode(EChargePoint.class, code);
	}
	
	/** 
	 * @param id
	 * @return
	 */
	public static EChargePoint getById(long id) {
		return getById(EChargePoint.class, id);
	}
}