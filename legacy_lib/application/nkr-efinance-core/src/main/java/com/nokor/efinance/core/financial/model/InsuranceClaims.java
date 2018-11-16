package com.nokor.efinance.core.financial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.Organization;


/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "tu_insurance_claims")
public class InsuranceClaims extends EntityA implements MInsuranceClaims {
	
	/** */
	private static final long serialVersionUID = -2710183086906801443L;
	
	private Organization insurance;
	private Integer rangeOfYear;
	private Integer from;
	private Integer to;
	private Double premiumnRefundedPercentage;
	
	/**
     * 
     * @return
     */
    public static InsuranceClaims createInstance() {
    	InsuranceClaims instance = EntityFactory.createInstance(InsuranceClaims.class);
        return instance;
    }
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ins_cla_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
   	
	/**
	 * @return the insurance
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
	public Organization getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(Organization insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return the rangeOfYear
	 */
	@Column(name = "ins_cla_range_of_year", nullable = true)
	public Integer getRangeOfYear() {
		return rangeOfYear;
	}

	/**
	 * @param rangeOfYear the rangeOfYear to set
	 */
	public void setRangeOfYear(Integer rangeOfYear) {
		this.rangeOfYear = rangeOfYear;
	}

	/**
	 * @return the from
	 */
	@Column(name = "ins_cla_from", nullable = true)
	public Integer getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Integer from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	@Column(name = "ins_cla_to", nullable = true)
	public Integer getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Integer to) {
		this.to = to;
	}

	/**
	 * @return the premiumnRefundedPercentage
	 */
	@Column(name = "ins_cla_premiumn_refunded_percentage", nullable = true)
	public Double getPremiumnRefundedPercentage() {
		return premiumnRefundedPercentage;
	}

	/**
	 * @param premiumnRefundedPercentage the premiumnRefundedPercentage to set
	 */
	public void setPremiumnRefundedPercentage(Double premiumnRefundedPercentage) {
		this.premiumnRefundedPercentage = premiumnRefundedPercentage;
	}
	
}
