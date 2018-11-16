package com.nokor.efinance.share.campaign;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class CampaignCreditBureauGradeDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 5463189236910747428L;

	private Long id;
	private String grade;
	private Long campaignId;
		
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
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	/**
	 * @return the campaignId
	 */
	public Long getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CampaignCreditBureauGradeDTO)) {
			 return false;
		 }
		 CampaignCreditBureauGradeDTO campaignCreditBureauGradeDTO = (CampaignCreditBureauGradeDTO) arg0;
		 return getId() != null && getId().equals(campaignCreditBureauGradeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
