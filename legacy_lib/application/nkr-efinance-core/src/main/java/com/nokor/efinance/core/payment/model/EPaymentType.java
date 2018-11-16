package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Payment type
 * 
 * @author ly.youhort
 *
 */

public class EPaymentType extends BaseERefData implements AttributeConverter<EPaymentType, Long> {
	/** */
	private static final long serialVersionUID = 8422326209247893704L;

	public final static EPaymentType ORC = new EPaymentType("ORC", 1);	// Down Payment (Pay the dealer for the financing amount: asset price - advance payment)
	public final static EPaymentType IRC = new EPaymentType("IRC", 2);  // Installment  (Every month)
	public final static EPaymentType DCO = new EPaymentType("DCO", 3); 	// Direct Cost  (Cash out other than Down payment: Commissions, Insurance..)

	/**
	 * 
	 */
	public EPaymentType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPaymentType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPaymentType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EPaymentType> values() {
		return getValues(EPaymentType.class);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EPaymentType getByCode(String code) {
		return getByCode(EPaymentType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EPaymentType getById(long id) {
		return getById(EPaymentType.class, id);
	}
}