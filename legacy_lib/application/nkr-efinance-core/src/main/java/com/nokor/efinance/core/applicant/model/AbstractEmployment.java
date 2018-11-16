package com.nokor.efinance.core.applicant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.address.BaseAddress;
import com.nokor.ersys.core.hr.model.eref.ECompanySize;
import com.nokor.ersys.core.hr.model.eref.EEmploymentCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustryCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ELegalForm;
import com.nokor.ersys.core.hr.model.eref.ESeniorityLevel;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractEmployment extends EntityA {
	/** */
	private static final long serialVersionUID = 4288524795479334115L;

	private String position;
	private String licenceNo;
	private String employerName;
	private Date since;
	private Integer timeWithEmployerInYear;
	private Integer timeWithEmployerInMonth;
	private Double revenue;
	private Double allowance;
	private Double businessExpense;
	private Integer noMonthInYear;
	private EEmploymentStatus employmentStatus;
	private EEmploymentIndustry employmentIndustry;
	private EEmploymentIndustryCategory employmentIndustryCategory;
	private EEmploymentType employmentType;
	private EEmploymentCategory employmentCategory;
	private ELegalForm legalForm;
	private boolean allowCallToWorkPlace;
	private boolean sameApplicantAddress;
	private String workPhone;
	private String managerPhone;
	private String managerName;
	private ECompanySize companySize;
	private ESeniorityLevel seniorityLevel;
	private String empRemarks;
	private String departmentPhone;
	


	/**
	 * @return the position
	 */
	@Column(name = "emp_va_position", nullable = true, length = 100)
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
	 * @return the licenceNo
	 */
	@Column(name = "emp_va_licence_no", nullable = true, length = 50)
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
	 * @return the employerName
	 */
	@Column(name = "emp_va_employer_name", nullable = true, length = 100)
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
	 * @return the since
	 */
	@Column(name = "emp_dt_since", nullable = true)
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
	 * @return the timeWithEmployerInYear
	 */
	@Column(name = "emp_nu_time_with_employer_year", nullable = true)
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
	@Column(name = "emp_nu_time_with_employer_month", nullable = true)
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
	@Column(name = "emp_am_revenu", nullable = true)
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
	 * @return the allowance
	 */
	@Column(name = "emp_am_allowance", nullable = true)
	public Double getAllowance() {
		return allowance;
	}

	/**
	 * @param allowance the allowance to set
	 */
	public void setAllowance(Double allowance) {
		this.allowance = allowance;
	}

	/**
	 * @return the businessExpense
	 */
	@Column(name = "emp_am_business_expense", nullable = true)
	public Double getBusinessExpense() {
		return businessExpense;
	}

	/**
	 * @param businessExpense the businessExpense to set
	 */
	public void setBusinessExpense(Double businessExpense) {
		this.businessExpense = businessExpense;
	}
	
	/**
	 * @return the noMonthInYear
	 */
	@Column(name = "emp_nu_no_month_in_year", nullable = true)
	public Integer getNoMonthInYear() {
		return noMonthInYear;
	}

	/**
	 * @param noMonthInYear the noMonthInYear to set
	 */
	public void setNoMonthInYear(Integer noMonthInYear) {
		this.noMonthInYear = noMonthInYear;
	}

	/**
	 * @return the employmentStatus
	 */
    @Column(name = "emp_sta_id", nullable = true)
    @Convert(converter = EEmploymentStatus.class)
	public EEmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * @param employmentStatus the employmentStatus to set
	 */
	public void setEmploymentStatus(EEmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	

	/**
	 * @return the employmentIndustry
	 */
    @Column(name = "emp_ind_id", nullable = true)
    @Convert(converter = EEmploymentIndustry.class)
	public EEmploymentIndustry getEmploymentIndustry() {
		return employmentIndustry;
	}

	/**
	 * @param employmentIndustry the employmentIndustry to set
	 */
	public void setEmploymentIndustry(EEmploymentIndustry employmentIndustry) {
		this.employmentIndustry = employmentIndustry;
	}	

	/**
	 * @return the employmentIndustryCategory
	 */
	@Column(name = "emp_ind_cat_id", nullable = true)
    @Convert(converter = EEmploymentIndustryCategory.class)
	public EEmploymentIndustryCategory getEmploymentIndustryCategory() {
		return employmentIndustryCategory;
	}

	/**
	 * @param employmentIndustryCategory the employmentIndustryCategory to set
	 */
	public void setEmploymentIndustryCategory(EEmploymentIndustryCategory employmentIndustryCategory) {
		this.employmentIndustryCategory = employmentIndustryCategory;
	}

	/**
	 * @return the employmentType
	 */
    @Column(name = "emp_typ_id", nullable = true)
    @Convert(converter = EEmploymentType.class)
	public EEmploymentType getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType the employmentType to set
	 */
	public void setEmploymentType(EEmploymentType employmentType) {
		this.employmentType = employmentType;
	}	
	
	/**
	 * @return the employmentCategory
	 */
	@Column(name = "emp_cat_id", nullable = true)
    @Convert(converter = EEmploymentCategory.class)
	public EEmploymentCategory getEmploymentCategory() {
		return employmentCategory;
	}

	/**
	 * @param employmentCategory the employmentCategory to set
	 */
	public void setEmploymentCategory(EEmploymentCategory employmentCategory) {
		this.employmentCategory = employmentCategory;
	}
	
	/**
	 * @return the legalForm
	 */
	@Column(name = "leg_for_id", nullable = true)
    @Convert(converter = ELegalForm.class)
	public ELegalForm getLegalForm() {
		return legalForm;
	}

	/**
	 * @param legalForm the legalForm to set
	 */
	public void setLegalForm(ELegalForm legalForm) {
		this.legalForm = legalForm;
	}

	/**
	 * @return the companySize
	 */
	@Column(name = "com_siz_id", nullable = true)
    @Convert(converter = ECompanySize.class)
	public ECompanySize getCompanySize() {
		return companySize;
	}


	/**
	 * @param companySize the companySize to set
	 */
	public void setCompanySize(ECompanySize companySize) {
		this.companySize = companySize;
	}

	
	/**
	 * @return the allowCallToWorkPlace
	 */
	@Column(name = "emp_bl_allow_call_work_place", nullable = true)
	public boolean isAllowCallToWorkPlace() {
		return allowCallToWorkPlace;
	}

	/**
	 * @param allowCallToWorkPlace the allowCallToWorkPlace to set
	 */
	public void setAllowCallToWorkPlace(boolean allowCallToWorkPlace) {
		this.allowCallToWorkPlace = allowCallToWorkPlace;
	}

	
	/**
	 * @return the sameApplicantAddress
	 */
	@Column(name = "emp_bl_same_applicant_address", nullable = true)
	public boolean isSameApplicantAddress() {
		return sameApplicantAddress;
	}

	/**
	 * @param sameApplicantAddress the sameApplicantAddress to set
	 */
	public void setSameApplicantAddress(boolean sameApplicantAddress) {
		this.sameApplicantAddress = sameApplicantAddress;
	}
	
	/**
	 * @return the workPhone
	 */
	@Column(name = "emp_va_work_phone", nullable = true, length = 100)
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}	
	
	/**
	 * @return the managerPhone
	 */
	@Column(name = "emp_va_manager_phone", nullable = true, length = 30)
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
	 * @return the managerName
	 */
	@Column(name = "emp_va_manager_name", nullable = true, length = 100)
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
	 * @return the seniorityLevel
	 */
    @Column(name = "sen_lev_id", nullable = true)
    @Convert(converter = ESeniorityLevel.class)
	public ESeniorityLevel getSeniorityLevel() {
		return seniorityLevel;
	}

	/**
	 * @param seniorityLevel the seniorityLevel to set
	 */
	public void setSeniorityLevel(ESeniorityLevel seniorityLevel) {
		this.seniorityLevel = seniorityLevel;
	}

	/**
	 * @return the empRemarks
	 */
	@Column(name = "emp_remarks", nullable = true, length = 1000)
	public String getEmpRemarks() {
		return empRemarks;
	}

	/**
	 * @param empRemarks the empRemarks to set
	 */
	public void setEmpRemarks(String empRemarks) {
		this.empRemarks = empRemarks;
	}
	
	@Transient
	public abstract BaseAddress getBaseAddress();
	
	@Transient
	public boolean isPersistent() {
		return getId() != null
				|| StringUtils.isNotEmpty(position)
				|| employmentStatus != null
				|| employmentIndustry != null
				|| StringUtils.isNotEmpty(employerName)
				|| (revenue != null && revenue > 0)
				|| (businessExpense != null && businessExpense > 0) 
				|| (noMonthInYear != null && noMonthInYear > 0)
				|| (getBaseAddress() != null && getBaseAddress().isPersistent());
	}

	/**
	 * @return the departmentPhone
	 */
	@Column(name = "emp_va_department_phone", nullable = true, length = 30)
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
