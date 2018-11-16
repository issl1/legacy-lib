package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EUnit extends BaseERefData implements AttributeConverter<EUnit, Long> {
	/** */
	private static final long serialVersionUID = 2531763498429974745L;

	public final static EUnit DEFAULT = new EUnit("DEFAULT", 1);
	public final static EUnit PIECE = new EUnit("PIECE", 2);
	public final static EUnit MONTH = new EUnit("MONTH", 3);

	/**
	 */
	public EUnit() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EUnit(String code, long id) {
		super(code, id);
	}

	@Override
	public EUnit convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EUnit arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EUnit> values() {
		return getValues(EUnit.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EUnit getByCode(String code) {
		return getByCode(EUnit.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EUnit getById(long id) {
		return getById(EUnit.class, id);
	}
}
