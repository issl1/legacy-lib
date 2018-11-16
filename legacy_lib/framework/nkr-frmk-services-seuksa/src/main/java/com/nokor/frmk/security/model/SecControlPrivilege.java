package com.nokor.frmk.security.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * Used only for the display of control's access rights
 * 
 * It is not used to store the access rights for a profile (for that case see SecControlProfilePrivilege)
 * 
 * @author phirun.kong
 *
 */
@Entity
@Table(name = "tu_sec_control_privilege")
public class SecControlPrivilege extends EntityA {

	/**	 */
	private static final long serialVersionUID = 4090662008197958435L;
	
	private SecControl control;
	private ESecPrivilege privilege;
	
	/**
     * 
     * @return
     */
    public static SecControlPrivilege createInstance(SecControl control, ESecPrivilege privilege) {
    	SecControlPrivilege ctlPri = EntityFactory.createInstance(SecControlPrivilege.class);
    	ctlPri.setControl(control);
    	ctlPri.setPrivilege(privilege);
        return ctlPri;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_cop_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
	/**
	 * @return the control
	 */
    @ManyToOne(fetch=FetchType.LAZY)
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
