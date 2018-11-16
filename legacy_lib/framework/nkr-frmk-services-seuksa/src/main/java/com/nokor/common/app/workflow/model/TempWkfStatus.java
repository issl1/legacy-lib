package com.nokor.common.app.workflow.model;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * 
 * @author phirun.kong
 *
 */
public class TempWkfStatus {

	public final static EWkfStatus AMENDED = EWkfStatus.getById(1100); // amended
	public final static EWkfStatus AMENDED_SUBMI = EWkfStatus.getById(1101); // submitted
	public final static EWkfStatus AMENDED_REFUS = EWkfStatus.getById(1102); // rejected
	public final static EWkfStatus AMENDED_VALID = EWkfStatus.getById(1103); // validated
	
}