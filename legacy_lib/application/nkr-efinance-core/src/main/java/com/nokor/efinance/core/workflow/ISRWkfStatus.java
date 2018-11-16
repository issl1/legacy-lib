package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * 
 * @author buntha.chea
 *
 */
public class ISRWkfStatus {
	
	public final static EWkfStatus ISRPEN = EWkfStatus.getById(1200); // Pending
	public final static EWkfStatus ISRCONF = EWkfStatus.getById(1201); // Confirm
}
