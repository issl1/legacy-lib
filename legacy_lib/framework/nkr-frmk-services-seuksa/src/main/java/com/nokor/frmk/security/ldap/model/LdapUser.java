package com.nokor.frmk.security.ldap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.IVersion;
import org.springframework.security.core.GrantedAuthority;

import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.spring.SaltedUser;
import com.nokor.frmk.security.spring.SecSaltGenerator;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Entity
@Table(name = "tu_sec_ldap_user")
public class LdapUser extends EntityA implements SaltedUser, IVersion {
	/** */
	private static final long serialVersionUID = -7255492907513404391L;

	private Long version;

	private String login;
    private String desc;
    private String password;
    private String passwordSalt;
    private List<String> profiles;

    /**
	 * 
	 * @return
	 */
	public static LdapUser createInstance() {
		return SecurityEntityFactory.createLdapUserInstance();
	}
	
	
    /**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ldp_usr_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
	 * @return the login
	 */
    @Audited
    @Override
	@Column(name = "ldp_usr_login", unique = true, nullable = false)
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
	@Column(name = "ldp_usr_desc", nullable = false)
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
	 * @return the password
	 */
	@Column(name = "ldp_usr_pwd", nullable = false)
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
	 * @return the passwordSalt
	 */
	@Column(name = "ldp_usr_salt", nullable = true)
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


    @Override
    public String toString() {
    	return login;
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
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}


	@Transient
	@Override
	public String getUsername() {
		return login;
	}


	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}


	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}


	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}


	@Transient
	@Override
	public boolean isEnabled() {
		return isAccountNonLocked();
	}


	/**
	 * @return the profiles
	 */
	public List<String> getProfiles() {
		if (profiles == null) {
			profiles = new ArrayList<>();
		}
		return profiles;
	}


	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}
	
	/**
	 * 
	 */
	public void addProfile(String profile) {
		getProfiles().add(profile);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDefaultProfile() {
		return getProfiles().size() > 0 ? getProfiles().get(0) : null;
	}
	
}
