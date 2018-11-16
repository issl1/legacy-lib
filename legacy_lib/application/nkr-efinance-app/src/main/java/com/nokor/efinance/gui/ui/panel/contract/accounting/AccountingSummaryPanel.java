package com.nokor.efinance.gui.ui.panel.contract.accounting;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountingSummaryPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 6616507597811087655L;
	
	private AccountingSummaryTopPanel accountingSummaryTopPanel;
	private AccountingSummaryBottomPanel accountingSummaryBottomPanel;
	
	/**
	 * Accounting Summary Panel
	 */
	public AccountingSummaryPanel() {
		accountingSummaryTopPanel = new AccountingSummaryTopPanel();
		accountingSummaryTopPanel.assignValues();
		accountingSummaryBottomPanel = new AccountingSummaryBottomPanel();
		accountingSummaryBottomPanel.assignValues();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(accountingSummaryTopPanel);
		mainLayout.addComponent(accountingSummaryBottomPanel);
		
		addComponent(mainLayout);
	}
	

}
