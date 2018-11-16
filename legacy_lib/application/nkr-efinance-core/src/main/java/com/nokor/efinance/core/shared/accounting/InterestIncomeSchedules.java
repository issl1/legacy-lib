package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.finance.services.shared.CalculationParameter;

/**
 * 
 * @author meng
 *
 */
public class InterestIncomeSchedules implements Serializable {

	private static final long serialVersionUID = -8427665584435681184L;
	
	private Date startDate;
	private Date firstInstallmentDate;
	private CalculationParameter calculationParameter;
	private List<InterestIncomeSchedule> schedules;
	private List<Cashflow> principalCashflows;
	private List<Cashflow> interestCashflows;

	/**
	 * @param calculationParameter
	 */
	public InterestIncomeSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
		this.startDate = startDate;
		this.firstInstallmentDate = firstInstallmentDate;
		this.calculationParameter = calculationParameter;
	}
	
	/**
	 * @return the calculationParameter
	 */
	public CalculationParameter getCalculationParameter() {
		return calculationParameter;
	}

	/**
	 * @param calculationParameter the calculationParameter to set
	 */
	public void setCalculationParameter(CalculationParameter calculationParameter) {
		this.calculationParameter = calculationParameter;
	}

	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the firstInstallmentDate
	 */
	public Date getFirstInstallmentDate() {
		return firstInstallmentDate;
	}

	/**
	 * @param firstInstallmentDate the firstInstallmentDate to set
	 */
	public void setFirstInstallmentDate(Date firstInstallmentDate) {
		this.firstInstallmentDate = firstInstallmentDate;
	}

	/**
	 * @return the schedules
	 */
	public List<InterestIncomeSchedule> getSchedules() {
		return schedules;
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void setSchedules(List<InterestIncomeSchedule> schedules) {
		this.schedules = schedules;
	}

	/**
	 * @return the principalCashflows
	 */
	public List<Cashflow> getPrincipalCashflows() {
		return principalCashflows;
	}

	/**
	 * @param principalCashflows the principalCashflows to set
	 */
	public void setPrincipalCashflows(List<Cashflow> principalCashflows) {
		this.principalCashflows = principalCashflows;
	}

	/**
	 * @return the interestCashflows
	 */
	public List<Cashflow> getInterestCashflows() {
		return interestCashflows;
	}

	/**
	 * @param interestCashflows the interestCashflows to set
	 */
	public void setInterestCashflows(List<Cashflow> interestCashflows) {
		this.interestCashflows = interestCashflows;
	}

	@Override
	public String toString() {
		String output = "";
	
		output += StringUtils.rightPad("No.", 5);
		output += "|" + StringUtils.rightPad("Date.", 15);
		output += "|" + StringUtils.rightPad("Start Date.", 15);
		output += "|" + StringUtils.rightPad("End Date.", 15);
		output += "|" + StringUtils.leftPad("Int In Suspd.", 15);
		output += "|" + StringUtils.leftPad("Int Cmulated.", 15);
		output += "|" + StringUtils.leftPad("Int Revenue.", 15);
		output += "|" + StringUtils.leftPad("Int Inc Recei.", 15);
		output += "|" + StringUtils.leftPad("Pri Repay.", 15);
		output += "|" + StringUtils.leftPad("Pri Balance.", 15);
		output += "|" + StringUtils.leftPad("Int Inc Repay.", 15);
		output += "|" + StringUtils.leftPad("Bal Unearn Inc.", 15);
		output += "|" + StringUtils.leftPad("Num Install.", 15);
		output += "|" + StringUtils.leftPad("Overdue 30d.", 15);
		output += "\n";
		output += StringUtils.rightPad("", 5);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "\n";
		
		for (InterestIncomeSchedule schedule : schedules) {
			output += StringUtils.rightPad(String.valueOf(schedule.getN()), 5);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getInstallmentDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodStartDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodEndDate()), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInterestInSuspend())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInterestInSuspendCumulated())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInterestRevenue())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInterestIncomeReceivable())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getPrincipalRepayment())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getPrincipalBalance())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInterestIncomeRepayment())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getBalanceUnearedIncome())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(schedule.getNumInstallment()), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(schedule.isContractInOverdueMoreThanOneMonth()), 15);
			output += "\n";
		}
		
		return output.toString();
	}
		
}

