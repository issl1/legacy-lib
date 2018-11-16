package com.nokor.ersys.messaging.share.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * 
 * @author bunlong.taing
 */
public class JournalEntryDTO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = 8615757002285304646L;

	private String desc;
	private String descEn;
	private String journalEventCode;
	private String reference;
	private String info;
	private String otherInfo;
	private Date when;
	private List<BigDecimal> amounts;
	private Long organizationId;
	private String wkfStatusCode;

	/**
     * 
     */
    public JournalEntryDTO() {
    	
    }
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @param descEn
	 *            the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the otherInfo
	 */
	public String getOtherInfo() {
		return otherInfo;
	}

	/**
	 * @param otherInfo
	 *            the otherInfo to set
	 */
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	/**
	 * @return the when
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when
	 *            the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return the amounts
	 */
	public List<BigDecimal> getAmounts() {
		return amounts;
	}

	/**
	 * @param amounts the amounts to set
	 */
	public void setAmounts(List<BigDecimal> amounts) {
		this.amounts = amounts;
	}

	/**
	 * @return the journalEventCode
	 */
	public String getJournalEventCode() {
		return journalEventCode;
	}

	/**
	 * @param journalEventCode the journalEventCode to set
	 */
	public void setJournalEventCode(String journalEventCode) {
		this.journalEventCode = journalEventCode;
	}

	/**
	 * @return the organizationId
	 */
	public Long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the wkfStatusCode
	 */
	public String getWkfStatusCode() {
		return wkfStatusCode;
	}

	/**
	 * @param wkfStatusCode the wkfStatusCode to set
	 */
	public void setWkfStatusCode(String wkfStatusCode) {
		this.wkfStatusCode = wkfStatusCode;
	}

}
