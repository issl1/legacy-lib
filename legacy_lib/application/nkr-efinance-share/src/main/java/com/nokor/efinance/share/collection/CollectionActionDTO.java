package com.nokor.efinance.share.collection;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionActionDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 1217812310185163436L;
	
	private Long id;
	private String creationUser;
	private Date creationDate;
	private String contractNo;
	private RefDataDTO colAction;
	private Date nextActionDate;
	private String userLogin;
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
	 * @return the creationUser
	 */
	public String getCreationUser() {
		return creationUser;
	}

	/**
	 * @param creationUser the creationUser to set
	 */
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
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
	 * @return the colAction
	 */
	public RefDataDTO getColAction() {
		return colAction;
	}

	/**
	 * @param colAction the colAction to set
	 */
	public void setColAction(RefDataDTO colAction) {
		this.colAction = colAction;
	}
	
	/**
	 * @return the nextActionDate
	 */
	public Date getNextActionDate() {
		return nextActionDate;
	}

	/**
	 * @param nextActionDate the nextActionDate to set
	 */
	public void setNextActionDate(Date nextActionDate) {
		this.nextActionDate = nextActionDate;
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
		 if (arg0 == null || !(arg0 instanceof CollectionActionDTO)) {
			 return false;
		 }
		 CollectionActionDTO collectionActionDTO = (CollectionActionDTO) arg0;
		 return getId() != null && getId().equals(collectionActionDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
