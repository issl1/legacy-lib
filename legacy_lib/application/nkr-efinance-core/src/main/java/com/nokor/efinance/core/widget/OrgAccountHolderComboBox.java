package com.nokor.efinance.core.widget;

import java.util.List;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;

/**
 * 
 * @author uhout.cheng
 *
 */
public class OrgAccountHolderComboBox extends EntityAComboBox<OrgAccountHolder> {

	/** */
	private static final long serialVersionUID = -4257081344801007835L;

	/**
	 * 
	 * @param orgAccountHolders
	 */
	public OrgAccountHolderComboBox(List<OrgAccountHolder> orgAccountHolders) {
		super(orgAccountHolders);
	}
	
	/**
	 * 
	 * @param caption
	 * @param orgAccountHolders
	 */
	public OrgAccountHolderComboBox(String caption, List<OrgAccountHolder> orgAccountHolders) {
		super(caption, orgAccountHolders);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(OrgAccountHolder orgAccountHolder) {
		AccountHolderDTO accountHolderDTO = ClientAccountHolder.getAccountHolderById(orgAccountHolder.getAccountHolder());
		return accountHolderDTO == null ? null : accountHolderDTO.getName();
	}

}
