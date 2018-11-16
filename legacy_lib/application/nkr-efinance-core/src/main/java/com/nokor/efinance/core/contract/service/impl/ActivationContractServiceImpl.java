package com.nokor.efinance.core.contract.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.EntityAlreadyProcessedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.finwiz.share.domain.AP.PaymentMethod;
import com.gl.finwiz.share.domain.AP.PaymentOrderBatchDTO;
import com.gl.finwiz.share.domain.AP.PaymentOrderDTO;
import com.gl.finwiz.share.domain.AP.PaymentOrderItemDTO;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.core.address.model.AddressArc;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantArc;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.EmploymentArc;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualAddressArc;
import com.nokor.efinance.core.applicant.model.IndividualArc;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualContactInfoArc;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfoArc;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfoArc;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetArc;
import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.service.AssetService;
import com.nokor.efinance.core.collection.service.ContractOtherDataService;
import com.nokor.efinance.core.contract.dao.ContractDao;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.contract.model.ContractFinService;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ActivationContractService;
import com.nokor.efinance.core.contract.service.ContractApplicantRestriction;
import com.nokor.efinance.core.contract.service.ContractSequenceImpl;
import com.nokor.efinance.core.contract.service.ContractSimulationApplicantRestriction;
import com.nokor.efinance.core.contract.service.ContractSimulationRestriction;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.contract.service.DealerPaymentException;
import com.nokor.efinance.core.contract.service.JournalEntryException;
import com.nokor.efinance.core.contract.service.SequenceManager;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAttribute;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.InsuranceFinService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.efinance.core.issue.model.ContractIssue;
import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.exception.ValidationFields;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.third.efinance.ClientAccounting;
import com.nokor.efinance.third.finwiz.client.admin.ClientAdministration;
import com.nokor.efinance.third.finwiz.client.ap.ClientAP;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.efinance.third.finwiz.client.ins.ClientInsurance;
import com.nokor.efinance.third.finwiz.client.los.ClientLOSApplication;
import com.nokor.efinance.third.finwiz.client.reg.ClientRegistration;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.finance.services.tools.formula.Rate;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * Activation Contract Service Implementation
 * @author bunlong.taing
 */
@Service("activationContractService")
public class ActivationContractServiceImpl extends BaseEntityServiceImpl implements ActivationContractService, MContract {

	/** */
	private static final long serialVersionUID = -5241077153142416137L;
	
	protected Logger LOG = LoggerFactory.getLogger(ActivationContractServiceImpl.class);
	
	
	@Autowired
	private ContractDao dao;
	
	@Autowired
	private PaymentService paymentService;	
	@Autowired
	private ApplicantService applicantService;
	@Autowired
	private AssetService assetService;
	@Autowired
	private CashflowService cashflowService;
	@Autowired
	private FinHistoryService finHistoryService;
	@Autowired
	private FinanceCalculationService financeCalculationService;
	@Autowired
	private ContractOtherDataService contractOtherDataService;
	
