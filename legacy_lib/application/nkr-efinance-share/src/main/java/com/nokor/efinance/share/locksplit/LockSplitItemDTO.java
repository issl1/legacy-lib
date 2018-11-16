package com.nokor.efinance.share.locksplit;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;


/**
 * 
 * @author uhout.cheng
 */
public class LockSplitItemDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 7015312126016184773L;

	private Long id;
	private String createdUser;
	private Date createdDate;
	private String receiptCode;
	private Double amount;
	private Double vatAmount;
	private Integer priority;
	private LockSplitStatus status;
	private RefDataDTO category;
	private UriDTO operation;
	
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
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the receiptCode
	 */
	public String getReceiptCode() {
		return receiptCode;
	}

	/**
	 * @param receiptCode the receiptCode to set
	 */
	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}	

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @return the vatAmount
	 */
	public Double getVatAmount() {
		return vatAmount;
	}

	/**
	 * @param vatAmount the vatAmount to set
	 */
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}	

	/**
	 * @return the status
	 */
	public LockSplitStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(LockSplitStatus status) {
		this.status = status;
	}

	/**
	 * @return the category
	 */
	public RefDataDTO getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(RefDataDTO category) {
		this.category = category;
	}

	/**
	 * @return the operation
	 */
	public UriDTO getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(UriDTO operation) {
		this.operation = operation;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LockSplitItemDTO)) {
			 return false;
		 }
		 LockSplitItemDTO itemDTO = (LockSplitItemDTO) arg0;
		 return getId() != null && getId().equals(itemDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
