package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.accounting.InsuranceExpenseSchedule;
import com.nokor.efinance.core.shared.accounting.InsuranceExpenseSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInsuranceExpenseCalculatorImpl {
		
	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public InsuranceExpenseSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		
		InsuranceExpenseSchedules insuranceExpenseSchedules = new InsuranceExpenseSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<InsuranceExpenseSchedule> schedules = new ArrayList<>();
		double assetPrice = calculationParameter.getAssetPrice();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		int nbInstallments = numberOfPeriods / 12;
		
		int i = 0;
		for (int iter = 1; iter <= nbInstallments; iter++) {
		
			double totalInsuranceExpense = 0d;
			if (iter == 1) {
				totalInsuranceExpense = assetPrice * 0.90 * 0.0115;
			} else if (iter == 2) {
				totalInsuranceExpense = assetPrice * 0.72 * 0.0115;
			} else if (iter == 3) {
				totalInsuranceExpense = assetPrice * 0.50 * 0.0115;
			}
			double eirRate = Rate.calculateRate(12, -1 * (initialPrincipal + totalInsuranceExpense) / 12, initialPrincipal);
			
			startDate = DateUtils.addMonthsDate(startDate, 12 * (iter -1));
			Date installmentDate = startDate;
			Date periodStartDate = startDate;
			Date periodEndDate = null;
			Date contractEndDate = DateUtils.addMonthsDate(startDate, 12);
			
			int j = 0;
			while (periodStartDate.compareTo(contractEndDate) < 0) {
				InsuranceExpenseSchedule schedule = new InsuranceExpenseSchedule();
				double cumulativeBalance = 0d;
				double insuranceExpenseDistribution2 = 0d;
				double insuranceExpenseDistribution3 = 0d;
				double balanceInsuranceExpense = 0d;
				double insuranceExpensePaid = 0d;
				
				if (j > 0) {
					installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
				}
				
				periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
				
				if (j == 0) {
					insuranceExpenseDistribution3 = initialPrincipal * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
					balanceInsuranceExpense = totalInsuranceExpense - insuranceExpenseDistribution3;
					insuranceExpensePaid = totalInsuranceExpense;
				} else if (j == 1) {
					InsuranceExpenseSchedule prevSchedule = schedules.get(i - 1);
					cumulativeBalance = initialPrincipal * eirRate;
					insuranceExpenseDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceExpense) / 12) * j) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
					insuranceExpenseDistribution2 = cumulativeBalance - prevSchedule.getInsuranceExpenseDistribution3() - prevSchedule.getCumulativeBalance();
					balanceInsuranceExpense = prevSchedule.getBalanceInsuranceExpense() - insuranceExpenseDistribution3 - insuranceExpenseDistribution2;
				} else {
					InsuranceExpenseSchedule prevSchedule = schedules.get(i - 1);
					cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((initialPrincipal + totalInsuranceExpense) / 12) * (j - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
					insuranceExpenseDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceExpense) / 12) * j) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
					insuranceExpenseDistribution2 = cumulativeBalance - prevSchedule.getInsuranceExpenseDistribution3() - prevSchedule.getCumulativeBalance();
					balanceInsuranceExpense = prevSchedule.getBalanceInsuranceExpense() - insuranceExpenseDistribution3 - insuranceExpenseDistribution2;
				}
				
				schedule.setN(i);
				schedule.setInstallmentDate(installmentDate);
				schedule.setPeriodStartDate(periodStartDate);
				schedule.setPeriodEndDate(periodEndDate);
				schedule.setInsuranceExpenseDistribution2(insuranceExpenseDistribution2);
				schedule.setInsuranceExpenseDistribution3(insuranceExpenseDistribution3);
				schedule.setBalanceInsuranceExpense(balanceInsuranceExpense);
				schedule.setCumulativeBalance(cumulativeBalance);
				schedule.setInsuranceExpensePaid(insuranceExpensePaid);
				schedules.add(schedule);
				periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
				i++;
				j++;
			}
		}		
		// insuranceExpenseSchedules.setEirRate(eirRate);
		// insuranceExpenseSchedules.setTotalInsuranceExpense(totalInsuranceExpense);
		insuranceExpenseSchedules.setSchedules(schedules);
		return insuranceExpenseSchedules;
	}
}
