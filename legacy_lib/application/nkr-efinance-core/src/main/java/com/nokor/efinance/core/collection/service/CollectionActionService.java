package com.nokor.efinance.core.collection.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.contract.model.Contract;
/**
 * 
 * @author buntha.chea
 *
 */
public interface CollectionActionService extends BaseEntityService {
	
	/**
	 * 
	 * @return
	 */
	List<CollectionAction> getCollectionActionOfWeek();
	
	/**
	 * 
	 * @return
	 */
	List<CollectionAction> getCollectionActionOfMonth();
	
	/**
	 * Get collection actions unprocessed
	 * @param conId
	 * @return
	 */
	List<CollectionAction> getCollectionActionsUnProcessed(Long conId);
	
	/**
	 * Get collection actions by contract id & next action date
	 * @param startDate
	 * @param endDate
	 * @param conId
	 * @return
	 */
	List<CollectionAction> getCollectionActionsByNextActionDate(Date startDate, Date endDate, Long conId);
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	boolean isNextActionDateInThisWeek(CollectionAction collectionAction);
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	boolean isNextActionDateInNextWeek(CollectionAction collectionAction);
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	boolean isNextActionDateInWeekAfter(CollectionAction collectionAction);
	
	/**
	 * 
	 * @param nextActionDate
	 * @return
	 */
	String getNameOfDay(Date nextActionDate);
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	int countCollectionAction(EColAction action, Date date);
	/**
	 * 
	 * @return
	 */
	List<EColAction> getCollectionActionInCollection();
	
	/**
	 * 
	 * @return
	 */
	List<Contract> getContractsAssigned();

	/**
	 * 
	 * 
	 * @return
	 */
	int countNoAction();
}
