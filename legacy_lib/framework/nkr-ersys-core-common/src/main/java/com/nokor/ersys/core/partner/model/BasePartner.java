package com.nokor.ersys.core.partner.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.nokor.ersys.core.hr.model.organization.BaseOrganization;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BasePartner extends BaseOrganization {
	/** */
	private static final long serialVersionUID = -8429466506910965847L;

	private EPartnerType type;				// partnership, ..
	private EPartnerCategory category;		// Company, Association, Individual..
	
	private Boolean isCustomer;
	private Boolean isSupplier;

	private PartnerEmployee mainContact;
	private PartnerEmployee otherContact;
	
	private List<PartnerContactInfo> contactInfos;
	private List<PartnerAddress> addresses;
	private List<PartnerBankAccount> bankAccounts;
	
	private List<PartnerEmployee> employees;
	

	/**
	 * @return the type
	 */
    @Column(name = "par_typ_id", nullable = false)
    @Convert(converter = EPartnerType.class)
	public EPartnerType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EPartnerType type) {
		this.type = type;
	}

	/**
	 * @return the category
	 */
    @Column(name = "par_cat_id", nullable = true)
    @Convert(converter = EPartnerCategory.class)
	public EPartnerCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EPartnerCategory category) {
		this.category = category;
	}


	/**
	 * @return the isCustomer
	 */
    @Column(name = "par_is_customer", nullable = true)
	public Boolean getIsCustomer() {
		return isCustomer;
	}

	/**
	 * @param isCustomer the isCustomer to set
	 */
	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	/**
	 * @return the isSupplier
	 */
    @Column(name = "par_is_supplier", nullable = true)
	public Boolean getIsSupplier() {
		return isSupplier;
	}

	/**
	 * @param isSupplier the isSupplier to set
	 */
	public void setIsSupplier(Boolean isSupplier) {
		this.isSupplier = isSupplier;
	}

	/**
	 * @return the mainContact
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_contact_par_id", nullable = true)
	public PartnerEmployee getMainContact() {
		return mainContact;
	}

	/**
	 * @param mainContact the mainContact to set
	 */
	public void setMainContact(PartnerEmployee mainContact) {
		this.mainContact = mainContact;
	}

	/**
	 * @return the otherContact
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_contact_par_id", nullable = true)
	public PartnerEmployee getOtherContact() {
		return otherContact;
	}

	/**
	 * @param otherContact the otherContact to set
	 */
	public void setOtherContact(PartnerEmployee otherContact) {
		this.otherContact = otherContact;
	}

	/**
	 * @return the contactInfos
	 */
	@OneToMany(mappedBy="partner", fetch = FetchType.LAZY)
	public List<PartnerContactInfo> getContactInfos() {
		return contactInfos;
	}

	/**
	 * @param contactInfos the contactInfos to set
	 */
	public void setContactInfos(List<PartnerContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}

	/**
	 * @return the addresses
	 */
	@OneToMany(mappedBy="partner", fetch = FetchType.LAZY)
	public List<PartnerAddress> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<PartnerAddress> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the bankAccounts
	 */
	@OneToMany(mappedBy="partner", fetch = FetchType.LAZY)
	public List<PartnerBankAccount> getBankAccounts() {
		return bankAccounts;
	}

	/**
	 * @param bankAccounts the bankAccounts to set
	 */
	public void setBankAccounts(List<PartnerBankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	/**
	 * @return the employees
	 */
	@OneToMany(mappedBy="partner", fetch = FetchType.LAZY)
	public List<PartnerEmployee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(List<PartnerEmployee> employees) {
		this.employees = employees;
	}
	
}
