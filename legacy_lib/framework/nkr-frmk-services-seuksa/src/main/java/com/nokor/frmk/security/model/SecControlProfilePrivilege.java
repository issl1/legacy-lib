package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * It is used to store the control's access rights for a given profile
 *  
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_sec_control_profile_privilege")
public class SecControlProfilePrivilege extends EntityA implements MSecControlProfilePrivilege {
	/** */
	private static final long serialVersionUID = 5135486814057630320L;

	private SecControl control;
	private SecProfile profile;
	private ESecPrivilege privilege;
	
	/**
     * 
     * @return
     */
    public static SecControlProfilePrivilege createInstance(SecProfile profile, SecControl control, ESecPrivilege privilege) {
    	SecControlProfilePrivilege ctlProPri = EntityFactory.createInstance(SecControlProfilePrivilege.class);
    	ctlProPri.setProfile(profile);
    	ctlProPri.setControl(control);
    	ctlProPri.setPrivilege(privilege);
        return ctlProPri;
    }
    
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_cpp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    
    /**
	 * @return the profile
	 */
    @ManyToOne
    @JoinColumn(name="sec_pro_id", nullable = false)
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
	}

	/**
	 * @return the control
	 */
    @ManyToOne
    @JoinColumn(name="sec_ctl_id", nullable = false)
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}

	/**
	 * @return the privilege
	 */
    @Column(name = "sec_pri_id", nullable = false)
    @Convert(converter = ESecPrivilege.class)
	public ESecPrivilege getPrivilege() {
		return privilege;
	}

	/**
	 * @param privilege the privilege to set
	 */
	public void setPrivilege(ESecPrivilege privilege) {
		this.privilege = privilege;
	}

	
}
