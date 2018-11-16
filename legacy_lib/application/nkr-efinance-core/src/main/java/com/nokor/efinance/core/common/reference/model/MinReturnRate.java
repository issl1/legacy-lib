package com.nokor.efinance.core.common.reference.model;

import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "tu_min_return_rate")
public class MinReturnRate extends EntityA {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6994382605941907082L;
	
	private String descEn;
	private String desc;
	private Date startDate;
	private Date endDate;
	private Double rateValue;
	

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "min_ret_rat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}


	/**
	 * @return the descEn
	 */
	@Column(name = "min_ret_rat_desc_en", nullable = false, length=255)
	public String getDescEn() {
		return descEn;
	}


	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}


	/**
	 * @return the desc
	 */
	@Column(name = "min_ret_rat_desc", nullable = true, length=255)
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}


	/**
	 * @return the startDate
	 */
	@Column(name = "min_ret_rat_dt_start", nullable = true)
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	@Column(name = "min_ret_rat_dt_end", nullable = true)
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the rateValue
	 */
	@Column(name = "min_ret_rat_value", nullable = true)
	public Double getRateValue() {
		return rateValue;
	}


	/**
	 * @param rateValue the rateValue to set
	 */
	public void setRateValue(Double rateValue) {
		this.rateValue = rateValue;
	}


}
