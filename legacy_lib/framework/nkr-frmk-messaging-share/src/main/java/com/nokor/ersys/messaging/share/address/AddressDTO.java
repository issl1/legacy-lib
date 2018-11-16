package com.nokor.ersys.messaging.share.address;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author prasnar
 *
 */
public class AddressDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -604256803639969228L;

	private Long id;
	private String buildingName;
	private String moo;
	private String soi;
	private String subSoi;
	private String postalCode;
	private String city;
	private RefDataDTO addressType;
	private RefDataDTO category;
	
	private ProvinceDTO province;
	private SubDistrictDTO subDistrict;
	private DistrictDTO district;
	private RefDataDTO country;
	
	private Double latitude;
	private Double longitude;
	private String houseNo;
	private String street;
	private String floor;
	private String roomNo;
	private Integer livingPeriodInYear;
	private Integer livingPeriodInMonth;
	private RefDataDTO residenceStatus;
	private RefDataDTO residenceType;	
	
	/**
	 * 
	 */
	public AddressDTO() {
		
	}
		
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
	 * @return the moo
	 */
	public String getMoo() {
		return moo;
	}

	/**
	 * @param moo the moo to set
	 */
	public void setMoo(String moo) {
		this.moo = moo;
	}

	/**
	 * @return the soi
	 */
	public String getSoi() {
		return soi;
	}

	/**
	 * @param soi the soi to set
	 */
	public void setSoi(String soi) {
		this.soi = soi;
	}

	/**
	 * @return the subSoi
	 */
	public String getSubSoi() {
		return subSoi;
	}

	/**
	 * @param subSoi the subSoi to set
	 */
	public void setSubSoi(String subSoi) {
		this.subSoi = subSoi;
	}

	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}
	
	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
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
	 * @return the houseNo
	 */
	public String getHouseNo() {
		return houseNo;
	}
	
	/**
	 * @param houseNo the houseNo to set
	 */
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
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
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}
	
	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	/**
	 * @return the roomNo
	 */
	public String getRoomNo() {
		return roomNo;
	}
	
	/**
	 * @param roomNo the roomNo to set
	 */
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	/**
	 * @return the livingPeriodInYear
	 */
	public Integer getLivingPeriodInYear() {
		return livingPeriodInYear;
	}
	
	/**
	 * @param livingPeriodInYear the livingPeriodInYear to set
	 */
	public void setLivingPeriodInYear(Integer livingPeriodInYear) {
		this.livingPeriodInYear = livingPeriodInYear;
	}
	
	/**
	 * @return the livingPeriodInMonth
	 */
	public Integer getLivingPeriodInMonth() {
		return livingPeriodInMonth;
	}
	
	/**
	 * @param livingPeriodInMonth the livingPeriodInMonth to set
	 */
	public void setLivingPeriodInMonth(Integer livingPeriodInMonth) {
		this.livingPeriodInMonth = livingPeriodInMonth;
	}

	/**
	 * @return the addressType
	 */
	public RefDataDTO getAddressType() {
		return addressType;
	}

	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(RefDataDTO addressType) {
		this.addressType = addressType;
	}

	/**
	 * @return the category
	 */
	public RefDataDTO getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(RefDataDTO category) {
		this.category = category;
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
	 * @return the subDistrict
	 */
	public SubDistrictDTO getSubDistrict() {
		return subDistrict;
	}

	/**
	 * @param subDistrict the subDistrict to set
	 */
	public void setSubDistrict(SubDistrictDTO subDistrict) {
		this.subDistrict = subDistrict;
	}

	/**
	 * @return the district
	 */
	public DistrictDTO getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(DistrictDTO district) {
		this.district = district;
	}

	/**
	 * @return the country
	 */
	public RefDataDTO getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(RefDataDTO country) {
		this.country = country;
	}

	/**
	 * @return the residenceStatus
	 */
	public RefDataDTO getResidenceStatus() {
		return residenceStatus;
	}

	/**
	 * @param residenceStatus the residenceStatus to set
	 */
	public void setResidenceStatus(RefDataDTO residenceStatus) {
		this.residenceStatus = residenceStatus;
	}

	/**
	 * @return the residenceType
	 */
	public RefDataDTO getResidenceType() {
		return residenceType;
	}

	/**
	 * @param residenceType the residenceType to set
	 */
	public void setResidenceType(RefDataDTO residenceType) {
		this.residenceType = residenceType;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AddressDTO)) {
			 return false;
		 }
		 AddressDTO addressDTO = (AddressDTO) arg0;
		 return getId() != null && getId().equals(addressDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
