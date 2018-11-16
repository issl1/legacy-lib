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

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.finance.services.shared.system.EFrequency;

/**
 * Service Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_service")
public class FinService extends EntityRefA {

	private static final long serialVersionUID = -2109001780570872581L;

	private EServiceType serviceType;
	private ECalculMethod calculMethod;
	private double percentage;
	private double tiPrice;
	private double tePrice;
	private double vatPrice;
	private ETreasuryType treasuryType;
	private boolean paidBeginContract;
	private boolean splitWithInstallment;
	
	//Add for direct cost
	private boolean contractDuration;
	private Integer termInMonths;
	private Date dueDate;
	private Double percentageOfAssetFirstYear;
	private Double percentageOfAssetSecondYear;
	private Double percentageOfAssetThirdYear;
	private Double percentageOfAssetForthYear;
	private Double percentageOfAssetFifthYear;
	private Double percentageOfPremiumFirstYear;
	private Double percentageOfPremiumSecondYear;
	private Double percentageOfPremiumThirdYear;
	private Double percentageOfPremiumForthYear;
	private Double percentageOfPremiumFifthYear;
	private String formula;
	
	private EFrequency frequency;
	private boolean allowChangePrice;
	private boolean paidOneShot;
	private JournalEvent journalEvent; 
	
	private List<FinProductService> financialProductServices;	
    
    public FinService() {
    }

    /**
     * Get service's is.
     * @return The service's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fin_srv_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    @Override
    @Column(name = "fin_srv_code", nullable = false, length=10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the service's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "fin_srv_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the service's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "fin_srv_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }

    
	/**
	 * @return the serviceType
	 */
    @Column(name = "ser_typ_id", nullable = false)
    @Convert(converter = EServiceType.class)
	public EServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(final EServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * @return the calculMethod
	 */
	@Column(name = "cal_met_id", nullable = false)
    @Convert(converter = ECalculMethod.class)
	public ECalculMethod getCalculMethod() {
		return calculMethod;
	}

	/**
	 * @param calculMethod the calculMethod to set
	 */
	public void setCalculMethod(ECalculMethod calculMethod) {
		this.calculMethod = calculMethod;
	}

	/**
	 * @return the percentage
	 */
	@Column(name = "fin_srv_rt_percentage", nullable = true)
	public double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	/**
	 * @return the tiPrice
	 */
	@Column(name = "fin_srv_am_ti_price", nullable = true)
	public double getTiPrice() {
		return tiPrice;
	}

	/**
	 * @param tiPrice the tiPrice to set
	 */
	public void setTiPrice(double tiPrice) {
		this.tiPrice = tiPrice;
	}

	/**
	 * @return the tePrice
	 */
	@Column(name = "fin_srv_am_te_price", nullable = true)
	public double getTePrice() {
		return tePrice;
	}

	/**
	 * @param tePrice the tePrice to set
	 */
	public void setTePrice(double tePrice) {
		this.tePrice = tePrice;
	}

	/**
	 * @return the vatPrice
	 */
	@Column(name = "fin_srv_am_vat_price", nullable = true)
	public double getVatPrice() {
		return vatPrice;
	}

	/**
	 * @param vatPrice the vatPrice to set
	 */
	public void setVatPrice(double vatPrice) {
		this.vatPrice = vatPrice;
	}

	/**
	 * @return the treasuryType
	 */
	@Column(name = "tre_typ_id", nullable = false)
    @Convert(converter = ETreasuryType.class)
	public ETreasuryType getTreasuryType() {
		return treasuryType;
	}

	/**
	 * @param treasuryType the treasuryType to set
	 */
	public void setTreasuryType(ETreasuryType treasuryType) {
		this.treasuryType = treasuryType;
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
	 * @return the paidBeginContract
	 */
	@Column(name = "fin_srv_bl_paid_begin_contract", nullable = true)
	public boolean isPaidBeginContract() {
		return paidBeginContract;
	}

	/**
	 * @param paidBeginContract the paidBeginContract to set
	 */
	public void setPaidBeginContract(boolean paidBeginContract) {
		this.paidBeginContract = paidBeginContract;
	}
	
	/**
	 * @return the financialProductServices
	 */
	@OneToMany(mappedBy="service", fetch = FetchType.LAZY)
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
	 * @return the allowChangePrice
	 */
	@Column(name = "fin_srv_bl_allow_change_price", nullable = true)
	public boolean isAllowChangePrice() {
		return allowChangePrice;
	}

	/**
	 * @param allowChangePrice the allowChangePrice to set
	 */
	public void setAllowChangePrice(boolean allowChangePrice) {
		this.allowChangePrice = allowChangePrice;
	}

	/**
	 * @return the splitWithInstallment
	 */
	@Column(name = "fin_srv_bl_split_with_installment", nullable = true)
	public boolean isSplitWithInstallment() {
		return splitWithInstallment;
	}

	/**
	 * @param splitWithInstallment the splitWithInstallment to set
	 */
	public void setSplitWithInstallment(boolean splitWithInstallment) {
		this.splitWithInstallment = splitWithInstallment;
	}

	/**
	 * @return the contractDuration
	 */
	@Column(name = "fin_srv_bl_contract_duration", nullable = true, columnDefinition = "boolean default false")
	public boolean isContractDuration() {
		return contractDuration;
	}
	/**
	 * @param contractDuration the contractDuration to set
	 */
	public void setContractDuration(boolean contractDuration) {
		this.contractDuration = contractDuration;
	}
	
	/**
	 * @return the termInMonths
	 */
	@Column(name = "fin_srv_nu_term_in_months", nullable = true)
	public Integer getTermInMonths() {
		return termInMonths;
	}

	/**
	 * @param termInMonths the termInMonths to set
	 */
	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	/**
	 * @return the dueDate
	 */
	@Column(name = "fin_srv_dt_due", nullable = true)
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
	/**
	 * @return the percentageOfAssetFirstYear
	 */
	@Column(name = "fin_srv_rt_pc_asset_first_year", nullable = true)
	public Double getPercentageOfAssetFirstYear() {
		return percentageOfAssetFirstYear;
	}

	/**
	 * @param percentageOfAssetFirstYear the percentageOfAssetFirstYear to set
	 */
	public void setPercentageOfAssetFirstYear(Double percentageOfAssetFirstYear) {
		this.percentageOfAssetFirstYear = percentageOfAssetFirstYear;
	}

	/**
	 * @return the percentageOfAssetSecondYear
	 */
	@Column(name = "fin_srv_rt_pc_asset_second_year", nullable = true)
	public Double getPercentageOfAssetSecondYear() {
		return percentageOfAssetSecondYear;
	}

	/**
	 * @param percentageOfAssetSecondYear the percentageOfAssetSecondYear to set
	 */
	public void setPercentageOfAssetSecondYear(Double percentageOfAssetSecondYear) {
		this.percentageOfAssetSecondYear = percentageOfAssetSecondYear;
	}

	/**
	 * @return the percentageOfAssetThirdYear
	 */
	@Column(name = "fin_srv_rt_pc_asset_third_year", nullable = true)
	public Double getPercentageOfAssetThirdYear() {
		return percentageOfAssetThirdYear;
	}

	/**
	 * @param percentageOfAssetThirdYear the percentageOfAssetThirdYear to set
	 */
	public void setPercentageOfAssetThirdYear(Double percentageOfAssetThirdYear) {
		this.percentageOfAssetThirdYear = percentageOfAssetThirdYear;
	}

	/**
	 * @return the percentageOfAssetForthYear
	 */
	@Column(name = "fin_srv_rt_pc_asset_forth_year", nullable = true)
	public Double getPercentageOfAssetForthYear() {
		return percentageOfAssetForthYear;
	}

	/**
	 * @param percentageOfAssetForthYear the percentageOfAssetForthYear to set
	 */
	public void setPercentageOfAssetForthYear(Double percentageOfAssetForthYear) {
		this.percentageOfAssetForthYear = percentageOfAssetForthYear;
	}

	/**
	 * @return the percentageOfAssetFifthYear
	 */
	@Column(name = "fin_srv_rt_pc_asset_fifth_year", nullable = true)
	public Double getPercentageOfAssetFifthYear() {
		return percentageOfAssetFifthYear;
	}

	/**
	 * @param percentageOfAssetFifthYear the percentageOfAssetFifthYear to set
	 */
	public void setPercentageOfAssetFifthYear(Double percentageOfAssetFifthYear) {
		this.percentageOfAssetFifthYear = percentageOfAssetFifthYear;
	}

	/**
	 * @return the percentageOfPremiumFirstYear
	 */
	@Column(name = "fin_srv_rt_pc_premium_first_year", nullable = true)
	public Double getPercentageOfPremiumFirstYear() {
		return percentageOfPremiumFirstYear;
	}

	/**
	 * @param percentageOfPremiumFirstYear the percentageOfPremiumFirstYear to set
	 */
	public void setPercentageOfPremiumFirstYear(Double percentageOfPremiumFirstYear) {
		this.percentageOfPremiumFirstYear = percentageOfPremiumFirstYear;
	}

	/**
	 * @return the percentageOfPremiumSecondYear
	 */
	@Column(name = "fin_srv_rt_pc_premium_second_year", nullable = true)
	public Double getPercentageOfPremiumSecondYear() {
		return percentageOfPremiumSecondYear;
	}

	/**
	 * @param percentageOfPremiumSecondYear the percentageOfPremiumSecondYear to set
	 */
	public void setPercentageOfPremiumSecondYear(
			Double percentageOfPremiumSecondYear) {
		this.percentageOfPremiumSecondYear = percentageOfPremiumSecondYear;
	}

	/**
	 * @return the percentageOfPremiumThirdYear
	 */
	@Column(name = "fin_srv_rt_pc_premium_third_year", nullable = true)
	public Double getPercentageOfPremiumThirdYear() {
		return percentageOfPremiumThirdYear;
	}

	/**
	 * @param percentageOfPremiumThirdYear the percentageOfPremiumThirdYear to set
	 */
	public void setPercentageOfPremiumThirdYear(Double percentageOfPremiumThirdYear) {
		this.percentageOfPremiumThirdYear = percentageOfPremiumThirdYear;
	}

	/**
	 * @return the percentageOfPremiumForthYear
	 */
	@Column(name = "fin_srv_rt_pc_premium_forth_year", nullable = true)
	public Double getPercentageOfPremiumForthYear() {
		return percentageOfPremiumForthYear;
	}

	/**
	 * @param percentageOfPremiumForthYear the percentageOfPremiumForthYear to set
	 */
	public void setPercentageOfPremiumForthYear(Double percentageOfPremiumForthYear) {
		this.percentageOfPremiumForthYear = percentageOfPremiumForthYear;
	}

	/**
	 * @return the percentageOfPremiumFifthYear
	 */
	@Column(name = "fin_srv_rt_pc_premium_fifth_year", nullable = true)
	public Double getPercentageOfPremiumFifthYear() {
		return percentageOfPremiumFifthYear;
	}

	/**
	 * @param percentageOfPremiumFifthYear the percentageOfPremiumFifthYear to set
	 */
	public void setPercentageOfPremiumFifthYear(Double percentageOfPremiumFifthYear) {
		this.percentageOfPremiumFifthYear = percentageOfPremiumFifthYear;
	}

	/**
	 * @return the formula
	 */
	@Column(name = "fin_srv_va_formula", nullable = true, length = 255)
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	/**
	 * @return the paidOneShot
	 */
	@Column(name = "fin_srv_bl_paid_one_shot", nullable = true, columnDefinition = "boolean default false")
	public boolean isPaidOneShot() {
		return paidOneShot;
	}

	/**
	 * @param paidOneShot the paidOneShot to set
	 */
	public void setPaidOneShot(boolean paidOneShot) {
		this.paidOneShot = paidOneShot;
	}
	
	/**
	 * @return the journalEvent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jou_eve_id")
	public JournalEvent getJournalEvent() {
		return journalEvent;
	}

	/**
	 * @param journalEvent the journalEvent to set
	 */
	public void setJournalEvent(JournalEvent journalEvent) {
		this.journalEvent = journalEvent;
	}
}
