package com.nokor.efinance.gui.ui.panel.report.directcost;

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
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DirectCostReportsPanel.NAME)
public class DirectCostReportsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "report.direct.costs";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DirectCostReportTablePanel directCostReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		directCostReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(directCostReportTablePanel);
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
		if (selectedTab == directCostReportTablePanel && getTabSheet().isNeedRefresh()) {
			directCostReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	public void redirectToSecondPaymentTable() {
		directCostReportTablePanel.refresh();
		getTabSheet().setSelectedTab(directCostReportTablePanel);
	}
}
