package com.nokor.ersys.core.hr.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;

/**
 * 
 * @author phirun.kong
 *
 */
public class CommuneRestriction extends BaseRestrictions<Commune> {
    
	/**	 */
	private static final long serialVersionUID = -7909666300232862767L;
	
	private String code;
	private String desc;
	private District district;
	private Province province;
	
	/**
	 * 
	 */
    public CommuneRestriction() {
		super(Commune.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	if (StringUtils.isNotEmpty(code)) { 
       		addCriterion(Restrictions.ilike(Commune.CODE, code, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(desc)) { 
			Criterion descEn = Restrictions.ilike(Commune.DESCEN, desc,MatchMode.ANYWHERE);
			Criterion descKh = Restrictions.ilike(Commune.DESC, desc, MatchMode.ANYWHERE);
			addCriterion(Restrictions.or(descEn, descKh));
		}
		if (district != null) { 
			addCriterion(Restrictions.eq(Commune.DISTRICT, district));
		}
		if (province != null) {
			addAssociation(Commune.DISTRICT, "dis", JoinType.INNER_JOIN);
			addCriterion(Restrictions.eq("dis" + DOT + District.PROVINCE, province));
		}
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the province
	 */
	public Province getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Province province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(District district) {
		this.district = district;
	}
	
}
