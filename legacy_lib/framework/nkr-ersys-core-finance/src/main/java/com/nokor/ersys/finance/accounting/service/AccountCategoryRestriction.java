package com.nokor.ersys.finance.accounting.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;

/**
 * @author bunlong.taing
 */
public class AccountCategoryRestriction extends BaseRestrictions<AccountCategory> {
	/** */
	private static final long serialVersionUID = 5408050394911860536L;
	
	private AccountCategory parent;
	private ECategoryRoot root;

	/**
	 */
	public AccountCategoryRestriction() {
		super(AccountCategory.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (parent != null) {
			addCriterion(Restrictions.eq(AccountCategory.PARENT, parent));
		}
		
		if (root != null) {
			addCriterion(Restrictions.eq(AccountCategory.ROOT, root));
		}
	}

	/**
	 * @return the parent
	 */
	public AccountCategory getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AccountCategory parent) {
		this.parent = parent;
	}

	/**
	 * @return the root
	 */
	public ECategoryRoot getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(ECategoryRoot root) {
		this.root = root;
	}

}
