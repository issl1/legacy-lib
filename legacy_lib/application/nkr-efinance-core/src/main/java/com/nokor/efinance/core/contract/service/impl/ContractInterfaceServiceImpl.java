package com.nokor.efinance.core.contract.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.SerializationUtils;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetArc;
import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.contract.dao.ContractDao;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractInterfaceService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.tools.LoanUtils;

/**
 * Contract service
 * @author ly.youhort
 *
 */
@Service("contractInterfaceService")
public class ContractInterfaceServiceImpl extends BaseEntityServiceImpl implements ContractInterfaceService {
	
	/** */
	private static final long serialVersionUID = 7810321879286803579L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FinanceCalculationService financeCalculationService;
	
	@Autowired
	private ApplicantService applicantService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
    private ContractDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public ContractDao getDao() {
		return dao;
	}
	
	/**
	 * @param quotaId
	 */
	@Override
	public Contract activateDownPaymentContract(Long quotaId) {
		return activateDownPaymentContract(getById(Quotation.class, quotaId));
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public Contract activateDownPaymentContract(Quotation quotation) {
		Applicant foApplicant = quotation.getApplicant();
		ApplicantArc applicant = copyApplicant(foApplicant);
		applicantService.saveOrUpdateApplicantArc(applicant);
		
		Applicant foGuarantor = quotation.getGuarantor();
		ApplicantArc guarantor = null;
		if (foGuarantor != null) {
			guarantor = copyApplicant(foGuarantor);
			applicantService.saveOrUpdateApplicantArc(guarantor);
		}
		
		saveOrUpdate(foApplicant);
		
		AssetArc asset = copyAsset(quotation.getAsset());
		saveOrUpdate(asset);
		
		Dealer dealer = quotation.getDealer();
		FinProduct financialProduct = quotation.getFinancialProduct(); 
		ProductLine productLine = financialProduct.getProductLine();
				
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
		calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
		calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
		calculationParameter.setFrequency(quotation.getFrequency());
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(quotation.getNumberOfPrincipalGracePeriods()));
		
		Date firstDueDate = quotation.getFirstDueDate() == null ? DateUtils.today() : quotation.getFirstDueDate() ;
		Date contractStartDate = quotation.getContractStartDate() == null ? DateUtils.today() : quotation.getContractStartDate();
		
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstDueDate, calculationParameter);
				
		Contract contract = new Contract();
		contract.setProductLineType(EProductLineType.FNC);
		contract.setProductLine(productLine);
		contract.setFinancialProduct(financialProduct);
		contract.setAssetArc(asset);
		contract.setDealer(dealer);
		contract.setPenaltyRule(productLine.getPenaltyRule());
		contract.setReference(quotation.getReference());
		contract.setWkfStatus(ContractWkfStatus.PEN);
		contract.setFirstDueDate(firstDueDate);
		contract.setSigatureDate(contractStartDate);
		contract.setStartDate(contractStartDate);
		contract.setCreationDate(quotation.getActivationDate());
		contract.setInitialStartDate(contract.getStartDate());
		contract.setEndDate(DateUtils.addDaysDate(DateUtils.addMonthsDate(contractStartDate, quotation.getTerm() * quotation.getFrequency().getNbMonths()), -1));
		contract.setInitialEndDate(contract.getEndDate());
		contract.setTiAdvancePaymentAmount(quotation.getTiAdvancePaymentAmount());
		contract.setTeAdvancePaymentAmount(quotation.getTeAdvancePaymentAmount());
		contract.setVatAdvancePaymentAmount(quotation.getVatAdvancePaymentAmount());
		contract.setAdvancePaymentPercentage(quotation.getAdvancePaymentPercentage());
		contract.setTiFinancedAmount(quotation.getTiFinanceAmount());
		contract.setVatFinancedAmount(quotation.getVatFinanceAmount());
		contract.setTeFinancedAmount(quotation.getTeFinanceAmount());
		saveOrUpdate(contract);
		
		List<ContractApplicant> contractApplicants = new ArrayList<ContractApplicant>();
		
		ContractApplicant contractApplicant = new ContractApplicant();
		contractApplicant.setApplicant(foApplicant);
		contractApplicant.setContract(contract);
		contractApplicant.setApplicantType(EApplicantType.C);
		saveOrUpdate(contractApplicant);
		contractApplicants.add(contractApplicant);
		
