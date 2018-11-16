package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "ts_sec_application")
public class SecApplication extends EntityRefA {
	/** */
	private static final long serialVersionUID = 7861116000048153225L;

    public final static String DEFAULT_APP_CODE_BO_SUFFIX = "_BO";
    public final static String DEFAULT_APP_CODE_FO_SUFFIX = "_FO";
    public final static String DEFAULT_APP_CODE_RA_SUFFIX = "_RO";

	public static final String DEFAULT_APP = "MAIN";
    public final static String DEFAULT_APP_CODE_BO = "APP" + DEFAULT_APP_CODE_BO_SUFFIX;
    public final static String DEFAULT_APP_CODE_FO = "APP" + DEFAULT_APP_CODE_FO_SUFFIX;
    public final static String DEFAULT_APP_CODE_RA = "APP" + DEFAULT_APP_CODE_RA_SUFFIX;


	private List<SecProfile> profiles;
	private List<SecPage> pages;
	private List<SecControl> controls;

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_app_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_app_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_app_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_app_desc_en", nullable = false)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * 
	 * @return
	 */
	@ManyToMany(mappedBy = "applications")
	public List<SecProfile> getProfiles() {
		if (profiles == null) {
			profiles = new ArrayList<SecProfile>();
		}
		return profiles;
	}

	/**
	 * 
	 * @param profiles
	 */
	public void setProfiles(List<SecProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * 
	 * @return
	 */
	@OneToMany(mappedBy = "application")
	public List<SecPage> getPages() {
		if (pages == null) {
			pages = new ArrayList<>();
		}
		return pages;
	}

	/**
	 * 
	 * @param pages
	 */
	public void setPages(List<SecPage> pages) {
		this.pages = pages;
	}	
	
	/**
	 * @return the controls
	 */
	@OneToMany(mappedBy = "application")
	public List<SecControl> getControls() {
		if (controls == null) {
			controls = new ArrayList<>();
		}
		return controls;
	}

	/**
	 * @param controls the controls to set
	 */
	public void setControls(List<SecControl> controls) {
		this.controls = controls;
	}

	@Transient
	public boolean isFrontOffice() {
		return code.endsWith(DEFAULT_APP_CODE_FO_SUFFIX);
	}
	@Transient
	public boolean isBackOffice() {
		return code.endsWith(DEFAULT_APP_CODE_BO_SUFFIX);
	}
	@Transient
	public boolean isRepoAdmin() {
		return code.endsWith(DEFAULT_APP_CODE_RA_SUFFIX);
	}
}
