package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EContactPerson extends BaseERefData implements AttributeConverter<EContactPerson, Long> {
	
	/**
	 */
	private static final long serialVersionUID = -2563760474638635425L;
	
	public static final EContactPerson OTHER =  new EContactPerson("007", 7);

	/**
	 */
	public EContactPerson() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EContactPerson(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EContactPerson convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EContactPerson arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EContactPerson> values() {
		return getValues(EContactPerson.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EContactPerson getByCode(String code) {
		return getByCode(EContactPerson.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EContactPerson getById(long id) {
		return getById(EContactPerson.class, id);
	}

}