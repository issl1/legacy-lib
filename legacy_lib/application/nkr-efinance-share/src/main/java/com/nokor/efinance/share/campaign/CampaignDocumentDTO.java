package com.nokor.efinance.share.campaign;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class CampaignDocumentDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -4454042352467280605L;

	private Long id;
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
		 if (arg0 == null || !(arg0 instanceof CampaignDocumentDTO)) {
			 return false;
		 }
		 CampaignDocumentDTO campaignDocumentDTO = (CampaignDocumentDTO) arg0;
		 return getId() != null && getId().equals(campaignDocumentDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
