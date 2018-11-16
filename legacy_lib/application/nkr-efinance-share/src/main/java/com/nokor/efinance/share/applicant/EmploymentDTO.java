package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author youhort.ly
 *
 */
public class EmploymentDTO implements Serializable {
	/** */
	private static final long serialVersionUID = 4288524795479334115L;

	private Long id;
	private String position;
	private String companyName;
	private Date since;
	private Integer workingPeriodInYear;
	private Integer workingPeriodInMonth;
	private Double income;

	private String commercialID;
	private RefDataDTO companySector;
	private RefDataDTO occupationGroups;
	
	private RefDataDTO employmentType;
	private RefDataDTO employmentCategory;
	private RefDataDTO companySize;
	
	private String companyPhone;
	private String managerName;
	private String managerPhone;
	private String departmentPhone;

	private AddressDTO address;

	public EmploymentDTO() {
		
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the since
	 */
	public Date getSince() {
		return since;
	}

	/**
	 * @param since the since to set
	 */
	public void setSince(Date since) {
		this.since = since;
	}

	/**
	 * @return the workingPeriodInYear
	 */
	public Integer getWorkingPeriodInYear() {
		return workingPeriodInYear;
	}

	/**
	 * @param workingPeriodInYear the workingPeriodInYear to set
	 */
	public void setWorkingPeriodInYear(Integer workingPeriodInYear) {
		this.workingPeriodInYear = workingPeriodInYear;
	}

	/**
	 * @return the workingPeriodInMonth
	 */
	public Integer getWorkingPeriodInMonth() {
		return workingPeriodInMonth;
	}

	/**
	 * @param workingPeriodInMonth the workingPeriodInMonth to set
	 */
	public void setWorkingPeriodInMonth(Integer workingPeriodInMonth) {
		this.workingPeriodInMonth = workingPeriodInMonth;
	}

	/**
	 * @return the income
	 */
	public Double getIncome() {
		return income;
	}

	/**
	 * @param income the income to set
	 */
	public void setIncome(Double income) {
		this.income = income;
	}	

	/**
	 * @return the employmentType
	 */
	public RefDataDTO getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType the employmentType to set
	 */
	public void setEmploymentType(RefDataDTO employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * @return the employmentCategory
	 */
	public RefDataDTO getEmploymentCategory() {
		return employmentCategory;
	}

	/**
	 * @param employmentCategory the employmentCategory to set
	 */
	public void setEmploymentCategory(RefDataDTO employmentCategory) {
		this.employmentCategory = employmentCategory;
	}

	/**
	 * @return the commercialID
	 */
	public String getCommercialID() {
		return commercialID;
	}

	/**
	 * @param commercialID the commercialID to set
	 */
	public void setCommercialID(String commercialID) {
		this.commercialID = commercialID;
	}	

	/**
	 * @return the companySector
	 */
	public RefDataDTO getCompanySector() {
		return companySector;
	}

	/**
	 * @param companySector the companySector to set
	 */
	public void setCompanySector(RefDataDTO companySector) {
		this.companySector = companySector;
	}

	/**
	 * @return the occupationGroups
	 */
	public RefDataDTO getOccupationGroups() {
		return occupationGroups;
	}

	/**
	 * @param occupationGroups the occupationGroups to set
	 */
	public void setOccupationGroups(RefDataDTO occupationGroups) {
		this.occupationGroups = occupationGroups;
	}

	/**
	 * @return the companyPhone
	 */
	public String getCompanyPhone() {
		return companyPhone;
	}

	/**
	 * @param companyPhone the companyPhone to set
	 */
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	/**
	 * @return the companySize
	 */
	public RefDataDTO getCompanySize() {
		return companySize;
	}

	/**
	 * @param companySize the companySize to set
	 */
	public void setCompanySize(RefDataDTO companySize) {
		this.companySize = companySize;
	}

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}

	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * @return the managerPhone
	 */
	public String getManagerPhone() {
		return managerPhone;
	}

	/**
	 * @param managerPhone the managerPhone to set
	 */
	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	/**
	 * @return the address
	 */
	public AddressDTO getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDTO address) {
		this.address = address;
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof EmploymentDTO)) {
			 return false;
		 }
		 EmploymentDTO employmentDTO = (EmploymentDTO) arg0;
		 return getId() != null && getId().equals(employmentDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}

	/**
	 * @return the departmentPhone
	 */
	public String getDepartmentPhone() {
		return departmentPhone;
	}

	/**
	 * @param departmentPhone the departmentPhone to set
	 */
	public void setDepartmentPhone(String departmentPhone) {
		this.departmentPhone = departmentPhone;
	}
}
