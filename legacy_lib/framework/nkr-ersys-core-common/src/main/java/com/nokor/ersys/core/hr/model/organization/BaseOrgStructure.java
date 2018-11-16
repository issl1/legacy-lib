package com.nokor.ersys.core.hr.model.organization;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MainEntity;

import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.eref.EOrgStandardLevel;

/**
 * See EOrgLevel
 * Example for Private Company
 *    Holding, Subsidiary, Head Office/Branch, Department, Division, Office,..
 * Example for Ministry
 * 	   Ministry, Secretariat, General Department, Department, Cabinet, Institute, National Hospital, HC (standard), Referral Hospital...
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseOrgStructure extends MainEntity implements MBaseOrgStructure {
	/** */
	private static final long serialVersionUID = -6988777715566136251L;

	private EOrgLevel level;
	private EOrgStandardLevel standardLevel;
	
	private String code;
	private String name;
	private String nameEn;
	private String tel;
	private String mobile;
	private String email;
	private String fax;
	
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_str_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @return the code
	 */
	@Column(name = "org_str_code", nullable = true, length = 100)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


    
	@Column(name = "org_str_name", nullable = false, length = 100)
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
	@Column(name = "org_str_name_en", nullable = true, length = 100)
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "org_str_email", nullable = true, length = 100)
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

    @Column(name = "org_str_tel", nullable = true, length = 30)
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
    @Column(name = "org_str_mobile", nullable = true, length = 30)
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
	@Column(name = "org_str_fax", nullable = true, length = 30)
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
	 * @return the level
	 */
    @Column(name = "org_lev_id", nullable = false)
    @Convert(converter = EOrgLevel.class)
	public EOrgLevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(EOrgLevel level) {
		this.level = level;
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

	/**
	 * @return the standardLevel
	 */
	@Column(name = "org_sta_lev_id", nullable = true)
    @Convert(converter = EOrgStandardLevel.class)
	public EOrgStandardLevel getStandardLevel() {
		return standardLevel;
	}

	/**
	 * @param standardLevel the standardLevel to set
	 */
	public void setStandardLevel(EOrgStandardLevel standardLevel) {
		this.standardLevel = standardLevel;
	}


}
