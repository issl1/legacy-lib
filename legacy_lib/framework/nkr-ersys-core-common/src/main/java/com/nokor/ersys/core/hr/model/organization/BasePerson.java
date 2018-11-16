package com.nokor.ersys.core.hr.model.organization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.content.model.file.FileData;
import com.nokor.common.app.eref.ECountry;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;

/**
 * @author ly.youhort
 */
@MappedSuperclass
public abstract class BasePerson extends EntityWkf implements MBasePerson {
	/** */
	private static final long serialVersionUID = 3469386575982741317L;

	protected String lastName;
	protected String lastNameEn;
	protected String firstName;
	protected String firstNameEn;
	private String nickName;
	private ETypeIdNumber typeIdNumber;
	private String idNumber;
	private Date issuingIdNumberDate;
	private Date expiringIdNumberDate;
	
	protected ECivility civility;
	protected Date birthDate;
	protected Province placeOfBirth;
	protected EGender gender;
	protected EMaritalStatus maritalStatus;
	protected ENationality nationality;
	protected String mobilePerso;
	protected String mobilePerso2;
	protected String emailPerso;		
	private FileData photo;
	private ECountry birthCountry;


	/**
	 * @return the lastName
	 */
	@Column(name = "per_lastname", nullable = true, length = 100)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the lastNameEn
	 */
	@Column(name = "per_lastname_en", nullable = true, length = 100)
	public String getLastNameEn() {
		return lastNameEn;
	}

	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}
	
    @Transient
    public String getCivilityAndFullName() {
    	if (civility != null && getFullNameLocale() != null) {
    		return civility.getDescLocale() + " " + getFullNameLocale();
    	}
    	else if (getFullNameLocale() != null) {
    		return getFullNameLocale();
    	}
    	else if (civility != null) {
    		return civility.getDescLocale();
    	}
    	return "";
    }
    
	/**
     * @return the fullName
     */
    @Transient
    public String getFullName() {
        return getLastNameFirstName();
    }

    /**
     * @return The full name in English
     */
    @Transient
    public String getFullNameEn() {
    	return getLastNameFirstNameEn();
    }
    
    @Transient
    public String getFullNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getFullNameEn();
    	} else {
    		return getFullName();
    	}
    }
	
	/**
     * @return the lastNameFirstName
     */
    @Transient
    public String getLastNameFirstName() {
        String str = "";
        if (StringUtils.isNotEmpty(getLastName())) {
        	str += getLastName();
        }
        if (StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(getFirstName())) {
        	str += " " + getFirstName();
        } else if (StringUtils.isNotEmpty(getFirstName())) {
        	str = getFirstName();
        }
        
        return str;
    }

    /**
     * 
     * @return The last name first name in English
     */
    @Transient
    public String getLastNameFirstNameEn() {
    	String str = "";
        if (StringUtils.isNotEmpty(getLastNameEn())) {
        	str += getLastNameEn();
        }
        if (StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(getFirstNameEn())) {
        	str += " " + getFirstNameEn();
        } else if (StringUtils.isNotEmpty(getFirstNameEn())) {
        	str = getFirstNameEn();
        }
        
        return str;
    }
    
    /**
     * 
     * @return The last name first name 
     */
    @Transient
    public String getLastNameFirstNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getLastNameFirstNameEn();
    	} else {
    		return getLastNameFirstName();
    	}
    }

    /**
     * 
     * @return
     */
	@Transient
    public String getLastNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getLastNameEn();
    	} else {
    		return getLastName();
    	}
    }

	/**
	 * @return the firstName
	 */
	@Column(name = "per_firstname", nullable = true, length = 100)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the firstNameEn
	 */
	@Column(name = "per_firstname_en", nullable = true, length = 100)
	public String getFirstNameEn() {
		return firstNameEn;
	}

	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}
	
    /**
     * 
     * @return
     */
	@Transient
    public String getFirstNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getFirstNameEn();
    	} else {
    		return getFirstName();
    	}
    }
	
	/**
	 * @return the nickName
	 */
	@Column(name = "per_nickname", nullable = true, length = 50)
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}	

	/**
	 * @return the typeIdNumber
	 */
	@Column(name = "tid_id", nullable = true)
    @Convert(converter = ETypeIdNumber.class)
	public ETypeIdNumber getTypeIdNumber() {
		return typeIdNumber;
	}

	/**
	 * @param typeIdNumber the typeIdNumber to set
	 */
	public void setTypeIdNumber(ETypeIdNumber typeIdNumber) {
		this.typeIdNumber = typeIdNumber;
	}

	/**
	 * @return the idNumber
	 */
	@Column(name = "per_id_number", nullable = true, length = 50)
	public String getIdNumber() {
		return idNumber;
	}		


	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	/**
	 * @return the issuingIdNumberDate
	 */
	@Column(name = "per_dt_issuing_id_number", nullable = true)
	public Date getIssuingIdNumberDate() {
		return issuingIdNumberDate;
	}

	/**
	 * @param issuingIdNumberDate the issuingIdNumberDate to set
	 */
	public void setIssuingIdNumberDate(Date issuingIdNumberDate) {
		this.issuingIdNumberDate = issuingIdNumberDate;
	}

	/**
	 * @return the expiringIdNumberDate
	 */
	@Column(name = "per_dt_expiring_id_number", nullable = true)
	public Date getExpiringIdNumberDate() {
		return expiringIdNumberDate;
	}

	/**
	 * @param expiringIdNumberDate the expiringIdNumberDate to set
	 */
	public void setExpiringIdNumberDate(Date expiringIdNumberDate) {
		this.expiringIdNumberDate = expiringIdNumberDate;
	}


	/**
	 * @return the Civility
	 */
    @Column(name = "civ_id", nullable = true)
    @Convert(converter = ECivility.class)
	public ECivility getCivility() {
		return civility;
	}

	/**
	 * @param civility the civility to set
	 */
	public void setCivility(ECivility civility) {
		this.civility = civility;
	}

	/**
	 * @return the birthDate
	 */
	@Column(name = "per_birth_date", nullable = true)
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * @return the gender
	 */
    @Column(name = "gen_id", nullable = true)
    @Convert(converter = EGender.class)
	public EGender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(EGender gender) {
		this.gender = gender;
	}

	/**
	 * @return the maritalStatus
	 */
    @Column(name = "mar_sta_id", nullable = true)
    @Convert(converter = EMaritalStatus.class)
	public EMaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(EMaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the nationality
	 */
    @Column(name = "nat_id", nullable = true)
    @Convert(converter = ENationality.class)
	public ENationality getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(ENationality nationality) {
		this.nationality = nationality;
	}
	

	/**
	 * @return the mobilePerso
	 */
	@Column(name = "per_mobile_perso", nullable = true, length = 30)
	public String getMobilePerso() {
		return mobilePerso;
	}

	/**
	 * @param mobilePerso the mobilePerso to set
	 */
	public void setMobilePerso(String mobilePerso) {
		this.mobilePerso = mobilePerso;
	}
	
	/**
	 * @return the mobilePerso2
	 */
	@Column(name = "per_mobile_perso2", nullable = true, length = 30)
	public String getMobilePerso2() {
		return mobilePerso2;
	}

	/**
	 * @param mobilePerso2 the mobilePerso2 to set
	 */
	public void setMobilePerso2(String mobilePerso2) {
		this.mobilePerso2 = mobilePerso2;
	}

	/**
	 * @return the emailPerso
	 */
	@Column(name = "per_email_perso", nullable = true, length = 60)
	public String getEmailPerso() {
		return emailPerso;
	}

	/**
	 * @param emailPerso the emailPerso to set
	 */
	public void setEmailPerso(String emailPerso) {
		this.emailPerso = emailPerso;
	}

	/**
	 * @return the placeOfBirth
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
	public Province getPlaceOfBirth() {
		return placeOfBirth;
	}

	/**
	 * @param placeOfBirth the placeOfBirth to set
	 */
	public void setPlaceOfBirth(Province placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	/**
	 * @return the photo
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo_fil_dat_id")
	public FileData getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(FileData photo) {
		this.photo = photo;
	}

	/**
	 * @return the birthCountry
	 */
	@Column(name = "emp_birth_cou_id", nullable = true)
	@Convert(converter = ECountry.class)
	public ECountry getBirthCountry() {
		return birthCountry;
	}

	/**
	 * @param birthCountry
	 *            the birthCountry to set
	 */
	public void setBirthCountry(ECountry birthCountry) {
		this.birthCountry = birthCountry;
	}

	
}
