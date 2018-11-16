package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.IVersion;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nokor.frmk.config.model.ELocale;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.AuthorityFactory;
import com.nokor.frmk.security.SecProfileGrantedAuthority;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.spring.SaltedUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;

/**
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_sec_user")
public class SecUser extends EntityA implements MSecUser, UserDetails, SaltedUser, IVersion {
	/** */
	private static final long serialVersionUID = -4994748026472811207L;

	public final static String LOGIN_SEP_DUPLICATE = "-";
    public static final String NOT_AUTHENTICATED_USER = EntityA.NOT_AUTHENTICATED_USER;
    private Long version;

	private String login;
    private String desc;
    private String password;
    private Integer passwordFormat;
    private String passwordSalt;
    private String passwordQuestion;
    private String passwordAnswer;
    private Date expirationDate;
    private String email;
    private String comment;
    private Boolean isApproved;
    private Date lastLoginDate;
    private Date lastLogoutDate;
    private String lastLoginIp;
    private Boolean isConnected;
    private Date lastLockedOutDate;
    private Date lastPasswordChangeDate;
    private Integer failedPasswordAttemptCount = 0;
    private Integer failedPasswordAnswerAttemptsCount = 0;
    private SecProfile defaultProfile; 
    private ELocale localeType;
	
	private List<SecProfile> profiles;
	private String allProfilesDesc;
	private String allProfilesDescEn;

    private transient Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    /**
	 * 
	 * @return
	 */
	public static SecUser createInstance() {
		return SecurityEntityFactory.createSecUserInstance();
	}
	
    /**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_usr_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
	 * @return the login
	 */
    @Audited
    @Override
	@Column(name = "sec_usr_login", unique = true, nullable = false)
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @return the desc
	 */
	@Column(name = "sec_usr_desc", nullable = false)
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
	 * @return the expirationDate
	 */
	@Column(name = "sec_usr_expiration_date", nullable = true)
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the passwordFormat
	 */
	@Column(name = "sec_usr_format", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public Integer getPasswordFormat() {
		return passwordFormat;
	}

	/**
	 * @param passwordFormat the passwordFormat to set
	 */
	public void setPasswordFormat(Integer passwordFormat) {
		this.passwordFormat = passwordFormat;
	}

	/**
	 * @return the passwordSalt
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@Column(name = "sec_usr_salt", nullable = true)
	public String getPasswordSalt() {
		return passwordSalt;
	}

	/**
	 * @param passwordSalt the passwordSalt to set
	 */
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	
	/**
	 * 
	 */
	public void setPasswordSaltSelfProtection() {
		this.passwordSalt = SecSaltGenerator.generateSaltFromUser(this);
	}

	/**
	 * @return the passwordQuestion
	 */
	@Column(name = "sec_usr_question", nullable = true)
	public String getPasswordQuestion() {
		return passwordQuestion;
	}

	/**
	 * @param passwordQuestion the passwordQuestion to set
	 */
	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	/**
	 * @return the passwordAnswer
	 */
	@Column(name = "sec_usr_answer", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public String getPasswordAnswer() {
		return passwordAnswer;
	}

	/**
	 * @param passwordAnswer the passwordAnswer to set
	 */
	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	/**
	 * @return the email
	 */
	@Column(name = "sec_usr_email", nullable = true)
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
	 * @return the localeType
	 */
    @Column(name = "typ_loc_id", nullable = true)
    @Convert(converter = ELocale.class)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public ELocale getLocaleType() {
		return localeType;
	}

	/**
	 * @param localeType the localeType to set
	 */
	public void setLocaleType(ELocale localeType) {
		this.localeType = localeType;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "sec_usr_comment", nullable = true)
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
	 * @return the isApproved
	 */
	@Column(name = "sec_usr_is_approved", nullable = true)
	public Boolean isApproved() {
		return isApproved != null ? isApproved : Boolean.TRUE;
	}

	/**
	 * @param isApproved the isApproved to set
	 */
	public void setApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @return the isLockedOut
	 */
	@Transient
	public Boolean isLockedOut() {
		return !isActive();
	}

	/**
	 * @param isLockedOut the isLockedOut to set
	 */
	public void setLockedOut(Boolean isLockedOut) {
		setActive(isLockedOut != null ? isLockedOut : true);
	}

	/**
	 * @return the lastLoginDate
	 */
	@Audited
	@Column(name = "sec_usr_last_login_date", nullable = true)
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	

	/**
	 * @return the lastLogoutDate
	 */
	@Audited
	@Column(name = "sec_usr_last_logout_date", nullable = true)
	public Date getLastLogoutDate() {
		return lastLogoutDate;
	}

	/**
	 * @param lastLogoutDate the lastLogoutDate to set
	 */
	public void setLastLogoutDate(Date lastLogoutDate) {
		this.lastLogoutDate = lastLogoutDate;
	}

	/**
	 * @return the lastLoginIp
	 */
	@Audited
    @Column(name = "sec_usr_last_login_ip")
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * @param lastLoginIp the lastLoginIp to set
	 */
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	

	/**
	 * @return the isConnected
	 */
	@Column(name = "sec_usr_is_connected", nullable = true)
	public Boolean isConnected() {
		return isConnected;
	}

	/**
	 * @param isConnected the isConnected to set
	 */
	public void setConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * @return the lastLockedOutDate
	 */
	@Column(name = "sec_usr_last_locked_out_date", nullable = true)
	public Date getLastLockedOutDate() {
		return lastLockedOutDate;
	}

	/**
	 * @param lastLockedOutDate the lastLockedOutDate to set
	 */
	public void setLastLockedOutDate(Date lastLockedOutDate) {
		this.lastLockedOutDate = lastLockedOutDate;
	}

	/**
	 * @return the lastPasswordChangeDate
	 */
	@Column(name = "sec_usr_last_pwd_change_date", nullable = true)
	public Date getLastPasswordChangeDate() {
		return lastPasswordChangeDate != null ? lastPasswordChangeDate : getCreateDate();
	}

	/**
	 * @param lastPasswordChangeDate the lastPasswordChangeDate to set
	 */
	public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
		this.lastPasswordChangeDate = lastPasswordChangeDate;
	}

	/**
	 * @return the failedPasswordAttemptCount
	 */
	@Column(name = "sec_usr_failed_pwd_count", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public Integer getFailedPasswordAttemptCount() {
		if (failedPasswordAttemptCount == null) {
			failedPasswordAttemptCount = 0;
		}
		return failedPasswordAttemptCount;
	}

	/**
	 * @param failedPasswordAttemptCount the failedPasswordAttemptCount to set
	 */
	public void setFailedPasswordAttemptCount(Integer failedPasswordAttemptCount) {
		if (failedPasswordAttemptCount == null) {
			failedPasswordAttemptCount = 0;
		}
		this.failedPasswordAttemptCount = failedPasswordAttemptCount;
	}

	/**
	 * @return the failedPasswordAnswerAttemptCount
	 */
	@Column(name = "sec_usr_failed_pwd_answer_count", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public Integer getFailedPasswordAnswerAttemptsCount() {
		if (failedPasswordAnswerAttemptsCount == null) {
			failedPasswordAnswerAttemptsCount = 0;
		}
		return failedPasswordAnswerAttemptsCount;
	}

	/**
	 * @param failedPasswordAnswerAttemptsCount the failedPasswordAnswerAttemptCount to set
	 */
	public void setFailedPasswordAnswerAttemptsCount(Integer failedPasswordAnswerAttemptsCount) {
		if (failedPasswordAnswerAttemptsCount == null) {
			failedPasswordAnswerAttemptsCount = 0;
		}
		this.failedPasswordAnswerAttemptsCount = failedPasswordAnswerAttemptsCount;
	}
	
	/**
	 * 
	 */
	@Transient
	public void incrementFailedPasswordAnswerAttemptsCount() {
		failedPasswordAnswerAttemptsCount++;
	}
	
	/**
	 * 
	 */
	@Transient
	public void clearFailedPasswordAnswerAttemptsCount() {
		failedPasswordAnswerAttemptsCount = 0;
	}
	
	/**
	 * @return the profiles
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_sec_user_profile",
				joinColumns = { @JoinColumn(name = "sec_usr_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_pro_id") })
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public List<SecProfile> getProfiles() {
		if (profiles == null) {
			profiles = new ArrayList<SecProfile>();
		}
		return profiles;
	}
	

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<SecProfile> profiles) {
		this.profiles = profiles;
		if (defaultProfile == null && getProfiles().size() > 0) {
    		defaultProfile = getProfiles().get(0);
    	}
	}
	
	@Transient
	public boolean hasMultiProfiles() {
		return getProfiles().size() > 1;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public void addProfile(SecProfile secPro) {
		if (secPro == null) {
			return;
		}
		getProfiles().add(secPro);
		if (defaultProfile == null) {
    		defaultProfile = getProfiles().get(0);
    	}
	}


	@Transient
	public boolean hasProfile(SecProfile secPro) {
		return secPro != null && hasProfile(profiles, secPro);
	}
	
	@Transient
	public static boolean hasProfile(List<SecProfile> profiles, SecProfile secPro) {
		if (profiles == null || secPro == null) {
			return false;
		}
		for (SecProfile pro : profiles) {
			if (pro != null && pro.getId() != null 
					&& pro.equals(secPro)) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public static boolean hasControlProfilePrivilege(List<SecProfile> profiles, Long secConId, ESecPrivilege secPri) {
		if (profiles == null || secConId == null || secPri == null) {
			return false;
		}
		for (SecProfile pro : profiles) {
			for (SecControlProfilePrivilege conProfile : pro.getControlProfilePrivileges()) {
				if (conProfile.getControl().getId().equals(secConId) 
						&& conProfile.getPrivilege().equals(secPri)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public boolean isInAdminProfile() {
		return hasProfile(profiles, SecProfile.ADMIN);
	}
	
	@Transient 
	public boolean hasAccessToApp(String appCode) {
		if (profiles == null || appCode == null) {
			return false;
		}
		for (SecProfile profile : profiles) {
			if (profile != null && profile.getApplications() != null) {
				for (SecApplication app : profile.getApplications()) {
					if (app != null && app.getCode() != null 
							&& app.getCode().equals(appCode)) {
						return true;
					}
				}
			}
		}
		return false;	
	}
	
	@Transient 
	public boolean hasAccessToApp(Long appId) {
		if (profiles == null || appId == null) {
			return false;
		}
		for (SecProfile profile : profiles) {
			if (profile != null && profile.getApplications() != null) {
				for (SecApplication app : profile.getApplications()) {
					if (app != null && app.getId() != null 
							&& app.getId().equals(appId)) {
						return true;
					}
				}
			}
		}
		return false;	
	}
	
    /**
     * 
     * @param grant
     * @param appCode
     */
    public void grantAccessToApp(boolean grant, String appCode) {
		SecApplication appGrant = SeuksaServicesHelper.SECURITY_SRV.getApplication(appCode);

		if (getProfiles().size() == 0) {
			throw new IllegalStateException("This user [" + login + "] has not profile defined.");
		}
		if (appGrant == null) {
			throw new IllegalStateException("Empty appCode to grant.");
		}

		SecProfile foundProfile = null;
		for (SecProfile profile : profiles) {
			for (SecApplication app : profile.getApplications()) {
				if (app.getCode().equals(appCode)) {
					foundProfile = profile;
					break;
				}
			}
			if (foundProfile != null) break;
		}
		
		if (grant && foundProfile != null) {
			foundProfile.getApplications().add(appGrant);
		} else if (!grant && foundProfile !=null) {
			foundProfile.getApplications().remove(appGrant);
		}

	}
	
	/**_______________________________________________________________________________________
     * 
     * START BLOCK [SPRING SECURITY]
     * _______________________________________________________________________________________
     */
	
	/**
	 * @return the defaultProfile
	 */
    @ManyToOne
    @JoinColumn(name="default_sec_pro_id", nullable = true)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public SecProfile getDefaultProfile() {
    	if (defaultProfile == null && getProfiles().size() > 0) {
    		defaultProfile = getProfiles().get(0);
    	} else {
    		boolean found = false;
    		for (SecProfile pro : getProfiles()) {
    			if (pro.getId().equals(defaultProfile.getId())) {
    				found = true;
    				break;
    			}
    		}
    		if (!found) {
    			if (getProfiles().size() > 0) {
    				defaultProfile= getProfiles().get(0);
    			}
    		}
    	}
    	
		return defaultProfile;
	}

	/**
	 * @param defaultProfile the defaultProfile to set
	 */
	public void setDefaultProfile(SecProfile defaultProfile) {
		this.defaultProfile = defaultProfile;
		if (getProfiles().size() == 0) {
			addProfile(defaultProfile);
		}
	}
	
	@Transient
	public String getAllProfilesDesc() {
		allProfilesDesc = "";
		if (profiles != null && !profiles.isEmpty()) {
			for (SecProfile profile : profiles) {
				if (!allProfilesDesc.isEmpty()) {
					allProfilesDesc += ", ";
				}
				allProfilesDesc += profile.getDesc();
			}
		}
		return allProfilesDesc;
	}
	
	@Transient
	public String getAllProfilesDescEn() {
		allProfilesDescEn = "";
		if (profiles != null && !profiles.isEmpty()) {
			for (SecProfile profile : profiles) {
				if (!allProfilesDescEn.isEmpty()) {
					allProfilesDescEn += ", ";
				}
				allProfilesDescEn += profile.getDescEn();
			}
		}
		return allProfilesDescEn;
	}
	
	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 * A granted Authority is checled for example at the Login
	 * org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter()/invoke()/beforeInvocation()
	 *     org.springframework.security.access.vote..AffirmativeBased.decide()
	 *        org.springframework.security.access.vote.RoleVoter.vote() / rolePrefix = "ROLE_"
	 */
	@Override
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
		if (profiles != null) {
			for (SecProfile profile : profiles) {
				SecProfileGrantedAuthority auth = AuthorityFactory.getInstance().grantAuthority(profile);
				authorities.add(auth);
			}
		}
		//authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
        return authorities;
    }

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
    @Override
 	@Column(name = "sec_usr_pwd", nullable = false)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
   public String getPassword() {
        return password;
    }
    
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    @Transient
    public String getUsername() {
        return login;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return getExpirationDate() == null 
        		|| getExpirationDate().before(DateUtils.today());
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return !isLockedOut();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
    	boolean credentialsNonExpired = true;
        int expiredInDays = 0;
        try {
        	expiredInDays = SecurityHelper.getCredentialExpiredAfterNbDays();
        } catch (Exception e) {
        	// Nothing to do here
        }

        if (getLastPasswordChangeDate() != null && expiredInDays > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getLastPasswordChangeDate());
            calendar.add(Calendar.DAY_OF_YEAR, expiredInDays);
            credentialsNonExpired = calendar.getTime().after(new Date());
        }
        return credentialsNonExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    @Transient
    public boolean isEnabled() {
        return getStatusRecord() == null || EStatusRecord.ACTIV.equals(getStatusRecord());
    }

    /**_______________________________________________________________________________________
     * 
     * END BLOCK [SPRING SECURITY]
     * _______________________________________________________________________________________
     */

    @Override
    public String toString() {
    	return login;
    }
    
    @Transient
    public boolean isAdmin() {
    	return getDefaultProfile().isAdmin();
    }

    @Transient
    public boolean isSysAdmin() {
    	return getDefaultProfile().isSysAdmin();
    }
    
    @Transient
    public boolean isAnonymous() {
    	return SecurityHelper.DEFAULT_ANONYMOUS_ID.equals(getId());
    }
    
    /**
	 * @return the version
	 */
    @Version
    //@org.hibernate.annotations.SourceType.DB
    //@org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
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
}
