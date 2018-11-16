package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class ContractRequestDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 2103668947164894877L;
	
	private Long id;
	private Date creationDate;
	private String contractNo;
	private RefDataDTO requestType;
	private String otherValue;
	private String remark;
	private String userLogin;
	private boolean processed;

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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	 * @return the requestType
	 */
	public RefDataDTO getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(RefDataDTO requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the otherValue
	 */
	public String getOtherValue() {
		return otherValue;
	}

	/**
	 * @param otherValue the otherValue to set
	 */
	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the userLogin
	 */
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	/**
	 * @return the processed
	 */
	public boolean isProcessed() {
		return processed;
	}

	/**
	 * @param processed the processed to set
	 */
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ContractRequestDTO)) {
			 return false;
		 }
		 ContractRequestDTO contractRequestDTO = (ContractRequestDTO) arg0;
		 return getId() != null && getId().equals(contractRequestDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
