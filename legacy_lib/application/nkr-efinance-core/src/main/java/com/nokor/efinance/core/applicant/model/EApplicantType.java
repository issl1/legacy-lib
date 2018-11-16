package com.nokor.efinance.core.applicant.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Applicant type
 * 
 * @author youhort.ly
 */
public class EApplicantType extends BaseERefData implements AttributeConverter<EApplicantType, Long> {
	/** */
	private static final long serialVersionUID = 5017099510429618323L;

	public final static EApplicantType C = new EApplicantType("C", 1); //customer
	public final static EApplicantType G = new EApplicantType("G", 2); // 	
	public final static EApplicantType O = new EApplicantType("O", 3); // old.guarantor
	public final static EApplicantType S = new EApplicantType("S", 4); //
	public final static EApplicantType OLD_CUS = new EApplicantType("OLD_CUS", 5); // old customer
	public final static EApplicantType R = new EApplicantType("R", 6); // reference


	/**
	 * 
	 */
	public EApplicantType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EApplicantType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EApplicantType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EApplicantType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EApplicantType> values() {
		List<EApplicantType> values = new ArrayList<>();
		values.add(EApplicantType.C);
		values.add(EApplicantType.G);
		return values;
		// return getValues(EApplicantType.class);
		
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EApplicantType getByCode(String code) {
		return getByCode(EApplicantType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EApplicantType getById(long id) {
		return getById(EApplicantType.class, id);
	}
}
