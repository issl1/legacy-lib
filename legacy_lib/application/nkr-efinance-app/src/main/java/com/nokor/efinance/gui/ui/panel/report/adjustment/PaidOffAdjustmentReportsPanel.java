package com.nokor.efinance.gui.ui.panel.report.adjustment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

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
@VaadinView(PaidOffAdjustmentReportsPanel.NAME)
public class PaidOffAdjustmentReportsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "report.paid.off.adjustments";
		
	@Autowired
	private PaidOffAdjustmentReportTablePanel paidOffAdjusmentReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		paidOffAdjusmentReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(paidOffAdjusmentReportTablePanel);
	}
	
	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == paidOffAdjusmentReportTablePanel
				&& getTabSheet().isNeedRefresh()) {
			paidOffAdjusmentReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	public void redirectToPurchaseOrderTablePanel() {
		paidOffAdjusmentReportTablePanel.refresh();
		getTabSheet().setSelectedTab(paidOffAdjusmentReportTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
