package com.nokor.efinance.migration.model.lessee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name = "tm_lessee")
public class Lessee extends EntityRefA implements Serializable{

	/** 
	 */
	private static final long serialVersionUID = -3589266960180733198L;
	
	private Long id;
	private String migrationID;
	private String typeIdNumberCode;
	private String idNumber;
	private Date issuingIdNumberDate;
	private Date expiringIdNumberDate;
	
	private String reference;
	private String civilityCode;
	
	private String firstName;
	private String lastName;	
	private String nickName;
	private Date BirthDate;
	private String maritalStatusCode;
	private Integer numberOfChildren;
	private String educationCode;
	private Integer numberOfHousehold;
	private String nationalityCode;
	private String religionCode;
	private String secondLanguageCode;
	private Double householdExpenses;
	private Double householdIncome;
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
	 * @return the typeIdNumberCode
	 */
	public String getTypeIdNumberCode() {
		return typeIdNumberCode;
	}
	/**
	 * @param typeIdNumberCode the typeIdNumberCode to set
	 */
	public void setTypeIdNumberCode(String typeIdNumberCode) {
		this.typeIdNumberCode = typeIdNumberCode;
	}
	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return the issuingIdNumberDate
	 */
	public Date getIssuingIdNumberDate() {
		return issuingIdNumberDate;
	}
	/**
	 * @param issuingIdNumberDate the issuingIdNumberDate to set
	 */
	public void setIssuingIdNumberDate(Date issuingIdNumberDate) {
		this.issuingIdNumberDate = issuingIdNumberDate;
	}
	/**
	 * @return the expiringIdNumberDate
	 */
	public Date getExpiringIdNumberDate() {
		return expiringIdNumberDate;
	}
	/**
	 * @param expiringIdNumberDate the expiringIdNumberDate to set
	 */
	public void setExpiringIdNumberDate(Date expiringIdNumberDate) {
		this.expiringIdNumberDate = expiringIdNumberDate;
	}
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	/**
	 * @return the civilityCode
	 */
	public String getCivilityCode() {
		return civilityCode;
	}
	/**
	 * @param civilityCode the civilityCode to set
	 */
	public void setCivilityCode(String civilityCode) {
		this.civilityCode = civilityCode;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return BirthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		BirthDate = birthDate;
	}
	/**
	 * @return the maritalStatusCode
	 */
	public String getMaritalStatusCode() {
		return maritalStatusCode;
	}
	/**
	 * @param maritalStatusCode the maritalStatusCode to set
	 */
	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}
	/**
	 * @return the numberOfChildren
	 */
	public Integer getNumberOfChildren() {
		return numberOfChildren;
	}
	/**
	 * @param numberOfChildren the numberOfChildren to set
	 */
	public void setNumberOfChildren(Integer numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
	/**
	 * @return the educationCode
	 */
	public String getEducationCode() {
		return educationCode;
	}
	/**
	 * @param educationCode the educationCode to set
	 */
	public void setEducationCode(String educationCode) {
		this.educationCode = educationCode;
	}
	/**
	 * @return the numberOfHousehold
	 */
	public Integer getNumberOfHousehold() {
		return numberOfHousehold;
	}
	/**
	 * @param numberOfHousehold the numberOfHousehold to set
	 */
	public void setNumberOfHousehold(Integer numberOfHousehold) {
		this.numberOfHousehold = numberOfHousehold;
	}
	/**
	 * @return the nationalityCode
	 */
	public String getNationalityCode() {
		return nationalityCode;
	}
	/**
	 * @param nationalityCode the nationalityCode to set
	 */
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	/**
	 * @return the religionCode
	 */
	public String getReligionCode() {
		return religionCode;
	}
	/**
	 * @param religionCode the religionCode to set
	 */
	public void setReligionCode(String religionCode) {
		this.religionCode = religionCode;
	}
	/**
	 * @return the secondLanguageCode
	 */
	public String getSecondLanguageCode() {
		return secondLanguageCode;
	}
	/**
	 * @param secondLanguageCode the secondLanguageCode to set
	 */
	public void setSecondLanguageCode(String secondLanguageCode) {
		this.secondLanguageCode = secondLanguageCode;
	}
	/**
	 * @return the householdExpenses
	 */
	public Double getHouseholdExpenses() {
		return householdExpenses;
	}
	/**
	 * @param householdExpenses the householdExpenses to set
	 */
	public void setHouseholdExpenses(Double householdExpenses) {
		this.householdExpenses = householdExpenses;
	}
	/**
	 * @return the householdIncome
	 */
	public Double getHouseholdIncome() {
		return householdIncome;
	}
	/**
	 * @param householdIncome the householdIncome to set
	 */
	public void setHouseholdIncome(Double householdIncome) {
		this.householdIncome = householdIncome;
	}
}
