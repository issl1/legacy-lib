package com.nokor.efinance.core.organization.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.organization.model.OrgPaymentMethod
 * @author uhout.cheng
 */
public interface MOrgPaymentMethod extends MEntityA {
	
	public final static String ORGANIZATION = "organization";
	public final static String PAYMENTMETHOD = "paymentMethod";
	public final static String TYPE = "type";
	public final static String ORGACCOUNTHOLDER = "orgAccountHolder";
	public final static String ORGBANKACCOUNT = "orgBankAccount";
	
}
