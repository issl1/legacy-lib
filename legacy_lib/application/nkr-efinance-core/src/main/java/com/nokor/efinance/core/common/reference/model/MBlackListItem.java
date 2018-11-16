package com.nokor.efinance.core.common.reference.model;

import com.nokor.ersys.core.hr.model.organization.MBasePerson;

/**
 * Meta data of com.nokor.efinance.core.contract.model.BlackList
 * @author uhout.cheng
 */
public interface MBlackListItem extends MBasePerson {

	public final static String ID = "id";
	public final static String APPLICANTCATEGORY = "applicantCategory";
	public final static String SOURCE = "source";
	public final static String REASON = "reason";
	
	public final static String DETAILS = "details";
	public final static String REMARKS = "remarks";

}
