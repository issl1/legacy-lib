package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Registration book status of vehicle
 * @author uhout.cheng
 */
public class ERegBookStatus extends BaseERefData implements AttributeConverter<ERegBookStatus, Long> {
	
	/** */
	private static final long serialVersionUID = -8892261644870787790L;

	/** */
	public ERegBookStatus() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ERegBookStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public ERegBookStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ERegBookStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERegBookStatus> values() {
		return getValues(ERegBookStatus.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERegBookStatus getByCode(String code) {
		return getByCode(ERegBookStatus.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ERegBookStatus getById(long id) {
		return getById(ERegBookStatus.class, id);
	}
}
