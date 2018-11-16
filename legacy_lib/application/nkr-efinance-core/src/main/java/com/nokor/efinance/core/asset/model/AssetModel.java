package com.nokor.efinance.core.asset.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Asset Model Class.
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_asset_model")
public class AssetModel extends EntityRefA implements MAssetModel {

	private static final long serialVersionUID = -5660457637173007034L;
	
	private String serie;
	private String characteristic;
	private Integer year;
	private String cc;
	private double tiPrice;
	private double tePrice;
	private double vatPrice;
	private Double averageMarketPrice;
	private Double maxAdvancePaymentPercentage;
	private AssetRange assetRange;	
	private EFinAssetType assetType;
	private AssetCategory assetCategory;
	private ECalculMethod calculMethod;
	private boolean allowChangePrice;
	private EEngine engine;
	
	/**
     * 
     * @return
     */
    public static AssetModel createInstance() {
    	AssetModel instance = EntityFactory.createInstance(AssetModel.class);
        return instance;
    }
	
	/**
     * Get asset model's is.
     * @return The asset model's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_mod_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "ass_mod_code", nullable = true, length=50, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset model's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_mod_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset model's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_mod_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }
        
	/**
	 * @return the tiPrice
	 */
    @Column(name = "ass_mod_am_ti_price", nullable = true)
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
	@Column(name = "ass_mod_am_te_price", nullable = true)
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
	@Column(name = "ass_mod_am_vat_price", nullable = true)
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
	 * @return the averageMarketPrice
	 */
	@Column(name = "ass_mod_te_average_market_price", nullable = true)
	public Double getAverageMarketPrice() {
		return averageMarketPrice;
	}

	/**
	 * @param averageMarketPrice the averageMarketPrice to set
	 */
	public void setAverageMarketPrice(Double averageMarketPrice) {
		this.averageMarketPrice = averageMarketPrice;
	}

	/**
	 * @return the maxAdvancePaymentPercentage
	 */
	@Column(name = "ass_mod_max_advance_payment_pc", nullable = true)
	public Double getMaxAdvancePaymentPercentage() {
		return maxAdvancePaymentPercentage;
	}

	/**
	 * @param maxAdvancePaymentPercentage the maxAdvancePaymentPercentage to set
	 */
	public void setMaxAdvancePaymentPercentage(Double maxAdvancePaymentPercentage) {
		this.maxAdvancePaymentPercentage = maxAdvancePaymentPercentage;
	}

	/**
	 * @return the assetType
	 */
    @Column(name = "ass_typ_id", nullable = false)
    @Convert(converter = EFinAssetType.class)
	public EFinAssetType getAssetType() {
		return assetType;
	}

	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(final EFinAssetType assetType) {
		this.assetType = assetType;
	}
	
	
	/**
	 * @return the assetRange
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_ran_id")
	public AssetRange getAssetRange() {
		return assetRange;
	}

	/**
	 * @param assetRange the assetRange to set
	 */
	public void setAssetRange(final AssetRange assetRange) {
		this.assetRange = assetRange;
	}
	
	/**
	 * @return the allowChangePrice
	 */
	@Column(name = "ass_mod_bl_allow_change_price", nullable = true)
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
	 * @return the calculMethod
	 */
    @Column(name = "cal_met_id", nullable = false)
    @Convert(converter = ECalculMethod.class)
	public ECalculMethod getCalculMethod() {
		return calculMethod;
	}

	public void setCalculMethod(ECalculMethod calculMethod) {
		this.calculMethod = calculMethod;
	}

	/**
	 * @return the serie
	 */
	@Column(name = "ass_mod_va_serie", nullable = true, length = 50)
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the characteristic
	 */
	@Column(name = "ass_mod_va_characteristic", nullable = true, length = 100)
	public String getCharacteristic() {
		return characteristic;
	}

	/**
	 * @param characteristic the characteristic to set
	 */
	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	/**
	 * @return the year
	 */
	@Column(name = "ass_mod_nu_year", nullable = true)
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the cc
	 */
	@Column(name = "ass_mod_va_cc", nullable = true, length = 20)
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
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
	 * @return the engine
	 */
	@Column(name = "eng_id", nullable = false)
    @Convert(converter = EEngine.class)
	public EEngine getEngine() {
		return engine;
	}

	/**
	 * @param engine the engine to set
	 */
	public void setEngine(EEngine engine) {
		this.engine = engine;
	}
		
}
