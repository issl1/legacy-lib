package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EJobCategory extends BaseERefData implements AttributeConverter<EJobCategory, Long> {
	/** */
	private static final long serialVersionUID = 7311204264679068408L;

	public final static EJobCategory DEFAULT = new EJobCategory("DEFAULT", 1);

	/**
	 * 
	 */
	public EJobCategory() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EJobCategory(String code, long id) {
		super(code, id);
	}

	@Override
	public EJobCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EJobCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EJobCategory> values() {
		return getValues(EJobCategory.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EJobCategory getById(long id) {
		return getById(EJobCategory.class, id);
	}
}