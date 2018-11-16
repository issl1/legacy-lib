package com.nokor.efinance.core.workflow.model.history;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Process type
 * @author ly.youhort
 *
 */
public class EProcessType extends BaseERefData implements AttributeConverter<EProcessType, Long> {
	/** */
	private static final long serialVersionUID = 7307660635616756673L;

	public final static EProcessType CB = new EProcessType("CB", 1); // credit.bureau 

	/**
	 * 
	 */
	public EProcessType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EProcessType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EProcessType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EProcessType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EProcessType> values() {
		return getValues(EProcessType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EProcessType getByCode(String code) {
		return getByCode(EProcessType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EProcessType getById(long id) {
		return getById(EProcessType.class, id);
	}
}
