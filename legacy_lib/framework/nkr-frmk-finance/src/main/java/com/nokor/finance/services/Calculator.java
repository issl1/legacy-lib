package com.nokor.finance.services;

import java.util.Date;

import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;


/**
 * @author
 * @version $Revision: 1.0 $
 */
public interface Calculator {

	/**
	 * Calculate Periodic Payment (PP). <br/>
	 * 
	 * @param calculationParameter
	 *            dimensions from which the calculation must be performed
	 * @return install payment
	 * 
	 */
	double calculateInstallmentPayment(CalculationParameter calculationParameter);
	
	/**
	 * Calculate total interest. <br/>
	 * 
	 * @param calculationParameter
	 *            dimensions from which the calculation must be performed
	 * @return install payment
	 * 
	 */
	double calculateTotalInterest(CalculationParameter calculationParameter);
	
	/** 
	 * Get amortization schedule
	 * @param startDate start date
	 * @param firstInstallmentDate
	 * @param calculationParameter
	 * @return
	 */
	public AmortizationSchedules getAmortizationSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter);
}
