package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EPromiseType extends BaseERefData implements AttributeConverter<EPromiseType, Long> {
	
	/** */
	private static final long serialVersionUID = -7226366422413070194L;
	
	public static final EPromiseType PROMISEPAY =  new EPromiseType("001", 1l);

	/**
	 */
	public EPromiseType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPromiseType(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EPromiseType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EPromiseType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPromiseType> values() {
		return getValues(EPromiseType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPromiseType getByCode(String code) {
		return getByCode(EPromiseType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPromiseType getById(long id) {
		return getById(EPromiseType.class, id);
	}

}