package com.nokor.efinance.share.campaign;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class CampaignTermDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 123048437619061231L;

	private Long id;
	private Integer term;
		
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
	 * @return the term
	 */
	public Integer getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
		
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CampaignTermDTO)) {
			 return false;
		 }
		 CampaignTermDTO campaignTermDTO = (CampaignTermDTO) arg0;
		 return getId() != null && getId().equals(campaignTermDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
