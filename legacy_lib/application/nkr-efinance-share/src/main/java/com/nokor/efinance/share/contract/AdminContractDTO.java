package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;


/**
 * @author youhort.ly
 *
 */
public class AdminContractDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -524001051815327908L;
	
	private Long id;
	private Date sendContractDocumentDate;
	
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
	 * @return the sendContractDocumentDate
	 */
	public Date getSendContractDocumentDate() {
		return sendContractDocumentDate;
	}
	/**
	 * @param sendContractDocumentDate the sendContractDocumentDate to set
	 */
	public void setSendContractDocumentDate(Date sendContractDocumentDate) {
		this.sendContractDocumentDate = sendContractDocumentDate;
	}
}
