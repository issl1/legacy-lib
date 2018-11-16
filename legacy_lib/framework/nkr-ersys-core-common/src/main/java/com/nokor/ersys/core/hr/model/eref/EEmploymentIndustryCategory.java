package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class EEmploymentIndustryCategory extends BaseERefData implements AttributeConverter<EEmploymentIndustryCategory, Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -258462865793171499L;

	/**
	 * 
	 */
	public EEmploymentIndustryCategory() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentIndustryCategory(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EEmploymentIndustryCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentIndustryCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentIndustryCategory> values() {
		return getValues(EEmploymentIndustryCategory.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEmploymentIndustryCategory getByCode(String code) {
		return getByCode(EEmploymentIndustryCategory.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentIndustryCategory getById(long id) {
		return getById(EEmploymentIndustryCategory.class, id);
	}

}
