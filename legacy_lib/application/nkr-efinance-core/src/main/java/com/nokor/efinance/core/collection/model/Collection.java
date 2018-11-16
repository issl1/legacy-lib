package com.nokor.efinance.core.collection.model;

import java.util.Date;
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

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.payment.model.EPaymentMethod;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_collection")
public class Collection extends EntityWkf implements MCollection {
	/** */
	private static final long serialVersionUID = 332636116004520529L;

	private Contract contract;
	
	private List<ContractCollectionHistory> contractCollectionHistories;
	
	private Integer currentTerm;
	private Integer nbInstallmentsInOverdue;
	private Integer nbInstallmentsInOverdue0030;
	private Integer nbInstallmentsInOverdue3160;
	private Integer nbInstallmentsInOverdue6190;
	private Integer nbInstallmentsInOverdue91XX;
	
	private Integer nbOverdueInDays;
	private Integer debtLevel;
	private Integer dueDay;
	
	private Integer lastNumInstallmentPaid;
	private Integer nbInstallmentsPaid;
	private Double teTotalAmountInOverdue;
	private Double vatTotalAmountInOverdue;
	private Double tiTotalAmountInOverdue;
	
	private Double partialPaidInstallment;
	
	private EPaymentMethod lastPaidPaymentMethod;
	private Date lastPaymentDate;
	private Date nextDueDate;
	
	private Double tiLastPaidAmount;
	private Double teLastPaidAmount;
	private Double vatLastPaidAmount;
	
	private Double tePenaltyAmount;
	private Double vatPenaltyAmount;
	private Double tiPenaltyAmount;
	private Double tiFollowingFeeAmount;
	private Double teFollowingFeeAmount;
	private Double vatFollowingFeeAmount;
	
	private Double tiBalanceCapital;
	private Double teBalanceCapital;
	private Double vatBalanceCapital;
	
	private Double tiBalanceInterest;
	private Double teBalanceInterest;
	private Double vatBalanceInterest;
	
	private Double tiBalanceCollectionFee;
	private Double teBalanceCollectionFee;
	private Double vatBalanceCollectionFee;
	
	private Double tiBalanceOperationFee;
	private Double teBalanceOperationFee;
	private Double vatBalanceOperationFee;
	
	private Double tiBalanceRepossessionFee;
	private Double teBalanceRepossessionFee;
	private Double vatBalanceRepossessionFee;
	
	private Double tiBalanceTransferFee;
	private Double teBalanceTransferFee;
	private Double vatBalanceTransferFee;
	
	private Double tiBalancePressingFee;
	private Double teBalancePressingFee;
	private Double vatBalancePressingFee;
	
	private CollectionFlag lastCollectionFlag;
	private CollectionAssist lastCollectionAssist;
	private CollectionHistory lastCollectionHistory;
	private CallCenterHistory lastCallCenterHistory;
	private CollectionAction lastAction;
	
	private ERequestStatus requestExtendStatus;
	private Integer extendInDay;
	
	private Area area;
	
	private String message;
	
	/**
     * 
     * @return
     */
    public static Collection createInstance() {
    	Collection collection = EntityFactory.createInstance(Collection.class);
        return collection;
    }

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @return the collectionHistories
	 */
	@OneToMany(mappedBy="collection", fetch = FetchType.LAZY)
	public List<ContractCollectionHistory> getContractCollectionHistories() {
		return contractCollectionHistories;
	}

	/**
	 * @param collectionHistories the collectionHistories to set
	 */
	public void setContractCollectionHistories(
			List<ContractCollectionHistory> collectionHistories) {
		this.contractCollectionHistories = collectionHistories;
	}
	
	/**
	 * @return the nbInstallmentsInOverdue
	 */
    @Column(name = "col_nu_installments_in_overdue", nullable = true)
	public Integer getNbInstallmentsInOverdue() {
		return nbInstallmentsInOverdue;
	}

	/**
	 * @param nbInstallmentsInOverdue the nbInstallmentsInOverdue to set
	 */
	public void setNbInstallmentsInOverdue(Integer nbInstallmentsInOverdue) {
		this.nbInstallmentsInOverdue = nbInstallmentsInOverdue;
	}

