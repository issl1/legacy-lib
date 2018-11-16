package com.nokor.ersys.finance.accounting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author prasnar
 *
 */
public class JournalEntryRestriction extends BaseRestrictions<JournalEntry> {
	/** */
	private static final long serialVersionUID = 7523043853502262131L;

	private Long eventId;
	
	private EWkfStatus wkfStatus;
	private Date startDate;
	private Date endDate;
	private String searchText;
	private List<Journal> journals;

	/**
	 */
	public JournalEntryRestriction() {
		super(JournalEntry.class);
	}
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(JournalEntry.JOURNALEVENT, JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(searchText)) {
    		addCriterion(Restrictions.or(
    				Restrictions.ilike(JournalEntry.DESC, searchText, MatchMode.ANYWHERE),
    				Restrictions.ilike(JournalEntry.DESCEN, searchText, MatchMode.ANYWHERE),
    				Restrictions.ilike(JournalEntry.REFERENCE, searchText, MatchMode.ANYWHERE),
    				Restrictions.ilike(JournalEntry.INFO, searchText, MatchMode.ANYWHERE),
    				Restrictions.ilike(JournalEntry.OTHERINFO, searchText, MatchMode.ANYWHERE)
    				));
    	}
		if (eventId != null) {
			addCriterion(Restrictions.eq(JournalEntry.JOURNALEVENT + DOT + JournalEvent.ID, eventId));
		}
		if (wkfStatus != null) {
			addCriterion(Restrictions.eq(JournalEntry.WKFSTATUS, wkfStatus));
		}
		if (startDate != null) {
			addCriterion(Restrictions.ge(JournalEntry.WHEN, DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			addCriterion(Restrictions.le(JournalEntry.WHEN, DateUtils.getDateAtEndOfDay(endDate)));
		}
		if (journals != null && !journals.isEmpty()) {
			addCriterion(Restrictions.in(JournalEntry.JOURNALEVENT + DOT + JournalEvent.JOURNAL, journals));
		}
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

	/**
	 * @return the wkfStatus
	 */
	public EWkfStatus getWkfStatus() {
		return wkfStatus;
	}

	/**
	 * @param wkfStatus the wkfStatus to set
	 */
	public void setWkfStatus(EWkfStatus wkfStatus) {
		this.wkfStatus = wkfStatus;
	}

	/**
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

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the journals
	 */
	public List<Journal> getJournals() {
		return journals;
	}

	/**
	 * @param journals the journals to set
	 */
	public void setJournals(List<Journal> journals) {
		this.journals = journals;
	}
	
	public void addJournals(Journal journal) {
		if (this.journals == null) {
			journals = new ArrayList<Journal>();
		}
		journals.add(journal);
	}

}
