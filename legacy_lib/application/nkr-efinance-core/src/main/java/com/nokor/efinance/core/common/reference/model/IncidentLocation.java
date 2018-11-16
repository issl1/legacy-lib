package com.nokor.efinance.core.common.reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.ersys.core.hr.model.address.Province;

/**
 * incident location lost moterbike
 * @author poevminea.sann
 *
 */
@Entity
@Table(name = "tu_incident_location")
public class IncidentLocation extends EntityRefA {
	
	/** 
	 */
	private static final long serialVersionUID = 1558088587737448341L;
	
	private Province province;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inc_loc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Column(name = "inc_loc_code", nullable = true, length = 10)
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "inc_loc_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * @return <String>
     */
    @Override
    @Column(name = "inc_loc_desc_en", nullable = true, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
    
    /**
	 * @return the province
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
	public Province getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Province province) {
		this.province = province;
	}
}
