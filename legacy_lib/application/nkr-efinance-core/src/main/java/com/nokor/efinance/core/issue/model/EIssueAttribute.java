package com.nokor.efinance.core.issue.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class EIssueAttribute extends BaseERefData implements AttributeConverter<EIssueAttribute, Long> {
	
	/** */
	private static final long serialVersionUID = -7334334798733136184L;
	
	private EIssueType issueType;

	/** */
	public EIssueAttribute() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EIssueAttribute(String code, long id, EIssueType issueType) {
		super(code, id);
		this.issueType = issueType;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EIssueAttribute convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EIssueAttribute arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EIssueAttribute> values() {
		return getValues(EIssueAttribute.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EIssueAttribute getByCode(String code) {
		return getByCode(EIssueAttribute.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EIssueAttribute getById(long id) {
		return getById(EIssueAttribute.class, id);
	}

	/**
	 * @return the issueType
	 */
	public EIssueType getIssueType() {
		return issueType;
	}

	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(EIssueType issueType) {
		this.issueType = issueType;
	}
	
	/**
	 * @param issueType
	 * @return
	 */
	public static List<EIssueAttribute> values(EIssueType issueType) {
		List<EIssueAttribute>  issueAttributes = EIssueAttribute.values();
		
		List<EIssueAttribute> lstResIssueAttributes = new ArrayList<EIssueAttribute>();
		for (EIssueAttribute issueAttribute : issueAttributes) {
			if (issueAttribute.getIssueType().equals(issueType)) {
				lstResIssueAttributes.add(issueAttribute);
			}
		}
		return lstResIssueAttributes;
	}
	
	
}
