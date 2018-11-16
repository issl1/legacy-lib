package com.nokor.ersys.core.hr.model.eref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EEmploymentActivity extends BaseERefData implements AttributeConverter<EEmploymentActivity, Long> {
	/** */
	private static final long serialVersionUID = 882988222058332824L;

	private EEmploymentIndustryCategory category;
	
	/**
	 * 
	 */
	public EEmploymentActivity() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEmploymentActivity(String code, long id, EEmploymentIndustryCategory category) {
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
	public EEmploymentActivity convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEmploymentActivity arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentActivity> values() {
		return getValues(EEmploymentActivity.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EEmploymentActivity> values(EEmploymentIndustryCategory category) {
		List<EEmploymentActivity> lstEmploymentIndustries = EEmploymentActivity.values();
		
		List<EEmploymentActivity> lstResEmploymentIndustries = new ArrayList<EEmploymentActivity>();
		for (EEmploymentActivity employmentIndustry : lstEmploymentIndustries) {
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
	public static EEmploymentActivity getByCode(String code) {
		return getByCode(EEmploymentActivity.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEmploymentActivity getById(long id) {
		return getById(EEmploymentActivity.class, id);
	}
}
