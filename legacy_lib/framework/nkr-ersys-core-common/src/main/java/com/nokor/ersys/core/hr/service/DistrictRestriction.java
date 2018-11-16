package com.nokor.ersys.core.hr.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;

/**
 * 
 * @author phirun.kong
 *
 */
public class DistrictRestriction extends BaseRestrictions<District> {
    
	/**	 */
	private static final long serialVersionUID = 4727771438089640879L;
	
	private String code;
	private String desc;
	private Province province;
	
	/**
	 * 
	 */
    public DistrictRestriction() {
		super(District.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	if (StringUtils.isNotEmpty(code)) { 
       		addCriterion(Restrictions.ilike(District.CODE, code, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(desc)) { 
			Criterion descEn = Restrictions.ilike(District.DESCEN, desc,MatchMode.ANYWHERE);
			Criterion descKh = Restrictions.ilike(District.DESC, desc, MatchMode.ANYWHERE);
			addCriterion(Restrictions.or(descEn, descKh));
		}
		if (province != null) { 
			addCriterion(Restrictions.eq(District.PROVINCE, province));
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
	
}
