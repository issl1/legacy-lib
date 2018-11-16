package com.nokor.efinance.core.financial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * @author bunlong.taing
 */
@Entity
@Table(name = "tu_term")
public class Term extends EntityRefA implements MTerm {
	/** */
	private static final long serialVersionUID = -7860978560913586422L;
	
	private Integer value;

	/**
	 * @return id
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ter_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Override
    @Transient
    public String getCode() {
        return "TMP";
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Override
    @Column(name = "ter_desc", nullable = true, length=255)
	public String getDesc() {
		return super.getDesc();
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Override
	@Column(name = "ter_desc_en", nullable = true, length=255)
	public String getDescEn() {
		return super.getDescEn();
	}

	/**
	 * @return the value
	 */
	@Column(name = "ter_value", nullable = true)
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

}
