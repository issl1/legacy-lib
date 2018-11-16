package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETaskTemplateCategory extends BaseERefData implements AttributeConverter<ETaskTemplateCategory, Long> {
	/** */
	private static final long serialVersionUID = 5177487963175079530L;
	
	public final static ETaskTemplateCategory DEFAULT = new ETaskTemplateCategory("DEFAULT", 1);

	/**
	 * 
	 */
	public ETaskTemplateCategory() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETaskTemplateCategory(String code, long id) {
		super(code, id);
	}

	@Override
	public ETaskTemplateCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETaskTemplateCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETaskTemplateCategory> values() {
		return getValues(ETaskTemplateCategory.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETaskTemplateCategory getById(long id) {
		return getById(ETaskTemplateCategory.class, id);
	}
}