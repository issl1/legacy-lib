package com.nokor.efinance.core.contract.service.penalty;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.common.reference.model.MinReturnRate;

/**
 * 
 * @author uhout.cheng
 */
public class MinReturnRateRestriction extends BaseRestrictions<MinReturnRate> {
	
	/**
	 */
	private static final long serialVersionUID = 470947295151460598L;

	private Date startDate;
	
	/** 
	 */
    public MinReturnRateRestriction() {
		super(MinReturnRate.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (startDate != null) {
    		addCriterion(Restrictions.gt("startDate", startDate));
    	}
	}


	/**s
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
