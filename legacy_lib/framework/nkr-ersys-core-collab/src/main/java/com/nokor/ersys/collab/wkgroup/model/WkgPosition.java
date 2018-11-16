package com.nokor.ersys.collab.wkgroup.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.eref.EJobPosition;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_wkg_position")
public class WkgPosition extends EntityA {
	/** */
	private static final long serialVersionUID = 8330737959459514633L;
	
	private EJobPosition position;
    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkg_pos_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }


    @Transient
	public boolean isPresident() {
		return false; //position.;
	}


	/**
	 * @return the position
	 */
    @Column(name = "job_pos_id", nullable = false)
    @Convert(converter = EWkgGroupType.class)
	public EJobPosition getPosition() {
		return position;
	}


	/**
	 * @param position the position to set
	 */
	public void setPosition(EJobPosition position) {
		this.position = position;
	}
    

}
