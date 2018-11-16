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

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.address.Address;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_applicant")
public class Applicant extends EntityA {
	
	/**
	 */
	private static final long serialVersionUID = 5588684312778380400L;
	
	private EApplicantCategory applicantCategory;
	
	private Individual individual;
	private Company company;
	
	private List<Quotation> quotations;
	private List<Contract> contracts;
		
	/** 
     * @return
     */
    public static Applicant createInstance(EApplicantCategory applicantCategory) {
        Applicant applicant = EntityFactory.createInstance(Applicant.class);
        applicant.setApplicantCategory(applicantCategory);
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
	public Individual getIndividual() {
		return individual;
	}


	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}


	/**
	 * @return the company
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_id", nullable = true)
	public Company getCompany() {
		return company;
	}


	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
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
	
	@Transient
	public String getName() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getFirstName() + " " + individual.getLastName();
		} else {
			return company.getName();
		}
	}
	
	@Transient
	public String getNameEn() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getFirstNameEn() + " " + individual.getLastNameEn();
		} else {
			return company.getNameEn();
		}
	}
	
	@Transient
	public String getLastNameEn() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getLastNameEn();
		} else {
			return company.getNameEn();
		}
	}
	
	@Transient
	public String getLastName() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getLastName();
		} else {
			return company.getName();
		}
	}
	
	@Transient
	public String getFirstNameEn() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getFirstNameEn();
		} else {
			return company.getNameEn();
		}
	}
	
	@Transient
	public String getFirstName() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getFirstName();
		} else {
			return company.getName();
		}
	}
	
	@Transient
	public List<Address> getAddresses() {
		List<Address> addresses = new ArrayList<>();
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
			if (individualAddresses != null && !individualAddresses.isEmpty()) {
				for (IndividualAddress individualAddress : individualAddresses) {
					addresses.add(individualAddress.getAddress());
				}
			}
		}
		return addresses;
	}
	
	/**
     * 
     * @return
     */
	@Transient
    public String getLastNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getLastNameEn();
    	} else {
    		return getLastName();
    	}
    }
	
	/**
     * 
     * @return
     */
	@Transient
    public String getFirstNameLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getFirstNameEn();
    	} else {
    		return getFirstName();
    	}
    }
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getNameLocale() {
		return getFirstNameLocale() + StringUtils.SPACE + getLastNameLocale();
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getApplicantId() {
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
			return individual.getReference();
		} else if (EApplicantCategory.COMPANY.equals(applicantCategory)) {
			return company.getLicenceNo();
		}
		return StringUtils.EMPTY;
	}
}
