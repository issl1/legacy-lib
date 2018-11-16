/**
 * Auto-generated on Thu Jun 11 08:28:18 ICT 2015
 */
package com.nokor.efinance.core.collection.model;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.collection.model.Collection
 * @author 
 */
public interface MCollection extends MEntityA {

	// For Vaadin Grid
	public final static String SELECT = "select";
	public final static String CONTRACTID = "contract.id";
	public final static String ODM = "odm";
	public final static String BORROWER = "borrower";
	public final static String GUARANTOR = "guarantor";
	public final static String AMOUNTPASTDUE = "amount.past.due";
	public final static String DPD = "dpd";
	public final static String APD = "apd";
	public final static String LATESTRESULT = "latest.result";
	public final static String NEXTACTION = "next.action";
	public final static String NEXTACTIONDATE = "next.action.date";
	public final static String OVERUDE30 = "overdue30";
	public final static String OVERUDE60 = "overdue60";
	public final static String OVERUDE90 = "overdue90";
	public final static String OVERUDE91PLUS = "overdue91.plus";
	public final static String AMOUNTCOLLECTED = "amount.collected";
	public final static String ASSIGNDATE = "assign.date";
	public final static String COLLECTOR = "collector";
	public final static String LESSESS = "lessee";
	public final static String PRIMARYPHONENO = "primary.phone.no";
	public final static String DAYSSINCECONTRACT = "days.since.contract";
	public final static String PI = "pi";
	public final static String RI = "ri";
	public final static String ODI = "odi";
	public final static String INSTALLMENT = "installment";
	public final static String COLLECTED = "collected";
	public final static String ASSIST = "assist";
	public final static String FLAG = "flag";
	public final static String FOLLOWUP = "follow.up";
	public final static String ACTION = "action";
	
	
	public final static String COLLECTIONTASK = "collectionTask";
	public final static String GROUP = "group";
	public final static String NBINSTALLMENTSINOVERDUE = "nbInstallmentsInOverdue";
	public final static String NBOVERDUEINDAYS = "nbOverdueInDays";
	public final static String LATESTNUMINSTALLMENTPAID = "latestNumInstallmentPaid";
	public final static String NBINSTALLMENTSPAID = "nbInstallmentsPaid";
	public final static String TETOTALAMOUNTINOVERDUEUSD = "teTotalAmountInOverdueUsd";
	public final static String VATTOTALAMOUNTINOVERDUEUSD = "vatTotalAmountInOverdueUsd";
	public final static String TITOTALAMOUNTINOVERDUEUSD = "tiTotalAmountInOverdueUsd";
	public final static String TETOTALPENALTYAMOUNTINOVERDUEUSD = "teTotalPenaltyAmountInOverdueUsd";
	public final static String VATTOTALPENALTYAMOUNTINOVERDUEUSD = "vatTotalPenaltyAmountInOverdueUsd";
	public final static String TITOTALPENALTYAMOUNTINOVERDUEUSD = "tiTotalPenaltyAmountInOverdueUsd";
	public final static String LASTPAIDPAYMENTMETHOD = "lastPaidPaymentMethod";
	public final static String LATESTPAYMENTDATE = "latestPaymentDate";
	public final static String WKFSTATUS = "wkfStatus";
	public final static String WKFCHANGEDDATE = "wkfChangedDate";
	public final static String WKFREASON = "wkfReason";
	public final static String HISTCOMMENT = "histComment";
	public final static String STATUSRECORD = "statusRecord";
	public final static String CRUDACTION = "crudAction";
	public final static String DEBTLEVEL = "debtLevel";
	public final static String DUEDAY = "dueDay";
	public final static String NEXTDUEDATE = "nextDueDate";
	public final static String LASTCOLLECTIONFLAG = "lastCollectionFlag";
	public final static String LASTCOLLECTIONASSIST = "lastCollectionAssist";
	public final static String LASTCOLLECTIONHISTORY = "lastCollectionHistory";
	public final static String REQUESTEXTENDSTATUS = "requestExtendStatus";
	public final static String EXTENDINDAY = "extendInDay";
	public final static String NBINSTALLMENTPAIDCURRMONTH = "nbInstallmentsPaidCurrMonth";
	public final static String NBCLEAREDCONTRACTSCURRMONTH = "nbClearedContractsCurrMonth";
	public final static String TETOTALAMOUNTPAIDCURRMONTH = "teTotalAmountPaidCurrMonth";
	public final static String VATTOTALAMOUNTPAIDCURRMONTH = "vatTotalAmountPaidCurrMonth";
	public final static String TITOTALAMOUNTPAIDCURRMONTH = "tiTotalAmountPaidCurrMonth";
	
	final static String FIRST_INSTALLMENT = I18N.message("first.installment");
	final static String SECOND_INSTALLMENT = I18N.message("second.installment");
	final static String THIRD_INSTALLMENT = I18N.message("third.installment");
	final static String FOLLOW_UP = I18N.message("follow.up");
	final static String PROCESSED = I18N.message("processed");
	
	final static String LOST = I18N.message("lost");
	final static String DAMAGED = I18N.message("damaged");
	final static String RESULT = "result";
	
	final static String AREA = "area";
	final static String BRANCH = "branch";
	
	final static String DATE = "date";
	final static String DESC = "desc";
	final static String AMOUNT = "amount";
	final static String BALANCE = "balance";
	final static String ITEM = "item";
	final static String DUEDATE = "due.date";
	final static String DUEAMOUNT = "due.amount";
	final static String PERCENTAGAMOUNT = "percentag.amount";
	final static String STATUS = "status";

}
