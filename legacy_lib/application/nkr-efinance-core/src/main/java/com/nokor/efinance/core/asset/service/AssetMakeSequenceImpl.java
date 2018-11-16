package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Code generator
 * @author uhout.cheng
 */
public class AssetMakeSequenceImpl implements SequenceGenerator {
	
	private Integer sequence;
		
	/**
	 * 
	 * @param sequence
	 */
	public AssetMakeSequenceImpl(Integer sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "000" + sequence;
		return StringUtils.right(sequenceNumber, 3);
	}
}
