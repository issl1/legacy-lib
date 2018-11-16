package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Individual reference type
 * 
 * @author youhort.ly
 */
public class EIndividualReferenceType extends BaseERefData implements AttributeConverter<EIndividualReferenceType, Long> {
	
	/**
	 */
	private static final long serialVersionUID = -3732666695175057053L;

	/**
	 */
	public EIndividualReferenceType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EIndividualReferenceType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EIndividualReferenceType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EIndividualReferenceType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EIndividualReferenceType> values() {
		return getValues(EIndividualReferenceType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EIndividualReferenceType getByCode(String code) {
		return getByCode(EIndividualReferenceType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EIndividualReferenceType getById(long id) {
		return getById(EIndividualReferenceType.class, id);
	}
}
