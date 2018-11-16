package com.nokor.finance.services.tools;

import com.nokor.finance.services.shared.system.EFrequency;

/**
 * 
 * @author prasnar
 *
 */
public final class LoanUtils {
	/**
	 * Get Number Of Periods
	 * @param termInMonth
	 * @param frequency
	 * @return
	 */
	public static int getNumberOfPeriods(int termInMonth, EFrequency frequency) {
		return termInMonth / frequency.getNbMonths();
	}
	
	
}
