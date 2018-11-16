package com.nokor.ersys.core.hr.model.organization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seuksa.frmk.tools.security.CryptoHelper;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.model.SecUserAware;

/**
 * 
 * @author prasnar
 * 
 */
@MappedSuperclass
public abstract class Staff extends BasePerson implements SecUserAware, MStaff {
	/** */
	private static final long serialVersionUID = 6061097032779777186L;

	private static final String REF_BLANK = "REF_BLANK";
	
	private Long version;

	private String reference;
	private Date enrollmentDate;
	private Date quitDate;
	private String emailPro;
	private String telPro;
	private String mobilePro;
	private String fax;
	private String comment;
	private EJobPosition jobPosition;
    private ETypeContact typeContact;

	private Address address; // Current Address

    // Use to store decryptable keys
    private String empTmp1;
    private String empTmp2;
	private SecUser secUser;

	/**
	 * @return the reference
	 */
	@Column(name = "emp_reference", unique = true,  nullable = true, length = 20)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	/**
	 * 
	 */
	public void setEmptyReference() {
		this.reference = REF_BLANK;
	}

	/**
	 * @return the enrollmentDate
	 */
	@Column(name = "emp_enrollment_date", nullable = true)
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	/**
	 * @param enrollmentDate the enrollmentDate to set
	 */
	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	/**
	 * @return the quitDate
	 */
	@Column(name = "emp_quit_date", nullable = true)
	public Date getQuitDate() {
		return quitDate;
	}

	/**
	 * @param quitDate the quitDate to set
	 */
	public void setQuitDate(Date quitDate) {
		this.quitDate = quitDate;
	}

	/**
	 * @return the emailPro
	 */
	@Column(name = "emp_email_pro", nullable = true)
	public String getEmailPro() {
		return emailPro;
	}

	/**
	 * @param emailPro the emailPro to set
	 */
	public void setEmailPro(String emailPro) {
		this.emailPro = emailPro;
	}

	
	/**
	 * @return the telPro
	 */
	@Column(name = "emp_tel_pro", nullable = true)
	public String getTelPro() {
		return telPro;
	}

	/**
	 * @param telPro the telPro to set
	 */
	public void setTelPro(String telPro) {
		this.telPro = telPro;
	}

	/**
	 * @return the mobilePro
	 */
	@Column(name = "emp_mobile_pro", nullable = true)
	public String getMobilePro() {
		return mobilePro;
	}

	/**
	 * @param mobilePro the mobilePro to set
	 */
	public void setMobilePro(String mobilePro) {
		this.mobilePro = mobilePro;
	}

	/**
	 * @return the fax
	 */
	@Column(name = "emp_fax", nullable = true, length = 60)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}



	/**
	 * @return the jobPosition
	 */
    @Column(name = "job_pos_id", nullable = true)
    @Convert(converter = EJobPosition.class)
	public EJobPosition getJobPosition() {
		return jobPosition;
	}

	/**
	 * @param jobPosition the jobPosition to set
	 */
	public void setJobPosition(EJobPosition jobPosition) {
		this.jobPosition = jobPosition;
	}

    /**
     * @return the typeContact
     */
    @Column(name = "typ_con_id", nullable = true)
    @Convert(converter = ETypeContact.class)
    public ETypeContact getTypeContact() {
        return typeContact;
    }

    /**
     * @param typeContact the typeContact to set
     */
    public void setTypeContact(ETypeContact typeContact) {
        this.typeContact = typeContact;
    }

	/**
	 * @return the comment
	 */
	@Column(name = "emp_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "add_id", nullable = true)
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}


	/**
	 * @return the empTmp1
	 */
	@Column(name = "emp_tmp1")
	public String getEmpTmp1() {
		return empTmp1;
	}

	/**
	 * @param empTmp1 the empTmp1 to set
	 */
	public void setEmpTmp1(String empTmp1) {
		this.empTmp1 = empTmp1;
	}

	/**
	 * @return the empTmp2
	 */
	@Column(name = "emp_tmp2")
	public String getEmpTmp2() {
		return empTmp2;
	}

	/**
	 * @param empTmp2 the empTmp2 to set
	 */
	public void setEmpTmp2(String empTmp2) {
		this.empTmp2 = empTmp2;
	}

	

	/**
	 * @return the user
	 */
	@Override
    @ManyToOne (fetch=FetchType.LAZY, cascade = {})
    @JoinColumn(name="sec_usr_id", nullable = true)
 	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	@Override
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	/**
	 * @return the version
	 */
    @Version
    @Column(name = "version", nullable = false)
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

    @Transient
	public boolean isUserFO() {
		return getSecUser().hasAccessToApp(AppConfigFileHelper.getApplicationCodeFO());
	}
	
	@Transient
	public boolean isUserBO() {
		return getSecUser().hasAccessToApp(AppConfigFileHelper.getApplicationCodeBO());
	}
	
	@Transient
	public boolean isUserRA() {
		return getSecUser().hasAccessToApp(AppConfigFileHelper.getApplicationCodeRA());
	}
	
	@Transient
	public boolean isUserCMS() {
		return getSecUser().hasAccessToApp(AppConfigFileHelper.getApplicationCodeCMS());
	}
	
	/**
	 * 
	 * @param isUserBo
	 */
	public void setUserBO(boolean isUserBO) {
		getSecUser().grantAccessToApp(isUserBO, AppConfigFileHelper.getApplicationCodeBO());
	}
	
	/**
	 * 
	 * @param isUserFO
	 */
	public void setUserFO(boolean isUserFO) {
		getSecUser().grantAccessToApp(isUserFO, AppConfigFileHelper.getApplicationCodeFO());
	}
	
	/**
	 * 
	 * @param isUserRA
	 */
	public void setUserRA(boolean isUserRA) {
		getSecUser().grantAccessToApp(isUserRA, AppConfigFileHelper.getApplicationCodeRA());
	}
	
	/**
	 * 
	 * @param isUserCMS
	 */
	public void setUserCMS(boolean isUserCMS) {
		getSecUser().grantAccessToApp(isUserCMS, AppConfigFileHelper.getApplicationCodeCMS());
	}
	/**
	 * 
	 * @param passwordRaw
	 */
    public void setEmployeePwdInTmpforRecoverMode(String passwordRaw) {
		if (AppConfigFileHelper.isRecoverPwdMode() ||
				(AppConfigFileHelper.INIT_PWD_TOKEN.equals(getEmpTmp1()) 
						&& AppConfigFileHelper.INIT_PWD_TOKEN.equals(getEmpTmp2()))) {
			setEmpTmp1(CryptoHelper.encrypt(passwordRaw.substring(0, 5)));
			setEmpTmp2(CryptoHelper.encrypt(passwordRaw.substring(5)));
		} else {
			setEmpTmp1(null);
			setEmpTmp2(null);
		}
	}
	
	@Transient
	public void setPwdInTmp(String passwordRaw) {
		setEmployeePwdInTmpforRecoverMode(passwordRaw);
	}

	
}
