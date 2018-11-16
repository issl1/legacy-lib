package com.nokor.ersys.messaging.ws.resource.accounting;

import com.nokor.common.messaging.ws.resource.BaseWsMessage;

/**
 * 
 * @author prasnar
 *
 */
public class AccountingWsMessage extends BaseWsMessage {
	public static final AccountingWsMessage JOURNAL_ID_MANDATORY = new AccountingWsMessage("Journal ID is mandatory");
	public static final AccountingWsMessage JOURNAL_EVENT_CODE_MANDATORY = new AccountingWsMessage("Journal event code is mandatory");
	public static final AccountingWsMessage JOURNAL_EVENT_NOT_FOUND = new AccountingWsMessage("Journal event not found");
	public static final AccountingWsMessage AMOUNT_MANDATORY = new AccountingWsMessage("Amount is mandatory");
	public static final AccountingWsMessage ORGANIZATION_MANDATORY = new AccountingWsMessage("Organization is mandatory");
	public static final AccountingWsMessage ORGANIZATION_NOT_FOUND = new AccountingWsMessage("Organization not found");
	public static final AccountingWsMessage ACCOUNT_MANDATORY = new AccountingWsMessage("Account is mandatory");
	public static final AccountingWsMessage ACCOUNT_NOT_FOUND = new AccountingWsMessage("Account not found");
	public static final AccountingWsMessage EVENT_MANDATORY = new AccountingWsMessage("Event is mandatory");
	public static final AccountingWsMessage EVENT_NOT_FOUND = new AccountingWsMessage("Event not found");
	public static final AccountingWsMessage JOURNAL_MANDATORY = new AccountingWsMessage("Journal is mandatory");
	public static final AccountingWsMessage JOURNAL_NOT_FOUND = new AccountingWsMessage("Journal not found");
	public static final AccountingWsMessage JOURNAL_EVENT_GROUP_NOT_FOUND = new AccountingWsMessage("Journal event group not found");

	/**
	 */
	private AccountingWsMessage(String desc) {
		super(desc);
	}
}
