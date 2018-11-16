package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Payment file format
 * 
 * @author ly.youhort
 *
 */

public class EPaymentFileFormat extends BaseERefData implements AttributeConverter<EPaymentFileFormat, Long> {
	/** */
	private static final long serialVersionUID = 8422326209247893704L;

	public final static EPaymentFileFormat BAY = new EPaymentFileFormat("BAY", 1);
	public final static EPaymentFileFormat SCB = new EPaymentFileFormat("SCB", 2);
	public final static EPaymentFileFormat SCIB = new EPaymentFileFormat("SCIB", 3);
	public final static EPaymentFileFormat PAP = new EPaymentFileFormat("PAP", 4);
	public final static EPaymentFileFormat CSV = new EPaymentFileFormat("CSV", 5);
	public final static EPaymentFileFormat GSB = new EPaymentFileFormat("GSB", 6);
	public final static EPaymentFileFormat LOT = new EPaymentFileFormat("LOT", 7);
	public final static EPaymentFileFormat TMN = new EPaymentFileFormat("TMN", 8);
	public final static EPaymentFileFormat KBK = new EPaymentFileFormat("KBK", 9);
	public final static EPaymentFileFormat BAA = new EPaymentFileFormat("BAA", 10);

	/**
	 * 
	 */
	public EPaymentFileFormat() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPaymentFileFormat(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPaymentFileFormat convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentFileFormat arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EPaymentFileFormat> values() {
		return getValues(EPaymentFileFormat.class);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EPaymentFileFormat getByCode(String code) {
		return getByCode(EPaymentFileFormat.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EPaymentFileFormat getById(long id) {
		return getById(EPaymentFileFormat.class, id);
	}
}