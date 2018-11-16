package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.RevenueSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInterestIncomeMonthlyCalculatorImpl {
		
	Logger logger = LoggerFactory.getLogger(GLFInterestIncomeMonthlyCalculatorImpl.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public InterestIncomeSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, List<Cashflow> calculatedCashflows, Date searchStartDate, Date searchEndDate) {
			
		InterestIncomeSchedules interestIncomeSchedules = new InterestIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		int startMonth = DateUtils.getMonth(startDate);
		int firstInstallmentMonth = DateUtils.getMonth(firstInstallmentDate);
		int installmentAddMonths = (firstInstallmentMonth - startMonth) - 1;
		if (installmentAddMonths < 0) {
			installmentAddMonths = 0;
		}
		
		List<InterestIncomeSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		boolean isStartInstallment = false;
		
		List<Cashflow> principalCashflows = getCashflowsByType(calculatedCashflows, ECashflowType.CAP);
		List<Cashflow> interestCashflows = getCashflowsByType(calculatedCashflows, ECashflowType.IAP);
		
		int numInstallment = 0;
		boolean contractInOverdueMoreThanOneMonth = false;
		
		RevenueSchedules revenueSchedules = new GLFRevenueCalculatorImpl().getSchedules(periodStartDate, firstInstallmentDate, calculationParameter);
				
		for (int i = 0; i <= numberOfPeriods; i++) {
			InterestIncomeSchedule schedule = new InterestIncomeSchedule();
			
			double interestInSuspend = 0d;
			double interestInSuspendCumulated = 0d;
			double interestIncomeRepayment = 0d;
			double interestIncomeReceivable = 0d;
			double revenue = 0d;
			double principalBalance = 0d;
			
			InterestIncomeSchedule prevSchedule = null;
			if (i > 0) {
				prevSchedule = schedules.get(i - 1);
			}
			
			if (i == 0) {
				installmentDate = startDate;
				periodStartDate = startDate;
			} else if (i == 1) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			
			if (periodStartDate.compareTo(firstInstallmentDate) <= 0 && periodEndDate.compareTo(firstInstallmentDate) >= 0) {
				isStartInstallment = true;
			}
			if (isStartInstallment) {
				numInstallment++;
			}
			
			principalBalance = getPrincipalBalance(periodEndDate, calculatedCashflows).getTiAmount();
			List<Cashflow> principalRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.CAP, principalCashflows);
			List<Cashflow> interestRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.IAP, interestCashflows);
			List<Cashflow> penaltyCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.PEN, getCashflowsByType(calculatedCashflows, ECashflowType.PEN));
						
			contractInOverdueMoreThanOneMonth = isContractInOverdueMoreThanOneMonth(principalCashflows, installmentDate);
			if (contractInOverdueMoreThanOneMonth) {
				interestInSuspend = revenueSchedules.getSchedules().get(i).getRevenueAmount();
			} else {
				revenue = revenueSchedules.getSchedules().get(i).getRevenueAmount();
				interestInSuspend = revenue;
			}
			
			interestInSuspend = revenueSchedules.getSchedules().get(i).getRevenueAmount();
			
			for (Cashflow interestCashflow : interestRepaymentCashflows) {
				interestIncomeRepayment += interestCashflow.getTiInstallmentAmount();
			}
			
			if (i == 0) {
				interestIncomeReceivable = revenue - interestIncomeRepayment;
			} else {
				interestIncomeReceivable = (prevSchedule.getInterestIncomeReceivable() + interestInSuspend) -  interestIncomeRepayment;
				interestInSuspendCumulated = prevSchedule.getInterestInSuspendCumulated();
			}
			
			interestInSuspendCumulated += interestInSuspend;
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setPrincipalBalance(principalBalance);
			schedule.setNumInstallment(numInstallment);
			
			schedule.setInterestIncomeReceivable(interestIncomeReceivable);
			schedule.setInterestRevenue(revenue);
			schedule.setInterestInSuspend(interestInSuspend);
			schedule.setInterestInSuspendCumulated(interestInSuspendCumulated);
			schedule.setContractInOverdueMoreThanOneMonth(contractInOverdueMoreThanOneMonth);
			schedule.setInterestRepaymentCashflows(interestRepaymentCashflows);
			schedule.setPrincipalRepaymentCashflows(principalRepaymentCashflows);
			schedule.setPenaltyCashflows(penaltyCashflows);
			
			schedules.add(schedule);
			
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			
			if (periodEndDate.compareTo(searchEndDate) >= 0) {
				break;
			}
		}
		
		interestIncomeSchedules.setPrincipalCashflows(principalCashflows);
		interestIncomeSchedules.setInterestCashflows(interestCashflows);
		interestIncomeSchedules.setSchedules(schedules);
		
		
		return interestIncomeSchedules;
	}
	
	/**
	 * 
	 * @param calculatedCashflows
	 * @return
	 */
	private List<Cashflow> getCashflowsByType(List<Cashflow> calculatedCashflows, ECashflowType cashflowType) {
		List<Cashflow> cashflows = new ArrayList<>();
		for (Cashflow cashflow : calculatedCashflows) {
			if (!cashflow.isCancel() && cashflow.getCashflowType() == cashflowType) {
				cashflows.add(cashflow);
			}
		}
		return cashflows;
	}
	
	
	/**
	 * @param calculatedCashflows
	 * @return
	 */
	private List<Cashflow> getRepaymentByCashflowType(Date startDate, Date endDate, ECashflowType cashflowType, List<Cashflow> cashflows) {
		List<Cashflow> repaymentCashflows = new ArrayList<>();
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType() == cashflowType
					&& cashflow.isPaid()
					&& !cashflow.isCancel()
					&& cashflow.getPayment() != null
					&& DateUtils.getDateAtBeginningOfDay(startDate).compareTo(cashflow.getPayment().getPaymentDate()) <= 0 
					&& DateUtils.getDateAtEndOfDay(endDate).compareTo(cashflow.getPayment().getPaymentDate()) >= 0) {
				repaymentCashflows.add(cashflow);
			}
		}
		return repaymentCashflows;
	}
	
	/**
	 * 
	 * @param cashflow
	 * @param calDate
	 * @return
	 */
	protected boolean isContractOverdue30Days(List<Cashflow> cashflows, Date calDate) {
		for (Cashflow cashflow : cashflows) {
			Date installmentDate = cashflow.getInstallmentDate();
			if (calDate.compareTo(installmentDate) >= 0 && !DateUtils.withinInterval(calDate, DateUtils.getDateAtBeginningOfMonth(installmentDate), DateUtils.getDateAtEndOfMonth(installmentDate))) {
				int numOverdueDays = 0;
				if (!cashflow.isPaid()) {
					numOverdueDays = DateUtils.getDiffInDaysPlusOneDay(calDate, installmentDate).intValue();
				} else if (cashflow.getPayment() != null) {
					Date paymentDate = cashflow.getPayment().getPaymentDate();
					if (calDate.compareTo(paymentDate) < 0) {
						numOverdueDays = DateUtils.getDiffInDaysPlusOneDay(calDate, installmentDate).intValue();
					}
				}
				if (numOverdueDays >= 30) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isContractInOverdueMoreThanOneMonth(List<Cashflow> cashflows, Date calDate) {
		for (Cashflow cashflow : cashflows) {
			Date installmentDate = cashflow.getInstallmentDate();
			Date overdueDate = null;
			if (calDate.compareTo(installmentDate) >= 0 && !DateUtils.withinInterval(calDate, DateUtils.getDateAtBeginningOfMonth(installmentDate), DateUtils.getDateAtEndOfMonth(installmentDate))) {
				if (!cashflow.isPaid()) {
					overdueDate = DateUtils.addMonthsDate(installmentDate, 2);
				} else if (cashflow.getPayment() != null) {
					Date paymentDate = cashflow.getPayment().getPaymentDate();
					if (calDate.compareTo(paymentDate) < 0) {
						overdueDate = DateUtils.addMonthsDate(installmentDate, 2);	
					}
				}
				
				if (overdueDate != null && overdueDate.compareTo(calDate) <= 0) {
					return true;
				}
			}
		}
		return false;
	}
		
	public Amount getPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount principalBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {			
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				principalBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				principalBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				principalBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return principalBalance;
	}
		
	
	private double getRevenue() {
		return 0d;
	}
	
	/*	
	public Amount getInterestRevenue(Date startDate, Date endDate, List<Cashflow> cashflows) {
		Amount interestIncome = new Amount(0d, 0d, 0d);
		
		for (Cashflow cashflow : cashflows) {
			Date periodStartDate = DateUtils.getDateAtBeginningOfDay(cashflow.getPeriodStartDate());
			Date periodEndDate = DateUtils.getDateAtBeginningOfDay(cashflow.getPeriodEndDate());
			if (cashflow.getCashflowType() == CashflowType.IAP
					&& !cashflow.isCancel()	
					&& (
						(periodStartDate.compareTo(startDate) >= 0 && periodStartDate.compareTo(endDate) <= 0)
						||
						(periodEndDate.compareTo(startDate) >= 0 && periodEndDate.compareTo(endDate) <= 0)
					)
				) {
				Date calculStartDate = periodStartDate;
				if (DateUtils.isBeforeDay(calculStartDate, startDate)) {
					calculStartDate = startDate;
				}
				Date calculEndDate = DateUtils.addDaysDate(periodEndDate, 1);
				if (DateUtils.isBeforeDay(endDate, calculEndDate)) {
					calculEndDate = endDate;
				}
				long coeff = 30; // DateUtils.getDiffInDaysPlusOneDay(periodEndDate, periodStartDate);
				long nbDays = 0;
				if (DateUtils.isAfterDay(periodEndDate, calculEndDate)) {
					nbDays = DateUtils.getDiffInDaysPlusOneDay(calculEndDate, calculStartDate);
				} else {
					nbDays = 30 - DateUtils.getDiffInDays(calculStartDate, periodStartDate);
				}
				double teAmountUsd = 0d;
				double vatAmountUsd = 0d;
				double tiAmountUsd = 0d;
				
				teAmountUsd = MathUtils.roundAmountTo((cashflow.getTeInstallmentAmount() / coeff) * nbDays);
				vatAmountUsd = MathUtils.roundAmountTo((cashflow.getVatInstallmentAmount() / coeff) * nbDays);
				tiAmountUsd = MathUtils.roundAmountTo((cashflow.getTiInstallmentAmount() / coeff) * nbDays);
				interestIncome.plus(new Amount(teAmountUsd, vatAmountUsd, tiAmountUsd));
			}
		}
		return interestIncome;
	}*/
}
