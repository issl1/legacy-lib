package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EColType extends BaseERefData implements AttributeConverter<EColType, Long> {
	
	/** */
	private static final long serialVersionUID = 1926023334544670059L;

	public final static EColType PHONE = new EColType("PHONE", 1); 
	public final static EColType FIELD = new EColType("FIELD", 2); 
	public final static EColType INSIDE_REPO = new EColType("INSIDE_REPO", 3);
	public final static EColType OA = new EColType("OA", 4);
	public final static EColType REQUEST = new EColType("REQUEST", 5);
	public final static EColType MKT = new EColType("MKT", 6);
	public final static EColType CALL = new EColType("CALL", 7);
	
	/**
	 */
	public EColType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EColType(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EColType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EColType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EColType> values() {
		return getValues(EColType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EColType getByCode(String code) {
		return getByCode(EColType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EColType getById(long id) {
		return getById(EColType.class, id);
	}
}