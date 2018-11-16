package com.nokor.efinance.core.collection.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.security.model.SecUser;

/**
 * Contract Other data service interface
 * @author youhort.ly
 */
public interface CollectionService extends BaseEntityService {

	List<SecUser> getCollectionUsers(String[] proCode);
	
	void reassignPhoneContracts();
	
	void reassignFieldContracts();
	
	void reassignInsideRepoContracts();
	
	PhoneSummary getPhoneSummaries();
	
	FieldSummary getFieldSummaries();
	
	void assignDayEndContracts(Date processDate);
	
	void assignPhoneContracts(Date processDate);
	
	void assignFieldContracts();
	
	void assignInsideRepoContracts();
	
	void assignOAContracts();
	
	List<Contract> getRequestAssistContracts();
	
	List<Contract> getRejectAssistContracts();

	List<Contract> getRequestFlagContracts();
	
	List<Contract> getAssistContractStatusApporve(EColType assistTeam);
	
	List<Contract> getFlagContractStatusApprove(EColType flagTeam);
	
	/**
	 * Get collection contracts by next action date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Contract> getCollectionContractsByNextActionDate(Date startDate, Date endDate);
	
	/**
	 * 
	 * @return
	 */
	List<Contract> getCollectionContractsUnProcessed();
	
	/**
	 * Get collection histories by contract id
	 * @param conId
	 * @return
	 */
	List<CollectionHistory> getCollectionHistoriesByContractId(Long conId);
	
	/**
	 * Get collection actions by contract id
	 * @param conId
	 * @return
	 */
	List<CollectionAction> getCollectionActionsByContractId(Long conId);
	
	/**
	 * Get list of collection contract by user
	 * @return
	 */
	List<Contract> getCollectionContractsByUser();
	
	void requestFlagRequest(Long conId, String reason);
	
	void assignFlagRequest(Long conId);
	
	/**
	 * 
	 * @param conIds
	 * @param flagTeam
	 */
	void approveFlagRequest(List<Long> conIds, EColType flagTeam);
	
	void rejectFlagRequest(Long conId);
	
	void requestAssistRequest(Long conId, String reason);
	
	void assignAssistRequest(Long conId);
	
	/**
	 * 
	 * @param conIds
	 * @param assistTeam
	 */
	void approveAssistRequest(List<Long> conIds, EColType assistTeam);
	
	void rejectAssistRequest(Long conId);
	
	/**
	 * saveOrUpdate Latest Collection History
	 * @param colHistory
	 */
	void saveOrUpdateLatestColHistory(CollectionHistory colHistory);
	
	/**
	 * Delete latest collection history
	 * @param colHistroy
	 */
	void deleteLatestColHistory(CollectionHistory colHistroy);
	
	/**
	 * saveOrUpdate Latest Collection Action
	 * @param colAction
	 */
	void saveOrUpdateLatestColAction(CollectionAction colAction);
	
	/**
	 * Delete latest collection action
	 * @param action
	 */
	void deleteLatestColAction(CollectionAction action);
	
	List<SecUserDeptLevel> getSecUserDeptLevel(Long usrId);
	
	/**
	 * Unassign the assigned contracts
	 * @param contractIds
	 */
	void unassignPhoneContracts(List<Long> contractIds);
	
	/**
	 * Unassign the assigned contracts
	 * @param contractIds
	 */
	void unassignFieldContracts(List<Long> contractIds);
	
	/**
	 * Unassign the assigned contract
	 * @param contract
	 */
	void unassignPhoneContract(Contract contract);
	
	/**
	 * Unassign the assigned contracts
	 * @param contractIds
	 */
	void unassignFieldContract(Contract contract);
	
	/**
	 * 
	 * @return
	 */
	ContractRestriction getContractRestrictionByUser();
	/**
	 * 
	 * @return
	 */
	List<ContractUserSimulInbox> getUnmatchedFieldContracts();
	/**
	 * 
	 * @return
	 */
	List<ContractUserSimulInbox> getUnmatchedInsideRepoContracts();
	
	/**
	 * 
	 * @return
	 */
	List<ContractUserSimulInbox> getUnmatchedOAContracts();
	
	/**
	 * 
	 * @param contractsUserSimulInbox
	 */
	void assingUnmatchedFieldContracts(List<ContractUserSimulInbox> contractsUserSimulInbox);
	/**
	 * 
	 * @param contractIds
	 */
	void unassignInsideRepoContracts(List<Long> contractIds);
	/**
	 * 
	 * @param contract
	 */
	void unassignInsideRepoContract(Contract contract);
	
	/**
	 * 
	 * @param contractIds
	 */
	void unassignOAContracts(List<Long> contractIds);
	
	/**
	 * 
	 * @param contract
	 */
	void unassignOAContract(Contract contract);
	
	/**
	 * 
	 * @param contra
	 */
	void validateAssistFlagContract(Contract contra);
	
	/**
	 * @param comp
	 * @return
	 */
	Organization createOrganizationWithDefaultUser(Organization comp);
	
	/**
	 * @param conId
	 * @return
	 */
	SecUser getCollectionUser(Long conId);
	
	/**
	 * Get contract Assign
	 * @return
	 */
	List<Contract> getContractAssigned();
	
	/**
	 * 
	 * @return
	 */
	List<CollectionHistory> getCalledContract(List<Contract> contracts);
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	List<CollectionAction> getCollectionActionFollowup(List<Contract> contracts);
	
	/**
	 * 
	 * @return
	 */
	List<Collection> getContractAssignPendding(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<Collection> getContractFlagPendding(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<Collection> getContractAssistReject(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<Collection> getContractFlagReject(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<ContractFlag> getContractReturnRepoPendding(List<Contract> contracts);
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	List<ContractFlag> getContractReturnRepoAlready(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<Contract> getContractWithPromise();
	/**
	 * 
	 * @return
	 */
	List<Contract> getContractNoOverdue();
	
	/**
	 * 
	 * @return
	 */
	List<Collection> getCollectionAssistValidated(List<Contract> contracts);
	/**
	 * 
	 * @return
	 */
	List<Collection> getCollectionFlagValidated(List<Contract> contracts);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	Collection getCollection(Contract contract);
}
