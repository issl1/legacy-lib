package com.nokor.ersys.collab.membership.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ERemovedReason extends BaseERefData implements AttributeConverter<ERemovedReason, Long> {
	/** */
	private static final long serialVersionUID = 112335386635029442L;

	public final static ERemovedReason DEFAULT = new ERemovedReason("DEFAULT", 1);

	/**
	 */
	public ERemovedReason() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ERemovedReason(String code, long id) {
		super(code, id);
	}

	@Override
	public ERemovedReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ERemovedReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ERemovedReason> values() {
		return getValues(ERemovedReason.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERemovedReason getByCode(String code) {
		return getByCode(ERemovedReason.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ERemovedReason getById(long id) {
		return getById(ERemovedReason.class, id);
	}
}
