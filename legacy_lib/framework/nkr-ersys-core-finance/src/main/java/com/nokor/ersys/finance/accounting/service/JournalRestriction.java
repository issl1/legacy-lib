package com.nokor.ersys.finance.accounting.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author prasnar
 *
 */
public class JournalRestriction extends BaseRestrictions<Journal> {
	/** */
	private static final long serialVersionUID = -3343843456130854989L;

	private String code;
	private Long eventId;

	/**
	 */
	public JournalRestriction() {
		super(Journal.class);
	}
	
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(Journal.EVENTS, JoinType.INNER_JOIN);
    }
    
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (eventId != null) {
			addCriterion(Restrictions.eq(Journal.EVENTS + DOT + JournalEvent.ID, eventId));
		}
		if (code != null) {
			addCriterion(Restrictions.eq(Journal.CODE, code));
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
	 * @return the eventId
	 */
	public Long getEventId() {
		return eventId;
	}


	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
