package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * 
 * @author bunlong.taing
 *
 */
public class PaymentFileWkfStatus {
	
	public final static EWkfStatus NEW = EWkfStatus.getById(800);
	public final static EWkfStatus ALLOCATED = EWkfStatus.getById(801);
	public final static EWkfStatus UNIDENTIFIED = EWkfStatus.getById(802);
	public final static EWkfStatus OVER = EWkfStatus.getById(803);
	public final static EWkfStatus PARTIAL_ALLOCATED = EWkfStatus.getById(804);
	public final static EWkfStatus UNMATCHED = EWkfStatus.getById(805);
	public final static EWkfStatus SUSPENDED = EWkfStatus.getById(806);
	public final static EWkfStatus MATCHED = EWkfStatus.getById(807);
	public final static EWkfStatus ERROR = EWkfStatus.getById(808);

}
