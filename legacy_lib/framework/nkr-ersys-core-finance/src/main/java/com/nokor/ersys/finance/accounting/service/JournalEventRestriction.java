package com.nokor.ersys.finance.accounting.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author prasnar
 *
 */
public class JournalEventRestriction extends BaseRestrictions<JournalEvent> {
	/** */
	private static final long serialVersionUID = 7635400282972232762L;

	private Long journalId;
	private EJournalEventGroup eventGroup;
	private String code;
	private String desc;

	/**
	 */
	public JournalEventRestriction() {
		super(JournalEvent.class);
	}

	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(JournalEvent.JOURNAL, JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
		if (journalId != null) {
			addCriterion(Restrictions.eq(JournalEvent.JOURNAL + DOT + Journal.ID, journalId));
		}
		if (eventGroup != null) {
			addCriterion(Restrictions.eq(JournalEvent.EVENTGROUP, eventGroup));
		}
    }


	/**
	 * @return the journalId
	 */
	public Long getJournalId() {
		return journalId;
	}


	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(Long journalId) {
		this.journalId = journalId;
	}


	/**
	 * @return the eventGroup
	 */
	public EJournalEventGroup getEventGroup() {
		return eventGroup;
	}


	/**
	 * @param eventGroup the eventGroup to set
	 */
	public void setEventGroup(EJournalEventGroup eventGroup) {
		this.eventGroup = eventGroup;
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
