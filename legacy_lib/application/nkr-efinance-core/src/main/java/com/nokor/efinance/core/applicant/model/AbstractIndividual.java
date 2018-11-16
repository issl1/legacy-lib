package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.ersys.core.hr.model.eref.EEducation;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.ersys.core.hr.model.eref.EReligion;
import com.nokor.ersys.core.hr.model.eref.ETitle;
import com.nokor.ersys.core.hr.model.organization.BasePerson;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractIndividual extends BasePerson implements MAbstractIndividual {
	/** */
	private static final long serialVersionUID = 3834251079538576695L;

	private String reference;
	private String otherNationality;
	
	private Integer numberOfChildren;
	private Integer numberOfHousehold;
	
	private EReligion religion;
	private EEducation education;
	private ELanguage preferredLanguage;
	private ELanguage secondLanguage;
	private ETitle title;

	private Double monthlyGrossIncome;
	private Double monthlyExpenses;
	private Double monthlyPersonalExpenses;
	private Double monthlyFamilyExpenses;
	private boolean debtFromOtherSource;
	private Double totalDebtInstallment;
	private boolean guarantorOtherLoan;
	private String convenientVisitTime;
	private String grade;
	
	private Double householdExpenses;
	private Double householdIncome;
	
	private Integer totalFamilyMember;
	
	private Double fixedIncome;
	private Double otherIncomes;
	private Double debtOtherLoans;
	private String debtSource;
	private boolean staffFinancialCompany;
		
	private List<QuotationApplicant> quotationApplicants;
	

	private Integer timestamp;
	private String middleNameEn;
	
	
	/**
	 * @return the reference
	 */
	@Column(name = "ind_va_reference", nullable = true, length = 25)
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
	 * @return the numberOfChildren
	 */
	@Column(name = "ind_nu_number_children", nullable = true)
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
	 * @return the numberOfHousehold
	 */
	@Column(name = "ind_nu_number_house_hold", nullable = true)
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
	 * @return the religion
	 */
	@Column(name = "reg_id", nullable = true)
    @Convert(converter = EReligion.class)
	public EReligion getReligion() {
		return religion;
	}

	/**
	 * @param religion the religion to set
	 */
	public void setReligion(EReligion religion) {
		this.religion = religion;
	}

	/**
	 * @return the education
	 */
	@Column(name = "edu_id", nullable = true)
    @Convert(converter = EEducation.class)
	public EEducation getEducation() {
		return education;
	}

	/**
	 * @param education the education to set
	 */
	public void setEducation(EEducation education) {
		this.education = education;
	}
	
	/**
	 * @return the preferredLanguage
	 */
	@Column(name = "lan_id_preferred", nullable = true)
    @Convert(converter = ELanguage.class)
	public ELanguage getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(ELanguage preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	/**
	 * @return the secondLanguage
	 */
	@Column(name = "lan_id_second", nullable = true)
    @Convert(converter = ELanguage.class)
	public ELanguage getSecondLanguage() {
		return secondLanguage;
	}

	/**
	 * @param secondLanguage the secondLanguage to set
	 */
	public void setSecondLanguage(ELanguage secondLanguage) {
		this.secondLanguage = secondLanguage;
	}
	
	/**
	 * @return the title
	 */
	@Column(name = "tit_id", nullable = true)
    @Convert(converter = ETitle.class)
	public ETitle getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(ETitle title) {
		this.title = title;
	}

	/**
	 * @return the monthlyPersonalExpenses
	 */
	@Column(name = "ind_am_monthly_personal_expenses", nullable = true)
	public Double getMonthlyPersonalExpenses() {
		return monthlyPersonalExpenses;
	}

	/**
	 * @param monthlyPersonalExpenses the monthlyPersonalExpenses to set
	 */
	public void setMonthlyPersonalExpenses(Double monthlyPersonalExpenses) {
		this.monthlyPersonalExpenses = monthlyPersonalExpenses;
	}

	/**
	 * @return the monthlyFamilyExpenses
	 */
	@Column(name = "ind_am_monthly_family_expenses", nullable = true)
	public Double getMonthlyFamilyExpenses() {
		return monthlyFamilyExpenses;
	}

	/**
	 * @param monthlyFamilyExpenses the monthlyFamilyExpenses to set
	 */
	public void setMonthlyFamilyExpenses(Double monthlyFamilyExpenses) {
		this.monthlyFamilyExpenses = monthlyFamilyExpenses;
	}

	/**
	 * @return the debtFromOtherSource
	 */
	@Column(name = "ind_bl_debt_from_other_source", nullable = true)
	public boolean isDebtFromOtherSource() {
		return debtFromOtherSource;
	}

	/**
	 * @param debtFromOtherSource the debtFromOtherSource to set
	 */
	public void setDebtFromOtherSource(boolean debtFromOtherSource) {
		this.debtFromOtherSource = debtFromOtherSource;
	}

	/**
	 * @return the totalDebtInstallment
	 */
	@Column(name = "ind_am_total_debt_installment", nullable = true)
	public Double getTotalDebtInstallment() {
		return totalDebtInstallment;
	}

	/**
	 * @param totalDebtInstallment the totalDebtInstallment to set
	 */
	public void setTotalDebtInstallment(Double totalDebtInstallment) {
		this.totalDebtInstallment = totalDebtInstallment;
	}
	
	
	/**
	 * @return the guarantorOtherLoan
	 */
	@Column(name = "ind_bl_guarantor_other_loan", nullable = true)
	public boolean isGuarantorOtherLoan() {
		return guarantorOtherLoan;
	}

	/**
	 * @param guarantorOtherLoan the guarantorOtherLoan to set
	 */
	public void setGuarantorOtherLoan(boolean guarantorOtherLoan) {
		this.guarantorOtherLoan = guarantorOtherLoan;
	}

	/**
	 * @return the convenientVisitTime
	 */
	@Column(name = "ind_va_convenient_visit_time", nullable = true, length = 50)
	public String getConvenientVisitTime() {
		return convenientVisitTime;
	}

	/**
	 * @param convenientVisitTime the convenientVisitTime to set
	 */
	public void setConvenientVisitTime(String convenientVisitTime) {
		this.convenientVisitTime = convenientVisitTime;
	}	
	
	/**
	 * @return the grade
	 */
	@Column(name = "ind_va_grade", nullable = true, length = 2)
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the householdExpenses
	 */
	@Column(name = "ind_am_household_expenses", nullable = true)
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
	@Column(name = "ind_am_household_income", nullable = true)
	public Double getHouseholdIncome() {
		return householdIncome;
	}

	/**
	 * @param householdIncome the householdIncome to set
	 */
	public void setHouseholdIncome(Double householdIncome) {
		this.householdIncome = householdIncome;
	}

	/**
	 * @return the quotationApplicants
	 */
	@OneToMany(mappedBy="applicant", fetch = FetchType.LAZY)
	public List<QuotationApplicant> getQuotationApplicants() {
		return quotationApplicants;
	}

	/**
	 * @param quotationApplicants the quotationApplicants to set
	 */
	public void setQuotationApplicants(List<QuotationApplicant> quotationApplicants) {
		this.quotationApplicants = quotationApplicants;
	}
	
	/**
	 * @return the timestamp
	 */
	@Column(name = "timestamp")
	public Integer getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return totalFamilyMember
	 */
	@Column(name = "ind_nu_number_family_member", nullable = true)
	public Integer getTotalFamilyMember() {
		return totalFamilyMember;
	}
	
	/**
	 * @param totalFamilyMember
	 */
	public void setTotalFamilyMember(Integer totalFamilyMember) {
		this.totalFamilyMember = totalFamilyMember;
	}

	/**
	 * @return the otherNationality
	 */
	@Column(name = "ind_other_nationality", nullable = true, length = 30)
	public String getOtherNationality() {
		return otherNationality;
	}

	/**
	 * @param otherNationality the otherNationality to set
	 */
	public void setOtherNationality(String otherNationality) {
		this.otherNationality = otherNationality;
	}		
	
	/**
	 * @return the fixedIncome
	 */
	@Column(name = "ind_am_fixed_income", nullable = true)
	public Double getFixedIncome() {
		return fixedIncome;
	}

	/**
	 * @param fixedIncome the fixedIncome to set
	 */
	public void setFixedIncome(Double fixedIncome) {
		this.fixedIncome = fixedIncome;
	}

	/**
	 * @return the otherIncomes
	 */
	@Column(name = "ind_am_other_incomes", nullable = true)
	public Double getOtherIncomes() {
		return otherIncomes;
	}

	/**
	 * @param otherIncomes the otherIncomes to set
	 */
	public void setOtherIncomes(Double otherIncomes) {
		this.otherIncomes = otherIncomes;
	}

	/**
	 * @return the debtOtherLoans
	 */
	@Column(name = "ind_am_debt_other_loans", nullable = true)
	public Double getDebtOtherLoans() {
		return debtOtherLoans;
	}

	/**
	 * @param debtOtherLoans the debtOtherLoans to set
	 */
	public void setDebtOtherLoans(Double debtOtherLoans) {
		this.debtOtherLoans = debtOtherLoans;
	}

	/**
	 * @return the debtSource
	 */
	@Column(name = "ind_va_debt_sources", nullable = true, length = 255)
	public String getDebtSource() {
		return debtSource;
	}

	/**
	 * @param debtSource the debtSource to set
	 */
	public void setDebtSource(String debtSource) {
		this.debtSource = debtSource;
	}	

	/**
	 * @return the staffFinancialCompany
	 */
	@Column(name = "ind_bl_staff_fin_company", nullable = true, columnDefinition = "boolean default false")
	public boolean isStaffFinancialCompany() {
		return staffFinancialCompany;
	}

	/**
	 * @param staffFinancialCompany the staffFinancialCompany to set
	 */
	public void setStaffFinancialCompany(boolean staffFinancialCompany) {
		this.staffFinancialCompany = staffFinancialCompany;
	}
		

	/**
	 * @return the monthlyGrossIncome
	 */
	@Column(name = "ind_am_monthly_gross_income", nullable = true)
	public Double getMonthlyGrossIncome() {
		return monthlyGrossIncome;
	}

	/**
	 * @param monthlyGrossIncome the monthlyGrossIncome to set
	 */
	public void setMonthlyGrossIncome(Double monthlyGrossIncome) {
		this.monthlyGrossIncome = monthlyGrossIncome;
	}

	/**
	 * @return the monthlyExpenses
	 */
	@Column(name = "ind_am_monthly_expenses", nullable = true)
	public Double getMonthlyExpenses() {
		return monthlyExpenses;
	}

	/**
	 * @param monthlyExpenses the monthlyExpenses to set
	 */
	public void setMonthlyExpenses(Double monthlyExpenses) {
		this.monthlyExpenses = monthlyExpenses;
	}

	/**
	 * Disable the Workflow
	 * @return
	 */
	@Override
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}

	/**
	 * @return the middleNameEn
	 */
	@Column(name = "ind_middlename_en", nullable = true, length = 100)
	public String getMiddleNameEn() {
		return middleNameEn;
	}

	/**
	 * @param middleNameEn the middleNameEn to set
	 */
	public void setMiddleNameEn(String middleNameEn) {
		this.middleNameEn = middleNameEn;
	}

	
}