		if (guarantor != null) {
			ContractApplicant contractGuarantor = new ContractApplicant();
			contractGuarantor.setApplicant(foGuarantor);
			contractGuarantor.setContract(contract);
			contractGuarantor.setApplicantType(EApplicantType.G);
			saveOrUpdate(contractGuarantor);
			contractApplicants.add(contractGuarantor);
		}
		contract.setContractApplicants(contractApplicants);
		
		contract.setTerm(quotation.getTerm());
		contract.setInterestRate(quotation.getInterestRate());
		contract.setFrequency(quotation.getFrequency());
		contract.setTiInstallmentAmount(quotation.getTiInstallmentAmount());
		contract.setVatInstallmentAmount(quotation.getVatInstallmentAmount());
		contract.setTeInstallmentAmount(quotation.getTeInstallmentAmount());
		contract.setIrrRate(amortizationSchedules.getIrrRate());
		contract.setNumberOfPrincipalGracePeriods(quotation.getNumberOfPrincipalGracePeriods());
		
		List<Cashflow> cashflows = new ArrayList<Cashflow>();
		
		Cashflow cashflowFin = CashflowUtils.createCashflow(productLine,
				null, contract, contract.getVatValue(),
				ECashflowType.FIN, ETreasuryType.DEA, getByCode(JournalEvent.class, ECashflowType.FIN_JOURNAL_EVENT), productLine.getPaymentConditionFin(),
				-1 * quotation.getTeFinanceAmount(), 0d, -1 * quotation.getTiFinanceAmount(),
				contractStartDate, contractStartDate, contractStartDate, 0);
		cashflows.add(cashflowFin);
				
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				Date installmentDate = firstDueDate;
				Integer numInstallment = null;
				boolean generate = true;
				if (!quotationService.isIncludeInInstallment() && !quotationService.isSplitWithInstallment()) {
					if (quotationService.getService().isPaidBeginContract()) {
						installmentDate = contractStartDate;
						numInstallment = 0;
					}
				} else {
					generate = false;
				}
				if (generate) {
					Cashflow cashflowFee = CashflowUtils.createCashflow(productLine,
							null, contract, contract.getVatValue(),
							ECashflowType.FEE, quotationService.getService().getTreasuryType(), quotationService.getService().getJournalEvent(),
							productLine.getPaymentConditionFee(),
							quotationService.getTePrice(), 0d, quotationService.getTiPrice(),
							installmentDate, installmentDate, installmentDate, numInstallment);
					cashflowFee.setService(quotationService.getService());
					cashflows.add(cashflowFee);
				}
			}
		}
		Collections.sort(cashflows, new CashflowComparatorByInstallmentDate());
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getNumInstallment() == null) {
				cashflow.setNumInstallment(getNumInstallment(cashflow.getInstallmentDate(), cashflows));
			}
			saveOrUpdate(cashflow);
		}
		
		saveOrUpdate(quotation);
		
		// TODO PYI
