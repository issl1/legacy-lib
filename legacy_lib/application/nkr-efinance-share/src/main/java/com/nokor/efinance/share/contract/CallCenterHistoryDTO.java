package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class CallCenterHistoryDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -2738483835075807006L;
	
	private Long id;
	private String contractNo;
	private CallCenterResultDTO result;
	private String comment;

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
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/**
	 * @return the result
	 */
	public CallCenterResultDTO getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(CallCenterResultDTO result) {
		this.result = result;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CallCenterHistoryDTO)) {
			 return false;
		 }
		 CallCenterHistoryDTO callCenterHistoryDTO = (CallCenterHistoryDTO) arg0;
		 return getId() != null && getId().equals(callCenterHistoryDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
