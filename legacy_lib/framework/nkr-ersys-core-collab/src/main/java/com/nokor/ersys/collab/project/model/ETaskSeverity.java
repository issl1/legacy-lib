package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETaskSeverity extends BaseERefData implements AttributeConverter<ETaskSeverity, Long> {
	/** */
	private static final long serialVersionUID = 6605638181754019712L;

	public final static ETaskSeverity BLOCKER = new ETaskSeverity("BLOCKER", 1);
	public final static ETaskSeverity MAJOR = new ETaskSeverity("MAJOR", 2);
	public final static ETaskSeverity NORMAL = new ETaskSeverity("NORMAL", 3);
	public final static ETaskSeverity MINOR = new ETaskSeverity("MINOR", 4);

	/**
	 * 
	 */
	public ETaskSeverity() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETaskSeverity(String code, long id) {
		super(code, id);
	}

	@Override
	public ETaskSeverity convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETaskSeverity arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETaskSeverity> values() {
		return getValues(ETaskSeverity.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETaskSeverity getById(long id) {
		return getById(ETaskSeverity.class, id);
	}
}