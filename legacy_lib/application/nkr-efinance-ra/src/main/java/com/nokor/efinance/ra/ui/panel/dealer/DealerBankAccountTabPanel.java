package com.nokor.efinance.ra.ui.panel.dealer;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class DealerBankAccountTabPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = -2318833965052975156L;

	private TabSheet tabSheet;
    
	private DealerBankAccountTable dealerAccountHolderTable;
	private DealerBankAccountTable dealerBankAccountTable;
	
	private DealerBankAccountsPanel dealerBankAccountsPanel;
	
	private Dealer dealer;

	/**
	 * 
	 * @param dealerBankAccountsPanel
	 */
	public DealerBankAccountTabPanel(DealerBankAccountsPanel dealerBankAccountsPanel) {
		this.dealerBankAccountsPanel = dealerBankAccountsPanel;
		setSpacing(true);
		
		dealerAccountHolderTable = new DealerBankAccountTable(EPaymentMethod.CHEQUE, this);
		dealerBankAccountTable = new DealerBankAccountTable(EPaymentMethod.TRANSFER, this);
		
		tabSheet = new TabSheet();
		tabSheet.addTab(dealerAccountHolderTable, I18N.message("account.holder"));
		tabSheet.addTab(dealerBankAccountTable, I18N.message("bank.account"));
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			/** */
			private static final long serialVersionUID = 3355384197416964293L;

			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (tabSheet.getSelectedTab().equals(dealerBankAccountTable)) {
					dealerBankAccountTable.assignValues(dealer);
				}
			}
		});
		
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public void assignValue(Dealer dealer) {
		this.dealer = dealer;
		tabSheet.setSelectedTab(dealerAccountHolderTable);
		dealerAccountHolderTable.assignValues(dealer);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	protected void refreshPaymentMethod(Dealer dealer) {
		dealerBankAccountsPanel.refreshPaymentMethod(dealer);
	}

}
