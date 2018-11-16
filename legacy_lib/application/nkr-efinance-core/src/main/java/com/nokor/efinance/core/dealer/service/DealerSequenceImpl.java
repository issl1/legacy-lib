package com.nokor.efinance.core.dealer.service;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerCategory;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Code generator for dealer
 * @author uhout.cheng
 */
public class DealerSequenceImpl implements SequenceGenerator {
	
	private Dealer dealer;
	private Integer sequence;
		
	/**
	 * 
	 * @param dealer
	 * @param sequence
	 */
	public DealerSequenceImpl(Dealer dealer, Integer sequence) {
		this.dealer = dealer;
		this.sequence = sequence;
	}
	
	/**
	 * @see com.nokor.efinance.core.quotation.SequenceGenerator#generate()
	 */
	@Override
	public String generate() {
		String deaCatCode = StringUtils.EMPTY;
		EDealerCategory deaCategory = dealer.getDealerCategory();
		EDealerType deaType = dealer.getDealerType();
		if (deaCategory != null) {
			if (EDealerCategory.AUTHORIZED.equals(deaCategory)) {
				deaCatCode = "1";
			} else if (EDealerCategory.BROKER.equals(deaCategory)) {
				deaCatCode = "2";
			}
		} else {
			deaCatCode = "1";
		}
		if (EDealerType.HEAD.equals(deaType)) {
			String sequenceNbHead = "000" + sequence;
			return deaCatCode + StringUtils.right(sequenceNbHead, 4);
		} else if (EDealerType.BRANCH.equals(deaType)) {
			String sequenceNbBranch = "000" + sequence;
			Dealer mainDealer = dealer.getParent();
			return mainDealer.getCode() + StringUtils.right(sequenceNbBranch, 3);
		} 
		return StringUtils.EMPTY;
	}
}
