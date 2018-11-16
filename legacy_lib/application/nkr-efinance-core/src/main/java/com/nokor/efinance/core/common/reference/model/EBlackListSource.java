package com.nokor.efinance.core.common.reference.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EBlackListSource extends BaseERefData implements AttributeConverter<EBlackListSource, Long> {
	
	/** */
	private static final long serialVersionUID = 8197564057005914130L;

	/**
	 * 
	 */
	public EBlackListSource() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EBlackListSource(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EBlackListSource convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EBlackListSource arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EBlackListSource> values() {
		return getValues(EBlackListSource.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EBlackListSource getByCode(String code) {
		return getByCode(EBlackListSource.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EBlackListSource getById(long id) {
		return getById(EBlackListSource.class, id);
	}
}
