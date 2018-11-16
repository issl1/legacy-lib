package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Standard Staffing Level
 * @author phirun.kong
 *
 */
public class EOrgStandardLevel extends BaseERefData implements AttributeConverter<EOrgStandardLevel, Long> {
	
	/**	 */
	private static final long serialVersionUID = 4507489445818091495L;

	/**
	 * 
	 */
	public EOrgStandardLevel() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EOrgStandardLevel(String code, long id) {
		super(code, id);
	}

	@Override
	public EOrgStandardLevel convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EOrgStandardLevel arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EOrgStandardLevel> values() {
		return getValues(EOrgStandardLevel.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EOrgStandardLevel getById(long id) {
		return getById(EOrgStandardLevel.class, id);
	}

}
