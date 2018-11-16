package com.nokor.efinance.core.shared.accounting;

import java.io.IOException;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;

import com.nokor.finance.services.impl.GLFCalculatorImpl;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.finance.services.tools.formula.Rate;


public class MainTst {

	public static void main(String[] args) throws IOException {

		/*CalculationParameter p = new CalculationParameter();
		p.setInitialPrincipal(45794.39d);
		p.setNumberOfPeriods(36);
		p.setNumberOfPrincipalGracePeriods(0);
		p.setPeriodicInterestRate(1.99/100);
		p.setFrequency(EFrequency.M);*/
					
		/*AmortizationSchedules amortizationSchedules = new GLFCalculatorImpl().getAmortizationSchedules(DateUtils.getDate("06062014",  "ddMMyyyy"), DateUtils.getDate("11072014",  "ddMMyyyy"), p);
		
		System.out.println(MyMathUtils.calculateFromAmountExcl(amortizationSchedules.getSchedules().get(1).getInstallmentPayment(), 0.09).getTiAmount());
		
		System.out.println(MyMathUtils.calculateFromAmountExcl(amortizationSchedules.getSchedules().get(1).getInstallmentPayment(), 0.09).getTiAmount());
		
		System.out.println(amortizationSchedules);*/
		CalculationParameter p = new CalculationParameter();
		p.setInitialPrincipal(1800.00d);
		p.setNumberOfPeriods(24);
		p.setNumberOfPrincipalGracePeriods(0);
		p.setPeriodicInterestRate(1.95/100);
		p.setFrequency(EFrequency.M);
		
		/*AmortizationSchedules amortizationSchedules = new GLFCalculatorImpl().getAmortizationSchedules(DateUtils.getDate("06062014",  "ddMMyyyy"), DateUtils.getDate("11072014",  "ddMMyyyy"), p);
		System.out.println(amortizationSchedules);
		
		System.out.println(Rate.calculateIRR(LoanUtils.getNumberOfPeriods(24, EFrequency.M), 110.1d, 1800.00d));*/
		
		double a  = 20000;
		int r = 5;
		
		double aa = Math.ceil(a / r) * r;
		//System.out.println(aa);
		
		System.out.println(MyMathUtils.calculateFromAmountIncl(2235,0.07).getTeAmount());
		System.out.println(MyMathUtils.calculateFromAmountIncl(2235,0.07).getVatAmount());
		
	}
}
