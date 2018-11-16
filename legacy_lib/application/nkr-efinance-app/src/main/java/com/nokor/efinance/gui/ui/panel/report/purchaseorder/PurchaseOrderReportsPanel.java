package com.nokor.efinance.gui.ui.panel.report.purchaseorder;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PurchaseOrderReportsPanel.NAME)
public class PurchaseOrderReportsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "report.purchase.orders";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PurchaseOrderReportTablePanel purchaseOrderReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		purchaseOrderReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(purchaseOrderReportTablePanel);
	}
	@Override
	public void onAddEventClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditEventClick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == purchaseOrderReportTablePanel
				&& getTabSheet().isNeedRefresh()) {
			purchaseOrderReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	public void redirectToPurchaseOrderTablePanel() {
		purchaseOrderReportTablePanel.refresh();
		getTabSheet().setSelectedTab(purchaseOrderReportTablePanel);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}


}
