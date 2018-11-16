package com.nokor.frmk.config.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.SimpleERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ERefType extends SimpleERefData implements AttributeConverter<ERefType, Long> {
	/** */
	private static final long serialVersionUID = -6476979802191472147L;

	public static final ERefType REFDATA = new ERefType("REFDATA", 1);
	public static final ERefType ENTITY_REF = new ERefType("ENTITY_REF", 2);
	public static final ERefType ENTITY_REFDATA = new ERefType("ENTITY_REFDATA", 3);
	

	/**
	 * 
	 */
	public ERefType() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ERefType(String code, long id) {
		super(code, id);
	}

	@Override
	public ERefType convertToEntityAttribute(Long id) {
		return ((ERefType) super.convertToEntityAttribute(id));
	}
	
	@Override
	public Long convertToDatabaseColumn(ERefType refType) {
		return super.convertToDatabaseColumn(refType);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ERefType getById(long id) {
		return SimpleERefData.getById(ERefType.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ERefType getByCode(String code) {
		return SimpleERefData.getByCode(ERefType.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ERefType valueOf(String code) {
		return SimpleERefData.valueOf(ERefType.class, code);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERefType> values() {
		return SimpleERefData.getValues(ERefType.class);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static  boolean isRefData(ERefType type) {
		return ERefType.REFDATA.equals(type) || type == null;
	}
}
