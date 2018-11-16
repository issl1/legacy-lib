package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.accounting.AccountingUtils;
import com.nokor.efinance.core.shared.accounting.RevenueIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.RevenueIncomeSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInterestIncomeCalculatorImpl2 {
		
	Logger logger = LoggerFactory.getLogger(GLFInterestIncomeCalculatorImpl2.class);
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public RevenueIncomeSchedules getSchedulesCase1(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		RevenueIncomeSchedules revenueIncomeSchedules = new RevenueIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<RevenueIncomeSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
								
		List<Double> revenueSchedules = AccountingUtils.getInterestSchedules(startDate, calculationParameter, calculationParameter2);
				
		for (int i = 0; i <= numberOfPeriods; i++) {
			RevenueIncomeSchedule schedule = new RevenueIncomeSchedule();
			
			double interestAmount = 0d;
			
			if (i == 0) {
				installmentDate = startDate;
			} else if (i == 1) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			if (i == 0) {
				interestAmount = revenueSchedules.get(i + 1);
				dailyInterest1 = interestAmount / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate);
			} else {
				RevenueIncomeSchedule prevSchedule = schedules.get(i - 1);
				if (i < numberOfPeriods) {
					interestAmount = revenueSchedules.get(i + 1);
					dailyInterest1 = interestAmount / DateUtils.getDiffInDays(DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths()), installmentDate);
				}
				
				int plusOneDay = (i == 1 ? 1 : 0);
				dailyInterest2 = (revenueSchedules.get(i) 
									- (revenueSchedules.get(i) / (DateUtils.getDiffInDays(installmentDate, prevSchedule.getInstallmentDate()) + plusOneDay) 
										* (DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(prevSchedule.getInstallmentDate()), prevSchedule.getInstallmentDate()) + plusOneDay))
							) / (DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate)));
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue((dailyInterest1 * (DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate) + (i == 0 ? 1 : 0))) 
						+ (dailyInterest2 * DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate))));
			schedules.add(schedule);
		}
				
		revenueIncomeSchedules.setSchedules(schedules);
		revenueIncomeSchedules.setRunningCase(1);
		
		return revenueIncomeSchedules;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public RevenueIncomeSchedules getSchedulesCase2(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		RevenueIncomeSchedules revenueIncomeSchedules = new RevenueIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<RevenueIncomeSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
				
		Date installmentDate = null;
								
		List<Double> revenueSchedules = AccountingUtils.getInterestSchedules(startDate, calculationParameter, calculationParameter2);
				
		for (int i = 0; i < numberOfPeriods; i++) {
			RevenueIncomeSchedule schedule = new RevenueIncomeSchedule();
						
			if (i == 0) {
				installmentDate = firstInstallmentDate;
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			long nbFromBeginOfMonth = 0;
			
			if (i == 0) {
				nbFromBeginOfMonth = DateUtils.getDiffInDaysPlusOneDay(installmentDate, startDate);
				dailyInterest1 = revenueSchedules.get(i + 2) / DateUtils.getDiffInDays(DateUtils.addMonthsDate(firstInstallmentDate, calculationParameter.getFrequency().getNbMonths()), firstInstallmentDate);
				dailyInterest2 = revenueSchedules.get(i + 1) / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate);
			} else {
				RevenueIncomeSchedule prevSchedule = schedules.get(i - 1);
				if (i < numberOfPeriods - 1) {
					dailyInterest1 = revenueSchedules.get(i + 2) / DateUtils.getDiffInDays(DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths()), installmentDate);
				}
				
				nbFromBeginOfMonth = DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate)); 
				dailyInterest2 = (revenueSchedules.get(i + 1) 
									- (revenueSchedules.get(i + 1) / DateUtils.getDiffInDays(installmentDate, prevSchedule.getInstallmentDate()) 
										* DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(prevSchedule.getInstallmentDate()), prevSchedule.getInstallmentDate()))
							) / nbFromBeginOfMonth;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue((dailyInterest1 * DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate)) + (dailyInterest2 * nbFromBeginOfMonth));
			schedules.add(schedule);
		}
				
		revenueIncomeSchedules.setSchedules(schedules);
		revenueIncomeSchedules.setRunningCase(2);
		
		return revenueIncomeSchedules;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public RevenueIncomeSchedules getSchedulesCase3(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
			
		RevenueIncomeSchedules revenueIncomeSchedules = new RevenueIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);

		List<RevenueIncomeSchedule> schedules = new ArrayList<>();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();

		Date installmentDate = null;
								
		List<Double> revenueSchedules = AccountingUtils.getInterestSchedules(startDate, calculationParameter, calculationParameter2);
		
		double part10 = 0d;
		double part11 = 0d;
		double part21 = 0d;
		
		for (int i = 0; i <= (numberOfPeriods + 1); i++) {
			RevenueIncomeSchedule schedule = new RevenueIncomeSchedule();
			
			if (i == 0) {
				installmentDate = startDate;
			} else if (i == 1) {
				installmentDate =  DateUtils.addMonthsDate(firstInstallmentDate, -1);
			} else {			
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			double dailyInterest1 = 0d;
			double dailyInterest2 = 0d;
			double interest1 = 0d;
			double interest2 = 0d;
			
			if (i == 0) {
				long x = DateUtils.getDiffInDaysPlusOneDay(DateUtils.getDateAtEndOfMonth(startDate), startDate);
				part10 = (revenueSchedules.get(i + 1) / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate) * x);
				interest1 = part10;
				dailyInterest1 = revenueSchedules.get(i + 1) / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate);
			} else if (i == 1) {
				long x1 = DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
				part11 = (revenueSchedules.get(i) / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate) * x1);
				interest1 = part11;
				dailyInterest1 = revenueSchedules.get(i) / DateUtils.getDiffInDaysPlusOneDay(firstInstallmentDate, startDate);
				long x2 =  DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
				part21 = dailyInterest1 * x2;
				interest2 = part21;
				dailyInterest2 = part21 / x2;
			} else {
				if (i <= numberOfPeriods) {
					dailyInterest1 = (revenueSchedules.get(i) / DateUtils.getDiffInDays(DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths()), installmentDate));
					interest1 = dailyInterest1 * DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(installmentDate), installmentDate);
				}
				long x2 =  DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
				if (i == 2) {
					double part22 = revenueSchedules.get(i - 1) - part21 - part11 - part10;
					dailyInterest2 = part22 / x2;
				} else {
					RevenueIncomeSchedule prevSchedule = schedules.get(i - 1);
					dailyInterest2 = (revenueSchedules.get(i - 1) 
							- (revenueSchedules.get(i - 1) / DateUtils.getDiffInDays(installmentDate, prevSchedule.getInstallmentDate()) 
									* DateUtils.getDiffInDays(DateUtils.getDateAtEndOfMonth(prevSchedule.getInstallmentDate()), prevSchedule.getInstallmentDate()))) / x2;
				}
				interest2 = dailyInterest2 * DateUtils.getDiffInDaysPlusOneDay(installmentDate, DateUtils.getDateAtBeginningOfMonth(installmentDate));
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setDailyInterest1(dailyInterest1);
			schedule.setDailyInterest2(dailyInterest2);
			schedule.setMonthlyRevenue(interest1 + interest2);
			schedules.add(schedule);
		}
				
		revenueIncomeSchedules.setSchedules(schedules);
		revenueIncomeSchedules.setRunningCase(3);
		
		return revenueIncomeSchedules;
	}
}
