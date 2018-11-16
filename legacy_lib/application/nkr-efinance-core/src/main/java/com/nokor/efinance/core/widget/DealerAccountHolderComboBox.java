package com.nokor.efinance.core.widget;

import java.util.List;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;

/**
 * 
 * @author uhout.cheng
 *
 */
public class DealerAccountHolderComboBox extends EntityAComboBox<DealerAccountHolder> {

	/** */
	private static final long serialVersionUID = -5880875513671974484L;

	/**
	 * 
	 * @param dealerAccountHolders
	 */
	public DealerAccountHolderComboBox(List<DealerAccountHolder> dealerAccountHolders) {
		super(dealerAccountHolders);
	}
	
	/**
	 * 
	 * @param caption
	 * @param dealerAccountHolders
	 */
	public DealerAccountHolderComboBox(String caption, List<DealerAccountHolder> dealerAccountHolders) {
		super(caption, dealerAccountHolders);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(DealerAccountHolder dealerAccountHolder) {
		AccountHolderDTO accountHolderDTO = ClientAccountHolder.getAccountHolderById(dealerAccountHolder.getAccountHolder());
		return accountHolderDTO == null ? null : accountHolderDTO.getName();
	}

}
