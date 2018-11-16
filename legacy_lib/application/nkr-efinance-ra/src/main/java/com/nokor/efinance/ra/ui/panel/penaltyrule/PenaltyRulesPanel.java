package com.nokor.efinance.ra.ui.panel.penaltyrule;

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
 *  Penalty Rules Panel
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PenaltyRulesPanel.NAME)
public class PenaltyRulesPanel extends AbstractTabsheetPanel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4061639975938523404L;

	public static final String NAME = "penalty.rules";
	
	@Autowired
	private PenaltyRuleTablePanel penaltyRuleTablePanel;
	@Autowired
	private PenaltyRuleFormPanel penaltyRuleFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		penaltyRuleTablePanel.setMainPanel(this);
		penaltyRuleFormPanel.setCaption(I18N.message("penalty.rule"));
		getTabSheet().setTablePanel(penaltyRuleTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		penaltyRuleFormPanel.reset();
		getTabSheet().addFormPanel(penaltyRuleFormPanel);
		getTabSheet().setSelectedTab(penaltyRuleFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(penaltyRuleFormPanel);
		initSelectedTab(penaltyRuleFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == penaltyRuleFormPanel) {
			penaltyRuleFormPanel.assignValues(penaltyRuleTablePanel.getItemSelectedId());
		} else if (selectedTab == penaltyRuleTablePanel && getTabSheet().isNeedRefresh()) {
			penaltyRuleTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
