package com.nokor.efinance.core.payment.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.exception.TechnicalException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.actor.service.ActorService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.payment.dao.PaymentDao;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.InstallmentChecked;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentThirdParty;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.payment.HistoryPaymentVO;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.share.contract.PaymentType;
import com.nokor.efinance.third.efinance.ClientAccounting;
import com.nokor.efinance.third.efinance.ClientAccounting.AccountingDTO;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.security.model.SecUser;


/**
 * 
 * @author prasnar
 *
 */
@Service("paymentService")
public class PaymentServiceImpl extends BaseEntityServiceImpl implements PaymentService, PaymentEntityField, CashflowEntityField {
	
	/** */
	private static final long serialVersionUID = -2242622240103257924L;
	
	// private final static String RECEIPT_OR_REF_NUM = "receipt_or_ref_num";
	private final static String RECEIPT_IR_REF_NUM = "receipt_ir_ref_num";
	private final static String RECEIPT_DIRECT_COST_NUM = "receipt_direct_cost_num";
	
	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private PaymentDao dao;	
	@Autowired
	private CashflowService cashflowService;		
	@Autowired
	private QuotationService quotationService;	
	@Autowired
	private ContractService contractService;	
	@Autowired 
	private ActorService actorService;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public PaymentDao getDao() {
		return dao;
	}

	/**
	 * Create Down Payment
	 * @see com.nokor.efinance.core.payment.service.PaymentService#createDownPayment(com.nokor.efinance.core.contract.model.Contract, java.util.Date)
	 */
	@Override
	public Payment createDownPayment(Contract contract, Date paymentDate) {
		// SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Payment payment = new Payment();
		payment.setApplicant(contract.getApplicant());
		payment.setPaymentDate(paymentDate);
		payment.setWkfStatus(PaymentWkfStatus.RVA);
		
		payment.setPayee(actorService.getActorByDealer(contract.getDealer()));
		DealerPaymentMethod dealerPaymentMethod = contract.getDealer().getDealerPaymentMethod(EPaymentFlowType.FIN);		
		payment.setPaymentMethod(dealerPaymentMethod.getPaymentMethod());
		DealerBankAccount dealerBankAccount = dealerPaymentMethod.getDealerBankAccount();
		if (dealerBankAccount != null) {
			payment.setPayeeBankAccount(dealerBankAccount.getBankAccount());		
		}
		DealerAccountHolder dealerAccountHolder = dealerPaymentMethod.getDealerAccountHolder();
		payment.setPayeeAccountHolder(dealerAccountHolder.getAccountHolder());
				
		// payment.setReceivedUser(receivedUser);
		payment.setPaymentType(EPaymentType.ORC);
		payment.setContract(contract);
		payment.setDealer(contract.getDealer());
		List<Cashflow> cashflows = cashflowService.getOfficialCashflowsToPaid(contract.getId());
		if (cashflows == null || cashflows.isEmpty()) {
			LOG.error("Error creating down payment, cashflows list could not be EMPTY");
			throw new IllegalArgumentException("Error creating down payment, cashflows list could not be EMPTY");
		}		
		payment.setPayer(actorService.getFinancialCompany());		
		payment.setCashflows(cashflows);
		payment = createPayment(payment);
		
		return payment;
	}
	
	/**
	 * Create a direct costs
	 * @param paymentDate
	 * @param paymentMethod
	 * @return
	 */
	@Override
	public List<Payment> createDirectCosts(List<Long> caflwIds) {
		List<Payment> payments = new ArrayList<>();
		for (Long caflwId : caflwIds) {
			payments.add(createDirectCost(caflwId));
		}
		return payments;
	}
	
