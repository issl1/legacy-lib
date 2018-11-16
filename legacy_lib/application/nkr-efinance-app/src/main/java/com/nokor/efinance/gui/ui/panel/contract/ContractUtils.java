package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.workflow.ContractWkfStatus;

/**
 * @author youhort.ly
 *
 */
public class ContractUtils {

	/**
	 * @param contract
	 * @return
	 */
	public static boolean isBeforeActive(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.PEN)
				|| contractWkfStatus.equals(ContractWkfStatus.BLOCKED);
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public static boolean isPending(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.PEN);
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public static boolean isHoldPayment(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.HOLD_PAY);
	}

	/**
	 * @param contract
	 * @return
	 */
	public static boolean isActivated(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.FIN);
	}
	

	/**
	 * @param contract
	 * @return
	 */
	public static boolean isTerminated(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.WRI);
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public static boolean isCancelled(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
		return contractWkfStatus.equals(ContractWkfStatus.CAN);
	}
}
