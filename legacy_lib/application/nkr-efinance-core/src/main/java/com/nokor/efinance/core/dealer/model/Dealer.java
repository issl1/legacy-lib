package com.nokor.efinance.core.dealer.model;

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

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.config.model.ELocale;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_dealer")
public class Dealer extends BaseOrganization {
	
	private static final long serialVersionUID = -1244256876127622717L;
	
	private Dealer parent;
	
	private EDealerType dealerType;
	private LadderType ladderType;
	private EDealerCategory dealerCategory;
	private EDealerCustomer dealerCustomer;
		
	private List<DealerContactInfo> dealerContactInfos;
	private List<DealerAddress> dealerAddresses;
	private List<DealerBankAccount> dealerBankAccounts;	
	private List<DealerAccountHolder> dealerAccountHolders;
	
	private List<DealerAttribute> dealerAttributes;	
	private List<DealerPaymentMethod> dealerPaymentMethods;
	
	private List<DealerEmployee> dealerEmployees;
	private DealerGroup dealerGroup;
	private OrgStructure financialCompanyBranch;
	
	private Integer monthlyTargetSales;
	private Double registrationCost;
	private String branchNo;
			
	private boolean deaBlIncludeDailyReport;

	/**
     * 
     * @return
     */
    public static Dealer createInstance() {
    	Dealer dea = EntityFactory.createInstance(Dealer.class);
        return dea;
    }
    
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}		
	
	/**
	 * @return the parent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id_parent", nullable = true)
	public Dealer getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Dealer parent) {
		this.parent = parent;
	}
		
	/**
	 * @return the dealerType
	 */
    @Column(name = "dea_typ_id", nullable = false)
    @Convert(converter = EDealerType.class)
	public EDealerType getDealerType() {
		return dealerType;
	}

	/**
	 * @param dealerType the dealerType to set
	 */
	public void setDealerType(EDealerType dealerType) {
		this.dealerType = dealerType;
	}

	/**
	 * @return the ladderType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lad_typ_id", nullable = true)
	public LadderType getLadderType() {
		return ladderType;
	}

	/**
	 * @param ladderType the ladderType to set
	 */
	public void setLadderType(LadderType ladderType) {
		this.ladderType = ladderType;
	} 

