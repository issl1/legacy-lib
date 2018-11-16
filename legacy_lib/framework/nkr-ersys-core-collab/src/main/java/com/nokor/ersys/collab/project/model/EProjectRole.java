package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EProjectRole extends BaseERefData implements AttributeConverter<EProjectRole, Long> {
	/** */
	private static final long serialVersionUID = 1223894140083460892L;
	
	public final static EProjectRole PROJECT_MANAGER = new EProjectRole("PROJECT_MANAGER", 1);
	public final static EProjectRole ADMINISTRATOR = new EProjectRole("ADMINISTRATOR", 2);

	/**
	 * 
	 */
	public EProjectRole() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EProjectRole(String code, long id) {
		super(code, id);
	}

	@Override
	public EProjectRole convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(EProjectRole arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EProjectRole> values() {
		return getValues(EProjectRole.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EProjectRole getFromId(long id) {
		return getById(EProjectRole.class, id);
	}
}