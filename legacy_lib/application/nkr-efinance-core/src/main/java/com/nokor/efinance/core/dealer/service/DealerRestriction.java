package com.nokor.efinance.core.dealer.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;

/**
 * 
 * @author prasnar
 *
 */
public class DealerRestriction extends BaseRestrictions<Dealer> implements DealerEntityField {
	/**
	 */
	private static final long serialVersionUID = 8730175150518367788L;
	
	private Dealer parent;
	private String internalCode;
	private String name;
		
	/**
	 */
    public DealerRestriction() {
		super(Dealer.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (parent != null) {
    		addCriterion(Restrictions.eq("parent.id", parent.getId()));
    	}    		
    	if (StringUtils.isNotEmpty(internalCode)) {
    		addCriterion(Restrictions.ilike(INTERNAL_CODE, internalCode, MatchMode.ANYWHERE));
    	}
    	if (StringUtils.isNotEmpty(name)) {
			addCriterion(Restrictions.or(Restrictions.ilike(Dealer.NAME, name, MatchMode.ANYWHERE),
					Restrictions.ilike(Dealer.NAMEEN, name, MatchMode.ANYWHERE)));
		}
	}

	/**
	 * @return the parent
	 */
	public Dealer getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Dealer parent) {
		this.parent = parent;
	}

	/**
	 * @return the internalCode
	 */
	public String getInternalCode() {
		return internalCode;
	}

	/**
	 * @param internalCode the internalCode to set
	 */
	public void setInternalCode(String internalCode) {
		this.internalCode = internalCode;
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
