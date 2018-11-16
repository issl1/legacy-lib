package com.nokor.ersys.messaging.share.address;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class DistrictDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 7937444681873168917L;
	
	private Long id;
	private String code;
	private String descEn;
	private String desc;
	private ProvinceDTO province;
	private String gpsCoordinates;
	private Double latitude;
	private Double longitude;
	
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
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}
	
	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
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
	
	/**
	 * @return the province
	 */
	public ProvinceDTO getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(ProvinceDTO province) {
		this.province = province;
	}

	/**
	 * @return the gpsCoordinates
	 */
	public String getGpsCoordinates() {
		return gpsCoordinates;
	}
	
	/**
	 * @param gpsCoordinates the gpsCoordinates to set
	 */
	public void setGpsCoordinates(String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}
	
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof DistrictDTO)) {
			 return false;
		 }
		 DistrictDTO districtDTO = (DistrictDTO) arg0;
		 return getId() != null && getId().equals(districtDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}


