package com.nokor.frmk.security.model;

import javax.persistence.Column;
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
 * 
 * @author phirun.kong
 *
 */
@Entity
@Table(name = "tu_sec_managed_profile")
public class SecManagedProfile extends EntityA {

	/**	 */
	private static final long serialVersionUID = 4231652453909016731L;
	
	private SecProfile parent;
	private SecProfile child;
	
	/**
     * 
     * @return
     */
    public static SecManagedProfile createInstance() {
    	SecManagedProfile mgdPro = EntityFactory.createInstance(SecManagedProfile.class);
        return mgdPro;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_mgd_pro_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_sec_pro_id", nullable = false)
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
	 * @return the child
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="child_sec_pro_id", nullable = false)
	public SecProfile getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(SecProfile child) {
		this.child = child;
	}
    
}
