package com.nokor.efinance.core.marketing.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.marketing.model.Team;

/**
 * 
 * @author uhout.cheng
 */
public class TeamRestriction extends BaseRestrictions<Team> {
	
	/** */
	private static final long serialVersionUID = 7118276240244432049L;
	
	private String description;
	
	/**
	 * 
	 */
    public TeamRestriction() {
		super(Team.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(description)) {
			addCriterion(Restrictions.ilike(Team.DESCRIPTION, description, MatchMode.ANYWHERE));
		}
    	addOrder(Order.desc(Team.ID));
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
