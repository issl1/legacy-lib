package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.seuksa.frmk.model.eref.BaseERefData;
/**
 * 
 * @author buntha.chea
 *
 */
public class EColByPassRule extends BaseERefData implements AttributeConverter<EColByPassRule, Long> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1137015091502956098L;

	/**
	 */
	public EColByPassRule() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EColByPassRule(String code, long id) {
		super(code, id);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_by_pas_rul_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EColByPassRule convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EColByPassRule arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EColByPassRule> values() {
		return getValues(EColByPassRule.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EColByPassRule getByCode(String code) {
		return getByCode(EColByPassRule.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EColByPassRule getById(long id) {
		return getById(EColByPassRule.class, id);
	}
}
