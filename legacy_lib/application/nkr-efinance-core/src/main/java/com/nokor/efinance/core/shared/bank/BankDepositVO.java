package com.nokor.efinance.core.shared.bank;

import java.util.Date;
import java.util.List;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.Payment;

/**
 * 
 * @author bunlong.taing
 *
 */
public class BankDepositVO {

	private int numInstallment;
	private double installmentAmount;
	private double totalOtherAmount;
	private double totalPenalty;
	private double totalAmount;
	private double amountPerWing;
	private double amountPerCash;
	private double amountReceivedFromDealerUSD;
	private double remainingAmount;
	private Date requestDate;
	private Dealer dealer;
	private List<Payment> payments;
	private double totalAmountRemaining;
	
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
	 * @return the amountReceivedFromDealerUSD
	 */
	public double getAmountReceivedFromDealerUSD() {
		return amountReceivedFromDealerUSD;
	}
	/**
	 * @param amountReceivedFromDealerUSD the amountReceivedFromDealerUSD to set
	 */
	public void setAmountReceivedFromDealerUSD(double amountReceivedFromDealerUSD) {
		this.amountReceivedFromDealerUSD = amountReceivedFromDealerUSD;
	}
	/**
	 * @return the remainingAmount
	 */
	public double getRemainingAmount() {
		return remainingAmount;
	}
	/**
	 * @param remainingAmount the remainingAmount to set
	 */
	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}
	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
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
	 * @return the totalAmountRemaining
	 */
	public double getTotalAmountRemaining() {
		return totalAmountRemaining;
	}
	/**
	 * @param totalAmountRemaining the totalAmountRemaining to set
	 */
	public void setTotalAmountRemaining(double totalAmountRemaining) {
		this.totalAmountRemaining = totalAmountRemaining;
	}
}
