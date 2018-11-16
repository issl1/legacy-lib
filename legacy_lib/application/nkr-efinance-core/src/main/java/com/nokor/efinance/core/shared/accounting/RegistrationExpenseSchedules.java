package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;

import com.nokor.finance.services.shared.CalculationParameter;

/**
 * Amortization Schedule
 * @author ly.youhort
 *
 */
public class RegistrationExpenseSchedules implements Serializable {

	private static final long serialVersionUID = -8427665584435681184L;
	
	private Date startDate;
	private Date firstInstallmentDate;
	private CalculationParameter calculationParameter;
	private List<RegistrationExpenseSchedule> schedules;
	private Double eirRate;
	private Double totalRegistrationExpense;

	/**
	 * @param calculationParameter
	 */
	public RegistrationExpenseSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
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
	 * @return the schedules
	 */
	public List<RegistrationExpenseSchedule> getSchedules() {
		return schedules;
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void setSchedules(List<RegistrationExpenseSchedule> schedules) {
		this.schedules = schedules;
	}
		
	/**
	 * @return the startDate
	 */
	public Date getStartDate1() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate1(Date startDate) {
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
	 * @return the eirRate
	 */
	public Double getEirRate() {
		return eirRate;
	}

	/**
	 * @param eirRate the eirRate to set
	 */
	public void setEirRate(Double eirRate) {
		this.eirRate = eirRate;
	}

	/**
	 * @return the totalRegistrationExpense
	 */
	public Double getTotalRegistrationExpense() {
		return totalRegistrationExpense;
	}

	/**
	 * @param totalRegistrationExpense the totalRegistrationExpense to set
	 */
	public void setTotalRegistrationExpense(Double totalRegistrationExpense) {
		this.totalRegistrationExpense = totalRegistrationExpense;
	}

	@Override
	public String toString() {
		String output = "";
	
		output += StringUtils.rightPad("No.", 5);
		output += "|" + StringUtils.rightPad("Date.", 15);
		output += "|" + StringUtils.rightPad("Start Date.", 15);
		output += "|" + StringUtils.rightPad("End Date.", 15);
		output += "|" + StringUtils.leftPad("Ins Ex dist. 2.", 15);
		output += "|" + StringUtils.leftPad("Ins Ex dist. 3.", 15);
		output += "|" + StringUtils.leftPad("Cumulative.", 15);
		output += "|" + StringUtils.leftPad("Bal Ins Ex.", 15);
		output += "\n";
		output += StringUtils.rightPad("", 5);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "|" + StringUtils.rightPad("", 15);
		output += "\n";
		
		for (RegistrationExpenseSchedule schedule : schedules) {
			output += StringUtils.rightPad(String.valueOf(schedule.getN()), 5);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getInstallmentDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodStartDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodEndDate()), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getRegistrationExpenseDistribution2())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getRegistrationExpenseDistribution3())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getCumulativeBalance())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getBalanceRegistrationExpense())), 15);
			output += "\n";
		}
		
		return output.toString();
	}
		
}

