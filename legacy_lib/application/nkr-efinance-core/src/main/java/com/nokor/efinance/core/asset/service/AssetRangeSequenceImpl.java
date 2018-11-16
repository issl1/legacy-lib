package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Code generator
 * @author uhout.cheng
 */
public class AssetRangeSequenceImpl implements SequenceGenerator {
	
	private String assMakeCode;
	private Integer sequence;
		
	/**
	 * 
	 * @param assMakeCode
	 * @param sequence
	 */
	public AssetRangeSequenceImpl(String assMakeCode, Integer sequence) {
		this.assMakeCode = assMakeCode;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "000" + sequence;
		return assMakeCode + StringUtils.right(sequenceNumber, 3);
	}
}
