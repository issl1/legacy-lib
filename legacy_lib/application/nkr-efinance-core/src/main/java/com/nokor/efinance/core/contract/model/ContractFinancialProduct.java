package com.nokor.efinance.core.contract.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

@Entity
@Table(name = "td_contract_financial_product")
public class ContractFinancialProduct  extends EntityA {

	private static final long serialVersionUID = -3519639545646109117L;
	
	private Contract contract;
	private String desc;
	private String descEn;
	private Integer duration;
	private Double customerRate; 
	private Double irrRate;
	private Integer term;
	private Double tiInstallmentUsd;
	private Double teInstallmentUsd;
	private Double vatInstallmentUsd;
		
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cofin_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the contract
	 */
    @OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the desc
	 */
	@Column(name = "cofin_desc", nullable = true, length = 255)
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
	 * @return the descEn
	 */
	@Column(name = "cofin_desc_en", nullable = true, length = 255)
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
	 * @return the duration
	 */
	@Column(name = "cofin_nu_duration", nullable = true)
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the customerRate
	 */
	@Column(name = "cofin_rt_customer", nullable = true)
	public Double getCustomerRate() {
		return customerRate;
	}

	/**
	 * @param customerRate the customerRate to set
	 */
	public void setCustomerRate(Double customerRate) {
		this.customerRate = customerRate;
	}

	/**
	 * @return the irrRate
	 */
	@Column(name = "cofin_rt_irr", nullable = true)
	public Double getIrrRate() {
		return irrRate;
	}

	/**
	 * @param irrRate the irrRate to set
	 */
	public void setIrrRate(Double irrRate) {
		this.irrRate = irrRate;
	}

	/**
	 * @return the term
	 */
	@Column(name = "cofin_nu_term", nullable = true)
	public Integer getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	
	/**
	 * @return the tiInstallmentUsd
	 */
	@Column(name = "cofin_am_ti_installment_usd", nullable = true)
	public Double getTiInstallmentUsd() {
		return tiInstallmentUsd;
	}

	/**
	 * @param tiInstallmentUsd the tiInstallmentUsd to set
	 */
	public void setTiInstallmentUsd(Double tiInstallmentUsd) {
		this.tiInstallmentUsd = tiInstallmentUsd;
	}

	/**
	 * @return the teInstallmentUsd
	 */
	@Column(name = "cofin_am_te_installment_usd", nullable = true)
	public Double getTeInstallmentUsd() {
		return teInstallmentUsd;
	}

	/**
	 * @param teInstallmentUsd the teInstallmentUsd to set
	 */
	public void setTeInstallmentUsd(Double teInstallmentUsd) {
		this.teInstallmentUsd = teInstallmentUsd;
	}

	/**
	 * @return the vatInstallmentUsd
	 */
	@Column(name = "cofin_am_vat_installment_usd", nullable = true)
	public Double getVatInstallmentUsd() {
		return vatInstallmentUsd;
	}

	/**
	 * @param vatInstallmentUsd the vatInstallmentUsd to set
	 */
	public void setVatInstallmentUsd(Double vatInstallmentUsd) {
		this.vatInstallmentUsd = vatInstallmentUsd;
	}

}
