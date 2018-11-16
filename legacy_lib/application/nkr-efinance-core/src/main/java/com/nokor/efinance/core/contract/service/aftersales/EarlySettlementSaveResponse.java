package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;

import com.nokor.efinance.core.contract.model.LockSplit;

public class EarlySettlementSaveResponse implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -3296597281216467209L;

	private LockSplit lockSplit;

	/**
	 * @return the lockSplit
	 */
	public LockSplit getLockSplit() {
		return lockSplit;
	}

	/**
	 * @param lockSplit the lockSplit to set
	 */
	public void setLockSplit(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
	}
	
}
