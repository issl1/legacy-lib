package com.nokor.efinance.third.creditbureau.cbc.model;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Header implements Serializable {

	private static final long serialVersionUID = -2802278382089285004L;
	
	private String memberId;
	private String userId;
	private String runNo;
	private Integer totItems = 1;
	private String errItems;
	
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the runNo
	 */
	public String getRunNo() {
		return runNo;
	}
	/**
	 * @param runNo the runNo to set
	 */
	public void setRunNo(String runNo) {
		this.runNo = runNo;
	}
	/**
	 * @return the totItems
	 */
	public Integer getTotItems() {
		return totItems;
	}
	/**
	 * @param totItems the totItems to set
	 */
	public void setTotItems(Integer totItems) {
		this.totItems = totItems;
	}
	/**
	 * @return the errItems
	 */
	public String getErrItems() {
		return errItems;
	}
	/**
	 * @param errItems the errItems to set
	 */
	public void setErrItems(String errItems) {
		this.errItems = errItems;
	}
}
