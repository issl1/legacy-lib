package com.nokor.ersys.core.hr.model.organization;

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
import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.ersys.core.hr.model.address.Province;

/**
 * OrgGroup is used in OrgStructure that managed by SecUserGroup
 * For MOH : OrgGroup's data is copy from Province and insert new value 'MOH'
 * @author phirun.kong
 *
 */
@Entity
@Table(name = "tu_org_group")
public class OrgGroup extends EntityRefA {
	
	private Province province;

	/**
	 * 
	 * @return
	 */
	public static OrgGroup createInstance() {
		OrgGroup secUserRole = EntityFactory.createInstance(OrgGroup.class);
        return secUserRole;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "org_grp_code", nullable = true, length = 10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "org_grp_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "org_grp_desc_en", nullable = false, length = 100)
	@Override
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @return the province
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id", nullable = true)
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
