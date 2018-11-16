package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment status
 * 
 * @author ly.youhort
 */
public class EEmploymentCategory extends BaseERefData implements AttributeConverter<EEmploymentCategory, Long> {
	/** */
	private static final long serialVersionUID = -7975283856613461134L;

	/**
	 * 
	 */
	public EEmploymentCategory() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentCategory(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEmploymentCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentCategory> values() {
		return getValues(EEmploymentCategory.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEmploymentCategory getByCode(String code) {
		return getByCode(EEmploymentCategory.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentCategory getById(long id) {
		return getById(EEmploymentCategory.class, id);
	}
}
