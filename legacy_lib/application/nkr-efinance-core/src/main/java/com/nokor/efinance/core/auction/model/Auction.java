package com.nokor.efinance.core.auction.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.asset.model.AssetExteriorStatus;
import com.nokor.efinance.core.asset.model.AssetPartsStatus;
import com.nokor.efinance.core.asset.model.AssetPlateNumber;
import com.nokor.efinance.core.asset.model.AssetRegistrationStatus;
import com.nokor.efinance.core.asset.model.EEngineStatus;
import com.nokor.efinance.core.contract.model.Contract;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_auction")
public class Auction extends EntityWkf implements MAuction {
	/** */
	private static final long serialVersionUID = -6853915649974341577L;
	
	private Double penaltyUsd;
	private Double repossessionFeeUsd;
	private Double collectionFeeUsd;
	private Double teAssetEstimationPrice1;
	private Double vatAssetEstimationPrice1;
	private Double tiAssetEstimationPrice1;
	private Double teAssetEstimationPrice2;
	private Double vatAssetEstimationPrice2;
	private Double tiAssetEstimationPrice2;
	private Double teAssetEstimationPrice3;
	private Double vatAssetEstimationPrice3;
	private Double tiAssetEstimationPrice3;
	private Double teAssetSellingPrice;
	private Double vatAssetSellingPrice;
	private Double tiAssetSellingPrice;
	
	private Integer milleage;
	private AssetExteriorStatus assetExteriorStatus;
	private EEngineStatus assetEnginStatus;
	private AssetPartsStatus assetPartsStatus;
	private AssetRegistrationStatus assetRegistrationStatus;
	private AssetPlateNumber assetPlateNumber;
	private Buyer buyer;
	private String purchaseAgreementFile;
	
	private Contract contract;
	
	private Date sellingDate;
	private Date requestRepossessedDate;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
	/**
	 * @return the contract
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "con_id", unique = true)
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
	 * @return the penaltyUsd
	 */
    @Column(name = "auc_am_penalty_usd", nullable = true)
	public Double getPenaltyUsd() {
		return penaltyUsd;
	}

	/**
	 * @param penaltyUsd the penaltyUsd to set
	 */
	public void setPenaltyUsd(Double penaltyUsd) {
		this.penaltyUsd = penaltyUsd;
	}

	/**
	 * @return the repossessionFeeUsd
	 */
    @Column(name = "auc_am_repossession_fee_usd", nullable = true)
	public Double getRepossessionFeeUsd() {
		return repossessionFeeUsd;
	}

	/**
	 * @param repossessionFeeUsd the repossessionFeeUsd to set
	 */
	public void setRepossessionFeeUsd(Double repossessionFeeUsd) {
		this.repossessionFeeUsd = repossessionFeeUsd;
	}

	/**
	 * @return the collectionFeeUsd
	 */
	@Column(name = "auc_am_collection_fee_usd", nullable = true)
	public Double getCollectionFeeUsd() {
		return collectionFeeUsd;
	}

	/**
	 * @param collectionFeeUsd the collectionFeeUsd to set
	 */
	public void setCollectionFeeUsd(Double collectionFeeUsd) {
		this.collectionFeeUsd = collectionFeeUsd;
	}

	/**
	 * @return the teAssetEstimationPrice1
	 */
	@Column(name = "auc_am_te_asset_estimation_price_1", nullable = true)
	public Double getTeAssetEstimationPrice1() {
		return teAssetEstimationPrice1;
	}

	/**
	 * @param teAssetEstimationPrice1 the teAssetEstimationPrice1 to set
	 */
	public void setTeAssetEstimationPrice1(Double teAssetEstimationPrice1) {
		this.teAssetEstimationPrice1 = teAssetEstimationPrice1;
	}

	/**
	 * @return the vatAssetEstimationPrice1
	 */
	@Column(name = "auc_am_vat_asset_estimation_price_1", nullable = true)
	public Double getVatAssetEstimationPrice1() {
		return vatAssetEstimationPrice1;
	}

	/**
	 * @param vatAssetEstimationPrice1 the vatAssetEstimationPrice1 to set
	 */
	public void setVatAssetEstimationPrice1(Double vatAssetEstimationPrice1) {
		this.vatAssetEstimationPrice1 = vatAssetEstimationPrice1;
	}

	/**
	 * @return the tiAssetEstimationPrice1
	 */
	@Column(name = "auc_am_ti_asset_estimation_price_1", nullable = true)
	public Double getTiAssetEstimationPrice1() {
		return tiAssetEstimationPrice1;
	}

