package com.nokor.efinance.share.locksplit;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.Gson;


/**
 * 
 * @author uhout.cheng
 */
public class LockSplitCriteriaDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 6953091622326531745L;
	
	private String lockSplitNo;
	private String contractID;
	private Date dueDateFrom;
	private Date dueDateTo;
	private Long paymentChannelId;

	/**
	 * @return the lockSplitNo
	 */
	public String getLockSplitNo() {
		return lockSplitNo;
	}

	/**
	 * @param lockSplitNo the lockSplitNo to set
	 */
	public void setLockSplitNo(String lockSplitNo) {
		this.lockSplitNo = lockSplitNo;
	}

	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}

	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the dueDateFrom
	 */
	public Date getDueDateFrom() {
		return dueDateFrom;
	}

	/**
	 * @param dueDateFrom the dueDateFrom to set
	 */
	public void setDueDateFrom(Date dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	/**
	 * @return the dueDateTo
	 */
	public Date getDueDateTo() {
		return dueDateTo;
	}

	/**
	 * @param dueDateTo the dueDateTo to set
	 */
	public void setDueDateTo(Date dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	/**
	 * @return the paymentChannelId
	 */
	public Long getPaymentChannelId() {
		return paymentChannelId;
	}

	/**
	 * @param paymentChannelId the paymentChannelId to set
	 */
	public void setPaymentChannelId(Long paymentChannelId) {
		this.paymentChannelId = paymentChannelId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
