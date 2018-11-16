package com.nokor.efinance.share.area;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.share.campaign.CampaignDTO;

/**
 * 
 * @author uhout.cheng
 */
public class AreaDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -226272002963780589L;
	
	private Long id;
	private String code;
	private String shordCode;
	private String postalCode;
	private String street;
	
	private UriDTO province;
	private UriDTO district;
	private UriDTO commune;

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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the shordCode
	 */
	public String getShordCode() {
		return shordCode;
	}

	/**
	 * @param shordCode the shordCode to set
	 */
	public void setShordCode(String shordCode) {
		this.shordCode = shordCode;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the province
	 */
	public UriDTO getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(UriDTO province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	public UriDTO getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(UriDTO district) {
		this.district = district;
	}

	/**
	 * @return the commune
	 */
	public UriDTO getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	public void setCommune(UriDTO commune) {
		this.commune = commune;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CampaignDTO)) {
			 return false;
		 }
		 AreaDTO areaDTO = (AreaDTO) arg0;
		 return getId() != null && getId().equals(areaDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
