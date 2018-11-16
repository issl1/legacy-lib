package com.nokor.ersys.finance.accounting.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;

/**
 * @author bunlong.taing
 */
public class AccountRestriction extends BaseRestrictions<Account> {
	/** */
	private static final long serialVersionUID = 4054120725357814119L;
	
	private AccountCategory category;
	private String code;
	private String name;
	private String desc;

	/**
	 */
	public AccountRestriction() {
		super(Account.class);
	}
	
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(Account.CATEGORY, JoinType.INNER_JOIN);
    }
    
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (category != null) {
			addCriterion(Restrictions.eq(Account.CATEGORY, category));
		}
		if (StringUtils.isNotEmpty(code)) {
	    	addCriterion(Restrictions.ilike(Account.CODE, code, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(name)) {
			addCriterion(Restrictions.or(Restrictions.ilike(Account.NAME, name, MatchMode.ANYWHERE),
			Restrictions.ilike(Account.NAMEEN, name, MatchMode.ANYWHERE)));
		}
		if (StringUtils.isNotEmpty(desc)) {
			addCriterion(Restrictions.or(Restrictions.ilike(Account.DESC, desc, MatchMode.ANYWHERE),
			Restrictions.ilike(Account.DESCEN, desc, MatchMode.ANYWHERE)));
		} 
	}

	/**
	 * @return the category
	 */
	public AccountCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AccountCategory category) {
		this.category = category;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
