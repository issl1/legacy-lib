package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETaskPriority extends BaseERefData implements AttributeConverter<ETaskPriority, Long> {
	/** */
	private static final long serialVersionUID = 1608244360789596003L;

	public final static ETaskPriority P1 = new ETaskPriority("P1", 1);
	public final static ETaskPriority P2 = new ETaskPriority("P2", 2);
	public final static ETaskPriority P3 = new ETaskPriority("P3", 3);
	public final static ETaskPriority P4 = new ETaskPriority("P4", 4);

	/**
	 * 
	 */
	public ETaskPriority() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETaskPriority(String code, long id) {
		super(code, id);
	}

	@Override
	public ETaskPriority convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETaskPriority arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETaskPriority> values() {
		return getValues(ETaskPriority.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETaskPriority getById(long id) {
		return getById(ETaskPriority.class, id);
	}
}