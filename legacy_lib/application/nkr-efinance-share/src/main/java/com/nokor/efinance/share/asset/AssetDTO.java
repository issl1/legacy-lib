package com.nokor.efinance.share.asset;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;

/**
 * @author youhort.ly
 */
public class AssetDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 863380265012236244L;
	
	private Long id;
	private String code;

	private String assetStatus;
	private RefDataDTO color;
	private Integer year;
	private RefDataDTO engine;
	private Double assetPrice;
	private Double vat;
	
	private String grade;
	
	private AssetModelDTO assetModel;
	
	private Date registrationDate;
	private ProvinceDTO registrationProvince;
	private RefDataDTO registrationPlateType;
		
	private String registrationNumber;
	private String chassisNumber;
	private String engineNumber;
		
	private String riderName;	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
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
	 * @return the assetStatus
	 */
	public String getAssetStatus() {
		return assetStatus;
	}

	/**
	 * @param assetStatus the assetStatus to set
	 */
	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	/**
	 * @return the year
	 */
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
	 * @return the assetPrice
	 */
	public Double getAssetPrice() {
		return assetPrice;
	}

	/**
	 * @param assetPrice the assetPrice to set
	 */
	public void setAssetPrice(Double assetPrice) {
		this.assetPrice = assetPrice;
	}

	/**
	 * @return the vat
	 */
	public Double getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(Double vat) {
		this.vat = vat;
	}

	/**
	 * @return the grade
	 */
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
	 * @return the assetModel
	 */
	public AssetModelDTO getAssetModel() {
		return assetModel;
	}

	/**
	 * @param assetModel the assetModel to set
	 */
	public void setAssetModel(AssetModelDTO assetModel) {
		this.assetModel = assetModel;
	}

	/**
	 * @return the registrationDate
	 */
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
	 * @return the registrationProvince
	 */
	public ProvinceDTO getRegistrationProvince() {
		return registrationProvince;
	}

	/**
	 * @param registrationProvince the registrationProvince to set
	 */
	public void setRegistrationProvince(ProvinceDTO registrationProvince) {
		this.registrationProvince = registrationProvince;
	}

	/**
	 * @return the registrationNumber
	 */
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	/**
	 * @param registrationNumber the registrationNumber to set
	 */
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	/**
	 * @return the chassisNumber
	 */
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
	 * @return the riderName
	 */
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
	 * @return the color
	 */
	public RefDataDTO getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(RefDataDTO color) {
		this.color = color;
	}

	/**
	 * @return the engine
	 */
	public RefDataDTO getEngine() {
		return engine;
	}

	/**
	 * @param engine the engine to set
	 */
	public void setEngine(RefDataDTO engine) {
		this.engine = engine;
	}

	/**
	 * @return the registrationPlateType
	 */
	public RefDataDTO getRegistrationPlateType() {
		return registrationPlateType;
	}

	/**
	 * @param registrationPlateType the registrationPlateType to set
	 */
	public void setRegistrationPlateType(RefDataDTO registrationPlateType) {
		this.registrationPlateType = registrationPlateType;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AssetDTO)) {
			 return false;
		 }
		 AssetDTO assetDTO = (AssetDTO) arg0;
		 return getId() != null && getId().equals(assetDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
