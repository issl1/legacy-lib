package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Dealer payment type
 * 
 * @author youhort.ly
 */
public class EPaymentFlowType extends BaseERefData implements AttributeConverter<EPaymentFlowType, Long> {
	/** */
	private static final long serialVersionUID = -5713129714458211130L;

	public final static EPaymentFlowType FIN = new EPaymentFlowType("FIN", 1);
	// public final static EPaymentFlowType COM1 = new EPaymentFlowType("COM1", 2);
	// public final static EPaymentFlowType COM2 = new EPaymentFlowType("COM2", 3);
	public final static EPaymentFlowType COM = new EPaymentFlowType("COM", 2);
	public final static EPaymentFlowType LOS = new EPaymentFlowType("LOS", 3);
	public final static EPaymentFlowType AOM = new EPaymentFlowType("AOM", 4);
	
	/**
	 */
	public EPaymentFlowType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPaymentFlowType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPaymentFlowType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentFlowType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPaymentFlowType> values() {
		return getValues(EPaymentFlowType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPaymentFlowType getByCode(String code) {
		return getByCode(EPaymentFlowType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPaymentFlowType getById(long id) {
		return getById(EPaymentFlowType.class, id);
	}
}
