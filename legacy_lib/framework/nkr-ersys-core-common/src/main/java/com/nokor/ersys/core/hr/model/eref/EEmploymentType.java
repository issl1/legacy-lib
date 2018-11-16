package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment type
 * 
 * @author ly.youhort
 *
 */
public class EEmploymentType extends BaseERefData implements AttributeConverter<EEmploymentType, Long> {
	/** */
	private static final long serialVersionUID = -5713129714458211130L;

	public final static EEmploymentType CURR = new EEmploymentType("CURR", 1); // current
	public final static EEmploymentType PREV = new EEmploymentType("PREV", 2); // previous
	public final static EEmploymentType SECO = new EEmploymentType("SECO", 3); // secondary

	/**
	 * 
	 */
	public EEmploymentType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEmploymentType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentType> values() {
		return getValues(EEmploymentType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEmploymentType getByCode(String code) {
		return getByCode(EEmploymentType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentType getById(long id) {
		return getById(EEmploymentType.class, id);
	}
}
