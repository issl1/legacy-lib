package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;

public class EarlySettlementSimulateRequest implements Serializable {

	private static final long serialVersionUID = -5866246138376182931L;
	
	private Long aftEvtId;
	private Long cotraId;
	private Date eventDate;
	private boolean includePenalty;
		
	
	/**
	 * @return the aftEvtId
	 */
	public Long getAftEvtId() {
		return aftEvtId;
	}

	/**
	 * @param aftEvtId the aftEvtId to set
	 */
	public void setAftEvtId(Long aftEvtId) {
		this.aftEvtId = aftEvtId;
	}

	/**
	 * @return the cotraId
	 */
	public Long getCotraId() {
		return cotraId;
	}
	
	/**
	 * @param cotraId the cotraId to set
	 */
	public void setCotraId(Long cotraId) {
		this.cotraId = cotraId;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the includePenalty
	 */
	public boolean isIncludePenalty() {
		return includePenalty;
	}

	/**
	 * @param includePenalty the includePenalty to set
	 */
	public void setIncludePenalty(boolean includePenalty) {
		this.includePenalty = includePenalty;
	}
}