	/**
	 * Create a direct cost
	 * @param paymentDate
	 * @param paymentMethod
	 * @return
	 */
	@Override
	public Payment createDirectCost(Long caflwId) {
		Cashflow cashflow = getById(Cashflow.class, caflwId);		
		if (cashflow == null) {
			LOG.error("Error creating direct cost, cashflow could not be EMPTY");
			throw new IllegalArgumentException("Error creating direct cost, cashflow could not be EMPTY");
		}
		
		SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Payment payment = new Payment();
		payment.setApplicant(cashflow.getContract().getApplicant());
		payment.setPaymentDate(DateUtils.today());
		payment.setPaymentMethod(cashflow.getPaymentMethod());
		payment.setWkfStatus(PaymentWkfStatus.PAI);
		payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
		payment.setReceivedUser(receivedUser);
		payment.setPaymentType(EPaymentType.DCO);
		payment.setContract(cashflow.getContract());
		payment.setDealer(cashflow.getContract().getDealer());
		
		List<Cashflow> cashflows = new ArrayList<>();
		cashflows.add(cashflow);
		
		payment.setCashflows(cashflows);
		payment = createPayment(payment);
		
		return payment;
	}
	
	/**
	 * Cancel a down payment
	 * @param paymnId
	 */
	@Override
	public void cancelDownPayment(Long paymnId) {
		Payment payment = getById(Payment.class, paymnId);
		if (payment.getPaymentType() != EPaymentType.ORC) {
			throw new IllegalArgumentException("Payment MUST be a down payment");
		}
		
		String reference = payment.getReference();
		String delearCode = payment.getDealer().getCode();
		
		Contract contract = payment.getCashflows().get(0).getContract();
		Quotation quotation = contract.getApplicant().getQuotation();
		quotation.setIssueDownPayment(false);
		quotation.setDocumentController(null);
		
		quotationService.changeQuotationStatus(quotation, QuotationWkfStatus.APV);
		
		for (Cashflow cashflow : payment.getCashflows()) {
			delete(cashflow);
		}
			
		delete(payment);
		delete(contract.getAsset());
		
		for (ContractApplicant contractApplicant : contract.getContractApplicants()) {
			delete(contractApplicant);
		}
				
		delete(contract);
		
		if (StringUtils.isNotEmpty(reference) && reference.length() > 6) {
			try {
				long referenceNumber = Long.parseLong(reference = reference.substring(reference.length() - 6, reference.length()));
				if (referenceNumber > 0) {
					SettingConfig receiptNumber = getByCode(SettingConfig.class, delearCode);
					long sequence = Long.parseLong(receiptNumber.getValue());
					if (referenceNumber == sequence - 1) {
						receiptNumber.setValue(String.valueOf(sequence - 1));
						saveOrUpdate(receiptNumber);
					}
				}
			} catch (NumberFormatException e) {
			}
		}
	}
	
	
	@Override
	public Payment createPayment(Payment payment) {
		LOG.debug("[>> saveOrUpdatePayment]");
		Assert.notNull(payment, "Payment could not be null.");	
		LOG.debug("Update payment,  id:" + payment.getId());
		
		List<Cashflow> cashflows = payment.getCashflows();
		if (cashflows == null || cashflows.isEmpty()) {
			LOG.error("Error creating down payment, cashflows list could not be EMPTY");
			throw new IllegalArgumentException("Error creating installment payment, cashflows list could not be EMPTY");
		}
		
		if (StringUtils.isEmpty(payment.getReference())) {
			String receiptCode = RECEIPT_IR_REF_NUM;
			// Reload contract object to avoid Lazy loading issue
			Contract contract = getById(Contract.class, payment.getContract().getId());
			String type = "IR";
			if (payment.getPaymentType().equals(EPaymentType.ORC)) {
				receiptCode = contract.getDealer().getCode();
				type = "OR";
			} else if (payment.getPaymentType().equals(EPaymentType.DCO)) {
				receiptCode = RECEIPT_DIRECT_COST_NUM;
				type = "DC";
			}
			SettingConfig receiptNumber = getSettingConfigByCode(receiptCode);
			long sequence = 1;
			if (receiptNumber == null) {
				receiptNumber = new SettingConfig();
				if (receiptCode == RECEIPT_IR_REF_NUM) {
					receiptNumber.setCode(RECEIPT_IR_REF_NUM);
					receiptNumber.setDesc("Installment Receipt Number");
				} else if (receiptCode == RECEIPT_DIRECT_COST_NUM) {
					receiptNumber.setCode(RECEIPT_DIRECT_COST_NUM);
					receiptNumber.setDesc("Direct Cost Receipt Number");
				} else {
					receiptNumber.setCode(contract.getDealer().getCode());
					receiptNumber.setDesc("Official Receipt Number of dealer " + contract.getDealer().getCode());
				}
				receiptNumber.setStatusRecord(EStatusRecord.ACTIV);
				receiptNumber.setReadOnly(false);
			} else {
				sequence = Long.parseLong(receiptNumber.getValue());
			}
			SequenceGenerator sequenceGenerator = new ReceiptGeneratorImpl(contract.getDealer(), type, sequence); 
			payment.setReference(sequenceGenerator.generate());
			receiptNumber.setValue(String.valueOf(sequence + 1));
			saveOrUpdate(receiptNumber);
		}
		double tePaidUsd = 0d;
		double vatPaidUsd = 0d;
		double tiPaidUsd = 0d;
		
		
		for (Cashflow cashflow : cashflows) {
			tePaidUsd += cashflow.getTeInstallmentAmount();
			vatPaidUsd += cashflow.getVatInstallmentAmount();
			tiPaidUsd += cashflow.getTiInstallmentAmount();
		}
		payment.setTePaidAmount(Math.abs(MyMathUtils.roundAmountTo(tePaidUsd)));
		payment.setVatPaidAmount(Math.abs(MyMathUtils.roundAmountTo(vatPaidUsd)));
		payment.setTiPaidAmount(Math.abs(MyMathUtils.roundAmountTo(tiPaidUsd)));
				
		saveOrUpdate(payment);
		for (Cashflow cashflow : cashflows) {
			cashflow.setPaid(true);
			cashflow.setPayment(payment);
			cashflowService.saveOrUpdateCashflow(cashflow);
		}
		
		if (payment.getPaymentType().equals(EPaymentType.IRC)) {
			try {
				sendToAccounting(payment);
			} catch (Exception e) {
				LOG.error("Error while calling Accounting service", e);
				throw new TechnicalException("Error while calling Accounting service", e);
			}
		}
		
		LOG.debug("[<< saveOrUpdatePayment]");
		return payment;
	}
	
