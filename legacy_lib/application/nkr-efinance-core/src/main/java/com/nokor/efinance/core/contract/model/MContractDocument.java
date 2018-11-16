package com.nokor.efinance.core.contract.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.contract.model.ContractDocument
 * @author uhout.cheng
 */
public interface MContractDocument extends MEntityA {
	
	// For Vaadin Grid
	public final static String DOCUMENTTYPE = "document.type";
	public final static String ACTIONS = "actions";
	public final static String SENTON = "sent.on";
	public final static String ADDRESS = "address";
	public final static String DETAIL = "detail";
	public final static String RESULT = "result";
	public final static String RESULTDATE = "result.date";

	public final static String PATH = "path";
	public final static String REFERENCE = "reference";
	public final static String CONTRACT = "contract";
	public final static String DOCUMENT = "document";
	public final static String ISSUEDATE = "issueDate";
	public final static String EXPIREDATE = "expireDate";
	public final static String COMMENT = "comment";
	public final static String STATUS = "status";

}
