package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Title for applicant
 * @author uhout.cheng
 */
public class ETitle extends BaseERefData implements AttributeConverter<ETitle, Long> {
	
	/** */
	private static final long serialVersionUID = -5672943036063709167L;

	/** */
	public ETitle() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ETitle(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public ETitle convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ETitle arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETitle> values() {
		return getValues(ETitle.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ETitle getByCode(String code) {
		return getByCode(ETitle.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ETitle getById(long id) {
		return getById(ETitle.class, id);
	}
}
