package com.nokor.efinance.core.collection.model;

import org.seuksa.frmk.model.entity.MEntityA;


/**
 * Meta data of com.nokor.efinance.core.collection.model.ContractOperation
 * @author uhout.cheng
 */
public interface MContractOperation extends MEntityA {
	// For Vaadin Grid
	public final static String BALANCE = "balance";
	public final static String DEADLINE = "deadline";
	public final static String ACTIONS = "actions";
	
	public final static String CONTRACT = "contract";
	public final static String OPERATIONTYPE = "operationType";
	public final static String TIPRICE = "tiPrice";

}
