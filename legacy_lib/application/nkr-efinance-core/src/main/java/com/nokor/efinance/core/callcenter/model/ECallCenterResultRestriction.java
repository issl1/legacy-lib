package com.nokor.efinance.core.callcenter.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ECallCenterResultRestriction extends BaseRestrictions<ECallCenterResult>{

	/** */
	private static final long serialVersionUID = -2443148803204281273L;

	private String code;
	private String desc;
	
	public ECallCenterResultRestriction() {
		super(ECallCenterResult.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (StringUtils.isNotEmpty(code)) {
			addCriterion(Restrictions.eq(ECallCenterResult.CODE, code));
		}
		if (StringUtils.isNotEmpty(desc)) {
			addCriterion(Restrictions.ilike(ECallCenterResult.DESC, desc));
		}
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
