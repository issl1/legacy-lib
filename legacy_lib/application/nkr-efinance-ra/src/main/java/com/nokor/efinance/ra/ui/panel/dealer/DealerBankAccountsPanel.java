package com.nokor.efinance.ra.ui.panel.dealer;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author sok.vina
 *
 */
public class DealerBankAccountsPanel extends AbstractTabPanel implements FinServicesHelper, FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private DealerBankAccountTabPanel dealerBankAccountTabPanel;
	
	private DealerPaymentMethodsPanel paymentMethodsPanel;
	
	/**
	 */
	public DealerBankAccountsPanel() {
		super();
		setSizeFull();
		setMargin(true);
		setSpacing(true);			
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dealerBankAccountTabPanel = new DealerBankAccountTabPanel(this);
		paymentMethodsPanel = new DealerPaymentMethodsPanel();
		
		TabSheet paymentMethodTab = new TabSheet();
		paymentMethodTab.addTab(paymentMethodsPanel, I18N.message("payment.methods"));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		contentLayout.addComponent(dealerBankAccountTabPanel);
		contentLayout.addComponent(paymentMethodTab);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		dealerBankAccountTabPanel.assignValue(dealer);
		refreshPaymentMethod(dealer);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	protected void refreshPaymentMethod(Dealer dealer) {
		paymentMethodsPanel.assignValues(dealer);
	}
}
