package com.nokor.efinance.share.locksplit;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitRecapDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 8592559585094174715L;

	private String desc;
	private double amountToPay;
	private double inLockSplitAmount;
	private List<LockSplitRecapDTO> subLockSplitRecapDTOs;

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the amountToPay
	 */
	public double getAmountToPay() {
		return amountToPay;
	}

	/**
	 * @param amountToPay
	 *            the amountToPay to set
	 */
	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}

	/**
	 * @return the inLockSplitAmount
	 */
	public double getInLockSplitAmount() {
		return inLockSplitAmount;
	}

	/**
	 * @param inLockSplitAmount
	 *            the inLockSplitAmount to set
	 */
	public void setInLockSplitAmount(double inLockSplitAmount) {
		this.inLockSplitAmount = inLockSplitAmount;
	}

	/**
	 * @return the subLockSplitRecapDTOs
	 */
	public List<LockSplitRecapDTO> getSubLockSplitRecapDTOs() {
		return subLockSplitRecapDTOs;
	}

	/**
	 * @param subLockSplitRecapDTOs
	 *            the subLockSplitRecapDTOs to set
	 */
	public void setSubLockSplitRecapDTOs(List<LockSplitRecapDTO> subLockSplitRecapDTOs) {
		this.subLockSplitRecapDTOs = subLockSplitRecapDTOs;
	}

}