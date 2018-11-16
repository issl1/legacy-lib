package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.accounting.ReferalFeeSchedule;
import com.nokor.efinance.core.shared.accounting.ReferalFeeSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFReferalFeeCalculatorImpl {
		
	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public ReferalFeeSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		
		ReferalFeeSchedules referalFeeSchedules = new ReferalFeeSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<ReferalFeeSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		double dealerCommission = ((initialPrincipal * 100) / 1386) + ((20d * 14) / 86);
		double eirRate = Rate.calculateRate(numberOfPeriods, -1 * (initialPrincipal + dealerCommission) / numberOfPeriods, initialPrincipal);
		Date contractEndDate = DateUtils.addMonthsDate(startDate, numberOfPeriods);
		
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		int i = 0;
		while (periodStartDate.compareTo(contractEndDate) < 0) {
			ReferalFeeSchedule schedule = new ReferalFeeSchedule();
			double cumulativeBalance = 0d;
			double referalFeeDistribution2 = 0d;
			double referalFeeDistribution3 = 0d;
			double deferredCommissionReferalFee = 0d;
			double acrrualExpenses = 0d;
			double paymentToDealer = 0d;
			
			if (i == 0) {
				installmentDate = startDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			
			if (i == 0) {
				referalFeeDistribution3 = initialPrincipal * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
				acrrualExpenses = dealerCommission;
				deferredCommissionReferalFee = dealerCommission - referalFeeDistribution3;
			} else if (i == 1) {
				ReferalFeeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = initialPrincipal * eirRate;
				referalFeeDistribution3 = (initialPrincipal - ((((initialPrincipal + dealerCommission) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				referalFeeDistribution2 = cumulativeBalance - prevSchedule.getReferalFeeDistribution3() - prevSchedule.getCumulativeBalance();
				if (i <= 12) {
					paymentToDealer = dealerCommission / 12;
				}
				acrrualExpenses = prevSchedule.getAcrrualExpenses() - paymentToDealer;
				deferredCommissionReferalFee = prevSchedule.getDeferredCommissionReferalFee() - referalFeeDistribution3 - referalFeeDistribution2;
			} else {
				ReferalFeeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((initialPrincipal + dealerCommission) / numberOfPeriods) * (i - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
				referalFeeDistribution3 = (initialPrincipal - ((((initialPrincipal + dealerCommission) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				referalFeeDistribution2 = cumulativeBalance - prevSchedule.getReferalFeeDistribution3() - prevSchedule.getCumulativeBalance();
				if (i <= 12) {
					paymentToDealer = dealerCommission / 12;
				}
				acrrualExpenses = prevSchedule.getAcrrualExpenses() - paymentToDealer;
				deferredCommissionReferalFee = dealerCommission - referalFeeDistribution3;
				deferredCommissionReferalFee = prevSchedule.getDeferredCommissionReferalFee() - referalFeeDistribution3 - referalFeeDistribution2;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setReferalFeeDistribution2(referalFeeDistribution2);
			schedule.setReferalFeeDistribution3(referalFeeDistribution3);
			schedule.setDeferredCommissionReferalFee(deferredCommissionReferalFee);
			schedule.setCumulativeBalance(cumulativeBalance);
			schedule.setAcrrualExpenses(acrrualExpenses);
			schedule.setPaymentToDealer(paymentToDealer);
			schedules.add(schedule);
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			i++;
		}
		
		referalFeeSchedules.setEirRate(eirRate);
		referalFeeSchedules.setDealerCommission(dealerCommission);
		referalFeeSchedules.setSchedules(schedules);
		return referalFeeSchedules;
	}
}
