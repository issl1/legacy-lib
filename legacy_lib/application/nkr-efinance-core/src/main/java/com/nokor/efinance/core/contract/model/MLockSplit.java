package com.nokor.efinance.core.contract.model;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.contract.model.LockSplit
 * @author uhout.cheng
 */
public interface MLockSplit extends MEntityA {

	// For Vaadin grid
	public final static String ACTIONS = "actions";
	public final static String DEPARTMENT = "department";
	public final static String DETAIL = "detail";
	
	public final static String CONTRACT = "contract";
	public final static String REFERENCE = "reference";
	public final static String PAYMENTDATE = "from";
	public final static String EXPIRYDATE = "to";
	public final static String PAYMENTCHANNEL = "paymentChannel";
	public final static String WHITECLEARANCE = "whiteClearance";
	public final static String TOTALAMOUNT = "totalAmount";
	public final static String TOTALVATAMOUNT = "totalVatAmount";
	public final static String ITEMS = "items";
	public final static String AFTERSALEEVENT = "afterSaleEvent";
	public final static String LOCKSPLIT = "lockSplit";
	public final static String LOCKSPLITTYPE = "lockSplitType";
	public final static String LOCKSPLITGROUP = "lockSplitGroup";
	public final static String TIAMOUNT = "tiAmount";
	public final static String VATAMOUNT = "vatAmount";
	public final static String PRIORITY = "priority";
	public final static String STATUS = "wkfStatus";

}
