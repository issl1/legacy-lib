package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Registration plate type of vehicle
 * 
 * @author ly.youhort
 */
public class ERegPlateType extends BaseERefData implements AttributeConverter<ERegPlateType, Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4809561906184207117L;

	/**
	 */
	public ERegPlateType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ERegPlateType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ERegPlateType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ERegPlateType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERegPlateType> values() {
		return getValues(ERegPlateType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERegPlateType getByCode(String code) {
		return getByCode(ERegPlateType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ERegPlateType getById(long id) {
		return getById(ERegPlateType.class, id);
	}
}
