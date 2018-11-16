package com.nokor.ersys.finance.accounting.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * 
 * @author prasnar
 *
 */
public class JournalEntryWkfStatus {

	public final static EWkfStatus NEW = EWkfStatus.getById(1001); // not.allocated
	public final static EWkfStatus VALIDATED = EWkfStatus.getById(1002); // validation
	public final static EWkfStatus CANCELLED = EWkfStatus.getById(1003); // cancelled
	public final static EWkfStatus POSTED = EWkfStatus.getById(1004); // posted
	
	/**
	 * @return
	 */
	public static List<EWkfStatus> values() {
		List<EWkfStatus> values = new ArrayList<EWkfStatus>();
		values.add(NEW);
		values.add(VALIDATED);
		values.add(CANCELLED);
		values.add(POSTED);
		return values;
	}
	
}