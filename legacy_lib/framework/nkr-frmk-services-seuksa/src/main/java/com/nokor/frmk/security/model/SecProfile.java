package com.nokor.frmk.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_sec_profile")
public class SecProfile extends EntityRefA implements MSecControl {
	/** */
	private static final long serialVersionUID = -2136920128162817855L;

	public static final SecProfile SYS_ADMIN = new SecProfile("SYS_ADMIN", 999999L);
	public static final SecProfile ADMIN = new SecProfile("ADMIN", 1L);

	private SecProfile parent;
    private List<SecProfile> children;

	private List<SecUser> users; 
	private List<SecControlProfilePrivilege> controlProfilePrivileges;
	private List<SecApplication> applications; 

	private List<SecManagedProfile> managedProfiles;

	/**
     * 
     * @return
     */
    public static SecProfile createInstance() {
    	SecProfile pro = EntityFactory.createInstance(SecProfile.class);
        return pro;
    }

    /**
     * 
     */
    public SecProfile() {
    	// do not remove
    }
    
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public SecProfile(String code, long id) {
		this.code  = code;
		this.id = id;
	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_pro_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sec_pro_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sec_pro_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "sec_pro_desc_en", nullable = true)
	@Override
    public String getDescEn() {
        return descEn;
    }


	/**
	 * 
	 * @return
	 */
	@ManyToMany(mappedBy = "profiles")
	public List<SecUser> getUsers() {
		if (users == null) {
			users = new ArrayList<SecUser>();
		}
		return users;
	}

	/**
	 * 
	 * @param users
	 */
	public void setUsers(List<SecUser> users) {
		this.users = users;
	}
	
	/**
	 * @return the applications
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_sec_application_profile",
				joinColumns = { @JoinColumn(name = "sec_pro_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_app_id") })
	public List<SecApplication> getApplications() {
		if (applications == null) {
			applications = new ArrayList<SecApplication>();
		}
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(List<SecApplication> applications) {
		this.applications = applications;
	}

	/**
	 * @return the controlProfilePrivileges
	 */
	@OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
	public List<SecControlProfilePrivilege> getControlProfilePrivileges() {
		if (controlProfilePrivileges == null) {
			controlProfilePrivileges = new ArrayList<SecControlProfilePrivilege>();
		}
		return controlProfilePrivileges;
	}

	/**
	 * @param controlProfilePrivileges the controlProfilePrivileges to set
	 */
	public void setControlProfilePrivileges(List<SecControlProfilePrivilege> controlProfilePrivileges) {
		this.controlProfilePrivileges = controlProfilePrivileges;
	}	

	/**
	 * 
	 * @return
	 */
	@Transient
	public boolean isAdmin() {
		return SecProfile.ADMIN.equals(this);
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public boolean isSysAdmin() {
		return SecProfile.SYS_ADMIN.equals(this);
	}
	
	/**
	 * 
	 */
	@Transient
	public void addControlAccess(SecControl control) {
		for (ESecPrivilege pri : ESecPrivilege.values()) {
			controlProfilePrivileges.add(SecControlProfilePrivilege.createInstance(this, control, pri));
		}
	}
	
	/**
	 * 
	 */
	@Transient
	public void initControlAccess() {
		controlProfilePrivileges = new ArrayList<SecControlProfilePrivilege>();
	}

	/**
	 * @return the managedProfiles
	 */
	@ManyToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<SecManagedProfile> getManagedProfiles() {
		return managedProfiles;
	}

	/**
	 * @param managedProfiles the managedProfiles to set
	 */
	public void setManagedProfiles(List<SecManagedProfile> managedProfiles) {
		this.managedProfiles = managedProfiles;
	}


	/**
	 * @return the parent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
	public SecProfile getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(SecProfile parent) {
		this.parent = parent;
	}


	/**
	 * @return the children
	 */
    @OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
    @OrderBy(value = "sort_index, id")
	public List<SecProfile> getChildren() {
    	if (children == null) {
    		children = new ArrayList<>();
    	}
		return children;
	}


	/**
	 * @param children the children to set
	 */
	public void setChildren(List<SecProfile> children) {
		this.children = children;
	}

	
	
}
