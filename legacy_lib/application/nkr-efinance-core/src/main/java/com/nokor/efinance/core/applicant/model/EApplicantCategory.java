package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;
/**
 * 
 * @author buntha.chea
 *
 */
public class EApplicantCategory extends BaseERefData implements AttributeConverter<EApplicantCategory, Long> {
	/** */
	private static final long serialVersionUID = -1972052245638249314L;

	public final static EApplicantCategory INDIVIDUAL = new EApplicantCategory("IND", 1l); 
	public final static EApplicantCategory COMPANY = new EApplicantCategory("COM", 2l); 	
	public final static EApplicantCategory GLSTAFF = new EApplicantCategory("GLS", 3l);

	/**
	 * 
	 */
	public EApplicantCategory() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EApplicantCategory(String code, long id) {
		super(code, id);
	}
	
	@Override
	public EApplicantCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EApplicantCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EApplicantCategory> values() {
		return getValues(EApplicantCategory.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EApplicantCategory getByCode(String code) {
		return getByCode(EApplicantCategory.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EApplicantCategory getById(long id) {
		return getById(EApplicantCategory.class, id);
	}

}
