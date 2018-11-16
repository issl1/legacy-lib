package com.nokor.efinance.core.quotation.model;

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

import com.nokor.frmk.security.model.SecUser;

/**
 * Registration Expense Model
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_quotation_registration_expense")
public class QuotationRegistrationExpense extends EntityA {

	private static final long serialVersionUID = 8935751397622214352L;
	
	private Double registrationFeeUsd;
	private Double otherFeeUsd;
	private Double gasolineFeeUsd;
	private Double parkingFeeUsd;
	
	private Quotation quotation;
	private SecUser secUser;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_reg_exp_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	/**
	 * @return the registrationFeeUsd
	 */
	@Column(name = "quo_reg_exp_am_registration_fee_usd", nullable = true)
	public Double getRegistrationFeeUsd() {
		return registrationFeeUsd;
	}

	/**
	 * @param registrationFeeUsd the registrationFeeUsd to set
	 */
	public void setRegistrationFeeUsd(Double registrationFeeUsd) {
		this.registrationFeeUsd = registrationFeeUsd;
	}

	/**
	 * @return the otherFeeUsd
	 */
	@Column(name = "quo_reg_exp_am_other_fee_usd", nullable = true)
	public Double getOtherFeeUsd() {
		return otherFeeUsd;
	}

	/**
	 * @param otherFeeUsd the otherFeeUsd to set
	 */
	
	public void setOtherFeeUsd(Double otherFeeUsd) {
		this.otherFeeUsd = otherFeeUsd;
	}
	/**
	 * @return the gasolineFeeUsd
	 */
	@Column(name = "quo_reg_exp_am_gasoline_fee_usd", nullable = true)
	public Double getGasolineFeeUsd() {
		return gasolineFeeUsd;
	}
	/**
	 * @param gasolineFeeUsd the gasolineFeeUsd to set
	 */
	public void setGasolineFeeUsd(Double gasolineFeeUsd) {
		this.gasolineFeeUsd = gasolineFeeUsd;
	}
	/**
	 * @return the parkingFeeUsd
	 */
	@Column(name = "quo_reg_exp_am_parking_fee_usd", nullable = true)
	public Double getParkingFeeUsd() {
		return parkingFeeUsd;
	}
	/**
	 * @param parkingFeeUsd the parkingFeeUsd to set
	 */
	public void setParkingFeeUsd(Double parkingFeeUsd) {
		this.parkingFeeUsd = parkingFeeUsd;
	}
	/**
	 * @return the quotation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quo_id")
	public Quotation getQuotation() {
		return quotation;
	}
	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
	/**
	 * @return the secUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getSecUser() {
		return secUser;
	}
	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
}
