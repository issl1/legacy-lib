package com.nokor.efinance.glf.statistic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * 
 * @author sok.vina
 *
 */
@Entity
@Table(name = "tu_statistic_config")
public class StatisticConfig extends EntityA {

    private static final long serialVersionUID = -4053267507390718585L;
    
    private Dealer dealer;
	private Integer targetLow;
    private Integer targetHigh;
    private Date startDate;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "staco_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the targetLow
	 */
	@Column(name = "staco_nu_target_low", nullable = true)
	public Integer getTargetLow() {
		return targetLow;
	}

	/**
	 * @param targetLow the targetLow to set
	 */
	public void setTargetLow(Integer targetLow) {
		this.targetLow = targetLow;
	}

	/**
	 * @return the targetHigh
	 */
	@Column(name = "staco_nu_target_high", nullable = true)
	public Integer getTargetHigh() {
		return targetHigh;
	}

	/**
	 * @param targetHigh the targetHigh to set
	 */
	public void setTargetHigh(Integer targetHigh) {
		this.targetHigh = targetHigh;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "staco_nu_dt_start", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
}
