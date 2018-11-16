package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author uhout.cheng
 */
public class ReminderDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 396075564765316335L;
	
	private Long id;
	private String contractNo;	
	private Date date;
	private String comment;
	private boolean dismiss;

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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
	 * @return the dismiss
	 */
	public boolean isDismiss() {
		return dismiss;
	}

	/**
	 * @param dismiss the dismiss to set
	 */
	public void setDismiss(boolean dismiss) {
		this.dismiss = dismiss;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ReminderDTO)) {
			 return false;
		 }
		 ReminderDTO reminderDTO = (ReminderDTO) arg0;
		 return getId() != null && getId().equals(reminderDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