//		ContractHistory history = new ContractHistory();
//		history.setContract(contract);
//		history.setHistoryDate(DateUtils.today());
//		history.sethisReason(EhisReason.CONTRACT_0001);
//		saveOrUpdate(history);
		
		return contract;
	}

	/**
	 * @param quotation
	 */
	@Override
	public Contract activateContract(Long quotaId) {
		return activateContract(getById(Quotation.class, quotaId));
	}
	
	/**
	 * @param quotation
	 */
	@Override
	public Contract activateContract(Quotation quotation) {
		
		Contract contract = contractService.getByFoReference(quotation.getId());
		if (contract == null) {
			/*paymentService.createDownPayment(quotation.getId(), quotation.getContractStartDate(), 
					quotation.getFinancialProduct().getProductLine().getPaymentConditionFin().getPaymentMethod());
			contract = contractService.getByFoReference(quotation.getId());*/
		} else {
			if (contract.getWkfStatus().equals(ContractWkfStatus.FIN)) {
				throw new IllegalArgumentException("Contract is already activated");
			}
		}
		
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
		calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
		calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
		calculationParameter.setFrequency(quotation.getFrequency());
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(quotation.getNumberOfPrincipalGracePeriods()));
		
		Date firstDueDate = quotation.getFirstDueDate();
		Date contractStartDate = quotation.getContractStartDate();
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstDueDate, calculationParameter);
				
		contract.setReference(quotation.getReference());
		contract.setWkfStatus(ContractWkfStatus.FIN);
		contract.setFirstDueDate(firstDueDate);
		contract.setSigatureDate(contractStartDate);
		contract.setStartDate(contractStartDate);
		contract.setInitialStartDate(contractStartDate);
		contract.setCreationDate(quotation.getActivationDate());
		contract.setEndDate(DateUtils.addDaysDate(DateUtils.addMonthsDate(contractStartDate, quotation.getTerm() * quotation.getFrequency().getNbMonths()), -1));
		contract.setInitialEndDate(contract.getEndDate());
		
		contract.setTerm(quotation.getTerm());
		contract.setInterestRate(quotation.getInterestRate());
		contract.setFrequency(quotation.getFrequency());
		contract.setTiInstallmentAmount(quotation.getTiInstallmentAmount());
		contract.setVatInstallmentAmount(quotation.getVatInstallmentAmount());
		contract.setTeInstallmentAmount(quotation.getTeInstallmentAmount());
		contract.setIrrRate(amortizationSchedules.getIrrRate());
		contract.setNumberOfPrincipalGracePeriods(quotation.getNumberOfPrincipalGracePeriods());
		
		saveOrUpdate(contract);
		
		ProductLine productLine = contract.getProductLine();
		
		List<Cashflow> cashflows = new ArrayList<Cashflow>();
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		Integer term = quotation.getTerm();
		
		List<Schedule> schedules = amortizationSchedules.getSchedules();
		double totalCapitalAmount = 0d;
		
		JournalEvent capJournalEvent = getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT);
		JournalEvent iapJournalEvent = getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT);
		
		for (int i = 0; i < schedules.size(); i++) {
			Schedule schedule = schedules.get(i);
			double capitalAmount = MyMathUtils.roundAmountTo(schedule.getPrincipalAmount());
			if (i == schedules.size() - 1) {
				capitalAmount = MyMathUtils.roundAmountTo(quotation.getTeFinanceAmount()) - MyMathUtils.roundAmountTo(totalCapitalAmount);
			}
			
			if (capitalAmount > 0) {
				Cashflow cashflowCap = CashflowUtils.createCashflow(productLine,
						null, contract, contract.getVatValue(),
						ECashflowType.CAP, ETreasuryType.APP, capJournalEvent, productLine.getPaymentConditionCap(),
						capitalAmount, 0d, capitalAmount,
						schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflows.add(cashflowCap);
			}
			
			totalCapitalAmount += capitalAmount;
			
			if (schedule.getInterestAmount() > 0) {
				Cashflow cashflowIap = CashflowUtils.createCashflow(productLine,
					null, contract, contract.getVatValue(),
					ECashflowType.IAP, ETreasuryType.APP, iapJournalEvent, productLine.getPaymentConditionIap(),
					schedule.getInterestAmount(), 0d, schedule.getInterestAmount(),
					schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
				cashflows.add(cashflowIap);
			}
			
			if (quotationServices != null && !quotationServices.isEmpty()) {
				for (QuotationService quotationService : quotationServices) {
					if (quotationService.isSplitWithInstallment()) {
						double serviceTiAmount = 0d; 
						if (quotationService.getService().getFrequency() != null) {
							int nbMonths = quotationService.getService().getFrequency().getNbMonths();
							serviceTiAmount = MyMathUtils.roundAmountTo(((term / nbMonths) * quotationService.getTiPrice()) / term);
						} else {
							serviceTiAmount = MyMathUtils.roundAmountTo(quotationService.getTiPrice() / term);
						}
						Cashflow cashflowFee = CashflowUtils.createCashflow(productLine,
								null, contract, contract.getVatValue(),
								ECashflowType.FEE, quotationService.getService().getTreasuryType(), quotationService.getService().getJournalEvent(), 
								productLine.getPaymentConditionFee(),
								serviceTiAmount, 0d, serviceTiAmount,
								schedule.getInstallmentDate(), schedule.getPeriodStartDate(), schedule.getPeriodEndDate(), schedule.getN());
						cashflowFee.setService(quotationService.getService());
						cashflows.add(cashflowFee);
					}
				}
			}
		}
		
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				if (!quotationService.isSplitWithInstallment()) {
					int nbInstallments = 1;
					int nbMonths = 0;
					if (quotationService.getService().getFrequency() != null) {
						nbMonths = quotationService.getService().getFrequency().getNbMonths();
						nbInstallments = term / nbMonths;
					}
					Date installmentDate = firstDueDate;
					Date periodStartDate = contractStartDate;
					Date periodEndDate = null;
					Integer numInstallment = null;
					for (int i = 0; i < nbInstallments; i++) {
						boolean generate = true;
						if (i == 0) {
							generate = false;
							installmentDate = firstDueDate;
							periodStartDate = contractStartDate;
						} else {
							installmentDate = DateUtils.addMonthsDate(installmentDate, nbMonths);
							periodStartDate = DateUtils.addMonthsDate(periodStartDate, nbMonths);
							periodEndDate = DateUtils.addMonthsDate(periodStartDate, quotation.getFrequency().getNbMonths());
							numInstallment = null;
						}
						
						if (generate) {
							Cashflow cashflowFee = CashflowUtils.createCashflow(productLine,
									null, contract, contract.getVatValue(),
									ECashflowType.FEE, quotationService.getService().getTreasuryType(), quotationService.getService().getJournalEvent(),
									productLine.getPaymentConditionFee(),
									quotationService.getTePrice(), 0d, quotationService.getTiPrice(),
									installmentDate, periodStartDate, periodEndDate, numInstallment);
							cashflowFee.setService(quotationService.getService());
							cashflows.add(cashflowFee);
						}
					}
				}
			}
		}
		
		//Direct Cost
		for (FinProductService financialProductService : contract.getFinancialProduct().getFinancialProductServices()) {
			com.nokor.efinance.core.financial.model.FinService service = financialProductService.getService();
			if (EServiceType.listDirectCosts().contains(service.getServiceType())) {
				cashflows.addAll(createDirectCost(contract, service));
			}
		}
		
		Collections.sort(cashflows, new CashflowComparatorByInstallmentDate());
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getNumInstallment() == null) {
				cashflow.setNumInstallment(getNumInstallment(cashflow.getInstallmentDate(), cashflows));
			}
			saveOrUpdate(cashflow);
		}
		
		
		return contract;
	}	

	/**
	 * @param contract
	 * @param service
	 * @return
	 */
	private List<Cashflow> createDirectCost(Contract contract, com.nokor.efinance.core.financial.model.FinService service) {
		List<Cashflow> cashflows = new ArrayList<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contract.getFirstDueDate());
		calendar.set(Calendar.DAY_OF_MONTH, DateUtils.getDay(service.getDueDate()));
		Date installmentDate = calendar.getTime();
	    int nbInstallments = 1;
		int nbIncreaseMonth = 0;
		int nbInstallmentsPerYear = 1;
		
		if (service.isPaidOneShot()) {
			nbInstallments = 1;
		} else {
			if (service.getFrequency() != null) {
				nbInstallments = (service.isContractDuration() ? contract.getTerm() : service.getTermInMonths()) / service.getFrequency().getNbMonths();
				nbInstallmentsPerYear = 12 / service.getFrequency().getNbMonths();
			}
		}
		
		double teTotalDirectCostPrice = 0d;
		double tiTotalDirectCostPrice = 0d;
		double[] premiumYear = new double[5];
		
		if (service.getCalculMethod().equals(ECalculMethod.FIX)) {
			teTotalDirectCostPrice = service.getTePrice();
			tiTotalDirectCostPrice = service.getTiPrice();
    	} else if (service.getCalculMethod().equals(ECalculMethod.FOR)) {
    		String calculFormula = service.getFormula();
    		//Replace String ap for asset_price and la for loan_amount 
    		calculFormula = calculFormula.replace("ap", contract.getAsset().getTiAssetPrice().toString());//Asset Price
    		calculFormula = calculFormula.replace("la", contract.getTiFinancedAmount().toString());//Loan Amount
    		//Convert String (calculFormula) for Calculate 
    		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    		try {
    			tiTotalDirectCostPrice = Double.parseDouble(engine.eval(calculFormula).toString());
    			teTotalDirectCostPrice = tiTotalDirectCostPrice;
    		} catch (ScriptException e) {
    			throw new IllegalArgumentException("Script invalid");
    		}
    	} else if (service.getCalculMethod().equals(ECalculMethod.PAP)) {
    		double assetPriceFirstYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetFirstYear()) / 100;
    		double assetPriceSecondYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetSecondYear()) / 100;
    		double assetPriceThirdYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetThirdYear()) / 100;
    		double assetPriceForthYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetForthYear()) / 100;
    		double assetPriceFifthYear = (contract.getAsset().getTiAssetPrice() * service.getPercentageOfAssetFifthYear()) / 100;
    		
    		premiumYear[0] = (assetPriceFirstYear * 0.01 * service.getPercentageOfPremiumFirstYear()) / nbInstallmentsPerYear;
    		premiumYear[1] = (assetPriceSecondYear * 0.01 * service.getPercentageOfPremiumSecondYear()) / nbInstallmentsPerYear;
    		premiumYear[2] = (assetPriceThirdYear * 0.01 * service.getPercentageOfPremiumThirdYear()) / nbInstallmentsPerYear;
    		premiumYear[3] = (assetPriceForthYear * 0.01 * service.getPercentageOfPremiumForthYear()) / nbInstallmentsPerYear;
    		premiumYear[4] = (assetPriceFifthYear * 0.01 * service.getPercentageOfPremiumFifthYear()) / nbInstallmentsPerYear;
		}
		
		double tePriceUsd = 0d;
    	double tiPriceUsd = 0d;
		if (service.getCalculMethod() != ECalculMethod.PAP) {
			tePriceUsd = -1 * MyMathUtils.roundAmountTo(teTotalDirectCostPrice / nbInstallments);
	    	tiPriceUsd = tePriceUsd;
		}
    							
	    for (int i = 0; i < nbInstallments; i++) {
	    	installmentDate = DateUtils.plusMonth(installmentDate, nbIncreaseMonth);
	    	if (service.getFrequency() != null) {
	    		nbIncreaseMonth = service.getFrequency().getNbMonths();
	    	}
	    	
	    	if (service.getCalculMethod().equals(ECalculMethod.PAP)) {
	    		tePriceUsd = -1 * premiumYear[getNumYear(i, nbInstallmentsPerYear)];
	    		tiPriceUsd = tePriceUsd;
	    	}

	    	Cashflow cashflowService = CashflowUtils.createCashflow(contract.getProductLine(), null, contract, contract.getVatValue(),
	    			ECashflowType.SRV, service.getTreasuryType(), service.getJournalEvent(),
	    			contract.getProductLine().getPaymentConditionFee(), tePriceUsd, 0d, tiPriceUsd,
	    			installmentDate, installmentDate, installmentDate, (i + 1));
	    	cashflowService.setService(service);
	    	cashflows.add(cashflowService);
	    }
	    return cashflows;
	}
	
	/**
	 * @param contractReference
	 * @param directCoseCode
	 * @param directCostAmount
	 */
	public void addDirectCost(String contractReference, String directCostCode, Amount directCostAmount) {
		Contract contract = contractService.getByReference(contractReference);
		if (contract != null) {
			com.nokor.efinance.core.financial.model.FinService service = getByCode(com.nokor.efinance.core.financial.model.FinService.class, directCostCode);
			if (service != null) {
				com.nokor.efinance.core.financial.model.FinService directCost = (com.nokor.efinance.core.financial.model.FinService) SerializationUtils.clone(service);
				directCost.setCalculMethod(ECalculMethod.FIX);
				directCost.setTePrice(directCostAmount.getTeAmount());
				directCost.setVatPrice(directCostAmount.getVatAmount());
				directCost.setTiPrice(directCostAmount.getTiAmount());
				List<Cashflow> cashflows = createDirectCost(contract, directCost);
				for (Cashflow cashflow : cashflows) {
					saveOrUpdate(cashflow);
				}
			} else {
				throw new IllegalArgumentException("Direct Cost " + directCostCode + " not found");
			}
		} else {
			throw new IllegalArgumentException("Contract " + contractReference + " not found");
		}
	}
	
	/**
	 * @param i
	 * @param nbInstallmentsPerYear
	 * @return
	 */
	private int getNumYear(int i, int nbInstallmentsPerYear) {
		return (i / nbInstallmentsPerYear) + 1;
	}
	
	/**
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
	 * @param applicant
	 * @return
	 */
	private ApplicantArc copyApplicant(Applicant applicant) {
		
		ApplicantArc appArc = new ApplicantArc();
		appArc.setQuotations(applicant.getQuotations());
		appArc.setApplicantCategory(applicant.getApplicantCategory());

		Individual individual = applicant.getIndividual();
		IndividualArc individualArc = IndividualArc.createInstance();
		
		individualArc.setBirthDate(individual.getBirthDate());
		individualArc.setCivility(individual.getCivility());
		individualArc.setFirstName(individual.getFirstName());
		individualArc.setFirstNameEn(individual.getFirstNameEn());
		individualArc.setGender(individual.getGender());
		individualArc.setLastName(individual.getLastName());
		individualArc.setLastNameEn(individual.getLastNameEn());
		individualArc.setMaritalStatus(individual.getMaritalStatus());
		individualArc.setNationality(individual.getNationality());
		individualArc.setNickName(individual.getNickName());
		individualArc.setNumberOfChildren(individual.getNumberOfChildren());
		individualArc.setOtherNationality(individual.getOtherNationality());
		individualArc.setPlaceOfBirth(individual.getPlaceOfBirth());
		
		List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();		
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			List<IndividualAddressArc> boApplicantAddresses = new ArrayList<>(); 
			for (IndividualAddress individualAddress : individualAddresses) {
				IndividualAddressArc individualAddressArc = new IndividualAddressArc();
				individualAddressArc.getAddress().setType(individualAddress.getAddress().getType());
				individualAddressArc.setAddress(copyAddress(individualAddress.getAddress()));
				boApplicantAddresses.add(individualAddressArc);
			}
			individualArc.setIndividualAddresses(boApplicantAddresses);
		}
		List<Employment> employments = individual.getEmployments();				
		if (employments != null && !employments.isEmpty()) {
			List<EmploymentArc> boEmployments = new ArrayList<>();
			for (Employment employment : employments) {
				EmploymentArc boEmployment = copyEmployment(employment);
				boEmployments.add(boEmployment);
			}
			individualArc.setEmployments(boEmployments);
		}
		return appArc;
	}
	
	
	/**
	 * @param employment
	 * @return
	 */
	private EmploymentArc copyEmployment(Employment employment) {
		EmploymentArc empArc = new EmploymentArc();
		empArc.setAllowance(employment.getAllowance());
		empArc.setAllowCallToWorkPlace(employment.isAllowCallToWorkPlace());
		empArc.setBusinessExpense(employment.getBusinessExpense());
		empArc.setEmployerName(employment.getEmployerName());
		empArc.setEmploymentIndustry(employment.getEmploymentIndustry());
		empArc.setEmploymentStatus(employment.getEmploymentStatus());
		empArc.setEmploymentType(employment.getEmploymentType());
		empArc.setPosition(employment.getPosition());
		empArc.setRevenue(employment.getRevenue());
		empArc.setSameApplicantAddress(employment.isSameApplicantAddress());
		empArc.setTimeWithEmployerInMonth(employment.getTimeWithEmployerInMonth());
		empArc.setTimeWithEmployerInYear(employment.getTimeWithEmployerInYear());
		empArc.setWorkPhone(employment.getWorkPhone());
		if (employment.getAddress() != null) {
			empArc.setAddress(copyAddress(employment.getAddress()));
		}
		return empArc;
	}
	
	/**
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
		return addArc;
	}
	
	
	/**
	 * @param asset
	 * @return
	 */
	private AssetArc copyAsset(Asset asset) {
		AssetArc assArc = new AssetArc();
		assArc.setModel(asset.getModel());
		assArc.setAssetGender(asset.getAssetGender());
		assArc.setChassisNumber(asset.getChassisNumber());
		assArc.setCode(asset.getCode());
		assArc.setColor(asset.getColor());
		assArc.setDesc(asset.getDesc());
		assArc.setDescEn(asset.getDescEn());
		assArc.setEngine(asset.getEngine());
		assArc.setEngineNumber(asset.getEngineNumber());
		assArc.setPlateNumber(asset.getPlateNumber());
		assArc.setRegistrationDate(asset.getRegistrationDate());
		assArc.setTeAssetPrice(asset.getTeAssetPrice());
		assArc.setTiAssetPrice(asset.getTiAssetPrice());
		assArc.setVatAssetPrice(asset.getVatAssetPrice());
		assArc.setYear(asset.getYear());
		return assArc;
	}
	
	/**
	 * @author ly.youhort
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
}
