package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EPaymentReason extends BaseERefData implements AttributeConverter<EPaymentReason, Long> {
	/** */
	private static final long serialVersionUID = -3095277427963942088L;

	public final static EPaymentReason DEFAULT = new EPaymentReason("DEFAULT", 1);
	public final static EPaymentReason PURCHASE = new EPaymentReason("PURCHASE", 2);
	public final static EPaymentReason MEMBERSHIP = new EPaymentReason("MEMBERSHIP", 3);
	public final static EPaymentReason REIMBURSEMENT = new EPaymentReason("REIMBURSEMENT", 4);


	/**
	 */
	public EPaymentReason() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPaymentReason(String code, long id) {
		super(code, id);
	}

	@Override
	public EPaymentReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPaymentReason> values() {
		return getValues(EPaymentReason.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPaymentReason getByCode(String code) {
		return getByCode(EPaymentReason.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPaymentReason getById(long id) {
		return getById(EPaymentReason.class, id);
	}
}
