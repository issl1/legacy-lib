package com.nokor.efinance.core.financial.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.finance.services.shared.system.EFrequency;

/**
 * Financial Product Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_financial_product")
public class FinProduct extends EntityRefA {

	private static final long serialVersionUID = 3344239267196568225L;

	private ProductLine productLine;
	
	private Date startDate;
	private Date endDate;
	
	private Vat vat;
	private PenaltyRule penaltyRule;
	private LockSplitRule lockSplitRule;
	private ECurrency currency;
	
	private Integer maxFirstPaymentDay;
	private Integer term;
	private Double periodicInterestRate;
	private Double advancePaymentPercentage;
	private EFrequency frequency;
	private Double minAdvancePaymentPercentage;
	private Integer numberOfPrincipalGracePeriods;
	
	private EOptionality guarantor;
	private EOptionality collateral;
	private EOptionality reference;
		
	private List<FinProductService> financialProductServices;	
	
	private ERoundingFormat repaymentRounding;
	private Integer repaymentDecimals;

	/**
     * Get financial product's is.
     * @return The financial product's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fpd_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "fpd_code", nullable = false, length=10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset financial product's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "fpd_desc", nullable = true)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset financial product's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "fpd_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }

    
	/**
	 * @return the startDate
	 */
    @Column(name = "fpd_dt_start", nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "fpd_dt_end", nullable = false)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}	
	
	/**
	 * @return the vat
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_id")
	public Vat getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(Vat vat) {
		this.vat = vat;
	}

	/**
	 * @return the penaltyRule
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pen_rul_id")
	public PenaltyRule getPenaltyRule() {
		return penaltyRule;
	}

	/**
	 * @param penaltyRule the penaltyRule to set
	 */
	public void setPenaltyRule(PenaltyRule penaltyRule) {
		this.penaltyRule = penaltyRule;
	}	
	
	/**
	 * @return the lockSplitRule
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_rul_id")
	public LockSplitRule getLockSplitRule() {
		return lockSplitRule;
	}

	/**
	 * @param lockSplitRule the lockSplitRule to set
	 */
	public void setLockSplitRule(LockSplitRule lockSplitRule) {
		this.lockSplitRule = lockSplitRule;
	}

	/**
	 * @return the currency
	 */
	@Column(name = "cur_id", nullable = true)
	@Convert(converter = ECurrency.class)
	public ECurrency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(ECurrency currency) {
		this.currency = currency;
	}

	/**
	 * @return the term
	 */
	@Column(name = "fpd_nu_term", nullable = true)
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
	 * @return the periodicInterestRate
	 */
	@Column(name = "fpd_rt_periodic_interest_rate", nullable = true)
	public Double getPeriodicInterestRate() {
		return periodicInterestRate;
	}

	/**
	 * @param periodicInterestRate the periodicInterestRate to set
	 */
	public void setPeriodicInterestRate(Double periodicInterestRate) {
		this.periodicInterestRate = periodicInterestRate;
	}

	/**
	 * @return the advancePaymentPercentage
	 */
	@Column(name = "fpd_rt_advance_payment_pc", nullable = true)
	public Double getAdvancePaymentPercentage() {
		return advancePaymentPercentage;
	}

	/**
	 * @param advancePaymentPercentage the advancePaymentPercentage to set
	 */
	public void setAdvancePaymentPercentage(Double advancePaymentPercentage) {
		this.advancePaymentPercentage = advancePaymentPercentage;
	}	

	/**
	 * @return the guarantor
	 */
	@Column(name = "opt_id_guarantor", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getGuarantor() {
		return guarantor;
	}

	/**
	 * @param guarantor the guarantor to set
	 */
	public void setGuarantor(EOptionality guarantor) {
		this.guarantor = guarantor;
	}

	/**
	 * @return the collateral
	 */
	@Column(name = "opt_id_collateral", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getCollateral() {
		return collateral;
	}

	/**
	 * @param collateral the collateral to set
	 */
	public void setCollateral(EOptionality collateral) {
		this.collateral = collateral;
	}

	/**
	 * @return the reference
	 */
	@Column(name = "opt_id_reference", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(EOptionality reference) {
		this.reference = reference;
	}

	/**
	 * @return the minAdvancePaymentPercentage
	 */
	@Column(name = "fpd_rt_min_advance_payment_pc", nullable = true)
	public Double getMinAdvancePaymentPercentage() {
		return minAdvancePaymentPercentage;
	}

	/**
	 * @param minAdvancePaymentPercentage the minAdvancePaymentPercentage to set
	 */
	public void setMinAdvancePaymentPercentage(Double minAdvancePaymentPercentage) {
		this.minAdvancePaymentPercentage = minAdvancePaymentPercentage;
	}
	/**
	 * @return the maxFirstPaymentDay
	 */
	@Column(name = "fpd_nu_max_first_payment", nullable = true)
	public Integer getMaxFirstPaymentDay() {
		return maxFirstPaymentDay;
	}

	/**
	 * @param maxFirstPaymentDay the maxFirstPaymentDay to set
	 */
	public void setMaxFirstPaymentDay(Integer maxFirstPaymentDay) {
		this.maxFirstPaymentDay = maxFirstPaymentDay;
	}

	/**
	 * @return the frequency
	 */
	@Column(name = "fre_id", nullable = true)
    @Convert(converter = EFrequency.class)
	public EFrequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(EFrequency frequency) {
		this.frequency = frequency;
	}	

	/**
	 * @return the numberOfPrincipalGracePeriods
	 */
	@Column(name = "fpd_nu_principal_grace_periods", nullable = true)
	public Integer getNumberOfPrincipalGracePeriods() {
		return numberOfPrincipalGracePeriods;
	}

	/**
	 * @param numberOfPrincipalGracePeriods the numberOfPrincipalGracePeriods to set
	 */
	public void setNumberOfPrincipalGracePeriods(Integer numberOfPrincipalGracePeriods) {
		this.numberOfPrincipalGracePeriods = numberOfPrincipalGracePeriods;
	}

	/**
	 * @return the productLine
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_lin_id", nullable = true)
	public ProductLine getProductLine() {
		return productLine;
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
	}

	/**
	 * @return the financialProductServices
	 */
	@OneToMany(mappedBy="financialProduct", fetch = FetchType.LAZY)
	public List<FinProductService> getFinancialProductServices() {
		return financialProductServices;
	}

	/**
	 * @param financialProductServices the financialProductServices to set
	 */
	public void setFinancialProductServices(
			List<FinProductService> financialProductServices) {
		this.financialProductServices = financialProductServices;
	}

	/**
	 * @return the repaymentRounding
	 */
	@Column(name = "rou_fom_id", nullable = true)
    @Convert(converter = ERoundingFormat.class)
	public ERoundingFormat getRepaymentRounding() {
		return repaymentRounding;
	}

	/**
	 * @param repaymentRounding the repaymentRounding to set
	 */
	public void setRepaymentRounding(ERoundingFormat repaymentRounding) {
		this.repaymentRounding = repaymentRounding;
	}

	/**
	 * @return the repaymentDecimals
	 */
	@Column(name = "fpd_nu_repayment_decimals", nullable = true)
	public Integer getRepaymentDecimals() {
		return repaymentDecimals;
	}

	/**
	 * @param repaymentDecimals the repaymentDecimals to set
	 */
	public void setRepaymentDecimals(Integer repaymentDecimals) {
		this.repaymentDecimals = repaymentDecimals;
	}	

}
