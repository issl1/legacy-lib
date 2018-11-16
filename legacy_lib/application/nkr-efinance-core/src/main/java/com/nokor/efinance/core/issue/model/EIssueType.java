package com.nokor.efinance.core.issue.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;
/**
 * 
 * @author buntha.chea
 *
 */
public class EIssueType extends BaseERefData implements AttributeConverter<EIssueType, Long> {

	/** */
	private static final long serialVersionUID = -221868636704120766L;
	
	public final static EIssueType MISTAKES = new EIssueType("I001", 1); 
	public final static EIssueType DISCREPANCY = new EIssueType("I002", 2);
	public final static EIssueType MISSING_OR_WRONG_DOCUMENT = new EIssueType("I003", 3);
	public final static EIssueType UNCLEAR_DOCUMENT = new EIssueType("I004", 4);
	public final static EIssueType INCOMPLETE_DOCUMENT = new EIssueType("I005", 5);

	/** */
	public EIssueType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EIssueType(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EIssueType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EIssueType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EIssueType> values() {
		return getValues(EIssueType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EIssueType getByCode(String code) {
		return getByCode(EIssueType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EIssueType getById(long id) {
		return getById(EIssueType.class, id);
	}

}
