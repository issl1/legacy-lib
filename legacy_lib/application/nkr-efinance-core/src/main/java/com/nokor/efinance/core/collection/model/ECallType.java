package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ECallType extends BaseERefData implements AttributeConverter<ECallType, Long> {
	
	/**
	 */
	private static final long serialVersionUID = 2248425983952407983L;
	
	public static final ECallType SMS =  new ECallType("SMS", 1);
	public static final ECallType CALL =  new ECallType("CALL", 2);
	public static final ECallType FIELD =  new ECallType("FIELD", 3);
	public static final ECallType MAIL =  new ECallType("MAIL", 4);

	/**
	 */
	public ECallType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ECallType(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ECallType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECallType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ECallType> values() {
		return getValues(ECallType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ECallType getByCode(String code) {
		return getByCode(ECallType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ECallType getById(long id) {
		return getById(ECallType.class, id);
	}

}