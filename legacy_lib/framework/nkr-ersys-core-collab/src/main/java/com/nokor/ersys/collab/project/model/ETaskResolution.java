package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETaskResolution extends BaseERefData implements AttributeConverter<ETaskResolution, Long> {
	/** */
	private static final long serialVersionUID = -7374505694857500771L;

	public final static ETaskResolution FIXED = new ETaskResolution("FIXED", 1);
	public final static ETaskResolution INVALID  = new ETaskResolution("INVALID", 2);
	public final static ETaskResolution WONTFIX = new ETaskResolution("WONTFIX", 3);
	public final static ETaskResolution DUPLICATE = new ETaskResolution("DUPLICATE", 4);

	/**
	 * 
	 */
	public ETaskResolution() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETaskResolution(String code, long id) {
		super(code, id);
	}

	@Override
	public ETaskResolution convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETaskResolution arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETaskResolution> values() {
		return getValues(ETaskResolution.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETaskResolution getById(long id) {
		return getById(ETaskResolution.class, id);
	}
}