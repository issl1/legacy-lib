package com.nokor.efinance.core.payment.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.payment.model.PaymentFileItem
 * @author bunlong.taing
 */
public interface MPaymentFileItem extends MEntityA {
	
	// For Vaadin Grid
	public final static String SELECT = "select";
	public final static String TYPE = "type";
	public final static String ACTIONS = "actions";
	
	
	public final static String PAYMENTFILE = "paymentFile";
	
	public final static String SEQUENCE = "sequence";
	public final static String BANKCODE = "bankCode";
	public final static String COMPANYACCOUNT = "companyAccount";
	public final static String PAYMENTDATE = "paymentDate";
	public final static String DEALERNO = "dealerNo";
	public final static String CUSTOMERID = "customerId";
	public final static String CUSTOMERNAME = "customerName";
	public final static String CUSTOMERREF1 = "customerRef1";
	public final static String CUSTOMERREF2 = "customerRef2";
	public final static String CUSTOMERREF3 = "customerRef3";
	public final static String BRANCHNO = "branchNo";
	public final static String TELLERNO = "tellerNo";
	public final static String TRANSACTIONKIND = "transactionKind";
	public final static String TRANSACTIONCODE = "transactionCode";
	public final static String CHEQUENO = "chequeNo";
	public final static String AMOUNT = "amount";
	public final static String CHEQUEBANKCODE = "chequeBankCode";
	public final static String CHEQUEBRANCHCODE = "chequeBranchCode";
	public final static String FILTER1 = "filter1";
	public final static String CHEQUENONEW = "chequeNoNew";
	public final static String FILTER2 = "filter2";
	public final static String CHEQUENONEW1 = "chequeNoNew1";
	public final static String FIXCODE = "fixCode";
	public final static String POSTCODE = "postCode";
	public final static String RECEIVENO = "receiveNo";
	public final static String PAYEEFEESAMEZONE = "payeeFeeSameZone";
	public final static String PAYEEFEEDIFFZONE = "payeeFeeDiffZone";
	public final static String BANKBRANCH = "bankBranch";
	public final static String BANKNAME = "bankName";
	public final static String OWNER = "owner";
	public final static String PAYMENTCHANNEL = "paymentChannel";
	public final static String PAYMENTMETHOD = "paymentMethod";
	public final static String STAFF_IN_CHARGE = "staffInCharge";
	public final static String CONTRACT_ID = "contractId";
	
}
