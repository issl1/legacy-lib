package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.RevenueIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.RevenueIncomeSchedules;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInterestIncomeDailyCalculatorImpl implements FinServicesHelper {
	
	private static String NUM_OF_MONTHS_MINUS_REVENU = "num.of.months.minus.revenu";
	private static String MINUS_REVENUE_WHEN_OVERDUE = "minus.revenue.when.overdue";
	
		
	Logger logger = LoggerFactory.getLogger(GLFInterestIncomeDailyCalculatorImpl.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public InterestIncomeSchedules getSchedules(Contract contract, Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, 
			CalculationParameter calculationParameter2, List<Cashflow> cashflows, Date searchStartDate, Date searchEndDate, EWkfStatus contractStatus, Date eventDate) {
		
		InterestIncomeSchedules interestIncomeSchedules = new InterestIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		GLFInterestIncomeCalculatorImpl2 interestIncomeCalculator = new GLFInterestIncomeCalculatorImpl2();
		RevenueIncomeSchedules monthlySchedules = null;
		if (isSameMonth(DateUtils.addMonthsDate(startDate, 1), firstInstallmentDate)) {
			monthlySchedules = interestIncomeCalculator.getSchedulesCase1(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
		} else if (isSameMonth(startDate, firstInstallmentDate)) {
			monthlySchedules = interestIncomeCalculator.getSchedulesCase2(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
		} else {
			monthlySchedules = interestIncomeCalculator.getSchedulesCase3(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
		}
		
		List<InterestIncomeSchedule> schedules = new ArrayList<>();
					
		Date installmentDate = null;
		Date periodStartDate = null;
		Date periodEndDate = null;
						
		int numInstallment = 0;
		
		boolean contractHasOverdueMoreThanOneMonth = false;
		boolean contractInOverdueMoreThanOneMonth = false;
				
		if (eventDate != null && searchEndDate.compareTo(eventDate) > 0) {
			searchEndDate = eventDate;
		}
		
		long nbDays = DateUtils.getDiffInDaysPlusOneDay(searchEndDate, startDate);
		
		List<Cashflow> principalCashflows = getCashflowsByType(cashflows, ECashflowType.CAP);
		List<Cashflow> interestCashflows = getCashflowsByType(cashflows, ECashflowType.IAP);
		List<Cashflow> penaltyCashflows = getCashflowsByType(cashflows, ECashflowType.PEN);
		
		double revenuePerDay = 0d;
		Date endInstallmentDate = DateUtils.addMonthsDate(firstInstallmentDate, calculationParameter.getNumberOfPeriods() - 1);
		
		int numberOfMonthsInOverdue = SETTING_SRV.getValueInt(NUM_OF_MONTHS_MINUS_REVENU, 1);
		int minusRevenueWhenOverdue = SETTING_SRV.getValueInt(MINUS_REVENUE_WHEN_OVERDUE, 1);
		
		for (int i = 0; i < nbDays; i++) {
			
			InterestIncomeSchedule schedule = new InterestIncomeSchedule();
			InterestIncomeSchedule prevSchedule = null;
			if (i > 0) {
				prevSchedule = schedules.get(i - 1);
			}
			
			double interestInSuspend = 0d;
			double interestInSuspendCumulated = 0d;
			double interestIncomeRepayment = 0d;
			double interestIncomeReceivable = 0d;
			double revenue = 0d;
			// double principalBalance = 0d;
			
			
			if (i == 0) {
				installmentDate = DateUtils.getDateAtBeginningOfDay(startDate);
			} else {
				installmentDate = DateUtils.addDaysDate(installmentDate, 1);
			}
			
			periodStartDate = installmentDate;
			periodEndDate = installmentDate;
			
			List<Cashflow> principalRepaymentCashflows = null;
			List<Cashflow> interestRepaymentCashflows = null;
			List<Cashflow> penaltyRepaymentCashflows = null;
			
			if (eventDate != null && isSameDay(eventDate, installmentDate)) {
				principalRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, ECashflowType.CAP, principalCashflows);
				interestRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, ECashflowType.IAP, interestCashflows);
				penaltyRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, ECashflowType.PEN, penaltyCashflows);
				if (contract.getWkfStatus().equals(ContractWkfStatus.EAR)) {
					for (Cashflow interestCashflow : interestRepaymentCashflows) {
						revenue += interestCashflow.getTiInstallmentAmount();
					}
					revenue -= prevSchedule.getInterestIncomeReceivable();
				} else {
					revenue = 0d;
				}
			} else {
			
				// principalBalance = getPrincipalBalance(periodEndDate, principalCashflows).getTiAmountUsd();
				principalRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.CAP, principalCashflows);
				interestRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.IAP, interestCashflows);
				penaltyRepaymentCashflows = getRepaymentByCashflowType(periodStartDate, periodEndDate, ECashflowType.PEN, penaltyCashflows);
				
				contractHasOverdueMoreThanOneMonth = isContractHasOverdueMoreThanXMonths(principalCashflows, installmentDate, numberOfMonthsInOverdue);
				contractInOverdueMoreThanOneMonth = isContractInOverdueMoreThanXMonths(principalCashflows, installmentDate, searchEndDate, numberOfMonthsInOverdue);
				
				//if (prevSchedule == null || !isSameMonth(prevSchedule.getInstallmentDate(), installmentDate)) {
					revenuePerDay = getRevenueOfDay(monthlySchedules, installmentDate);
				//}
				
				if (contractInOverdueMoreThanOneMonth) {
					interestInSuspend = revenuePerDay;
				}
				
				for (Cashflow interestCashflow : interestRepaymentCashflows) {
					interestIncomeRepayment += interestCashflow.getTiInstallmentAmount();
				}
				
				if (i == 0) {
					interestIncomeReceivable = revenuePerDay -  interestIncomeRepayment;
				} else {
					interestIncomeReceivable = prevSchedule.getInterestIncomeReceivable() + revenuePerDay -  interestIncomeRepayment;
				}
						
				if (contractHasOverdueMoreThanOneMonth) {
					
					if (prevSchedule != null && prevSchedule.isContractInOverdueMoreThanOneMonth()) {
						revenue = 0;
						// stop calculate interest income receivable
						interestIncomeReceivable = 0;
						interestInSuspendCumulated = prevSchedule.getInterestInSuspendCumulated() + revenuePerDay;
					} else {
						Date calEndDate = DateUtils.addDaysDate(installmentDate, -1);
						Date calStartDate = DateUtils.addMonthsDate(DateUtils.getDateAtBeginningOfDay(calEndDate), -1 * (numberOfMonthsInOverdue + 1));
						calStartDate = DateUtils.addDaysDate(calStartDate, 1);
						if (calEndDate.compareTo(endInstallmentDate) > 0) {
							calEndDate = endInstallmentDate;
						}
						interestInSuspendCumulated = getInterestInSupendCumulated(monthlySchedules, calStartDate, calEndDate);
						if (minusRevenueWhenOverdue == 1) {
							revenue = -1 * interestIncomeReceivable;
						}
						interestInSuspendCumulated += revenuePerDay;
						
					}
				} else {
					revenue = revenuePerDay;
					if (prevSchedule != null) {
						revenue += prevSchedule.getInterestInSuspendCumulated();
					}
				}
				
				for (Cashflow interestCashflow : interestRepaymentCashflows) {
					Date paymentDate = DateUtils.addMonthsDate(DateUtils.getDateAtBeginningOfDay(interestCashflow.getPayment().getPaymentDate()), -1);
					Date calDate = DateUtils.getDateAtBeginningOfDay(interestCashflow.getInstallmentDate());
					if (contractHasOverdueMoreThanOneMonth && prevSchedule != null && prevSchedule.isContractInOverdueMoreThanOneMonth() && calDate.compareTo(paymentDate) < 0) {
						double revenueOfInstallment = getRevenueOfInstallment(monthlySchedules, calDate);
						revenue += revenueOfInstallment;
						interestInSuspendCumulated -= revenueOfInstallment;
					}
				}
			}
			
			schedule.setN(i + 1);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			// schedule.setPrincipalBalance(principalBalance);
			schedule.setNumInstallment(numInstallment);
			schedule.setInterestIncomeReceivable(interestIncomeReceivable);
			schedule.setInterestRevenue(revenue);
			schedule.setInterestInSuspend(interestInSuspend);
			schedule.setInterestInSuspendCumulated(interestInSuspendCumulated);
			schedule.setContractInOverdueMoreThanOneMonth(contractHasOverdueMoreThanOneMonth);
			schedule.setInterestRepaymentCashflows(interestRepaymentCashflows);
			schedule.setPrincipalRepaymentCashflows(principalRepaymentCashflows);
			schedule.setPenaltyCashflows(penaltyRepaymentCashflows);
			
			schedules.add(schedule);
		}
		
		/*
		if (!schedules.isEmpty()) {
			double principalBalance = getPrincipalBalance(periodEndDate, principalCashflows).getTiAmountUsd();
			schedules.get(schedules.size() - 1).setPrincipalBalance(principalBalance);
		}*/
		
		interestIncomeSchedules.setSchedules(schedules);
		
		return interestIncomeSchedules;
	}
	
	
	/**
	 * @param revenueSchedules
	 * @param calDate
	 * @return
	 */
	private double getRevenueOfDay(RevenueIncomeSchedules monthlySchedules, Date calDate) {
		double revenueOfDay = 0d;
		for (RevenueIncomeSchedule revenueIncomeSchedule : monthlySchedules.getSchedules()) {
			if (isSameMonth(calDate, revenueIncomeSchedule.getInstallmentDate())) {
				if (revenueIncomeSchedule.getInstallmentDate().compareTo(calDate) < 0
						|| (monthlySchedules.getRunningCase() != 2 && isSameDay(calDate, monthlySchedules.getStartDate()))) {
					revenueOfDay = revenueIncomeSchedule.getDailyInterest1();
				} else {
					revenueOfDay = revenueIncomeSchedule.getDailyInterest2();
				}
				break;
			}
		}
		return MyMathUtils.roundTo(revenueOfDay, 8);
	}
	
	/**
	 * @param revenueSchedules
	 * @param calDate
	 * @return
	 */
	private double getInterestInSupendCumulated(RevenueIncomeSchedules monthlySchedules, Date calStartDate, Date calEndDate) {
		double interestInSupendCumulated = 0d;
		Date startDate = calStartDate;
		Date endDate = null;
		while (startDate.compareTo(calEndDate) < 0) {
			endDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(startDate));
			if (endDate.compareTo(calEndDate) > 0) {
				endDate = calEndDate;
			}
			long nbDays = DateUtils.getDiffInDaysPlusOneDay(endDate, startDate);
			double revenueOfDay = getRevenueOfDay(monthlySchedules, startDate);
			interestInSupendCumulated += nbDays * revenueOfDay;
			startDate = DateUtils.addDaysDate(endDate, 1);
		}
		return interestInSupendCumulated;
	}
	
	/**
	 * @param monthlySchedules
	 * @param calDate
	 * @return
	 */
	private double getRevenueOfInstallment(RevenueIncomeSchedules monthlySchedules, Date calDate) {
		double revenueOfInstallment = 0d;
		Date startDate = DateUtils.addMonthsDate(DateUtils.getDateAtBeginningOfDay(calDate), -1);
		startDate = DateUtils.addDaysDate(startDate, 1);
		Date endDate = null;
		while (startDate.compareTo(calDate) < 0) {
			endDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(startDate));
			if (endDate.compareTo(calDate) > 0) {
				endDate = calDate;
			}
			long nbDays = DateUtils.getDiffInDaysPlusOneDay(endDate, startDate);
			double revenueOfDay = getRevenueOfDay(monthlySchedules, startDate);
			revenueOfInstallment += nbDays * revenueOfDay;
			// System.out.println(DateUtils.date2StringDDMMYYYY_SLASH(startDate) + "," + DateUtils.date2StringDDMMYYYY_SLASH(endDate) + " : " + nbDays * revenueOfDay);
			startDate = DateUtils.addDaysDate(endDate, 1);
		}
		return revenueOfInstallment;
	}
	
	/**
	 * @param cashflows
	 * @param calDate
	 * @return
	 */
	private boolean isContractInOverdueMoreThanXMonths(List<Cashflow> cashflows, Date calDate, Date endSearchDate, int numberOfMonthsInOverdue) {
		for (Cashflow cashflow : cashflows) {
			Date installmentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
			Date overdueDate = null;
			if (calDate.compareTo(installmentDate) >= 0 && !DateUtils.withinInterval(calDate, DateUtils.getDateAtBeginningOfMonth(installmentDate), DateUtils.getDateAtEndOfMonth(installmentDate))) {
				if (!cashflow.isPaid()) {
					overdueDate = DateUtils.addMonthsDate(installmentDate, numberOfMonthsInOverdue);
					overdueDate = DateUtils.addDaysDate(overdueDate, 1);
				} else if (cashflow.getPayment() != null) {
					Date paymentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getPayment().getPaymentDate());
					if (paymentDate.compareTo(endSearchDate) > 0 && calDate.compareTo(paymentDate) < 0) {
						overdueDate = DateUtils.addMonthsDate(installmentDate, numberOfMonthsInOverdue);
						overdueDate = DateUtils.addDaysDate(overdueDate, 1);
					}
				}
				if (overdueDate != null) {
					if (overdueDate.compareTo(calDate) <= 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean isContractHasOverdueMoreThanXMonths(List<Cashflow> cashflows, Date calDate, int numberOfMonthsInOverdue) {
		for (Cashflow cashflow : cashflows) {
			Date installmentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate());
			Date overdueDate = null;
			if (calDate.compareTo(installmentDate) >= 0 && !DateUtils.withinInterval(calDate, DateUtils.getDateAtBeginningOfMonth(installmentDate), DateUtils.getDateAtEndOfMonth(installmentDate))) {
				if (!cashflow.isPaid()) {
					overdueDate = DateUtils.addMonthsDate(installmentDate, numberOfMonthsInOverdue);
					overdueDate = DateUtils.addDaysDate(overdueDate, 1);
				} else if (cashflow.getPayment() != null) {
					Date paymentDate = DateUtils.getDateAtBeginningOfDay(cashflow.getPayment().getPaymentDate());
					if (calDate.compareTo(paymentDate) < 0) {
						overdueDate = DateUtils.addMonthsDate(installmentDate, numberOfMonthsInOverdue);
						overdueDate = DateUtils.addDaysDate(overdueDate, 1);
					}
				}
				if (overdueDate != null) {
					if (overdueDate.compareTo(calDate) <= 0) {
						return true;
					}
				}
			}
		}
		return false;
	} 
	
	/**
	 * 
	 * @param calculatedCashflows
	 * @return
	 */
	private List<Cashflow> getCashflowsByType(List<Cashflow> calculatedCashflows, ECashflowType cashflowType) {
		List<Cashflow> cashflows = new ArrayList<>();
		for (Cashflow cashflow : calculatedCashflows) {
			if ((!cashflow.isCancel() || cashflow.getCashflowCode() != null) && cashflow.getCashflowType() == cashflowType) {
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
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType() == cashflowType
						&& cashflow.isPaid()
						&& !cashflow.isCancel()
						&& cashflow.getPayment() != null) {
					Date installmentDate = cashflow.getInstallmentDate();
					if (installmentDate.compareTo(cashflow.getPayment().getPaymentDate()) < 0) {
						installmentDate = cashflow.getPayment().getPaymentDate();
					}
					installmentDate = DateUtils.getDateAtBeginningOfDay(installmentDate);
					if (DateUtils.getDateAtBeginningOfDay(startDate).compareTo(installmentDate) <= 0 
							&& DateUtils.getDateAtEndOfDay(endDate).compareTo(installmentDate) >= 0) {
						repaymentCashflows.add(cashflow);
					}
				}
			}
		}
		return repaymentCashflows;
	}
	
	private List<Cashflow> getRepaymentByCashflowType(Date startDate, ECashflowType cashflowType, List<Cashflow> cashflows) {
		List<Cashflow> repaymentCashflows = new ArrayList<>();
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType() == cashflowType
						&& cashflow.isPaid()
						&& !cashflow.isCancel()
						&& cashflow.getPayment() != null) {					
					Date installmentDate = cashflow.getInstallmentDate();
					if (installmentDate.compareTo(cashflow.getPayment().getPaymentDate()) < 0) {
						installmentDate = cashflow.getPayment().getPaymentDate();
					}
					if (DateUtils.getDateAtBeginningOfDay(startDate).compareTo(installmentDate) <= 0) {
						repaymentCashflows.add(cashflow);
					}
				}
			}
		}
		return repaymentCashflows;
	}
	
	protected Amount getPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
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
	
	/**
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean isSameMonth(Date date1, Date date2) {
		return DateUtils.getDateLabel(date1, "MMyyyy").equals(DateUtils.getDateLabel(date2, "MMyyyy"));
	}
	
	/**
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean isSameDay(Date date1, Date date2) {
		return DateUtils.getDateLabel(date1, "ddMMyyyy").equals(DateUtils.getDateLabel(date2, "ddMMyyyy"));
	}
	

}
