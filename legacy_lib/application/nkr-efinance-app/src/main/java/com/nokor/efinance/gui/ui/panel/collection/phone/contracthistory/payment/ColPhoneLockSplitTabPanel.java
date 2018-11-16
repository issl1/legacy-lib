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
public class ColPhoneLockSplitTabPanel extends AbstractControlPanel implements SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = -37298199709443973L;
	
	private TabSheet accordionPanel;
	
	private ColPhoneLockSplitSummaryPanel colPhoneLockSplitSummaryPanel;
	private ColPhoneLockSplitHistoryPanel colPhoneLockSplitHistoryPanel;
	private ColPhoneLockSplitAdvancesTablePanel advancesTablePanel;
	private ColPhoneLockSplitTablePanel colPhoneLockSplitTablePanel;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitTabPanel() {
		colPhoneLockSplitSummaryPanel = new ColPhoneLockSplitSummaryPanel();
		colPhoneLockSplitHistoryPanel = new ColPhoneLockSplitHistoryPanel();
		advancesTablePanel = new ColPhoneLockSplitAdvancesTablePanel();
		colPhoneLockSplitTablePanel = new ColPhoneLockSplitTablePanel(this);
		
		accordionPanel = new TabSheet();
		accordionPanel.addSelectedTabChangeListener(this);
		accordionPanel.addTab(colPhoneLockSplitSummaryPanel, I18N.message("summary"));
		accordionPanel.addTab(colPhoneLockSplitHistoryPanel, I18N.message("history"));
		accordionPanel.addTab(advancesTablePanel, I18N.message("advances"));
		accordionPanel.addTab(colPhoneLockSplitTablePanel, I18N.message("lock.split"));
		
		addComponent(accordionPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValue(Contract contract) {
		this.contract = contract;
		colPhoneLockSplitSummaryPanel.assignValues(contract);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void refereshValues(Contract contract) {
		colPhoneLockSplitSummaryPanel.assignValues(contract);
		colPhoneLockSplitTablePanel.assignValue(contract);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (accordionPanel.getSelectedTab().equals(colPhoneLockSplitHistoryPanel)) {
			colPhoneLockSplitHistoryPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(colPhoneLockSplitTablePanel)) {
			colPhoneLockSplitTablePanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(advancesTablePanel)) {
			advancesTablePanel.assignValues(contract);
		}
	}
}
