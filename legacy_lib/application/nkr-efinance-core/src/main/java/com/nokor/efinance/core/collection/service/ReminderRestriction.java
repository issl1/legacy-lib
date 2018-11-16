package com.nokor.efinance.core.collection.service;

import java.util.Date;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class ReminderRestriction extends BaseRestrictions<Reminder> {
	
	/** */
	private static final long serialVersionUID = -2608012993641891802L;
	
	private Long conId;
	private Date date;
	
	/**
	 * 
	 */
    public ReminderRestriction() {
		super(Reminder.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(Reminder.CONTRACT + DOT + Contract.ID, conId));
    	}
    	if (date != null) {
    		addCriterion(Restrictions.ge(Reminder.DATE, DateUtils.getDateAtBeginningOfDay(date)));
    		addCriterion(Restrictions.le(Reminder.DATE, DateUtils.getDateAtEndOfDay(date)));
    	}
    	addCriterion(Restrictions.eq(Reminder.DISMISS, Boolean.FALSE));
    	addOrder(Order.desc(Reminder.ID));
    }

	/**
	 * @return the conId
	 */
	public Long getConId() {
		return conId;
	}

	/**
	 * @param conId the conId to set
	 */
	public void setConId(Long conId) {
		this.conId = conId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
