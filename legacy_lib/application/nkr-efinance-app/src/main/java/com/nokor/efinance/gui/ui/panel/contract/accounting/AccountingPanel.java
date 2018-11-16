package com.nokor.efinance.gui.ui.panel.contract.accounting;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.TabSheet;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountingPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 1125305399310794089L;

	private TabSheet accordionPanel;
	
	private AccountingSummaryPanel accountingSummaryPanel;
	private AccountingJournalPanel accountingJournalPanel;
	
	/**
	 * Accounting Panel
	 */
	public AccountingPanel() {
		accountingSummaryPanel = new AccountingSummaryPanel();
		accountingJournalPanel = new AccountingJournalPanel();
		
		accordionPanel = new TabSheet();
		accordionPanel.addTab(accountingSummaryPanel, I18N.message("summary"));
		accordionPanel.addTab(accountingJournalPanel, I18N.message("journal"));
		
		addComponent(accordionPanel);
	}
	
}
