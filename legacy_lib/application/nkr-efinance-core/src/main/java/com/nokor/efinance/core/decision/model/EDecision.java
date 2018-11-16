package com.nokor.efinance.core.decision.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class EDecision extends BaseERefData implements AttributeConverter<EDecision, Long> {
	/** */
	private static final long serialVersionUID = -278822957785373420L;
	
	private EDecisionCategory category;

	/**
	 */
	public EDecision() {
	}

	/**
	 * @param code
	 * @param id
	 */
	public EDecision(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EDecision decision) {
		return super.convertToDatabaseColumn(decision);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EDecision convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EDecision> values() {
		return getValues(EDecision.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDecision getByCode(String code) {
		return getByCode(EDecision.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDecision getById(long id) {
		return getById(EDecision.class, id);
	}

	/**
	 * @return the category
	 */
	public EDecisionCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EDecisionCategory category) {
		this.category = category;
	}

}
