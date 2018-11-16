package com.nokor.efinance.glf.accounting.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.accounting.InsuranceExpense;
import com.nokor.efinance.core.shared.accounting.InsuranceIncome;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeAdjustment;
import com.nokor.efinance.core.shared.accounting.LeaseAdjustment;
import com.nokor.efinance.core.shared.accounting.LeaseTransaction;
import com.nokor.efinance.core.shared.accounting.LeasesReport;
import com.nokor.efinance.core.shared.accounting.ReferalFee;
import com.nokor.efinance.core.shared.accounting.ReferalFeeAdjustment;
import com.nokor.efinance.core.shared.accounting.RegistrationExpense;
import com.nokor.efinance.core.shared.accounting.ServiceTransaction;
import com.nokor.efinance.core.shared.accounting.ServicingIncome;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeAdjustment;

/**
 * @author ly.youhort
 */
public interface GLFLeasingAccountingService extends BaseEntityService {
	
	List<LeaseTransaction> getLeaseTransactions(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<LeaseTransaction> getNetLeasings(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<ServiceTransaction> getServiceTransactions(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
		
	List<ReferalFee> getReferalFees(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
		
	List<InsuranceIncome> getInsuranceIncomes(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<InsuranceExpense> getInsuranceExpenses(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);	

	List<RegistrationExpense> getRegistrationExpenses(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);	
	
	List<ServicingIncome> getServicingIncomes(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<LeaseAdjustment> getLeaseAdjustments(EDealerType dealerType, Dealer dealer, EWkfStatus contractStatus, String reference, Date startDate, Date endDate);
	
	List<ReferalFeeAdjustment> getReferalFeeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<InsuranceIncomeAdjustment> getInsuranceIncomeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<ServicingIncomeAdjustment> getServicingIncomeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate);
	
	List<LeaseTransaction> getRemainingBalance(EDealerType dealerType, Dealer dealer, String reference, Date endDate);
	
	List<LeasesReport> getLeaseReports(Date calculDate, BaseRestrictions<Contract> restrictions);
}
