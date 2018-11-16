package com.nokor.efinance.core.workflow;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * Applicant Status
 * @author bunlong.taing
 */
public class ApplicantWkfStatus {
	
	public final static EWkfStatus ACTIVE = EWkfStatus.getById(900);
	public final static EWkfStatus INACTIVE = EWkfStatus.getById(901);
	public final static EWkfStatus DEAD = EWkfStatus.getById(902);
	
	/**
	 * @return
	 */
	public static List<EWkfStatus> values() {
		List<EWkfStatus> values = new ArrayList<EWkfStatus>();
		values.add(ACTIVE);
		values.add(INACTIVE);
		values.add(DEAD);
		return values;
	}

}
