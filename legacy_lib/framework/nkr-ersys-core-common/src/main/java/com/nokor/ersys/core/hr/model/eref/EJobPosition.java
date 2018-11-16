package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EJobPosition extends BaseERefData implements AttributeConverter<EJobPosition, Long> {
	/** */
	private static final long serialVersionUID = -602827446555522898L;
	
	public final static EJobPosition DEFAULT = new EJobPosition("DEFAULT", 1);

	private EJobCategory category;
	
	/**
	 * 
	 */
	public EJobPosition() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EJobPosition(String code, long id) {
		super(code, id);
	}

	@Override
	public EJobPosition convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EJobPosition arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EJobPosition> values() {
		return getValues(EJobPosition.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EJobPosition getById(long id) {
		return getById(EJobPosition.class, id);
	}

	/**
	 * @return the category
	 */
	public EJobCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EJobCategory category) {
		this.category = category;
	}
	
	
}