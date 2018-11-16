package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.accounting.AccountingUtils;
import com.nokor.efinance.core.shared.accounting.ServiceSchedule;
import com.nokor.efinance.core.shared.accounting.ServiceSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFServiceCalculatorImpl2 {
		
	Logger logger = LoggerFactory.getLogger(GLFServiceCalculatorImpl2.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param calculationParameter2
	 * @return
	 */
	public ServiceSchedules getSchedulesCase1(Date startDate, Date firstInstallmentDate, double serviceAmountUsd, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		ServiceSchedules serviceSchedules = new ServiceSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<ServiceSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
								
		List<Double> distributionRateSchedules = AccountingUtils.getDistributionsRate(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
				
		for (int i = 0; i <= numberOfPeriods; i++) {
			ServiceSchedule schedule = new ServiceSchedule();
			
			double interestAmount = serviceAmountUsd * distributionRateSchedules.get(i);
			
			if (i == 0) {
				installmentDate = startDate;
			} else if (i == 1) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			double interest1 = 0d;
			double interest2 = 0d;
			
			if (i == 0) {
				interest2 = interestAmount;
				dailyInterest2 = interest2 / DateUtils.getDiffInDaysPlusOneDay(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			} else {
				long nbDaysFromBeginMonth = DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
				if (i < numberOfPeriods) {
					dailyInterest1 = interestAmount / DateUtils.getDiffInDaysPlusOneDay(installmentDate, schedules.get(i - 1).getInstallmentDate());
					interest1 = nbDaysFromBeginMonth * dailyInterest1;
				}
				interest2 = (interestAmount - nbDaysFromBeginMonth * dailyInterest1);
				dailyInterest2 =  interest2 / DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue(interest1 + interest2);
			schedules.add(schedule);
		}
				
		serviceSchedules.setSchedules(schedules);
		serviceSchedules.setRunningCase(1);
		
		return serviceSchedules;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param calculationParameter2
	 * @return
	 */
	public ServiceSchedules getSchedulesCase2(Date startDate, Date firstInstallmentDate, double serviceAmountUsd, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		ServiceSchedules directCostSchedules = new ServiceSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<ServiceSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
								
		List<Double> distributionRateSchedules = AccountingUtils.getDistributionsRate(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
				
		for (int i = 0; i < numberOfPeriods; i++) {
			ServiceSchedule schedule = new ServiceSchedule();
			
			double interestAmount = serviceAmountUsd * distributionRateSchedules.get(i);
			
			if (i == 0) {
				installmentDate = startDate;
			} else if (i == 1) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			double interest1 = 0d;
			double interest2 = 0d;
			
			if (i == 0) {
				interest2 = interestAmount;
				dailyInterest2 = interest2 / DateUtils.getDiffInDaysPlusOneDay(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			} else {
				long nbDaysFromBeginMonth = DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
				if (i < numberOfPeriods) {
					dailyInterest1 = interestAmount / DateUtils.getDiffInDaysPlusOneDay(installmentDate, schedules.get(i - 1).getInstallmentDate());
					interest1 = nbDaysFromBeginMonth * dailyInterest1;
				}
				interest2 = (interestAmount - nbDaysFromBeginMonth * dailyInterest1);
				dailyInterest2 =  interest2 / DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue(interest1 + interest2);
			schedules.add(schedule);
		}
				
		directCostSchedules.setSchedules(schedules);
		directCostSchedules.setRunningCase(1);
		
		return directCostSchedules;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public ServiceSchedules getSchedulesCase3(Date startDate, Date firstInstallmentDate, double serviceAmountUsd, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		ServiceSchedules directCostSchedules = new ServiceSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<ServiceSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
								
		List<Double> distributionRateSchedules = AccountingUtils.getDistributionsRate(startDate, firstInstallmentDate, calculationParameter, calculationParameter2);
				
		for (int i = 0; i < numberOfPeriods; i++) {
			ServiceSchedule schedule = new ServiceSchedule();
			
			double interestAmount = serviceAmountUsd * distributionRateSchedules.get(i);
			
			if (i == 0) {
				installmentDate = startDate;
			} else if (i == 1) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			double interest1 = 0d;
			double interest2 = 0d;
			
			if (i == 0) {
				interest2 = interestAmount;
				dailyInterest2 = interest2 / DateUtils.getDiffInDaysPlusOneDay(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			} else {
				long nbDaysFromBeginMonth = DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
				if (i < numberOfPeriods) {
					dailyInterest1 = interestAmount / DateUtils.getDiffInDaysPlusOneDay(installmentDate, schedules.get(i - 1).getInstallmentDate());
					interest1 = nbDaysFromBeginMonth * dailyInterest1;
				}
				interest2 = (interestAmount - nbDaysFromBeginMonth * dailyInterest1);
				dailyInterest2 =  interest2 / DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue(interest1 + interest2);
			schedules.add(schedule);
		}
				
		directCostSchedules.setSchedules(schedules);
		directCostSchedules.setRunningCase(1);
		
		return directCostSchedules;
	}
}
