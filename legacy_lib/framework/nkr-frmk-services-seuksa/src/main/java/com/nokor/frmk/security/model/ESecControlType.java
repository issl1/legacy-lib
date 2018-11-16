package com.nokor.frmk.security.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author phirun.kong
 *
 */
public class ESecControlType extends BaseERefData implements AttributeConverter<ESecControlType, Long> {

	/**	 */
	private static final long serialVersionUID = 6986121186341001803L;
	
	public final static ESecControlType MENU = new ESecControlType("MENU", 1);
	public final static ESecControlType PAGE = new ESecControlType("PAGE", 2);
	public final static ESecControlType FORM = new ESecControlType("FORM", 3);
	public final static ESecControlType ACTION = new ESecControlType("ACTION", 4);
	public final static ESecControlType OTHER = new ESecControlType("OTHER", 5);

	/**
	 * 
	 */
	public ESecControlType() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecControlType(String code, long id) {
		super(code, id);
	}

	@Override
	public Long convertToDatabaseColumn(ESecControlType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	@Override
	public ESecControlType convertToEntityAttribute(Long arg0) {
		return super.convertToEntityAttribute(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESecControlType> values() {
		return getValues(ESecControlType.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecControlType getById(long id) {
		return getById(ESecControlType.class, id);
	}

}