	/**
	 * @return the nbOverdueInDays
	 */
    @Column(name = "col_nu_nb_overdue_in_days", nullable = true)
	public Integer getNbOverdueInDays() {
		return nbOverdueInDays;
	}

	/**
	 * @param nbOverdueInDays the nbOverdueInDays to set
	 */
	public void setNbOverdueInDays(Integer nbOverdueInDays) {
		this.nbOverdueInDays = nbOverdueInDays;
	}
	
	/**
	 * @return the debtLevel
	 */
	@Column(name = "col_nu_debt_level", nullable = true)
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
	 * @return the dueDay
	 */
	@Column(name = "col_nu_due_day", nullable = true)
	public Integer getDueDay() {
		return dueDay;
	}

	/**
	 * @param dueDay the dueDay to set
	 */
	public void setDueDay(Integer dueDay) {
		this.dueDay = dueDay;
	}

	/**
	 * @return the lastNumInstallmentPaid
	 */
	@Column(name = "col_nu_last_num_installment_paid", nullable = true)
	public Integer getLastNumInstallmentPaid() {
		return lastNumInstallmentPaid;
	}

	/**
	 * @param lastNumInstallmentPaid the lastNumInstallmentPaid to set
	 */
	public void setLastNumInstallmentPaid(Integer lastNumInstallmentPaid) {
		this.lastNumInstallmentPaid = lastNumInstallmentPaid;
	}

	/**
	 * @return the nbInstallmentsPaid
	 */
	@Column(name = "col_nu_nb_installments_paid", nullable = true)
	public Integer getNbInstallmentsPaid() {
		return nbInstallmentsPaid;
	}

	/**
	 * @param nbInstallmentsPaid the nbInstallmentsPaid to set
	 */
	public void setNbInstallmentsPaid(Integer nbInstallmentsPaid) {
		this.nbInstallmentsPaid = nbInstallmentsPaid;
	}

	/**
	 * @return the teTotalAmountInOverdue
	 */
	@Column(name = "col_am_te_total_amount_in_overdue", nullable = true)
	public Double getTeTotalAmountInOverdue() {
		return teTotalAmountInOverdue;
	}

	/**
	 * @param teTotalAmountInOverdue the teTotalAmountInOverdue to set
	 */
	public void setTeTotalAmountInOverdue(Double teTotalAmountInOverdue) {
		this.teTotalAmountInOverdue = teTotalAmountInOverdue;
	}

	/**
	 * @return the vatTotalAmountInOverdue
	 */
	@Column(name = "col_am_vat_total_amount_in_overdue", nullable = true)
	public Double getVatTotalAmountInOverdue() {
		return vatTotalAmountInOverdue;
	}

	/**
	 * @param vatTotalAmountInOverdue the vatTotalAmountInOverdue to set
	 */
	public void setVatTotalAmountInOverdue(Double vatTotalAmountInOverdue) {
		this.vatTotalAmountInOverdue = vatTotalAmountInOverdue;
	}

	/**
	 * @return the tiTotalAmountInOverdue
	 */
	@Column(name = "col_am_ti_total_amount_in_overdue", nullable = true)
	public Double getTiTotalAmountInOverdue() {
		return tiTotalAmountInOverdue;
	}

	/**
	 * @param tiTotalAmountInOverdue the tiTotalAmountInOverdue to set
	 */
	public void setTiTotalAmountInOverdue(Double tiTotalAmountInOverdue) {
		this.tiTotalAmountInOverdue = tiTotalAmountInOverdue;
	}

	/**
	 * @return the partialPaidInstallment
	 */
	@Column(name = "col_nu_nb_partial_installments_paid", nullable = true)
	public Double getPartialPaidInstallment() {
		return partialPaidInstallment;
	}

	/**
	 * @param partialPaidInstallment the partialPaidInstallment to set
	 */
	public void setPartialPaidInstallment(Double partialPaidInstallment) {
		this.partialPaidInstallment = partialPaidInstallment;
	}

