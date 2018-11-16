package com.nokor.common.app.eventlog.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ESecEventLevel extends BaseERefData implements AttributeConverter<ESecEventLevel, Long> {
	/** */
	private static final long serialVersionUID = 7373672127875930550L;

	public final static ESecEventLevel INFO = new ESecEventLevel("INFO", 1); 
	public final static ESecEventLevel DEBUG = new ESecEventLevel("DEBUG", 2); 
	public final static ESecEventLevel WARN = new ESecEventLevel("WARN", 3); 
	public final static ESecEventLevel CRITICAL = new ESecEventLevel("CRITICAL", 4); 
	public final static ESecEventLevel ERROR = new ESecEventLevel("ERROR", 5); 

	/**
	 * 
	 */
	public ESecEventLevel() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESecEventLevel(String code, long id) {
		super(code, id);
	}

	@Override
	public ESecEventLevel convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESecEventLevel arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static List<ESecEventLevel> values() {
		return getValues(ESecEventLevel.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESecEventLevel getById(long id) {
		return getById(ESecEventLevel.class, id);
	}
}