package com.nokor.efinance.share.organization;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceLostClaimDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -7710295681176891575L;

	private Integer rangeOfYear;
	private Integer from;
	private Integer to;
	private Double premiumnRefundedPercentage;
	
	/**
	 * @return the rangeOfYear
	 */
	public Integer getRangeOfYear() {
		return rangeOfYear;
	}
	
	/**
	 * @param rangeOfYear the rangeOfYear to set
	 */
	public void setRangeOfYear(Integer rangeOfYear) {
		this.rangeOfYear = rangeOfYear;
	}
	
	/**
	 * @return the from
	 */
	public Integer getFrom() {
		return from;
	}
	
	/**
	 * @param from the from to set
	 */
	public void setFrom(Integer from) {
		this.from = from;
	}
	
	/**
	 * @return the to
	 */
	public Integer getTo() {
		return to;
	}
	
	/**
	 * @param to the to to set
	 */
	public void setTo(Integer to) {
		this.to = to;
	}
	
	/**
	 * @return the premiumnRefundedPercentage
	 */
	public Double getPremiumnRefundedPercentage() {
		return premiumnRefundedPercentage;
	}
	
	/**
	 * @param premiumnRefundedPercentage the premiumnRefundedPercentage to set
	 */
	public void setPremiumnRefundedPercentage(Double premiumnRefundedPercentage) {
		this.premiumnRefundedPercentage = premiumnRefundedPercentage;
	}
	
}
