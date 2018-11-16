package com.nokor.efinance.gui.ui.panel.report.installment;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * 
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CashInstallmentPerDealerReportPanel.NAME)
public class CashInstallmentPerDealerReportPanel extends AbstractTabsheetPanel implements View, CashflowEntityField {
	
	/** */
	private static final long serialVersionUID = -4618786633559261506L;
	
	public static final String NAME = "cash.installment.report.panel";
	private int numTabChange = 0;
	
	@Autowired
	private CashInstallmentPerDealerReportTablePanel reportTablePanel;
	@Autowired
	private CashInstallmentPerDealerFormPanel reportFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		buildPanel();
	}
	
	/**
	 * 
	 */
	private void buildPanel() {
		reportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(reportTablePanel);
		getTabSheet().addSelectedTabChangeListener(new SelectedTabChangeListener() {
			/** */
			private static final long serialVersionUID = -5252804986021038915L;
			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				numTabChange++;
				if (reportFormPanel != null && numTabChange == 2) {
					getTabSheet().removeComponent(reportFormPanel);
					numTabChange = 0;
					reportTablePanel.refresh();
				}
			}
		});
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		Dealer dealer = reportTablePanel.getDealerSelected();
		Date date = reportTablePanel.getDateSelected();
		reportFormPanel.assignValue(dealer, date);
		getTabSheet().addTab(reportFormPanel, I18N.message("cash.installment"));
		getTabSheet().setSelectedTab(reportFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == reportTablePanel && getTabSheet().isNeedRefresh()) {
			reportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
}
