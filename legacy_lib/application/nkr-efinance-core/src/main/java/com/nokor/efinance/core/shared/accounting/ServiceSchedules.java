package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.finance.services.shared.CalculationParameter;

/**
 * 
 * @author meng
 *
 */
public class ServiceSchedules implements Serializable {
	
	private static final long serialVersionUID = 4802761263532411418L;
	
	private Date startDate;
	private Date firstInstallmentDate;
	private CalculationParameter calculationParameter;
	private List<ServiceSchedule> schedules;
	private int runningCase;
	
	/**
	 * @param calculationParameter
	 */
	public ServiceSchedules(Date startDate, Date firstInstallmentDate, CalculationParameter calculationParameter) {
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
	public List<ServiceSchedule> getSchedules() {
		return schedules;
	}

	/**
	 * @param schedules the schedules to set
	 */
	public void setSchedules(List<ServiceSchedule> schedules) {
		this.schedules = schedules;
	}	

	/**
	 * @return the runningCase
	 */
	public int getRunningCase() {
		return runningCase;
	}

	/**
	 * @param runningCase the runningCase to set
	 */
	public void setRunningCase(int runningCase) {
		this.runningCase = runningCase;
	}

	public double getMonthyTotalRevenue() {
		double totalMonthyRevenue = 0d;
		for (ServiceSchedule schedule : schedules) {
			totalMonthyRevenue += schedule.getMonthlyRevenue();
		}
		return totalMonthyRevenue;
	}
	
	@Override
	public String toString() {
		String output = "";
	
		output += StringUtils.rightPad("No.", 5);
		output += "|" + StringUtils.rightPad("Date.", 25);
		output += "|" + StringUtils.rightPad("Daily Int. 1.", 25);
		output += "|" + StringUtils.rightPad("Daily Int. 2", 25);
		output += "|" + StringUtils.rightPad("Monthly Rev.", 25);
		output += "\n";
		for (ServiceSchedule schedule : schedules) {
			output += StringUtils.rightPad(String.valueOf(schedule.getN()), 5);
			output += "|" + StringUtils.rightPad(DateUtils.date2StringDDMMYYYY_SLASH(schedule.getInstallmentDate()), 25);
			output += "|" + StringUtils.rightPad(String.valueOf(schedule.getDailyInterest1()), 25);
			output += "|" + StringUtils.rightPad(String.valueOf(schedule.getDailyInterest2()), 25);
			output += "|" + StringUtils.rightPad(String.valueOf(schedule.getMonthlyRevenue()), 25);
			output += "\n";
		}
		
		return output.toString();
	}
		
}

