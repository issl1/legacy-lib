package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.payment.locksplit.LockSplitFormPanel;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class RedemptionLockSplitPopupPanel extends Window implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2769927261753425786L;
	
	private LockSplitFormPanel lockSplitFormPanel;
	
	public RedemptionLockSplitPopupPanel() {
		setModal(true);
		setCaption(I18N.message("lock.split"));
		lockSplitFormPanel = new LockSplitFormPanel();
		setContent(lockSplitFormPanel);
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValues(Contract contract, double redemptionFee, double discountFee) {
		LockSplit lockSplit = LCK_SPL_SRV.getLockSplitByContract(contract);
		FinService finService = LCK_SPL_SRV.getServicebyType(EServiceType.REDEMFEE);
		JournalEvent journalEvent = null;
		if (finService != null) {
			journalEvent = finService.getJournalEvent();
		}
		if (lockSplit != null) {
			LockSplitItem lockSplitItem = LCK_SPL_SRV.getLockSplitItemByJournalEvent(lockSplit, journalEvent);
			if (lockSplitItem == null) {
				lockSplitItem = LockSplitItem.createInstance();
				lockSplitItem.setLockSplit(lockSplit);
				lockSplitItem.setJournalEvent(journalEvent);
				lockSplitItem.setLockSplitCategory(ELockSplitCategory.DUE);
			}
			lockSplitItem.setTiAmount(redemptionFee);
			lockSplitItem.setVatAmount(redemptionFee);
			LCK_SPL_SRV.saveOrUpdate(lockSplitItem);
			
			lockSplitFormPanel.assignValues(lockSplit.getId());
		}
	}
}
