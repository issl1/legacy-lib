package com.nokor.efinance.core.quotation.impl;

import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Quotation reference number generator
 * @author uhout.cheng
 */
public class QuotationReferenceGeneratorImpl implements SequenceGenerator {

	private String year;
	private Long sequence;
	private FinProduct finProduct;
	
	/**
	 * 
	 * @param dealer
	 * @param type
	 * @param sequence
	 */
	public QuotationReferenceGeneratorImpl(FinProduct dealer, String type, Long sequence) {
		this.finProduct = dealer;
		this.year = type;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "00000000" + sequence;
		return finProduct.getCode() + "" + year + "" + sequenceNumber.substring(sequenceNumber.length() - 6);
	}
}
