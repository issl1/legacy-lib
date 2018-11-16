package com.nokor.efinance.core.dealer.model;

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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "tu_dealer_attribute")
public class DealerAttribute extends EntityA implements MDealerAttribute {
		
	/** */
	private static final long serialVersionUID = 3049944775519022073L;
	
	private Dealer dealer;
	private AssetMake assetMake;
	private AssetCategory assetCategory;
	private Integer insuranceCoverageDuration;
	private Double tiContractFeeAmount;
	private Double vatContractFeeAmount;
	private Double teContractFeeAmount;
	
	
	private EChargePoint contractFeeChargePoint;
	
	private Double teCommission1Amount;
	private Double vatCommission1Amount;
	private Double tiCommission1Amount;
	private double vatValue;
	
	private EChargePoint commission1ChargePoint;
		
	private EPaymentMethod paymentMethod;
	private String payeeName;
	private DealerBankAccount dealerBankAccount;
	
	private boolean commission2Enabled;
	private ELadderOption ladderOption;
	private LadderType ladderType;
	
	private boolean insuranceFeeEnabled;
	private EChargePoint insuranceFeeChargePoint;
	private Organization insuranceCompany;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_att_id", unique = true, nullable = false)
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
	 * @return the assetMake
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_mak_id")
	public AssetMake getAssetMake() {
		return assetMake;
	}

	/**
	 * @param assetMake the assetMake to set
	 */
	public void setAssetMake(AssetMake assetMake) {
		this.assetMake = assetMake;
	}

	/**
	 * @return the assetCategory
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_cat_id")
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}

	/**
	 * @return the insuranceCoverageDuration
	 */
	@Column(name = "dea_att_ins_coverage_duration", nullable = true)
	public Integer getInsuranceCoverageDuration() {
		return insuranceCoverageDuration;
	}

	/**
	 * @param insuranceCoverageDuration the insuranceCoverageDuration to set
	 */
	public void setInsuranceCoverageDuration(Integer insuranceCoverageDuration) {
		this.insuranceCoverageDuration = insuranceCoverageDuration;
	}

	/**
	 * @return the tiContractFeeAmount
	 */
	@Column(name = "dea_att_ti_contract_fee_amount", nullable = true)
	public Double getTiContractFeeAmount() {
		return tiContractFeeAmount;
	}

	/**
	 * @param tiContractFeeAmount the tiContractFeeAmount to set
	 */
	public void setTiContractFeeAmount(Double tiContractFeeAmount) {
		this.tiContractFeeAmount = tiContractFeeAmount;
	}

	/**
	 * @return the vatContractFeeAmount
	 */
	@Column(name = "dea_att_vat_contract_fee_amount", nullable = true)
	public Double getVatContractFeeAmount() {
		return vatContractFeeAmount;
	}

	/**
	 * @param vatContractFeeAmount the vatContractFeeAmount to set
	 */
	public void setVatContractFeeAmount(Double vatContractFeeAmount) {
		this.vatContractFeeAmount = vatContractFeeAmount;
	}

	/**
	 * @return the teContractFeeAmount
	 */
	@Column(name = "dea_att_te_contract_fee_amount", nullable = true)
	public Double getTeContractFeeAmount() {
		return teContractFeeAmount;
	}

	/**
	 * @param teContractFeeAmount the teContractFeeAmount to set
	 */
	public void setTeContractFeeAmount(Double teContractFeeAmount) {
		this.teContractFeeAmount = teContractFeeAmount;
	}

	/**
	 * @return the contractFeeChargePoint
	 */
	@Column(name = "cha_pnt_id_cnt_fee", nullable = true)
    @Convert(converter = EChargePoint.class)
	public EChargePoint getContractFeeChargePoint() {
		return contractFeeChargePoint;
	}

	/**
	 * @param contractFeeChargePoint the contractFeeChargePoint to set
	 */
	public void setContractFeeChargePoint(EChargePoint contractFeeChargePoint) {
		this.contractFeeChargePoint = contractFeeChargePoint;
	}

	/**
	 * @return the teCommission1Amount
	 */
	@Column(name = "dea_att_te_commission1_amount", nullable = true)
	public Double getTeCommission1Amount() {
		return teCommission1Amount;
	}

	/**
	 * @param teCommission1Amount the teCommission1Amount to set
	 */
	public void setTeCommission1Amount(Double teCommission1Amount) {
		this.teCommission1Amount = teCommission1Amount;
	}

	/**
	 * @return the vatCommission1Amount
	 */
	@Column(name = "dea_att_vat_commission1_amount", nullable = true)
	public Double getVatCommission1Amount() {
		return vatCommission1Amount;
	}

