package com.nokor.efinance.core.dealer.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.dealer.model.EDealerType;

/**
 * Dealer service interface
 * @author ly.youhort
 *
 */
public interface DealerService extends BaseEntityService {
	
	/**
	 * Create a new Dealer
	 * @param dealer
	 * @return
	 */
	Dealer createDealer(Dealer dealer);
	
	/**
	 * @param bankAccount
	 * @return
	 */
	DealerBankAccount saveOrUpdateBankAccount(DealerBankAccount bankAccount);
	
	/**
	 * @param parent
	 * @return
	 */
	List<Dealer> getBranches(Dealer parent);
	
	/**
	 * 
	 * @param group
	 * @return
	 */
	List<Dealer> getGroupType(DealerGroup group, EDealerType type);
	
	/**
	 * 
	 * @param dealerAddress
	 */
	void saveOrUpdateDealerAddress(DealerAddress dealerAddress);
	
	/**
	 * 
	 * @param dealerPaymentMethod
	 */
	void saveOrUpdateDealerPaymentMethod(DealerPaymentMethod dealerPaymentMethod);
	
	/**
	 * 
	 * @param dealerEmployee
	 */
	void saveOrUpdateDealerEmployeeAddress(DealerEmployee dealerEmployee);
}
