package com.nokor.efinance.share.organization;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceSerieDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 6859210328371928769L;

	private UriDTO serie;
	
	private Double premium1Y;
	private Double premium2Y;
	private Double claimAmount1Y;
	private Double claimAmount2YFirstYear;
	private Double claimAmount2YSecondYear;
	
	/**
	 * @return the serie
	 */
	public UriDTO getSerie() {
		return serie;
	}
	
	/**
	 * @param serie the serie to set
	 */
	public void setSerie(UriDTO serie) {
		this.serie = serie;
	}

	/**
	 * @return the premium1Y
	 */
	public Double getPremium1Y() {
		return premium1Y;
	}

	/**
	 * @param premium1y the premium1Y to set
	 */
	public void setPremium1Y(Double premium1y) {
		premium1Y = premium1y;
	}

	/**
	 * @return the premium2Y
	 */
	public Double getPremium2Y() {
		return premium2Y;
	}

	/**
	 * @param premium2y the premium2Y to set
	 */
	public void setPremium2Y(Double premium2y) {
		premium2Y = premium2y;
	}

	/**
	 * @return the claimAmount1Y
	 */
	public Double getClaimAmount1Y() {
		return claimAmount1Y;
	}

	/**
	 * @param claimAmount1Y the claimAmount1Y to set
	 */
	public void setClaimAmount1Y(Double claimAmount1Y) {
		this.claimAmount1Y = claimAmount1Y;
	}

	/**
	 * @return the claimAmount2YFirstYear
	 */
	public Double getClaimAmount2YFirstYear() {
		return claimAmount2YFirstYear;
	}

	/**
	 * @param claimAmount2YFirstYear the claimAmount2YFirstYear to set
	 */
	public void setClaimAmount2YFirstYear(Double claimAmount2YFirstYear) {
		this.claimAmount2YFirstYear = claimAmount2YFirstYear;
	}

	/**
	 * @return the claimAmount2YSecondYear
	 */
	public Double getClaimAmount2YSecondYear() {
		return claimAmount2YSecondYear;
	}

	/**
	 * @param claimAmount2YSecondYear the claimAmount2YSecondYear to set
	 */
	public void setClaimAmount2YSecondYear(Double claimAmount2YSecondYear) {
		this.claimAmount2YSecondYear = claimAmount2YSecondYear;
	}
}
