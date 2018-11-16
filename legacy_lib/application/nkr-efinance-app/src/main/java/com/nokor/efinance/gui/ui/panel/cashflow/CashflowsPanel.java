package com.nokor.efinance.gui.ui.panel.cashflow;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CashflowsPanel.NAME)
public class CashflowsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = 5875568105689149743L;

	public static final String NAME = "cashflows";
	
	@Autowired
	private CashflowTablePanel cashflowTablePanel;
	@Autowired
	private CashflowFormPanel cashflowFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		cashflowTablePanel.setMainPanel(this);
		cashflowFormPanel.setCaption(I18N.message("cashflow"));
		getTabSheet().setTablePanel(cashflowTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		cashflowFormPanel.reset();
		getTabSheet().addFormPanel(cashflowFormPanel);
		getTabSheet().setSelectedTab(cashflowFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(cashflowFormPanel);
		initSelectedTab(cashflowFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == cashflowFormPanel) {
			cashflowFormPanel.assignValues(cashflowTablePanel.getItemSelectedId());
		} else if (selectedTab == cashflowTablePanel && getTabSheet().isNeedRefresh()) {
			cashflowTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
