package com.nokor.finance.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.MyMathUtils;

import com.nokor.finance.services.Calculator;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;

/**
 * @author ly.youhort
 *
 */
public class CalculatorImpl implements Calculator {
	
	/**
	 * Calculate periodic installment amount
	 * @param calculationParameter
	 * @return
	 */
	@Override
	public double calculateInstallmentPayment(CalculationParameter calculationParameter) {
		
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
						
		double installmentPayment = initialPrincipal * (periodicInterestRate * (Math.pow(1 + periodicInterestRate, numberOfPeriods)) / (Math.pow(1 + periodicInterestRate, numberOfPeriods) - 1));
		
		return MyMathUtils.roundAmountTo(installmentPayment);
	}
	
	@Override
	public double calculateTotalInterest(CalculationParameter calculationParameter) {
		
		// TODO
		return MyMathUtils.roundAmountTo(0);
	}
	
	/**
	 * Get amortization schedule
	 * @param calculationParameter
	 * @return
	 */
	public AmortizationSchedules getAmortizationSchedules(Date startDate, Date installmentDate, CalculationParameter calculationParameter) {
		
		AmortizationSchedules amortizationSchedules = new AmortizationSchedules(startDate, installmentDate, calculationParameter);
		List<Schedule> schedules = new ArrayList<Schedule>();
		double initialPrincipal = calculationParameter.getInitialPrincipal();
		double periodicInterestRate = calculationParameter.getPeriodicInterestRate();
		int numberOfPeriods = calculationParameter.getNumberOfPeriods();
		
		double installmentPayment = calculateInstallmentPayment(calculationParameter);
		
		for (int i = 0; i < numberOfPeriods; i++) {
			Schedule schedule = new Schedule();
			double interestAmount =  MyMathUtils.roundAmountTo(initialPrincipal * periodicInterestRate);
			double principalAmount = MyMathUtils.roundAmountTo(installmentPayment - interestAmount);
			double balanceAmount = MyMathUtils.roundAmountTo(initialPrincipal - principalAmount);
			if (balanceAmount < 0) {
				balanceAmount = 0.0;
			}
			schedule.setN(i + 1);
			schedule.setInstallmentPayment(installmentPayment);
			schedule.setInterestAmount(interestAmount);
			schedule.setPrincipalAmount(principalAmount);
			schedule.setBalanceAmount(balanceAmount);
			initialPrincipal = balanceAmount;
			schedules.add(schedule);
		}
		amortizationSchedules.setSchedules(schedules);
		return amortizationSchedules;
	}
}
