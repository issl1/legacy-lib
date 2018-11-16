package com.nokor.ersys.messaging.share.accounting;

import java.io.Serializable;
import java.util.List;

public class TransactionEntryDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -7289346318090013794L;
	
	private Long id;
	private String desc;
	private List<JournalEntryDTO> journalEntries;
	private List<String> callBackUrls;
	private TransactionEntryStatus status;  // DRAFT, REJECTED, APPROVED, POSTED
	
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
	 * @return the journalEntries
	 */
	public List<JournalEntryDTO> getJournalEntries() {
		return journalEntries;
	}
	/**
	 * @param journalEntries the journalEntries to set
	 */
	public void setJournalEntries(List<JournalEntryDTO> journalEntries) {
		this.journalEntries = journalEntries;
	}
	/**
	 * @return the callBackUrls
	 */
	public List<String> getCallBackUrls() {
		return callBackUrls;
	}
	/**
	 * @param callBackUrls the callBackUrls to set
	 */
	public void setCallBackUrls(List<String> callBackUrls) {
		this.callBackUrls = callBackUrls;
	}
	/**
	 * @return the status
	 */
	public TransactionEntryStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(TransactionEntryStatus status) {
		this.status = status;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
