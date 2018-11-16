package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * Payment status
 * 
 * @author ly.youhort
 *
 */
public class PaymentWkfStatus {

	public final static EWkfStatus NAL = EWkfStatus.getById(2201); // not.allocated
	public final static EWkfStatus RVA = EWkfStatus.getById(2202); // request.validation
	public final static EWkfStatus VAL = EWkfStatus.getById(2203); // validation
	public final static EWkfStatus PAI = EWkfStatus.getById(2204); // paid
	public final static EWkfStatus CAN = EWkfStatus.getById(2205); // cancelled
	public final static EWkfStatus UNP = EWkfStatus.getById(2206); // unpaid
	public final static EWkfStatus PAL = EWkfStatus.getById(2207); // partially.allocated
	public final static EWkfStatus ERR = EWkfStatus.getById(2208); // Error
	
}