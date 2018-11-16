package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfStatus;

public class ReturnWkfStatus {
	public final static EWkfStatus REPEN = EWkfStatus.getById(1400); // Pending
	public final static EWkfStatus RECLO = EWkfStatus.getById(1401); // close
}
