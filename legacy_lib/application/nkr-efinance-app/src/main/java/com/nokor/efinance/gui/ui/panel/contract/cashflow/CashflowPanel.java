package com.nokor.efinance.gui.ui.panel.contract.cashflow;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.gui.ui.panel.contract.cashflow.locksplit.CashflowLockSplitPanel;
import com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.DueTransactionsPanel;
import com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.in.InTransactionPanel;
import com.nokor.efinance.gui.ui.panel.contract.cashflow.transaction.out.OutTransactionPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

/**
 * Cashflow Panel
 * @author bunlong.taing
 */
public class CashflowPanel extends AbstractTabPanel {
	/** */
	private static final long serialVersionUID = -5221332564592552515L;
	
	private TabSheet accordingTab;
	
	private OutTransactionPanel outTransactionPanel;
	private InTransactionPanel inTransactionPanel;
	private DueTransactionsPanel transactionPanel;
	private CashflowLockSplitPanel lockSplitPanel; 

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		outTransactionPanel = new OutTransactionPanel();
		inTransactionPanel = new InTransactionPanel();
		transactionPanel = new DueTransactionsPanel();
		lockSplitPanel = new CashflowLockSplitPanel();
		
		accordingTab = new TabSheet();
		accordingTab.addTab(outTransactionPanel, I18N.message("out"));
		accordingTab.addTab(inTransactionPanel, I18N.message("in"));
		accordingTab.addTab(transactionPanel, I18N.message("dues"));
		accordingTab.addTab(lockSplitPanel, I18N.message("lock.splits"));
		
		return accordingTab;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		transactionPanel.reset();
		lockSplitPanel.reset();
		if (contract != null) {
			outTransactionPanel.assignValues(contract.getId());
			inTransactionPanel.assignValues(contract.getId());
			transactionPanel.assignValues(contract.getId());
			lockSplitPanel.assignValues(contract);
		}
	}

}
