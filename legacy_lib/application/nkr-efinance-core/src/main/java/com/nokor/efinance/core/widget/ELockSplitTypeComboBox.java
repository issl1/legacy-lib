package com.nokor.efinance.core.widget;

import java.util.List;

import com.nokor.efinance.core.collection.model.ELockSplitType;

/**
 * ELock Split Type ComboBox
 * @author bunlong.taing
 */
public class ELockSplitTypeComboBox extends EntityAComboBox<ELockSplitType> {

	/** */
	private static final long serialVersionUID = -1536423462666487764L;

	/**
	 * ELock Split Type ComboBox
	 * @param ELockSplitType
	 */
	public ELockSplitTypeComboBox(List<ELockSplitType> eLockSplitType) {
		super(eLockSplitType);
	}
	
	/**
	 * ELock Split Type ComboBox
	 * @param caption
	 * @param ELockSplitType
	 */
	public ELockSplitTypeComboBox(String caption, List<ELockSplitType> eLockSplitType) {
		super(caption, eLockSplitType);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(ELockSplitType eLockSplitType) {
		return eLockSplitType.getDescLocale();
	}

}
