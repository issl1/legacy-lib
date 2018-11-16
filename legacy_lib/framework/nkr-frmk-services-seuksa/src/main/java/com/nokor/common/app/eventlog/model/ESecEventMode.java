package com.nokor.common.app.eventlog.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ESecEventMode extends BaseERefData implements AttributeConverter<ESecEventMode, Long> {
	/** */
	private static final long serialVersionUID = -551553947326768423L;

	public final static ESecEventMode USER = new ESecEventMode("USER", 1); 
	public final static ESecEventMode SYSTEM = new ESecEventMode("SYSTEM", 2); 
	public final static ESecEventMode TRIGGERED = new ESecEventMode("TRIGGERED", 3); 
	public final static ESecEventMode SCHEDULED = new ESecEventMode("SCHEDULED", 4); 
	public final static ESecEventMode OTHER = new ESecEventMode("OTHER", 10); 

	/**
	 * 
	 */
	public ESecEventMode() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecEventMode(String code, long id) {
		super(code, id);
	}

	@Override
	public ESecEventMode convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESecEventMode arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static List<ESecEventMode> values() {
		return getValues(ESecEventMode.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecEventMode getById(long id) {
		return getById(ESecEventMode.class, id);
	}
}