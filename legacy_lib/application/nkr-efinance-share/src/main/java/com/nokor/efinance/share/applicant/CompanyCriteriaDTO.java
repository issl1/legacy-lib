package com.nokor.efinance.share.applicant;

import java.io.Serializable;

import com.google.gson.Gson;


/**
 * 
 * @author uhout.cheng
 */
public class CompanyCriteriaDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -478524623130072816L;
	
	private String code;
	private String name;
	private String externalCode;
	private String vatRegistrationNo;
	private String licenceNo;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the externalCode
	 */
	public String getExternalCode() {
		return externalCode;
	}

	/**
	 * @param externalCode the externalCode to set
	 */
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	/**
	 * @return the vatRegistrationNo
	 */
	public String getVatRegistrationNo() {
		return vatRegistrationNo;
	}

	/**
	 * @param vatRegistrationNo the vatRegistrationNo to set
	 */
	public void setVatRegistrationNo(String vatRegistrationNo) {
		this.vatRegistrationNo = vatRegistrationNo;
	}

	/**
	 * @return the licenceNo
	 */
	public String getLicenceNo() {
		return licenceNo;
	}

	/**
	 * @param licenceNo the licenceNo to set
	 */
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
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
