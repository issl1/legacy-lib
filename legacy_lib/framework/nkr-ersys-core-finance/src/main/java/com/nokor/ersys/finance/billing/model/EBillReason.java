package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EBillReason extends BaseERefData implements AttributeConverter<EBillReason, Long> {
	/** */
	private static final long serialVersionUID = 1026407115308645700L;

	public final static EBillReason PROMOTION = new EBillReason("PROMOTION", 1, EBillType.CREDIT_NOTE);
	public final static EBillReason REIMBURSEMENT = new EBillReason("REIMBURSEMENT", 2, EBillType.CREDIT_NOTE);
	public final static EBillReason INVOICE_CANCELLATION = new EBillReason("INVOICE_CANCELLATION", 3, EBillType.CREDIT_NOTE);

	// - The CreditNote is a simple correction of Invoice, it is a not disbursement(cashout)/payment
	// - The CreditNote is a disbursement (cashout) when a Payment is already created

	private EBillType type;

	/**
	 */
	public EBillReason() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EBillReason(String code, long id, EBillType type) {
		super(code, id);
		this.type = type;
	}

	@Override
	public EBillReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EBillReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EBillReason> values() {
		return getValues(EBillReason.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EBillReason getByCode(String code) {
		return getByCode(EBillReason.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EBillReason getById(long id) {
		return getById(EBillReason.class, id);
	}

	/**
	 * @return the type
	 */
	public EBillType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EBillType type) {
		this.type = type;
	}
	
	
}
