package com.nokor.efinance.core.collection.model;

import com.nokor.common.app.workflow.model.MEWkfFlow;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public interface MCollectionLetter extends MEWkfFlow {
	
	public final static String NAME = "name";
	public final static String DESC_EN = "desc";
	public final static String SEND_TO = "send.to";
	public final static String SEND_DATE = "send.date";
	public final static String ADDRESS = "address";
	public final static String SEND_ON = "send.on";
	public final static String RESULT = "result";
	public final static String RESULT_DATE = "result.date";
	public final static String COMMENT = "comment";
	public static final String STATUS_DATE = "statusDate";
	public static final String DETAILS = "details";

}
