
package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Auction status
 * 
 * @author ly.youhort
 *
 */
public class AuctionWkfStatus {
	public final static EWkfStatus EVA = new EWkfStatus("EVA", 401); // evaluation
	public final static EWkfStatus VAL = new EWkfStatus("VAL", 402); // manager.validation
	public final static EWkfStatus WRE = new EWkfStatus("WRE", 403); // waiting.for.result
	public final static EWkfStatus SOL = new EWkfStatus("SOL", 404); // sold
	public final static EWkfStatus CNS = new EWkfStatus("CNS", 405); // cannot.sell
	
}