	/**
	 * @return the lastPaidPaymentMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_pay_met_id", nullable = true)
	public EPaymentMethod getLastPaidPaymentMethod() {
		return lastPaidPaymentMethod;
	}

	/**
	 * @param lastPaidPaymentMethod the lastPaidPaymentMethod to set
	 */
	public void setLastPaidPaymentMethod(EPaymentMethod lastPaidPaymentMethod) {
		this.lastPaidPaymentMethod = lastPaidPaymentMethod;
	}
	
	/**
	 * @return the tiLastPaidAmount
	 */
	@Column(name = "col_am_ti_last_pay_amount", nullable = true)
	public Double getTiLastPaidAmount() {
		return tiLastPaidAmount;
	}

	/**
	 * @param tiLastPaidAmount the tiLastPaidAmount to set
	 */
	public void setTiLastPaidAmount(Double tiLastPaidAmount) {
		this.tiLastPaidAmount = tiLastPaidAmount;
	}

	/**
	 * @return the teLastPaidAmount
	 */
	@Column(name = "col_am_te_last_pay_amount", nullable = true)
	public Double getTeLastPaidAmount() {
		return teLastPaidAmount;
	}

	/**
	 * @param teLastPaidAmount the teLastPaidAmount to set
	 */
	public void setTeLastPaidAmount(Double teLastPaidAmount) {
		this.teLastPaidAmount = teLastPaidAmount;
	}

	/**
	 * @return the vatLastPaidAmount
	 */
	@Column(name = "col_am_vat_last_pay_amount", nullable = true)
	public Double getVatLastPaidAmount() {
		return vatLastPaidAmount;
	}

	/**
	 * @param vatLastPaidAmount the vatLastPaidAmount to set
	 */
	public void setVatLastPaidAmount(Double vatLastPaidAmount) {
		this.vatLastPaidAmount = vatLastPaidAmount;
	}

	/**
	 * @return the tePenaltyAmount
	 */
	@Column(name = "col_am_te_penalty_amount", nullable = true)
	public Double getTePenaltyAmount() {
		return tePenaltyAmount;
	}

	/**
	 * @param tePenaltyAmount the tePenaltyAmount to set
	 */
	public void setTePenaltyAmount(Double tePenaltyAmount) {
		this.tePenaltyAmount = tePenaltyAmount;
	}

	/**
	 * @return the vatPenaltyAmount
	 */
	@Column(name = "col_am_vat_penalty_amount", nullable = true)
	public Double getVatPenaltyAmount() {
		return vatPenaltyAmount;
	}

	/**
	 * @param vatPenaltyAmount the vatPenaltyAmount to set
	 */
	public void setVatPenaltyAmount(Double vatPenaltyAmount) {
		this.vatPenaltyAmount = vatPenaltyAmount;
	}

	/**
	 * @return the tiPenaltyAmount
	 */
	@Column(name = "col_am_ti_penalty_amount", nullable = true)
	public Double getTiPenaltyAmount() {
		return tiPenaltyAmount;
	}

	/**
	 * @param tiPenaltyAmount the tiPenaltyAmount to set
	 */
	public void setTiPenaltyAmount(Double tiPenaltyAmount) {
		this.tiPenaltyAmount = tiPenaltyAmount;
	}

	/**
	 * @return the lastPaymentDate
	 */
	@Column(name = "col_dt_last_payment", nullable = true)
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
	 * @return the currentTerm
	 */
	@Column(name = "col_nu_current_term", nullable = true)
	public Integer getCurrentTerm() {
		return currentTerm;
	}

	/**
	 * @param currentTerm the currentTerm to set
	 */
	public void setCurrentTerm(Integer currentTerm) {
		this.currentTerm = currentTerm;
	}

	/**
	 * @return the nbInstallmentsInOverdue0030
	 */
	@Column(name = "col_nu_installments_in_overdue_00_30", nullable = true)
	public Integer getNbInstallmentsInOverdue0030() {
		return nbInstallmentsInOverdue0030;
	}

	/**
	 * @param nbInstallmentsInOverdue0030 the nbInstallmentsInOverdue0030 to set
	 */
	public void setNbInstallmentsInOverdue0030(Integer nbInstallmentsInOverdue0030) {
		this.nbInstallmentsInOverdue0030 = nbInstallmentsInOverdue0030;
	}

