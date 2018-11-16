package com.nokor.efinance.core.financial.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Represents the product line type
 * 
 * @author ly.youhort
 */
public class EProductLineType extends BaseERefData implements AttributeConverter<EProductLineType, Long> {
	/** */
	private static final long serialVersionUID = 4581506670312594888L;

	public final static EProductLineType FNC = new EProductLineType("FNC", 1); // financing

	/**
	 * 
	 */
	public EProductLineType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EProductLineType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EProductLineType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EProductLineType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EProductLineType> values() {
		return getValues(EProductLineType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EProductLineType getByCode(String code) {
		return getByCode(EProductLineType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EProductLineType getById(long id) {
		return getById(EProductLineType.class, id);
	}
}
