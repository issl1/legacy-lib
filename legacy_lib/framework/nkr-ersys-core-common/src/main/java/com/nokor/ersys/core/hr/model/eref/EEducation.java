package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Education 
 * @author ly.youhort
 */
public class EEducation extends BaseERefData implements AttributeConverter<EEducation, Long> {
	
	/** */
	private static final long serialVersionUID = -1353795686846272112L;

	/**
	 */
	public EEducation() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EEducation(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEducation convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEducation arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EEducation> values() {
		return getValues(EEducation.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EEducation getByCode(String code) {
		return getByCode(EEducation.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EEducation getById(long id) {
		return getById(EEducation.class, id);
	}
}
