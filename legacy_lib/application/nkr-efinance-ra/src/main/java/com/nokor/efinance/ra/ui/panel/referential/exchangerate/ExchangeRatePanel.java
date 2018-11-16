package com.nokor.efinance.ra.ui.panel.referential.exchangerate;

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
 *  Exchange Rate panel
 * @author nora.ky
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ExchangeRatePanel.NAME)
public class ExchangeRatePanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 6762221053318346279L;

	public static final String NAME = "exchangerate";
	
	@Autowired
	private ExchangeRateTablePanel exchangeRateTablePanel;
	@Autowired
	private ExchangeRateFormPanel exchangeRateFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		exchangeRateTablePanel.setMainPanel(this);
		exchangeRateFormPanel.setCaption(I18N.message("exchangerate"));
		getTabSheet().setTablePanel(exchangeRateTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		exchangeRateFormPanel.reset();
		getTabSheet().addFormPanel(exchangeRateFormPanel);
		getTabSheet().setSelectedTab(exchangeRateFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(exchangeRateFormPanel);
		initSelectedTab(exchangeRateFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == exchangeRateFormPanel) {
			exchangeRateFormPanel.assignValues(exchangeRateTablePanel.getItemSelectedId());
		} else if (selectedTab == exchangeRateTablePanel && getTabSheet().isNeedRefresh()) {
			exchangeRateTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
