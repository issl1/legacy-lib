package com.nokor.efinance.core.quotation.impl;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Default Sequence generator 
 * @author ly.youhort
 *
 */
public class SequenceGeneratorByDealerImpl implements SequenceGenerator {

	private Long sequence;
	private String prefix;
	private Dealer dealer;
	
	/**
	 * @param dealer
	 */
	public SequenceGeneratorByDealerImpl(String prefix, Dealer dealer, Long sequence) {
		this.prefix = prefix;
		this.dealer = dealer;
		this.sequence = sequence;
	}
	
	@Override
	public String generate() {
		String sequenceNumber = "00000000" + sequence;
		return prefix + "-" + dealer.getCode() + "-" + sequenceNumber.substring(sequenceNumber.length() - 8);
	}
}
