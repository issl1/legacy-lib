package com.nokor.efinance.core.callcenter;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.frmk.security.model.SecUser;

/**
 * Call Center service interface
 * @author youhort.ly
 */
public interface CallCenterService extends BaseEntityService {

	/**
	 * @param processDate
	 */
	void assignContracts(Date processDate);
	
	/**
	 * 
	 */
	void reassignContracts();
	
	/**
	 * @param contractIds
	 */
	void unassignContracts(List<Long> contractIds);
	
	/**
	 * Get last call center history by contract id
	 * @param conId
	 * @return
	 */
	CallCenterHistory getLastCallCenterHistory(Long conId);
	
	/**
	 * Get call center histories by contract id 
	 * @param conId
	 * @return
	 */
	List<CallCenterHistory> getCallCenterHistories(Long conId);
	
	/**
	 * Get staffs by call center profile
	 * @return
	 */
	List<SecUser> getStaffsByCallCenterProfile();
	
	/**
	 * 
	 * @param secUser
	 * @param callCenterHistory
	 */
	void saveOrUpdateCallCenterHistory(SecUser secUser, CallCenterHistory callCenterHistory);
}
