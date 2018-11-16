package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class ELockSplitCategory extends BaseERefData implements AttributeConverter<ELockSplitCategory, Long> {
		
	/** */
	private static final long serialVersionUID = -8312207895487281209L;
	
	public final static ELockSplitCategory DUE = new ELockSplitCategory("DUE", 1l);
	public final static ELockSplitCategory OPERATION = new ELockSplitCategory("OPERATION", 1l);
	
	/**
	 * 
	 */
	public ELockSplitCategory() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ELockSplitCategory(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public ELockSplitCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ELockSplitCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ELockSplitCategory> values() {
		return getValues(ELockSplitCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELockSplitCategory getByCode(String code) {
		return getByCode(ELockSplitCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELockSplitCategory getById(long id) {
		return getById(ELockSplitCategory.class, id);
	}

}