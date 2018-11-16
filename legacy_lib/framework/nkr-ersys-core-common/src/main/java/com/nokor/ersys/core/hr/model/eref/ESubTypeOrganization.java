package com.nokor.ersys.core.hr.model.eref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ESubTypeOrganization extends BaseERefData implements AttributeConverter<ESubTypeOrganization, Long> {
	/** */
	private static final long serialVersionUID = 2311312592679068232L;
	
	private ETypeOrganization typeOrganization;
	
	/**
	 * 
	 */
	public ESubTypeOrganization() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESubTypeOrganization(String code, long id, ETypeOrganization typeOrganization) {
		super(code, id);
		this.typeOrganization = typeOrganization;
	}

	/**
	 * @return the typeOrganization
	 */
	public ETypeOrganization getTypeOrganization() {
		return typeOrganization;
	}

	/**
	 * @param typeOrganization the typeOrganization to set
	 */
	public void setTypeOrganization(ETypeOrganization typeOrganization) {
		this.typeOrganization = typeOrganization;
	}

	@Override
	public ESubTypeOrganization convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESubTypeOrganization arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESubTypeOrganization> values() {
		return getValues(ESubTypeOrganization.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ESubTypeOrganization> values(ETypeOrganization typeOrganization) {
		List<ESubTypeOrganization> lstSubType = ESubTypeOrganization.values();
		
		List<ESubTypeOrganization> lstResSubType = new ArrayList<ESubTypeOrganization>();
		for (ESubTypeOrganization subTyp : lstSubType) {
			if (subTyp.getTypeOrganization() != null && subTyp.getTypeOrganization().equals(typeOrganization)) {
				lstResSubType.add(subTyp);
			}
		}
		return lstResSubType;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESubTypeOrganization getById(long id) {
		return getById(ESubTypeOrganization.class, id);
	}
}