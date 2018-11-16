package com.nokor.efinance.third.wing.server.payment.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ly.youhort
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Financial")
public class Financial implements Serializable {
	
	private static final long serialVersionUID = 8901056607594814471L;
	
	@XmlElement(name = "installmentAmount", required = true)
	private double installmentAmount;
	@XmlElement(name = "insuranceFeeAmount", required = false)
	private double insuranceFeeAmount;
	@XmlElement(name = "servicingFeeAmount", required = false)
	private double servicingFeeAmount;
	@XmlElement(name = "penaltyAmount", required = false)
	private double penaltyAmount;
	@XmlElement(name = "otherFeeAmount", required = false)
	private double otherFeeAmount;
	@XmlElement(name = "totalAmountToPaid", required = true)
	private double totalAmountToPaid;
	
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
	 * @return the insuranceFeeAmount
	 */
	public double getInsuranceFeeAmount() {
		return insuranceFeeAmount;
	}
	/**
	 * @param insuranceFeeAmount the insuranceFeeAmount to set
	 */
	public void setInsuranceFeeAmount(double insuranceFeeAmount) {
		this.insuranceFeeAmount = insuranceFeeAmount;
	}
	/**
	 * @return the servicingFeeAmount
	 */
	public double getServicingFeeAmount() {
		return servicingFeeAmount;
	}
	/**
	 * @param servicingFeeAmount the servicingFeeAmount to set
	 */
	public void setServicingFeeAmount(double servicingFeeAmount) {
		this.servicingFeeAmount = servicingFeeAmount;
	}
	/**
	 * @return the penaltyAmount
	 */
	public double getPenaltyAmount() {
		return penaltyAmount;
	}
	/**
	 * @param penaltyAmount the penaltyAmount to set
	 */
	public void setPenaltyAmount(double penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}	
	/**
	 * @return the otherFeeAmount
	 */
	public double getOtherFeeAmount() {
		return otherFeeAmount;
	}
	/**
	 * @param otherFeeAmount the otherFeeAmount to set
	 */
	public void setOtherFeeAmount(double otherFeeAmount) {
		this.otherFeeAmount = otherFeeAmount;
	}
	/**
	 * @return the totalAmountToPaid
	 */
	public double getTotalAmountToPaid() {
		return totalAmountToPaid;
	}
	/**
	 * @param totalAmountToPaid the totalAmountToPaid to set
	 */
	public void setTotalAmountToPaid(double totalAmountToPaid) {
		this.totalAmountToPaid = totalAmountToPaid;
	}
}
