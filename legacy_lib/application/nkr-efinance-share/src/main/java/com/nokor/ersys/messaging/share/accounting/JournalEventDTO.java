package com.nokor.ersys.messaging.share.accounting;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class JournalEventDTO extends BaseEntityRefDTO {
    /** */
	private static final long serialVersionUID = -5383643604641523876L;
	
	private Long journalId;
	private Long eventGroupId;
	
    /**
     * 
     */
    public JournalEventDTO() {
    	
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
	 * @return the eventGroupId
	 */
	public Long getEventGroupId() {
		return eventGroupId;
	}

	/**
	 * @param eventGroupId the eventGroupId to set
	 */
	public void setEventGroupId(Long eventGroupId) {
		this.eventGroupId = eventGroupId;
	}

	
}
