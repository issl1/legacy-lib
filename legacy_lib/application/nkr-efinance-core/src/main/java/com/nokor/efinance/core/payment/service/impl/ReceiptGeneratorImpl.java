package com.nokor.efinance.core.payment.service.impl;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Default Sequence generator 
 * @author ly.youhort
 *
 */
public class ReceiptGeneratorImpl implements SequenceGenerator {

	private String type;
	private Long sequence;
	private Dealer dealer;
	
	/**
	 * @param dealer
	 */
	public ReceiptGeneratorImpl(Dealer dealer, String type, Long sequence) {
		this.dealer = dealer;
		this.type = type;
		this.sequence = sequence;
	}
	
	@Override
	public String generate() {
		String sequenceNumber = "00000000" + sequence;
		return dealer.getCode() + "-" + type + "-" + sequenceNumber.substring(sequenceNumber.length() - 6);
	}
}
