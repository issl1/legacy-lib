package com.nokor.efinance.core.financial.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.finance.services.Calculator;
import com.nokor.finance.services.impl.GLFCalculatorImpl;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * Finance calculation service
 * @author ly.youhort
 */
@Service("financeCalculationService")
public class FinanceCalculationServiceImpl implements FinanceCalculationService {

	/**
	 * Calculate installment amount
	 * @return
	 */
	@Override
	public double getInstallmentPayment(CalculationParameter calculationParameter) {
		return getCalculator().calculateInstallmentPayment(calculationParameter);
	}
	
	/**
	 * Calculate total interest
	 * @return
	 */
	@Override
	public double getTotalInterest(CalculationParameter calculationParameter) {
		return getCalculator().calculateTotalInterest(calculationParameter);
	}

	/**
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	@Override
	public AmortizationSchedules getAmortizationSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		return getCalculator().getAmortizationSchedules(startDate, firstInstallmentDate, calculationParameter);
	}
	
	/**
	 * @return
	 */
	private Calculator getCalculator() {
 		 return new GLFCalculatorImpl();
	}
	
}
