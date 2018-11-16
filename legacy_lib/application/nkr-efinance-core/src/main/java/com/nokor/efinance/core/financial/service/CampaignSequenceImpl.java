package com.nokor.efinance.core.financial.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Code generator for campaign
 * @author uhout.cheng
 */
public class CampaignSequenceImpl implements SequenceGenerator {
	
	private Date createDate;
	private Integer sequence;
		
	/**
	 * 
	 * @param createDate
	 * @param sequence
	 */
	public CampaignSequenceImpl(Date createDate, Integer sequence) {
		this.createDate = createDate;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String yearLabel = DateUtils.getDateLabel(createDate, "yy");
		String sequenceNumber = "000" + sequence;
		return yearLabel + StringUtils.right(sequenceNumber, 3);
	}
}
