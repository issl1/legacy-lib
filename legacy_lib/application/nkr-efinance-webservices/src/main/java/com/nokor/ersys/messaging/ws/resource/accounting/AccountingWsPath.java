package com.nokor.ersys.messaging.ws.resource.accounting;

/**
 * 
 * @author prasnar
 *
 */
public interface AccountingWsPath {
	final static String ACCOUNTING = "/accounting";
	final static String ACCOUNTS = "/accounts";
	final static String JOURNALS = "/journals";
	final static String JOURNALENTRIES = "/journalentries";
	final static String RECONCILE = "/reconcile";
	final static String POST = "/post";
	final static String EVENTACCOUNTS = "/eventaccounts";
	final static String EVENTS = "/events";
	final static String JOURNAL_RECEIPTS = "/receipts";
	final static String JOURNAL_PAYMENTS = "/payments";
	final static String PATH_JOURNAL_ID = "{slash:/?}{journalId:.*}";
	final static String JOURNAL_ID = "journalId";
	final static String EVENT_ID = "eventId";
	final static String ACCOUNT_ID = "accountId";
	final static String EVENT_GROUP_ID = "eventGroupId";
	final static String TRANSACTIONENTRIES = "/transactionentries";
}
