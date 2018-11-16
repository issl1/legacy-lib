package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitHistoryPanel extends AbstractControlPanel implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -4581969668756464704L;
	
	private TabSheet accordionPanel;
	
	private ColPhoneLockSplitReceiptCodeHistoryPanel colPhoneLockSplitReceiptCodeHistoryPanel;
	private ColPhoneLockSplitListHistoryPanel colPhoneLockSplitListHistoryPanel;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitHistoryPanel() {
		colPhoneLockSplitReceiptCodeHistoryPanel = new ColPhoneLockSplitReceiptCodeHistoryPanel();
		colPhoneLockSplitListHistoryPanel = new ColPhoneLockSplitListHistoryPanel();
		
		accordionPanel = new TabSheet();
		accordionPanel.addSelectedTabChangeListener(this);
		accordionPanel.addTab(colPhoneLockSplitListHistoryPanel, I18N.message("list"));
		accordionPanel.addTab(colPhoneLockSplitReceiptCodeHistoryPanel, I18N.message("display.by.receipt.code"));
		
		setMargin(true);
		setSpacing(true);
		addComponent(accordionPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		this.contract = contract;
		colPhoneLockSplitListHistoryPanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (accordionPanel.getSelectedTab().equals(colPhoneLockSplitReceiptCodeHistoryPanel)) {
			colPhoneLockSplitReceiptCodeHistoryPanel.assignValues(contract);
		}
	}

}
