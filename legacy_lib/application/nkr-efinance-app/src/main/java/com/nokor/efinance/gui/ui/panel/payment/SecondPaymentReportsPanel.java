package com.nokor.efinance.gui.ui.panel.payment;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
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
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SecondPaymentReportsPanel.NAME)
public class SecondPaymentReportsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "report.second.payments";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SecondPaymentReportTablePanel secondPaymentReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		secondPaymentReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(secondPaymentReportTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		String paymnId = event.getParameters();
		if (StringUtils.isNotEmpty(paymnId)) {;
			getTabSheet().setForceSelected(true);
		}
	}

	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == secondPaymentReportTablePanel && getTabSheet().isNeedRefresh()) {
			secondPaymentReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	public void redirectToSecondPaymentTable() {
		secondPaymentReportTablePanel.refresh();
		getTabSheet().setSelectedTab(secondPaymentReportTablePanel);
	}
}
