package com.nokor.efinance.core.callcenter.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.callcenter.model.CallCenterHistory
 * @author uhout.cheng
 */
public interface MCallCenterHistory extends MEntityA {
	
	// For Vaadin Grid
	public final static String ACTION = "action";
	
	public final static String CONTRACT = "contract";
	public final static String RESULT = "result";
	public final static String COMMENT = "comment";

}
