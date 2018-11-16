package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EProjectCategory extends BaseERefData implements AttributeConverter<EProjectCategory, Long> {
	/** */
	private static final long serialVersionUID = 2591289374186261615L;
	
	public final static EProjectCategory DEFAULT = new EProjectCategory("DEFAULT", 1);

	/**
	 * 
	 */
	public EProjectCategory() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EProjectCategory(String code, long id) {
		super(code, id);
	}

	@Override
	public EProjectCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(EProjectCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EProjectCategory> values() {
		return getValues(EProjectCategory.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EProjectCategory getFromId(long id) {
		return getById(EProjectCategory.class, id);
	}
}