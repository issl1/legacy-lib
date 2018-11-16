package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Relationship
 * 
 * @author ly.youhort
 */
public class ERelationship extends BaseERefData implements AttributeConverter<ERelationship, Long> {
	/** */
	private static final long serialVersionUID = -7553631143293826132L;

	/**
	 * 
	 */
	public ERelationship() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ERelationship(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ERelationship convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ERelationship arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERelationship> values() {
		return getValues(ERelationship.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ERelationship getByCode(String code) {
		return getByCode(ERelationship.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ERelationship getById(long id) {
		return getById(ERelationship.class, id);
	}
}
