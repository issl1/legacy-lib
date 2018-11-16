package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EOperationType extends BaseERefData implements AttributeConverter<EOperationType, Long> {
		
	/**
	 */
	private static final long serialVersionUID = -1852619696394338966L;
	
	public final static EOperationType ASS_CHG_COLOR = new EOperationType("ASS_CHG_COLOR", 1); 
	
	/**
	 */
	public EOperationType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EOperationType(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EOperationType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EOperationType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EOperationType> values() {
		return getValues(EOperationType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EOperationType getByCode(String code) {
		return getByCode(EOperationType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EOperationType getById(long id) {
		return getById(EOperationType.class, id);
	}

}