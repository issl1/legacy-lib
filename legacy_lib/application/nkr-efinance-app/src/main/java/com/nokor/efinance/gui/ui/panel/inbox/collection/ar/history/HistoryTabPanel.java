package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.history;

import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.BlockedPaymentDetailsPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class HistoryTabPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2097000895720241162L;
	
	private HistoryPaymentsTabPanel paymentsTabPanel;
	private BlockedPaymentDetailsPanel errorDetailsPanel;
	
	private TabSheet tabSheet;
	
	public HistoryTabPanel() {
		setMargin(true);
		
		paymentsTabPanel = new HistoryPaymentsTabPanel();
		errorDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.ERROR);
		
		tabSheet = new TabSheet();
		tabSheet.addTab(paymentsTabPanel, I18N.message("payments"));
		tabSheet.addTab(errorDetailsPanel, I18N.message("error"));
		
		addComponent(tabSheet);
	}
	
	public void assignValue() {
	}
}
