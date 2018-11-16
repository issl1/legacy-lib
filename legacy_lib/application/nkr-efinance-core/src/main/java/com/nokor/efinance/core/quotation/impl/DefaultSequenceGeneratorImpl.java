package com.nokor.efinance.core.quotation.impl;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Default Sequence generator 
 * @author ly.youhort
 *
 */
public class DefaultSequenceGeneratorImpl implements SequenceGenerator {

	@Override
	public String generate() {
		return "SEQ";
	}

}