	/**
	 * @param vatCommission1Amount the vatCommission1Amount to set
	 */
	public void setVatCommission1Amount(Double vatCommission1Amount) {
		this.vatCommission1Amount = vatCommission1Amount;
	}

	/**
	 * @return the tiCommission1Amount
	 */
	@Column(name = "dea_att_ti_commission1_amount", nullable = true)
	public Double getTiCommission1Amount() {
		return tiCommission1Amount;
	}

	/**
	 * @param tiCommission1Amount the tiCommission1Amount to set
	 */
	public void setTiCommission1Amount(Double tiCommission1Amount) {
		this.tiCommission1Amount = tiCommission1Amount;
	}

	/**
	 * @return the vatValue
	 */
	@Column(name = "dea_att_rt_vat", nullable = true, columnDefinition="double precision default '0'")
	public double getVatValue() {
		return vatValue;
	}

	/**
	 * @param vatValue the vatValue to set
	 */
	public void setVatValue(double vatValue) {
		this.vatValue = vatValue;
	}

	/**
	 * @return the paymentMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_met_id")
	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	/**
	 * @return the payeeName
	 */
	@Column(name = "dea_att_payee_name", nullable = true, length = 100)
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @return the dealerBankAccount
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_ban_id")
	public DealerBankAccount getDealerBankAccount() {
		return dealerBankAccount;
	}

	/**
	 * @param dealerBankAccount the dealerBankAccount to set
	 */
	public void setDealerBankAccount(DealerBankAccount dealerBankAccount) {
		this.dealerBankAccount = dealerBankAccount;
	}

	/**
	 * @return the commission1ChargePoint
	 */
	@Column(name = "cha_pnt_id_commissiom1", nullable = true)
    @Convert(converter = EChargePoint.class)
	public EChargePoint getCommission1ChargePoint() {
		return commission1ChargePoint;
	}

	/**
	 * @param commission1ChargePoint the commission1ChargePoint to set
	 */
	public void setCommission1ChargePoint(EChargePoint commission1ChargePoint) {
		this.commission1ChargePoint = commission1ChargePoint;
	}

	/**
	 * @return the commission2Enable
	 */
	@Column(name = "dea_att_commission2_enabled", nullable = true)
	public boolean isCommission2Enabled() {
		return commission2Enabled;
	}

	/**
	 * @param commission2Enabled the commission2Enabled to set
	 */
	public void setCommission2Enabled(boolean commission2Enabled) {
		this.commission2Enabled = commission2Enabled;
	}	
	
	/**
	 * @return the ladderOption
	 */
	@Column(name = "lad_opt_id", nullable = true)
    @Convert(converter = ELadderOption.class)
	public ELadderOption getLadderOption() {
		return ladderOption;
	}

	/**
	 * @param ladderOption the ladderOption to set
	 */
	public void setLadderOption(ELadderOption ladderOption) {
		this.ladderOption = ladderOption;
	}

	/**
	 * @return the ladderType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lad_typ_id", nullable = true)
	public LadderType getLadderType() {
		return ladderType;
	}

	/**
	 * @param ladderType the ladderType to set
	 */
	public void setLadderType(LadderType ladderType) {
		this.ladderType = ladderType;
	}

	/**
	 * @return the insuranceFeeEnabled
	 */
	@Column(name = "dea_att_ins_fee_enabled", nullable = true, columnDefinition = "boolean default false")
	public boolean isInsuranceFeeEnabled() {
		return insuranceFeeEnabled;
	}

	/**
	 * @param insuranceFeeEnabled the insuranceFeeEnabled to set
	 */
	public void setInsuranceFeeEnabled(boolean insuranceFeeEnabled) {
		this.insuranceFeeEnabled = insuranceFeeEnabled;
	}

	/**
	 * @return the insuranceFeeChargePoint
	 */
	@Column(name = "cha_pnt_id_ins_fee", nullable = true)
    @Convert(converter = EChargePoint.class)
	public EChargePoint getInsuranceFeeChargePoint() {
		return insuranceFeeChargePoint;
	}

	/**
	 * @param insuranceFeeChargePoint the insuranceFeeChargePoint to set
	 */
	public void setInsuranceFeeChargePoint(EChargePoint insuranceFeeChargePoint) {
		this.insuranceFeeChargePoint = insuranceFeeChargePoint;
	}
	
	/**
	 * @return the insuranceCompany
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ins_comp_id", nullable = true)
	public Organization getInsuranceCompany() {
		return insuranceCompany;
	}

	/**
	 * @param insuranceCompany the insuranceCompany to set
	 */
	public void setInsuranceCompany(Organization insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	
}
