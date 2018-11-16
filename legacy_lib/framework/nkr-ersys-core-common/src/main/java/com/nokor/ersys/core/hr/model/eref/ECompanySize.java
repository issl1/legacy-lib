package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment status
 * 
 * @author ly.youhort
 */
public class ECompanySize extends BaseERefData implements AttributeConverter<ECompanySize, Long> {
	/** */
	private static final long serialVersionUID = -7975283856613461134L;

	/**
	 */
	public ECompanySize() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECompanySize(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECompanySize convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECompanySize arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECompanySize> values() {
		return getValues(ECompanySize.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECompanySize getByCode(String code) {
		return getByCode(ECompanySize.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECompanySize getById(long id) {
		return getById(ECompanySize.class, id);
	}
}
