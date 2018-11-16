package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.Gson;


/**
 * 
 * @author uhout.cheng
 */
public class ApplicantCriteriaDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -1988114757789578120L;
	
	public static final int SEARCH_BY_INDIVIDUAL = 1; 
	public static final int SEARCH_BY_COMPANY = 2; 
	
	private int applicantCategoryID;
	private String applicantID;
	private Date createDate;
	private String status;
	private String idNumber;
	private String phoneNumber;
	private String firstName;
	private String lastName;

	/**
	 * @return the applicantCategoryID
	 */
	public int getApplicantCategoryID() {
		return applicantCategoryID;
	}

	/**
	 * @param applicantCategoryID the applicantCategoryID to set
	 */
	public void setApplicantCategoryID(int applicantCategoryID) {
		this.applicantCategoryID = applicantCategoryID;
	}

	/**
	 * @return the applicantID
	 */
	public String getApplicantID() {
		return applicantID;
	}

	/**
	 * @param applicantID the applicantID to set
	 */
	public void setApplicantID(String applicantID) {
		this.applicantID = applicantID;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
