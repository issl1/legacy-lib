package com.nokor.efinance.core.contract.service;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Reference number generator
 * @author uhout.cheng
 */
public class LockSplitSequenceImpl implements SequenceGenerator {
	
	private Long sequence;
		
	/**
	 * 
	 * @param dealer
	 * @param type
	 * @param sequence
	 */
	public LockSplitSequenceImpl(Long sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "0000000000" + sequence;
		return sequenceNumber.substring(sequenceNumber.length() - 10);
	}
}