	/**
	 * @param cashflows
	 * @throws Exception 
	 */
	private void sendToAccounting(Payment payment) throws Exception {
		Map<String, AccountingDTO> values = new HashMap<>();
		for (Cashflow cashflow : payment.getCashflows()) {
			if (!values.containsKey(cashflow.getJournalEvent().getCode())) {
				AccountingDTO accountingDTO = new AccountingDTO(cashflow.getJournalEvent().getCode());
				accountingDTO.setReference(payment.getReference());
				accountingDTO.setDesc(cashflow.getJournalEvent().getDescEn());
				accountingDTO.setStartDate(payment.getPaymentDate());
				accountingDTO.addAmount(new BigDecimal(cashflow.getTiInstallmentAmount()));
				accountingDTO.addAmount(new BigDecimal(cashflow.getTeInstallmentAmount()));
				accountingDTO.addAmount(new BigDecimal(cashflow.getVatValue()));
				values.put(accountingDTO.getEventCode(), accountingDTO);
			} else {
				AccountingDTO accountingDTO = values.get(cashflow.getJournalEvent().getCode());
				accountingDTO.addAmount(0, new BigDecimal(cashflow.getTiInstallmentAmount()));
				accountingDTO.addAmount(1, new BigDecimal(cashflow.getTeInstallmentAmount()));
				accountingDTO.addAmount(2, new BigDecimal(cashflow.getVatValue()));
			}
		}
		
		for (AccountingDTO accountingDTO : values.values()) {
			ClientAccounting.createPayment(accountingDTO);
		}
	}
	
	
	/**
	 * Change payment status
	 * @param payment
	 * @param paymentStatus
	 */
	@Override
	public void changePaymentStatus(Payment payment, EWkfStatus paymentStatus) {
		payment.setWkfStatus(paymentStatus);
		saveOrUpdate(payment);
	}
	
