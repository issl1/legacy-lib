package com.nokor.efinance.core.contract.model.cashflow;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Transaction VO
 * @author bunlong.taing
 */
public class TransactionVO implements Comparable<TransactionVO> {
	
	public static final String ID = "id";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String REFERENCE = "reference";
	public static final String DETAILS = "details";
	public static final String BALANCEAMOUNT = "balanceAmount";
	public static final String PASTDUEAMOUNT = "pastDueAmount";
	public static final String PAYMENTDATE = "paymentDate";
	public static final String STATUS = "status";

	private Long cashflowId;
	private Date date;
	private Type type;
	private String reference;
	
	private Amount principal;
	private Amount interest;
	
	private Amount balanceAmount;
	private Amount pastDueAmount;
	
	private Integer numInstallment;
	private EWkfStatus wkfStatus;
	private Date paymentDate;
	
	private Amount paidAmount;
	private Integer nbOverdueDay;
	
	/**
	 * @return the cashflowId
	 */
	public Long getCashflowId() {
		return cashflowId;
	}

	/**
	 * @param cashflowId the cashflowId to set
	 */
	public void setCashflowId(Long cashflowId) {
		this.cashflowId = cashflowId;
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
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * @return
	 */
	public double getDueAmount() {
		return (getPrincipal() != null ? getPrincipal().getTiAmount() : 0d) + (getInterest() != null ? getInterest().getTiAmount() : 0d);
	}
	
	/**
	 * @return
	 */
	public double getDueVatAmount() {
		return (getPrincipal() != null ? getPrincipal().getVatAmount() : 0d) + (getInterest() != null ? getInterest().getVatAmount() : 0d);
	}
	
	/**
	 * @return the details
	 */
	public String getDueDetails() {
		String details = "";		
		details += I18N.message("principle") + " : " + "<b>" + AmountUtils.format(getPrincipal() != null ? getPrincipal().getTeAmount() : 0d) + "</b>";
		details += "<br/>";
		details += I18N.message("interest") + " : " + "<b>" + AmountUtils.format(getInterest() != null ? getInterest().getTeAmount() : 0d) + "</b>";				
		details += "<br/>";
		details += I18N.message("vat") + " : " + "<b>" + AmountUtils.format((getPrincipal() != null ? getPrincipal().getVatAmount() : 0d) + 
				(getInterest() != null ? getInterest().getVatAmount() : 0d)) + "</b>";
		return details;
	}	

	/**
	 * @return the wkfStatus
	 */
	public EWkfStatus getWkfStatus() {
		return wkfStatus;
	}

	/**
	 * @param wkfStatus the wkfStatus to set
	 */
	public void setWkfStatus(EWkfStatus wkfStatus) {
		this.wkfStatus = wkfStatus;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}	
	
	/**
	 * @return the numInstallment
	 */
	public Integer getNumInstallment() {
		return numInstallment;
	}

	/**
	 * @param numInstallment the numInstallment to set
	 */
	public void setNumInstallment(Integer numInstallment) {
		this.numInstallment = numInstallment;
	}
		
	
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the principal
	 */
	public Amount getPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(Amount principal) {
		this.principal = principal;
	}

	/**
	 * @return the interest
	 */
	public Amount getInterest() {
		return interest;
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(Amount interest) {
		this.interest = interest;
	}	

	/**
	 * @return the balanceAmount
	 */
	public Amount getBalanceAmount() {
		return balanceAmount;
	}

	/**
	 * @param balanceAmount the balanceAmount to set
	 */
	public void setBalanceAmount(Amount balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	/**
	 * @return the pastDueAmount
	 */
	public Amount getPastDueAmount() {
		return pastDueAmount;
	}

	/**
	 * @param pastDueAmount the pastDueAmount to set
	 */
	public void setPastDueAmount(Amount pastDueAmount) {
		this.pastDueAmount = pastDueAmount;
	}
	
	/**
	 * @return the paidAmount
	 */
	public Amount getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(Amount paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @return the nbOverdueDay
	 */
	public Integer getNbOverdueDay() {
		return nbOverdueDay;
	}

	/**
	 * @param nbOverdueDay the nbOverdueDay to set
	 */
	public void setNbOverdueDay(Integer nbOverdueDay) {
		this.nbOverdueDay = nbOverdueDay;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TransactionVO value) {
		return this.date == null
				? (value.getDate() == null ? 0 : -1)
				: (value.getDate() == null ? 1 : this.date.compareTo(value.getDate()));
	}

	/**
	 * @author bunlong.taing
	 */
	public enum Type {
		LoanOrigination,
		Installment,
		Fee,
		Penalty,
		ServicingFee,
		InsuranceFee,
		Commission,
		CollectionFee,
		RepossessionFee,
		OperationFee,
		TransferFee,
		PressingFee,
		Payment;
	}

}
