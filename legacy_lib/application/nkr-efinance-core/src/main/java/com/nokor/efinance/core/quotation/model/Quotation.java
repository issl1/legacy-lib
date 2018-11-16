package com.nokor.efinance.core.quotation.model;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.CrudAction;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantArc;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetArc;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EMediaPromoting;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.security.model.SecUser;

/**
 * Quotation class
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation", indexes = {
		@Index(name = "idx_quo_va_reference", columnList = "quo_va_reference"),
		@Index(name = "idx_quo_va_external_reference", columnList = "quo_va_external_reference"),
		@Index(name = "idx_quo_dea_id", columnList = "dea_id"),
		@Index(name = "idx_quo_fpd_id", columnList = "fpd_id"),
		@Index(name = "idx_quo_cam_id", columnList = "cam_id")
})
public class Quotation extends EntityWkf implements MQuotation {

	private static final long serialVersionUID = 8132370206805536749L;

	private Contract contract;
	private Applicant applicant;
	private ApplicantArc applicantArc;

	private String reference;
	private String externalReference;
	private String transferReference;
	
	private Dealer dealer;
	private SecUser secUser;
	private SecUser creditOfficer;
	private SecUser productionOfficer;
	private SecUser underwriter;
	private SecUser underwriterSupervisor;
	private SecUser manager;
	private SecUser documentController;
	private SecUser fieldCheck;
	
	private Asset asset;
	private AssetArc assetArc;
	
	private FinProduct financialProduct;
	private Campaign campaign;
	
	private Double tiDefaultFinanceAmount;
	private Double teDefaultFinanceAmount;
	private Double vatDefaultFinanceAmount;
	
	private Double tiFinanceAmount;
	private Double teFinanceAmount;
	private Double vatFinanceAmount;
	
	private Double tiAdvancePaymentAmount;
	private Double teAdvancePaymentAmount;
	private Double vatAdvancePaymentAmount;	
	private Double advancePaymentPercentage;
	private Double tiInstallmentAmount;
	private Double teInstallmentAmount;
	private Double vatInstallmentAmount;
	private boolean valid;

	private Date startCreationDate;
	private Date quotationDate;
	private Date submissionDate;
	private Date firstSubmissionDate;
	private Date acceptationDate;
	private Date rejectDate;
	private Date declineDate;
	private Date activationDate;
	private Date contractStartDate;
	private Date firstDueDate;
	private Date lastDueDate;
	private Date bookingDate;
	
	private Integer term;
	private Double interestRate;	
	private Double irrRate;
	private EFrequency frequency;
	private Integer numberOfPrincipalGracePeriods;	
	
	private EPlaceInstallment placeInstallment;
	private EMediaPromoting wayOfKnowing;
	
	private Double uwRevenuEstimation;
	private Double uwAllowanceEstimation;
	private Double uwNetIncomeEstimation;
	private Double uwBusinessExpensesEstimation;
	private Double uwPersonalExpensesEstimation;
	private Double uwFamilyExpensesEstimation;
	private Double uwLiabilityEstimation;
	
	private Double coRevenuEstimation;
	private Double coAllowanceEstimation;
	private Double coNetIncomeEstimation;
	private Double coBusinessExpensesEstimation;
	private Double coPersonalExpensesEstimation;
	private Double coFamilyExpensesEstimation;
	private Double coLiabilityEstimation;
	
	private Double coGuarantorRevenuEstimation;
	private Double coGuarantorAllowanceEstimation;
	private Double coGuarantorNetIncomeEstimation;
	private Double coGuarantorBusinessExpensesEstimation;
	private Double coGuarantorPersonalExpensesEstimation;
	private Double coGuarantorFamilyExpensesEstimation;
	private Double coGuarantorLiabilityEstimation;
		
	//Guarantor
	private Double uwGuarantorRevenuEstimation;
	private Double uwGuarantorAllowanceEstimation;
	private Double uwGuarantorNetIncomeEstimation;
	private Double uwGuarantorBusinessExpensesEstimation;
	private Double uwGuarantorPersonalExpensesEstimation;
	private Double uwGuarantorFamilyExpensesEstimation;
	private Double uwGuarantorLiabilityEstimation;

	private boolean fieldCheckPerformed;
	private boolean locked;
	private SecUser userLocked;
	
	private boolean issueDownPayment;
	
	private Double totalAR;
	private Double totalUE;
	private Double totalVAT;
	private Double tiPrepaidInstallment;
	private Integer numberPrepaidTerm;
	private double vatValue;
		
	private String checkerID;
	private String checkerName;
	private String checkerPhoneNumber;
	
	private List<Comment> comments;
	private List<QuotationApplicant> quotationApplicants;
	private List<QuotationService> quotationServices;
	private List<QuotationDocument> quotationDocuments;
	private List<QuotationExtModule> quotationExtModules;
	private List<QuotationSupportDecision> quotationSupportDecisions;
	
	private Integer timestamp;
	
	private OrgStructure originBranch;
	private String migrationID;
	
	/**
     * 
     * @return
     */
    public static Quotation createInstance() {
    	Quotation quotation = EntityFactory.createInstance(Quotation.class);
        return quotation;
    }
    
	/**
     * Get asset type's is.
     * @return The asset type's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the reference
	 */
    @Column(name = "quo_va_reference", unique = true, nullable = true, length = 20)
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
	@Column(name = "quo_va_external_reference", unique = true, nullable = true, length = 20)
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
	 * @return the transferReference
	 */
	@Transient
	public String getTransferReference() {
		return transferReference;
	}

	/**
	 * @param transferReference the transferReference to set
	 */
	public void setTransferReference(String transferReference) {
		this.transferReference = transferReference;
	}

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
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
    @JoinColumn(name = "arc_app_id")
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
	 * @return the creditOfficer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_co")
	public SecUser getCreditOfficer() {
		return creditOfficer;
	}

	/**
	 * @param creditOfficer the creditOfficer to set
	 */
	public void setCreditOfficer(SecUser creditOfficer) {
		this.creditOfficer = creditOfficer;
	}

	/**
	 * @return the productionOfficer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_po")
	public SecUser getProductionOfficer() {
		return productionOfficer;
	}

	/**
	 * @param productionOfficer the productionOfficer to set
	 */
	public void setProductionOfficer(SecUser productionOfficer) {
		this.productionOfficer = productionOfficer;
	}
	

	/**
	 * @return the underwriter
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_uw")
	public SecUser getUnderwriter() {
		return underwriter;
	}

	/**
	 * @param underwriter the underwriter to set
	 */
	public void setUnderwriter(SecUser underwriter) {
		this.underwriter = underwriter;
	}
	
	/**
	 * @return the underwriterSupervisor
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_us")
	public SecUser getUnderwriterSupervisor() {
		return underwriterSupervisor;
	}

	/**
	 * @param underwriterSupervisor the underwriterSupervisor to set
	 */
	public void setUnderwriterSupervisor(SecUser underwriterSupervisor) {
		this.underwriterSupervisor = underwriterSupervisor;
	}

	/**
	 * @return the manager
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_ma")
	public SecUser getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(SecUser manager) {
		this.manager = manager;
	}
	
	/**
	 * @return the documentController
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_dc")
	public SecUser getDocumentController() {
		return documentController;
	}

	/**
	 * @param documentController the documentController to set
	 */
	public void setDocumentController(SecUser documentController) {
		this.documentController = documentController;
	}

	/**
	 * @return the fieldCheck
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_fc")
	public SecUser getFieldCheck() {
		return fieldCheck;
	}

	/**
	 * @param fieldCheck the fieldCheck to set
	 */
	public void setFieldCheck(SecUser fieldCheck) {
		this.fieldCheck = fieldCheck;
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
	 * @return the financialProduct
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fpd_id")
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
	 * @return the previousQuotationStatus
	 */
 	public EWkfStatus getPreviousWkfStatus() {
    	// TO DO PYI: from Histo
		return null;
	}

	/**
	 * @param previousQuotationStatus the previousQuotationStatus to set
	 */
	public void setPreviousWkfStatus(EWkfStatus previousQuotationStatus) {
		
	}
		
	/**
	 * @return the tiDefaultFinanceAmount
	 */
	@Column(name = "quo_am_ti_default_finance_amount", nullable = true)
	public Double getTiDefaultFinanceAmount() {
		return tiDefaultFinanceAmount;
	}

	/**
	 * @param tiDefaultFinanceAmount the tiDefaultFinanceAmount to set
	 */
	public void setTiDefaultFinanceAmount(Double tiDefaultFinanceAmount) {
		this.tiDefaultFinanceAmount = tiDefaultFinanceAmount;
	}

	/**
	 * @return the teDefaultFinanceAmount
	 */
	@Column(name = "quo_am_te_default_finance_amount", nullable = true)
	public Double getTeDefaultFinanceAmount() {
		return teDefaultFinanceAmount;
	}

	/**
	 * @param teDefaultFinanceAmount the teDefaultFinanceAmount to set
	 */
	public void setTeDefaultFinanceAmount(Double teDefaultFinanceAmount) {
		this.teDefaultFinanceAmount = teDefaultFinanceAmount;
	}

	/**
	 * @return the vatDefaultFinanceAmount
	 */
	@Column(name = "quo_am_vat_default_finance_amount", nullable = true)
	public Double getVatDefaultFinanceAmount() {
		return vatDefaultFinanceAmount;
	}

	/**
	 * @param vatDefaultFinanceAmount the vatDefaultFinanceAmount to set
	 */
	public void setVatDefaultFinanceAmount(Double vatDefaultFinanceAmount) {
		this.vatDefaultFinanceAmount = vatDefaultFinanceAmount;
	}

	/**
	 * @return the tiFinanceAmount
	 */
	@Column(name = "quo_am_ti_finance_amount", nullable = true)
	public Double getTiFinanceAmount() {
		return tiFinanceAmount;
	}

	/**
	 * @param tiFinanceAmount the tiFinanceAmount to set
	 */
	public void setTiFinanceAmount(Double tiFinanceAmount) {
		this.tiFinanceAmount = tiFinanceAmount;
	}

	/**
	 * @return the teFinanceAmount
	 */
	@Column(name = "quo_am_te_finance_amount", nullable = true)
	public Double getTeFinanceAmount() {
		return teFinanceAmount;
	}

	/**
	 * @param teFinanceAmount the teFinanceAmount to set
	 */
	public void setTeFinanceAmount(Double teFinanceAmount) {
		this.teFinanceAmount = teFinanceAmount;
	}

	/**
	 * @return the vatFinanceAmount
	 */
	@Column(name = "quo_am_vat_finance_amount", nullable = true)
	public Double getVatFinanceAmount() {
		return vatFinanceAmount;
	}

	/**
	 * @param vatFinanceAmount the vatFinanceAmount to set
	 */
	public void setVatFinanceAmount(Double vatFinanceAmount) {
		this.vatFinanceAmount = vatFinanceAmount;
	}

	/**
	 * @return the tiAdvancePaymentAmount
	 */
	@Column(name = "quo_am_ti_advance_payment", nullable = true)
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
	@Column(name = "quo_am_te_advance_payment_amount", nullable = true)
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
	@Column(name = "quo_am_vat_advance_payment_amount", nullable = true)
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
	@Column(name = "quo_rt_advance_payment_pc", nullable = true)
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
	 * @return the term
	 */
	@Column(name = "quo_nu_term", nullable = true)
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
	@Column(name = "quo_rt_interest_rate", nullable = true)
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
	@Column(name = "quo_rt_irr_rate", nullable = true)
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
	 * @return the tiInstallmentAmount
	 */
	@Column(name = "quo_am_ti_installment_amount", nullable = true)
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
	@Column(name = "quo_am_te_installment_amount", nullable = true)
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
	@Column(name = "quo_am_vat_installment_amount", nullable = true)
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
	 * @return the valid
	 */
	@Column(name = "quo_bl_valid", nullable = false)
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	
	/**
	 * @return the startCreationDate
	 */
	@Column(name = "quo_dt_start_creation", nullable = true)
	public Date getStartCreationDate() {
		return startCreationDate;
	}

	/**
	 * @param startCreationDate the startCreationDate to set
	 */
	public void setStartCreationDate(Date startCreationDate) {
		this.startCreationDate = startCreationDate;
	}

	/**
	 * @return the quotationDate
	 */
	@Column(name = "quo_dt_quotation", nullable = true)
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
	 * @return the submissionDate
	 */
	@Column(name = "quo_dt_submission", nullable = true)
	public Date getSubmissionDate() {
		return submissionDate;
	}

	/**
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	/**
	 * @return the firstSubmissionDate
	 */
	@Column(name = "quo_dt_first_submission", nullable = true)
	public Date getFirstSubmissionDate() {
		return firstSubmissionDate;
	}

	/**
	 * @param firstSubmissionDate the firstSubmissionDate to set
	 */
	public void setFirstSubmissionDate(Date firstSubmissionDate) {
		this.firstSubmissionDate = firstSubmissionDate;
	}
	
	/**
	 * @return the acceptationDate
	 */
	@Column(name = "quo_dt_acceptation", nullable = true)
	public Date getAcceptationDate() {
		return acceptationDate;
	}

	/**
	 * @param acceptationDate the acceptationDate to set
	 */
	public void setAcceptationDate(Date acceptationDate) {
		this.acceptationDate = acceptationDate;
	}
	
	
	/**
	 * @return the rejectDate
	 */
	@Column(name = "quo_dt_reject", nullable = true)
	public Date getRejectDate() {
		return rejectDate;
	}

	/**
	 * @param rejectDate the rejectDate to set
	 */
	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
	}
	
	/**
	 * @return the declineDate
	 */
	@Column(name = "quo_dt_decline", nullable = true)
	public Date getDeclineDate() {
		return declineDate;
	}

	/**
	 * @param declineDate the declineDate to set
	 */
	public void setDeclineDate(Date declineDate) {
		this.declineDate = declineDate;
	}

	/**
	 * @return the activationDate
	 */
	@Column(name = "quo_dt_activation", nullable = true)
	public Date getActivationDate() {
		return activationDate;
	}

	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	

	/**
	 * @return the contractStartDate
	 */
	@Column(name = "quo_dt_contract_start", nullable = true)
	public Date getContractStartDate() {
		return contractStartDate;
	}

	/**
	 * @param contractStartDate the contractStartDate to set
	 */
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
		
	/**
	 * @return the firstDueDate
	 */
	@Column(name = "quo_dt_first_due", nullable = true)
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
	@Column(name = "quo_dt_last_due", nullable = true)
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
	@Column(name = "quo_dt_booking", nullable = true)
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
	@Column(name = "quo_nu_principal_grace_periods", nullable = true)
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
	 * @return the placeInstallment
	 */
    @Column(name = "pla_ins_id", nullable = true)
    @Convert(converter = EPlaceInstallment.class)
	public EPlaceInstallment getPlaceInstallment() {
		return placeInstallment;
	}

	/**
	 * @param placeInstallment the placeInstallment to set
	 */
	public void setPlaceInstallment(EPlaceInstallment placeInstallment) {
		this.placeInstallment = placeInstallment;
	}
	
	/**
	 * @return the wayOfKnowing
	 */
    @Column(name = "way_knw_id", nullable = true)
    @Convert(converter = EEmploymentStatus.class)
	public EMediaPromoting getWayOfKnowing() {
		return wayOfKnowing;
	}

	/**
	 * @param wayOfKnowing the wayOfKnowing to set
	 */
	public void setWayOfKnowing(EMediaPromoting wayOfKnowing) {
		this.wayOfKnowing = wayOfKnowing;
	}
	
	/**
	 * @return the uwRevenuEstimation
	 */
	@Column(name = "quo_am_uw_revenu_estimation", nullable = true)
	public Double getUwRevenuEstimation() {
		return uwRevenuEstimation;
	}

	/**
	 * @param uwRevenuEstimation the uwRevenuEstimation to set
	 */
	public void setUwRevenuEstimation(Double uwRevenuEstimation) {
		this.uwRevenuEstimation = uwRevenuEstimation;
	}
	
	/**
	 * @return the uwAllowanceEstimation
	 */
	@Column(name = "quo_am_uw_allowance_estimation", nullable = true)
	public Double getUwAllowanceEstimation() {
		return uwAllowanceEstimation;
	}

	/**
	 * @param uwAllowanceEstimation the uwAllowanceEstimation to set
	 */
	public void setUwAllowanceEstimation(Double uwAllowanceEstimation) {
		this.uwAllowanceEstimation = uwAllowanceEstimation;
	}

	/**
	 * @return the uwNetIncomeEstimation
	 */
	@Column(name = "quo_am_uw_net_income_estimation", nullable = true)
	public Double getUwNetIncomeEstimation() {
		return uwNetIncomeEstimation;
	}

	/**
	 * @param uwNetIncomeEstimation the uwNetIncomeEstimation to set
	 */
	public void setUwNetIncomeEstimation(Double uwNetIncomeEstimation) {
		this.uwNetIncomeEstimation = uwNetIncomeEstimation;
	}

	/**
	 * @return the uwBusinessExpensesEstimation
	 */
	@Column(name = "quo_am_uw_business_expenses_estimation", nullable = true)
	public Double getUwBusinessExpensesEstimation() {
		return uwBusinessExpensesEstimation;
	}

	/**
	 * @param uwBusinessExpensesEstimation the uwBusinessExpensesEstimation to set
	 */
	public void setUwBusinessExpensesEstimation(Double uwBusinessExpensesEstimation) {
		this.uwBusinessExpensesEstimation = uwBusinessExpensesEstimation;
	}

	/**
	 * @return the uwPersonalExpensesEstimation
	 */
	@Column(name = "quo_am_uw_personal_expenses_estimation", nullable = true)
	public Double getUwPersonalExpensesEstimation() {
		return uwPersonalExpensesEstimation;
	}

	/**
	 * @param uwPersonalExpensesEstimation the uwPersonalExpensesEstimation to set
	 */
	public void setUwPersonalExpensesEstimation(Double uwPersonalExpensesEstimation) {
		this.uwPersonalExpensesEstimation = uwPersonalExpensesEstimation;
	}

	/**
	 * @return the uwFamilyExpensesEstimation
	 */
	@Column(name = "quo_am_uw_family_expenses_estimation", nullable = true)
	public Double getUwFamilyExpensesEstimation() {
		return uwFamilyExpensesEstimation;
	}

	/**
	 * @param uwFamilyExpensesEstimation the uwFamilyExpensesEstimation to set
	 */
	public void setUwFamilyExpensesEstimation(Double uwFamilyExpensesEstimation) {
		this.uwFamilyExpensesEstimation = uwFamilyExpensesEstimation;
	}
	
	/**
	 * @return the uwLiabilityEstimation
	 */
	@Column(name = "quo_am_uw_liability_estimation", nullable = true)
	public Double getUwLiabilityEstimation() {
		return uwLiabilityEstimation;
	}

	/**
	 * @param uwLiabilityEstimation the uwLiabilityEstimation to set
	 */
	public void setUwLiabilityEstimation(Double uwLiabilityEstimation) {
		this.uwLiabilityEstimation = uwLiabilityEstimation;
	}

	/**
	 * @return the coRevenuEstimation
	 */
	@Column(name = "quo_am_co_revenu_estimation", nullable = true)
	public Double getCoRevenuEstimation() {
		return coRevenuEstimation;
	}

	/**
	 * @param coRevenuEstimation the coRevenuEstimation to set
	 */
	public void setCoRevenuEstimation(Double coRevenuEstimation) {
		this.coRevenuEstimation = coRevenuEstimation;
	}

	/**
	 * @return the coAllowanceEstimation
	 */
	@Column(name = "quo_am_co_allowance_estimation", nullable = true)
	public Double getCoAllowanceEstimation() {
		return coAllowanceEstimation;
	}

	/**
	 * @param coAllowanceEstimation the coAllowanceEstimation to set
	 */
	public void setCoAllowanceEstimation(Double coAllowanceEstimation) {
		this.coAllowanceEstimation = coAllowanceEstimation;
	}

	/**
	 * @return the coNetIncomeEstimation
	 */
	@Column(name = "quo_am_co_net_income_estimation", nullable = true)
	public Double getCoNetIncomeEstimation() {
		return coNetIncomeEstimation;
	}

	/**
	 * @param coNetIncomeEstimation the coNetIncomeEstimation to set
	 */
	public void setCoNetIncomeEstimation(Double coNetIncomeEstimation) {
		this.coNetIncomeEstimation = coNetIncomeEstimation;
	}

	/**
	 * @return the coBusinessExpensesEstimation
	 */
	@Column(name = "quo_am_co_business_expenses_estimation", nullable = true)
	public Double getCoBusinessExpensesEstimation() {
		return coBusinessExpensesEstimation;
	}

	/**
	 * @param coBusinessExpensesEstimation the coBusinessExpensesEstimation to set
	 */
	public void setCoBusinessExpensesEstimation(Double coBusinessExpensesEstimation) {
		this.coBusinessExpensesEstimation = coBusinessExpensesEstimation;
	}

	/**
	 * @return the coPersonalExpensesEstimation
	 */
	@Column(name = "quo_am_co_personal_expenses_estimation", nullable = true)
	public Double getCoPersonalExpensesEstimation() {
		return coPersonalExpensesEstimation;
	}

	/**
	 * @param coPersonalExpensesEstimation the coPersonalExpensesEstimation to set
	 */
	public void setCoPersonalExpensesEstimation(Double coPersonalExpensesEstimation) {
		this.coPersonalExpensesEstimation = coPersonalExpensesEstimation;
	}

	/**
	 * @return the coFamilyExpensesEstimation
	 */
	@Column(name = "quo_am_co_family_expenses_estimation", nullable = true)
	public Double getCoFamilyExpensesEstimation() {
		return coFamilyExpensesEstimation;
	}

	/**
	 * @param coFamilyExpensesEstimation the coFamilyExpensesEstimation to set
	 */
	public void setCoFamilyExpensesEstimation(Double coFamilyExpensesEstimation) {
		this.coFamilyExpensesEstimation = coFamilyExpensesEstimation;
	}

	/**
	 * @return the coLiabilityEstimation
	 */
	@Column(name = "quo_am_co_liability_estimation", nullable = true)
	public Double getCoLiabilityEstimation() {
		return coLiabilityEstimation;
	}

	/**
	 * @param coLiabilityEstimation the coLiabilityEstimation to set
	 */
	public void setCoLiabilityEstimation(Double coLiabilityEstimation) {
		this.coLiabilityEstimation = coLiabilityEstimation;
	}
	
	/**
	 * @return the coGuarantorRevenuEstimation
	 */
	@Column(name = "quo_am_co_gua_revenu_estimation", nullable = true)
	public Double getCoGuarantorRevenuEstimation() {
		return coGuarantorRevenuEstimation;
	}

	/**
	 * @param coGuarantorRevenuEstimation the coGuarantorRevenuEstimation to set
	 */
	public void setCoGuarantorRevenuEstimation(Double coGuarantorRevenuEstimation) {
		this.coGuarantorRevenuEstimation = coGuarantorRevenuEstimation;
	}

	/**
	 * @return the coGuarantorAllowanceEstimation
	 */
	@Column(name = "quo_am_co_gua_allowance_estimation", nullable = true)
	public Double getCoGuarantorAllowanceEstimation() {
		return coGuarantorAllowanceEstimation;
	}

	/**
	 * @param coGuarantorAllowanceEstimation the coGuarantorAllowanceEstimation to set
	 */
	public void setCoGuarantorAllowanceEstimation(
			Double coGuarantorAllowanceEstimation) {
		this.coGuarantorAllowanceEstimation = coGuarantorAllowanceEstimation;
	}

	/**
	 * @return the coGuarantorNetIncomeEstimation
	 */
	@Column(name = "quo_am_co_gua_net_income_estimation", nullable = true)
	public Double getCoGuarantorNetIncomeEstimation() {
		return coGuarantorNetIncomeEstimation;
	}

	/**
	 * @param coGuarantorNetIncomeEstimation the coGuarantorNetIncomeEstimation to set
	 */
	public void setCoGuarantorNetIncomeEstimation(
			Double coGuarantorNetIncomeEstimation) {
		this.coGuarantorNetIncomeEstimation = coGuarantorNetIncomeEstimation;
	}

	/**
	 * @return the coGuarantorBusinessExpensesEstimation
	 */
	@Column(name = "quo_am_co_gua_business_expenses_estimation", nullable = true)
	public Double getCoGuarantorBusinessExpensesEstimation() {
		return coGuarantorBusinessExpensesEstimation;
	}

	/**
	 * @param coGuarantorBusinessExpensesEstimation the coGuarantorBusinessExpensesEstimation to set
	 */
	public void setCoGuarantorBusinessExpensesEstimation(
			Double coGuarantorBusinessExpensesEstimation) {
		this.coGuarantorBusinessExpensesEstimation = coGuarantorBusinessExpensesEstimation;
	}

	/**
	 * @return the coGuarantorPersonalExpensesEstimation
	 */
	@Column(name = "quo_am_co_gua_personal_expenses_estimation", nullable = true)
	public Double getCoGuarantorPersonalExpensesEstimation() {
		return coGuarantorPersonalExpensesEstimation;
	}

	/**
	 * @param coGuarantorPersonalExpensesEstimation the coGuarantorPersonalExpensesEstimation to set
	 */
	public void setCoGuarantorPersonalExpensesEstimation(
			Double coGuarantorPersonalExpensesEstimation) {
		this.coGuarantorPersonalExpensesEstimation = coGuarantorPersonalExpensesEstimation;
	}

	/**
	 * @return the coGuarantorFamilyExpensesEstimation
	 */
	@Column(name = "quo_am_co_gua_family_expenses_estimation", nullable = true)
	public Double getCoGuarantorFamilyExpensesEstimation() {
		return coGuarantorFamilyExpensesEstimation;
	}

	/**
	 * @param coGuarantorFamilyExpensesEstimation the coGuarantorFamilyExpensesEstimation to set
	 */
	public void setCoGuarantorFamilyExpensesEstimation(
			Double coGuarantorFamilyExpensesEstimation) {
		this.coGuarantorFamilyExpensesEstimation = coGuarantorFamilyExpensesEstimation;
	}

	/**
	 * @return the coGuarantorLiabilityEstimation
	 */
	@Column(name = "quo_am_co_gua_liability_estimation", nullable = true)
	public Double getCoGuarantorLiabilityEstimation() {
		return coGuarantorLiabilityEstimation;
	}

	/**
	 * @param coGuarantorLiabilityEstimation the coGuarantorLiabilityEstimation to set
	 */
	public void setCoGuarantorLiabilityEstimation(
			Double coGuarantorLiabilityEstimation) {
		this.coGuarantorLiabilityEstimation = coGuarantorLiabilityEstimation;
	}

	/**
	 * @return the fieldCheckPerformed
	 */
	@Column(name = "quo_bl_field_check", nullable = true)
	public boolean isFieldCheckPerformed() {
		return fieldCheckPerformed;
	}

	/**
	 * @param fieldCheckPerformed the fieldCheckPerformed to set
	 */
	public void setFieldCheckPerformed(boolean fieldCheckPerformed) {
		this.fieldCheckPerformed = fieldCheckPerformed;
	}
	

	/**
	 * @return the locked
	 */
	@Column(name = "quo_bl_locked", nullable = true)
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * @return the issueDownPayment
	 */
	@Column(name = "quo_bl_issue_down_payment", nullable = true, columnDefinition = "boolean default false")
	public boolean isIssueDownPayment() {
		return issueDownPayment;
	}

	/**
	 * @param issueDownPayment the issueDownPayment to set
	 */
	public void setIssueDownPayment(boolean issueDownPayment) {
		this.issueDownPayment = issueDownPayment;
	}
	
	/**
	 * @return the checkerID
	 */
	@Column(name = "quo_va_checker_id", nullable = true, length = 20)
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
	@Column(name = "quo_va_checker_name", nullable = true, length = 50)
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
	@Column(name = "quo_va_checker_phone_number", nullable = true, length = 50)
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
	 * @return the userLocked
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_locked")
	public SecUser getUserLocked() {
		return userLocked;
	}

	/**
	 * @param userLocked the userLocked to set
	 */
	public void setUserLocked(SecUser userLocked) {
		this.userLocked = userLocked;
	}

	/**
	 * @return the quotationApplicants
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
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
	 * @return the quotationServices
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	public List<QuotationService> getQuotationServices() {
		if (quotationServices == null) {
			quotationServices = new ArrayList<QuotationService>();
		}
		return quotationServices;
	}

	/**
	 * @param quotationServices the quotationServices to set
	 */
	public void setQuotationServices(List<QuotationService> quotationServices) {
		this.quotationServices = quotationServices;
	}

	/**
	 * @return the quotationDocuments
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	public List<QuotationDocument> getQuotationDocuments() {
		if (quotationDocuments == null) {
			quotationDocuments = new ArrayList<QuotationDocument>();
		}
		return quotationDocuments;
	}
	
	/**
	 * @return the quotationDocuments
	 */
	@Transient
	public List<QuotationDocument> getQuotationDocuments(EApplicantType applicationType) {
		List<QuotationDocument> quotationDocumentsByType = new ArrayList<QuotationDocument>();
		if (quotationDocuments != null) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if (quotationDocument.getDocument().getApplicantType() == applicationType) {
					quotationDocumentsByType.add(quotationDocument);
				}
			}
		}
		return quotationDocumentsByType;
	}

	/**
	 * @param quotationDocuments the quotationDocuments to set
	 */
	public void setQuotationDocuments(List<QuotationDocument> quotationDocuments) {
		this.quotationDocuments = quotationDocuments;
	}	

	/**
	 * @return the quotationExtModules
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	@OrderBy("processDate DESC")
	public List<QuotationExtModule> getQuotationExtModules() {
		return quotationExtModules;
	}

	/**
	 * @param quotationExtModules the quotationExtModules to set
	 */
	public void setQuotationExtModules(List<QuotationExtModule> quotationExtModules) {
		this.quotationExtModules = quotationExtModules;
	}

	/**
	 * @return the quotationSupportDecisions
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	public List<QuotationSupportDecision> getQuotationSupportDecisions() {
		return quotationSupportDecisions;
	}

	/**
	 * @param quotationSupportDecisions the quotationSupportDecisions to set
	 */
	public void setQuotationSupportDecisions(
			List<QuotationSupportDecision> quotationSupportDecisions) {
		this.quotationSupportDecisions = quotationSupportDecisions;
	}

	/**
	 * @return the comments
	 */
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	@OrderBy("updateDate DESC")
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
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
	 * @return the uwGuarantorRevenuEstimation
	 */
	@Column(name = "quo_am_uw_gua_revenu_estimation", nullable = true)
	public Double getUwGuarantorRevenuEstimation() {
		return uwGuarantorRevenuEstimation;
	}

	/**
	 * @param uwGuarantorRevenuEstimation
	 *            the uwGuarantorRevenuEstimation to set
	 */
	public void setUwGuarantorRevenuEstimation(
			Double uwGuarantorRevenuEstimation) {
		this.uwGuarantorRevenuEstimation = uwGuarantorRevenuEstimation;
	}

	/**
	 * @return the uwAllowanceEstimationGuarantor
	 */
	@Column(name = "quo_am_uw_gua_allowance_estimation", nullable = true)
	public Double getUwGuarantorAllowanceEstimation() {
		return uwGuarantorAllowanceEstimation;
	}

	/**
	 * @param uwGuarantorAllowanceEstimation
	 *            the uwGuarantorAllowanceEstimatio to set
	 */
	public void setUwGuarantorAllowanceEstimation(
			Double uwGuarantorAllowanceEstimation) {
		this.uwGuarantorAllowanceEstimation = uwGuarantorAllowanceEstimation;
	}

	/**
	 * @return the uwNetIncomeEstimationGuarantor
	 */
	@Column(name = "quo_am_uw_gua_netincome_estimation", nullable = true)
	public Double getUwGuarantorNetIncomeEstimation() {
		return uwGuarantorNetIncomeEstimation;
	}

	/**
	 * @param uwGuarantorNetIncomeEstimation
	 *            the uwGuarantorNetIncomeEstimation to set
	 */
	public void setUwGuarantorNetIncomeEstimation(
			Double uwGuarantorNetIncomeEstimation) {
		this.uwGuarantorNetIncomeEstimation = uwGuarantorNetIncomeEstimation;
	}

	/**
	 * @return the uwGuarantorBusinessExpensesEstimation
	 */
	@Column(name = "quo_am_uw_gua_business_expenses_estimation", nullable = true)
	public Double getUwGuarantorBusinessExpensesEstimation() {
		return uwGuarantorBusinessExpensesEstimation;
	}

	/**
	 * @param uwGuarantorBusinessExpensesEstimation
	 *            the uwGuarantorBusinessExpensesEstimation to set
	 */
	public void setUwGuarantorBusinessExpensesEstimation(
			Double uwGuarantorBusinessExpensesEstimation) {
		this.uwGuarantorBusinessExpensesEstimation = uwGuarantorBusinessExpensesEstimation;
	}

	/**
	 * @return the uwGuarantorPersonalExpensesEstimation
	 */
	@Column(name = "quo_am_uw_gua_personal_expenses_estimation", nullable = true)
	public Double getUwGuarantorPersonalExpensesEstimation() {
		return uwGuarantorPersonalExpensesEstimation;
	}

	/**
	 * @param uwGuarantorPersonalExpensesEstimation
	 *            the uwGuarantorPersonalExpensesEstimation to set
	 */
	public void setUwGuarantorPersonalExpensesEstimation(
			Double uwGuarantorPersonalExpensesEstimation) {
		this.uwGuarantorPersonalExpensesEstimation = uwGuarantorPersonalExpensesEstimation;
	}

	/**
	 * @return the uwGuarantorFamilyExpensesEstimation
	 */
	@Column(name = "quo_am_uw_gua_family_expenses_estimation", nullable = true)
	public Double getUwGuarantorFamilyExpensesEstimation() {
		return uwGuarantorFamilyExpensesEstimation;
	}

	/**
	 * @param uwGuarantorFamilyExpensesEstimation
	 *            the uwGuarantorFamilyExpensesEstimation to set
	 */
	public void setUwGuarantorFamilyExpensesEstimation(
			Double uwGuarantorFamilyExpensesEstimation) {
		this.uwGuarantorFamilyExpensesEstimation = uwGuarantorFamilyExpensesEstimation;
	}

	/**
	 * @return the uwGuarantorLiabilityEstimation
	 */
	@Column(name = "quo_am_uw_gua_liability_estimation", nullable = true)
	public Double getUwGuarantorLiabilityEstimation() {
		return uwGuarantorLiabilityEstimation;
	}

	/**
	 * @param uwGuarantorLiabilityEstimation
	 *            the uwGuarantorLiabilityEstimation to set
	 */
	public void setUwGuarantorLiabilityEstimation(
			Double uwGuarantorLiabilityEstimation) {
		this.uwGuarantorLiabilityEstimation = uwGuarantorLiabilityEstimation;
	}	
	
	/**
	 * @return the totalAR
	 */
	@Column(name = "quo_am_total_ar", nullable = true)
	public Double getTotalAR() {
		return totalAR;
	}

	/**
	 * @param totalAR the totalAR to set
	 */
	public void setTotalAR(Double totalAR) {
		this.totalAR = totalAR;
	}

	/**
	 * @return the totalUE
	 */
	@Column(name = "quo_am_total_ue", nullable = true)
	public Double getTotalUE() {
		return totalUE;
	}

	/**
	 * @param totalUE the totalUE to set
	 */
	public void setTotalUE(Double totalUE) {
		this.totalUE = totalUE;
	}

	/**
	 * @return the totalVAT
	 */
	@Column(name = "quo_am_total_vat", nullable = true)
	public Double getTotalVAT() {
		return totalVAT;
	}

	/**
	 * @param totalVAT the totalVAT to set
	 */
	public void setTotalVAT(Double totalVAT) {
		this.totalVAT = totalVAT;
	}

	/**
	 * @return the tiPrepaidInstallment
	 */
	@Column(name = "quo_am_ti_prepaid_installment", nullable = true)
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
	@Column(name = "quo_nu_number_prepaid_term", nullable = true)
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
	 * @return the vatValue
	 */
	@Column(name = "quo_rt_vat", nullable = true, columnDefinition="double precision default '0'")
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
	 * @return the migrationID
	 */
	@Column(name = "quo_va_migration_id", nullable = true, length = 30)
	public String getMigrationID() {
		return migrationID;
	}

	/**
	 * @param migrationID the migrationID to set
	 */
	public void setMigrationID(String migrationID) {
		this.migrationID = migrationID;
	}

	/**
	 * @return the originBranch
	 */
	@Transient
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
	 * @param applicantType
	 * @return
	 */
	@Transient
	public QuotationApplicant getQuotationApplicant(EApplicantType applicantType) {
		if (quotationApplicants != null && !quotationApplicants.isEmpty()) {
			for (QuotationApplicant quotationApplicant : quotationApplicants) {
				if (applicantType.getId().equals(quotationApplicant.getApplicantType().getId()) && CrudAction.DELETE != quotationApplicant.getCrudAction()) {
					return quotationApplicant;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param serviId
	 * @return
	 */
	@Transient
	public QuotationService getQuotationService(Long serviId) {
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				if (quotationService.getService().getId().equals(serviId)) {
					return quotationService;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param serviId
	 * @return
	 */
	@Transient
	public QuotationService getQuotationService(String serviCode) {
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				if ((quotationService.getService() != null) && quotationService.getService().getCode().equals(serviCode)) {
					return quotationService;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param serviId
	 * @return
	 */
	@Transient
	public QuotationDocument getQuotationDocument(Long documId) {
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if ((quotationDocument.getDocument() != null) && quotationDocument.getDocument().getId().equals(documId)) {
					return quotationDocument;
				}
			}
		}
		return null;
	}
	
	/**
	 * Set main applicant
	 * @param mainApplicant
	 */
	public void setMainApplicant(Applicant mainApplicant) {
		if (quotationApplicants == null) {
			quotationApplicants = new ArrayList<QuotationApplicant>();
		}
		QuotationApplicant quotationApplicant = getQuotationApplicant(EApplicantType.C);
		if (quotationApplicant == null) {
			quotationApplicant = new QuotationApplicant();
			quotationApplicants.add(quotationApplicant);
		}		
		quotationApplicant.setApplicant(mainApplicant);
		quotationApplicant.setQuotation(this);
		quotationApplicant.setApplicantType(EApplicantType.C);
	}
	
	/**
	 * Get customer
	 * @return
	 */
	@Transient
	public Applicant getGuarantor() {
		QuotationApplicant quotationApplicant = getQuotationApplicant(EApplicantType.G);
		if (quotationApplicant != null) {
			return quotationApplicant.getApplicant();
		}
		return null;
	}
	
	/**
	 * @param guarantor
	 */
	public void setGuarantor(Applicant guarantor) {
		if (quotationApplicants == null) {
			quotationApplicants = new ArrayList<QuotationApplicant>();
		}
		QuotationApplicant quotationApplicant = getQuotationApplicant(EApplicantType.G);
		if (quotationApplicant == null) {
			quotationApplicant = new QuotationApplicant();
			quotationApplicants.add(quotationApplicant);
		}		
		quotationApplicant.setApplicant(guarantor);
		quotationApplicant.setQuotation(this);
		quotationApplicant.setApplicantType(EApplicantType.G);
	}
	
	
	/**
	 * @param quotationStatus
	 * @return
	 */
	public List<QuotationSupportDecision> getQuotationSupportDecisions(EWkfStatus quotationStatus) {
		List<QuotationSupportDecision> quotationSupportDecisionsbyStatus = new ArrayList<QuotationSupportDecision>();
		if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				if (quotationSupportDecision.getWkfStatus()== quotationStatus && !quotationSupportDecision.isProcessed()) {
					quotationSupportDecisionsbyStatus.add(quotationSupportDecision);
				}
			}
		}
		return quotationSupportDecisionsbyStatus;
	}

	/**
	 * @return the secUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secus_id")
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	
	/**
	 * @return 
	 */
	@Transient
	@Override
	public List<QuotationWkfHistoryItem> getHistories() {
		return (List<QuotationWkfHistoryItem>) getHistories();
	}


}
