package com.nokor.efinance.core.payment.model;

/**
 * Meta data of com.nokor.efinance.core.payment.model.PaymentFile
 * @author bunlong.taing
 */
public interface MPaymentFile {
	
	public static final String FORMAT = "format";
	
	// Header
	public static final String SEQUENCE = "sequence";
	public static final String BANKCODE = "bankCode";
	public static final String COMPANYACCOUNT = "companyAccount";
	public static final String COMPANYACCOUNTOPTIONAL = "companyAccountOptional";
	public static final String COMPANYNAME = "companyName";
	public static final String EFFECTIVEDATE = "effectiveDate";
	public static final String SERVICECODE = "serviceCode";
	public static final String FILTER = "filter";
	
	// Total
	public static final String LASTSEQUENCE = "lastSequence";
	public static final String FOOTERBANKCODE = "footerBankCode";
	public static final String FOOTERCOMPANYACCOUNT = "footerCompanyAccount";
	public static final String TOTALDEBITAMOUNT = "totalDebitAmount";
	public static final String TOTALDEBITTRANSACTION = "totalDebitTransaction";
	public static final String TOTALCREDITAMOUNT = "totalCreditAmount";
	public static final String TOTALCREDITTRANSACTION = "totalCreditTransaction";
	public static final String TOTALTRANSACTION = "totalTransaction";
	public static final String FOOTERFILTER = "footerFilter";
	public static final String FIXCODE = "fixCode";
	public static final String TOTALAMOUNT = "totalAmount";

}
