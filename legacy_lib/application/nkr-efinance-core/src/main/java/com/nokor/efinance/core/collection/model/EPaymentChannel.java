package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Payment Channel
 * @author uhout.cheng
 */
public class EPaymentChannel extends BaseERefData implements AttributeConverter<EPaymentChannel, Long> {
	
	public static final EPaymentChannel DEALERCOUNTER = new EPaymentChannel("2", 2l);
	public static final EPaymentChannel KEYSYSTEMBYBILL = new EPaymentChannel("9", 9l);
	public static final EPaymentChannel BAY = new EPaymentChannel("BAY", 22l);
	
	/** */
	private static final long serialVersionUID = 140905107461770835L;

	/**
	 * 
	 */
	public EPaymentChannel() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPaymentChannel(String code, long id) {
		super(code, id);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EPaymentChannel convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EPaymentChannel arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EPaymentChannel> values() {
		return getValues(EPaymentChannel.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPaymentChannel getByCode(String code) {
		return getByCode(EPaymentChannel.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPaymentChannel getById(long id) {
		return getById(EPaymentChannel.class, id);
	}
}
