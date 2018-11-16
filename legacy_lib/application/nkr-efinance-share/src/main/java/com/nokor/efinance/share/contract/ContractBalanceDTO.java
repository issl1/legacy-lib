package com.nokor.efinance.share.contract;

import java.io.Serializable;

import com.nokor.efinance.share.common.BalanceAmountDTO;

/**
 * 
 * @author uhout.cheng
 */
public class ContractBalanceDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 7981285941548985931L;

	private Long id;
	private String contractID;
	private BalanceAmountDTO balanceCapital;
	private BalanceAmountDTO balanceInterest;
	private BalanceAmountDTO discountInterest;
	private BalanceAmountDTO balancePenalty;
	private BalanceAmountDTO balanceFollowingFee;
	private BalanceAmountDTO balanceRepossessionFee;
	private BalanceAmountDTO balanceCollectionFee;
	private BalanceAmountDTO balanceOperationFee;
	private BalanceAmountDTO balancePressingFee;
	private BalanceAmountDTO balanceTransferFee;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}
	
	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the balanceCapital
	 */
	public BalanceAmountDTO getBalanceCapital() {
		return balanceCapital;
	}

	/**
	 * @param balanceCapital the balanceCapital to set
	 */
	public void setBalanceCapital(BalanceAmountDTO balanceCapital) {
		this.balanceCapital = balanceCapital;
	}

	/**
	 * @return the balanceInterest
	 */
	public BalanceAmountDTO getBalanceInterest() {
		return balanceInterest;
	}

	/**
	 * @param balanceInterest the balanceInterest to set
	 */
	public void setBalanceInterest(BalanceAmountDTO balanceInterest) {
		this.balanceInterest = balanceInterest;
	}

	/**
	 * @return the balancePenalty
	 */
	public BalanceAmountDTO getBalancePenalty() {
		return balancePenalty;
	}

	/**
	 * @param balancePenalty the balancePenalty to set
	 */
	public void setBalancePenalty(BalanceAmountDTO balancePenalty) {
		this.balancePenalty = balancePenalty;
	}

	/**
	 * @return the balanceFollowingFee
	 */
	public BalanceAmountDTO getBalanceFollowingFee() {
		return balanceFollowingFee;
	}

	/**
	 * @param balanceFollowingFee the balanceFollowingFee to set
	 */
	public void setBalanceFollowingFee(BalanceAmountDTO balanceFollowingFee) {
		this.balanceFollowingFee = balanceFollowingFee;
	}

	/**
	 * @return the balanceRepossessionFee
	 */
	public BalanceAmountDTO getBalanceRepossessionFee() {
		return balanceRepossessionFee;
	}

	/**
	 * @param balanceRepossessionFee the balanceRepossessionFee to set
	 */
	public void setBalanceRepossessionFee(BalanceAmountDTO balanceRepossessionFee) {
		this.balanceRepossessionFee = balanceRepossessionFee;
	}

	/**
	 * @return the balanceCollectionFee
	 */
	public BalanceAmountDTO getBalanceCollectionFee() {
		return balanceCollectionFee;
	}

	/**
	 * @param balanceCollectionFee the balanceCollectionFee to set
	 */
	public void setBalanceCollectionFee(BalanceAmountDTO balanceCollectionFee) {
		this.balanceCollectionFee = balanceCollectionFee;
	}

	/**
	 * @return the balanceOperationFee
	 */
	public BalanceAmountDTO getBalanceOperationFee() {
		return balanceOperationFee;
	}

	/**
	 * @param balanceOperationFee the balanceOperationFee to set
	 */
	public void setBalanceOperationFee(BalanceAmountDTO balanceOperationFee) {
		this.balanceOperationFee = balanceOperationFee;
	}

	/**
	 * @return the balancePressingFee
	 */
	public BalanceAmountDTO getBalancePressingFee() {
		return balancePressingFee;
	}

	/**
	 * @param balancePressingFee the balancePressingFee to set
	 */
	public void setBalancePressingFee(BalanceAmountDTO balancePressingFee) {
		this.balancePressingFee = balancePressingFee;
	}

	/**
	 * @return the balanceTransferFee
	 */
	public BalanceAmountDTO getBalanceTransferFee() {
		return balanceTransferFee;
	}

	/**
	 * @param balanceTransferFee the balanceTransferFee to set
	 */
	public void setBalanceTransferFee(BalanceAmountDTO balanceTransferFee) {
		this.balanceTransferFee = balanceTransferFee;
	}

	/**
	 * @return the discountInterest
	 */
	public BalanceAmountDTO getDiscountInterest() {
		return discountInterest;
	}

	/**
	 * @param discountInterest the discountInterest to set
	 */
	public void setDiscountInterest(BalanceAmountDTO discountInterest) {
		this.discountInterest = discountInterest;
	}
}
