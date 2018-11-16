
package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Contract status
 * 
 * @author prasnar
 *
 */
public class ContractSimulationWkfStatus {
	
	public final static EWkfStatus SIMULATED = EWkfStatus.getById(5000);														
	public final static EWkfStatus VALIDATED = EWkfStatus.getById(5001);
	public final static EWkfStatus CANCELLED = EWkfStatus.getById(5002);
}