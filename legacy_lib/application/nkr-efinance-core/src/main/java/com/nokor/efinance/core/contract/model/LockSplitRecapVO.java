package com.nokor.efinance.core.contract.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitRecapVO implements Serializable {

	/** */
	private static final long serialVersionUID = 9010123482788345142L;

	private String desc;
	private Integer nbInstallment;
	private double amountToPay;
	private double inLockSplitAmount;
	private List<LockSplitRecapVO> subLockSplitRecap;
	
	/**
	 * 
	 * @param desc
	 */
	public LockSplitRecapVO(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the nbInstallment
	 */
	public Integer getNbInstallment() {
		return nbInstallment;
	}

	/**
	 * @param nbInstallment the nbInstallment to set
	 */
	public void setNbInstallment(Integer nbInstallment) {
		this.nbInstallment = nbInstallment;
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
	 * 
	 * @param amountToPay
	 */
	public void addAmountToPay(double amountToPay) {
		this.amountToPay += amountToPay;
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
	 * @return the subLockSplitRecap
	 */
	public List<LockSplitRecapVO> getSubLockSplitRecap() {
		if (subLockSplitRecap == null) {
			subLockSplitRecap = new ArrayList<LockSplitRecapVO>();
		}
		return subLockSplitRecap;
	}

	/**
	 * @param subLockSplitRecap
	 *            the subLockSplitRecap to set
	 */
	public void setSubLockSplitRecap(List<LockSplitRecapVO> subLockSplitRecap) {
		this.subLockSplitRecap = subLockSplitRecap;
	}
	
	/**
	 * 
	 * @param lockSplitRecapVO
	 */
	public void addSubLockSplitRecap(LockSplitRecapVO lockSplitRecapVO) {
		getSubLockSplitRecap().add(lockSplitRecapVO);
	}
	
	/**
	 * 
	 * @param nbInstallment
	 * @return
	 */
	public LockSplitRecapVO getLockSplitRecapByNbInstallment(Integer nbInstallment) {
		for (LockSplitRecapVO lockSplitRecapVO : getSubLockSplitRecap()) {
			if (lockSplitRecapVO.getNbInstallment() == nbInstallment) {
				return lockSplitRecapVO;
			}
		}
		return null;
	}

}