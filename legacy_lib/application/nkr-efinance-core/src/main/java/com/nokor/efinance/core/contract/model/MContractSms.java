package com.nokor.efinance.core.contract.model;

import org.seuksa.frmk.model.entity.MEntityA;


/**
 * Meta data of com.nokor.efinance.core.contract.model.ContractSms
 * @author bunlong.taing
 */
public interface MContractSms extends MEntityA {
	
	// For Vaadin Grid
	public final static String STATUS = "status";
	public final static String ACTION = "action";
	
	public final static String CONTRACT = "contract";
	public final static String SENDTO = "sendTo";
	public final static String PHONENUMBER = "phoneNumber";
	public final static String USER = "user";
	public final static String MESSAGE = "message";

}