	/**
	 * @return the nbInstallmentsInOverdue3160
	 */
	@Column(name = "col_nu_installments_in_overdue_31_60", nullable = true)
	public Integer getNbInstallmentsInOverdue3160() {
		return nbInstallmentsInOverdue3160;
	}

	/**
	 * @param nbInstallmentsInOverdue3160 the nbInstallmentsInOverdue3160 to set
	 */
	public void setNbInstallmentsInOverdue3160(Integer nbInstallmentsInOverdue3160) {
		this.nbInstallmentsInOverdue3160 = nbInstallmentsInOverdue3160;
	}

	/**
	 * @return the nbInstallmentsInOverdue6190
	 */
	@Column(name = "col_nu_installments_in_overdue_61_90", nullable = true)
	public Integer getNbInstallmentsInOverdue6190() {
		return nbInstallmentsInOverdue6190;
	}

	/**
	 * @param nbInstallmentsInOverdue6190 the nbInstallmentsInOverdue6190 to set
	 */
	public void setNbInstallmentsInOverdue6190(Integer nbInstallmentsInOverdue6190) {
		this.nbInstallmentsInOverdue6190 = nbInstallmentsInOverdue6190;
	}

	
	/**
	 * @return the nbInstallmentsInOverdue91XX
	 */
	@Column(name = "col_nu_installments_in_overdue_91_XX", nullable = true)
	public Integer getNbInstallmentsInOverdue91XX() {
		return nbInstallmentsInOverdue91XX;
	}

	/**
	 * @param nbInstallmentsInOverdue91XX the nbInstallmentsInOverdue91XX to set
	 */
	public void setNbInstallmentsInOverdue91XX(Integer nbInstallmentsInOverdue91XX) {
		this.nbInstallmentsInOverdue91XX = nbInstallmentsInOverdue91XX;
	}

	/**
	 * @return the nextDueDate
	 */
	@Column(name = "col_dt_next_due", nullable = true)
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
	 * @return the tiFollowingFeeAmount
	 */
	@Column(name = "col_am_ti_following_fee", nullable = true)
	public Double getTiFollowingFeeAmount() {
		return tiFollowingFeeAmount;
	}

	/**
	 * @param tiFollowingFeeAmount the tiFollowingFeeAmount to set
	 */
	public void setTiFollowingFeeAmount(Double tiFollowingFeeAmount) {
		this.tiFollowingFeeAmount = tiFollowingFeeAmount;
	}

	/**
	 * @return the teFollowingFeeAmount
	 */
	@Column(name = "col_am_te_following_fee", nullable = true)
	public Double getTeFollowingFeeAmount() {
		return teFollowingFeeAmount;
	}

	/**
	 * @param teFollowingFeeAmount the teFollowingFeeAmount to set
	 */
	public void setTeFollowingFeeAmount(Double teFollowingFeeAmount) {
		this.teFollowingFeeAmount = teFollowingFeeAmount;
	}

	/**
	 * @return the vatFollowingFeeAmount
	 */
	@Column(name = "col_am_vat_following_fee", nullable = true)
	public Double getVatFollowingFeeAmount() {
		return vatFollowingFeeAmount;
	}

	/**
	 * @param vatFollowingFeeAmount the vatFollowingFeeAmount to set
	 */
	public void setVatFollowingFeeAmount(Double vatFollowingFeeAmount) {
		this.vatFollowingFeeAmount = vatFollowingFeeAmount;
	}

	
	/**
	 * @return the tiBalanceCapital
	 */
	@Column(name = "col_am_ti_balance_cap", nullable = true)
	public Double getTiBalanceCapital() {
		return tiBalanceCapital;
	}

	/**
	 * @param tiBalanceCapital the tiBalanceCapital to set
	 */
	public void setTiBalanceCapital(Double tiBalanceCapital) {
		this.tiBalanceCapital = tiBalanceCapital;
	}

	/**
	 * @return the teBalanceCapital
	 */
	@Column(name = "col_am_te_balance_cap", nullable = true)
	public Double getTeBalanceCapital() {
		return teBalanceCapital;
	}

	/**
	 * @param teBalanceCapital the teBalanceCapital to set
	 */
	public void setTeBalanceCapital(Double teBalanceCapital) {
		this.teBalanceCapital = teBalanceCapital;
	}

