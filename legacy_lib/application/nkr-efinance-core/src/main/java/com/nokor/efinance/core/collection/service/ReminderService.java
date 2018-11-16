package com.nokor.efinance.core.collection.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Comment;

/**
 * Reminder service interface
 * @author uhout.cheng
 */
public interface ReminderService extends BaseEntityService {
	
	/**
	 * saveOrUpdate Reminder
	 * @param reminder
	 */
	void saveOrUpdateReminder(Reminder reminder);
	
	/**
	 * saveOrUpdate Comment
	 * @param comment
	 */
	void saveOrUpdateComment(Comment comment);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	List<Reminder> getReminderByContract(Contract contract);
}
