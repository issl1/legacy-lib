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
@Table(name = "tm_lessee_employment")
public class LesseeEmployment extends EntityRefA implements Serializable {
		
	/**
	 */
	private static final long serialVersionUID = -608404964571544566L;
	
	private Long id;
	private String migrationID;
	private String employmentTypeCode;
	private String employmentCategoryCode;
	private String employerName;
	private String position;
	
	private Integer timeWithEmployerInYear;
	private Integer timeWithEmployerInMonth;
				
	private Double revenue;
		
	private String houseNo;
	private String line1;
	private String line2;
	private String street;
	private String provinceCode;
	private String districtCode;
	private String communeCode;
	private String postalCode;
	private String workPhone;
	
	
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
	 * @return the employmentTypeCode
	 */
	public String getEmploymentTypeCode() {
		return employmentTypeCode;
	}
	/**
	 * @param employmentTypeCode the employmentTypeCode to set
	 */
	public void setEmploymentTypeCode(String employmentTypeCode) {
		this.employmentTypeCode = employmentTypeCode;
	}
	/**
	 * @return the employmentCategoryCode
	 */
	public String getEmploymentCategoryCode() {
		return employmentCategoryCode;
	}
	/**
	 * @param employmentCategoryCode the employmentCategoryCode to set
	 */
	public void setEmploymentCategoryCode(String employmentCategoryCode) {
		this.employmentCategoryCode = employmentCategoryCode;
	}
	/**
	 * @return the employerName
	 */
	public String getEmployerName() {
		return employerName;
	}
	/**
	 * @param employerName the employerName to set
	 */
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the timeWithEmployerInYear
	 */
	public Integer getTimeWithEmployerInYear() {
		return timeWithEmployerInYear;
	}
	/**
	 * @param timeWithEmployerInYear the timeWithEmployerInYear to set
	 */
	public void setTimeWithEmployerInYear(Integer timeWithEmployerInYear) {
		this.timeWithEmployerInYear = timeWithEmployerInYear;
	}
	/**
	 * @return the timeWithEmployerInMonth
	 */
	public Integer getTimeWithEmployerInMonth() {
		return timeWithEmployerInMonth;
	}
	/**
	 * @param timeWithEmployerInMonth the timeWithEmployerInMonth to set
	 */
	public void setTimeWithEmployerInMonth(Integer timeWithEmployerInMonth) {
		this.timeWithEmployerInMonth = timeWithEmployerInMonth;
	}
	/**
	 * @return the revenue
	 */
	public Double getRevenue() {
		return revenue;
	}
	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
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
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}
	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}	
}
