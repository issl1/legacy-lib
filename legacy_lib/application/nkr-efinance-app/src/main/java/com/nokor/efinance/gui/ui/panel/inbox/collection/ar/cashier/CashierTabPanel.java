package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.cashier;

import com.nokor.efinance.gui.ui.panel.payment.cashier.PaymentCashierDetailTablePanel;
import com.nokor.efinance.share.contract.PaymentType;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class CashierTabPanel extends VerticalLayout implements SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1620282847060643343L;

	private TabSheet tabSheet;
	
	private PaymentThirdPartiesTabPanel auctionTabPanel;
	private PaymentThirdPartiesTabPanel claimsTabPanel;
	private PaymentThirdPartiesTabPanel compansationTabPanel;
	
	private PaymentCashierDetailTablePanel paymentCashierDetailTablePanel;
	
	/**
	 * 
	 */
	public CashierTabPanel() {
		setMargin(true);
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		
		auctionTabPanel = new PaymentThirdPartiesTabPanel(PaymentType.AUCTION);
		claimsTabPanel = new PaymentThirdPartiesTabPanel(PaymentType.CLAIM);
		compansationTabPanel = new PaymentThirdPartiesTabPanel(PaymentType.COMPENSATION);
		paymentCashierDetailTablePanel = new PaymentCashierDetailTablePanel(false);
		
		tabSheet.addTab(paymentCashierDetailTablePanel, I18N.message("cashier"));
		tabSheet.addTab(auctionTabPanel, I18N.message("auction"));
		tabSheet.addTab(claimsTabPanel, I18N.message("claims"));
		tabSheet.addTab(compansationTabPanel, I18N.message("compansation"));
		
		addComponent(tabSheet);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(auctionTabPanel)) {
			auctionTabPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(claimsTabPanel)) {
			claimsTabPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(compansationTabPanel)) {
			compansationTabPanel.refresh();
		} 
	}

}
