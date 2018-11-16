package com.nokor.efinance.core.applicant.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_applicant_arc")
public class ApplicantArc extends EntityA {
	
	/**
	 */
	private static final long serialVersionUID = 5588684312778380400L;
	
	private EApplicantCategory applicantCategory;
	
	private IndividualArc individual;
	private Organization company;
	
	private List<Quotation> quotations;
	private List<Contract> contracts;
		
	/** 
     * @return
     */
    public static ApplicantArc createInstance() {
        ApplicantArc applicant = EntityFactory.createInstance(ApplicantArc.class);
        return applicant;
    }
    
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	
	
	/**
	 * @return the applicantCategory
	 */
	@Column(name = "app_cat_id", nullable = true)
    @Convert(converter = EApplicantCategory.class)
	public EApplicantCategory getApplicantCategory() {
		return applicantCategory;
	}

	/**
	 * @param applicantCategory the applicantCategory to set
	 */
	public void setApplicantCategory(EApplicantCategory applicantCategory) {
		this.applicantCategory = applicantCategory;
	}	

	/**
	 * @return the individual
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ind_id", nullable = true)
	public IndividualArc getIndividual() {
		return individual;
	}


	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(IndividualArc individual) {
		this.individual = individual;
	}


	/**
	 * @return the company
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_id", nullable = true)
	public Organization getCompany() {
		return company;
	}


	/**
	 * @param company the company to set
	 */
	public void setCompany(Organization company) {
		this.company = company;
	}


	/**
	 * @return the quotations
	 */
	@OneToMany(mappedBy="applicant", fetch = FetchType.LAZY)
	public List<Quotation> getQuotations() {
		if (quotations == null) {
			quotations = new ArrayList<>();
		}
		return quotations;
	}


	/**
	 * @param quotations the quotations to set
	 */
	public void setQuotations(List<Quotation> quotations) {
		this.quotations = quotations;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Quotation getQuotation() {
		return quotations.get(0);
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void setQuotation(Quotation quotation) {
		this.quotations.add(quotation);
	}
	
	/**
	 * @return the contracts
	 */
	@OneToMany(mappedBy="applicant", fetch = FetchType.LAZY)
	public List<Contract> getContracts() {
		if (contracts == null) {
			contracts = new ArrayList<>();
		}
		return contracts;
	}


	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Contract getContract() {
		return contracts.get(0);
	}

	/**
	 * 
	 * @param contract
	 */
	public void setContract(Contract contract) {
		this.contracts.add(contract);
	}
		
}
