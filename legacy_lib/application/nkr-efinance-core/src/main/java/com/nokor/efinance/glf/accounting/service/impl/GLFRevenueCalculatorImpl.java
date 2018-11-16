package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.accounting.RevenueSchedule;
import com.nokor.efinance.core.shared.accounting.RevenueSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFRevenueCalculatorImpl {
		
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param servicingCashflows
	 * @return
	 */
	public RevenueSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		
		RevenueSchedules revenueSchedules = new RevenueSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<RevenueSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		double installmentPayment = calculateInstallmentPayment(calculationParameter);
		
		double irrRate = Rate.calculateIRR(numberOfPeriods, installmentPayment, initialPrincipal);
						
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		for (int i = 0; i <= numberOfPeriods; i++) {
			RevenueSchedule schedule = new RevenueSchedule();
			
			double revenueAmount = 0d;
			
			if (i == 0) {
				installmentDate = firstInstallmentDate;
				periodStartDate = startDate;
			} else if (i == 1) {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
				periodStartDate = firstInstallmentDate;
			} else {
				periodStartDate = DateUtils.addMonthsDate(periodStartDate, 1);
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtEndOfMonth(periodStartDate);
			
			double interestAmount =  0d;
			double principalAmount = 0d;
			
			if (i > 0) {
				interestAmount =  initialPrincipal * irrRate;
				principalAmount = installmentPayment - interestAmount;
			}
			
			double balanceAmount = initialPrincipal - principalAmount;
			if (balanceAmount < 0 || (i == numberOfPeriods)) {
				balanceAmount = 0.0;
			}
			
			if (i == 0) {
				revenueAmount = (balanceAmount * irrRate * DateUtils.getDiffInDaysPlusOneDay(periodEndDate, periodStartDate)) / 30;
			} else {
				RevenueSchedule prevSchedule = schedules.get(i - 1);
				revenueAmount = ((balanceAmount * irrRate * DateUtils.getDiffInDaysPlusOneDay(periodEndDate, periodStartDate)) / 30)
						+ (prevSchedule.getBalanceAmount() * irrRate) 
								- (prevSchedule.getBalanceAmount() * irrRate * DateUtils.getDiffInDaysPlusOneDay(prevSchedule.getPeriodEndDate(), prevSchedule.getPeriodStartDate())) / 30;
			}
						
			schedule.setN(i);
			schedule.setBalanceAmount(balanceAmount);
			schedule.setRevenueAmount(revenueAmount);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			
			initialPrincipal = balanceAmount;
			
			schedules.add(schedule);
		}
		
		revenueSchedules.setSchedules(schedules);
		return revenueSchedules;
	}
	
	/**
	 * @param calculationParameter
	 * @return
	 */
	private double calculateInstallmentPayment(CalculationParameter calculationParameter) {		
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods() - calculationParameter.getNumberOfPrincipalGracePeriods();		
		double totalInterest = initialPrincipal * periodicInterestRate * numberOfPeriods;		
		double installmentPayment = (initialPrincipal + totalInterest) / numberOfPeriods;		
		return installmentPayment;
	}
}
