package com.nokor.efinance.core.payment.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.collection.model.ELockSplitGroup;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.core.contract.model.LockSplitTypeBalanceVO;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.ersys.finance.accounting.model.JournalEvent;


/**
 * Lock Split service
 * @author youhort.ly
 */
public interface LockSplitService extends BaseEntityService {
	
	List<ELockSplitType> getLockSplitTypes();
	
	/**
	 * Get list lock split types by group
	 * @param group
	 * @return
	 */
	List<ELockSplitType> getLockSplitTypesByGroup(ELockSplitGroup group);
	
	/**
	 * Get list lock split by contract && not equal current lock split id
	 * @param conId
	 * @param currentLckId
	 * @return
	 */
	List<LockSplit> getLockSplitsByContract(Long conId, Long currentLckId);
	
	/**
	 * 
	 * @param contRef
	 * @param wkfStatuses
	 * @return
	 */
	List<LockSplit> getLockSplits(String contRef, List<EWkfStatus> wkfStatuses);
	
	List<LockSplit> getLockSplitByContract(Long conId);
	
	/**
	 * Get list lock split items by receipt code
	 * @param lockSplits
	 * @param receiptCode
	 * @return
	 */
	List<LockSplitItem> getLockSplitItemsByType(List<LockSplit> lockSplits, String receiptCode);
	
	/**
	 * Get list lock split items by lock split id
	 * @param lckId
	 * @return
	 */
	List<LockSplitItem> getLockSplitItemByLockSplit(Long lckId);
	
	LockSplit saveLockSplit(LockSplit lockSplit);
	LockSplit updateLockSplit(LockSplit oldLockSplit, LockSplit newLockSplit);
	
	void validate(LockSplit lockSplit);
	
	/**
	 * Delete a lock split
	 * @param lockSplit
	 */
	void deleteLockSplit(LockSplit lockSplit);
	
	/**
	 * Delete a lock split item 
	 * @param lockSplit
	 * @param lockSplitItem
	 */
	void deleteLockSplitItem(LockSplit lockSplit, LockSplitItem lockSplitItem);
	
	/**
	 * 
	 * @param conId
	 * @param currentLckSplit
	 * @return
	 */
	List<LockSplitRecapVO> getLockSplitRecapVOs(Long conId, LockSplit currentLckSplit);
	
	/**
	 * 
	 * @param lockSplitItems
	 * @return
	 */
	String getLockSplitTypeCode(List<LockSplitItem> lockSplitItems);
	
	
	LockSplitItem getLockSplitItemByLockSplit(Long lockSplitId, Long lockSplitItemId);
	
	/**
	 * 
	 * @param lockSplit
	 * @param category
	 * @return
	 */
	Map<String, List<LockSplitItem>> getLockSplitItemsByCategory(LockSplit lockSplit, ELockSplitCategory category);
	
	/**
	 * 
	 * @return
	 */
	List<LockSplit> getLockSplitsByAfterSaleEventType(Contract contract, EAfterSaleEventType afterSaleEventType);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	LockSplit getLockSplitByContract(Contract contract);
	
	/**
	 * 
	 * @param contract
	 */
	void removeLockSplitByContract(Contract contract);
	
	/**
	 * 
	 * @return
	 */
	void updateLockSplitsToExpired();
	
	/**
	 * 
	 */
	void updatePromisesToExpired();
	
	/**
	 * 
	 * @param contractID
	 * @param installmentDate
	 * @return
	 */
	List<LockSplitTypeBalanceVO> getLockSplitTypeBalanceVOs(String contractID, Date installmentDate);
	
	/**
	 * 
	 * @param newLockSplit
	 * @param lockSplit
	 * @return
	 */
	LockSplit copyLockSplit(LockSplit newLockSplit, LockSplit lockSplit);
	
	/**
	 * 
	 * @return
	 */
	LockSplitItem copyLocksplitItem(LockSplitItem newLocksplitItem, LockSplitItem lockSplitItem);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	FinService getServicebyType(EServiceType type);
	
	/**
	 * 
	 * @param journalEvent
	 * @return
	 */
	LockSplitItem getLockSplitItemByJournalEvent(LockSplit lockSplit, JournalEvent journalEvent);
}

