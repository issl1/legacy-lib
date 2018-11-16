package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author ly.youhort
 *
 */
public class ECategoryPaymentMethod extends BaseERefData implements AttributeConverter<ECategoryPaymentMethod, Long> {
	/** */
	private static final long serialVersionUID = 1274373216564561019L;

	public final static ECategoryPaymentMethod AUT = new ECategoryPaymentMethod("AUT", 1); // automatic
	public final static ECategoryPaymentMethod MAN = new ECategoryPaymentMethod("MAN", 2); // manual
	public final static ECategoryPaymentMethod SYS = new ECategoryPaymentMethod("SYS", 3); // system

	/**
	 * 
	 */
	public ECategoryPaymentMethod() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECategoryPaymentMethod(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECategoryPaymentMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECategoryPaymentMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECategoryPaymentMethod> values() {
		return getValues(ECategoryPaymentMethod.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECategoryPaymentMethod getByCode(String code) {
		return getByCode(ECategoryPaymentMethod.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECategoryPaymentMethod getById(long id) {
		return getById(ECategoryPaymentMethod.class, id);
	}
}
