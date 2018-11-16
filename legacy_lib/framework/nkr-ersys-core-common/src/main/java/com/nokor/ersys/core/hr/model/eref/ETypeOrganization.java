package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeOrganization extends BaseERefData implements AttributeConverter<ETypeOrganization, Long> {
	
	/** */
	private static final long serialVersionUID = -459195192551289285L;
	
	public final static ETypeOrganization MAIN = new ETypeOrganization("MAIN", 1);
	public final static ETypeOrganization INSURANCE = new ETypeOrganization("INSURANCE", 2);
	public final static ETypeOrganization AGENT= new ETypeOrganization("AGENT", 2);
	public final static ETypeOrganization LOCATION = new ETypeOrganization("LOCATION", 4);
	

	
	/**
	 * 
	 */
	public ETypeOrganization() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeOrganization(String code, long id) {
		super(code, id);
	}

	@Override
	public ETypeOrganization convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETypeOrganization arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeOrganization> values() {
		return getValues(ETypeOrganization.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeOrganization getById(long id) {
		return getById(ETypeOrganization.class, id);
	}
}