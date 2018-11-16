package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Color
 * 
 * @author ly.youhort
 */
public class EColor extends BaseERefData implements AttributeConverter<EColor, Long> {
	/** */
	private static final long serialVersionUID = 3408947621943222035L;

	
	/**
	 * 
	 */
	public EColor() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EColor(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EColor convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EColor arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EColor> values() {
		return getValues(EColor.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EColor getByCode(String code) {
		return getByCode(EColor.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EColor getById(long id) {
		return getById(EColor.class, id);
	}
}
