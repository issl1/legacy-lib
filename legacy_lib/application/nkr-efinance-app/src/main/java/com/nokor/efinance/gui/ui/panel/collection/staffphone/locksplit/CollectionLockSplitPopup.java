package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.CollectionLockSplitsPanel;
import com.vaadin.ui.Window;

/**
 * Lock split pop up in collection
 * @author uhout.cheng
 */
public class CollectionLockSplitPopup extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 8351802341394410453L;

	private ColLockSplitsPanel lockSplitsPanel;
	
	private CollectionLockSplitsPanel colMainPanelLockSplit;
	
	private LockSplit lockSplit;
	
	/**
	 * 
	 * @param colMainPanelLockSplit
	 */
	public CollectionLockSplitPopup(CollectionLockSplitsPanel colMainPanelLockSplit) {
		this.colMainPanelLockSplit = colMainPanelLockSplit;
		setModal(true);
		setResizable(false);
		setWidth(73, Unit.PERCENTAGE);
		setCaption(I18N.message("current.lock.split"));
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		lockSplitsPanel = new ColLockSplitsPanel(this);
		setContent(lockSplitsPanel);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
		lockSplitsPanel.assignValues(lockSplit);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void refresh(LockSplit lockSplit) {
		LCK_SPL_SRV.refresh(lockSplit);
		colMainPanelLockSplit.assignValues(this.lockSplit.getContract());
	}
	
}
