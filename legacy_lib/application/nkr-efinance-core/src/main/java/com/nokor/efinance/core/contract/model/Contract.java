package com.nokor.efinance.core.contract.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantArc;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetArc;
import com.nokor.efinance.core.auction.model.Auction;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.issue.model.ContractIssue;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_contract", indexes = {
		@Index(name = "idx_con_va_reference", columnList = "con_va_reference"),
		@Index(name = "idx_con_va_external_reference", columnList = "con_va_external_reference"),
		@Index(name = "idx_con_fpd_id", columnList = "fpd_id"),
		@Index(name = "idx_con_cam_id", columnList = "cam_id"),
		@Index(name = "idx_con_dea_id", columnList = "dea_id"),
		@Index(name = "idx_con_dt_start", columnList = "con_dt_start"),
		@Index(name = "idx_con_pen_rul_id", columnList = "pen_rul_id")
})
public class Contract extends EntityWkf implements MContract {

	private static final long serialVersionUID = -1893813037868210744L;
	
	private List<Auction> auctions;
	private List<Quotation> quotations;

	private EProductLineType productLineType;
	private ProductLine productLine;
	private FinProduct financialProduct;
	private Campaign campaign;

	private String reference;
	private String externalReference;
	
	private ApplicantArc applicantArc;
	private Applicant applicant;

	private Dealer dealer;

	private Asset asset;
	private AssetArc assetArc;

	private Date quotationDate;
	private Date approvalDate;	
	
	private Date creationDate;
	private Date sigatureDate;
	private Date sendDocumentDate;
	private Date startDate;
	private Date initialStartDate;
	private Date endDate;
	private Date initialEndDate;
	private Date firstDueDate;
	private Date lastDueDate;
	private Date bookingDate;
	
	private double vatValue;
	
	private Double tiAdvancePaymentAmount;
	private Double teAdvancePaymentAmount;
	private Double vatAdvancePaymentAmount;
	private Double advancePaymentPercentage;
	
	private Double tiFinancedAmount;
	private Double teFinancedAmount;
	private Double vatFinancedAmount;
				
	private PenaltyRule penaltyRule;
	private Organization insuranceCompany;
		
	private Integer term;
	private Double interestRate;
	private Double irrRate;
	private EFrequency frequency;
	private Integer numberOfPrincipalGracePeriods;
	
	private Double tiInstallmentAmount;
	private Double teInstallmentAmount;
	private Double vatInstallmentAmount;
	
	private boolean forceActivated;
	private boolean transfered;
	private boolean overdue;
	private Integer nbPrints;
			
	private Double tiPrepaidInstallment;
	private Integer numberPrepaidTerm;
	private Integer numberGuarantors;
	
	private String checkerID;
	private String checkerName;
	private String checkerPhoneNumber;
	
	private List<ContractFinService> contractFinServices;
	private List<Cashflow> cashflows;
	private List<ContractApplicant> contractApplicants;
	private List<Collection> collections;
	private List<ContractDocument> contractDocuments;
	private List<ContractIssue> contractIssues;
	private List<ContractNote> contractNotes;
	private List<ContractRequest> contractRequests;
	private List<ContractSms> contractSmss;
	private List<ContractSimulation> contractSimulations;
	private List<ContractRedemption> contractRedemptions;
	
	private Vat vat;
	private OrgStructure originBranch;
	private OrgStructure branchInCharge;
	private String legalGroupCode;
	private SaleLostCutLost saleLostCutLost; 
	
	private Double tiInvoiceAmount;
	private Double teInvoiceAmount;
	private Double vatInvoiceAmount;
	private Date taxInvoiceDate;
	
	private Date foreclosureDate;
	
	/**
     * 
     * @return
     */
    public static Contract createInstance() {
    	Contract con = EntityFactory.createInstance(Contract.class);
        return con;
    }
	