	/**
	 * @return the vatBalanceCapital
	 */
	@Column(name = "col_am_vat_balance_cap", nullable = true)
	public Double getVatBalanceCapital() {
		return vatBalanceCapital;
	}

	/**
	 * @param vatBalanceCapital the vatBalanceCapital to set
	 */
	public void setVatBalanceCapital(Double vatBalanceCapital) {
		this.vatBalanceCapital = vatBalanceCapital;
	}

	/**
	 * @return the tiBalanceInterest
	 */
	@Column(name = "col_am_ti_balance_iap", nullable = true)
	public Double getTiBalanceInterest() {
		return tiBalanceInterest;
	}

	/**
	 * @param tiBalanceInterest the tiBalanceInterest to set
	 */
	public void setTiBalanceInterest(Double tiBalanceInterest) {
		this.tiBalanceInterest = tiBalanceInterest;
	}

	/**
	 * @return the teBalanceInterest
	 */
	@Column(name = "col_am_te_balance_iap", nullable = true)
	public Double getTeBalanceInterest() {
		return teBalanceInterest;
	}

	/**
	 * @param teBalanceInterest the teBalanceInterest to set
	 */
	public void setTeBalanceInterest(Double teBalanceInterest) {
		this.teBalanceInterest = teBalanceInterest;
	}

	/**
	 * @return the vatBalanceInterest
	 */
	@Column(name = "col_am_vat_balance_iap", nullable = true)
	public Double getVatBalanceInterest() {
		return vatBalanceInterest;
	}

	/**
	 * @param vatBalanceInterest the vatBalanceInterest to set
	 */
	public void setVatBalanceInterest(Double vatBalanceInterest) {
		this.vatBalanceInterest = vatBalanceInterest;
	}

	/**
	 * @return the tiBalanceCollectionFee
	 */
	@Column(name = "col_am_ti_balance_collection_fee", nullable = true)
	public Double getTiBalanceCollectionFee() {
		return tiBalanceCollectionFee;
	}

	/**
	 * @param tiBalanceCollectionFee the tiBalanceCollectionFee to set
	 */
	public void setTiBalanceCollectionFee(Double tiBalanceCollectionFee) {
		this.tiBalanceCollectionFee = tiBalanceCollectionFee;
	}

	/**
	 * @return the teBalanceCollectionFee
	 */
	@Column(name = "col_am_te_balance_collection_fee", nullable = true)
	public Double getTeBalanceCollectionFee() {
		return teBalanceCollectionFee;
	}

	/**
	 * @param teBalanceCollectionFee the teBalanceCollectionFee to set
	 */
	public void setTeBalanceCollectionFee(Double teBalanceCollectionFee) {
		this.teBalanceCollectionFee = teBalanceCollectionFee;
	}

	/**
	 * @return the vatBalanceCollectionFee
	 */
	@Column(name = "col_am_vat_balance_collection_fee", nullable = true)
	public Double getVatBalanceCollectionFee() {
		return vatBalanceCollectionFee;
	}

	/**
	 * @param vatBalanceCollectionFee the vatBalanceCollectionFee to set
	 */
	public void setVatBalanceCollectionFee(Double vatBalanceCollectionFee) {
		this.vatBalanceCollectionFee = vatBalanceCollectionFee;
	}

	/**
	 * @return the tiBalanceOperationFee
	 */
	@Column(name = "col_am_ti_balance_operation_fee", nullable = true)
	public Double getTiBalanceOperationFee() {
		return tiBalanceOperationFee;
	}

	/**
	 * @param tiBalanceOperationFee the tiBalanceOperationFee to set
	 */
	public void setTiBalanceOperationFee(Double tiBalanceOperationFee) {
		this.tiBalanceOperationFee = tiBalanceOperationFee;
	}

	/**
	 * @return the teBalanceOperationFee
	 */
	@Column(name = "col_am_te_balance_operation_fee", nullable = true)
	public Double getTeBalanceOperationFee() {
		return teBalanceOperationFee;
	}

