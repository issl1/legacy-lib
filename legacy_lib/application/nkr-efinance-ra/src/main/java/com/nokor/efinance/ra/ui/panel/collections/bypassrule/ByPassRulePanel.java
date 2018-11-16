package com.nokor.efinance.ra.ui.panel.collections.bypassrule;

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
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ByPassRulePanel.NAME)
public class ByPassRulePanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 6519756110864230560L;

	public static final String NAME = "by.pass.rule";
	
	@Autowired
	private ByPassRuleTablePanel byPassRuleTablePanel;
	@Autowired
	private ByPassRuleFormPanel byPassRuleFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		byPassRuleTablePanel.setMainPanel(this);
		byPassRuleFormPanel.setCaption( I18N.message("by.pass.rule"));
		getTabSheet().setTablePanel(byPassRuleTablePanel);		
	}

	@Override
	public void onAddEventClick() {
		byPassRuleFormPanel.reset();
		getTabSheet().addFormPanel(byPassRuleFormPanel);
		getTabSheet().setSelectedTab(byPassRuleFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(byPassRuleFormPanel);
		initSelectedTab(byPassRuleFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == byPassRuleFormPanel) {
			byPassRuleFormPanel.assignValues(byPassRuleTablePanel.getItemSelectedId());
		} else if (selectedTab == byPassRuleTablePanel && getTabSheet().isNeedRefresh()) {
			byPassRuleTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
