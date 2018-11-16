package com.nokor.efinance.core.contract.service;

import java.util.List;
import java.util.Map;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.ContractDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * User Inbox Service
 * @author youhort.ly
 */
public interface UserInboxService extends BaseEntityService {
	
	/**
	 * @param user
	 * @param contracts
	 * @param type
	 */
	void addContractsToInbox(SecUser user, List<Contract> contracts, EColType type);
	
	/**
	 * @param secUser
	 * @param contractDetails
	 * @param type
	 */
	void addContractDetailsToInbox(SecUser secUser, List<ContractDetail> contractDetails, EColType type);
	
	/**
	 * 
	 * @param user
	 * @param contracts
	 * @param secProfile
	 */
	void addContractsToInbox(SecUser user, List<Contract> contracts, SecProfile secProfile);
	
	/**
	 * @param mapStaffContract
	 */
	void addContractsToInbox(Map<Long, List<Long>> mapStaffContracts, EColType type);

	/**
	 * @param usrId
	 * @param conIds
	 */
	void addContractsToInbox(Long usrId, List<Long> conIds, EColType type);
	
	/**
	 * @param contract
	 * @param secUser
	 */
	void addContractToInbox(SecUser secUser, Contract contract);
	
	/**
	 * @param contract
	 * @param secUser
	 */
	void addContractToInbox(Contract contract);
	
	
	/**
	 * Delete contract from inbox user
	 * @param contract
	 * @param secUser
	 */
	void deleteContractFromInbox(SecUser secUser, Contract contract);

	/**
	 * Delete contract from inbox user
	 * @param contract
	 */
	public void deleteContractFromInbox(Contract contract);
	
	/**
	 * @param contract
	 */
	void deleteContractFromCmStaffInbox(Contract contract);
	
	/**
	 * @param secUser
	 * @param contract
	 */
	public void deleteContractSimulFromInbox(Contract contract, EColType type);
	
	/**
	 * Delete contract simulation
	 * @param colType
	 */
	void deleteContractSimulByType(EColType colType);
	
	/**
	 * Delete contract simulation
	 * @param colType
	 */
	void deleteContractSimulByDebtLevels(Integer[] debtLevels);
	
	/**
	 * Delete contract simulation by profile code
	 * @param profileCode
	 */
	void deleteContractSimulByProfileCode(String profileCode);
	
	/**
	 * 
	 * @param secUser
	 * @param contract
	 * @param profileCode
	 */
	void addContractToSimulInbox(SecUser secUser, Contract contract);
	
	/**
	 * @param usrId
	 * @return
	 */
	List<ContractUserInbox> getContractUserInboxByUser(Long usrId);
	
	/**
	 * @param conId
	 * @return
	 */
	List<ContractUserInbox> getContractUserInboxByContract(Long conId);
	
	/**
	 * @param conId
	 * @return
	 */
	boolean isContractAssigned(Long conId);
	
	/**
	 * @param conId
	 * @param usrId
	 * @return
	 */
	boolean isContractAssignedToUser(Long conId, Long usrId);
	
	/**
	 * @param usrId
	 * @return
	 */
	List<ContractUserSimulInbox> getContractUserSimulInboxByUser(Long usrId);
	
	/**
	 * @param colType
	 * @return
	 */
	List<ContractUserSimulInbox> getContractUserSimulInboxByTeam(EColType colType);
	
	/**
	 * @param contract
	 */
	void receivedContract(Contract contract);
	
	/**
	 * @param secUser
	 * @param contract
	 */
	void bookContract(SecUser secUser, Contract contract);
	
	/**
	 * @param secUser
	 * @param contract
	 */
	void unbookContract(SecUser secUser, Contract contract);
	
	/**
	 * Get number of current contract in inbox by user
	 * @return
	 */
	Long countCurrentContractByUser(SecUser user);
	
	/**
	 * Count Current Odm Contract By User
	 * @param user
	 * @param odm
	 * @return
	 */
	Long countCurrentOdmContractByUser(SecUser user, int debtLevel);
	
	/**
	 * Count current user by debt level
	 * @param debtLevel
	 * @return
	 */
	Long countCurrentUserByDebtLevel(int debtLevel);
	
	/**
	 * Get number of current simul contract by user
	 * @param user
	 * @return
	 */
	Long countCurrentSimulContractByUser(SecUser user);
	
	/**
	 * @param fromUser
	 * @param toUsers
	 */
	void transferUserInbox(SecUser fromUser, Map<SecUser, Integer> toUsers);
	
	/***
	 * 
	 * @param profileCode
	 * @return
	 */
	List<ContractUserInbox> getContractUserInboxByProCode(String profileCode);
	
	/**
	 * 
	 * @param contraId
	 * @param profileCodes
	 * @return
	 */
	ContractUserInbox getContractUserInboxed(Long contraId, String[] profileCodes);
	
}
