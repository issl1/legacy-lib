package com.nokor.efinance.core.contract.service;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Reference number generator
 * @author uhout.cheng
 */
public class ContractSequenceImpl implements SequenceGenerator {

	private String year;
	private Long sequence;
	private String prefix;
	
	/**
	 * 
	 * @param dealer
	 * @param type
	 * @param sequence
	 */
	public ContractSequenceImpl(String prefix, String type, Long sequence) {
		this.prefix = prefix;
		this.year = type;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "00000000" + sequence;
		return prefix + "" + year + "" + sequenceNumber.substring(sequenceNumber.length() - 6);
	}
}
