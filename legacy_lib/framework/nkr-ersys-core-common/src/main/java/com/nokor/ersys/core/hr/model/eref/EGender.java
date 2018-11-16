package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EGender extends BaseERefData implements AttributeConverter<EGender, Long> {
	
	/** */
	private static final long serialVersionUID = -4265415309368336229L;
	
	public final static EGender M = new EGender("M", 1); 
	public final static EGender F = new EGender("F", 2); 
	public final static EGender U = new EGender("U", 3); 

	/**
	 * 
	 */
	public EGender() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EGender(String code, long id) {
		super(code, id);
	}

	@Override
	public EGender convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EGender arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EGender> values() {
		return getValues(EGender.class);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EGender getByCode(String code) {
		return getByCode(EGender.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EGender getById(long id) {
		return getById(EGender.class, id);
	}
}