package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeSchedules;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

/**
 * GLF calculation
 * @author ly.youhort
 */
public class GLFInsuranceIncomeCalculatorImpl {
		
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public InsuranceIncomeSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter)
	{
		return getSchedulesUnSplitWithInstallment(startDate, firstInstallmentDate, calculationParameter);
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceChasflow
	 * @return
	 */
	public InsuranceIncomeSchedules getSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, Map<Integer, Cashflow> insuranceChasflow) {
		return getSchedulesSplitWithInstallment(startDate, firstInstallmentDate, calculationParameter, insuranceChasflow);
	}
	
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param insuranceCashflows
	 * @return
	 */
	public InsuranceIncomeSchedules getSchedulesSplitWithInstallment(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter, Map<Integer, Cashflow> insuranceCashflows) {
		
		InsuranceIncomeSchedules insuranceIncomeSchedules = new InsuranceIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<InsuranceIncomeSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double insuranceAmount = calculationParameter.getInsuranceFee();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		double totalInsuranceIncome = (insuranceAmount * numberOfPeriods) / 12;
		double eirRate = Rate.calculateRate(numberOfPeriods, -1 * (initialPrincipal + totalInsuranceIncome) / numberOfPeriods, initialPrincipal);
		Date contractEndDate = DateUtils.addMonthsDate(startDate, numberOfPeriods);
		
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		int i = 0;
		boolean isFirstOverdueMorethen30Days = true;
		boolean isNextInstallmentOverdueMorethen30Days = false;
		
		while (periodStartDate.compareTo(contractEndDate) < 0) {
			InsuranceIncomeSchedule schedule = new InsuranceIncomeSchedule();
			double cumulativeBalance = 0d;
			double insuranceIncomeDistribution2 = 0d;
			double insuranceIncomeDistribution3 = 0d;
			double unearnedInsuranceIncome = 0d;
			double accountReceivable = 0d;
			double insuranceIncomeReceived = 0d;
			
			
			if (i == 0) {
				installmentDate = startDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			Cashflow cashflow = insuranceCashflows.get(i);
			if (cashflow != null && cashflow.isPaid() && cashflow.getTiInstallmentAmount() != null)
			{
				if(cashflow.getService() != null && ServiceEntityField.INSFEE.equals(cashflow.getService().getCode())) {
					insuranceIncomeReceived = cashflow.getTiInstallmentAmount();
				}
			}
			if (i == 0) {
				insuranceIncomeDistribution3 = insuranceAmount * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
				accountReceivable = insuranceIncomeDistribution3 - insuranceIncomeDistribution2;
				unearnedInsuranceIncome = totalInsuranceIncome - insuranceIncomeDistribution3;
			} else if (i == 1) {
				InsuranceIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = initialPrincipal * eirRate;
				insuranceIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				insuranceIncomeDistribution2 = cumulativeBalance - prevSchedule.getInsuranceIncomeDistribution3() - prevSchedule.getCumulativeBalance();
				accountReceivable = prevSchedule.getAccountReceivable() + (insuranceIncomeDistribution3 + insuranceIncomeDistribution2) - insuranceIncomeReceived;
				unearnedInsuranceIncome = prevSchedule.getUnearnedInsuranceIncome() - insuranceIncomeDistribution3 - insuranceIncomeDistribution2;
			} else {
				InsuranceIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((insuranceAmount + totalInsuranceIncome) / numberOfPeriods) * (i - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
				insuranceIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				insuranceIncomeDistribution2 = cumulativeBalance - prevSchedule.getInsuranceIncomeDistribution3() - prevSchedule.getCumulativeBalance();				
				accountReceivable = prevSchedule.getAccountReceivable() + (insuranceIncomeDistribution3 + insuranceIncomeDistribution2) -insuranceIncomeReceived;
				unearnedInsuranceIncome = prevSchedule.getUnearnedInsuranceIncome() - insuranceIncomeDistribution3 - insuranceIncomeDistribution2;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			
			schedule.setInsuranceIncomeDistribution2(insuranceIncomeDistribution2);
			schedule.setInsuranceIncomeDistribution3(insuranceIncomeDistribution3);
			
			double realInsuranceIncomeDistributed = insuranceIncomeDistribution2 + insuranceIncomeDistribution3;
			int numOverdueDays = getContractOverdueNumDays(cashflow, null);
			boolean isCashflowOverdueMorethen30Days = numOverdueDays > 30;
			if (isFirstOverdueMorethen30Days && isCashflowOverdueMorethen30Days)
			{
				realInsuranceIncomeDistributed = realInsuranceIncomeDistributed * -1;
				isFirstOverdueMorethen30Days = false;
				isNextInstallmentOverdueMorethen30Days = true;
			}
			else if (isNextInstallmentOverdueMorethen30Days)
			{
				realInsuranceIncomeDistributed = 0;
			}
			
			boolean isOverdue = numOverdueDays > 0;
			if (isOverdue)
			{
				schedule.setInsuranceIncomeInSuspend(insuranceIncomeDistribution2 + insuranceIncomeDistribution3);
				double insuranceIncomeInSuspendCumulated = schedule.getInsuranceIncomeInSuspendCumulated() + insuranceIncomeDistribution2 + insuranceIncomeDistribution3;
				schedule.setInsuranceIncomeInSuspendCumulated(insuranceIncomeInSuspendCumulated);
			}
			
			schedule.setRealInsuranceIncomeDistributed(realInsuranceIncomeDistributed);
			schedule.setUnearnedInsuranceIncome(unearnedInsuranceIncome);
			schedule.setCumulativeBalance(cumulativeBalance);
			schedule.setAccountReceivable(accountReceivable);
			schedule.setInsuranceIncomeReceived(insuranceIncomeReceived);
			schedules.add(schedule);
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			i++;
		}
		
		insuranceIncomeSchedules.setEirRate(eirRate);
		insuranceIncomeSchedules.setTotalInsuranceIncome(totalInsuranceIncome);
		insuranceIncomeSchedules.setSchedules(schedules);
		insuranceIncomeSchedules.setSplitWithInstallment(true);
		return insuranceIncomeSchedules;
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
	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public InsuranceIncomeSchedules getSchedulesUnSplitWithInstallment(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		
		InsuranceIncomeSchedules insuranceIncomeSchedules = new InsuranceIncomeSchedules(startDate, firstInstallmentDate, calculationParameter);
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		firstInstallmentDate = DateUtils.getDateAtBeginningOfDay(firstInstallmentDate);
		
		List<InsuranceIncomeSchedule> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double insuranceAmount = calculationParameter.getInsuranceFee();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		double totalInsuranceIncome = (insuranceAmount * numberOfPeriods) / 12;
		double eirRate = Rate.calculateRate(numberOfPeriods, -1 * (initialPrincipal + totalInsuranceIncome) / numberOfPeriods, initialPrincipal);
		Date contractEndDate = DateUtils.addMonthsDate(startDate, numberOfPeriods);
		
		Date installmentDate = null;
		Date periodStartDate = startDate;
		Date periodEndDate = null;
				
		int i = 0;
		while (periodStartDate.compareTo(contractEndDate) < 0) {
			InsuranceIncomeSchedule schedule = new InsuranceIncomeSchedule();
			double cumulativeBalance = 0d;
			double insuranceIncomeDistribution2 = 0d;
			double insuranceIncomeDistribution3 = 0d;
			double unearnedInsuranceIncome = 0d;
			double accountReceivable = 0d;
			
			if (i == 0) {
				installmentDate = startDate;
				periodStartDate = startDate;
			} else {
				installmentDate = DateUtils.addMonthsDate(installmentDate, calculationParameter.getFrequency().getNbMonths());
			}
			
			periodEndDate = DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtEndOfMonth(periodStartDate));
			
			if (i == 0) {
				insuranceIncomeDistribution3 = insuranceAmount * eirRate * DateUtils.getDiffInDays(periodEndDate, periodStartDate) / 30;
				accountReceivable = insuranceIncomeDistribution3 + insuranceIncomeDistribution2;
				unearnedInsuranceIncome = totalInsuranceIncome - insuranceIncomeDistribution3;
			} else if (i == 1) {
				InsuranceIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = initialPrincipal * eirRate;
				insuranceIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				insuranceIncomeDistribution2 = cumulativeBalance - prevSchedule.getInsuranceIncomeDistribution3() - prevSchedule.getCumulativeBalance();
				accountReceivable = prevSchedule.getAccountReceivable() + (insuranceIncomeDistribution3 + insuranceIncomeDistribution2) - totalInsuranceIncome / numberOfPeriods;
				unearnedInsuranceIncome = prevSchedule.getUnearnedInsuranceIncome() - insuranceIncomeDistribution3 - insuranceIncomeDistribution2;
			} else {
				InsuranceIncomeSchedule prevSchedule = schedules.get(i - 1);
				cumulativeBalance = prevSchedule.getCumulativeBalance() + (initialPrincipal - ((((insuranceAmount + totalInsuranceIncome) / numberOfPeriods) * (i - 1)) - prevSchedule.getCumulativeBalance())) * eirRate;
				insuranceIncomeDistribution3 = (initialPrincipal - ((((initialPrincipal + totalInsuranceIncome) / numberOfPeriods) * i) - cumulativeBalance)) * eirRate *  DateUtils.getDiffInDays(periodEndDate, installmentDate) / 30;
				insuranceIncomeDistribution2 = cumulativeBalance - prevSchedule.getInsuranceIncomeDistribution3() - prevSchedule.getCumulativeBalance();				
				accountReceivable = prevSchedule.getAccountReceivable() + (insuranceIncomeDistribution3 + insuranceIncomeDistribution2) - totalInsuranceIncome / numberOfPeriods;
				unearnedInsuranceIncome = prevSchedule.getUnearnedInsuranceIncome() - insuranceIncomeDistribution3 - insuranceIncomeDistribution2;
			}
			
			schedule.setN(i);
			schedule.setInstallmentDate(installmentDate);
			schedule.setPeriodStartDate(periodStartDate);
			schedule.setPeriodEndDate(periodEndDate);
			schedule.setInsuranceIncomeDistribution2(insuranceIncomeDistribution2);
			schedule.setInsuranceIncomeDistribution3(insuranceIncomeDistribution3);
			schedule.setUnearnedInsuranceIncome(unearnedInsuranceIncome);
			schedule.setCumulativeBalance(cumulativeBalance);
			schedule.setAccountReceivable(accountReceivable);
			schedules.add(schedule);
			periodStartDate = DateUtils.addDaysDate(periodEndDate, 1);
			i++;
		}
		
		insuranceIncomeSchedules.setEirRate(eirRate);
		insuranceIncomeSchedules.setTotalInsuranceIncome(totalInsuranceIncome);
		insuranceIncomeSchedules.setSchedules(schedules);
		insuranceIncomeSchedules.setSplitWithInstallment(false);
		return insuranceIncomeSchedules;
	}
}
