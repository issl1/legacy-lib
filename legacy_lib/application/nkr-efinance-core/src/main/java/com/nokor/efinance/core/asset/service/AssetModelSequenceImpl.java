package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Code generator
 * @author uhout.cheng
 */
public class AssetModelSequenceImpl implements SequenceGenerator {
	
	private String assRangeCode;
	private Integer sequence;
		
	/**
	 * 
	 * @param assRangeCode
	 * @param sequence
	 */
	public AssetModelSequenceImpl(String assRangeCode, Integer sequence) {
		this.assRangeCode = assRangeCode;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String sequenceNumber = "000" + sequence;
		return assRangeCode + StringUtils.right(sequenceNumber, 4);
	}

}
