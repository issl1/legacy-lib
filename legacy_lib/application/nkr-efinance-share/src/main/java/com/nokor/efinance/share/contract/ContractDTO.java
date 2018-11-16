package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.asset.AssetDTO;
import com.nokor.efinance.share.document.DocumentDTO;


/**
 * @author youhort.ly
 *
 */
public class ContractDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -524001051815327908L;
	
	private Long id;
	private ApplicantDTO lessee;
	private List<ApplicantDTO> guarantors;
	
	private String contractID;
	private String transferID;
	private String applicationID;
	
	private UriDTO dealer;
	
	private AssetDTO asset;
	
	private UriDTO product;
	private double vat;
	
	private UriDTO marketingCampaign;
	
	private Double financeAmount;	
	private Double downPaymentAmount;
	
	private Double downPaymentPercentage;
	private Double tiInstallmentAmount;
	private Double teInstallmentAmount;
	private Double vatInstallmentAmount;
	
	private Date applicationDate;
	private Date approvalDate;
	private Date firstDueDate;
	private Date lastDueDate;
	private Date nextDueDate;
	private Date lastPaymentDate;
	private Date bookingDate;
	private Date contractDate;
		
	private Integer term;
	private Double flatRate;	
	private Double effRate;
	
	private Double prepaidInstallment;
	private Integer numberPrepaidTerm;
	
	private int numberOfGuarantor;
	
	private Double serviceFee;
	private Double commission;
	
	private Date penaltyCalculationDate;
	private Integer debtLevel; 
	private Integer overdueTerms; 
	private Integer overdueDays;
	private Double overdueAmounts;
	private Double balancePenalty;
	private Double balanceFollowingFee;
	private Double balancePressingFee;
	private Double balanceInstallment;
	private Double balanceInterest;
	private Double balanceCollectionFee;
	private Double balanceOperationFee;
	private Double balanceVAT;
	private Double balanceAR;
	private Integer remainingFullTerms;
	private Integer paidFullTerms; 
	private Double paidPartialTerms;
	private Double remainingPartialTerms;
	private Double paidRemaining;	
	private Date nextAppointmentDate;
	
	private String staffInCharge;
	
	private String latestStaffCode;
	private String currentStaffCode;
	
	private String checkerID;
	private String checkerName;
	private String checkerPhoneNumber;
	private String contractStatus;

	private UriDTO originBranch;
	
	private List<DocumentDTO> documents;
	
	/**
	 * 
	 */
	public ContractDTO() {
		// Must have no-argument constructor for Web Service
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
	 * @return the lessee
	 */
	public ApplicantDTO getLessee() {
		return lessee;
	}
	
	/**
	 * @param lessee the lessee to set
	 */
	public void setLessee(ApplicantDTO lessee) {
		this.lessee = lessee;
	}

	/**
	 * @return the guarantors
	 */
	public List<ApplicantDTO> getGuarantors() {
		return guarantors;
	}

	/**
	 * @param guarantors the guarantors to set
	 */
	public void setGuarantors(List<ApplicantDTO> guarantors) {
		this.guarantors = guarantors;
	}

	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}

	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * @param applicationID the applicationID to set
	 */
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	
	/**
	 * @return the asset
	 */
	public AssetDTO getAsset() {
		return asset;
	}
	
	/**
	 * @param asset the asset to set
	 */
	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}	
	

	/**
	 * @return the vat
	 */
	public double getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(double vat) {
		this.vat = vat;
	}	

	/**
	 * @return the financeAmount
	 */
	public Double getFinanceAmount() {
		return financeAmount;
	}

	/**
	 * @param financeAmount the financeAmount to set
	 */
	public void setFinanceAmount(Double financeAmount) {
		this.financeAmount = financeAmount;
	}	
	
	/**
	 * @return the downPaymentAmount
	 */
	public Double getDownPaymentAmount() {
		return downPaymentAmount;
	}

	/**
	 * @param downPaymentAmount the downPaymentAmount to set
	 */
	public void setDownPaymentAmount(Double downPaymentAmount) {
		this.downPaymentAmount = downPaymentAmount;
	}

	/**
	 * @return the downPaymentPercentage
	 */
	public Double getDownPaymentPercentage() {
		return downPaymentPercentage;
	}

	/**
	 * @param downPaymentPercentage the downPaymentPercentage to set
	 */
	public void setDownPaymentPercentage(Double downPaymentPercentage) {
		this.downPaymentPercentage = downPaymentPercentage;
	}

	/**
	 * @return the tiInstallmentAmount
	 */
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
	 * @return the applicationDate
	 */
	public Date getApplicationDate() {
		return applicationDate;
	}

	/**
	 * @param applicationDate the applicationDate to set
	 */
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}	

	/**
	 * @return the firstDueDate
	 */
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
	 * @return the nextDueDate
	 */
	public Date getNextDueDate() {
		return nextDueDate;
	}

	/**
	 * @param nextDueDate the nextDueDate to set
	 */
	public void setNextDueDate(Date nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	/**
	 * @return the lastPaymentDate
	 */
	public Date getLastPaymentDate() {
		return lastPaymentDate;
	}

	/**
	 * @param lastPaymentDate the lastPaymentDate to set
	 */
	public void setLastPaymentDate(Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	/**
	 * @return the bookingDate
	 */
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
	 * @return the term
	 */
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
	 * @return the flatRate
	 */
	public Double getFlatRate() {
		return flatRate;
	}

	/**
	 * @param flatRate the flatRate to set
	 */
	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}

	/**
	 * @return the effRate
	 */
	public Double getEffRate() {
		return effRate;
	}

	/**
	 * @param effRate the effRate to set
	 */
	public void setEffRate(Double effRate) {
		this.effRate = effRate;
	}
	
	/**
	 * @return the prepaidInstallment
	 */
	public Double getPrepaidInstallment() {
		return prepaidInstallment;
	}

	/**
	 * @param prepaidInstallment the prepaidInstallment to set
	 */
	public void setPrepaidInstallment(Double prepaidInstallment) {
		this.prepaidInstallment = prepaidInstallment;
	}

	/**
	 * @return the numberPrepaidTerm
	 */
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
	 * @return the numberOfGuarantor
	 */
	public int getNumberOfGuarantor() {
		return numberOfGuarantor;
	}

	/**
	 * @param numberOfGuarantor the numberOfGuarantor to set
	 */
	public void setNumberOfGuarantor(int numberOfGuarantor) {
		this.numberOfGuarantor = numberOfGuarantor;
	}

	/**
	 * @return the serviceFee
	 */
	public Double getServiceFee() {
		return serviceFee;
	}

	/**
	 * @param serviceFee the serviceFee to set
	 */
	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}	
	
	/**
	 * @return the commission
	 */
	public Double getCommission() {
		return commission;
	}

	/**
	 * @param commission the commission to set
	 */
	public void setCommission(Double commission) {
		this.commission = commission;
	}

	/**
	 * @return the penaltyCalculationDate
	 */
	public Date getPenaltyCalculationDate() {
		return penaltyCalculationDate;
	}

	/**
	 * @param penaltyCalculationDate the penaltyCalculationDate to set
	 */
	public void setPenaltyCalculationDate(Date penaltyCalculationDate) {
		this.penaltyCalculationDate = penaltyCalculationDate;
	}

	/**
	 * @return the debtLevel
	 */
	public Integer getDebtLevel() {
		return debtLevel;
	}

	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDebtLevel(Integer debtLevel) {
		this.debtLevel = debtLevel;
	}

	/**
	 * @return the overdueTerms
	 */
	public Integer getOverdueTerms() {
		return overdueTerms;
	}

	/**
	 * @return the overdueDays
	 */
	public Integer getOverdueDays() {
		return overdueDays;
	}

	/**
	 * @param overdueDays the overdueDays to set
	 */
	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}

	/**
	 * @param overdueTerms the overdueTerms to set
	 */
	public void setOverdueTerms(Integer overdueTerms) {
		this.overdueTerms = overdueTerms;
	}

	/**
	 * @return the overdueAmounts
	 */
	public Double getOverdueAmounts() {
		return overdueAmounts;
	}

	/**
	 * @param overdueAmounts the overdueAmounts to set
	 */
	public void setOverdueAmounts(Double overdueAmounts) {
		this.overdueAmounts = overdueAmounts;
	}

	/**
	 * @return the balancePenalty
	 */
	public Double getBalancePenalty() {
		return balancePenalty;
	}

	/**
	 * @param balancePenalty the balancePenalty to set
	 */
	public void setBalancePenalty(Double balancePenalty) {
		this.balancePenalty = balancePenalty;
	}	
	
	/**
	 * @return the balanceFollowingFee
	 */
	public Double getBalanceFollowingFee() {
		return balanceFollowingFee;
	}

	/**
	 * @param balanceFollowingFee the balanceFollowingFee to set
	 */
	public void setBalanceFollowingFee(Double balanceFollowingFee) {
		this.balanceFollowingFee = balanceFollowingFee;
	}

	/**
	 * @return the balancePressingFee
	 */
	public Double getBalancePressingFee() {
		return balancePressingFee;
	}

	/**
	 * @param balancePressingFee the balancePressingFee to set
	 */
	public void setBalancePressingFee(Double balancePressingFee) {
		this.balancePressingFee = balancePressingFee;
	}

	/**
	 * @return the balanceInstallment
	 */
	public Double getBalanceInstallment() {
		return balanceInstallment;
	}

	/**
	 * @param balanceInstallment the balanceInstallment to set
	 */
	public void setBalanceInstallment(Double balanceInstallment) {
		this.balanceInstallment = balanceInstallment;
	}

	/**
	 * @return the balanceInterest
	 */
	public Double getBalanceInterest() {
		return balanceInterest;
	}

	/**
	 * @param balanceInterest the balanceInterest to set
	 */
	public void setBalanceInterest(Double balanceInterest) {
		this.balanceInterest = balanceInterest;
	}

	/**
	 * @return the balanceCollectionFee
	 */
	public Double getBalanceCollectionFee() {
		return balanceCollectionFee;
	}

	/**
	 * @param balanceCollectionFee the balanceCollectionFee to set
	 */
	public void setBalanceCollectionFee(Double balanceCollectionFee) {
		this.balanceCollectionFee = balanceCollectionFee;
	}

	/**
	 * @return the balanceOperationFee
	 */
	public Double getBalanceOperationFee() {
		return balanceOperationFee;
	}

	/**
	 * @param balanceOperationFee the balanceOperationFee to set
	 */
	public void setBalanceOperationFee(Double balanceOperationFee) {
		this.balanceOperationFee = balanceOperationFee;
	}

	/**
	 * @return the balanceVAT
	 */
	public Double getBalanceVAT() {
		return balanceVAT;
	}

	/**
	 * @param balanceVAT the balanceVAT to set
	 */
	public void setBalanceVAT(Double balanceVAT) {
		this.balanceVAT = balanceVAT;
	}

	/**
	 * @return the balanceAR
	 */
	public Double getBalanceAR() {
		return balanceAR;
	}

	/**
	 * @param balanceAR the balanceAR to set
	 */
	public void setBalanceAR(Double balanceAR) {
		this.balanceAR = balanceAR;
	}

	/**
	 * @return the remainingFullTerms
	 */
	public Integer getRemainingFullTerms() {
		return remainingFullTerms;
	}

	/**
	 * @param remainingFullTerms the remainingFullTerms to set
	 */
	public void setRemainingFullTerms(Integer remainingFullTerms) {
		this.remainingFullTerms = remainingFullTerms;
	}

	/**
	 * @return the paidFullTerms
	 */
	public Integer getPaidFullTerms() {
		return paidFullTerms;
	}

	/**
	 * @param paidFullTerms the paidFullTerms to set
	 */
	public void setPaidFullTerms(Integer paidFullTerms) {
		this.paidFullTerms = paidFullTerms;
	}

	/**
	 * @return the paidPartialTerms
	 */
	public Double getPaidPartialTerms() {
		return paidPartialTerms;
	}

	/**
	 * @param paidPartialTerms the paidPartialTerms to set
	 */
	public void setPaidPartialTerms(Double paidPartialTerms) {
		this.paidPartialTerms = paidPartialTerms;
	}

	/**
	 * @return the remainingPartialTerms
	 */
	public Double getRemainingPartialTerms() {
		return remainingPartialTerms;
	}

	/**
	 * @param remainingPartialTerms the remainingPartialTerms to set
	 */
	public void setRemainingPartialTerms(Double remainingPartialTerms) {
		this.remainingPartialTerms = remainingPartialTerms;
	}

	/**
	 * @return the paidRemaining
	 */
	public Double getPaidRemaining() {
		return paidRemaining;
	}

	/**
	 * @param paidRemaining the paidRemaining to set
	 */
	public void setPaidRemaining(Double paidRemaining) {
		this.paidRemaining = paidRemaining;
	}

	/**
	 * @return the nextAppointmentDate
	 */
	public Date getNextAppointmentDate() {
		return nextAppointmentDate;
	}

	/**
	 * @param nextAppointmentDate the nextAppointmentDate to set
	 */
	public void setNextAppointmentDate(Date nextAppointmentDate) {
		this.nextAppointmentDate = nextAppointmentDate;
	}	

	/**
	 * @return the approvalDate
	 */
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
	 * @return the staffInCharge
	 */
	public String getStaffInCharge() {
		return staffInCharge;
	}

	/**
	 * @param staffInCharge the staffInCharge to set
	 */
	public void setStaffInCharge(String staffInCharge) {
		this.staffInCharge = staffInCharge;
	}

	/**
	 * @return the latestStaffCode
	 */
	public String getLatestStaffCode() {
		return latestStaffCode;
	}

	/**
	 * @param latestStaffCode the latestStaffCode to set
	 */
	public void setLatestStaffCode(String latestStaffCode) {
		this.latestStaffCode = latestStaffCode;
	}

	/**
	 * @return the currentStaffCode
	 */
	public String getCurrentStaffCode() {
		return currentStaffCode;
	}

	/**
	 * @param currentStaffCode the currentStaffCode to set
	 */
	public void setCurrentStaffCode(String currentStaffCode) {
		this.currentStaffCode = currentStaffCode;
	}	
	
	/**
	 * @return the checkerID
	 */
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
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}	
	
	/**
	 * @return the dealer
	 */
	public UriDTO getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(UriDTO dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the product
	 */
	public UriDTO getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(UriDTO product) {
		this.product = product;
	}

	/**
	 * @return the marketingCampaign
	 */
	public UriDTO getMarketingCampaign() {
		return marketingCampaign;
	}

	/**
	 * @param marketingCampaign the marketingCampaign to set
	 */
	public void setMarketingCampaign(UriDTO marketingCampaign) {
		this.marketingCampaign = marketingCampaign;
	}

	/**
	 * @return the documents
	 */
	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

	/**
	 * @return the transferID
	 */
	public String getTransferID() {
		return transferID;
	}

	/**
	 * @param transferID the transferID to set
	 */
	public void setTransferID(String transferID) {
		this.transferID = transferID;
	}		
	
	/**
	 * @return the contractDate
	 */
	public Date getContractDate() {
		return contractDate;
	}

	/**
	 * @param contractDate the contractDate to set
	 */
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	
	/**
	 * @return the originBranch
	 */
	public UriDTO getOriginBranch() {
		return originBranch;
	}

	/**
	 * @param originBranch the originBranch to set
	 */
	public void setOriginBranch(UriDTO originBranch) {
		this.originBranch = originBranch;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ContractDTO)) {
			 return false;
		 }
		 ContractDTO contractDTO = (ContractDTO) arg0;
		 return getId() != null && getId().equals(contractDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