    /**
     * 
     * @return
     */
    public static Contract createInstance(Long id) {
    	Contract con = EntityFactory.createInstance(Contract.class);
    	con.setId(id);
        return con;
    }
    
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
	 * @return the reference
	 */
    @Column(name = "con_va_reference", unique = true, nullable = true, length = 20)
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
	 * @return the externalReference
	 */
	@Column(name = "con_va_external_reference", unique = true, nullable = true, length = 20)
	public String getExternalReference() {
		return externalReference;
	}

	/**
	 * @param externalReference the externalReference to set
	 */
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	
	/**
	 * @return the applicant
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the applicantArc
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id_arc")
	public ApplicantArc getApplicantArc() {
		return applicantArc;
	}

	/**
	 * @param applicantArc the applicantArc to set
	 */
	public void setApplicantArc(ApplicantArc applicantArc) {
		this.applicantArc = applicantArc;
	}
	
	/**
	 * @return the quotations
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
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
		if (quotations != null && !quotations.isEmpty()) {
			return quotations.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void setQuotation(Quotation quotation) {
		this.quotations.add(quotation);
	}
	
	/**
	 * @return the auctions
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<Auction> getAuctions() {
		if (auctions == null) {
			auctions = new ArrayList<>();
		}
		return auctions;
	}

	/**
	 * @param auctions the auctions to set
	 */
	public void setAuctions(List<Auction> auctions) {
		this.auctions = auctions;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Auction getAuction() {
		return auctions.get(0);
	}
	
	/**
	 * 
	 * @param auction
	 */
	public void setAuction(Auction auction) {
		this.auctions.add(auction);
	}

	/**
	 * @return the productLineType
	 */
    @Column(name = "pro_lin_typ_id", nullable = true)
	@Convert(converter = EProductLineType.class)
	public EProductLineType getProductLineType() {
		return productLineType;
	}

	/**
	 * @param productLineType the productLineType to set
	 */
	public void setProductLineType(EProductLineType productLineType) {
		this.productLineType = productLineType;
	}
	
	/**
	 * @return the productLine
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_lin_id", nullable = true)
	public ProductLine getProductLine() {
		return productLine;
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
	}
	
	/**
	 * @return the financialProduct
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fpd_id", nullable = true)
	public FinProduct getFinancialProduct() {
		return financialProduct;
	}

	/**
	 * @param financialProduct the financialProduct to set
	 */
	public void setFinancialProduct(FinProduct financialProduct) {
		this.financialProduct = financialProduct;
	}	

	/**
	 * @return the campaign
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cam_id")
	public Campaign getCampaign() {
		return campaign;
	}

	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	
	/**
	 * @return the asset
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_id")
	public Asset getAsset() {
		return asset;
	}

	/**
	 * @param asset the asset to set
	 */
	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	/**
	 * @return the assetArc
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_id_arc")
	public AssetArc getAssetArc() {
		return assetArc;
	}

	/**
	 * @param assetArc the assetArc to set
	 */
	public void setAssetArc(AssetArc assetArc) {
		this.assetArc = assetArc;
	}	
	
	/**
	 * @return the quotationDate
	 */
	@Column(name = "con_dt_quotation", nullable = true)
	public Date getQuotationDate() {
		return quotationDate;
	}

	/**
	 * @param quotationDate the quotationDate to set
	 */
	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	/**
	 * @return the approvalDate
	 */
	@Column(name = "con_dt_approval", nullable = true)
	public Date getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}	

	/**
	 * @return the creationDate
	 */
	@Column(name = "con_dt_creation", nullable = true)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the sigatureDate
	 */
	@Column(name = "con_dt_signature", nullable = true)
	public Date getSigatureDate() {
		return sigatureDate;
	}

	/**
	 * @param sigatureDate the sigatureDate to set
	 */
	public void setSigatureDate(Date sigatureDate) {
		this.sigatureDate = sigatureDate;
	}

	/**
	 * @return the sendDocumentDate
	 */
	public Date getSendDocumentDate() {
		return sendDocumentDate;
	}

