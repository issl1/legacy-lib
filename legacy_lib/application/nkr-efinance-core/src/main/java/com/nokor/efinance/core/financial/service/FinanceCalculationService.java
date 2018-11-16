package com.nokor.efinance.core.financial.service;

import java.util.Date;

import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * Calculation Service
 * @author ly.youhort
 */
public interface FinanceCalculationService {

	/**
	 * Calculate installment amount
	 * @return
	 */
	double getInstallmentPayment(CalculationParameter calculationParameter);
	
	/**
	 * Calculate total interest
	 * @return
	 */
	double getTotalInterest(CalculationParameter calculationParameter);
	
	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	AmortizationSchedules getAmortizationSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter);
}
