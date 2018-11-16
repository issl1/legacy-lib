package com.nokor.ersys.collab.event.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeEvent extends BaseERefData implements AttributeConverter<ETypeEvent, Long> {

	private static final long serialVersionUID = 7718658845763304208L;
	
	public final static ETypeEvent TE01 = new ETypeEvent("TE01", 1); 
	public final static ETypeEvent TE02 = new ETypeEvent("TE02", 2); 

	/**
	 * 
	 */
	public ETypeEvent() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeEvent(String code, long id) {
		super(code, id);
	}

	@Override
	public ETypeEvent convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETypeEvent arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeEvent> values() {
		return getValues(ETypeEvent.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeEvent getById(long id) {
		return getById(ETypeEvent.class, id);
	}
}