package com.nokor.ersys.core.finance.model.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.finance.model.Bank;

/**
 * @author prasnar
 * 
 */
public class BankRestriction extends BaseRestrictions<Bank> {
	/** */
	private static final long serialVersionUID = -7324959586188599237L;
	private String name;
	
	/**
	 * 
	 */
    public BankRestriction() {
		super(Bank.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	addOrder(Order.asc(Bank.NAME));
    	if (name != null) {
    		addCriterion(Restrictions.or(
    				Restrictions.ilike(Bank.NAME, name), 
    				Restrictions.ilike(Bank.NAMEEN, name),
    				Restrictions.ilike(Bank.CODE, name))
    				);
		}
		
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

	

	
}
