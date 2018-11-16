package com.nokor.ersys.core.hr.model.address;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.ECategoryAddress;
import com.nokor.ersys.core.hr.model.eref.EResidenceStatus;
import com.nokor.ersys.core.hr.model.eref.EResidenceType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;


/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseAddress extends EntityA implements MBaseAddress {
	/** */
	private static final long serialVersionUID = 5424910653571001449L;

	private String line1;
	private String line2;
	private String line3;
	private String buildingName;
	private String postalCode;
	private String city;
	private ETypeAddress type;
	private ECategoryAddress category;
	
	private Province province;
	private District district;
	private Commune commune;
	private Village village;
	private ECountry country;
	
	private Double latitude;
	private Double longitude;
	
	private String houseNo;
	private String street;
	private String floor;
	private String roomNumber;
	private Integer timeAtAddressInYear;
	private Integer timeAtAddressInMonth;
	private EResidenceStatus residenceStatus;
	private EResidenceType residenceType;

	private String freeField1; // Village
	private String freeField2; // Commune
	private String freeField3; // District
	private String freeField4; // Province
	private String freeField5; // City

	private String comment;

	private String oldAddress;
	private Integer yearOfData; 

	/**
	 * @return the line1
	 */
	@Column(name = "add_line1", nullable = true, length = 100)
	public String getLine1() {
		return line1;
	}

	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	@Column(name = "add_line2", nullable = true, length = 100)
	public String getLine2() {
		return line2;
	}

	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	/**
	 * @return the line3
	 */
	@Column(name = "add_line3", nullable = true, length = 100)
	public String getLine3() {
		return line3;
	}

	/**
	 * @param line3 the line3 to set
	 */
	public void setLine3(String line3) {
		this.line3 = line3;
	}
			
	/**
	 * @return the buildingName
	 */
	@Column(name = "add_building_name", nullable = true, length = 100)
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
	@Column(name = "add_postal_code", nullable = true, length = 10)
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
	@Column(name = "add_city", nullable = true, length = 100)
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
	 * @return the type
	 */
    @Column(name = "typ_add_id", nullable = true)
    @Convert(converter = ETypeAddress.class)
	public ETypeAddress getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ETypeAddress type) {
		this.type = type;
	}

	/**
	 * @return the category
	 */
    @Column(name = "cat_add_id", nullable = true)
    @Convert(converter = ECategoryAddress.class)
	public ECategoryAddress getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ECategoryAddress category) {
		this.category = category;
	}	

	/**
	 * @return the province
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id", nullable = true)
	public Province getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Province province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dis_id", nullable = true)
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(District district) {
		this.district = district;
	}

	/**
	 * @return the commune
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "com_id", nullable = true)
	public Commune getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	public void setCommune(Commune commune) {
		this.commune = commune;
	}

	/**
	 * @return the village
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vil_id", nullable = true)
	public Village getVillage() {
		return village;
	}

	/**
	 * @param village the village to set
	 */
	public void setVillage(Village village) {
		this.village = village;
	}

	/**
	 * @return the country
	 */
    @Column(name = "cou_id", nullable = true)
    @Convert(converter = ECountry.class)
	public ECountry getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(ECountry country) {
		this.country = country;
	}

	 
    /**
	 * @return the latitude
	 */
    @Column(name = "add_latitude", nullable = true)
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
	@Column(name = "add_longitude", nullable = true)
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
    @Column(name = "add_va_house_no", nullable = true, length = 100)
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
	@Column(name = "add_va_street", nullable = true, length = 100)
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
	@Column(name = "add_va_floor", nullable = true, length = 100)
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
	 * @return the roomNumber
	 */
	@Column(name = "add_va_room_number", nullable = true, length = 100)
	public String getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	/**
	 * @return the timeAtAddressInYear
	 */
	@Column(name = "add_nu_time_in_year", nullable = true)
	public Integer getTimeAtAddressInYear() {
		return timeAtAddressInYear;
	}

	/**
	 * @param timeAtAddressInYear the timeAtAddressInYear to set
	 */
	public void setTimeAtAddressInYear(Integer timeAtAddressInYear) {
		this.timeAtAddressInYear = timeAtAddressInYear;
	}

	/**
	 * @return the timeAtAddressInMonth
	 */
	@Column(name = "add_nu_time_in_month", nullable = true)
	public Integer getTimeAtAddressInMonth() {
		return timeAtAddressInMonth;
	}

	/**
	 * @param timeAtAddressInMonth the timeAtAddressInMonth to set
	 */
	public void setTimeAtAddressInMonth(Integer timeAtAddressInMonth) {
		this.timeAtAddressInMonth = timeAtAddressInMonth;
	}
	
	/**
	 * @return the residenceStatus
	 */
	@Column(name = "res_id", nullable = true)
    @Convert(converter = EResidenceStatus.class)
	public EResidenceStatus getResidenceStatus() {
		return residenceStatus;
	}

	/**
	 * @param residenceStatus the residenceStatus to set
	 */
	public void setResidenceStatus(EResidenceStatus residenceStatus) {
		this.residenceStatus = residenceStatus;
	}
	
	/**
	 * @return the residenceType
	 */
	@Column(name = "res_typ_id", nullable = true)
    @Convert(converter = EResidenceType.class)
	public EResidenceType getResidenceType() {
		return residenceType;
	}

	/**
	 * @param residenceType the residenceType to set
	 */
	public void setResidenceType(EResidenceType residenceType) {
		this.residenceType = residenceType;
	}
    
	@Transient
	public boolean isPersistent() {
		return  getId() != null
				|| StringUtils.isNotEmpty(houseNo)
				|| StringUtils.isNotEmpty(street)
				|| StringUtils.isNotEmpty(getLine1())
				|| StringUtils.isNotEmpty(getLine2())
				|| StringUtils.isNotEmpty(getLine3())
				|| getCountry() != null
				|| getProvince() != null;
	}
	
	@Transient
	public String getResumeAddress() {
		String resumeAddress = "";
		resumeAddress = concat(resumeAddress, houseNo);
		resumeAddress = concat(resumeAddress, street);
		resumeAddress = concat(resumeAddress, getLine1());
		resumeAddress = concat(resumeAddress, getLine2());
		resumeAddress = concat(resumeAddress, getLine3());
		
		/*if (district != null && !"OTH".equals(district.getCode())) {
			resumeAddress = concat(resumeAddress, district.getDescEn());
		}
		if (commune != null && !"OTH".equals(commune.getCode())) {
			resumeAddress = concat(resumeAddress, commune.getDescEn());
		}*/
		
		if (getVillage() != null && !"OTH".equals(getVillage().getCode())) {
			resumeAddress = concat(resumeAddress, getVillage().getDescEn());
		}
		resumeAddress = concat(resumeAddress, getProvince().getDescEn());
		return resumeAddress;
	}
	
	@Transient
	private String concat(String resumeAddress, String value) {
		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(resumeAddress)) {
				resumeAddress += ", ";
			}
			resumeAddress += value;
		}
		return resumeAddress;
	}


	@Transient
	public EResidenceStatus getProperty() {
		return getResidenceStatus();
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(EResidenceStatus property) {
		this.setResidenceStatus(property);
	}
	
	/**
	 * @return the freeField1
	 */
	@Column(name = "add_va_free_field_1", nullable = true, length = 30)
	public String getFreeField1() {
		return freeField1;
	}

	/**
	 * @param freeField1 the freeField1 to set
	 */
	public void setFreeField1(String freeField1) {
		this.freeField1 = freeField1;
	}

	/**
	 * @return the freeField2
	 */
	@Column(name = "add_va_free_field_2", nullable = true, length = 30)
	public String getFreeField2() {
		return freeField2;
	}

	/**
	 * @param freeField2 the freeField2 to set
	 */
	public void setFreeField2(String freeField2) {
		this.freeField2 = freeField2;
	}

	/**
	 * @return the freeField3
	 */
	@Column(name = "add_va_free_field_3", nullable = true, length = 30)
	public String getFreeField3() {
		return freeField3;
	}

	/**
	 * @param freeField3 the freeField3 to set
	 */
	public void setFreeField3(String freeField3) {
		this.freeField3 = freeField3;
	}

	/**
	 * @return the freeField4
	 */
	@Column(name = "add_va_free_field_4", nullable = true, length = 30)
	public String getFreeField4() {
		return freeField4;
	}

	/**
	 * @param freeField4 the freeField4 to set
	 */
	public void setFreeField4(String freeField4) {
		this.freeField4 = freeField4;
	}

	/**
	 * @return the freeField5
	 */
	@Column(name = "add_va_free_field_5", nullable = true, length = 30)
	public String getFreeField5() {
		return freeField5;
	}

	/**
	 * @param freeField5 the freeField5 to set
	 */
	public void setFreeField5(String freeField5) {
		this.freeField5 = freeField5;
	}
	

	/**
	 * @return the comment
	 */
    @Column(name = "add_comment", nullable = true)
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
	 * @return the oldAddress
	 */
	 @Column(name = "old_address", nullable = true, length = 1000)
	public String getOldAddress() {
		return oldAddress;
	}

	/**
	 * @param oldAddress the oldAddress to set
	 */
	public void setOldAddress(String oldAddress) {
		this.oldAddress = oldAddress;
	}
	/**
	 * @return the yearOfData
	 */
	@Column(name = "year_of_data", nullable = true)
	public Integer getYearOfData() {
		return yearOfData;
	}

	/**
	 * @param yearOfData the yearOfData to set
	 */
	public void setYearOfData(Integer yearOfData) {
		this.yearOfData = yearOfData;
	}
}
