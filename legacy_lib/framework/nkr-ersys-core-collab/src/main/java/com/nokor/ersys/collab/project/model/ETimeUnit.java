package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETimeUnit extends BaseERefData implements AttributeConverter<ETimeUnit, Long> {
	/** */
	private static final long serialVersionUID = 646060694004735307L;

	public final static ETimeUnit DAY = new ETimeUnit("DAY", 1);
	public final static ETimeUnit WEEK = new ETimeUnit("WEEK", 2);
	public final static ETimeUnit MONTH = new ETimeUnit("MONTH", 3);
	

	/**
	 * 
	 */
	public ETimeUnit() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETimeUnit(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ETimeUnit convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETimeUnit arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETimeUnit> values() {
		return getValues(ETimeUnit.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETimeUnit getById(long id) {
		return getById(ETimeUnit.class, id);
	}
}