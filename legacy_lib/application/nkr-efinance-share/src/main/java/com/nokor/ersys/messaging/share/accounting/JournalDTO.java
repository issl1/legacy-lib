package com.nokor.ersys.messaging.share.accounting;

import java.util.List;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class JournalDTO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = 8434220667435455376L;

	private List<JournalEventDTO> events;
	
    /**
     * 
     */
    public JournalDTO() {
    	
    }

	/**
	 * @return the events
	 */
	public List<JournalEventDTO> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<JournalEventDTO> events) {
		this.events = events;
	}
	
}
