package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;
/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_col_by_pass_rule")
public class ColByPassRule extends EntityA {

	/** */
	private static final long serialVersionUID = -178369330161926911L;
	
	private EColByPassRule colByPassRule;
	private EColType byPassFrom;
	private EColType byPassTo;
	private String value;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "by_pas_rul_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the colByRule
	 */
	@Column(name = "col_by_pas_rul_id", nullable = false)
	@Convert(converter = EColByPassRule.class)
	public EColByPassRule getColByPassRule() {
		return colByPassRule;
	}

	/**
	 * @param colByPassRule the colByRule to set
	 */
	public void setColByPassRule(EColByPassRule colByPassRule) {
		this.colByPassRule = colByPassRule;
	}

	/**
	 * @return the byPassFrom
	 */
	@Column(name = "col_typ_id_from", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getByPassFrom() {
		return byPassFrom;
	}

	/**
	 * @param byPassFrom the byPassFrom to set
	 */
	public void setByPassFrom(EColType byPassFrom) {
		this.byPassFrom = byPassFrom;
	}

	/**
	 * @return the byPassTo
	 */
	@Column(name = "col_typ_id_to", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getByPassTo() {
		return byPassTo;
	}

	/**
	 * @param byPassTo the byPassTo to set
	 */
	public void setByPassTo(EColType byPassTo) {
		this.byPassTo = byPassTo;
	}

	/**
	 * @return the value1
	 */
	@Column(name = "col_by_pas_rul_value", nullable = true, length = 50)
	public String getValue() {
		return value;
	}

	/**
	 * @param value1 the value1 to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
