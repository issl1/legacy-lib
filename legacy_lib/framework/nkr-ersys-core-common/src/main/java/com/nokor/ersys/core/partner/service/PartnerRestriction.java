package com.nokor.ersys.core.partner.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.partner.model.Partner;

/**
 * @author prasnar
 * 
 */
public class PartnerRestriction extends BaseRestrictions<Partner> {
	/** */
	private static final long serialVersionUID = 56405335252146506L;

	private String code;
	private String name;
	private boolean customer;
	private boolean supplier;
	
	/**
	 * 
	 */
    public PartnerRestriction() {
		super(Partner.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (customer) {
    		addCriterion(Restrictions.eq("isCustomer", Boolean.TRUE));
    	}
    	if (supplier) {
    		addCriterion(Restrictions.eq("isSupplier", Boolean.TRUE));
    	}
    	if (StringUtils.isNotEmpty(code)) { 
			addCriterion(Restrictions.ilike(Partner.CODE, getCode(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(name)) {
			Criterion nameEn = Restrictions.ilike(Partner.NAMEEN, name, MatchMode.ANYWHERE);
			Criterion nameKh = Restrictions.ilike(Partner.NAME, name, MatchMode.ANYWHERE);
			addCriterion(Restrictions.or(nameEn, nameKh));
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
	 * @return the customer
	 */
	public boolean isCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(boolean customer) {
		this.customer = customer;
	}

	/**
	 * @return the supplier
	 */
	public boolean isSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(boolean supplier) {
		this.supplier = supplier;
	}

}
