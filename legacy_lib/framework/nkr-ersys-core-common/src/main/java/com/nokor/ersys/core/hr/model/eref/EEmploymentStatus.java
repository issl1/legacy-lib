package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment status
 * 
 * @author ly.youhort
 */
public class EEmploymentStatus extends BaseERefData implements AttributeConverter<EEmploymentStatus, Long> {
	/** */
	private static final long serialVersionUID = -7975283856613461134L;

	/**
	 * 
	 */
	public EEmploymentStatus() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEmploymentStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentStatus> values() {
		return getValues(EEmploymentStatus.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEmploymentStatus getByCode(String code) {
		return getByCode(EEmploymentStatus.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentStatus getById(long id) {
		return getById(EEmploymentStatus.class, id);
	}
}
