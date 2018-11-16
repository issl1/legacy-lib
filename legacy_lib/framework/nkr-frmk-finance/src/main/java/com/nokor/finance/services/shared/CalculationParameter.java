package com.nokor.finance.services.shared;

import java.io.Serializable;

import com.nokor.finance.services.shared.system.EFrequency;

/**
 * Finance calculation parameter
 * @author ly.youhort
 */
public class CalculationParameter implements Serializable {
		
	private static final long serialVersionUID = 5454459817868446286L;	
	
	/** Asset amount */
	private double assetPrice;
	/** Total Number of periods */
	private int numberOfPeriods;	
	/** Initial Principal (loan amount) */
	private double initialPrincipal;
	/** Periodic Interest rate */
	private double periodicInterestRate;
	/** Frequency */
	private EFrequency frequency;	
	/** Number of principal grace period */
	private int numberOfPrincipalGracePeriods;
	
	/** Installment amount */
	private double installmentAmount;
	
	/** Insurance fee */
	private double insuranceFee;
	/** Insurance fee */
	private double registrationFee;
	/** Insurance fee */
	private double servicingFee;
			
	/**
	 * @return the assetPrice
	 */
	public double getAssetPrice() {
		return assetPrice;
	}
	/**
	 * @param assetPrice the assetPrice to set
	 */
	public void setAssetPrice(double assetPrice) {
		this.assetPrice = assetPrice;
	}
	/**
	 * @return the numberOfPeriods
	 */
	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}
	/**
	 * @param numberOfPeriods the numberOfPeriods to set
	 */
	public void setNumberOfPeriods(int numberOfPeriods) {
		this.numberOfPeriods = numberOfPeriods;
	}
	/**
	 * @return the initialPrincipal
	 */
	public double getInitialPrincipal() {
		return initialPrincipal;
	}
	/**
	 * @param initialPrincipal the initialPrincipal to set
	 */
	public void setInitialPrincipal(double initialPrincipal) {
		this.initialPrincipal = initialPrincipal;
	}
	/**
	 * @return the periodicInterestRate
	 */
	public double getPeriodicInterestRate() {
		return periodicInterestRate;
	}
	/**
	 * @param periodicInterestRate the periodicInterestRate to set
	 */
	public void setPeriodicInterestRate(double periodicInterestRate) {
		this.periodicInterestRate = periodicInterestRate;
	}
	/**
	 * @return the frequency
	 */
	public EFrequency getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(EFrequency frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the insuranceFee
	 */
	public double getInsuranceFee() {
		return insuranceFee;
	}
	/**
	 * @param insuranceFee the insuranceFee to set
	 */
	public void setInsuranceFee(double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	/**
	 * @return the registrationFee
	 */
	public double getRegistrationFee() {
		return registrationFee;
	}
	/**
	 * @param registrationFee the registrationFee to set
	 */
	public void setRegistrationFee(double registrationFee) {
		this.registrationFee = registrationFee;
	}
	/**
	 * @return the servicingFee
	 */
	public double getServicingFee() {
		return servicingFee;
	}
	/**
	 * @param servicingFee the servicingFee to set
	 */
	public void setServicingFee(double servicingFee) {
		this.servicingFee = servicingFee;
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
	 * @return the numberOfPrincipalGracePeriods
	 */
	public int getNumberOfPrincipalGracePeriods() {
		return numberOfPrincipalGracePeriods;
	}
	/**
	 * @param numberOfPrincipalGracePeriods the numberOfPrincipalGracePeriods to set
	 */
	public void setNumberOfPrincipalGracePeriods(int numberOfPrincipalGracePeriods) {
		this.numberOfPrincipalGracePeriods = numberOfPrincipalGracePeriods;
	}	
}
