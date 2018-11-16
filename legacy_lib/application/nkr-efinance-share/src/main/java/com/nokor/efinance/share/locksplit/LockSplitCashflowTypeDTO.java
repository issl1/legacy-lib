package com.nokor.efinance.share.locksplit;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitCashflowTypeDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -7380307782496236355L;
	
	private Long id;
	private UriDTO lockSplitType;
	private RefDataDTO cashflowType;
		
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the lockSplitType
	 */
	public UriDTO getLockSplitType() {
		return lockSplitType;
	}

	/**
	 * @param lockSplitType the lockSplitType to set
	 */
	public void setLockSplitType(UriDTO lockSplitType) {
		this.lockSplitType = lockSplitType;
	}

	/**
	 * @return the cashflowType
	 */
	public RefDataDTO getCashflowType() {
		return cashflowType;
	}

	/**
	 * @param cashflowType the cashflowType to set
	 */
	public void setCashflowType(RefDataDTO cashflowType) {
		this.cashflowType = cashflowType;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LockSplitCashflowTypeDTO)) {
			 return false;
		 }
		 LockSplitCashflowTypeDTO lockSplitCashflowTypeDTO = (LockSplitCashflowTypeDTO) arg0;
		 return getId() != null && getId().equals(lockSplitCashflowTypeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