//    /**
//	 * @return the externalCode
//	 */
//	@ov
//	@Column(name = "dea_external_code", nullable = true, length = 50)
//	public String getExternalCode() {
//		return externalCode;
//	}
//
//	/**
//	 * @param externalCode the externalCode to set
//	 */
//	public void setExternalCode(String externalCode) {
//		this.externalCode = externalCode;
//	}
//
//	/**
//	 * @return the vatRegistrationNo
//	 */
//	@Column(name = "dea_vat_number", nullable = true, length = 20)
//	public String getVatRegistrationNo() {
//		return vatRegistrationNo;
//	}
//
//	/**
//	 * @param vatRegistrationNo the vatRegistrationNo to set
//	 */
//	public void setVatRegistrationNo(String vatRegistrationNo) {
//		this.vatRegistrationNo = vatRegistrationNo;
//	}


	/**
	 * @return the dealerContactInfos
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerContactInfo> getDealerContactInfos() {
		return dealerContactInfos;
	}

	/**
	 * @param dealerContactInfos the dealerContactInfos to set
	 */
	public void setDealerContactInfos(List<DealerContactInfo> dealerContactInfos) {
		this.dealerContactInfos = dealerContactInfos;
	}

	/**
	 * @return the dealerAddresses
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerAddress> getDealerAddresses() {
		if (dealerAddresses == null) {
			dealerAddresses = new ArrayList<DealerAddress>();
		}
		return dealerAddresses;
	}

	/**
	 * @return the dealerAccountHolders
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerAccountHolder> getDealerAccountHolders() {
		return dealerAccountHolders;
	}

	/**
	 * @param dealerAccountHolders the dealerAccountHolders to set
	 */
	public void setDealerAccountHolders(
			List<DealerAccountHolder> dealerAccountHolders) {
		this.dealerAccountHolders = dealerAccountHolders;
	}

	/**
	 * @param dealerAddresses the dealerAddresses to set
	 */
	public void setDealerAddresses(List<DealerAddress> dealerAddresses) {
		this.dealerAddresses = dealerAddresses;
	}
	
	/**
	 * @return the dealerBankAccounts
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerBankAccount> getDealerBankAccounts() {
		if (dealerBankAccounts == null) {
			dealerBankAccounts = new ArrayList<DealerBankAccount>();
		}
		return dealerBankAccounts;
	}

	/**
	 * @param dealerBankAccounts the dealerBankAccounts to set
	 */
	public void setDealerBankAccounts(List<DealerBankAccount> dealerBankAccounts) {
		this.dealerBankAccounts = dealerBankAccounts;
	}	

	/**
	 * 
	 * @return
	 */
	@Transient
	public DealerBankAccount getCurrentBankAccount() {
		List<DealerBankAccount> lstAcc = getDealerBankAccounts();
		for (DealerBankAccount acc : lstAcc) {
			if (acc.isActive()) {
				return acc;
			}
		}
		
		return null;
	}	
	
	/**
	 * @return the dealerAttributes
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerAttribute> getDealerAttributes() {
		return dealerAttributes;
	}

	/**
	 * @param dealerAttributes the dealerAttributes to set
	 */
	public void setDealerAttributes(List<DealerAttribute> dealerAttributes) {
		this.dealerAttributes = dealerAttributes;
	}

	/**
	 * @return the dealerPaymentMethods
	 */
	@OneToMany(mappedBy="dealer", fetch = FetchType.LAZY)
	public List<DealerPaymentMethod> getDealerPaymentMethods() {
		return dealerPaymentMethods;
	}

	/**
	 * @param dealerPaymentMethods the dealerPaymentMethods to set
	 */
	public void setDealerPaymentMethods(List<DealerPaymentMethod> dealerPaymentMethods) {
		this.dealerPaymentMethods = dealerPaymentMethods;
	}

	/**
	 * @return the dealerEmployees
	 */
	@OneToMany(mappedBy="company", fetch = FetchType.LAZY)
	public List<DealerEmployee> getDealerEmployees() {
		return dealerEmployees;
	}

	/**
	 * @param dealerEmployees the dealerEmployees to set
	 */
	public void setDealerEmployees(List<DealerEmployee> dealerEmployees) {
		this.dealerEmployees = dealerEmployees;
	}

	/**
	 * Add a new address
	 * @param address
	 * @param addressType
	 */
	@Transient
	public void addAddress(Address address, ETypeAddress addressType) {
		if (dealerAddresses == null) {
			dealerAddresses = new ArrayList<DealerAddress>();
		}
		DealerAddress dealerAddress = new DealerAddress();
		dealerAddress.setDealer(this);
		address.setType(addressType);
		dealerAddress.setAddress(address);
		dealerAddresses.add(dealerAddress);
	}
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public Address getMainAddress() {
		if (dealerAddresses != null && !dealerAddresses.isEmpty()) {
			for (DealerAddress dealerAddress : dealerAddresses) {
				if (dealerAddress.getAddress().getType().equals(ETypeAddress.MAIN)) {
					return dealerAddress.getAddress();
				}
			}
		}
		return null;
	}
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public DealerAddress getDealerAddress(ETypeAddress type) {
		if (dealerAddresses != null && !dealerAddresses.isEmpty()) {
			for (DealerAddress dealerAddress : dealerAddresses) {
				if (type.equals(dealerAddress.getAddress().getType())) {
					return dealerAddress;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public DealerEmployee getDealerEmployee(ETypeContact type) {
		if (dealerEmployees != null && !dealerEmployees.isEmpty()) {
			for (DealerEmployee dealerEmployee : dealerEmployees) {
				if (type.equals(dealerEmployee.getTypeContact())) {
					return dealerEmployee;
				}
			}
		}
		return null;
	}
	
	@Transient
	/**
	 * @param type
	 * @return
	 */
	public DealerPaymentMethod getDealerPaymentMethod(EPaymentFlowType type) {
		if (dealerPaymentMethods != null && !dealerPaymentMethods.isEmpty()) {
			for (DealerPaymentMethod dealerPaymentMethod : dealerPaymentMethods) {
				if (type.equals(dealerPaymentMethod.getType())) {
					return dealerPaymentMethod;
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		if (I18N.getLocale().getLanguage().equals(ELocale.ENG.getLanguageTag())) {
			return getNameEn();
		}
		return getName();
	}

	/**
	 * @return the dealerGroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_grp_id", nullable = true)
	public DealerGroup getDealerGroup() {
		return dealerGroup;
	}

	/**
	 * @param dealerGroup the dealerGroup to set
	 */
	public void setDealerGroup(DealerGroup dealerGroup) {
		this.dealerGroup = dealerGroup;
	}

	/**
	 * @return the financialCompanyBranch
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id", nullable = true)
	public OrgStructure getFinancialCompanyBranch() {
		return financialCompanyBranch;
	}

	/**
	 * @param financialCompanyBranch the financialCompanyBranch to set
	 */
	public void setFinancialCompanyBranch(OrgStructure financialCompanyBranch) {
		this.financialCompanyBranch = financialCompanyBranch;
	}

	/**
	 * @return the monthlyTargetSales
	 */
	@Column(name = "dea_monthy_target_sales", nullable = true)
	public Integer getMonthlyTargetSales() {
		return monthlyTargetSales;
	}

	/**
	 * @param monthlyTargetSales the monthlyTargetSales to set
	 */
	public void setMonthlyTargetSales(Integer monthlyTargetSales) {
		this.monthlyTargetSales = monthlyTargetSales;
	}
	
	/**
	 * @return the registrationCost
	 */
	@Column(name = "dea_registration_cost", nullable = true)
	public Double getRegistrationCost() {
		return registrationCost;
	}

	/**
	 * @param registrationCost the registrationCost to set
	 */
	public void setRegistrationCost(Double registrationCost) {
		this.registrationCost = registrationCost;
	}

	/**
	 * @return the dealerCategory
	 */
	@Column(name = "dea_cat_id", nullable = true)
    @Convert(converter = EDealerCategory.class)
	public EDealerCategory getDealerCategory() {
		return dealerCategory;
	}

	/**
	 * @param dealerCategory the dealerCategory to set
	 */
	public void setDealerCategory(EDealerCategory dealerCategory) {
		this.dealerCategory = dealerCategory;
	}

	/**
	 * @return the dealerCustomer
	 */
	@Column(name = "dea_cust", nullable = true)
    @Convert(converter = EDealerCustomer.class)
	public EDealerCustomer getDealerCustomer() {
		return dealerCustomer;
	}

	/**
	 * @param dealerCustomer the dealerCustomer to set
	 */
	public void setDealerCustomer(EDealerCustomer dealerCustomer) {
		this.dealerCustomer = dealerCustomer;
	}

	/**
	 * @return the deaBlIncludeDailyReport
	 */
	@Column(name = "dea_bl_include_daily_report", nullable = true, columnDefinition = "boolean default true")
	public boolean isDeaBlIncludeDailyReport() {
		return deaBlIncludeDailyReport;
	}

	/**
	 * @param deaBlIncludeDailyReport the deaBlIncludeDailyReport to set
	 */
	public void setDeaBlIncludeDailyReport(boolean deaBlIncludeDailyReport) {
		this.deaBlIncludeDailyReport = deaBlIncludeDailyReport;
	}

	/**
	 * @return the branchNo
	 */
	@Column(name = "dea_va_branch_no", nullable = true, length = 25)
	public String getBranchNo() {
		return branchNo;
	}

	/**
	 * @param branchNo the branchNo to set
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	
}
