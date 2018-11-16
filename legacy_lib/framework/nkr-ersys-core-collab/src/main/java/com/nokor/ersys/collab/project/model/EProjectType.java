package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EProjectType extends BaseERefData implements AttributeConverter<EProjectType, Long> {
	/** */
	private static final long serialVersionUID = 2139523589111978308L;
	
	public final static EProjectType INTERNAL = new EProjectType("INTERNAL", 1);
	public final static EProjectType EXTERNAL = new EProjectType("EXTERNAL", 1);

	/**
	 * 
	 */
	public EProjectType() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EProjectType(String code, long id) {
		super(code, id);
	}

	@Override
	public EProjectType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(EProjectType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EProjectType> values() {
		return getValues(EProjectType.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EProjectType getFromId(long id) {
		return getById(EProjectType.class, id);
	}
}