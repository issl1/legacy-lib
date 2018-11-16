package com.nokor.ersys.collab.wkgroup.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EWkgPosition extends BaseERefData implements AttributeConverter<EWkgPosition, Long> {
	/** */
	private static final long serialVersionUID = -7122476334313044027L;

	public final static EWkgPosition PRESIDENT = new EWkgPosition("PRESIDENT", 1);
	public final static EWkgPosition VICE_PRESIDENT = new EWkgPosition("VICE_PRESIDENT", 2);
	public final static EWkgPosition MEMBER = new EWkgPosition("MEMBER", 3);
	public final static EWkgPosition GUEST = new EWkgPosition("GUEST", 100);

	/**
	 */
	public EWkgPosition() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EWkgPosition(String code, long id) {
		super(code, id);
	}

	@Override
	public EWkgPosition convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EWkgPosition arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EWkgPosition> values() {
		return getValues(EWkgPosition.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EWkgPosition getByCode(String code) {
		return getByCode(EWkgPosition.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EWkgPosition getById(long id) {
		return getById(EWkgPosition.class, id);
	}
}
