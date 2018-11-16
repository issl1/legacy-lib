package com.nokor.efinance.core.workflow;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.efinance.core.auction.model.Auction;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;

/**
 * List of WkfFlow 
 * 
 * @author prasnar
 *
 */
public class FinWkfFlow {
	public static final EWkfFlow QUOTATION = EWkfFlow.getByClass(Quotation.class);
	public static final EWkfFlow CONTRACT = EWkfFlow.getByClass(Contract.class);
	public static final EWkfFlow PAYMENT = EWkfFlow.getByClass(Payment.class);
	public static final EWkfFlow COLLECTION = EWkfFlow.getByClass(Collection.class);
	public static final EWkfFlow AUCTION = EWkfFlow.getByClass(Auction.class);
}