package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.ersys.core.hr.model.organization.MOrganization;

/**
 * @author prasnar
 *
 */
@Entity
@Table(name="td_company")
public class Company extends BaseOrganization implements MOrganization {
	/** */
	private static final long serialVersionUID = 4933631321817988036L;

	private List<CompanyAddress> companyAddresses;
	private List<CompanyEmployee> companyEmployees;
	private List<CompanyContactInfo> companyContactInfos;
	
	/**
     * 
     * @return
     */
    public static Company createInstance() {
        Company com = BaseOrganization.createInstance(Company.class);
        return com;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the companyAddresses
	 */
    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
	public List<CompanyAddress> getCompanyAddresses() {
		return companyAddresses;
	}

	/**
	 * @param companyAddresses the companyAddresses to set
	 */
	public void setCompanyAddresses(List<CompanyAddress> companyAddresses) {
		this.companyAddresses = companyAddresses;
	}

	/**
	 * @return the companyEmployees
	 */
	@OneToMany(mappedBy="company", fetch = FetchType.LAZY)
	public List<CompanyEmployee> getCompanyEmployees() {
		return companyEmployees;
	}

	/**
	 * @param companyEmployees the companyEmployees to set
	 */
	public void setCompanyEmployees(List<CompanyEmployee> companyEmployees) {
		this.companyEmployees = companyEmployees;
	}    	

	/**
	 * @return the companyContactInfos
	 */
	@OneToMany(mappedBy="company", fetch = FetchType.LAZY)
	public List<CompanyContactInfo> getCompanyContactInfos() {
		return companyContactInfos;
	}


	/**
	 * @param companyContactInfo the companyContactInfos to set
	 */
	public void setCompanyContactInfos(
			List<CompanyContactInfo> companyContactInfos) {
		this.companyContactInfos = companyContactInfos;
	}
}