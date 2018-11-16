package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.accounting.RegistrationExpenseSchedule;
import com.nokor.efinance.core.shared.accounting.RegistrationExpenseSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFRegistrationExpenseCalculatorImpl {
		
	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public RegistrationExpenseSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, double registrationPlateNumberFee) {
		
		RegistrationExpenseSchedules registrationExpenseSchedules = new RegistrationExpenseSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<RegistrationExpenseSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();		
				
		double totalRegistrationExpense = calculationParameter.getRegistrationFee();
		double eirRate = Rate.calculateRate(numberOfPeriods, -1 * (initialPrincipal + totalRegistrationExpense) / numberOfPeriods, initialPrincipal);
		
		Date installmentDate = startDate;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
		Date contractEndDate = DateUtils.addMonthsDate(startDate, numberOfPeriods);
		
		int i = 0;
		while (periodStartDate.compareTo(contractEndDate) < 0) {
			RegistrationExpenseSchedule schedule = new RegistrationExpenseSchedule();
			double cumulativeBalance = 0d;
			double registrationExpenseDistribution2 = 0d;
			double registrationExpenseDistribution3 = 0d;
			double balanceRegistrationExpense = 0d;
			
			if (i > 0) {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
				registrationPlateNumberFee = 0;
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			
			if (i == 0) {
				registrationExpenseDistribution3 = initialPrincipal * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
				balanceRegistrationExpense = totalRegistrationExpense - registrationExpenseDistribution3;
			} else if (i == 1) {
				RegistrationExpenseSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = initialPrincipal * eirRate;
				registrationExpenseDistribution3 = (initialPrincipal - ((((initialPrincipal + totalRegistrationExpense) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				registrationExpenseDistribution2 = cumulativeBalance - prevSchedule.getRegistrationExpenseDistribution3() - prevSchedule.getCumulativeBalance();
				balanceRegistrationExpense = prevSchedule.getBalanceRegistrationExpense() - registrationExpenseDistribution3 - registrationExpenseDistribution2;
			} else {
				RegistrationExpenseSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((initialPrincipal + totalRegistrationExpense) / numberOfPeriods) * (i - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
				registrationExpenseDistribution3 = (initialPrincipal - ((((initialPrincipal + totalRegistrationExpense) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				registrationExpenseDistribution2 = cumulativeBalance - prevSchedule.getRegistrationExpenseDistribution3() - prevSchedule.getCumulativeBalance();
				balanceRegistrationExpense = prevSchedule.getBalanceRegistrationExpense() - registrationExpenseDistribution3 - registrationExpenseDistribution2;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setRegistrationExpenseDistribution2(registrationExpenseDistribution2);
			schedule.setRegistrationExpenseDistribution3(registrationExpenseDistribution3);
			schedule.setBalanceRegistrationExpense(balanceRegistrationExpense);
			schedule.setCumulativeBalance(cumulativeBalance);
			schedule.setRegistrationPlateNumberFee(registrationPlateNumberFee);
			schedules.add(schedule);
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			i++;
		}
		registrationExpenseSchedules.setEirRate(eirRate);
		registrationExpenseSchedules.setTotalRegistrationExpense(totalRegistrationExpense);
		registrationExpenseSchedules.setSchedules(schedules);
		return registrationExpenseSchedules;
	}
}
