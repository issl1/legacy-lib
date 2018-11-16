package com.nokor.efinance.core.applicant.service;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Customer Reference number generator
 * @author uhout.cheng
 */
public class ApplicantSequenceImpl implements SequenceGenerator {

	private Long sequence;
	
	/**
	 * 
	 * @param sequence
	 */
	public ApplicantSequenceImpl(Long sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "0000000" + sequence;
		return "A2" + sequenceNumber.substring(sequenceNumber.length() - 6);
	}
}
