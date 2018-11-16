package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Religion
 * 
 * @author ly.youhort
 */
public class EReligion extends BaseERefData implements AttributeConverter<EReligion, Long> {
	
	/** */
	private static final long serialVersionUID = -1353795686846272112L;

	/**
	 */
	public EReligion() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EReligion(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EReligion convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EReligion arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EReligion> values() {
		return getValues(EReligion.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EReligion getByCode(String code) {
		return getByCode(EReligion.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EReligion getById(long id) {
		return getById(EReligion.class, id);
	}
}
