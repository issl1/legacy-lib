package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_col_cus_field")
public class ColCustField extends EntityRefA {

	/** */
	private static final long serialVersionUID = -6037054470700162832L;
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_cus_fie_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "col_cus_fie_code", nullable = false, length = 10)
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "col_cus_fie_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
    @Override
    @Column(name = "col_cus_fie_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
}
