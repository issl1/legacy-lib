package com.nokor.common.app.eventlog.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ESecEventType extends BaseERefData implements AttributeConverter<ESecEventType, Long> {
	/** */
	private static final long serialVersionUID = 2399852181486171719L;
	
	public final static ESecEventType CREATION = new ESecEventType("CREATION", 1); 
	public final static ESecEventType MODIFICATION = new ESecEventType("MODIFICATION", 2); 
	public final static ESecEventType DELETION = new ESecEventType("DELETION", 3); 
	public final static ESecEventType OTHER = new ESecEventType("OTHER", 10); 

	/**
	 * 
	 */
	public ESecEventType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecEventType(String code, long id) {
		super(code, id);
	}

	@Override
	public ESecEventType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESecEventType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static List<ESecEventType> values() {
		return getValues(ESecEventType.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecEventType getById(long id) {
		return getById(ESecEventType.class, id);
	}
}