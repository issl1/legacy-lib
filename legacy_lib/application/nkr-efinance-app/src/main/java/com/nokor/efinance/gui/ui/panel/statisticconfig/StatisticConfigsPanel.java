package com.nokor.efinance.gui.ui.panel.statisticconfig;

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
 * StatisticConfigs Panel
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StatisticConfigsPanel.NAME)
public class StatisticConfigsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 2553573409276658002L;

	public static final String NAME = "statistic.configs";
	
	@Autowired
	private StatisticConfigTablePanel statisticConfigTablePanel;
	@Autowired
	private StatisticConfigFormPanel statisticConfigFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		statisticConfigTablePanel.setMainPanel(this);
		statisticConfigFormPanel.setCaption(I18N.message("statistic.config"));
		getTabSheet().setTablePanel(statisticConfigTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		statisticConfigFormPanel.reset();
		getTabSheet().addFormPanel(statisticConfigFormPanel);
		getTabSheet().setSelectedTab(statisticConfigFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(statisticConfigFormPanel);
		initSelectedTab(statisticConfigFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == statisticConfigFormPanel) {
			statisticConfigFormPanel.assignValues(statisticConfigTablePanel.getItemSelectedId());
		} else if (selectedTab == statisticConfigTablePanel && getTabSheet().isNeedRefresh()) {
			statisticConfigTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
