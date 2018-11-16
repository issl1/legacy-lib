package com.nokor.efinance.core.payment.service.bankdeposit;


import java.util.Date;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.BankDepositReceivedFromDealer;

/**
 * Bank Deposit Service
 * @author meng.kim
 *
 */
public interface BankDepositService extends BaseEntityService  {

	/**
	 * Get Bank Deposit Amount Received From Dealer
	 * @param dealer
	 * @param requestDate
	 * @return
	 */
	BankDepositReceivedFromDealer getBankDepositReceivedFromDealer(Dealer dealer, Date requestDate);
	BankDeposit getBankDepositByDealerAndRequestDate(Dealer dealer, Date requestDate);
	
}
