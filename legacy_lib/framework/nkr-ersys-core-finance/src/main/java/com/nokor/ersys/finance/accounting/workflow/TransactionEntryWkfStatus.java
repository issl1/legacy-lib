package com.nokor.ersys.finance.accounting.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * 
 * @author prasnar
 *
 */
public class TransactionEntryWkfStatus {

	public final static EWkfStatus DRAFT = EWkfStatus.getById(1010); // draft
	public final static EWkfStatus REJECTED = EWkfStatus.getById(1011); // rejected
	public final static EWkfStatus POSTED = EWkfStatus.getById(1012); // posted
	public final static EWkfStatus APPROVED = EWkfStatus.getById(1013); // approved
	
	/**
	 * @return
	 */
	public static List<EWkfStatus> values() {
		List<EWkfStatus> values = new ArrayList<>();
		values.add(DRAFT);
		values.add(REJECTED);
		values.add(APPROVED);
		values.add(POSTED);
		return values;
	}
	
}