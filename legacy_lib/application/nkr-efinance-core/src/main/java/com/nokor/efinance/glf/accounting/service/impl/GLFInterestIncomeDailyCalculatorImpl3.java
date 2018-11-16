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
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInterestIncomeDailyCalculatorImpl3 {
		
	Logger logger = LoggerFactory.getLogger(GLFInterestIncomeDailyCalculatorImpl3.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public InterestIncomeSchedules getSchedules(Date calculDate, Date searchEndDate, 
			InterestIncomeSchedules monthySchedules, int index,
			List<Cashflow> principalCashflows) {
		
		InterestIncomeSchedules interestIncomeSchedules = new InterestIncomeSchedules(calculDate, searchEndDate, new CalculationParameter());
		
		List<InterestIncomeSchedule> schedules = new ArrayList<>();
					
		Date installmentDate = null;
		Date periodStartDate = null;
		Date periodEndDate = null;
						
		int numInstallment = 0;
		boolean overdueMoreThan30Days = false;
		
		InterestIncomeSchedule monthlySchedule = monthySchedules.getSchedules().get(index);
		InterestIncomeSchedule prevMonthlySchedule = null;
		if (index > 0) {
			prevMonthlySchedule = monthySchedules.getSchedules().get(index - 1);
		}
		
		long nbDaysInMonth = DateUtils.getNbDaysInMonth(calculDate);
		double revenuePerDay = monthlySchedule.getInterestInSuspend() / nbDaysInMonth;
		
		for (int i = 0; i < nbDaysInMonth; i++) {
			
			InterestIncomeSchedule schedule = new InterestIncomeSchedule();			
			
			double interestInSuspend = 0d;
			double interestInSuspendCumulated = 0d;
			double interestIncomeRepayment = 0d;
			double interestIncomeReceivable = 0d;
			double revenue = 0d;
			double principalBalance = 0d;
			double revenueRepayment = 0d;
			double currentRevenueRepayment = 0d;
			
			InterestIncomeSchedule prevSchedule = null;
			if (i > 0) {
				prevSchedule = schedules.get(i - 1);
			}
			
			if (i == 0) {
				installmentDate = DateUtils.getDateAtBeginningOfMonth(calculDate);
				if (prevMonthlySchedule != null) {
					interestInSuspendCumulated = prevMonthlySchedule.getInterestInSuspendCumulated();
				}
			} else {
				installmentDate = DateUtils.addDaysDate(installmentDate, 1);
			}
			
			periodStartDate = installmentDate;
			periodEndDate = installmentDate;
			
			principalBalance = getPrincipalBalance(periodEndDate, principalCashflows).getTiAmount();
			List<Cashflow> principalRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.CAP, monthlySchedule.getPrincipalRepaymentCashflows());
			List<Cashflow> interestRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.IAP, monthlySchedule.getInterestRepaymentCashflows());
			List<Cashflow> penaltyCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.PEN, monthlySchedule.getPenaltyCashflows());
										
			overdueMoreThan30Days = isContractOverdue30Days(principalCashflows, installmentDate);
			
			overdueMoreThan30Days = isContractOverdue30Days(principalCashflows, installmentDate);
			if (overdueMoreThan30Days) {
				if (prevSchedule != null && !prevSchedule.isContractInOverdueMoreThanOneMonth()) { 
					revenue = -1 * prevSchedule.getInterestInSuspendCumulated();
				} else {
					revenue = 0;
				}
				interestInSuspend = revenuePerDay;
			} else {
				revenue = revenuePerDay;
				interestInSuspend = revenue;
			}
			
			for (Cashflow interestCashflow : interestRepaymentCashflows) {
				interestIncomeRepayment += interestCashflow.getTiInstallmentAmount();
				revenueRepayment += revenuePerDay;
				if (monthlySchedule.getNumInstallment() != interestCashflow.getNumInstallment()) {
					if (monthlySchedule.isContractInOverdueMoreThanOneMonth()) {
						currentRevenueRepayment += monthySchedules.getSchedules().get(interestCashflow.getNumInstallment()).getInterestInSuspend();
					}
				} else if (monthlySchedule.isContractInOverdueMoreThanOneMonth() && monthlySchedule.getNumInstallment() == interestCashflow.getNumInstallment()) {
					currentRevenueRepayment +=  (revenuePerDay * i);
				}
				// schedule.setCurrentInterestRepayment(i == interestCashflow.getNumInstallment());
			}
			
			if (i == 0) {
				if (prevMonthlySchedule != null) {
					interestIncomeReceivable = prevMonthlySchedule.getInterestIncomeReceivable() + interestInSuspend - interestIncomeRepayment;
				} else {
					interestIncomeReceivable = interestInSuspend - interestIncomeRepayment;
				}
			} else {
				interestIncomeReceivable = (prevSchedule.getInterestIncomeReceivable() + interestInSuspend) -  interestIncomeRepayment;
				interestInSuspendCumulated = prevSchedule.getInterestInSuspendCumulated();
			}
						
			revenue += currentRevenueRepayment;
			interestInSuspendCumulated += (interestInSuspend - revenueRepayment);
						
			schedule.setN(i + 1);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setPrincipalBalance(principalBalance);
			schedule.setNumInstallment(numInstallment);
			schedule.setInterestIncomeReceivable(interestIncomeReceivable);
			schedule.setInterestRevenue(revenue);
			schedule.setInterestInSuspend(interestInSuspend);
			schedule.setInterestInSuspendCumulated(interestInSuspendCumulated);
			schedule.setContractInOverdueMoreThanOneMonth(overdueMoreThan30Days);
			schedule.setInterestRepaymentCashflows(interestRepaymentCashflows);
			schedule.setPrincipalRepaymentCashflows(principalRepaymentCashflows);
			schedule.setPenaltyCashflows(penaltyCashflows);
			
			schedules.add(schedule);
			
			/*
			if (periodEndDate.compareTo(searchEndDate) >= 0) {
				break;
			}*/
		}
		
		interestIncomeSchedules.setSchedules(schedules);
		
		return interestIncomeSchedules;
	}
			
	/**
	 * @param calculatedCashflows
	 * @return
	 */
	private List<Cashflow> getRepaymentByCashflowType(Date startDate, Date endDate, ECashflowType cashflowType, List<Cashflow> cashflows) {
		List<Cashflow> repaymentCashflows = new ArrayList<>();
		if (cashflows != null && !cashflows.isEmpty()) {
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
		}
		return repaymentCashflows;
	}
	
	/**
	 * 
	 * @param cashflow
	 * @param paymentDate
	 * @return
	 */
	private boolean isContractOverdue30Days(List<Cashflow> cashflows, Date calDate) {
		for (Cashflow cashflow : cashflows) {
			Date installmentDate = cashflow.getInstallmentDate();
			if (calDate.compareTo(installmentDate) >= 0 && !DateUtils.withinInterval(calDate, DateUtils.getDateAtBeginningOfMonth(installmentDate), DateUtils.getDateAtEndOfMonth(installmentDate))) {
				int numOverdueDays = 0;
				if (!cashflow.isPaid()) {
					numOverdueDays = DateUtils.getDiffInDaysPlusOneDay(calDate, installmentDate).intValue();
				} else if (cashflow.getPayment() != null) {
					Date paymentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getPayment().getPaymentDate());
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
		
	private Amount getPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
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
}
