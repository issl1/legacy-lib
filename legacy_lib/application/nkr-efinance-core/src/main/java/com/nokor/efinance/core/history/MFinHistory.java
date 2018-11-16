package com.nokor.efinance.core.history;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.history.FinHistory
 * @author uhout.cheng
 */
public interface MFinHistory extends MEntityA {

	// For Vaadin Grid
	public final static String DATE = "date";
	public final static String TIME = "time";
	public final static String DETAIL = "detail";
	public final static String USER = "user";
	
	public final static String CONTRACT = "contract";
	public final static String TYPE = "type";
	public final static String COMMENT = "comment";
	
	public final static String EVENT = "event";
	
}