	/**
	 * Cancel the given payements
	 * 
	 * @param ids
	 */
	public void cancelPayments(List<Long> ids) {
		for (Long id : ids) {
			Payment payment = getById(Payment.class, id);
			cancelPayment(payment);
		}
	}
	
	/**
	 * @param payment
	 */
	@Override
	public void cancelPayment(Payment payment) {
		payment.setWkfStatus(PaymentWkfStatus.CAN);
		saveOrUpdate(payment);
		
		List<Cashflow> cashflows = payment.getCashflows();
		for (Cashflow cashflow : cashflows) {
			cashflow.setCancel(true);
			cashflow.setCancelationDate(DateUtils.today());
			cashflowService.saveOrUpdateCashflow(cashflow);
			boolean isExcludedCashflow = cashflow.getCashflowType().equals(ECashflowType.PEN) 
					|| (cashflow.getCashflowType().equals(ECashflowType.FEE) 
						&& (Cashflow.WINFEE.equals(cashflow.getService().getCode()) || Cashflow.PAYGO.equals(cashflow.getService().getCode()))
							); 
			
			if (!isExcludedCashflow) {
				Cashflow newCashflow = (Cashflow) SerializationUtils.clone(cashflow);
				newCashflow.setId(null);
				newCashflow.setPaid(false);
				newCashflow.setUnpaid(false);
				newCashflow.setCancel(false);
				newCashflow.setConfirm(false);
				newCashflow.setCancelationDate(null);
				newCashflow.setPayment(null);
				cashflowService.saveOrUpdateCashflow(newCashflow);
			}
		}		
		if (!cashflows.isEmpty()) {
			Contract contract = cashflows.get(0).getContract();
			if (contract.getWkfStatus().equals(ContractWkfStatus.CLO)) {
				contractService.changeContractStatus(contract, ContractWkfStatus.FIN);
			}
		} else {
			ComponentLayoutFactory.displayErrorMsg(I18N.message("payment.does.not.have.cashflows"));
		}
	}

	/**
	 * Delete an applicant
	 * @param applicant
	 */
	@Override
	public void deletePayment(Payment payment) {
		delete(payment);
	}
	
	/**
	 * @param paymnId
	 */
	@Override
	public void issuePurchaseOrder(Payment payment) {
		/*Payment payment = getById(Payment.class, paymnId);
		if (StringUtils.isEmpty(payment.getCode())) {
			String receiptCode = payment.getDealer().getCode();
			SettingConfig receiptNumber = getByCode(SettingConfig.class, receiptCode);
			long sequence = 1;
			if (receiptNumber == null) {
				receiptNumber = new SettingConfig();
				receiptNumber.setCode(payment.getDealer().getCode());
				receiptNumber.setDesc("Official Receipt Number of dealer " + payment.getDealer().getCode());
				receiptNumber.setStatusRecord(StatusRecord.ACTIV);
				receiptNumber.setReadOnly(false);
			} else {
				sequence = Long.parseLong(receiptNumber.getValue());
			}
			SequenceGenerator sequenceGenerator = new ReceiptGeneratorImpl(payment.getDealer(), "OR", sequence); 
			payment.setCode(sequenceGenerator.generate());
			receiptNumber.setValue(String.valueOf(sequence + 1));
			saveOrUpdate(receiptNumber);
		}
		saveOrUpdate(payment); */
		payment.setPurchaseOrderDate(DateUtils.today());
	}
	
