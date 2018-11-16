package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;

public class LossSimulateRequest implements Serializable {

	private static final long serialVersionUID = -5866246138376182931L;
	
	private Long cotraId;
	private Date eventDate;
	
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
}