	/**
	 * @param teBalanceOperationFee the teBalanceOperationFee to set
	 */
	public void setTeBalanceOperationFee(Double teBalanceOperationFee) {
		this.teBalanceOperationFee = teBalanceOperationFee;
	}

	/**
	 * @return the vatBalanceOperationFee
	 */
	@Column(name = "col_am_vat_balance_operation_fee", nullable = true)
	public Double getVatBalanceOperationFee() {
		return vatBalanceOperationFee;
	}

	/**
	 * @param vatBalanceOperationFee the vatBalanceOperationFee to set
	 */
	public void setVatBalanceOperationFee(Double vatBalanceOperationFee) {
		this.vatBalanceOperationFee = vatBalanceOperationFee;
	}

	/**
	 * @return 
	 */
	@Transient
	@Override
	public List<CollectionWkfHistoryItem> getHistories() {
		return (List<CollectionWkfHistoryItem>) getHistories();
	}		
	
	/**
	 * @return the lastCollectionFlag
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cnt_col_fla_id")	
	public CollectionFlag getLastCollectionFlag() {
		return lastCollectionFlag;
	}

	/**
	 * @param lastCollectionFlag the lastCollectionFlag to set
	 */
	public void setLastCollectionFlag(CollectionFlag lastCollectionFlag) {
		this.lastCollectionFlag = lastCollectionFlag;
	}

	/**
	 * @return the lastCollectionAssist
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cnt_col_ass_id")	
	public CollectionAssist getLastCollectionAssist() {
		return lastCollectionAssist;
	}

	/**
	 * @param lastCollectionAssist the lastCollectionAssist to set
	 */
	public void setLastCollectionAssist(CollectionAssist lastCollectionAssist) {
		this.lastCollectionAssist = lastCollectionAssist;
	}	
	
	/**
	 * @return the lastCollectionHistory
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_his_id")
	public CollectionHistory getLastCollectionHistory() {
		return lastCollectionHistory;
	}

	/**
	 * @param lastCollectionHistory the lastCollectionHistory to set
	 */
	public void setLastCollectionHistory(CollectionHistory lastCollectionHistory) {
		this.lastCollectionHistory = lastCollectionHistory;
	}	
	
	/**
	 * @return the lastCallCenterHistory
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cal_his_id")
	public CallCenterHistory getLastCallCenterHistory() {
		return lastCallCenterHistory;
	}

	/**
	 * @param lastCallCenterHistory the lastCallCenterHistory to set
	 */
	public void setLastCallCenterHistory(CallCenterHistory lastCallCenterHistory) {
		this.lastCallCenterHistory = lastCallCenterHistory;
	}

	/**
	 * @return the requestExtendStatus
	 */
	@Column(name = "req_sta_id_extend", nullable = true)
	@Convert(converter = ERequestStatus.class)
	public ERequestStatus getRequestExtendStatus() {
		return requestExtendStatus;
	}

	/**
	 * @param requestExtendStatus the requestExtendStatus to set
	 */
	public void setRequestExtendStatus(ERequestStatus requestExtendStatus) {
		this.requestExtendStatus = requestExtendStatus;
	}

	/**
	 * @return the extendInDay
	 */
	@Column(name = "col_extend_in_day", nullable = true)
	public Integer getExtendInDay() {
		return extendInDay;
	}

	/**
	 * @param extendInDay the extendInDay to set
	 */
	public void setExtendInDay(Integer extendInDay) {
		this.extendInDay = extendInDay;
	}

	/**
	 * @return the lastAction
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cnt_col_act_id")	
	public CollectionAction getLastAction() {
		return lastAction;
	}

	/**
	 * @param lastAction the lastAction to set
	 */
	public void setLastAction(CollectionAction lastAction) {
		this.lastAction = lastAction;
	}
	
	/**
	 * @return the area
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "are_id")
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * @return the tiBalanceRepossessionFee
	 */
	@Column(name = "col_am_ti_balance_repossession_fee", nullable = true)
	public Double getTiBalanceRepossessionFee() {
		return tiBalanceRepossessionFee;
	}

	/**
	 * @param tiBalanceRepossessionFee the tiBalanceRepossessionFee to set
	 */
	public void setTiBalanceRepossessionFee(Double tiBalanceRepossessionFee) {
		this.tiBalanceRepossessionFee = tiBalanceRepossessionFee;
	}

