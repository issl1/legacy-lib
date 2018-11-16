package com.nokor.efinance.core.collection.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.collection.model.CollectionHistory
 * @author uhout.cheng
 */
public interface MCollectionHistory extends MEntityA {

	// For Vaadin Grid
	public final static String DEPARTMENT = "department";
	public final static String CHANNEL = "channel";
	public final static String NOADDMAIL = "no.add.mail";
	
	public final static String CONTRACT = "contract";
	public final static String RESULT = "result";
	public final static String COMMENT = "comment";
	public final static String ORIGIN = "origin";
	public final static String CONTACTWITH = "reachedPerson";
	public final static String CALLTYPE = "callType";
	public final static String ANSWER = "result";
	public final static String SUBJECT = "subject";
	public final static String CATEGORY = "category";
	
}
