package com.nokor.ersys.messaging.share.accounting;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * @author prasnar
 *
 */
public class JournalEventAccountDTO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = -4269768969194383472L;

//	private JournalEventDTO eventDTO;
	private Long eventId;
	private Long accountId;
	private Boolean isDebit;

	/**
     * 
     */
    public JournalEventAccountDTO() {
    	
    }
	
 	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the isDebit
	 */
	public Boolean getIsDebit() {
		return isDebit;
	}

	/**
	 * @param isDebit the isDebit to set
	 */
	public void setIsDebit(Boolean isDebit) {
		this.isDebit = isDebit;
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
