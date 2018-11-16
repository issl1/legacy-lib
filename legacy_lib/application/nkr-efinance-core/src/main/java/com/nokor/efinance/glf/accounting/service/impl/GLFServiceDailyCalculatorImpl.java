package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.accounting.ServiceIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.ServiceIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.ServiceSchedule;
import com.nokor.efinance.core.shared.accounting.ServiceSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFServiceDailyCalculatorImpl implements FinServicesHelper {
	
	private static String NUM_OF_MONTHS_MINUS_REVENU = "num.of.months.minus.revenu";
	private static String MINUS_REVENUE_WHEN_OVERDUE = "minus.revenue.when.overdue";
	
		
	Logger logger = LoggerFactory.getLogger(GLFServiceDailyCalculatorImpl.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public ServiceIncomeSchedules getSchedules(Contract contract, Date startDate, Date firstInstallmentDate, double serviceAmount, 
			CalculationParameter calculationParameter, CalculationParameter calculationParameter2, 
			List<Cashflow> cashflows, Date searchStartDate, Date searchEndDate, EWkfStatus contractStatus, Date eventDate) {
		
		ServiceIncomeSchedules serviceIncomeSchedules = new ServiceIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		GLFServiceCalculatorImpl2 serviceCalculator = new GLFServiceCalculatorImpl2();
		ServiceSchedules serviceSchedules = null;
		if (isSameMonth(DateUtils.addMonthsDate(startDate, 1), firstInstallmentDate)) {
			serviceSchedules = serviceCalculator.getSchedulesCase1(startDate, firstInstallmentDate, serviceAmount, calculationParameter, calculationParameter2);
		} else if (isSameMonth(startDate, firstInstallmentDate)) {
			serviceSchedules = serviceCalculator.getSchedulesCase2(startDate, firstInstallmentDate, serviceAmount, calculationParameter, calculationParameter2);
		} else {
			serviceSchedules = serviceCalculator.getSchedulesCase3(startDate, firstInstallmentDate, serviceAmount, calculationParameter, calculationParameter2);
		}
		
		List<ServiceIncomeSchedule> schedules = new ArrayList<>();
					
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
				
		double revenuePerDay = 0d;
		
		int numberOfMonthsInOverdue = SETTING_SRV.getValueInt(NUM_OF_MONTHS_MINUS_REVENU, 1);
		int minusRevenueWhenOverdue = SETTING_SRV.getValueInt(MINUS_REVENUE_WHEN_OVERDUE, 1);
		
		for (int i = 0; i < nbDays; i++) {
			
			ServiceIncomeSchedule schedule = new ServiceIncomeSchedule();
						
			ServiceIncomeSchedule prevSchedule = null;
			if (i > 0) {
				prevSchedule = schedules.get(i - 1);
			}
			
			double accruedIncome = 0d;
			double principalRepayment = 0d;
			
			double revenue = 0d;
			
			if (i == 0) {
				installmentDate = DateUtils.getDateAtBeginningOfDay(startDate);
			} else {
				installmentDate = DateUtils.addDaysDate(installmentDate, 1);
			}
			
			periodStartDate = installmentDate;
			periodEndDate = installmentDate;
			
			List<Cashflow> principalRepaymentCashflows = null;
			
			if (eventDate != null && isSameDay(eventDate, installmentDate)) {
				principalRepaymentCashflows = getRepaymentCashflows(periodStartDate, cashflows);
				revenue = 0d;
			} else {
			
				principalRepaymentCashflows = getRepaymentCashflows(periodStartDate, periodEndDate, cashflows);
				
				// contractHasOverdueMoreThanOneMonth = isContractHasOverdueMoreThanXMonths(cashflows, installmentDate, numberOfMonthsInOverdue);
				// contractInOverdueMoreThanOneMonth = isContractInOverdueMoreThanXMonths(cashflows, installmentDate, searchEndDate, numberOfMonthsInOverdue);
				
				revenuePerDay = getRevenueOfDay(serviceSchedules, installmentDate);
				
				for (Cashflow serviceCashflow : principalRepaymentCashflows) {
					principalRepayment += serviceCashflow.getTiInstallmentAmount();
				}
								
				if (i == 0) {
					accruedIncome = revenuePerDay;
				} else {
					accruedIncome = prevSchedule.getAccruedIncome() + revenuePerDay -  principalRepayment;
				}
						
				if (contractHasOverdueMoreThanOneMonth) {
					
					if (prevSchedule != null && prevSchedule.isContractInOverdueMoreThanOneMonth()) {
						revenue = 0;
						// stop calculate interest income receivable
						accruedIncome = 0;
					} else {
						if (minusRevenueWhenOverdue == 1) {
							revenue = -1 * accruedIncome;
						}
					}
				} else {
					revenue = revenuePerDay;		
				}
			}
			
			schedule.setN(i + 1);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setNumInstallment(numInstallment);
			schedule.setRevenue(revenue);
			schedule.setAccruedIncome(accruedIncome);
			schedule.setPrincipalRepaymentCashflows(principalRepaymentCashflows);			
			schedules.add(schedule);
		}
		
		serviceIncomeSchedules.setSchedules(schedules);
		
		return serviceIncomeSchedules;
	}
	
	
	/**
	 * @param revenueSchedules
	 * @param calDate
	 * @return
	 */
	private double getRevenueOfDay(ServiceSchedules serviceSchedules, Date calDate) {
		double revenueOfDay = 0d;
		for (ServiceSchedule serviceSchedule : serviceSchedules.getSchedules()) {
			if (isSameMonth(calDate, serviceSchedule.getInstallmentDate())) {
				if (serviceSchedule.getInstallmentDate().compareTo(calDate) < 0 || isSameDay(calDate, serviceSchedules.getStartDate())) {
					revenueOfDay = serviceSchedule.getDailyInterest2();
				} else {
					revenueOfDay = serviceSchedule.getDailyInterest1();
				}
				break;
			}
		}
		return MyMathUtils.roundTo(revenueOfDay, 8);
	}
		
	
	
	
	/**
	 * @param calculatedCashflows
	 * @return
	 */
	private List<Cashflow> getRepaymentCashflows(Date startDate, Date endDate, List<Cashflow> cashflows) {
		List<Cashflow> repaymentCashflows = new ArrayList<>();
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (cashflow.isPaid()
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
	
	private List<Cashflow> getRepaymentCashflows(Date startDate, List<Cashflow> cashflows) {
		List<Cashflow> repaymentCashflows = new ArrayList<>();
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (cashflow.isPaid()
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
