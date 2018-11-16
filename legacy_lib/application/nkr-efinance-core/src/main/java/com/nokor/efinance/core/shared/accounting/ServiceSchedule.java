package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author meng
 *
 */
public class ServiceSchedule implements Serializable {
	
	private static final long serialVersionUID = 2055645010414574256L;
	
	private int n;
	private Date installmentDate;
	
	private double dailyInterest1;
	private double dailyInterest2;
	private double monthlyRevenue;
	
	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * @param n the n to set
	 */
	public void setN(int n) {
		this.n = n;
	}

	/**
	 * @return the installmentDate
	 */
	public Date getInstallmentDate() {
		return installmentDate;
	}

	/**
	 * @param installmentDate the installmentDate to set
	 */
	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}

	/**
	 * @return the dailyInterest1
	 */
	public double getDailyInterest1() {
		return dailyInterest1;
	}

	/**
	 * @param dailyInterest1 the dailyInterest1 to set
	 */
	public void setDailyInterest1(double dailyInterest1) {
		this.dailyInterest1 = dailyInterest1;
	}

	/**
	 * @return the dailyInterest2
	 */
	public double getDailyInterest2() {
		return dailyInterest2;
	}

	/**
	 * @param dailyInterest2 the dailyInterest2 to set
	 */
	public void setDailyInterest2(double dailyInterest2) {
		this.dailyInterest2 = dailyInterest2;
	}

	/**
	 * @return the monthlyRevenue
	 */
	public double getMonthlyRevenue() {
		return monthlyRevenue;
	}

	/**
	 * @param monthlyRevenue the monthlyRevenue to set
	 */
	public void setMonthlyRevenue(double monthlyRevenue) {
		this.monthlyRevenue = monthlyRevenue;
	}
}
