package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETaskType extends BaseERefData implements AttributeConverter<ETaskType, Long> {
	/** */
	private static final long serialVersionUID = 646060694004735307L;

	public final static ETaskType TASK = new ETaskType("TASK", 1);
	public final static ETaskType BUG = new ETaskType("BUG", 2);
	public final static ETaskType ENHANCEMENT = new ETaskType("ENHANCEMENT", 2);
	public final static ETaskType TODO = new ETaskType("TODO", 3);

	/**
	 * 
	 */
	public ETaskType() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETaskType(String code, long id) {
		super(code, id);
	}

	@Override
	public ETaskType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETaskType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETaskType> values() {
		return getValues(ETaskType.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETaskType getById(long id) {
		return getById(ETaskType.class, id);
	}
}