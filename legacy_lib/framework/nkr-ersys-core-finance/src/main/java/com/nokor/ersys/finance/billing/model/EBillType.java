package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EBillType extends BaseERefData implements AttributeConverter<EBillType, Long> {
	/** */
	private static final long serialVersionUID = -5010184739530469877L;

	public final static EBillType QUOTATION = new EBillType("QUOTATION", 1);
	public final static EBillType INVOICE = new EBillType("INVOICE", 2);
	public final static EBillType ORDER = new EBillType("ORDER", 3);
	public final static EBillType CREDIT_NOTE = new EBillType("CREDIT_NOTE", 4);

	/**
	 */
	public EBillType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EBillType(String code, long id) {
		super(code, id);
	}

	@Override
	public EBillType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EBillType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EBillType> values() {
		return getValues(EBillType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EBillType getByCode(String code) {
		return getByCode(EBillType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EBillType getById(long id) {
		return getById(EBillType.class, id);
	}
}