	/**
	 * @param sendDocumentDate the sendDocumentDate to set
	 */
	public void setSendDocumentDate(Date sendDocumentDate) {
		this.sendDocumentDate = sendDocumentDate;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "con_dt_start", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the initialStartDate
	 */
	@Column(name = "con_dt_initial_start", nullable = true)
	public Date getInitialStartDate() {
		return initialStartDate;
	}

	/**
	 * @param initialStartDate the initialStartDate to set
	 */
	public void setInitialStartDate(Date initialStartDate) {
		this.initialStartDate = initialStartDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "con_dt_end", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the initialEndDate
	 */
	@Column(name = "con_dt_initial_end", nullable = true)
	public Date getInitialEndDate() {
		return initialEndDate;
	}

	/**
	 * @param initialEndDate the initialEndDate to set
	 */
	public void setInitialEndDate(Date initialEndDate) {
		this.initialEndDate = initialEndDate;
	}
			
	/**
	 * @return the firstDueDate
	 */
	@Column(name = "con_dt_first_due", nullable = true)
	public Date getFirstDueDate() {
		return firstDueDate;
	}

	/**
	 * @param firstDueDate the firstDueDate to set
	 */
	public void setFirstDueDate(Date firstDueDate) {
		this.firstDueDate = firstDueDate;
	}

	/**
	 * @return the lastDueDate
	 */
	@Column(name = "con_dt_last_due", nullable = true)
	public Date getLastDueDate() {
		return lastDueDate;
	}

	/**
	 * @param lastDueDate the lastDueDate to set
	 */
	public void setLastDueDate(Date lastDueDate) {
		this.lastDueDate = lastDueDate;
	}

	/**
	 * @return the bookingDate
	 */
	@Column(name = "con_dt_booking", nullable = true)
	public Date getBookingDate() {
		return bookingDate;
	}

	/**
	 * @param bookingDate the bookingDate to set
	 */
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	/**
	 * @return the tiAdvancePaymentAmount
	 */
	@Column(name = "con_am_ti_advance_payment", nullable = true)
	public Double getTiAdvancePaymentAmount() {
		return tiAdvancePaymentAmount;
	}

	/**
	 * @param tiAdvancePaymentAmount the tiAdvancePaymentAmount to set
	 */
	public void setTiAdvancePaymentAmount(Double tiAdvancePaymentAmount) {
		this.tiAdvancePaymentAmount = tiAdvancePaymentAmount;
	}

	/**
	 * @return the teAdvancePaymentAmount
	 */
	@Column(name = "con_am_te_advance_payment", nullable = true)
	public Double getTeAdvancePaymentAmount() {
		return teAdvancePaymentAmount;
	}

	/**
	 * @param teAdvancePaymentAmount the teAdvancePaymentAmount to set
	 */
	public void setTeAdvancePaymentAmount(Double teAdvancePaymentAmount) {
		this.teAdvancePaymentAmount = teAdvancePaymentAmount;
	}

	/**
	 * @return the vatAdvancePaymentAmount
	 */
	@Column(name = "con_am_vat_advance_payment", nullable = true)
	public Double getVatAdvancePaymentAmount() {
		return vatAdvancePaymentAmount;
	}

	/**
	 * @param vatAdvancePaymentAmount the vatAdvancePaymentAmount to set
	 */
	public void setVatAdvancePaymentAmount(Double vatAdvancePaymentAmount) {
		this.vatAdvancePaymentAmount = vatAdvancePaymentAmount;
	}

	/**
	 * @return the advancePaymentPercentage
	 */
	@Column(name = "con_rt_advance_payment_pc", nullable = true)
	public Double getAdvancePaymentPercentage() {
		return advancePaymentPercentage;
	}

	/**
	 * @param advancePaymentPercentage the advancePaymentPercentage to set
	 */
	public void setAdvancePaymentPercentage(Double advancePaymentPercentage) {
		this.advancePaymentPercentage = advancePaymentPercentage;
	}
	
	/**
	 * @return the tiFinancedAmount
	 */
	@Column(name = "con_am_ti_financed_amount", nullable = true)
	public Double getTiFinancedAmount() {
		return tiFinancedAmount;
	}

	/**
	 * @param tiFinancedAmount the tiFinancedAmount to set
	 */
	public void setTiFinancedAmount(Double tiFinancedAmount) {
		this.tiFinancedAmount = tiFinancedAmount;
	}

	/**
	 * @return the teFinancedAmount
	 */
	@Column(name = "con_am_te_financed_amount", nullable = true)
	public Double getTeFinancedAmount() {
		return teFinancedAmount;
	}

	/**
	 * @param teFinancedAmount the teFinancedAmount to set
	 */
	public void setTeFinancedAmount(Double teFinancedAmount) {
		this.teFinancedAmount = teFinancedAmount;
	}

	/**
	 * @return the vatFinancedAmount
	 */
	@Column(name = "con_am_vat_financed_amount", nullable = true)
	public Double getVatFinancedAmount() {
		return vatFinancedAmount;
	}

	/**
	 * @param vatFinancedAmount the vatFinancedAmount to set
	 */
	public void setVatFinancedAmount(Double vatFinancedAmount) {
		this.vatFinancedAmount = vatFinancedAmount;
	}	
	
	/**
	 * @return the contractFinServices
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractFinService> getContractFinServices() {
		return contractFinServices;
	}

	/**
	 * @param contractFinServices the contractFinServices to set
	 */
	public void setContractFinServices(List<ContractFinService> contractFinServices) {
		this.contractFinServices = contractFinServices;
	}

	/**
	 * @return the cashflows
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<Cashflow> getCashflows() {
		return cashflows;
	}

	/**
	 * @param cashflows the cashflows to set
	 */
	public void setCashflows(List<Cashflow> cashflows) {
		this.cashflows = cashflows;
	}

	/**
	 * @return the contractApplicants
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractApplicant> getContractApplicants() {
		return contractApplicants;
	}

	/**
	 * @param contractApplicants the contractApplicants to set
	 */
	public void setContractApplicants(List<ContractApplicant> contractApplicants) {
		this.contractApplicants = contractApplicants;
	}
	
	/**
	 * @return the penaltyRule
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pen_rul_id")
	public PenaltyRule getPenaltyRule() {
		return penaltyRule;
	}

	/**
	 * @param penaltyRule the penaltyRule to set
	 */
	public void setPenaltyRule(PenaltyRule penaltyRule) {
		this.penaltyRule = penaltyRule;
	}
		
	/**
	 * @return the insuranceCompany
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ins_com_id", nullable = true)
	public Organization getInsuranceCompany() {
		return insuranceCompany;
	}

	/**
	 * @param insuranceCompany the insuranceCompany to set
	 */
	public void setInsuranceCompany(Organization insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	
	/**
	 * @return the term
	 */
	@Column(name = "con_nu_term", nullable = true)
	public Integer getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}

	/**
	 * @return the interestRate
	 */
	@Column(name = "con_rt_interest_rate", nullable = true)
	public Double getInterestRate() {
		return interestRate;
	}

	/**
	 * @param interestRate the interestRate to set
	 */
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	
	/**
	 * @return the irrRate
	 */
	@Column(name = "con_rt_irr_rate", nullable = true)
	public Double getIrrRate() {
		return irrRate;
	}

	/**
	 * @param irrRate the irrRate to set
	 */
	public void setIrrRate(Double irrRate) {
		this.irrRate = irrRate;
	}

	/**
	 * @return the frequency
	 */
	@Column(name = "fre_id", nullable = true)
    @Convert(converter = EFrequency.class)
	public EFrequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(EFrequency frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * @return the numberOfPrincipalGracePeriods
	 */
	@Column(name = "con_nu_principal_grace_periods", nullable = true)
	public Integer getNumberOfPrincipalGracePeriods() {
		return numberOfPrincipalGracePeriods;
	}

	/**
	 * @param numberOfPrincipalGracePeriods the numberOfPrincipalGracePeriods to set
	 */
	public void setNumberOfPrincipalGracePeriods(
			Integer numberOfPrincipalGracePeriods) {
		this.numberOfPrincipalGracePeriods = numberOfPrincipalGracePeriods;
	}

		
	/**
	 * @return the tiInstallmentAmount
	 */
	@Column(name = "con_am_ti_installment_amount", nullable = true)
	public Double getTiInstallmentAmount() {
		return tiInstallmentAmount;
	}

	/**
	 * @param tiInstallmentAmount the tiInstallmentAmount to set
	 */
	public void setTiInstallmentAmount(Double tiInstallmentAmount) {
		this.tiInstallmentAmount = tiInstallmentAmount;
	}

	/**
	 * @return the teInstallmentAmount
	 */
	@Column(name = "con_am_te_installment_amount", nullable = true)
	public Double getTeInstallmentAmount() {
		return teInstallmentAmount;
	}

	/**
	 * @param teInstallmentAmount the teInstallmentAmount to set
	 */
	public void setTeInstallmentAmount(Double teInstallmentAmount) {
		this.teInstallmentAmount = teInstallmentAmount;
	}

	/**
	 * @return the vatInstallmentAmount
	 */
	@Column(name = "con_am_vat_installment_amount", nullable = true)
	public Double getVatInstallmentAmount() {
		return vatInstallmentAmount;
	}

	/**
	 * @param vatInstallmentAmount the vatInstallmentAmount to set
	 */
	public void setVatInstallmentAmount(Double vatInstallmentAmount) {
		this.vatInstallmentAmount = vatInstallmentAmount;
	}

	/**
	 * @return the tiPrepaidInstallment
	 */
	@Column(name = "con_am_ti_prepaid_installment", nullable = true)
	public Double getTiPrepaidInstallment() {
		return tiPrepaidInstallment;
	}

	/**
	 * @param tiPrepaidInstallment the tiPrepaidInstallment to set
	 */
	public void setTiPrepaidInstallment(Double tiPrepaidInstallment) {
		this.tiPrepaidInstallment = tiPrepaidInstallment;
	}

	/**
	 * @return the numberPrepaidTerm
	 */
	@Column(name = "con_nu_number_prepaid_term", nullable = true)
	public Integer getNumberPrepaidTerm() {
		return numberPrepaidTerm;
	}

	/**
	 * @param numberPrepaidTerm the numberPrepaidTerm to set
	 */
	public void setNumberPrepaidTerm(Integer numberPrepaidTerm) {
		this.numberPrepaidTerm = numberPrepaidTerm;
	}	
	
    /**
	 * @return the numberGuarantors
	 */
	@Column(name = "con_nu_number_guarantors", nullable = true)
	public Integer getNumberGuarantors() {
		return numberGuarantors;
	}

	/**
	 * @param numberGuarantors the numberGuarantors to set
	 */
	public void setNumberGuarantors(Integer numberGuarantors) {
		this.numberGuarantors = numberGuarantors;
	}

	/**
	 * @return the nbPrints
	 */
	@Column(name = "con_nu_number_prints", nullable = true)
	public Integer getNbPrints() {
		return nbPrints;
	}

	/**
	 * @param nbPrints the nbPrints to set
	 */
	public void setNbPrints(Integer nbPrints) {
		this.nbPrints = nbPrints;
	}
	
	/**
	 * @return the checkerID
	 */
	@Column(name = "con_va_checker_id", nullable = true, length = 20)
	public String getCheckerID() {
		return checkerID;
	}

	/**
	 * @param checkerID the checkerID to set
	 */
	public void setCheckerID(String checkerID) {
		this.checkerID = checkerID;
	}

	/**
	 * @return the checkerName
	 */
	@Column(name = "con_va_checker_name", nullable = true, length = 50)
	public String getCheckerName() {
		return checkerName;
	}

	/**
	 * @param checkerName the checkerName to set
	 */
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	/**
	 * @return the checkerPhoneNumber
	 */
	@Column(name = "con_va_checker_phone_number", nullable = true, length = 50)
	public String getCheckerPhoneNumber() {
		return checkerPhoneNumber;
	}

	/**
	 * @param checkerPhoneNumber the checkerPhoneNumber to set
	 */
	public void setCheckerPhoneNumber(String checkerPhoneNumber) {
		this.checkerPhoneNumber = checkerPhoneNumber;
	}
	
	/**
	 * @return the forceActivated
	 */
	@Column(name = "con_bl_force_activated", nullable = true, columnDefinition = "boolean default false")
	public boolean isForceActivated() {
		return forceActivated;
	}

	/**
	 * @param forceActivated the forceActivated to set
	 */
	public void setForceActivated(boolean forceActivated) {
		this.forceActivated = forceActivated;
	}
	
	/**
	 * @return the transfered
	 */
	@Column(name = "con_bl_transfered", nullable = true, columnDefinition = "boolean default false")
	public boolean isTransfered() {
		return transfered;
	}

	/**
	 * @param transfered the transfered to set
	 */
	public void setTransfered(boolean transfered) {
		this.transfered = transfered;
	}

	/**
	 * @return the overdue
	 */
	@Column(name = "con_bl_overdue", nullable = true, columnDefinition = "boolean default false")
	public boolean isOverdue() {
		return overdue;
	}

	/**
	 * @param overdue the overdue to set
	 */
	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}
	
	/**
	 * @return the collections
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<Collection> getCollections() {
		return collections;
	}

	/**
	 * @param collections the collections to set
	 */
	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	/**
	 * @return the contractAdjustment
	 */
	@Transient
	public ContractAdjustment getContractAdjustment() {
		return null;
	}

	/**
	 * @return the collection
	 */
	@Transient
	public Collection getCollection() {
		if (collections != null && !collections.isEmpty()) {
			return collections.get(0);
		}
		return null;
	}

	/**
	 * @param applicantType
	 * @return
	 */
	@Transient
	public ContractApplicant getContractApplicant(EApplicantType applicantType) {
		if (contractApplicants != null && !contractApplicants.isEmpty()) {
			for (ContractApplicant contractApplicant : contractApplicants) {
				if (applicantType.getId().equals(contractApplicant.getApplicantType().getId())) {
					return contractApplicant;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Get guarantor
	 * @return
	 */
	@Transient
	public Applicant getGuarantor() {
		ContractApplicant contractApplicant = getContractApplicant(EApplicantType.G);
		if (contractApplicant != null) {
			return contractApplicant.getApplicant();
		}
		return null;
	}

	/**
	 * @return the contractDocuments
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractDocument> getContractDocuments() {
		return contractDocuments;
	}

	/**
	 * @param contractDocuments the contractDocuments to set
	 */
	public void setContractDocuments(List<ContractDocument> contractDocuments) {
		this.contractDocuments = contractDocuments;
	}	
	
	/**
	 * @return the contractIssues
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractIssue> getContractIssues() {
		return contractIssues;
	}

	/**
	 * @param contractIssues the contractIssues to set
	 */
	public void setContractIssues(List<ContractIssue> contractIssues) {
		this.contractIssues = contractIssues;
	}

	/**
	 * @return the contractNotes
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractNote> getContractNotes() {
		return contractNotes;
	}

	/**
	 * @param contractNotes the contractNotes to set
	 */
	public void setContractNotes(List<ContractNote> contractNotes) {
		this.contractNotes = contractNotes;
	}

	/**
	 * @return the contractRequests
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractRequest> getContractRequests() {
		return contractRequests;
	}

	/**
	 * @param contractRequests the contractRequests to set
	 */
	public void setContractRequests(List<ContractRequest> contractRequests) {
		this.contractRequests = contractRequests;
	}

	/**
	 * @return the contractSmss
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractSms> getContractSmss() {
		return contractSmss;
	}

	/**
	 * @param contractSmss the contractSmss to set
	 */
	public void setContractSmss(List<ContractSms> contractSmss) {
		this.contractSmss = contractSmss;
	}
	
	/**
	 * @return the contractSimulations
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractSimulation> getContractSimulations() {
		return contractSimulations;
	}

	/**
	 * @param contractSimulations the contractSimulations to set
	 */
	public void setContractSimulations(List<ContractSimulation> contractSimulations) {
		this.contractSimulations = contractSimulations;
	}

	/**
	 * @return the contractRedemptions
	 */
	@OneToMany(mappedBy="contract", fetch = FetchType.LAZY)
	public List<ContractRedemption> getContractRedemptions() {
		return contractRedemptions;
	}

	/**
	 * @param contractRedemptions the contractRedemptions to set
	 */
	public void setContractRedemptions(List<ContractRedemption> contractRedemptions) {
		this.contractRedemptions = contractRedemptions;
	}

	/**
	 * @return the vatValue
	 */
	@Column(name = "con_rt_vat", nullable = true, columnDefinition="double precision default '0'")
	public double getVatValue() {
		return vatValue;
	}

	/**
	 * @param vatValue the vatValue to set
	 */
	public void setVatValue(double vatValue) {
		this.vatValue = vatValue;
	}

	/**
	 * @return 
	 */
	@Transient
	@Override
	public List<ContractWkfHistoryItem> getHistories() {
		return (List<ContractWkfHistoryItem>) getHistories();
	}

	/**
	 * Get Net finance amount
	 * @return
	 */
	@Transient
	public Amount getNetFinanceAmount() {
		Amount netFinanceAmount = new Amount(0d, 0d, 0d);
		netFinanceAmount.plus(new Amount(getTeFinancedAmount(), getVatFinancedAmount(), getTiFinancedAmount()));
		if (contractFinServices != null) {
			for (ContractFinService contractFinService : contractFinServices) {
				if (contractFinService.getService() != null) {
					if (EServiceType.COMM.equals(contractFinService.getService().getServiceType()) || EServiceType.INSFEE.equals(contractFinService.getService().getServiceType())) {
						netFinanceAmount.plus(new Amount(contractFinService.getTePrice(), contractFinService.getVatPrice(), contractFinService.getTiPrice()));
					} else {
						netFinanceAmount.minus(new Amount(contractFinService.getTePrice(), contractFinService.getVatPrice(), contractFinService.getTiPrice()));
					}
				}
			}
		}	
		return netFinanceAmount;
	}
	
	/**
	 * Get service amount
	 * @return
	 */
	@Transient
	public Amount getServiceAmount(EServiceType serviceType) {
		if (contractFinServices != null) {
			for (ContractFinService contractFinService : contractFinServices) {
				if (contractFinService.getService() != null) {
					if (serviceType.equals(contractFinService.getService().getServiceType())) {
						return new Amount(contractFinService.getTePrice(), contractFinService.getVatPrice(), contractFinService.getTiPrice());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get Loan amount
	 * @return
	 */
	@Transient
	public Amount getLoanAmount() {
		return new Amount(getTeInstallmentAmount() * getTerm(), getVatInstallmentAmount() * getTerm(), getTiInstallmentAmount() * getTerm());
	}

	/**
	 * Get Net Interest amount
	 * @return
	 */
	@Transient
	public Amount getGrossInterestAmount() {
		double teInterestAmount = getTeInstallmentAmount() * getTerm() - getTeFinancedAmount();
		return MyMathUtils.calculateFromAmountExcl(teInterestAmount, getVatValue());
	}
	
	/**
	 * Get Net Interest amount
	 * @return
	 */
	@Transient
	public Double getNetInterestAmount() {
		return getTeInstallmentAmount() * getTerm() - getNetFinanceAmount().getTeAmount();
	}
	
	/**
	 * Get net IRR rate
	 * @return
	 */
	@Transient
	public Double getNetIrrRate() {
		return 100 * Rate.calculateIRR(LoanUtils.getNumberOfPeriods(getTerm(), getFrequency()), getTeInstallmentAmount(), getNetFinanceAmount().getTeAmount());
	}

	/**
	 * @param type
	 * @param status
	 * @return
	 */
	@Transient
	public ContractSimulation getContractSimulation(EAfterSaleEventType type, EWkfStatus status) {
		if (contractSimulations != null && !contractSimulations.isEmpty()) {
			for (ContractSimulation contractSimulation : contractSimulations) {
				if (contractSimulation.getAfterSaleEventType().equals(type) && contractSimulation.getWkfStatus().equals(status)) {
					return contractSimulation;
				}
			}
		}
		return null;
	}
	
	/**
	 * @return the vat
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vat_id")
	public Vat getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(Vat vat) {
		this.vat = vat;
	}

	/**
	 * @return the originBrach
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id_origin_branch", nullable = true)	
	/**
	 * @return the originBranch
	 */
	public OrgStructure getOriginBranch() {
		return originBranch;
	}

	/**
	 * @param originBranch the originBranch to set
	 */
	public void setOriginBranch(OrgStructure originBranch) {
		this.originBranch = originBranch;
	}

	/**
	 * @return the branchInCharge
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id_branch_in_charge", nullable = true)
	public OrgStructure getBranchInCharge() {
		return branchInCharge;
	}

	/**
	 * @param branchInCharge the branchInCharge to set
	 */
	public void setBranchInCharge(OrgStructure branchInCharge) {
		this.branchInCharge = branchInCharge;
	}
	
	/**
	 * @return the legalGroupCode
	 */
	@Column(name = "legal_group_code", nullable = true, length = 15)
	public String getLegalGroupCode() {
		return legalGroupCode;
	}

	/**
	 * @param legalGroupCode the legalGroupCode to set
	 */
	public void setLegalGroupCode(String legalGroupCode) {
		this.legalGroupCode = legalGroupCode;
	}


	/**
	 * @return the saleLostCutLost
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sc_lost_id")
	public SaleLostCutLost getSaleLostCutLost() {
		return saleLostCutLost;
	}

	/**
	 * @param saleLostCutLost the saleLostCutLost to set
	 */
	public void setSaleLostCutLost(SaleLostCutLost saleLostCutLost) {
		this.saleLostCutLost = saleLostCutLost;
	}

	/**
	 * @return the tiInvoiceAmount
	 */
	@Column(name = "con_am_ti_invoice_amount", nullable = true)
	public Double getTiInvoiceAmount() {
		return tiInvoiceAmount;
	}

	/**
	 * @param tiInvoiceAmount the tiInvoiceAmount to set
	 */
	public void setTiInvoiceAmount(Double tiInvoiceAmount) {
		this.tiInvoiceAmount = tiInvoiceAmount;
	}

	/**
	 * @return the teInvoiceAmount
	 */
	@Column(name = "con_am_te_invoice_amount", nullable = true)
	public Double getTeInvoiceAmount() {
		return teInvoiceAmount;
	}

	/**
	 * @param teInvoiceAmount the teInvoiceAmount to set
	 */
	public void setTeInvoiceAmount(Double teInvoiceAmount) {
		this.teInvoiceAmount = teInvoiceAmount;
	}

	/**
	 * @return the vatInvoiceAmount
	 */
	@Column(name = "con_am_vat_invoice_amount", nullable = true)
	public Double getVatInvoiceAmount() {
		return vatInvoiceAmount;
	}

	/**
	 * @param vatInvoiceAmount the vatInvoiceAmount to set
	 */
	public void setVatInvoiceAmount(Double vatInvoiceAmount) {
		this.vatInvoiceAmount = vatInvoiceAmount;
	}

	/**
	 * @return the taxInvoiceDate
	 */
	@Column(name = "con_dt_tax_invoice", nullable = true)
	public Date getTaxInvoiceDate() {
		return taxInvoiceDate;
	}

	/**
	 * @param taxInvoiceDate the taxInvoiceDate to set
	 */
	public void setTaxInvoiceDate(Date taxInvoiceDate) {
		this.taxInvoiceDate = taxInvoiceDate;
	}

	/**
	 * @return the foreclosureDate
	 */
	@Column(name = "con_dt_foreclosure", nullable = true)
	public Date getForeclosureDate() {
		return foreclosureDate;
	}

	/**
	 * @param foreclosureDate the foreclosureDate to set
	 */
	public void setForeclosureDate(Date foreclosureDate) {
		this.foreclosureDate = foreclosureDate;
	}
}