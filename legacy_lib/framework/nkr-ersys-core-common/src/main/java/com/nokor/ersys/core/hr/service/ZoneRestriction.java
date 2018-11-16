package com.nokor.ersys.core.hr.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Zone;

/**
 * 
 * @author phirun.kong
 *
 */
public class ZoneRestriction extends BaseRestrictions<Zone> {
    
	/**	 */
	private static final long serialVersionUID = 3163120610803034455L;
	
	private String code;
	private String desc;
	private Province province;
	
	/**
	 * 
	 */
    public ZoneRestriction() {
		super(Zone.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	if (StringUtils.isNotEmpty(code)) { 
       		addCriterion(Restrictions.ilike(Zone.CODE, code, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(desc)) { 
			Criterion descEn = Restrictions.ilike(Zone.DESCEN, desc,MatchMode.ANYWHERE);
			Criterion descKh = Restrictions.ilike(Zone.DESC, desc, MatchMode.ANYWHERE);
			addCriterion(Restrictions.or(descEn, descKh));
		}
		if (province != null) { 
			addCriterion(Restrictions.eq(Zone.PROVINCE, province));
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
