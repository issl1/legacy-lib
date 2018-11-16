package com.nokor.ersys.collab.wkgroup.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EWkgGroupType extends BaseERefData implements AttributeConverter<EWkgGroupType, Long> {
	/** */
	private static final long serialVersionUID = 112335386635029442L;

	public final static EWkgGroupType DEFAULT = new EWkgGroupType("DEFAULT", 1);

	/**
	 */
	public EWkgGroupType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EWkgGroupType(String code, long id) {
		super(code, id);
	}

	@Override
	public EWkgGroupType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EWkgGroupType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EWkgGroupType> values() {
		return getValues(EWkgGroupType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EWkgGroupType getByCode(String code) {
		return getByCode(EWkgGroupType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EWkgGroupType getById(long id) {
		return getById(EWkgGroupType.class, id);
	}
}
