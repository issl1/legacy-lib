package com.nokor.efinance.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.util.Assert;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractInterfaceService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.AuthenticationService;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.testing.BaseTestCase;

/**
 * 
 * @author prasnar
 *
 */
public class TestCashFlow extends BaseTestCase implements CashflowEntityField {

	/**
	 * 
	 */
	public TestCashFlow() {
	}
	
	
	public void testAAAA(){
		Applicant app = ENTITY_SRV.getById(Applicant.class, 25L);
		logger.debug("cat" + app.getApplicantCategory());
	}
	public void xxtestStartPeriod() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		List<Cashflow> cashflows = cIService.list(restrictions);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getNumInstallment() == 0) {
				cashflow.setPeriodStartDate(cashflow.getInstallmentDate());
				cashflow.setPeriodEndDate(cashflow.getInstallmentDate());
			} else {
				Contract contract = cashflow.getContract();
				Date startDate = contract.getStartDate();
				Date periodStartDate = DateUtils.addMonthsDate(startDate, -1 * cashflow.getNumInstallment());
				Date periodEndDate = DateUtils.addMonthsDate(periodStartDate, -1);
				cashflow.setPeriodStartDate(periodStartDate);
				cashflow.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
			}
			cIService.saveOrUpdate(cashflow);
		}
	}
	
	public void xxtestBankDeposit() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq("paymentStatus", PaymentWkfStatus.PAI));
		restrictions.addOrder(Order.desc("id"));
		
		List<Payment> payments = cIService.list(restrictions);
		String str = "";
		
		for (Payment payment : payments) {
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null && !cashflows.isEmpty()) {
				double sum = 0d;
				for (Cashflow cashflow : cashflows) {
					sum += MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount());
				}
				if (MyMathUtils.roundAmountTo(sum) != MyMathUtils.roundAmountTo(payment.getTiPaidAmount())) {
					str += payment.getId() + " - " + payment.getReference() 
							+ "[" + MyMathUtils.roundAmountTo(payment.getTiPaidAmount()) + ", " + MyMathUtils.roundAmountTo(sum) + "]"
							+ "[" + DateUtils.getDateLabel(payment.getPaymentDate(), "dd/MM/yyyy") + "]\n";
				}
			}
		}
		System.out.println(str);
	}
	
	public void xxxtestInsurFeeUser() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
		// restrictions.addCriterion(Restrictions.eq(SERVICE + "." + ID, 2));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq(NUM_INSTALLMENT, 13), Restrictions.eq(NUM_INSTALLMENT, 25)));
		restrictions.addCriterion(Restrictions.eq(PAID, false));
		restrictions.addCriterion(Restrictions.gt("tiInstallmentUsd", 59d));
		
		List<Cashflow> cashflows = cIService.list(restrictions);
		
		String str = "";
		String str2 = "";
		
		for (Cashflow cashflow : cashflows) {
			str += cashflow.getContract().getReference() + "[" + cashflow.getId() + "]\n";
			
			BaseRestrictions<Cashflow> restrictions2 = new BaseRestrictions<>(Cashflow.class);
			restrictions2.addCriterion(Restrictions.eq(NUM_INSTALLMENT, cashflow.getNumInstallment() - 1));
			restrictions2.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cashflow.getContract().getId()));
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.CAP));
			
			List<Cashflow> cashflows2 = cIService.list(restrictions2);
			if (cashflows2.get(0).isPaid()) {
				str2 += cashflow.getContract().getReference() + "[" + cashflow.getId() + "]\n";
			} else {
				/*cashflow.setNumInstallment(cashflow.getNumInstallment() - 1);
				Date newInstallmentDate = DateUtils.addMonthsDate(cashflow.getInstallmentDate(), -1);
				cashflow.setInstallmentDate(newInstallmentDate);
				cashflow.setRealInstallmentDate(newInstallmentDate);
				cashflow.setRealIssueDate(newInstallmentDate);
				cashflow.setIssueDate(newInstallmentDate);
				cIService.saveOrUpdate(cashflow);*/
			}
		}
		System.out.println(str);
		System.out.println("=============================");
		System.out.println(str2);
	}

	public void xxxtestPOSUser() {
		SecurityService SECURITY_SRV = getBean(SecurityService.class);
		List<SecUser> users = SECURITY_SRV.list(SecUser.class);
		String usersNoDealer = "";
		for (SecUser secUser : users) {
			if (secUser.getStatusRecord().equals(EStatusRecord.ACTIV) && ProfileUtil.isPOS(secUser)) {
				SecUserDetail secUserDetail = getSecUserDetail(SECURITY_SRV, secUser);
				if (secUserDetail == null || secUserDetail.getDealer() == null) {
					usersNoDealer += secUser.getDesc() + " [" + secUser.getLogin() + "]\n";
				}
			}
		}
		System.out.println(usersNoDealer);
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail(SecurityService SECURITY_SRV, SecUser secUser) {
		return SECURITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
	
	public void xxtestFirstInstallmentDate() {
		ContractService cIService = getBean(ContractService.class);
		List<Contract> contracts = cIService.list(Contract.class);
		String contractsString = "";
		for (Contract contract : contracts) {
			BaseRestrictions<Cashflow> restrictions2 = new BaseRestrictions<Cashflow>(Cashflow.class);
			restrictions2.addCriterion(Restrictions.eq(CANCEL, false));
			restrictions2.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
			restrictions2.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.CAP));
			restrictions2.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 1));
			
			List<Cashflow> cashflows = cIService.list(restrictions2);
			// Quotation quotation = cIService.getById(Quotation.class, contract.getFoReference());
			if (cashflows != null && !cashflows.isEmpty()) {
				String cashflowInstallmentDate = DateUtils.getDateLabel(cashflows.get(0).getInstallmentDate(), "ddMMyyyy");
				String quotationInstallmentDate = DateUtils.getDateLabel(contract.getFirstDueDate(), "ddMMyyyy");
			
				if (!quotationInstallmentDate.equals(cashflowInstallmentDate)) {
					contractsString += contract.getReference() + "[" + cashflowInstallmentDate + "] [" + quotationInstallmentDate + "]\n";
				}
			}
		}
		System.out.println(contractsString);
	}
	
	public void xxxxtestNoFeeDate() {
		ContractService cIService = getBean(ContractService.class);
		List<Contract> contracts = cIService.list(Contract.class);
		String contractsString = "";
		for (Contract contract : contracts) {
			int nbYear = contract.getFinancialProduct().getTerm() / 12;
			BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
			restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
			restrictions.addCriterion(Restrictions.eq("service.id", new Long(2)));
			restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
			List<Cashflow> cashflows = cIService.list(restrictions);
			System.out.println("term = " + contract.getFinancialProduct().getTerm());
			System.out.println("contract = " + contract.getReference());
			System.out.println("fee size = " + cashflows.size());
			System.out.println("nb year = " + nbYear);
			if (cashflows != null && cashflows.size() < 4 && cashflows.size() != nbYear) {
				contractsString += contract.getReference() + "\n";
			}
		}
		System.out.println(contractsString);
	}
	
	public void xxxxx11testFirstInstallmentDate() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.isNotNull("boReference"));
		restrictions.addOrder(Order.asc("contractStartDate"));
		List<Quotation> quotations = cIService.list(restrictions);
		String resutl = "";
		for (Quotation quotation : quotations) {
			Contract contract = cIService.getById(Contract.class, quotation.getContract().getId());
			String firstInstallmentDate1 = DateUtils.getDateLabel(quotation.getFirstDueDate(), "ddMMyyyy");
			String firstInstallmentDate2 = DateUtils.getDateLabel(contract.getFirstDueDate(), "ddMMyyyy");
			if (!firstInstallmentDate1.equals(firstInstallmentDate2)) {
				resutl += contract.getId() + ", " + contract.getReference() +  ", " + firstInstallmentDate1 + ", " + firstInstallmentDate2 + "\n";
			}
		}
		System.out.println(resutl);
	}
	
	public void xxtestStartDate() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.isNotNull("boReference"));
		restrictions.addOrder(Order.asc("contractStartDate"));
		List<Quotation> quotations = cIService.list(restrictions);
		String resutl = "";
		for (Quotation quotation : quotations) {
			Contract contract = cIService.getById(Contract.class, quotation.getContract().getId());
			String startDate1 = DateUtils.getDateLabel(quotation.getContractStartDate(), "ddMMyyyy");
			String startDate2 = DateUtils.getDateLabel(contract.getStartDate(), "ddMMyyyy");
			if (!startDate1.equals(startDate2)) {
				resutl += contract.getId() + ", " + contract.getReference() +  ", " + startDate1 + ", " + startDate2 + "\n";
			}
		}
		System.out.println(resutl);
	}
	
	public void xxtestRecon() {
		String reference = "GLF-PLN-01-00003991";
		double oustanding = 1365.00d;
		
		ContractService cIService = getBean(ContractService.class);
		PaymentService cIPaymentService = getBean(PaymentService.class);
		Contract contract = cIService.getByReference(reference);
		Amount out = cIService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
		double diff = out.getTiAmount() - oustanding;
		if (diff < -0.05) {
			Payment payment = cIService.getLastPayment(contract.getId());
			double diffPos = -1 * diff;
			if (payment.getTiPaidAmount() - 0.05 <= diffPos && diffPos >= payment.getTiPaidAmount() + 0.05) {
				logger.info("Contract [ " + contract.getReference() + "], Cancel payment [" + payment.getReference() + "]");
				cIPaymentService.cancelPayment(payment);
			} else {
				logger.info("===> Warning contract [" + contract.getReference() + "] could not integrated. Issue on payment [" + payment.getReference() + "]");
			}
		} else if (diff > 0.05) {
			Payment payment = cIService.getNextPayment(contract.getId());
			if (payment.getTiPaidAmount() - 0.05 <= diff && diff >= payment.getTiPaidAmount() + 0.05) {
				payment = cIPaymentService.createPayment(payment);
				logger.info("Contract [ " + contract.getReference() + "], Generate payment [" + payment.getReference() + "]");
			} else {
				logger.info("===> Warning contract [" + contract.getReference() + "] could not integrated");
			}
		}
	}
	
	public void xxtestAddRole() {
		try {
			String username = "admin";
			SecurityService SECURITY_SRV = getBean(SecurityService.class);
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	public void xxtestCreateUser() {
		try {
			String username = "admin1";
			String password = "admin1";
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
//			SecUser secUser = authSrv.authenticate(username, password);
			Assert.isNull(secUser, "[" + username + "] already existed.");
			secUser = SecurityEntityFactory.createInstance(SecUser.class, "adminCreator");
			secUser.setLogin(username);
			secUser.setDesc(username);
			
			SECURITY_SRV.createUser(secUser, password);
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("Digested password [" + secUser.getPassword() + "]");
			logger.info("Salt password [" + secUser.getPasswordSalt() + "]");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void xxxxxtestFirstInstallmentDate() {
		ContractInterfaceService cIService = getBean(ContractInterfaceService.class);
		List<Contract> contracts = cIService.list(Contract.class);
		for (Contract contract : contracts) {
			Quotation quotation = contract.getQuotation();
			contract.setFirstDueDate(quotation.getFirstDueDate());
			cIService.saveOrUpdate(contract);
		}
		
	}
	
	public void xxxtestdd() {
		ContractInterfaceService cIService = getBean(ContractInterfaceService.class);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.isNotNull("boReference"));
		restrictions.addOrder(Order.asc("contractStartDate"));
		List<Quotation> quotations = cIService.list(restrictions);
		String ss = "";
		for (Quotation quotation : quotations) {
			BaseRestrictions<Cashflow> restrictions2 = new BaseRestrictions<Cashflow>(Cashflow.class);
			restrictions2.addCriterion(Restrictions.eq(CANCEL, false));
			restrictions2.addCriterion(Restrictions.eq(CONTRACT + "." + ID, quotation.getContract().getId()));
			restrictions2.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 1));
			restrictions2.addCriterion(Restrictions.eq(PAID, false));
			List<Cashflow> cashflows = cIService.list(restrictions2);
			if (!DateUtils.getDateLabel(cashflows.get(0).getInstallmentDate(), "ddMMyyyy")
					.equals(DateUtils.getDateLabel(quotation.getFirstDueDate(), "ddMMyyyy"))) {
				ss += quotation.getId() + ", " + DateUtils.getDateLabel(cashflows.get(0).getInstallmentDate(), "ddMMyyyy") 
						+ ", " + DateUtils.getDateLabel(quotation.getFirstDueDate(), "ddMMyyyy") + "\n";
			}
		}
		System.out.println(ss);
	}
	
	/**
	 * 
	 */
	public void xxtestActivateContract() {
		try {
			ContractInterfaceService cIService = getBean(ContractInterfaceService.class);
			PaymentService cIPaymentService = getBean(PaymentService.class);
			BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
			restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
			restrictions.addCriterion(Restrictions.isNull("boReference"));
			restrictions.addOrder(Order.asc("contractStartDate"));
			
			List<Quotation> quotations = cIService.list(restrictions);
			EPaymentMethod paymentMethod = cIService.getByCode(EPaymentMethod.class, "CASH");
			if (paymentMethod != null) {
				for (int i = 0; i < quotations.size(); i++) {
					Quotation quotation = quotations.get(i);
					System.out.println("============================= Process " + (i + 1) + "/" + quotations.size());
					cIPaymentService.createDownPayment(quotation.getContract(), quotation.getContractStartDate());
					cIService.activateContract(quotation.getId());
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void xxxtestOfficialPayments() {
		try {
			CashflowService cIService = getBean(CashflowService.class);
			PaymentService cIPaymentService = getBean(PaymentService.class);
			
			EPaymentMethod paymentMethod = cIService.getByCode(EPaymentMethod.class, "CASH");
			SecUser boMigration = cIService.getById(SecUser.class, new Long(247));
			
			BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
			//restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
			//restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, "GLF-RTK-01-00001172"));
			restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
			restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
			restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 0));
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.todayH00M00S00()));
			restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
			List<Cashflow> cashflows = cIService.getListCashflow(restrictions);
			List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
			for (Cashflow cashflow : cashflows) {
				CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
				if (cashflowPayment == null) {
					cashflowPayment = new CashflowPayment();
					cashflowPayment.setContract(cashflow.getContract());
					cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
					cashflowPayments.add(cashflowPayment);
				}
				cashflowPayment.setTiPaymentAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiPaymentAmountUsd()) + cashflow.getTiInstallmentAmount());
				cashflowPayment.addCashflow(cashflow);
			}
			for (CashflowPayment cashflowPayment : cashflowPayments) {
				Payment payment = new Payment();
				Contract contract = cashflowPayment.getContract();
				payment.setApplicant(contract.getApplicant());
				payment.setContract(contract);
				payment.setExternalReference("");
				payment.setPaymentDate(cashflowPayment.getInstallmentDate());
				payment.setPaymentMethod(paymentMethod);
				payment.setTiPaidAmount(cashflowPayment.getTiPaymentAmountUsd());
				payment.setWkfStatus(PaymentWkfStatus.RVA);
				payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
				payment.setReceivedUser(boMigration);
				payment.setPaymentType(EPaymentType.ORC);
				payment.setCashflows(cashflowPayment.getCashflows());
				cIPaymentService.createPayment(payment);
				//System.out.println("===> " + cashflowPayment.getTiPaymentAmountUsd());
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void xxxtestChangexx() {
		PaymentService cIPaymentService = getBean(PaymentService.class);
		Contract contract = cIPaymentService.getById(Contract.class, new Long(2560));
		Quotation quotation = cIPaymentService.getById(Quotation.class, new Long(2117));
		contract.setSigatureDate(quotation.getContractStartDate());
		contract.setStartDate(quotation.getContractStartDate());
		contract.setCreationDate(quotation.getActivationDate());
		contract.setInitialEndDate(contract.getStartDate());
		contract.setEndDate(DateUtils.addMonthsDate(quotation.getContractStartDate(), quotation.getTerm() * quotation.getFrequency().getNbMonths()));
		contract.setInitialEndDate(contract.getEndDate());
		cIPaymentService.saveOrUpdate(contract);	
	}
	
	
	
	public void xxtestOrderPurchases() {
		PaymentService cIPaymentService = getBean(PaymentService.class);
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.ORC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.RVA));
		restrictions.addOrder(Order.asc(PAYMENT_DATE));
		
		List<Payment> payments = cIPaymentService.list(restrictions);
		for (Payment payment : payments) {
			cIPaymentService.changePaymentStatus(payment, PaymentWkfStatus.VAL);
		}
	}
	
	public void xxtestSecondPayments() {
		PaymentService cIPaymentService = getBean(PaymentService.class);
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.ORC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.VAL));
		restrictions.addOrder(Order.asc(PAYMENT_DATE));
		List<Payment> payments = cIPaymentService.list(restrictions);
		for (Payment payment : payments) {
			payment.setPrintPurchaseOrderVersion(1);
			cIPaymentService.changePaymentStatus(payment, PaymentWkfStatus.PAI);
		}
	}
	
	public void xxxtestInstallmentPayments() {
		try {
			CashflowService cIService = getBean(CashflowService.class);
			PaymentService cIPaymentService = getBean(PaymentService.class);
			
			EPaymentMethod paymentMethod = cIService.getByCode(EPaymentMethod.class, "CASH");
			SecUser boMigration = cIService.getById(SecUser.class, new Long(269));
			
			List<String> conts = new ArrayList<>();
			
			conts.add("GLF-00000002");
			conts.add("GLF-00000003");
			conts.add("GLF-00000012");
			conts.add("GLF-00000013");
			conts.add("GLF-00000016");
			conts.add("GLF-00000018");
			conts.add("GLF-00000019");
			conts.add("GLF-00000020");
			conts.add("GLF-00000023");
			conts.add("GLF-00000024");
			conts.add("GLF-00000025");
			conts.add("GLF-00000026");
			conts.add("GLF-00000027");
			conts.add("GLF-00000028");
			conts.add("GLF-00000032");
			conts.add("GLF-00000035");
			conts.add("GLF-00000038");
			conts.add("GLF-00000041");
			conts.add("GLF-00000042");
			conts.add("GLF-00000045");
			conts.add("GLF-00000049");
			conts.add("GLF-00000050");
			conts.add("GLF-00000051");
			conts.add("GLF-00000052");
			conts.add("GLF-00000053");
			conts.add("GLF-00000054");
			conts.add("GLF-00000057");
			conts.add("GLF-00000059");
			conts.add("GLF-00000060");
			conts.add("GLF-00000062");
			conts.add("GLF-00000063");
			conts.add("GLF-00000064");
			conts.add("GLF-00000066");
			conts.add("GLF-00000070");
			conts.add("GLF-00000072");
			conts.add("GLF-00000075");
			conts.add("GLF-00000076");
			conts.add("GLF-00000077");
			conts.add("GLF-00000080");
			conts.add("GLF-00000081");
			conts.add("GLF-00000085");
			conts.add("GLF-00000086");
			conts.add("GLF-00000089");
			conts.add("GLF-00000091");
			conts.add("GLF-00000092");
			conts.add("GLF-00000097");
			conts.add("GLF-00000100");
			conts.add("GLF-00000101");
			conts.add("GLF-00000102");
			conts.add("GLF-00000110");
			conts.add("GLF-00000128");
			conts.add("GLF-00000142");
			conts.add("GLF-00000150");
			conts.add("GLF-00000153");
			conts.add("GLF-00000182");
			conts.add("GLF-00000190");
			conts.add("GLF-00000193");
			conts.add("GLF-00000197");
			conts.add("GLF-00000246");
			conts.add("GLF-00000263");
			conts.add("GLF-00000275");
			conts.add("GLF-00000286");
			conts.add("GLF-00000306");
			conts.add("GLF-00000307");
			conts.add("GLF-00000313");
			conts.add("GLF-00000347");
			conts.add("GLF-00000372");
			conts.add("GLF-00000488");
			conts.add("GLF-00000512");
			conts.add("GLF-KDL-00010");
			conts.add("GLF-KDL-00000017");
			conts.add("GLF-KDL-00000005");
			conts.add("GLF-KDL-00000008");
			conts.add("GLF-BTB-01-0000000675");
			conts.add("GLF-PNP-02-0000000707");
			conts.add("GLF-PNP-08-0000001117");
			conts.add("GLF-PLN-01-0000001363");
			conts.add("GLF-KPS-01-0000002521");

			
			if (paymentMethod != null && boMigration != null) {
				BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
				restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
				restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
				restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
				restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
				restrictions.addCriterion(Restrictions.lt(INSTALLMENT_DATE, DateUtils.addDaysDate(DateUtils.todayH00M00S00(), -9)));
				restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
				
				List<Cashflow> cashflows = cIService.getListCashflow(restrictions);
				List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
				for (Cashflow cashflow : cashflows) {
					if (!conts.contains(cashflow.getContract().getReference())) {
						CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
						if (cashflowPayment == null) {
							cashflowPayment = new CashflowPayment();
							cashflowPayment.setContract(cashflow.getContract());
							cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
							cashflowPayments.add(cashflowPayment);
						}
						cashflowPayment.setTiPaymentAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiPaymentAmountUsd()) + cashflow.getTiInstallmentAmount());
						cashflowPayment.addCashflow(cashflow);
					}
				}
				int i = 0;
				for (CashflowPayment cashflowPayment : cashflowPayments) {
					i++;
					System.out.println("============================= Process " + i + "/" + cashflowPayments.size());
					Payment payment = new Payment();
					Contract contract = cashflowPayment.getContract();
					payment.setApplicant(contract.getApplicant());
					payment.setContract(contract);
					payment.setExternalReference("");
					payment.setPaymentDate(cashflowPayment.getInstallmentDate());
					payment.setPaymentMethod(paymentMethod);
					payment.setTiPaidAmount(cashflowPayment.getTiPaymentAmountUsd());
					payment.setWkfStatus(PaymentWkfStatus.PAI);
					payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
					payment.setReceivedUser(boMigration);
					payment.setPaymentType(EPaymentType.IRC);
					payment.setCashflows(cashflowPayment.getCashflows());
					cIPaymentService.createPayment(payment);
				}
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 
	 */
	public void xxtestChangePenalty() {
		try {
			ContractService cIService = getBean(ContractService.class);
			PenaltyRule penaltyRule = cIService.getById(PenaltyRule.class, new Long(1));
			if (penaltyRule != null) {
				for (int i = 1; i <= 492; i++) {
					String reference = "00000000" + i;
					reference =  "GLF-" + reference.substring(reference.length() - 8);
					Contract contract = cIService.getByReference(reference);
					if (contract != null) {
						contract.setPenaltyRule(penaltyRule);
						cIService.saveOrUpdate(contract);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void xxtestSplitFees() {
		ContractService cIService = getBean(ContractService.class);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		EWkfStatus[] status = new EWkfStatus[] {
			QuotationWkfStatus.QUO,
			QuotationWkfStatus.RFC,
			QuotationWkfStatus.PRO,
			QuotationWkfStatus.PRA,
			QuotationWkfStatus.APU,
			QuotationWkfStatus.APS,
			QuotationWkfStatus.AWU,
			QuotationWkfStatus.AWS,
			QuotationWkfStatus.AWT,
			QuotationWkfStatus.APV
		};
		
		restrictions.addCriterion(Restrictions.in("quotationStatus", status));
		restrictions.addCriterion(Restrictions.isNull("boReference"));
		restrictions.addOrder(Order.asc("startCreationDate"));
		
		List<Quotation> quotations = cIService.list(restrictions);
		for (Quotation quotation : quotations) {
			System.out.println("[" + quotation.getId() + "] - " +  quotation.getWkfStatus() + "- [" + DateUtils.getDateLabel(quotation.getStartCreationDate()) + "] " + quotation.getApplicant().getIndividual().getLastNameEn() + " / " + quotation.getApplicant().getIndividual().getFirstNameEn());
			for (QuotationService quotationService : quotation.getQuotationServices()) {
				if ("REGFEE".equals(quotationService.getService().getCode())) {
					cIService.delete(quotationService); 
					System.out.println("Registration fee : " + quotationService.getTiPrice());
				} else {
					quotationService.setSplitWithInstallment(true);
					cIService.saveOrUpdate(quotationService);
				}
			}
			cIService.saveOrUpdate(quotation);
		}
	}
	
	
	/**
	 * 
	 */
	public void xxxtestChangePasswordUser() {
		try {
						
			SecurityService SECURITY_SRV = getBean(SecurityService.class);
			List<SecUser> users = SECURITY_SRV.list(SecUser.class);
			
			for (SecUser secUser : users) {
				String newPassword = "k123";
				String username = secUser.getLogin();
				// SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
				Assert.notNull(secUser, "[" + username + "] does not exist.");
				SECURITY_SRV.changePassword(secUser, newPassword);
				Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
				logger.info("New digested password [" + secUser.getPassword() + "]");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * SecUser authentication
	 */
	public void xxtestAuthentication() {
		try {
			String username = "admin";
			String password = "admin";
			AuthenticationService authSrv = getBean(AuthenticationService.class);
			//SecUser secUser = authSrv.loadUserByUsername(username);
			SecUser secUser = authSrv.authenticate(username, password);
			
			Assert.notNull(secUser, "[" + username + "] can not be authenticated.");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	

	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getReference().equals(cashflow.getContract().getReference())
					&& DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
	}
	
	/**
	 * @author ly.youhort
	 */
	private class CashflowPayment implements Serializable {
		private static final long serialVersionUID = 3112339520304252300L;
		private Contract contract;
		private Date installmentDate;
		private Double tiPaymentAmountUsd;
		
		private List<Cashflow> cashflows = new ArrayList<Cashflow>();
			
		/**
		 * @return the contract
		 */
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
		 * @return the installmentDate
		 */
		public Date getInstallmentDate() {
			return installmentDate;
		}
		/**
		 * @param installmentDate the installmentDate to set
		 */
		public void setInstallmentDate(Date installmentDate) {
			this.installmentDate = installmentDate;
		}
		/**
		 * @return the tiPaymentAmountUsd
		 */
		public Double getTiPaymentAmountUsd() {
			return tiPaymentAmountUsd;
		}
		/**
		 * @param tiPaymentAmountUsd the tiPaymentAmountUsd to set
		 */
		public void setTiPaymentAmountUsd(Double tiPaymentAmountUsd) {
			this.tiPaymentAmountUsd = tiPaymentAmountUsd;
		}
		public void addCashflow(Cashflow cashflow) {
			cashflows.add(cashflow);
		}
		/**
		 * @return the cashflows
		 */
		public List<Cashflow> getCashflows() {
			return cashflows;
		}
		
	}
}