	/**
	 * @param tiAssetEstimationPrice1 the tiAssetEstimationPrice1 to set
	 */
	public void setTiAssetEstimationPrice1(Double tiAssetEstimationPrice1) {
		this.tiAssetEstimationPrice1 = tiAssetEstimationPrice1;
	}

	/**
	 * @return the teAssetEstimationPrice2
	 */
	@Column(name = "auc_am_te_asset_estimation_price_2", nullable = true)
	public Double getTeAssetEstimationPrice2() {
		return teAssetEstimationPrice2;
	}

	/**
	 * @param teAssetEstimationPrice2 the teAssetEstimationPrice2 to set
	 */
	public void setTeAssetEstimationPrice2(Double teAssetEstimationPrice2) {
		this.teAssetEstimationPrice2 = teAssetEstimationPrice2;
	}

	/**
	 * @return the vatAssetEstimationPrice2
	 */
	@Column(name = "auc_am_vat_asset_estimation_price_2", nullable = true)
	public Double getVatAssetEstimationPrice2() {
		return vatAssetEstimationPrice2;
	}

	/**
	 * @param vatAssetEstimationPrice2 the vatAssetEstimationPrice2 to set
	 */
	public void setVatAssetEstimationPrice2(Double vatAssetEstimationPrice2) {
		this.vatAssetEstimationPrice2 = vatAssetEstimationPrice2;
	}

	/**
	 * @return the tiAssetEstimationPrice2
	 */
	@Column(name = "auc_am_ti_asset_estimation_price_2", nullable = true)
	public Double getTiAssetEstimationPrice2() {
		return tiAssetEstimationPrice2;
	}

	/**
	 * @param tiAssetEstimationPrice2 the tiAssetEstimationPrice2 to set
	 */
	public void setTiAssetEstimationPrice2(Double tiAssetEstimationPrice2) {
		this.tiAssetEstimationPrice2 = tiAssetEstimationPrice2;
	}

	/**
	 * @return the teAssetEstimationPrice3
	 */
	@Column(name = "auc_am_te_asset_estimation_price_3", nullable = true)
	public Double getTeAssetEstimationPrice3() {
		return teAssetEstimationPrice3;
	}

	/**
	 * @param teAssetEstimationPrice3 the teAssetEstimationPrice3 to set
	 */
	public void setTeAssetEstimationPrice3(Double teAssetEstimationPrice3) {
		this.teAssetEstimationPrice3 = teAssetEstimationPrice3;
	}

	/**
	 * @return the vatAssetEstimationPrice3
	 */
	@Column(name = "auc_am_vat_asset_estimation_price_3", nullable = true)
	public Double getVatAssetEstimationPrice3() {
		return vatAssetEstimationPrice3;
	}

	/**
	 * @param vatAssetEstimationPrice3 the vatAssetEstimationPrice3 to set
	 */
	public void setVatAssetEstimationPrice3(Double vatAssetEstimationPrice3) {
		this.vatAssetEstimationPrice3 = vatAssetEstimationPrice3;
	}

	/**
	 * @return the tiAssetEstimationPrice3
	 */
	@Column(name = "auc_am_ti_asset_estimation_price_3", nullable = true)
	public Double getTiAssetEstimationPrice3() {
		return tiAssetEstimationPrice3;
	}

	/**
	 * @param tiAssetEstimationPrice3 the tiAssetEstimationPrice3 to set
	 */
	public void setTiAssetEstimationPrice3(Double tiAssetEstimationPrice3) {
		this.tiAssetEstimationPrice3 = tiAssetEstimationPrice3;
	}	

	/**
	 * @return the teAssetSellingPrice
	 */
	@Column(name = "auc_am_te_asset_selling_price", nullable = true)
	public Double getTeAssetSellingPrice() {
		return teAssetSellingPrice;
	}

	/**
	 * @param teAssetSellingPrice the teAssetSellingPrice to set
	 */
	public void setTeAssetSellingPrice(Double teAssetSellingPrice) {
		this.teAssetSellingPrice = teAssetSellingPrice;
	}

	/**
	 * @return the vatAssetSellingPrice
	 */
	@Column(name = "auc_am_vat_asset_selling_price", nullable = true)
	public Double getVatAssetSellingPrice() {
		return vatAssetSellingPrice;
	}

	/**
	 * @param vatAssetSellingPrice the vatAssetSellingPrice to set
	 */
	public void setVatAssetSellingPrice(Double vatAssetSellingPrice) {
		this.vatAssetSellingPrice = vatAssetSellingPrice;
	}

