package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;
/**
 * 
 * @author buntha.chea
 *
 */
public class ERoundingFormat extends BaseERefData implements AttributeConverter<ERoundingFormat, Long> {
	/** */
	private static final long serialVersionUID = -7436143354810334980L;

	public final static ERoundingFormat UPPER_1_DECIMAL = new ERoundingFormat("UPPER1DECIMAL", 1l);
	public final static ERoundingFormat UPPER_2_DECIMALS = new ERoundingFormat("UPPER2DECIMALS", 2l);
	public final static ERoundingFormat UPPER_1_UNIT = new ERoundingFormat("UPPER1UNIT", 3l);
	public final static ERoundingFormat UPPER_5_UNITS = new ERoundingFormat("UPPER5UNITS", 4l);
	public final static ERoundingFormat UPPER_10_UNITS = new ERoundingFormat("UPPER10UNITS", 5l);

	/**
	 * 
	 */
	public ERoundingFormat() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ERoundingFormat(String code, long id) {
		super(code, id);
	}
	
	@Override
	public ERoundingFormat convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ERoundingFormat arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERoundingFormat> values() {
		return getValues(ERoundingFormat.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ERoundingFormat getByCode(String code) {
		return getByCode(ERoundingFormat.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ERoundingFormat getById(long id) {
		return getById(ERoundingFormat.class, id);
	}

}
