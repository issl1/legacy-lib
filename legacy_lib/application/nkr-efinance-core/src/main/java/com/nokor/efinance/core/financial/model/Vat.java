/**
 * 
 */
package com.nokor.efinance.core.financial.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Vat
 * @author youhort.ly
 */
@Entity
@Table(name = "tu_vat")
public class Vat extends EntityRefA {

	/**	 */
	private static final long serialVersionUID = -6449090770460270558L;

	private Double value;
	private Date startDate;
	private Date endDate;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Transient
	public String getCode() {
		return "XXX";
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "vat_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * Get the asset financial product's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "vat_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the value
	 */
    @Column(name = "vat_rt_value")
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "vat_dt_start")
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
	@Column(name = "vat_dt_end")
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
}
