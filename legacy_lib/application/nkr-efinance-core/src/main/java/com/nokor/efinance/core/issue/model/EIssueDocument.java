package com.nokor.efinance.core.issue.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class EIssueDocument extends BaseERefData implements AttributeConverter<EIssueDocument, Long> {

	
	/** */
	private static final long serialVersionUID = -1624417137730797010L;

	public EIssueDocument() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EIssueDocument(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EIssueDocument convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EIssueDocument arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EIssueDocument> values() {
		return getValues(EIssueDocument.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EIssueDocument getByCode(String code) {
		return getByCode(EIssueDocument.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EIssueDocument getById(long id) {
		return getById(EIssueDocument.class, id);
	}
	
}
