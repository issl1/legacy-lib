package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeContact extends BaseERefData implements AttributeConverter<ETypeContact, Long> {
	
	/** */
	private static final long serialVersionUID = -8726266780624232289L;
	
	public final static ETypeContact MAIN = new ETypeContact("MAIN", 1);
	public final static ETypeContact OTHER = new ETypeContact("OTHER", 2);
	public final static ETypeContact PROSPECT = new ETypeContact("PROSPECT", 3);
	public final static ETypeContact MANAGER = new ETypeContact("MANAGER", 4);
	public final static ETypeContact OWNER = new ETypeContact("OWNER", 5);

	
	/**
	 * 
	 */
	public ETypeContact() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeContact(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ETypeContact convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETypeContact arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeContact> values() {
		return getValues(ETypeContact.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeContact getById(long id) {
		return getById(ETypeContact.class, id);
	}
}