	/**
	 * @return the teBalanceRepossessionFee
	 */
	@Column(name = "col_am_te_balance_repossession_fee", nullable = true)
	public Double getTeBalanceRepossessionFee() {
		return teBalanceRepossessionFee;
	}

	/**
	 * @param teBalanceRepossessionFee the teBalanceRepossessionFee to set
	 */
	public void setTeBalanceRepossessionFee(Double teBalanceRepossessionFee) {
		this.teBalanceRepossessionFee = teBalanceRepossessionFee;
	}

	/**
	 * @return the vatBalanceRepossessionFee
	 */
	@Column(name = "col_am_vat_balance_repossession_fee", nullable = true)
	public Double getVatBalanceRepossessionFee() {
		return vatBalanceRepossessionFee;
	}

	/**
	 * @param vatBalanceRepossessionFee the vatBalanceRepossessionFee to set
	 */
	public void setVatBalanceRepossessionFee(Double vatBalanceRepossessionFee) {
		this.vatBalanceRepossessionFee = vatBalanceRepossessionFee;
	}

	/**
	 * @return the tiBalanceTransferFee
	 */
	@Column(name = "col_am_ti_balance_transfer_fee", nullable = true)
	public Double getTiBalanceTransferFee() {
		return tiBalanceTransferFee;
	}

	/**
	 * @param tiBalanceTransferFee the tiBalanceTransferFee to set
	 */
	public void setTiBalanceTransferFee(Double tiBalanceTransferFee) {
		this.tiBalanceTransferFee = tiBalanceTransferFee;
	}

	/**
	 * @return the teBalanceTransferFee
	 */
	@Column(name = "col_am_te_balance_transfer_fee", nullable = true)
	public Double getTeBalanceTransferFee() {
		return teBalanceTransferFee;
	}

	/**
	 * @param teBalanceTransferFee the teBalanceTransferFee to set
	 */
	public void setTeBalanceTransferFee(Double teBalanceTransferFee) {
		this.teBalanceTransferFee = teBalanceTransferFee;
	}

	/**
	 * @return the vatBalanceTransferFee
	 */
	@Column(name = "col_am_vat_balance_transfer_fee", nullable = true)
	public Double getVatBalanceTransferFee() {
		return vatBalanceTransferFee;
	}

	/**
	 * @param vatBalanceTransferFee the vatBalanceTransferFee to set
	 */
	public void setVatBalanceTransferFee(Double vatBalanceTransferFee) {
		this.vatBalanceTransferFee = vatBalanceTransferFee;
	}

	/**
	 * @return the tiBalancePressingFee
	 */
	@Column(name = "col_am_ti_balance_pressing_fee", nullable = true)
	public Double getTiBalancePressingFee() {
		return tiBalancePressingFee;
	}

	/**
	 * @param tiBalancePressingFee the tiBalancePressingFee to set
	 */
	public void setTiBalancePressingFee(Double tiBalancePressingFee) {
		this.tiBalancePressingFee = tiBalancePressingFee;
	}

	/**
	 * @return the teBalancePressingFee
	 */
	@Column(name = "col_am_te_balance_pressing_fee", nullable = true)
	public Double getTeBalancePressingFee() {
		return teBalancePressingFee;
	}

	/**
	 * @param teBalancePressingFee the teBalancePressingFee to set
	 */
	public void setTeBalancePressingFee(Double teBalancePressingFee) {
		this.teBalancePressingFee = teBalancePressingFee;
	}

	/**
	 * @return the vatBalancePressingFee
	 */
	@Column(name = "col_am_vat_balance_pressing_fee", nullable = true)
	public Double getVatBalancePressingFee() {
		return vatBalancePressingFee;
	}

	/**
	 * @param vatBalancePressingFee the vatBalancePressingFee to set
	 */
	public void setVatBalancePressingFee(Double vatBalancePressingFee) {
		this.vatBalancePressingFee = vatBalancePressingFee;
	}

	/**
	 * @return the message
	 */
	@Column(name = "col_va_message", nullable = true, length = 255)
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
