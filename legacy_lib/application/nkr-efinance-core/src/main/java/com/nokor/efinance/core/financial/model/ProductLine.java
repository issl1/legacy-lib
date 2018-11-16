package com.nokor.efinance.core.financial.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * 
 * @author @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_product_line")
public class ProductLine extends EntityRefA {

	private static final long serialVersionUID = 7084217116190417583L;
	
	private EProductLineType productLineType;
	
	private Organization financialCompany;
	
	private Vat vatCapital;
	private Vat vatInterest;
	private PenaltyRule penaltyRule;
	private LockSplitRule lockSplitRule;
	
	private EOptionality guarantorRequirement;
	private EOptionality collateralRequirement;
	private EOptionality referenceRequirement;
	
	private EPaymentCondition paymentConditionFin;
	private EPaymentCondition paymentConditionCap;
	private EPaymentCondition paymentConditionIap;
	private EPaymentCondition paymentConditionIma;
	private EPaymentCondition paymentConditionFee;
	private EPaymentCondition paymentConditionLoss;
	
	private ERoundingFormat roundingFormat;
	
	
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_lin_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
    /**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
    @Transient
	@Override
	public String getCode() {
		return "XXX";
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "pro_lin_desc", nullable = false, length = 50)
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
    @Column(name = "pro_lin_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
        
	/**
	 * @return the productLineType
	 */
    @Column(name = "pro_lin_typ_id", nullable = false)
    @Convert(converter = EProductLineType.class)
	public EProductLineType getProductLineType() {
		return productLineType;
	}

	/**
	 * @param productLineType the productLineType to set
	 */
	public void setProductLineType(EProductLineType productLineType) {
		this.productLineType = productLineType;
	}
	
	/**
	 * @return the financialCompany
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_id")
	public Organization getFinancialCompany() {
		return financialCompany;
	}

	/**
	 * @param financialCompany the financialCompany to set
	 */
	public void setFinancialCompany(Organization financialCompany) {
		this.financialCompany = financialCompany;
	}
	
	/**
	 * @return the vat
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_id_cap")
	public Vat getVatCapital() {
		return vatCapital;
	}

	/**
	 * @param vatCapital the vatCapital to set
	 */
	public void setVatCapital(Vat vatCapital) {
		this.vatCapital = vatCapital;
	}

	
	/**
	 * @return the vatInterest
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_id_iap")
	public Vat getVatInterest() {
		return vatInterest;
	}

	/**
	 * @param vatInterest the vatInterest to set
	 */
	public void setVatInterest(Vat vatInterest) {
		this.vatInterest = vatInterest;
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
	 * @return the guarantorRequirement
	 */
	@Column(name = "opt_id_guarantor", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getGuarantorRequirement() {
		return guarantorRequirement;
	}

	/**
	 * @param guarantorRequirement the guarantorRequirement to set
	 */
	public void setGuarantorRequirement(EOptionality guarantorRequirement) {
		this.guarantorRequirement = guarantorRequirement;
	}

	/**
	 * @return the collateralRequirement
	 */
	@Column(name = "opt_id_collateral", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getCollateralRequirement() {
		return collateralRequirement;
	}

	/**
	 * @param collateralRequirement the collateralRequirement to set
	 */
	public void setCollateralRequirement(EOptionality collateralRequirement) {
		this.collateralRequirement = collateralRequirement;
	}

	/**
	 * @return the referenceRequirement
	 */
	@Column(name = "opt_id_reference", nullable = true)
	@Convert(converter = EOptionality.class)
	public EOptionality getReferenceRequirement() {
		return referenceRequirement;
	}

	/**
	 * @param referenceRequirement the referenceRequirement to set
	 */
	public void setReferenceRequirement(EOptionality referenceRequirement) {
		this.referenceRequirement = referenceRequirement;
	}

	/**
	 * @return the paymentConditionFin
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_fin")
	public EPaymentCondition getPaymentConditionFin() {
		return paymentConditionFin;
	}

	/**
	 * @param paymentConditionFin the paymentConditionFin to set
	 */
	public void setPaymentConditionFin(EPaymentCondition paymentConditionFin) {
		this.paymentConditionFin = paymentConditionFin;
	}

	/**
	 * @return the paymentConditionCap
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_cap")
	public EPaymentCondition getPaymentConditionCap() {
		return paymentConditionCap;
	}

	/**
	 * @param paymentConditionCap the paymentConditionCap to set
	 */
	public void setPaymentConditionCap(EPaymentCondition paymentConditionCap) {
		this.paymentConditionCap = paymentConditionCap;
	}
	

	/**
	 * @return the paymentConditionIap
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_iap")
	public EPaymentCondition getPaymentConditionIap() {
		return paymentConditionIap;
	}

	/**
	 * @param paymentConditionIap the paymentConditionIap to set
	 */
	public void setPaymentConditionIap(EPaymentCondition paymentConditionIap) {
		this.paymentConditionIap = paymentConditionIap;
	}

	/**
	 * @return the paymentConditionIma
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_ima")
	public EPaymentCondition getPaymentConditionIma() {
		return paymentConditionIma;
	}

	/**
	 * @param paymentConditionIma the paymentConditionIma to set
	 */
	public void setPaymentConditionIma(EPaymentCondition paymentConditionIma) {
		this.paymentConditionIma = paymentConditionIma;
	}

	/**
	 * @return the paymentConditionFee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_fee")
	public EPaymentCondition getPaymentConditionFee() {
		return paymentConditionFee;
	}

	/**
	 * @param paymentConditionFee the paymentConditionFee to set
	 */
	public void setPaymentConditionFee(EPaymentCondition paymentConditionFee) {
		this.paymentConditionFee = paymentConditionFee;
	}
	

	/**
	 * @return the paymentConditionLoss
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id_loss")
	public EPaymentCondition getPaymentConditionLoss() {
		return paymentConditionLoss;
	}

	/**
	 * @param paymentConditionLoss the paymentConditionLoss to set
	 */
	public void setPaymentConditionLoss(EPaymentCondition paymentConditionLoss) {
		this.paymentConditionLoss = paymentConditionLoss;
	}

	/**
	 * @return the roundingFormat
	 */
	@Column(name = "pro_lin_installment_rouding_id", nullable = true)
    @Convert(converter = ERoundingFormat.class)
	public ERoundingFormat getRoundingFormat() {
		return roundingFormat;
	}

	/**
	 * @param roundingFormat the roundingFormat to set
	 */
	public void setRoundingFormat(ERoundingFormat roundingFormat) {
		this.roundingFormat = roundingFormat;
	}	
}
