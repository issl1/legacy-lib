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
@VaadinView(LossAdjustmentReportsPanel.NAME)
public class LossAdjustmentReportsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "report.loss.adjustments";
		
	@Autowired
	private LossAdjustmentReportTablePanel lossAdjusmentReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		lossAdjusmentReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(lossAdjusmentReportTablePanel);
	}
	
	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == lossAdjusmentReportTablePanel
				&& getTabSheet().isNeedRefresh()) {
			lossAdjusmentReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	public void redirectToPurchaseOrderTablePanel() {
		lossAdjusmentReportTablePanel.refresh();
		getTabSheet().setSelectedTab(lossAdjusmentReportTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
