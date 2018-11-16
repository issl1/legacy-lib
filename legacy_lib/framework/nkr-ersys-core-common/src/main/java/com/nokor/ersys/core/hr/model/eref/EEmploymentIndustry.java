package com.nokor.ersys.core.hr.model.eref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment Industry
 * 
 * @author ly.youhort
 */
public class EEmploymentIndustry extends BaseERefData implements AttributeConverter<EEmploymentIndustry, Long> {
	/** */
	private static final long serialVersionUID = -7065421640669343002L;

	private EEmploymentIndustryCategory category;
	
	/**
	 * 
	 */
	public EEmploymentIndustry() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentIndustry(String code, long id, EEmploymentIndustryCategory category) {
		super(code, id);
		this.category = category;
	}
	
	/**
	 * @return the category
	 */
	public EEmploymentIndustryCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EEmploymentIndustryCategory category) {
		this.category = category;
	}	

	@SuppressWarnings("unchecked")
	@Override
	public EEmploymentIndustry convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentIndustry arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentIndustry> values() {
		return getValues(EEmploymentIndustry.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentIndustry> values(EEmploymentIndustryCategory category) {
		List<EEmploymentIndustry> lstEmploymentIndustries = EEmploymentIndustry.values();
		
		List<EEmploymentIndustry> lstResEmploymentIndustries = new ArrayList<EEmploymentIndustry>();
		for (EEmploymentIndustry employmentIndustry : lstEmploymentIndustries) {
			if (employmentIndustry.getCategory().equals(category)) {
				lstResEmploymentIndustries.add(employmentIndustry);
			}
		}
		return lstResEmploymentIndustries;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEmploymentIndustry getByCode(String code) {
		return getByCode(EEmploymentIndustry.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentIndustry getById(long id) {
		return getById(EEmploymentIndustry.class, id);
	}
}
