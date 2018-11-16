package com.nokor.ersys.messaging.share.organization;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class BaseOrganizationDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 1495350522414883662L;

	private Long id;
	private Date createDate;
	private Date openingDate;
	private String code;
	private String name;
	private String nameEn;
	private String desc;
	private String descEn;
	private String tel;
	private String mobile;
	private String email;
	private String slogan;
	private String website;
	private String logoPath;
	private String externalCode;
	private String vatRegistrationNo;
	private String licenceNo;
	private RefDataDTO country;
	private String status;
	private RefDataDTO legalType;
	private RefDataDTO companySector;
	private RefDataDTO companySize;
	
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
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the openingDate
	 */
	public Date getOpeningDate() {
		return openingDate;
	}

	/**
	 * @param openingDate the openingDate to set
	 */
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}
	
	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	/**
	 * @return the desc
	 */
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
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}
	
	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	/**
	 * @return the logoPath
	 */
	public String getLogoPath() {
		return logoPath;
	}
	
	/**
	 * @param logoPath the logoPath to set
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	
	/**
	 * @return the externalCode
	 */
	public String getExternalCode() {
		return externalCode;
	}
	
	/**
	 * @param externalCode the externalCode to set
	 */
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	
	/**
	 * @return the vatRegistrationNo
	 */
	public String getVatRegistrationNo() {
		return vatRegistrationNo;
	}
	
	/**
	 * @param vatRegistrationNo the vatRegistrationNo to set
	 */
	public void setVatRegistrationNo(String vatRegistrationNo) {
		this.vatRegistrationNo = vatRegistrationNo;
	}
	
	/**
	 * @return the licenceNo
	 */
	public String getLicenceNo() {
		return licenceNo;
	}
	
	/**
	 * @param licenceNo the licenceNo to set
	 */
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}
	
	/**
	 * @return the country
	 */
	public RefDataDTO getCountry() {
		return country;
	}
	
	/**
	 * @param country the country to set
	 */
	public void setCountry(RefDataDTO country) {
		this.country = country;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the legalType
	 */
	public RefDataDTO getLegalType() {
		return legalType;
	}

	/**
	 * @param legalType the legalType to set
	 */
	public void setLegalType(RefDataDTO legalType) {
		this.legalType = legalType;
	}

	/**
	 * @return the companySector
	 */
	public RefDataDTO getCompanySector() {
		return companySector;
	}

	/**
	 * @param companySector the companySector to set
	 */
	public void setCompanySector(RefDataDTO companySector) {
		this.companySector = companySector;
	}

	/**
	 * @return the companySize
	 */
	public RefDataDTO getCompanySize() {
		return companySize;
	}

	/**
	 * @param companySize the companySize to set
	 */
	public void setCompanySize(RefDataDTO companySize) {
		this.companySize = companySize;
	}
	
}
