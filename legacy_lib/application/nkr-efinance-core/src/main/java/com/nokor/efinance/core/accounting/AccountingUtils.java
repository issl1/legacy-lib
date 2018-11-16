package com.nokor.efinance.core.accounting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.accounting.RevenueIncomeSchedules;
import com.nokor.efinance.glf.accounting.service.impl.GLFInterestIncomeCalculatorImpl2;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.tools.formula.Rate;

public class AccountingUtils {

	/**
	 * 
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @param calculationParameter2
	 * @return
	 */
	public static List<Double> getInterestSchedules(Date startDate, CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		
		List<Double> schedules = new ArrayList<>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		if (calculationParameter2 != null) {
			initialPrincipal = calculationParameter2.getInitialPrincipal();
		}
		
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		double installmentPayment = calculateInstallmentPayment(calculationParameter);
		
		double irrRate = Rate.calculateIRR(numberOfPeriods, installmentPayment, initialPrincipal);
				
		for (int i = 0; i <= numberOfPeriods; i++) {
			
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
			
			initialPrincipal = balanceAmount;
			
			schedules.add(interestAmount);
		}
		
		return schedules;
	}
	
	/**
	 * @param startDate
	 * @param firstInstallmentDate
	 * @param leaseTransationsParameter
	 * @param netLeasingsParameter
	 * @return
	 */
	public static List<Double> getDistributionsRate(Date startDate, Date firstInstallmentDate, 
			CalculationParameter leaseTransationsParameter, CalculationParameter netLeasingsParameter) {
		
		List<Double> schedules = new ArrayList<>();
		
		GLFInterestIncomeCalculatorImpl2 leaseTransactionsCalculator = new GLFInterestIncomeCalculatorImpl2();
		GLFInterestIncomeCalculatorImpl2 netLeasingsCalculator = new GLFInterestIncomeCalculatorImpl2();
		RevenueIncomeSchedules leaseTransationsSchedules = null;
		RevenueIncomeSchedules netLeasingsSchedules = null;
		if (isSameMonth(DateUtils.addMonthsDate(startDate, 1), firstInstallmentDate)) {
			leaseTransationsSchedules = leaseTransactionsCalculator.getSchedulesCase1(startDate, firstInstallmentDate, leaseTransationsParameter, null);
			netLeasingsSchedules = netLeasingsCalculator.getSchedulesCase1(startDate, firstInstallmentDate, leaseTransationsParameter, netLeasingsParameter);
		} else if (isSameMonth(startDate, firstInstallmentDate)) {
			leaseTransationsSchedules = leaseTransactionsCalculator.getSchedulesCase2(startDate, firstInstallmentDate, leaseTransationsParameter, null);
			netLeasingsSchedules = netLeasingsCalculator.getSchedulesCase2(startDate, firstInstallmentDate, leaseTransationsParameter, netLeasingsParameter);
		} else {
			leaseTransationsSchedules = leaseTransactionsCalculator.getSchedulesCase3(startDate, firstInstallmentDate, leaseTransationsParameter, null);
			netLeasingsSchedules = netLeasingsCalculator.getSchedulesCase3(startDate, firstInstallmentDate, leaseTransationsParameter, netLeasingsParameter);
		}
		
		double diffRevenue = leaseTransationsSchedules.getMonthyTotalRevenue() - netLeasingsSchedules.getMonthyTotalRevenue();
		
		for (int i = 0; i < leaseTransationsSchedules.getSchedules().size(); i++) {
			double distributionRate = (leaseTransationsSchedules.getSchedules().get(i).getMonthlyRevenue() - netLeasingsSchedules.getSchedules().get(i).getMonthlyRevenue()) / diffRevenue;
			schedules.add(distributionRate);
		}
		
		return schedules;
	}
	
	/**
	 * @param calculationParameter
	 * @return
	 */
	private static double calculateInstallmentPayment(CalculationParameter calculationParameter) {		
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods() - calculationParameter.getNumberOfPrincipalGracePeriods();		
		double totalInterest = initialPrincipal * periodicInterestRate * numberOfPeriods;		
		double installmentPayment = (initialPrincipal + totalInterest) / numberOfPeriods;		
		return installmentPayment;
	}
	
	/**
	 * @param date1
	 * @param date2
	 * @return
	 */
	private static boolean isSameMonth(Date date1, Date date2) {
		return DateUtils.getDateLabel(date1, "MMyyyy").equals(DateUtils.getDateLabel(date2, "MMyyyy"));
	}
}
