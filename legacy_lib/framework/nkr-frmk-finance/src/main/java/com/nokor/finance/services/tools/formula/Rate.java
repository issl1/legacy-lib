package com.nokor.finance.services.tools.formula;

/**
 * @author ly.youhort
 */
public final class Rate {
	
	/**
	 * @param nper
	 * @param pmt is the payment made each period and cannot change over the life of the annuity. Typically, pmt includes principal and interest but no other fees or taxes. If pmt is omitted, you must include the fv argument.
	 * @param pv is the present value — the total amount that a series of future payments is worth now.
	 * @return
	 */
	public static double calculateRate(int nper, double pmt, double pv) {
		return calculateRate(nper, pmt, pv, 0, 0, 0.1);
	}
	
	/**
	 * @param nper is the total number of payment periods in an annuity.
	 * @param pmt is the payment made each period and cannot change over the life of the annuity. Typically, pmt includes principal and interest but no other fees or taxes. If pmt is omitted, you must include the fv argument.
	 * @param pv is the present value — the total amount that a series of future payments is worth now.
	 * @param fv is the future value, or a cash balance you want to attain after the last payment is made. If fv is omitted, it is assumed to be 0 (the future value of a loan, for example, is 0).
	 * @param type  is the number 0 or 1 and indicates when payments are due. (0 or omitted	At the end of the period, 1	At the beginning of the period)
	 * @param guess is your guess for what the rate will be. (If you omit guess, it is assumed to be 10 percent)
	 * @return
	 */
	public static double calculateRate(int nper, double pmt, double pv, double fv, double type, double guess) {
		// FROM MS
		// http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
		int FINANCIAL_MAX_ITERATIONS = 20;// Bet accuracy with 128
		double FINANCIAL_PRECISION = 0.0000001;// 1.0e-8

		double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
		double rate = guess;
		if (Math.abs(rate) < FINANCIAL_PRECISION) {
			y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
		} else {
			f = Math.pow(1 + rate, nper);
			y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
		}
		
		y0 = pv + pmt * nper + fv;
		y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

		// find root by Newton secant method
		i = x0 = 0.0;
		x1 = rate;
		while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
			rate = (y1 * x0 - y0 * x1) / (y1 - y0);
			x0 = x1;
			x1 = rate;

			if (Math.abs(rate) < FINANCIAL_PRECISION) {
				y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
			} else {
				f = Math.pow(1 + rate, nper);
				y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
			}

			y0 = y1;
			y1 = y;
			++i;
		}
		return rate;
	}
	
	
	/**
	 * @param nper
	 * @param pmt
	 * @param pv
	 * @return
	 */
	public static double calculateIRR(int nper, double pmt, double pv) {	
		double[] cashflows = new double[nper + 1];
		cashflows[0] = -1 * pv;
		for (int i = 1; i <= nper; i++) {
			cashflows[i] = pmt;
		}
		return calculateIRR(cashflows);
	}
	
	/**
	 * Calculate 
	 * @param cashflows
	 * @return
	 */
	private static double calculateIRR(final double[] cashflows) {
		final int MAX_ITER = 10000;
		double EXCEL_EPSILON = 0.0000001;
		double irr = 0.1;
		int iter = 0;
		while (iter++ < MAX_ITER) {
			final double x1 = 1.0 + irr;
			double fx = 0.0;
			double dfx = 0.0;
			for (int i = 0; i < cashflows.length; i++) {
				final double v = cashflows[i];
				final double x1_i = Math.pow(x1, i);
				fx += v / x1_i;
				final double x1_i1 = x1_i * x1;
				dfx += -i * v / x1_i1;
			}
			final double new_irr = irr - fx / dfx;
			final double epsilon = Math.abs(new_irr - irr);

			if (epsilon <= EXCEL_EPSILON) {
				if (irr == 0.0 && Math.abs(new_irr) <= EXCEL_EPSILON) {
					return 0.0;
				} else {
					return new_irr;
				}
			}
			irr = new_irr;
		}
		return irr;
	}
}
