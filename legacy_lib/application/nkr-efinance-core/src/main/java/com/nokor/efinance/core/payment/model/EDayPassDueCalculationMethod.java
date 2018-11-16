package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author ly.youhort
 *
 */
public class EDayPassDueCalculationMethod extends BaseERefData implements AttributeConverter<EDayPassDueCalculationMethod, Long> {
	
	/** 
	 */
	private static final long serialVersionUID = -2270010384401316555L;
	
	public final static EDayPassDueCalculationMethod INCLUDEHOLIDAYS = new EDayPassDueCalculationMethod("INCLUDEHOLIDAYS", 1);
	public final static EDayPassDueCalculationMethod EXCLUDEHOLIDAYS = new EDayPassDueCalculationMethod("EXCLUDEHOLIDAYS", 2);	

	/**
	 */
	public EDayPassDueCalculationMethod() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EDayPassDueCalculationMethod(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EDayPassDueCalculationMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDayPassDueCalculationMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EDayPassDueCalculationMethod> values() {
		return getValues(EDayPassDueCalculationMethod.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDayPassDueCalculationMethod getByCode(String code) {
		return getByCode(EDayPassDueCalculationMethod.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDayPassDueCalculationMethod getById(long id) {
		return getById(EDayPassDueCalculationMethod.class, id);
	}
}
