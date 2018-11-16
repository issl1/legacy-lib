package com.nokor.efinance.core.asset.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.EColor;



/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractAsset extends EntityA {
	/** */
	private static final long serialVersionUID = 863380265012236244L;

	private String serie;
	private String code;
	private String desc;
	private String descEn;
	private EColor color;
	private Integer year;
	private EEngine engine;
	private EAssetGender assetGender;
	private Double tiAssetPrice;
	private Double teAssetPrice;
	private Double vatAssetPrice;
	private double vatValue;
	
	private String grade;
	
	private AssetModel model;
	
	private Date registrationDate;
	private Province registrationProvince;
	private ERegPlateType registrationPlateType;
	private ERegBookStatus registrationBookStatus;

	private String taxInvoiceNumber;
	private String plateNumber;
	private String chassisNumber;
	private String engineNumber;
		
	private boolean hasChanged = false;
	private boolean modelUsed;
	private Integer mileage;

	private String riderName;
	private Integer sortIndex;
	
	//private double taxInvoiceAmount;
	
	
    @Column(name = "ass_code", nullable = true, length = 20)
    public String getCode() {
        return code;
    }    

    /**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return the serie
	 */
	@Column(name = "ass_va_serie", nullable = true, length = 50)
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
     * Get the asset's description in locale language.
     * 
     * @return <String>
     */
    @Column(name = "ass_desc", nullable = true, length = 120)
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
     * Get the asset's name in English.
     * 
     * @return <String>
     */
    @Column(name = "ass_desc_en", nullable = true, length = 120)
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
	 * @return the color
	 */
    @Column(name = "col_id", nullable = true)
    @Convert(converter = EColor.class)
	public EColor getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(EColor color) {
		this.color = color;
	}

	/**
	 * @return the year
	 */
	@Column(name = "ass_nu_year", nullable = true)
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
	 * @return the assetGender
	 */
    @Column(name = "ass_gen_id", nullable = true)
    @Convert(converter = EAssetGender.class)
	public EAssetGender getAssetGender() {
		return assetGender;
	}

	/**
	 * @param assetGender the assetGender to set
	 */
	public void setAssetGender(EAssetGender assetGender) {
		this.assetGender = assetGender;
	}

	/**
	 * @return the engine
	 */
    @Column(name = "eng_id", nullable = true)
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
	
	/**
	 * @return the tiAssetPrice
	 */
	@Column(name = "ass_am_ti_ass_price", nullable = true)
	public Double getTiAssetPrice() {
		return tiAssetPrice;
	}

	/**
	 * @param tiAssetPrice the tiAssetPrice to set
	 */
	public void setTiAssetPrice(Double tiAssetPrice) {
		this.tiAssetPrice = tiAssetPrice;
	}

	/**
	 * @return the teAssetPrice
	 */
	@Column(name = "ass_am_te_ass_price", nullable = true)
	public Double getTeAssetPrice() {
		return teAssetPrice;
	}

	/**
	 * @param teAssetPrice the teAssetPrice to set
	 */
	public void setTeAssetPrice(Double teAssetPrice) {
		this.teAssetPrice = teAssetPrice;
	}

	/**
	 * @return the vatAssetPrice
	 */
	@Column(name = "ass_am_vat_ass_price", nullable = true)
	public Double getVatAssetPrice() {
		return vatAssetPrice;
	}

	/**
	 * @param vatAssetPrice the vatAssetPrice to set
	 */
	public void setVatAssetPrice(Double vatAssetPrice) {
		this.vatAssetPrice = vatAssetPrice;
	}
	
	/**
	 * @return the vatValue
	 */
	@Column(name = "ass_rt_vat", nullable = true, columnDefinition="double precision default '0'")
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
	 * @return the grade
	 */
	@Column(name = "ass_va_grade", nullable = true, length = 2)
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the model
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_mod_id", nullable = false)
	public AssetModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(AssetModel model) {
		this.model = model;
	}

	/**
	 * @return the registrationDate
	 */
	@Column(name = "ass_dt_registration", nullable = true)
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}	
	
	/**
	 * @return the taxInvoiceNumber
	 */
	@Column(name = "ass_va_tax_invoice_number", nullable = true, length = 50)
	public String getTaxInvoiceNumber() {
		return taxInvoiceNumber;
	}

	/**
	 * @param taxInvoiceNumber the taxInvoiceNumber to set
	 */
	public void setTaxInvoiceNumber(String taxInvoiceNumber) {
		this.taxInvoiceNumber = taxInvoiceNumber;
	}

	/**
	 * @return the plateNumber
	 */
	@Column(name = "ass_va_plate_number", nullable = true, length = 30)
	public String getPlateNumber() {
		return plateNumber;
	}

	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	/**
	 * @return the chassisNumber
	 */
	@Column(name = "ass_va_chassis_number", nullable = true, length = 50)
	public String getChassisNumber() {
		return chassisNumber;
	}

	/**
	 * @param chassisNumber the chassisNumber to set
	 */
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	/**
	 * @return the engineNumber
	 */
	@Column(name = "ass_va_engine_number", nullable = true, length = 50)
	public String getEngineNumber() {
		return engineNumber;
	}

	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	
	/**
	 * @return the hasChanged
	 */
	@Transient
	public boolean isHasChanged() {
		return hasChanged;
	}

	/**
	 * @param hasChanged the hasChanged to set
	 */
	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	
	/**
	 * @return the modelUsed
	 */
	@Column(name = "ass_bl_model_used", nullable = true, columnDefinition = "boolean default false")
	public boolean isModelUsed() {
		return modelUsed;
	}

	/**
	 * @param modelUsed the modelUsed to set
	 */
	public void setModelUsed(boolean modelUsed) {
		this.modelUsed = modelUsed;
	}

	/**
	 * @return the mileage
	 */
	@Column(name = "ass_nu_mileage", nullable = true)
	public Integer getMileage() {
		return mileage;
	}

	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the riderName
	 */
	@Column(name = "ass_va_rider_name", nullable = true, length = 200)
	public String getRiderName() {
		return riderName;
	}

	/**
	 * @param riderName the riderName to set
	 */
	public void setRiderName(String riderName) {
		this.riderName = riderName;
	}

	/**
	 * @return the registrationProvince
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_reg_id")
	public Province getRegistrationProvince() {
		return registrationProvince;
	}

	/**
	 * @param registrationProvince the registrationProvince to set
	 */
	public void setRegistrationProvince(Province registrationProvince) {
		this.registrationProvince = registrationProvince;
	}

	/**
	 * @return the registrationPlateType
	 */
	@Column(name = "reg_pla_typ_id", nullable = true)
    @Convert(converter = ERegPlateType.class)
	public ERegPlateType getRegistrationPlateType() {
		return registrationPlateType;
	}

	/**
	 * @param registrationPlateType the registrationPlateType to set
	 */
	public void setRegistrationPlateType(ERegPlateType registrationPlateType) {
		this.registrationPlateType = registrationPlateType;
	}	
	
	/**
	 * @return the registrationBookStatus
	 */
	@Column(name = "reg_boo_sta_id", nullable = true)
    @Convert(converter = ERegBookStatus.class)
	public ERegBookStatus getRegistrationBookStatus() {
		return registrationBookStatus;
	}

	/**
	 * @param registrationBookStatus the registrationBookStatus to set
	 */
	public void setRegistrationBookStatus(ERegBookStatus registrationBookStatus) {
		this.registrationBookStatus = registrationBookStatus;
	}

	/**
	 * @return the sort index
	 */
	@Column(name = "sort_index", nullable = true)
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
    public AssetRange getAssetRange() {
    	AssetRange assetRange = model.getAssetRange();
		return assetRange;
    }
	
	/**
	 * 
	 * @return
	 */
	@Transient
    public AssetMake getAssetMake() {
    	AssetMake assetMake = getAssetRange().getAssetMake();
		return assetMake;
    }
	
	/**
     * 
     * @return
     */
	@Transient
    public String getModelDescLocale() {
		AssetRange model = getAssetRange();
		return model == null ? "" : model.getDescLocale();
    }
	
	/**
     * 
     * @return
     */
	@Transient
    public String getBrandDescLocale() {
    	AssetMake brand = getAssetMake();
    	return brand == null ? "" : brand.getDescLocale();
    }
}
