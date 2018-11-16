package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeSchedules;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFServicingIncomeCalculatorImpl {
		
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param servicingCashflows
	 * @return
	 */
	public ServicingIncomeSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, Map<Integer, Cashflow> servicingCashflows) {
		
		ServicingIncomeSchedules servicingIncomeSchedules = new ServicingIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<ServicingIncomeSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		double totalServicingIncome = calculationParameter.getServicingFee();
		double eirRate = Rate.calculateRate(numberOfPeriods, -1 * (initialPrincipal + totalServicingIncome) / numberOfPeriods, initialPrincipal);
		Date contractEndDate = DateUtils.addMonthsDate(startDate, numberOfPeriods);
		
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		int i = 0;
		boolean isFirstOverdueMorethen30Days = true;
		boolean isNextInstallmentOverdueMorethen30Days = false;
		
		while (periodStartDate.compareTo(contractEndDate) < 0) {
			ServicingIncomeSchedule schedule = new ServicingIncomeSchedule();
			double cumulativeBalance = 0d;
			double servicingIncomeDistribution2 = 0d;
			double servicingIncomeDistribution3 = 0d;
			double unearnedServicingIncome = 0d;
			double accountReceivable = 0d;
			double servicingIncomeReceived = 0d;
			
			if (i == 0) {
				installmentDate = startDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			
			Cashflow cashflow = servicingCashflows.get(i);
			if (cashflow != null && cashflow.isPaid() && cashflow.getTiInstallmentAmount() != null)
			{
				if(cashflow.getService() != null && ServiceEntityField.SERFEE.equals(cashflow.getService().getCode())) {
					servicingIncomeReceived = cashflow.getTiInstallmentAmount();
				}
			}
			
			if (i == 0) {
				servicingIncomeDistribution3 = initialPrincipal * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
				accountReceivable = servicingIncomeDistribution3 - servicingIncomeDistribution2;
				unearnedServicingIncome = totalServicingIncome - servicingIncomeDistribution3;
			} else if (i == 1) {
				ServicingIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = initialPrincipal * eirRate;
				servicingIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalServicingIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				servicingIncomeDistribution2 = cumulativeBalance - prevSchedule.getServicingIncomeDistribution3() - prevSchedule.getCumulativeBalance();
				accountReceivable = prevSchedule.getAccountReceivable() + (servicingIncomeDistribution3 + servicingIncomeDistribution2) - totalServicingIncome / numberOfPeriods;
				unearnedServicingIncome = prevSchedule.getUnearnedServicingIncome() - servicingIncomeDistribution3 - servicingIncomeDistribution2;
			} else {
				ServicingIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((initialPrincipal + totalServicingIncome) / numberOfPeriods) * (i - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
				servicingIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalServicingIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				servicingIncomeDistribution2 = cumulativeBalance - prevSchedule.getServicingIncomeDistribution3() - prevSchedule.getCumulativeBalance();				
				accountReceivable = prevSchedule.getAccountReceivable() + (servicingIncomeDistribution3 + servicingIncomeDistribution2) - totalServicingIncome / numberOfPeriods;
				unearnedServicingIncome = prevSchedule.getUnearnedServicingIncome() - servicingIncomeDistribution3 - servicingIncomeDistribution2;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setServicingIncomeDistribution2(servicingIncomeDistribution2);
			schedule.setServicingIncomeDistribution3(servicingIncomeDistribution3);
			
			double realServicingIncomeDistributed = servicingIncomeDistribution2 + servicingIncomeDistribution3;
			int numOverdueDays = getContractOverdueNumDays(cashflow, null);
			boolean isCashflowOverdueMorethen30Days = numOverdueDays > 30;
			if (isFirstOverdueMorethen30Days && isCashflowOverdueMorethen30Days)
			{
				realServicingIncomeDistributed = realServicingIncomeDistributed * -1;
				isFirstOverdueMorethen30Days = false;
				isNextInstallmentOverdueMorethen30Days = true;
			}
			else if (isNextInstallmentOverdueMorethen30Days)
			{
				realServicingIncomeDistributed = 0;
			}
			
			boolean isOverdue = numOverdueDays > 0;
			if (isOverdue)
			{
				schedule.setServicingIncomeInSuspend(servicingIncomeDistribution2 + servicingIncomeDistribution3);
				double servicingIncomeInSuspendCumulated = schedule.getServicingIncomeInSuspend() + servicingIncomeDistribution2 + servicingIncomeDistribution3;
				schedule.setServicingIncomeInSuspendCumulated(servicingIncomeInSuspendCumulated);
			}
			
			schedule.setRealServicingIncomeDistributed(realServicingIncomeDistributed);
			schedule.setUnearnedServicingIncome(unearnedServicingIncome);
			schedule.setCumulativeBalance(cumulativeBalance);
			schedule.setAccountReceivable(accountReceivable);
			schedule.setServicingIncomeReceived(servicingIncomeReceived);
			schedules.add(schedule);
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			i++;
		}
		
		servicingIncomeSchedules.setEirRate(eirRate);
		servicingIncomeSchedules.setTotalServicingIncome(totalServicingIncome);
		servicingIncomeSchedules.setSchedules(schedules);
		return servicingIncomeSchedules;
	}
	
	/**
	 * 
	 * @param cashflow
	 * @param paymentDate
	 * @return
	 */
	private int getContractOverdueNumDays(Cashflow cashflow, Date paymentDate)
	{
		if (cashflow == null || cashflow.isPaid())
		{
			// no overdue if the current cashflow is paid
			return 0;
		}
		Date installmentDate = cashflow.getInstallmentDate();
		if (paymentDate == null)
		{
			paymentDate = DateUtils.todayH00M00S00();
		}
		int numOverdueDays = DateUtils.getDiffInDays(paymentDate, installmentDate).intValue();
		return numOverdueDays;
	}
	
}
