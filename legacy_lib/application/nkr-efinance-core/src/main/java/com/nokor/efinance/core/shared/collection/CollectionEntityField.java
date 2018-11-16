package com.nokor.efinance.core.shared.collection;

import com.nokor.efinance.core.shared.FMEntityField;

/**
 * 
 * @author uhout.cheng
 */
public interface CollectionEntityField extends FMEntityField {

	String ASSIGNEE = "assignee";
	
	//EColTeamGroup
	String TEAM = "team";
	String GROUP = "group";
	String TEAM_LEADER = "teamLeader";
	String DEBT_LEVEL = "deptLevel";
	String REMARK = "remark";
	//ColResult
	String COLLECTION_TYPE = "colTypes";
	//ColArea
	String STAFFS = "staffs";
	//ColWeight
	String STAFF = "staff";
	String DISTRIBUTION_RATE = "distributionRate";
	//SMSTemplate
	String DAY_PASS_DUE = "dayPassDue";
	
	// Lock Split Rule
	String PRIORITY = "priority";
	String LOCK_SPLIT_TYPE = "lockSplitType";
	String DEFAULT = "default";
	
	String LASTACTION = "lastAction";
}