	/**
	 * @return the tiAssetSellingPrice
	 */
	@Column(name = "auc_am_ti_asset_selling_price", nullable = true)
	public Double getTiAssetSellingPrice() {
		return tiAssetSellingPrice;
	}

	/**
	 * @param tiAssetSellingPrice the tiAssetSellingPrice to set
	 */
	public void setTiAssetSellingPrice(Double tiAssetSellingPrice) {
		this.tiAssetSellingPrice = tiAssetSellingPrice;
	}

	/**
	 * @return the milleage
	 */
	@Column(name = "auc_nu_milleage", nullable = true)
	public Integer getMilleage() {
		return milleage;
	}

	/**
	 * @param milleage the milleage to set
	 */
	public void setMilleage(Integer milleage) {
		this.milleage = milleage;
	}

	/**
	 * @return the assetExteriorStatus
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_ext_sta_id")
	public AssetExteriorStatus getAssetExteriorStatus() {
		return assetExteriorStatus;
	}

	/**
	 * @param assetExteriorStatus the assetExteriorStatus to set
	 */
	public void setAssetExteriorStatus(AssetExteriorStatus assetExteriorStatus) {
		this.assetExteriorStatus = assetExteriorStatus;
	}

	/**
	 * @return the assetEnginStatus
	 */
    @Column(name = "ass_eng_sta_id", nullable = true)
    @Convert(converter = EEngineStatus.class)
	public EEngineStatus getAssetEnginStatus() {
		return assetEnginStatus;
	}

	/**
	 * @param assetEnginStatus the assetEnginStatus to set
	 */
	public void setAssetEnginStatus(EEngineStatus assetEnginStatus) {
		this.assetEnginStatus = assetEnginStatus;
	}

	/**
	 * @return the assetPartsStatus
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_par_sta_id")
	public AssetPartsStatus getAssetPartsStatus() {
		return assetPartsStatus;
	}

	/**
	 * @param assetPartsStatus the assetPartsStatus to set
	 */
	public void setAssetPartsStatus(AssetPartsStatus assetPartsStatus) {
		this.assetPartsStatus = assetPartsStatus;
	}

	/**
	 * @return the assetRegistrationStatus
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_reg_sta_id")
	public AssetRegistrationStatus getAssetRegistrationStatus() {
		return assetRegistrationStatus;
	}

	/**
	 * @param assetRegistrationStatus the assetRegistrationStatus to set
	 */
	public void setAssetRegistrationStatus(
			AssetRegistrationStatus assetRegistrationStatus) {
		this.assetRegistrationStatus = assetRegistrationStatus;
	}

	/**
	 * @return the assetPlateNumber
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_pla_id")
	public AssetPlateNumber getAssetPlateNumber() {
		return assetPlateNumber;
	}

	/**
	 * @param assetPlateNumber the assetPlateNumber to set
	 */
	public void setAssetPlateNumber(AssetPlateNumber assetPlateNumber) {
		this.assetPlateNumber = assetPlateNumber;
	}

	/**
	 * @return the buyer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
	//@Index(name="idx_auc_buyer_id")
	public Buyer getBuyer() {
		return buyer;
	}

	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	/**
	 * @return the purchaseAgreementFile
	 */
	@Column(name = "auc_am_purchase_agreement_file", nullable = true)
	public String getPurchaseAgreementFile() {
		return purchaseAgreementFile;
	}

	/**
	 * @param purchaseAgreementFile the purchaseAgreementFile to set
	 */
	public void setPurchaseAgreementFile(String purchaseAgreementFile) {
		this.purchaseAgreementFile = purchaseAgreementFile;
	}
	
	/**
	 * @return the sellingDate
	 */
	@Column(name = "auc_dt_selling", nullable = true)
	public Date getSellingDate() {
		return sellingDate;
	}

	/**
	 * @param sellingDate the sellingDate to set
	 */
	public void setSellingDate(Date sellingDate) {
		this.sellingDate = sellingDate;
	}
	/**
	 * 
	 * @return the request repossessed date set by Collection Supervisor.
	 */
	@Column(name = "auc_dt_req_repossess", nullable = true)
	public Date getRequestRepossessedDate() {
		return requestRepossessedDate;
	}

	public void setRequestRepossessedDate(Date repossessedDate) {
		this.requestRepossessedDate = repossessedDate;
	}

	/**
	 * @return 
	 */
	@Transient
	@Override
	public List<AuctionWkfHistoryItem> getHistories() {
		return (List<AuctionWkfHistoryItem>) getHistories();
	}


}
