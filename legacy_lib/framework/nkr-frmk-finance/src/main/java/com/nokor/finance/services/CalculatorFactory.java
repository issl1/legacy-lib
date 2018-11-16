package com.nokor.finance.services;

import com.nokor.finance.services.exception.InvalidCashFlowLayoutException;
import com.nokor.finance.services.impl.BaseCalculatorStrategyImpl;
import com.nokor.finance.services.shared.CashFlowLayout;

/**
 * Calculator factory
 * @author ly.youhort
 */
public class CalculatorFactory {
	
	/**
	 * @param cashFlowLayout
	 * @return
	 * @throws InvalidCashFlowLayoutException
	 */
	public static Calculator getInstance(CashFlowLayout cashFlowLayout)
			throws InvalidCashFlowLayoutException {
		return getInstance(cashFlowLayout, new BaseCalculatorStrategyImpl());
	}

	/**
	 * @param cashFlowLayout
	 * @param calculatorStrategy
	 * @return
	 * @throws InvalidCashFlowLayoutException
	 */
	public static Calculator getInstance(CashFlowLayout cashFlowLayout, CalculatorStrategy calculatorStrategy)
			throws InvalidCashFlowLayoutException {
		return null;
	}
}
