package com.nokor.common.app.menu.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EMenuType extends BaseERefData implements AttributeConverter<EMenuType, Long> {
	/** */
	private static final long serialVersionUID = 7297313286206459830L;

	public final static EMenuType MAIN = new EMenuType("MAIN", 1);

	/**
	 * 
	 */
	public EMenuType() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EMenuType(String code, long id) {
		super(code, id);
	}

	@Override
	public Long convertToDatabaseColumn(EMenuType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	@Override
	public EMenuType convertToEntityAttribute(Long arg0) {
		return super.convertToEntityAttribute(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EMenuType> values() {
		return getValues(EMenuType.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EMenuType getById(long id) {
		return getById(EMenuType.class, id);
	}

}
