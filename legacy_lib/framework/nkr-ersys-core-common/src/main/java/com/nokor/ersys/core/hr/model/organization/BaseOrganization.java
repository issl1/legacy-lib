package com.nokor.ersys.core.hr.model.organization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.eref.ECountry;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.eref.ECompanySize;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.ELegalForm;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseOrganization extends EntityWkf implements MBaseOrganization {
	/** */
	private static final long serialVersionUID = -6652059399905096407L;

	private String code;
	private String name;
	private String nameEn;
	private String desc;
	private String descEn;
	private String tel;
	private String mobile;
	private String fax;
	private String email;
	private String slogan;
	private String website;
	private String logoPath;
	private Date startDate;

	private String externalCode;
	private String vatRegistrationNo;
	private Date registrationDate;
	private String registrationPlace;
	
	private String licenceNo;

    private ETypeOrganization typeOrganization;			// Main, Agency, .. 
	private ESubTypeOrganization subTypeOrganization;

	private ECompanySize companySize;
	private EEmploymentIndustry employmentIndustry;
	private ELegalForm legalForm;
    private ECountry country;

    /**
     * 
     * @return
     */
    public static <T extends BaseOrganization> T createInstance(Class<T> clazzOrg) {
        T org = EntityFactory.createInstance(clazzOrg);
        org.setTypeOrganization(ETypeOrganization.MAIN);
        return org;
    }
    
    
    /**
	 * @return the code
	 */
	@Column(name = "com_code", nullable = true, length = 100)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "com_name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }
    

    /**
	 * @return the nameEn
	 */
	@Column(name = "com_name_en", nullable = true, length = 100)
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
	@Column(name = "com_desc", nullable = true)
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
	@Column(name = "com_desc_en", nullable = true)
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
	 * @return the slogan
	 */
	@Column(name = "com_slogan", nullable = true, length = 150)
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	@Column(name = "com_email", nullable = true, length = 100)
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    @Column(name = "com_tel", nullable = true, length = 30)
    public String getTel() {
        return tel;
    }

    /**
     * 
     * @param tel
     */
    public void setTel(final String tel) {
        this.tel = tel;
    }

    /**
	 * @return the mobile
	 */
    @Column(name = "com_mobile", nullable = true, length = 50)
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
	 * @return the fax
	 */
	 @Column(name = "com_fax", nullable = true, length = 30)
	public String getFax() {
		return fax;
	}


	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}


	@Column(name = "com_website", nullable = true, length = 150)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    /**
	 * @return the logoPath
	 */
    @Column(name = "com_logo_path", nullable = true, length = 255)
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
	 * @return the startDate
	 */
	@Column(name = "com_start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the externalCode
	 */
	@Column(name = "com_external_code", nullable = true, length = 50)
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
	@Column(name = "com_vat_number", nullable = true, length = 20)
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
	 * @return the registrationDate
	 */
	@Column(name = "com_registration_date", nullable = true)
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
	 * @return the registrationPlace
	 */
	@Column(name = "com_registration_place", nullable = true, length = 50)
	public String getRegistrationPlace() {
		return registrationPlace;
	}


	/**
	 * @param registrationPlace the registrationPlace to set
	 */
	public void setRegistrationPlace(String registrationPlace) {
		this.registrationPlace = registrationPlace;
	}


	/**
	 * @return the licenceNo
	 */
	@Column(name = "com_licence_number", nullable = true, length = 20)
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
     * 
     * @return
     */
    @Column(name = "typ_org_id", nullable = false)
    @Convert(converter = ETypeOrganization.class)
    public ETypeOrganization getTypeOrganization() {
        return typeOrganization;
    }

    /**
     * 
     * @param typeOrganization
     */
    public void setTypeOrganization(ETypeOrganization typeOrganization) {
        this.typeOrganization = typeOrganization;
    }

	/**
	 * @return the subTypeOrganization
	 */
    @Column(name = "sub_typ_org_id", nullable = true)
    @Convert(converter = ESubTypeOrganization.class)
	public ESubTypeOrganization getSubTypeOrganization() {
		return subTypeOrganization;
	}

	/**
	 * @param subTypeOrganization the subTypeOrganization to set
	 */
	public void setSubTypeOrganization(ESubTypeOrganization subTypeOrganization) {
		this.subTypeOrganization = subTypeOrganization;
	}	
	
	@Column(name = "emp_ind_id", nullable = true)
    @Convert(converter = EEmploymentIndustry.class)
	public EEmploymentIndustry getEmploymentIndustry() {
		return employmentIndustry;
	}

	/**
	 * @param employmentIndustry the employmentIndustry to set
	 */
	public void setEmploymentIndustry(EEmploymentIndustry employmentIndustry) {
		this.employmentIndustry = employmentIndustry;
	}
	
	/**
	 * @return the legalForm
	 */
	@Column(name = "leg_for_id", nullable = true)
    @Convert(converter = ELegalForm.class)
	public ELegalForm getLegalForm() {
		return legalForm;
	}

	/**
	 * @param legalForm the legalForm to set
	 */
	public void setLegalForm(ELegalForm legalForm) {
		this.legalForm = legalForm;
	}	

	/**
	 * @return the companySize
	 */
	@Column(name = "com_siz_id", nullable = true)
    @Convert(converter = ECompanySize.class)
	public ECompanySize getCompanySize() {
		return companySize;
	}


	/**
	 * @param companySize the companySize to set
	 */
	public void setCompanySize(ECompanySize companySize) {
		this.companySize = companySize;
	}


	/**
	 * @return the country
	 */
    @Column(name = "cou_id", nullable = true)
    @Convert(converter = ECountry.class)
	public ECountry getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(ECountry country) {
		this.country = country;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getNameLocale() {
		if (I18N.isEnglishLocale()) {
    		return getNameEn();
    	} else {
    		return getName();
    	}
	}

	
}
