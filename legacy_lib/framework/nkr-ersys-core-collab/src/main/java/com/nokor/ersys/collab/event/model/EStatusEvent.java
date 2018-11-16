package com.nokor.ersys.collab.event.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EStatusEvent extends BaseERefData implements AttributeConverter<EStatusEvent, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5893053175719431849L;
	// TODO
	public final static EStatusEvent SE01 = new EStatusEvent("SE01", 1);
	public final static EStatusEvent SE02 = new EStatusEvent("SE02", 2);

	/**
	 * 
	 */
	public EStatusEvent() {
		
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EStatusEvent(String code, long id) {
		super(code, id);
	}

	@Override
	public EStatusEvent convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(EStatusEvent arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EStatusEvent> values() {
		return getValues(EStatusEvent.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EStatusEvent getById(long id) {
		return getById(EStatusEvent.class, id);
	}
}