	/**
	 * @param payment
	 */
	@Override
	public void secondPayment(Payment payment) {
		payment.setSecondPaymentDate(DateUtils.today());
		changePaymentStatus(payment, PaymentWkfStatus.PAI);
	}
	
	/**
	 * @param cotraId
	 * @param numInstallment
	 * @return
	 */
	@Transactional(readOnly = true)
	public Payment getPaymentByContract(Long cotraId, int numInstallment) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, numInstallment));
		restrictions.addCriterion(Restrictions.eq(PAID, true));
		List<Cashflow> cashflows = list(restrictions);
		if (cashflows != null && !cashflows.isEmpty()) {
			return cashflows.get(0).getPayment();
		}
		return null;
	}
	/**
	 * @param payments
	 * @return list of historypayment
	 */
	public List<HistoryPaymentVO> getHistoryPaymentOnOverdue(List<Payment> payments) {
		List<HistoryPaymentVO> historyPayments = new ArrayList<HistoryPaymentVO>();
		if (payments != null && !payments.isEmpty()) {
			Contract previouseContract = null;
			for (Payment payment : payments) {
				if (payment.getCashflows().get(0).getContract() != null) {
					if (previouseContract == null
							|| payment.getCashflows().get(0).getContract() != previouseContract) {
						Payment lastPayment = contractService.getLastPayment(payment.getCashflows().get(0).getContract().getId());
						HistoryPaymentVO historyPaymentVO = new HistoryPaymentVO();
						
						historyPaymentVO.setId(payment.getId());
						historyPaymentVO.setContractReference(payment.getCashflows().get(0).getContract().getReference() == null ? "" : payment.getCashflows().get(0).getContract().getReference());
						historyPaymentVO.setLastName(payment.getApplicant().getIndividual().getLastNameEn());
						historyPaymentVO.setFirstName(payment.getApplicant().getIndividual().getFirstNameEn());
						// TODO YLY
						// historyPaymentVO.setMobilePhone(payment.getApplicant().getMobilePhone());
						historyPaymentVO.setDealer(payment.getDealer() != null ? payment.getDealer().getNameEn() : "");
						historyPaymentVO.setDealerType(payment.getDealer() != null ? payment.getDealer().getDealerType().getDesc() : "");
						historyPaymentVO.setContractTerm(payment.getCashflows().get(0).getContract().getTerm());
						if (lastPayment != null) {
							historyPaymentVO.setLastInstallmentPaid(lastPayment.getPaymentDate());
						}
						int[] listSchedule = getNumInstallmentPaidOnSchedule(payment.getCashflows().get(0).getContract());
						historyPaymentVO.setNumInstallmentPaid(getNumInstallmentPaid(payment.getCashflows().get(0).getContract()));
						historyPaymentVO.setNumInstallmentPaidOnSchedule(listSchedule[0]);
						historyPaymentVO.setNumOverdue1to10(listSchedule[1]);
						historyPaymentVO.setNumOverdue11to30(listSchedule[2]);
						historyPaymentVO.setNumOverdueOver30(listSchedule[3]);
						historyPaymentVO.setCurrentContractInOverdue(isCurrentContract(
										payment.getCashflows().get(0).getContract(),
										DateUtils.getDateAtBeginningOfMonth(DateUtils.today()), DateUtils
												.getDateAtEndOfMonth(DateUtils.today())));
						historyPayments.add(historyPaymentVO);
						previouseContract = payment.getCashflows().get(0).getContract();	
					}
				}
			}
		}
		return historyPayments;
	}
	/**
	 * 
	 * @param contract
	 * @param date
	 * @return
	 */
	private boolean isCurrentContract(Contract contract, Date startDate, Date endDate) {
		List<Cashflow> cashflows = cashflowService.getCashflowsToPaid(contract.getId(), startDate, endDate);
		Integer overdueSchedule = null;
		if (cashflows != null && !cashflows.isEmpty()) {
			overdueSchedule = DateUtils.getDiffInDays(DateUtils.today(), cashflows.get(0).getInstallmentDate()).intValue();
		}
		if (overdueSchedule != null && overdueSchedule > 0) {
			return true;
		} else {
			return false;
		}
		
	}
	/**
	 * @param cashflows
	 * @return list of historypayment
	 */
	public List<HistoryPaymentVO> getHistoryPaymentCurrentOverdue(List<Cashflow> cashflows) {
		List<HistoryPaymentVO> historyPayments = new ArrayList<HistoryPaymentVO>();
		if (cashflows != null && !cashflows.isEmpty()) {
			Contract contract = null;
			for (Cashflow cashflow : cashflows) {
				if (contract == null
						|| contract.getId() != cashflow.getContract().getId()) {
					contract = cashflow.getContract();
					
					Integer overdueSchedule = DateUtils.getDiffInDays(DateUtils.today(), cashflow.getInstallmentDate()).intValue();
					if (overdueSchedule != null && overdueSchedule > 0) {
						HistoryPaymentVO historyPaymentVO = new HistoryPaymentVO();
						historyPaymentVO.setId(contract.getId());
						historyPaymentVO.setContractReference(contract.getReference() == null ? "" : contract.getReference());
						historyPaymentVO.setLastName(contract.getApplicant().getIndividual().getLastNameEn());
						historyPaymentVO.setFirstName(contract.getApplicant().getIndividual().getFirstNameEn());
						// TODO YLY
						// historyPaymentVO.setMobilePhone(contract.getApplicant().getMobilePhone());
						historyPaymentVO.setDealer(contract.getDealer() != null ? contract.getDealer().getNameEn() : "");
						historyPaymentVO.setDealerType(contract.getDealer() != null ? contract.getDealer().getDealerType().getDesc() : "");
						historyPaymentVO.setContractTerm(contract.getTerm());
						Payment lastPayment = contractService.getLastPayment(contract.getId());
						if (lastPayment != null) {
							historyPaymentVO.setLastInstallmentPaid(lastPayment.getPaymentDate());
						}
						int[] listSchedule = getNumInstallmentPaidOnSchedule(contract);
						historyPaymentVO.setNumInstallmentPaid(getNumInstallmentPaid(contract));
						historyPaymentVO.setNumInstallmentPaidOnSchedule(listSchedule[0]);
						historyPaymentVO.setNumOverdue1to10(listSchedule[1]);
						historyPaymentVO.setNumOverdue11to30(listSchedule[2]);
						historyPaymentVO.setNumOverdueOver30(listSchedule[3]);
						historyPaymentVO.setCurrentContractInOverdue(true);
						historyPayments.add(historyPaymentVO);	
					}
					
				}	
			}
		}
		return historyPayments;
	}
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private int getNumInstallmentPaid(Contract contract) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		List<Cashflow> cashflows = list(restrictions);
		int numNumInstallmentPaid = 0;
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getNumInstallment() > numNumInstallmentPaid) {
					numNumInstallmentPaid = cashflow.getNumInstallment();
				}
			}
		}
		return numNumInstallmentPaid;
	}

	/**
	 * 
	 * @param contract
	 * @return
	 */
	private int[] getNumInstallmentPaidOnSchedule(Contract contract) {
		int numInstallmentPaidOnSchedule = 0;
		int numOverdue1to10 = 0;
		int numOverdue11to30 = 0;
		int numOverdueOver30 = 0;
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addOrder(Order.asc("payment.id"));
		List<Cashflow> cashflows = list(restrictions);
		if (cashflows != null && !cashflows.isEmpty()) {
			Cashflow cashflowpreviouse = null;
			for (Cashflow cashflow : cashflows) {
				if(cashflowpreviouse == null || cashflowpreviouse.getNumInstallment() != cashflow.getNumInstallment()) {
					Payment payment = cashflow.getPayment();
					Integer overdueSchedule = 0;
					overdueSchedule = DateUtils.getDiffInDays(payment.getPaymentDate(), cashflow.getInstallmentDate()).intValue();
					if (overdueSchedule == null || overdueSchedule == 0) {
						numInstallmentPaidOnSchedule++;
					} else if (overdueSchedule >= 1 && overdueSchedule <= 10) {
						numOverdue1to10++;
					} else if (overdueSchedule >= 11 && overdueSchedule <= 30) {
						numOverdue11to30++;
					} else {
						numOverdueOver30++;
					}
					cashflowpreviouse = cashflow;	
				}
			}
		}
		return new int[]{numInstallmentPaidOnSchedule, numOverdue1to10,numOverdue11to30, numOverdueOver30};
	}
	
	/**
	 * 
	 */
	public List<Payment> getListPaymentRemaining(Dealer dealer, Date firstDate) {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
//		restrictions.addCriterion(Restrictions.isNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.ge("passToDealerPaymentDate", DateUtils.getDateAtBeginningOfDay(firstDate)));
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(firstDate)));
		
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
//		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(startDate)));
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		List<Payment> payments = entityService.list(restrictions);
		if (payments != null && !payments.isEmpty()) {
			return payments;		
		} else {
			return new ArrayList<Payment>();
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentService#getListPaymentPaidInCurrentMonth(java.lang.Long)
	 */
	@Override
	public List<Payment> getListPaymentPaidInCurrentMonth(Long conId) {
		PaymentRestriction restrictions = new PaymentRestriction();
		restrictions.setContractId(conId);
		restrictions.setPaymentFrom(DateUtils.getDateAtBeginningOfMonth(DateUtils.today()));
		restrictions.setPaymentTo(DateUtils.getDateAtEndOfMonth(DateUtils.today()));
		restrictions.setPaymentTypes(new EPaymentType[] { EPaymentType.IRC });
		List<EWkfStatus> wkfStatuses = new ArrayList<EWkfStatus>();
		wkfStatuses.add(PaymentWkfStatus.PAI);
		restrictions.setWkfStatusList(wkfStatuses);
		List<Payment> payments = list(restrictions);
		return payments;
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentService#getInstallmentChecked(com.nokor.efinance.core.dealer.model.Dealer, java.util.Date, java.util.Date)
	 */
	@Override
	public List<InstallmentChecked> getInstallmentChecked(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<InstallmentChecked> restrictions = new BaseRestrictions<InstallmentChecked>(InstallmentChecked.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("checkedDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("checkedDate", DateUtils.getDateAtEndOfDay(endDate)));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentService#getListPaymentsByContractID(java.lang.Long)
	 */
	@Override
	public List<Payment> getListPaymentsByContractID(Long conId, EPaymentType[] paymentTypes) {
		PaymentRestriction restrictions = new PaymentRestriction();
		restrictions.setContractId(conId);
		restrictions.setPaymentTypes(paymentTypes);
		return list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private SettingConfig getSettingConfigByCode(String code) {
		BaseRestrictions<SettingConfig> restrictions = new BaseRestrictions<>(SettingConfig.class);
		restrictions.addCriterion(Restrictions.eq("code", code));
		restrictions.addOrder(Order.asc("createDate"));
		List<SettingConfig> settingConfigs = list(restrictions);
		if (!settingConfigs.isEmpty()) {
			return settingConfigs.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param paymentType
	 * @return
	 */
	@Override
	public List<PaymentThirdParty> getPaymentThirdParty(PaymentType paymentType) {
		BaseRestrictions<PaymentThirdParty> restrictions = new BaseRestrictions<PaymentThirdParty>(PaymentThirdParty.class);
		restrictions.addCriterion(Restrictions.eq("paymentType", paymentType));
		return list(restrictions);
	}
}
