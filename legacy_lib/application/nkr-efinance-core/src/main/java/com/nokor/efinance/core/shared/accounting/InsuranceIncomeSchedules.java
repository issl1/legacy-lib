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
public class InsuranceIncomeSchedules implements Serializable {

	private static final long serialVersionUID = -8427665584435681184L;
	
	private Date startDate;
	private Date firstInstallmentDate;
	private CalculationParameter calculationParameter;
	private List<InsuranceIncomeSchedule> schedules;
	private Double eirRate;
	private Double totalInsuranceIncome;
	private boolean splitWithInstallment;

	/**
	 * @param calculationParameter
	 */
	public InsuranceIncomeSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
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
	public List<InsuranceIncomeSchedule> getSchedules() {
		return schedules;
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void setSchedules(List<InsuranceIncomeSchedule> schedules) {
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
	 * @return the totalInsuranceIncome
	 */
	public Double getTotalInsuranceIncome() {
		return totalInsuranceIncome;
	}

	/**
	 * @param totalInsuranceIncome the totalInsuranceIncome to set
	 */
	public void setTotalInsuranceIncome(Double totalInsuranceIncome) {
		this.totalInsuranceIncome = totalInsuranceIncome;
	}
	
	/**
	 * @return the splitWithInstallment
	 */
	public boolean isSplitWithInstallment() {
		return splitWithInstallment;
	}

	/**
	 * @param splitWithInstallment the splitWithInstallment to set
	 */
	public void setSplitWithInstallment(boolean splitWithInstallment) {
		this.splitWithInstallment = splitWithInstallment;
	}

	@Override
	public String toString() {
		String output = "";
	
		output += StringUtils.rightPad("No.", 5);
		output += "|" + StringUtils.rightPad("Date.", 15);
		output += "|" + StringUtils.rightPad("Start Date.", 15);
		output += "|" + StringUtils.rightPad("End Date.", 15);
		output += "|" + StringUtils.leftPad("Ins Income dist. 2.", 15);
		output += "|" + StringUtils.leftPad("Ins Income dist. 3.", 15);
		output += "|" + StringUtils.leftPad("Cumulative.", 15);
		output += "|" + StringUtils.leftPad("Unearned Ins Income.", 15);
		output += "|" + StringUtils.leftPad("Acc. Receivable.", 15);
		output += "|" + StringUtils.leftPad("Real Insurance Income.", 15);
		output += "|" + StringUtils.leftPad("In Suspend.", 15);
		output += "|" + StringUtils.leftPad("In Suspend Cumulated.", 15);
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
		output += "\n";
		
		for (InsuranceIncomeSchedule schedule : schedules) {
			output += StringUtils.rightPad(String.valueOf(schedule.getN()), 5);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getInstallmentDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodStartDate()), 15);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getPeriodEndDate()), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInsuranceIncomeDistribution2())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInsuranceIncomeDistribution3())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getCumulativeBalance())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getUnearnedInsuranceIncome())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInsuranceIncomeReceived())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getAccountReceivable())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getRealInsuranceIncomeDistributed())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInsuranceIncomeInSuspend())), 15);
			output += "|" + StringUtils.leftPad(String.valueOf(MyMathUtils.roundAmountTo(schedule.getInsuranceIncomeInSuspendCumulated())), 15);
			output += "\n";
		}
		
		return output.toString();
	}
		
}