	/**
	 * @param contract
	 * @throws ValidationFieldsException
	 */
	public void checkContract(Contract contract) throws ValidationFieldsException {
		ValidationFields validationFields = new ValidationFields();
		if (contract.getFirstDueDate() == null) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("first.due.date")));
		}
		Asset asset = contract.getAsset();
		if (asset.getColor() == null) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("marketing.color")));
		}
		if (StringUtils.isEmpty(asset.getChassisNumber())) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("chassis.no")));
		}
		if (StringUtils.isEmpty(asset.getEngineNumber())) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("engine.no")));
		}
		if (StringUtils.isEmpty(asset.getTaxInvoiceNumber())) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("tax.invoice.no")));
		}
		if (MyNumberUtils.getDouble(asset.getTiAssetPrice()) == 0) {
			validationFields.add(true, DomainType.CON, I18N.message("field.required.1", I18N.message("wholesale.price")));
		}
		if (!validationFields.getErrorMessages().isEmpty()) {
			throw new ValidationFieldsException(validationFields.getErrorMessages());
		}
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	@Override
	public Contract createContract(Quotation quotation) throws EntityAlreadyProcessedException {
		
		if (StringUtils.isNotEmpty(quotation.getExternalReference()) && isContractAlreadyProcessed(quotation.getExternalReference())) {
			throw new EntityAlreadyProcessedException(I18N.message("contract.already.processed"));
		}
		
		Contract contract = Contract.createInstance();
		FinProduct financialProduct = quotation.getFinancialProduct(); 
		ProductLine productLine = financialProduct.getProductLine();			
							
		
		contract.setProductLineType(EProductLineType.FNC);
		contract.setProductLine(productLine);
		contract.setFinancialProduct(financialProduct);
		contract.setCampaign(quotation.getCampaign());
		contract.setExternalReference(quotation.getReference());
		contract.setOriginBranch(quotation.getOriginBranch());
		contract.setBranchInCharge(quotation.getOriginBranch());
		
		Long sequence = SequenceManager.getInstance().getSequenceContract();
		
		String yearLabel = DateUtils.getDateLabel(DateUtils.today(), "yy");
		SequenceGenerator sequenceGenerator = new ContractSequenceImpl(contract.getFinancialProduct().getCode(), yearLabel, sequence); 
		contract.setReference(sequenceGenerator.generate());
		
		Applicant applicant = quotation.getApplicant();
		if (applicant != null && applicant.getId() == null) {
			applicantService.saveOrUpdateApplicant(applicant);
		}
		contract.setApplicant(applicant);
		
		Asset asset = quotation.getAsset();
		saveOrUpdate(asset);
		
		contract.setDealer(quotation.getDealer());		
		contract.setAsset(asset);
		
		contract.setApprovalDate(quotation.getAcceptationDate());
		contract.setQuotationDate(quotation.getQuotationDate());
		contract.setCreationDate(quotation.getActivationDate());
		contract.setInitialEndDate(contract.getEndDate());		
		contract.setWkfStatus(ContractWkfStatus.PEN);
		contract.setVatValue(quotation.getVatValue());
		contract.setTiAdvancePaymentAmount(quotation.getTiAdvancePaymentAmount());
		contract.setTeAdvancePaymentAmount(quotation.getTeAdvancePaymentAmount());
		contract.setVatAdvancePaymentAmount(quotation.getVatAdvancePaymentAmount());
		contract.setAdvancePaymentPercentage(quotation.getAdvancePaymentPercentage());		
		contract.setTiFinancedAmount(quotation.getTiFinanceAmount());
		contract.setTeFinancedAmount(quotation.getTeFinanceAmount());
		contract.setVatFinancedAmount(quotation.getVatFinanceAmount());		
		contract.setPenaltyRule(productLine.getPenaltyRule());		
		contract.setTerm(quotation.getTerm());
		contract.setInterestRate(quotation.getInterestRate());
		contract.setFrequency(quotation.getFrequency());
		contract.setNumberOfPrincipalGracePeriods(quotation.getNumberOfPrincipalGracePeriods());
		
		Amount installmentAmount = MyMathUtils.calculateFromAmountIncl(quotation.getTiInstallmentAmount(), quotation.getVatValue(), 2);			
		contract.setTeInstallmentAmount(MyMathUtils.roundAmountTo(installmentAmount.getTeAmount()));
		contract.setVatInstallmentAmount(MyMathUtils.roundAmountTo(installmentAmount.getVatAmount()));
		contract.setTiInstallmentAmount(MyMathUtils.roundAmountTo(installmentAmount.getTiAmount()));
				
		contract.setIrrRate(100 * Rate.calculateIRR(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()), 
				quotation.getTeInstallmentAmount(), quotation.getTiFinanceAmount()));
		
		contract.setCheckerID(quotation.getCheckerID());
		contract.setCheckerName(quotation.getCheckerName());
		contract.setCheckerPhoneNumber(quotation.getCheckerPhoneNumber());
				
		
		
		int numberGuarantors = 0;
		List<ContractApplicant> contractApplicants = new ArrayList<ContractApplicant>();
		if (quotation.getQuotationApplicants() != null) {
			for (QuotationApplicant quotationApplicant : quotation.getQuotationApplicants()) {
				Applicant guarantor = quotationApplicant.getApplicant();
				ContractApplicant contractApplicant = new ContractApplicant();
				contractApplicant.setApplicant(guarantor);
				contractApplicant.setContract(contract);
				contractApplicant.setApplicantType(quotationApplicant.getApplicantType());
				if (guarantor != null && guarantor.getId() == null) {
					applicantService.saveOrUpdateApplicant(guarantor);
				}
				saveOrUpdate(contractApplicant);
				if (quotationApplicant.getApplicantType().equals(EApplicantType.G)) {
					numberGuarantors++;
				}
				contractApplicants.add(contractApplicant);
			}
		}
		contract.setContractApplicants(contractApplicants);
		contract.setNumberGuarantors(numberGuarantors);
		
		LOG.debug(">> saveOrUpdate contract - Ref. [" + contract.getReference() + "]");
		saveOrUpdate(contract);
		LOG.debug("<< saveOrUpdate contract");
		
		List<QuotationService> quotationServices = quotation.getQuotationServices();
				
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				ContractFinService contractFinService = new ContractFinService();
				contractFinService.setService(quotationService.getService());
				contractFinService.setContract(contract);
				contractFinService.setVatValue(quotationService.getVatValue());
				contractFinService.setTePrice(quotationService.getTePrice());
				contractFinService.setVatPrice(quotationService.getVatPrice());
				contractFinService.setTiPrice(quotationService.getTiPrice());
				contractFinService.setChargePoint(quotationService.getChargePoint());
				saveOrUpdate(contractFinService);
			}
		}
		
		List<QuotationDocument> quotationDocuments = quotation.getQuotationDocuments();
		
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				ContractDocument contractDocument = new ContractDocument();
				contractDocument.setDocument(quotationDocument.getDocument());
				contractDocument.setContract(contract);
				contractDocument.setReference(quotationDocument.getReference());
				contractDocument.setPath(quotationDocument.getPath());
				contractDocument.setExpireDate(quotationDocument.getExpireDate());
				contractDocument.setIssueDate(quotationDocument.getIssueDate());
				saveOrUpdate(contractDocument);
			}
		}
		
				
		LOG.debug("<< activate contract");
		return contract;
	}
	
	/**
	 * 
	 * @param quotation
	 * @return
	 */
	private boolean isContractAlreadyProcessed(String externalReference) {
		// check duplicate external reference
		List<Contract> contracts = list(Contract.class, Restrictions.eq("externalReference", externalReference));
		return !contracts.isEmpty();
	}
		
	/**
	 * @see com.nokor.efinance.core.contract.service.ActivationContractService#complete(com.nokor.efinance.core.quotation.model.Quotation)
	 */
	@Override
	public Contract complete(Contract contract, boolean isForced, boolean disburse) throws DealerPaymentException, JournalEntryException {
		LOG.debug(">> activate contract");
		
		contract.setForceActivated(isForced);
		if (disburse) {
			contract.setWkfStatus(ContractWkfStatus.HOLD_PAY);
		} else {
			contract.setWkfStatus(ContractWkfStatus.FIN);
		}
		
		Applicant applicant = contract.getApplicant();
		ApplicantArc applicantArc = copyApplicant(applicant);
		applicantService.saveOrUpdateApplicantArc(applicantArc);
		
		if (contract.getStartDate() == null) {
			contract.setStartDate(DateUtils.todayH00M00S00());
		}
		contract.setSigatureDate(contract.getStartDate());
		contract.setInitialStartDate(contract.getStartDate());
		contract.setEndDate(DateUtils.addDaysDate(DateUtils.addMonthsDate(contract.getStartDate(), contract.getTerm() * contract.getFrequency().getNbMonths()), -1));
		contract.setInitialEndDate(contract.getEndDate());
		
		contract.setApplicantArc(applicantArc);
		contract.setApplicant(applicant);
		
		AssetArc assetArc = copyAsset(contract.getAsset());
		saveOrUpdate(assetArc);
		contract.setAsset(contract.getAsset());
		contract.setAssetArc(assetArc);
		
		DealerAttribute dealerAttribute = getDealerAttribute(contract.getDealer(), contract.getAsset().getAssetMake(), contract.getAsset().getModel().getAssetCategory());
		contract.setInsuranceCompany(dealerAttribute.getInsuranceCompany());
		
		saveOrUpdate(contract);
		
		List<Cashflow> cashflows = new ArrayList<>();
		
		ProductLine productLine = contract.getProductLine();
		Date firstDueDate = contract.getFirstDueDate();
		Date startDate = contract.getStartDate();

		int nbDecimal = 2;
		
		Cashflow cashflowFin = CashflowUtils.createCashflow(productLine,
				null, contract, contract.getVatValue(),
				ECashflowType.FIN, ETreasuryType.DEA, getByCode(JournalEvent.class, ECashflowType.FIN_JOURNAL_EVENT), productLine.getPaymentConditionFin(),
				-1 * contract.getTeFinancedAmount(), -1 * contract.getVatFinancedAmount(), -1 * contract.getTiFinancedAmount(),
				startDate, startDate, startDate, 0);
		cashflows.add(cashflowFin);
		
		List<ContractFinService> contractFinServices = contract.getContractFinServices();

		double vatValue = contract.getVatValue();		
		
		// Calculate installment amount from Financed amount Incl. VAT
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setInitialPrincipal(contract.getTiFinancedAmount());
		calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
		calculationParameter.setPeriodicInterestRate(contract.getInterestRate() / 100d);
		calculationParameter.setFrequency(contract.getFrequency());
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(contract.getNumberOfPrincipalGracePeriods()));		
		Amount installmentAmount = MyMathUtils.calculateFromAmountIncl(financeCalculationService.getInstallmentPayment(calculationParameter), vatValue, nbDecimal);
				
		calculationParameter.setInstallmentAmount(installmentAmount.getTiAmount());
		
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(startDate, firstDueDate, calculationParameter);
		
		List<Schedule> schedules = amortizationSchedules.getSchedules();
		double totalPrincipalAmount = 0d;
		
		JournalEvent capJournalEvent = getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT);
		JournalEvent iapJournalEvent = getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT);
		
		for (int i = 0; i < schedules.size(); i++) {
			Schedule schedule = schedules.get(i);
			double tiPrincipalAmount = MyMathUtils.roundTo(schedule.getPrincipalAmount(), nbDecimal);
			if (i == schedules.size() - 1) {
				tiPrincipalAmount = MyMathUtils.roundTo(contract.getTiFinancedAmount() - totalPrincipalAmount, nbDecimal);
			}
			
			Amount principalAmount = MyMathUtils.calculateFromAmountIncl(tiPrincipalAmount, vatValue, nbDecimal);
			
			if (tiPrincipalAmount > 0) {
				Cashflow cashflowCap = CashflowUtils.createCashflow(productLine,
						null, contract, contract.getVatValue(),
						ECashflowType.CAP, ETreasuryType.APP, capJournalEvent, productLine.getPaymentConditionCap(),
						principalAmount.getTeAmount(), principalAmount.getVatAmount(), principalAmount.getTiAmount(),
						schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflows.add(cashflowCap);
			}
			
			totalPrincipalAmount += tiPrincipalAmount;
			
			if (schedule.getInterestAmount() > 0) {
				Amount interestAmount = MyMathUtils.calculateFromAmountIncl(installmentAmount.getTiAmount() - tiPrincipalAmount, vatValue, nbDecimal);
				
				Cashflow cashflowIap = CashflowUtils.createCashflow(productLine,
					null, contract, contract.getVatValue(),
					ECashflowType.IAP, ETreasuryType.APP, iapJournalEvent, productLine.getPaymentConditionIap(),
					interestAmount.getTeAmount(), interestAmount.getVatAmount(), interestAmount.getTiAmount(),
					schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflows.add(cashflowIap);
			}
		}
		
		if (contractFinServices != null && !contractFinServices.isEmpty()) {
			for (ContractFinService contractFinService : contractFinServices) {
				
				Date installmentDate = null;
				Date periodStartDate = startDate;
				Date periodEndDate = startDate;
				Integer numInstallment = null;
				
				if (EChargePoint.PRI_ASS.equals(contractFinService.getChargePoint())) {
					installmentDate = startDate;
					numInstallment = 0;
				} else {
					installmentDate = DateUtils.getDateAtEndOfMonth(startDate);
					numInstallment = null;
				}
				
				int sign = EServiceType.getSign(contractFinService.getService().getServiceType());
				Cashflow cashflowFee = CashflowUtils.createCashflow(productLine,
						null, contract, contractFinService.getVatValue(),
						EServiceType.isService(contractFinService.getService().getServiceType()) ? ECashflowType.SRV : ECashflowType.FEE, contractFinService.getService().getTreasuryType(), contractFinService.getService().getJournalEvent(),
						productLine.getPaymentConditionFee(),
						sign * contractFinService.getTePrice(), sign * contractFinService.getVatPrice(), sign * contractFinService.getTiPrice(),
						installmentDate, periodStartDate, periodEndDate, numInstallment);
				cashflowFee.setService(contractFinService.getService());
				cashflows.add(cashflowFee);						
				
			}
		}
				
		Collections.sort(cashflows, new CashflowComparatorByInstallmentDate());
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getNumInstallment() == null) {
				cashflow.setNumInstallment(getNumInstallment(cashflow.getInstallmentDate(), cashflows));
			}
			LOG.debug(">> saveOrUpdate cashflow");
			saveOrUpdate(cashflow);
			LOG.debug("<< saveOrUpdate cashflow");
		}
		
		// Create a down payment
		paymentService.createDownPayment(contract, DateUtils.today());
		
		//-----------------------------------------------------------------------------
		// Prepaid Payment
		Integer numPrepaidTerm = contract.getNumberPrepaidTerm();
		if (numPrepaidTerm != null && numPrepaidTerm > 0) {
			Payment payment = createPayment(contract, cashflowService.getCashflowsToPaid(contract.getId(), numPrepaidTerm));
			paymentService.createPayment(payment);
		}
		
		contractOtherDataService.calculateOtherDataContract(contract);
		
		createJournalEntries(contract);
		
		// Call asynchronous WS Ins and WS Reg 
		// new Thread(() -> ClientRegistration.createRegistrationTask(contract.getReference())).start();
		
		ClientRegistration.createRegistrationTask(contract.getReference(), ClientRegistration.NEW);
		
		// Call asynchronous WS Ins
		Organization insuranceCompany = contract.getInsuranceCompany();
				
		int period = MyNumberUtils.getInteger(dealerAttribute.getInsuranceCoverageDuration());
				
		InsuranceFinService isrFinSrv = getInsuranceFinSrv(insuranceCompany, contract.getAsset().getModel());
		double sumInsure1 = 0d;
		double sumInsure2 = 0d;
		double premiumNet = 0d;
		double premiumTotal = 0d;
		if (isrFinSrv != null) {
			if (period == 1) {
				sumInsure1 = MyNumberUtils.getDouble(isrFinSrv.getClaimAmount1Y());
				sumInsure2 = 0d;
				
				premiumNet = MyNumberUtils.getDouble(isrFinSrv.getPremium1Y());
				premiumTotal = MyNumberUtils.getDouble(isrFinSrv.getPremium1Y());
			} else if (period == 2) {
				sumInsure1 = MyNumberUtils.getDouble(isrFinSrv.getClaimAmount2YFirstYear());
				sumInsure2 = MyNumberUtils.getDouble(isrFinSrv.getClaimAmount2YSecondYear());
				
				premiumNet = MyNumberUtils.getDouble(isrFinSrv.getPremium2Y());
				premiumTotal = MyNumberUtils.getDouble(isrFinSrv.getPremium2Y());
			}
		}
		
		ClientInsurance.createInsuranceTask(contract.getReference(), 
				contract.getUpdateUser(), insuranceCompany.getId(), period,
				new BigDecimal(sumInsure1), new BigDecimal(sumInsure2), new BigDecimal(premiumNet), new BigDecimal(premiumTotal));
		
		// Update application to in-active
		ClientLOSApplication.updateUnActiveApplication(ContractUtils.getApplicationID(contract));
		
		String loginUser = UserSessionManager.getCurrentUser() == null ? "test" : UserSessionManager.getCurrentUser().getLogin();
		
		ClientAdministration.createAdminstrationWelcomeTask(contract.getReference(), loginUser);
		
		String desc = I18N.message("msg.contract.activated", new String[] {contract.getReference(), loginUser});
		finHistoryService.addFinHistory(contract, FinHistoryType.FIN_HIS_SYS, desc);
		
		LOG.debug("<< activate contract");
		return contract;
	}
	
	/**
	 * 
	 * @param isrComId
	 * @return
	 */
	private InsuranceFinService getInsuranceFinSrv(Organization insuranceCompany, AssetModel assetModel) {
		BaseRestrictions<InsuranceFinService> restrictions = new BaseRestrictions<>(InsuranceFinService.class);
		restrictions.addCriterion(Restrictions.eq("insurance.id", insuranceCompany.getId()));
		restrictions.addCriterion(Restrictions.eq("assetModel.id", assetModel.getId()));
		restrictions.addOrder(Order.desc(InsuranceFinService.ID));
		if (list(restrictions) != null && !list(restrictions).isEmpty()) {
			return list(restrictions).get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param dealer
	 * @param assetMake
	 * @param assetCategory
	 * @return
	 */
	private DealerAttribute getDealerAttribute(Dealer dealer, AssetMake assetMake, AssetCategory assetCategory) {
		List<DealerAttribute> dealerAttributes = null;
		if (dealer != null) {
			dealerAttributes = dealer.getDealerAttributes();
		}
		if (dealerAttributes != null && !dealerAttributes.isEmpty()) {
			for (DealerAttribute dealerAttribute : dealerAttributes) {
				if (dealerAttribute.getAssetMake().getId().equals(assetMake.getId()) 
						&& dealerAttribute.getAssetCategory().getId().equals(assetCategory.getId())) {
					return dealerAttribute;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param contract
	 * @param cashflows
	 * @return
	 */
	private Payment createPayment(Contract contract, List<Cashflow> cashflows) {
		Payment payment = new Payment();
		payment.setApplicant(contract.getApplicant());
		payment.setContract(contract);
		payment.setPaymentDate(DateUtils.today());
		payment.setPaymentMethod(EPaymentMethod.CASH);
		payment.setWkfStatus(PaymentWkfStatus.PAI);
		payment.setPaymentType(EPaymentType.IRC);
		payment.setCashflows(cashflows);
		return payment;
	}
	
	/**
	 * Send Payment to AP module
	 * @param contract
	 */
	private void createJournalEntries(Contract contract) throws JournalEntryException {
		try {
			// Down payment
			
			double tiDownPayment = (contract.getTiInvoiceAmount() == null ? contract.getAsset().getTiAssetPrice() : contract.getTiInvoiceAmount()) - contract.getTiFinancedAmount();
			double teDownPayment = (contract.getTiInvoiceAmount() == null ? contract.getAsset().getTeAssetPrice() : contract.getTeInvoiceAmount()) - contract.getTeFinancedAmount();
			double vatDownPayment = (contract.getTiInvoiceAmount() == null ? contract.getAsset().getVatAssetPrice() : contract.getVatInvoiceAmount()) - contract.getVatFinancedAmount();
			
			ClientAccounting.createPayment(contract.getReference(), getByCode(JournalEvent.class, "73").getDescEn(), contract.getStartDate(), "73", 
					new BigDecimal(tiDownPayment),
					new BigDecimal(teDownPayment),
					new BigDecimal(vatDownPayment));
		
			// Service fee/ Contract fee
			Amount serviceFeeAmount = contract.getServiceAmount(EServiceType.SRVFEE);
			if (serviceFeeAmount != null) {
				ClientAccounting.createPayment(contract.getReference(), getByCode(JournalEvent.class, "79").getDescEn(), contract.getStartDate(), "79", 
					new BigDecimal(serviceFeeAmount.getTiAmount()),
					new BigDecimal(serviceFeeAmount.getTeAmount()),
					new BigDecimal(serviceFeeAmount.getVatAmount()));
			}
			
			// Purchase journal
			ClientAccounting.createPayment(contract.getReference(), getByCode(JournalEvent.class, "P0101").getDescEn(), contract.getStartDate(), "P0101", 
					new BigDecimal(contract.getLoanAmount().getTeAmount()),
					new BigDecimal(contract.getVatInvoiceAmount()),
					new BigDecimal((contract.getTiInvoiceAmount() == null ? contract.getAsset().getTeAssetPrice() : contract.getTeInvoiceAmount()) - contract.getTeFinancedAmount()),
					new BigDecimal(tiDownPayment + (serviceFeeAmount != null ? serviceFeeAmount.getTiAmount() : 0d)),
					new BigDecimal(contract.getGrossInterestAmount().getTeAmount()),
					new BigDecimal(contract.getTiFinancedAmount() - (serviceFeeAmount != null ? serviceFeeAmount.getTiAmount() : 0d))					
					);
			
			Amount commissionAmount = contract.getServiceAmount(EServiceType.COMM);
			if (commissionAmount != null) {
				ClientAccounting.createPayment(contract.getReference(), getByCode(JournalEvent.class, "P0102").getDescEn(), contract.getStartDate(), "P0102", 
					new BigDecimal(commissionAmount.getTeAmount()),
					new BigDecimal(commissionAmount.getVatAmount()),
					new BigDecimal(commissionAmount.getTiAmount()));
			}
			
			Amount insuranceFeeAmount = contract.getServiceAmount(EServiceType.INSFEE);
			if (insuranceFeeAmount != null) {
				ClientAccounting.createPayment(contract.getReference(), getByCode(JournalEvent.class, "P0103").getDescEn(), contract.getStartDate(), "P0103", 
					new BigDecimal(insuranceFeeAmount.getTeAmount()),
					new BigDecimal(insuranceFeeAmount.getVatAmount()),
					new BigDecimal(insuranceFeeAmount.getTiAmount()));
			}
			
		} catch (Exception e) {
			LOG.error("Create Accoutning Journal Entry", e);
			throw new JournalEntryException("Create not create Journal Entry", e);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ActivationContractService#sendPaymentToAP(java.util.List)
	 */
	@Override
	public void sendPaymentToAP(java.util.List<Long> paymentIds) throws DealerPaymentException {
		if (paymentIds != null && !paymentIds.isEmpty()) {
			Map<String, List<Payment>> groupPayments = getGroupPayment(paymentIds);
			List<PaymentOrderDTO> orderDTOs = new ArrayList<PaymentOrderDTO>();
			for (Iterator<String> iter = groupPayments.keySet().iterator(); iter.hasNext();) {
				List<Payment> payments = groupPayments.get(iter.next());
				if (!payments.isEmpty()) {
					List<PaymentOrderItemDTO> orderItemDTOs = new ArrayList<PaymentOrderItemDTO>();
					for (Payment payment : payments) {
						List<Cashflow> cashflows = payment.getCashflows();
						double totalAmount = 0d;
						if (cashflows != null && !cashflows.isEmpty()) {
							
							Cashflow financed = cashflowService.getTotalFinancedCashflow(cashflows);
							Cashflow commission = cashflowService.getTotalCommissionCashflow(cashflows);
							
							List<Cashflow> lstCashflow = new ArrayList<Cashflow>();
							if (financed != null) {
								lstCashflow.add(financed);
							}
							if (commission != null) {
								lstCashflow.add(commission);
							}
							
							for (Cashflow cashflow : lstCashflow) {								
								if (cashflow.getJournalEvent() == null) {
									throw new DealerPaymentException("Could not create payment to order batch, payment code is not configured for " + cashflow.getCashflowType().getDesc());
								}
								
								double vatValue = 0d;
								if (cashflow.getContract() != null) {
									vatValue = cashflow.getContract().getVatValue();
								}
								PaymentOrderItemDTO orderItemDTO = new PaymentOrderItemDTO();
								orderItemDTO.setAmountIncludedVat(new BigDecimal(MyMathUtils.roundTo(Math.abs(cashflow.getTiInstallmentAmount()), 2)), new BigDecimal(MyMathUtils.roundTo(vatValue, 2)));
								orderItemDTO.setContractNo(cashflow.getContract() == null ? StringUtils.EMPTY : cashflow.getContract().getReference());
								
								JournalEventDTO journalEvent = toJournalEventDTO(cashflow.getJournalEvent());
								if (journalEvent != null) {
									orderItemDTO.setPaymentCode(journalEvent);
									orderItemDTO.setPaymentCodeDesc(journalEvent.getDesc());
								}
								orderItemDTOs.add(orderItemDTO);
								totalAmount += Math.abs(cashflow.getTiInstallmentAmount());
							}
						}
						PaymentOrderDTO orderDTO = new PaymentOrderDTO();
						
						EJournalEventGroup journalEventGroup = EJournalEventGroup.getById(1l);
						if (journalEventGroup == null) {
							throw new DealerPaymentException("Could not create payment to order batch, payment type is cannot be null");
						}
						RefDataDTO paymentTypeRefData = toRefDataDTO(journalEventGroup);
						orderDTO.setPaymentType(paymentTypeRefData);
						
						orderDTO.setPaymentTypeDesc(paymentTypeRefData == null ? StringUtils.EMPTY : paymentTypeRefData.getDesc());
						orderDTO.setTotalAmount(new BigDecimal(MyMathUtils.roundTo(totalAmount, 2)));
						orderDTO.setPayeeAccountHolder(ClientAccountHolder.getAccountHolderById(payment.getPayeeAccountHolder()));
						if (EPaymentMethod.CHEQUE.equals(payment.getPaymentMethod())) {
							orderDTO.setPaymentMethod(PaymentMethod.CHEQUE);
						} else if (EPaymentMethod.TRANSFER.equals(payment.getPaymentMethod())) {
							orderDTO.setPaymentMethod(PaymentMethod.TRANSFER);
							orderDTO.setPayeeBankAccount(ClientBankAccount.getBankAccountById(payment.getPayeeBankAccount()));
						}
						orderDTO.setPaymentOrderItems(orderItemDTOs);
						orderDTOs.add(orderDTO);
					}
				}
			}
			PaymentOrderBatchDTO orderBatchDTO = new PaymentOrderBatchDTO();
			orderBatchDTO.setPaymentOrders(orderDTOs);
			try {
				orderBatchDTO = ClientAP.createPaymentOrderBatchs(orderBatchDTO);
				if (orderBatchDTO != null) {
					for (Long id : paymentIds) {
						Payment payment = paymentService.getById(Payment.class, id);
						payment.setWkfStatus(PaymentWkfStatus.VAL);
						update(payment);
					}
					String loginUser = UserSessionManager.getCurrentUser() == null ? "test" : UserSessionManager.getCurrentUser().getLogin();
					ClientAP.approvePaymentOrderBatch(loginUser, orderBatchDTO.getId(), true);
					ClientAP.approvePaymentOrderBatch(loginUser, orderBatchDTO.getId(), false);
				} else {
					throw new DealerPaymentException("Could not create payment to order batch");
				}
			} catch (Exception e) {
				LOG.error("Send AP", e);
				throw new DealerPaymentException("Could not create payment to dealer", e);
			}
		}
	};
	
	/**
	 * 
	 * @param refData
	 * @return
	 */
	private RefDataDTO toRefDataDTO(BaseERefData refData) {
		if (refData != null) {
			RefDataDTO refDataDTO = new RefDataDTO();
			refDataDTO.setId(refData.getId());
			refDataDTO.setCode(refData.getCode());
			refDataDTO.setDesc(refData.getDesc());
			refDataDTO.setDescEn(refData.getDescEn());
			refDataDTO.setIsActive(refData.isActive());
			refDataDTO.setSortIndex(refData.getSortIndex());
			return refDataDTO;
		}
		return null;
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	protected JournalEventDTO toJournalEventDTO(JournalEvent event) {
		JournalEventDTO eventDTO = new JournalEventDTO();
	
		eventDTO.setId(event.getId());
		eventDTO.setCode(event.getCode());
		eventDTO.setDesc(event.getDesc());
		eventDTO.setDescEn(event.getDescEn());
		eventDTO.setSortIndex(event.getSortIndex());
		eventDTO.setIsActive(event.isActive());
		eventDTO.setEventGroupId(event.getEventGroup() != null ? event.getEventGroup().getId() : null);
		
		return eventDTO;
	}
	
	/**
	 * 
	 * @param paymentIds
	 * @return
	 */
	private Map<String, List<Payment>> getGroupPayment(List<Long> paymentIds) {
		List<Payment> payments = new ArrayList<Payment>();
		Map<String, List<Payment>> mapPaymentGroups = new HashMap<String, List<Payment>>();
		for (Long payId : paymentIds) {
			Payment payment = paymentService.getById(Payment.class, payId);
			String code = payment.getDealer().getId() + "-" + payment.getPaymentMethod().getCode();
			if (!mapPaymentGroups.containsKey(code)) {
				payments.add(payment);
				mapPaymentGroups.put(code, payments);
			} else {
				payments = mapPaymentGroups.get(code);
				payments.add(payment);
			}
		}
		return mapPaymentGroups;
	}
	
	/**
	 * @param contract
	 */
	public void unholdDealerPayment(Contract contract) throws DealerPaymentException {
		contract.setWkfStatus(ContractWkfStatus.FIN);
		saveOrUpdate(contract);
//		userInboxService.deleteContractFromInbox(contract);
//		sentPaymentToAP(contract);
	}
	
	/**
	 * Get Number Installment
	 * @param installmenDate
	 * @param cashflows
	 * @return
	 */
	private Integer getNumInstallment(Date installmenDate, List<Cashflow> cashflows) {
		for (Cashflow cashflow : cashflows) {
			if (DateUtils.getDateAtBeginningOfDay(installmenDate).compareTo(DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate())) == 0
				&& cashflow.getNumInstallment() != null) {
				return cashflow.getNumInstallment();
			}
		}
		return null;
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class CashflowComparatorByInstallmentDate implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			Cashflow c1 = (Cashflow) o1;
			Cashflow c2 = (Cashflow) o2;
			if (c1 == null || c1.getInstallmentDate() == null) {
				if (c2 == null || c2.getInstallmentDate() == null) {
					return 0;
				}
				return -1;
			}
			if (c2 == null || c2.getInstallmentDate() == null) {
				return -1;
			}
			return c1.getInstallmentDate().compareTo(c2.getInstallmentDate());
		}
	}
		
	/**
	 * Copy Asset
	 * @param asset
	 * @return
	 */
	private AssetArc copyAsset(Asset asset) {
		AssetArc assArc = new AssetArc();
		assArc.setSerie(asset.getSerie());
		assArc.setColor(asset.getColor());
		assArc.setYear(asset.getYear());
		assArc.setEngine(asset.getEngine());
		assArc.setAssetGender(asset.getAssetGender());
		assArc.setTiAssetPrice(asset.getTiAssetPrice());
		assArc.setTeAssetPrice(asset.getTeAssetPrice());
		assArc.setVatAssetPrice(asset.getVatAssetPrice());
		assArc.setVatValue(asset.getVatValue());
		assArc.setGrade(asset.getGrade());
		assArc.setModel(asset.getModel());
		assArc.setRegistrationDate(asset.getRegistrationDate());
		assArc.setRegistrationProvince(asset.getRegistrationProvince());
		assArc.setRegistrationPlateType(asset.getRegistrationPlateType());
		assArc.setRegistrationBookStatus(asset.getRegistrationBookStatus());
		assArc.setPlateNumber(asset.getPlateNumber());
		assArc.setChassisNumber(asset.getChassisNumber());
		assArc.setEngineNumber(asset.getEngineNumber());
		assArc.setModelUsed(asset.isModelUsed());
		assArc.setMileage(asset.getMileage());
		assArc.setRiderName(asset.getRiderName());
		
		assArc.setCode(asset.getCode());
		assArc.setDesc(asset.getDesc());
		assArc.setDescEn(asset.getDescEn());
		
		return assArc;
	}
	
	/**
	 * Copy Applicant to Applicant Arc
	 * @param applicant
	 * @return
	 */
	private ApplicantArc copyApplicant(Applicant applicant) {
		ApplicantArc appArc = new ApplicantArc();
		appArc.setApplicantCategory(applicant.getApplicantCategory());

		Individual individual = applicant.getIndividual();
		IndividualArc individualArc = IndividualArc.createInstance();
		
		individualArc.setFirstName(individual.getFirstName());
		individualArc.setFirstNameEn(individual.getFirstNameEn());
		individualArc.setLastName(individual.getLastName());
		individualArc.setLastNameEn(individual.getLastNameEn());
		individualArc.setNickName(individual.getNickName());
		individualArc.setTypeIdNumber(individual.getTypeIdNumber());
		individualArc.setIdNumber(individual.getIdNumber());
		individualArc.setIssuingIdNumberDate(individual.getIssuingIdNumberDate());
		individualArc.setExpiringIdNumberDate(individual.getExpiringIdNumberDate());
		individualArc.setTitle(individual.getTitle());
		individualArc.setCivility(individual.getCivility());
		individualArc.setBirthDate(individual.getBirthDate());
		individualArc.setPlaceOfBirth(individual.getPlaceOfBirth());
		individualArc.setGender(individual.getGender());
		individualArc.setMaritalStatus(individual.getMaritalStatus());
		individualArc.setNationality(individual.getNationality());
		individualArc.setMobilePerso(individual.getMobilePerso());
		individualArc.setEmailPerso(individual.getEmailPerso());
		individualArc.setPhoto(individual.getPhoto());
		
		individualArc.setReference(individual.getReference());
		individualArc.setOtherNationality(individual.getOtherNationality());
		individualArc.setNumberOfChildren(individual.getNumberOfChildren());
		individualArc.setNumberOfHousehold(individual.getNumberOfHousehold());
		individualArc.setReligion(individual.getReligion());
		individualArc.setEducation(individual.getEducation());
		individualArc.setSecondLanguage(individual.getSecondLanguage());
		individualArc.setMonthlyPersonalExpenses(individual.getMonthlyPersonalExpenses());
		individualArc.setMonthlyFamilyExpenses(individual.getMonthlyFamilyExpenses());
		individualArc.setDebtFromOtherSource(individual.isDebtFromOtherSource());
		individualArc.setTotalDebtInstallment(individual.getTotalDebtInstallment());
		individualArc.setGuarantorOtherLoan(individual.isGuarantorOtherLoan());
		individualArc.setConvenientVisitTime(individual.getConvenientVisitTime());
		individualArc.setHouseholdExpenses(individual.getHouseholdExpenses());
		individualArc.setHouseholdIncome(individual.getHouseholdIncome());
		individualArc.setTotalFamilyMember(individual.getTotalFamilyMember());
		
		List<IndividualAddressArc> individualAddressArcs = copyIndividualAddressArcs(individual.getIndividualAddresses());
		if (individualAddressArcs != null && !individualAddressArcs.isEmpty()) {
			individualArc.setIndividualAddresses(individualAddressArcs);
		}
		
		List<EmploymentArc> employmentArcs = copyEmploymentArcs(individual.getEmployments());
		if (employmentArcs != null && !employmentArcs.isEmpty()) {
			individualArc.setEmployments(employmentArcs);
		}
		
		List<IndividualContactInfoArc> individualContactInfoArcs = copyIndividualContactInfoArcs(individual.getIndividualContactInfos());
		if (individualContactInfoArcs != null && !individualContactInfoArcs.isEmpty()) {
			individualArc.setIndividualContactInfos(individualContactInfoArcs);
		}
		
		List<IndividualReferenceInfoArc> applicantReferenceInfoArcs = copyIndividualReferenceInfoArcs(individual.getIndividualReferenceInfos());
		if (applicantReferenceInfoArcs != null && !applicantReferenceInfoArcs.isEmpty()) {
			individualArc.setIndividualReferenceInfos(applicantReferenceInfoArcs);
		}
		appArc.setIndividual(individualArc);
		return appArc;
	}
	
	/**
	 * Copy Applicant Reference Info Arc
	 * @param individualReferenceInfos
	 * @return
	 */
	private List<IndividualReferenceInfoArc> copyIndividualReferenceInfoArcs(List<IndividualReferenceInfo> individualReferenceInfos) {
		List<IndividualReferenceInfoArc> individualReferenceInfoArcs = null;
		if (individualReferenceInfos != null && !individualReferenceInfos.isEmpty()) {
			individualReferenceInfoArcs = new ArrayList<>();
			for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
				IndividualReferenceInfoArc individualReferenceInfoArc = copyIndividualReferenceInfo(individualReferenceInfo);
				individualReferenceInfoArcs.add(individualReferenceInfoArc);
			}
		}
		return individualReferenceInfoArcs;
	}
	
	/**
	 * Copy Applicant Reference Info
	 * @param individualReferenceInfo
	 * @return
	 */
	private IndividualReferenceInfoArc copyIndividualReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
		IndividualReferenceInfoArc individualReferenceInfoArc = new IndividualReferenceInfoArc();
		individualReferenceInfoArc.setReferenceType(individualReferenceInfo.getReferenceType());
		individualReferenceInfoArc.setRelationship(individualReferenceInfo.getRelationship());
		individualReferenceInfoArc.setLastNameEn(individualReferenceInfo.getLastNameEn());
		individualReferenceInfoArc.setFirstNameEn(individualReferenceInfo.getFirstNameEn());
		List<IndividualReferenceContactInfoArc> individualReferenceContactInfoArc = copyIndividualReferenceContactInfoArcs(individualReferenceInfo.getIndividualReferenceContactInfos());
		if (individualReferenceContactInfoArc != null && !individualReferenceContactInfoArc.isEmpty()) {
			individualReferenceInfoArc.setIndividualReferenceContactInfos(individualReferenceContactInfoArc);
		}
		return individualReferenceInfoArc;
	}
	
	/**
	 * Copy Applicant Reference Contact Info Arcs
	 * @param individualReferenceContactInfos
	 * @return
	 */
	private List<IndividualReferenceContactInfoArc> copyIndividualReferenceContactInfoArcs(List<IndividualReferenceContactInfo> individualReferenceContactInfos) {
		List<IndividualReferenceContactInfoArc> individualReferenceContactInfoArcs = null;
		if (individualReferenceContactInfos != null && !individualReferenceContactInfos.isEmpty()) {
			individualReferenceContactInfoArcs = new ArrayList<>();
			for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
				IndividualReferenceContactInfoArc individualReferenceContactInfoArc = new IndividualReferenceContactInfoArc();
				individualReferenceContactInfoArc.setContactInfo(copyContactInfo(individualReferenceContactInfo.getContactInfo()));
				individualReferenceContactInfoArcs.add(individualReferenceContactInfoArc);
			}
		}
		return individualReferenceContactInfoArcs;
	}
	
	/**
	 * Copy Applicant Contact Info Arc
	 * @param individualContactInfos
	 * @return
	 */
	private List<IndividualContactInfoArc> copyIndividualContactInfoArcs(List<IndividualContactInfo> individualContactInfos) {
		List<IndividualContactInfoArc> individualContactInfoArcs = null;
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			individualContactInfoArcs = new ArrayList<>();
			for (IndividualContactInfo individualContactInfo : individualContactInfos) {
				IndividualContactInfoArc individualContactInfoArc = copyIndividualContactInfo(individualContactInfo);
				individualContactInfoArcs.add(individualContactInfoArc);
			}
		}
		return individualContactInfoArcs;
	}
	
	/**
	 * Copy Applicant Contact Info
	 * @param individualContactInfo
	 * @return
	 */
	private IndividualContactInfoArc copyIndividualContactInfo(IndividualContactInfo individualContactInfo) {
		IndividualContactInfoArc individualContactInfoArc = new IndividualContactInfoArc();
		individualContactInfoArc.setContactInfo(copyContactInfo(individualContactInfo.getContactInfo()));
		return individualContactInfoArc;
	}
	
	/**
	 * Copy Contact Info
	 * @return
	 */
	private ContactInfo copyContactInfo(ContactInfo contactInfo) {
		ContactInfo contactInfoArc = new ContactInfo();
		contactInfoArc.setTypeInfo(contactInfo.getTypeInfo());
		contactInfoArc.setTypeAddress(contactInfo.getTypeAddress());
		contactInfoArc.setValue(contactInfo.getValue());
		return contactInfoArc;
	}
	
	/**
	 * Copy Employment Arcs
	 * @param employments
	 * @return
	 */
	private List<EmploymentArc> copyEmploymentArcs(List<Employment> employments) {
		List<EmploymentArc> employmentArcs = null;
		if (employments != null && !employments.isEmpty()) {
			employmentArcs = new ArrayList<EmploymentArc>();
			for (Employment employment : employments) {
				EmploymentArc employmentArc = copyEmployment(employment);
				employmentArcs.add(employmentArc);
			}
		}
		return employmentArcs;
	}
	
	/**
	 * Copy Employment
	 * @param employment
	 * @return
	 */
	private EmploymentArc copyEmployment(Employment employment) {
		EmploymentArc empArc = new EmploymentArc();
		
		empArc.setPosition(employment.getPosition());
		empArc.setEmployerName(employment.getEmployerName());
		empArc.setTimeWithEmployerInYear(employment.getTimeWithEmployerInYear());
		empArc.setTimeWithEmployerInMonth(employment.getTimeWithEmployerInMonth());
		empArc.setRevenue(employment.getRevenue());
		empArc.setAllowance(employment.getAllowance());
		empArc.setBusinessExpense(employment.getBusinessExpense());
		empArc.setNoMonthInYear(employment.getNoMonthInYear());
		empArc.setEmploymentStatus(employment.getEmploymentStatus());
		empArc.setEmploymentIndustry(employment.getEmploymentIndustry());
		empArc.setEmploymentType(employment.getEmploymentType());
		empArc.setEmploymentCategory(employment.getEmploymentCategory());
		empArc.setLegalForm(employment.getLegalForm());
		empArc.setAllowCallToWorkPlace(employment.isAllowCallToWorkPlace());
		empArc.setSameApplicantAddress(employment.isSameApplicantAddress());
		empArc.setWorkPhone(employment.getWorkPhone());
		empArc.setSeniorityLevel(employment.getSeniorityLevel());
		
		if (employment.getAddress() != null) {
			empArc.setAddress(copyAddress(employment.getAddress()));
		}
		return empArc;
	}
	
	/**
	 * Copy Applicant Address Arcs from quotation's applicantAddresses
	 * @param individualAddresses
	 * @return
	 */
	private List<IndividualAddressArc> copyIndividualAddressArcs(List<IndividualAddress> individualAddresses) {
		List<IndividualAddressArc> individualAddressArcs = null;
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			individualAddressArcs = new ArrayList<>();
			for (IndividualAddress individualAddress : individualAddresses) {
				IndividualAddressArc individualAddressArc = new IndividualAddressArc();
				individualAddressArc.setAddress(copyAddress(individualAddress.getAddress()));
				individualAddressArcs.add(individualAddressArc);
			}
		}
		return individualAddressArcs;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private AddressArc copyAddress(Address address) {
		AddressArc addArc = new AddressArc();
		addArc.setHouseNo(address.getHouseNo());
		addArc.setStreet(address.getStreet());
		addArc.setCountry(address.getCountry());
		addArc.setProvince(address.getProvince());
		addArc.setCommune(address.getCommune());
		addArc.setDistrict(address.getDistrict());
		addArc.setVillage(address.getVillage());
		addArc.setProperty(address.getProperty());
		addArc.setTimeAtAddressInMonth(address.getTimeAtAddressInMonth());
		addArc.setTimeAtAddressInYear(address.getTimeAtAddressInYear());
		addArc.setStatusRecord(EStatusRecord.ACTIV);
		addArc.setType(address.getType());
		return addArc;
	}
	
	/**
	 * @param firstDueDate
	 * @param chassisNo
	 * @param engineNo
	 * @param assetPrice
	 * @param conId
	 * @return
	 */
	public List<String> validation(Contract contract, Date firstDueDate, String chassisNo, String engineNo, String taxInvoiceNumber) {
		List<String> errors = new ArrayList<>();
		
		int day = DateUtils.getDay(firstDueDate);
		
		if (day > 20) {
			errors.add(I18N.message("first.due.date.should.less.than.20.th"));
		}
		
		if (StringUtils.isEmpty(chassisNo)) {
			errors.add(I18N.message("field.required.1", I18N.message("chassis.no")));
		}
		if (StringUtils.isEmpty(engineNo)) {
			errors.add(I18N.message("field.required.1", I18N.message("engine.no")));
		}
		if (StringUtils.isEmpty(taxInvoiceNumber)) {
			errors.add(I18N.message("field.required.1", I18N.message("tax.invoice.no")));
		}
		if (assetService.isChassisNumberExist(chassisNo, contract.getAsset())) {
			errors.add(I18N.message("chassis.already.existed", new String[] {chassisNo}));
		}
		
		if (assetService.isEnginNumberExist(engineNo, contract.getAsset())) {
			errors.add(I18N.message("engine.already.existed", new String[] {engineNo}));
		}

		if (contract.getDealer() == null) {
			errors.add(I18N.message("dealer.not.fond"));
		} else {
			
		}
		
		DealerAttribute dealerAttribute = getDealerAttribute(contract.getDealer(), contract.getAsset().getAssetMake(), contract.getAsset().getModel().getAssetCategory());	
		if (dealerAttribute == null || dealerAttribute.getInsuranceCompany() == null) {
			errors.add(I18N.message("insurance.company.not.found.in.dealer"));
		} else { 
			InsuranceFinService isrFinSrv = getInsuranceFinSrv(dealerAttribute.getInsuranceCompany(), contract.getAsset().getModel());
			if (isrFinSrv == null) {
				errors.add(I18N.message("insurance.fin.service.not.found"));
			}
		}
		
		if (firstDueDate != null) {
			SettingConfig settingConfig = assetService.getByCode(SettingConfig.class, "max.first.due.date.fixation");
			Integer maxFirstDueDay = 45;
			if (settingConfig != null && StringUtils.isNotEmpty(settingConfig.getValue())) {
				maxFirstDueDay = Integer.parseInt(settingConfig.getValue().toString());
			}
	
			Date firstDueDateBeginningOfDay = DateUtils.getDateAtBeginningOfDay(firstDueDate);
			Date maxFirstDueDate = DateUtils.addDaysDate(DateUtils.getDateAtBeginningOfDay(DateUtils.todayH00M00S00()), maxFirstDueDay);
	
			if (DateUtils.getDateAtBeginningOfDay(firstDueDateBeginningOfDay).compareTo(DateUtils.getDateAtBeginningOfDay(maxFirstDueDate)) < 0) {
				if (DateUtils.getDateAtBeginningOfDay(DateUtils.todayH00M00S00()).compareTo(DateUtils.getDateAtBeginningOfDay(firstDueDate)) > 0) {
					errors.add(I18N.message("first.due.date.should.be.greater.than.or.equals.contract.start.date", new String[] {DateUtils.getDateLabel(firstDueDate)}));
				} 
			} else {
				errors.add(I18N.message("first.due.date.should.be.less.than.max.first.date.payment", DateUtils.getDateLabel(firstDueDate), DateUtils.getDateLabel(maxFirstDueDate)));
			}		
		}
		List<ContractIssue> contractIssues = contract.getContractIssues();
		if (contractIssues != null && !contractIssues.isEmpty()) {
			for (ContractIssue contractIssue : contractIssues) {
				if (!contractIssue.isFixed() && !contractIssue.isForced()) {
					if (contractIssue.getIssueType() != null && contractIssue.getIssueDocument1() != null) {
						errors.add(I18N.message("issue.not.fixed", new String[] {contractIssue.getIssueType().getDescEn(), contractIssue.getIssueDocument1().getDescEn()}));
					}
				}
			}
		}
		
		return errors;
	}

	/* 
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	/**
	 * @param dealer
	 * @param assetMake
	 * @param assetCategory
	 * @return
	 */
	public double getInsurancePremium(Dealer dealer, AssetModel assetModel) {
		double premiumTotal = 0d;
		DealerAttribute dealerAttribute = getDealerAttribute(dealer, assetModel.getAssetRange().getAssetMake(), assetModel.getAssetCategory());
		if (dealerAttribute != null) {	
			Organization insuranceCompany = dealerAttribute.getInsuranceCompany();
			if (insuranceCompany != null) {
				int period = MyNumberUtils.getInteger(dealerAttribute.getInsuranceCoverageDuration());
				InsuranceFinService isrFinSrv = getInsuranceFinSrv(insuranceCompany, assetModel);
				if (isrFinSrv != null) {
					if (period == 1) {
						premiumTotal = MyNumberUtils.getDouble(isrFinSrv.getPremium1Y());
					} else if (period == 2) {
						premiumTotal = MyNumberUtils.getDouble(isrFinSrv.getPremium2Y());
					}
				}
			}
		}
		return premiumTotal;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ActivationContractService#updateContractData(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void updateContractData(Contract contract) {
		Asset asset = contract.getAsset();
		if (asset != null) {
			if (asset.getId() == null) {
				create(asset);
			} else {
				update(asset);
			}
		}
		if (!contract.isTransfered()) {
			ContractApplicantRestriction restrictions = new ContractApplicantRestriction();
			restrictions.setConId(contract.getId());
			List<ContractApplicant> oldContractApps = list(restrictions);
			if (oldContractApps != null && !oldContractApps.isEmpty()) {
				for (ContractApplicant oldContractApp : oldContractApps) {
					delete(oldContractApp);
				}
			}
			
			List<ContractApplicant> newContractApps = contract.getContractApplicants();
			if (newContractApps != null && !newContractApps.isEmpty()) {
				contract.setNumberGuarantors(newContractApps.size());
				for (ContractApplicant newConApp : newContractApps) {
					Applicant guarantor = newConApp.getApplicant();
					if (guarantor != null) {
						if (guarantor.getId() == null) {
							applicantService.saveOrUpdateApplicant(guarantor);
						} else {
							update(guarantor);
						}
					}
					create(newConApp);
				}	
			} else {
				contract.setNumberGuarantors(0);
			}
		} else {
			ContractSimulationRestriction simulationRestrictions = new ContractSimulationRestriction();
			simulationRestrictions.setConId(contract.getId());
			List<ContractSimulation> contractSimulations = list(simulationRestrictions);
			ContractSimulation conSimulation = null;
			if (contractSimulations != null && !contractSimulations.isEmpty()) {
				conSimulation = contractSimulations.get(0);
			}
			if (conSimulation != null) {
				ContractSimulationApplicantRestriction restrictions = new ContractSimulationApplicantRestriction();
				restrictions.setConSimulationId(conSimulation.getId());
				List<ContractSimulationApplicant> oldContractSimulationApps = list(restrictions);
				if (oldContractSimulationApps != null && !oldContractSimulationApps.isEmpty()) {
					for (ContractSimulationApplicant oldContractSimulApp : oldContractSimulationApps) {
						delete(oldContractSimulApp);
					}
				}
				
				List<ContractSimulationApplicant> newSimulationApplicants = conSimulation.getContractSimulationApplicants();
				if (newSimulationApplicants != null && !newSimulationApplicants.isEmpty()) {
					contract.setNumberGuarantors(newSimulationApplicants.size());
					for (ContractSimulationApplicant newSimulationApplicant : newSimulationApplicants) {
						Applicant guarantor = newSimulationApplicant.getApplicant();
						if (guarantor != null) {
							if (guarantor.getId() == null) {
								applicantService.saveOrUpdateApplicant(guarantor);
							} else {
								update(guarantor);
							}
						}
						create(newSimulationApplicant);
					}
				} else {
					contract.setNumberGuarantors(0);
				}
			}
		}
		update(contract);
	}
}
