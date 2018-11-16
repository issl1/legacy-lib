package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Residence type in address
 * @author uhout.cheng
 */
public class EResidenceType extends BaseERefData implements AttributeConverter<EResidenceType, Long> {
	
	/** */
	private static final long serialVersionUID = 6186443060986133469L;
		
	/** */
	public EResidenceType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EResidenceType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EResidenceType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EResidenceType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EResidenceType> values() {
		return getValues(EResidenceType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EResidenceType getByCode(String code) {
		return getByCode(EResidenceType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EResidenceType getById(long id) {
		return getById(EResidenceType.class, id);
	}
}