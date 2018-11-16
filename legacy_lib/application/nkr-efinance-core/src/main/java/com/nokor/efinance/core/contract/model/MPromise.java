package com.nokor.efinance.core.contract.model;

import org.seuksa.frmk.model.entity.MEntityA;


/**
 * Meta data of com.nokor.efinance.core.contract.model.Promise
 * @author uhout.cheng
 */
public interface MPromise extends MEntityA {
	
	// For Vaadin Grid
	public static final String OPTIONS = "options";
	
	public final static String CONTRACT = "contract";
	public final static String PROMISESTATUS = "promiseStatus";
	public final static String PROMISETYPE = "promiseType";
	public final static String PROMISEAMOUNT = "promiseAmount";
	public final static String PROMISEDATE = "promiseDate";
	public final static String REMARK = "remark";
	public final static String CREATEDBY = "createdBy";

}
