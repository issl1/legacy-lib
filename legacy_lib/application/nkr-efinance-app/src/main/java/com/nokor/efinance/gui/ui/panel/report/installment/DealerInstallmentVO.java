package com.nokor.efinance.gui.ui.panel.report.installment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.Payment;
import com.vaadin.ui.CheckBox;

/**
 * 
 * @author prasnar
 *
 */
public class DealerInstallmentVO implements Serializable {
	/** */
	private static final long serialVersionUID = -799303557062304069L;

	private Dealer dealer;
	private CheckBox checkBox;
	private Date date;
	
	private int numInstallment;
	private double installmentAmount;
	private double totalOtherAmount;
	private double totalPenalty;
	private double totalAmount;
	private double amountPerWing;
	private double amountPerCash;
	private List<Payment> payments;
	
	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}
	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	/**
	 * @return the checkBox
	 */
	public CheckBox getCheckBox() {
		return checkBox;
	}
	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the numInstallment
	 */
	public int getNumInstallment() {
		return numInstallment;
	}
	/**
	 * @param numInstallment the numInstallment to set
	 */
	public void setNumInstallment(int numInstallment) {
		this.numInstallment = numInstallment;
	}
	/**
	 * @return the installmentAmount
	 */
	public double getInstallmentAmount() {
		return installmentAmount;
	}
	/**
	 * @param installmentAmount the installmentAmount to set
	 */
	public void setInstallmentAmount(double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	/**
	 * @return the totalOtherAmount
	 */
	public double getTotalOtherAmount() {
		return totalOtherAmount;
	}
	/**
	 * @param totalOtherAmount the totalOtherAmount to set
	 */
	public void setTotalOtherAmount(double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}
	/**
	 * @return the totalPenalty
	 */
	public double getTotalPenalty() {
		return totalPenalty;
	}
	/**
	 * @param totalPenalty the totalPenalty to set
	 */
	public void setTotalPenalty(double totalPenalty) {
		this.totalPenalty = totalPenalty;
	}
	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * @return the amountPerWing
	 */
	public double getAmountPerWing() {
		return amountPerWing;
	}
	/**
	 * @param amountPerWing the amountPerWing to set
	 */
	public void setAmountPerWing(double amountPerWing) {
		this.amountPerWing = amountPerWing;
	}
	/**
	 * @return the amountPerCash
	 */
	public double getAmountPerCash() {
		return amountPerCash;
	}
	/**
	 * @param amountPerCash the amountPerCash to set
	 */
	public void setAmountPerCash(double amountPerCash) {
		this.amountPerCash = amountPerCash;
	}
	/**
	 * @return the payments
	 */
	public List<Payment> getPayments() {
		return payments;
	}
	/**
	 * @param payments the payments to set
	 */
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
}
