package com.nokor.ersys.core.hr.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.address.Province;

/**
 * 
 * @author phirun.kong
 *
 */
public class ProvinceRestriction extends BaseRestrictions<Province> {
    
	/**	 */
	private static final long serialVersionUID = -1640194129193805494L;
	
	private String code;
	private String desc;
	
	/**
	 * 
	 */
    public ProvinceRestriction() {
		super(Province.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	if (StringUtils.isNotEmpty(code)) { 
       		addCriterion(Restrictions.ilike(Province.CODE, code, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(desc)) { 
			Criterion descEn = Restrictions.ilike(Province.DESCEN, desc,MatchMode.ANYWHERE);
			Criterion descKh = Restrictions.ilike(Province.DESC, desc, MatchMode.ANYWHERE);
			addCriterion(Restrictions.or(descEn, descKh));
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
	
}
