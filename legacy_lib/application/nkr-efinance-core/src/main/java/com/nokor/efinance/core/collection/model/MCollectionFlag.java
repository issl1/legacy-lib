package com.nokor.efinance.core.collection.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.collection.model.CollectionAction
 * @author uhout.cheng
 */
public interface MCollectionFlag extends MEntityA {

	public final static String CONTRACT = "contract";
	public final static String REQUESTSTATUS = "requestStatus";
	public final static String COLTYPE = "colType";
	public final static String COLACTION = "colAction";
	public final static String NEXTACTIONDATE = "nextActionDate";
	public final static String USERLOGIN = "userLogin";
	public final static String REQUESTUSERLOGIN = "requestedUserLogin";
	public final static String APPROVEDUSERLOGIN = "approvedUserLogin";
	public final static String REJECTEDUSERLOGIN = "rejectedUserLogin";
	public final static String REQUESTEDDATE = "requestedDate";
	public final static String APPROVEDDATE = "approvedDate";
	public final static String REJECTEDDATE = "rejectedDate";
	
	static final String MONDAY = "monday";
	static final String TUESDAY = "tuesday";
	static final String WENDNESDAY = "wendnesday";
	static final String THURSDAY = "thursday";
    static final String FRIDAY = "friday";
	static final String SATURDAY = "saturday";
	static final String SUNDAY = "sunday";
	
	static final String THIS_WEEK = "this.week";
	static final String NEXT_WEEK = "next.week";
	static final String THE_WEEK_AFTER = "the.week.after";
	

}
