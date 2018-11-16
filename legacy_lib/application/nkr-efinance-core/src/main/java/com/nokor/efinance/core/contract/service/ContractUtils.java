
package com.nokor.efinance.core.contract.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.ContractSimulationWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;

/**
 * @author youhort.ly
 *
 */
public class ContractUtils implements FinServicesHelper {

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
	public static boolean isSubStatusBeforeActive(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfSubStatus(); 
		return contractWkfStatus != null && (contractWkfStatus.equals(ContractWkfStatus.PEN_TRAN)
				|| contractWkfStatus.equals(ContractWkfStatus.BLOCKED_TRAN));
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
	public static boolean isSubStatusPendingTransfer(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfSubStatus(); 
		return contractWkfStatus != null && contractWkfStatus.equals(ContractWkfStatus.PEN_TRAN);
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
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static boolean isPendingTransfer(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfSubStatus();
		return contract.isTransfered() && ContractWkfStatus.PEN_TRAN.equals(contractWkfStatus);
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static boolean isTranfered(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfSubStatus();
		return contract.isTransfered() && contractWkfStatus == null && contract.getNbPrints() == null;
	}
	
	/**
	 * 
	 * @param conId
	 * @return
	 */
	public static ContractSimulation getLastContractSimulation(Long conId) {
		ContractSimulationRestriction restrictions = new ContractSimulationRestriction();
		restrictions.setConId(conId);
		restrictions.setAfterSaleEventType(EAfterSaleEventType.TRANSFER_APPLICANT);
		restrictions.getWkfStatusList().add(ContractSimulationWkfStatus.SIMULATED);
		
		ContractSimulation contractSimulation = null;
		List<ContractSimulation> contractSimulations = ENTITY_SRV.list(restrictions);
		if (contractSimulations != null && !contractSimulations.isEmpty()) {
			contractSimulation = contractSimulations.get(0);
		}
		return contractSimulation;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static String getApplicationID(Contract contract) {
		String applicationID = StringUtils.EMPTY;
		if (isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				applicationID = contractSimulation.getExternalReference();
			}
		} else {
			applicationID = contract.getExternalReference();
		}
		return applicationID;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static String getApplicationName(Contract contract) {
		String applicationName = StringUtils.EMPTY;
		if (isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				applicationName = contractSimulation.getApplicant() == null ? StringUtils.EMPTY : 
					contractSimulation.getApplicant().getNameLocale();
			}
		} else {
			applicationName = contract.getApplicant() == null ? StringUtils.EMPTY : contract.getApplicant().getNameLocale();
		}
		return applicationName;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static Date getApplicationDate(Contract contract) {
		Date applicationDate = null;
		if (isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				applicationDate = contractSimulation.getApplicationDate();
			}
		} else {
			applicationDate = contract.getQuotationDate();
		}
		return applicationDate;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public static Date getApprovalDate(Contract contract) {
		Date approvalDate = null;
		if (isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				approvalDate = contractSimulation.getApprovalDate();
			}
		} else {
			approvalDate = contract.getApprovalDate();
		}
		return approvalDate;
	}
}
