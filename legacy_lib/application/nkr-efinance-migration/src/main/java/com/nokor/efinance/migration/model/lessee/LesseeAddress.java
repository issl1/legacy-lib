package com.nokor.efinance.migration.model.lessee;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tm_lessee_address")
public class LesseeAddress extends EntityRefA implements Serializable {
		
	/**
	 */
	private static final long serialVersionUID = 2346753189119112220L;
	
	private Long id;
	private String migrationID;
	private String typeAddressCode;
	private String houseNo;
	private String line1;
	private String line2;
	private String street;
	private String provinceCode;
	private String districtCode;
	private String communeCode;
	private String postalCode;
	private Integer timeAtAddressInYear;
	private Integer timeAtAddressInMonth;
	private String residenceStatusCode;	
	
	/**
	 * @return the id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 * @return the migrationID
	 */
	public String getMigrationID() {
		return migrationID;
	}
	/**
	 * @param migrationID the migrationID to set
	 */
	public void setMigrationID(String migrationID) {
		this.migrationID = migrationID;
	}
	/**
	 * @return the typeAddressCode
	 */
	public String getTypeAddressCode() {
		return typeAddressCode;
	}
	/**
	 * @param typeAddressCode the typeAddressCode to set
	 */
	public void setTypeAddressCode(String typeAddressCode) {
		this.typeAddressCode = typeAddressCode;
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
	 * @return the line1
	 */
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
	 * @return the provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	/**
	 * @param provinceCode the provinceCode to set
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	/**
	 * @return the districtCode
	 */
	public String getDistrictCode() {
		return districtCode;
	}
	/**
	 * @param districtCode the districtCode to set
	 */
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	/**
	 * @return the communeCode
	 */
	public String getCommuneCode() {
		return communeCode;
	}
	/**
	 * @param communeCode the communeCode to set
	 */
	public void setCommuneCode(String communeCode) {
		this.communeCode = communeCode;
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
	 * @return the timeAtAddressInYear
	 */
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
	 * @return the residenceStatusCode
	 */
	public String getResidenceStatusCode() {
		return residenceStatusCode;
	}
	/**
	 * @param residenceStatusCode the residenceStatusCode to set
	 */
	public void setResidenceStatusCode(String residenceStatusCode) {
		this.residenceStatusCode = residenceStatusCode;
	}	
}
