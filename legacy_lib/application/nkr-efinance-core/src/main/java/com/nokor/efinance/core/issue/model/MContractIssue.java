package com.nokor.efinance.core.issue.model;

import org.seuksa.frmk.model.entity.MEntityA;


/**
 * Meta data of com.nokor.efinance.core.contract.model.ContractIssue
 * @author uhout.cheng
 */
public interface MContractIssue extends MEntityA {
	
	// For Vaadin Grid
	public final static String ACTION = "action";
	
	public final static String CONTRACT = "contract";
	public final static String ISSUETYPE = "issueType";
	public final static String ISSUEATTRIBUTE = "issueAttribute";
	public final static String ISSUEDOCUMENT1 = "issueDocument1";
	public final static String ISSUEDOCUMENT2 = "issueDocument2";
	public final static String COMMENT = "comment";
	public final static String REMARK = "remark";
	public final static String DATEFIXED = "dateFixed";
	public final static String FIXED = "fixed";

}
