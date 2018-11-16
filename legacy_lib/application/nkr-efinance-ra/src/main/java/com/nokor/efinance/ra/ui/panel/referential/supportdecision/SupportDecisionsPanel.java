package com.nokor.efinance.ra.ui.panel.referential.supportdecision;

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
 * Relationship panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SupportDecisionsPanel.NAME)
public class SupportDecisionsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -483026454924464487L;

	public static final String NAME = "support.decisions";
	
	@Autowired
	private SupportDecisionTablePanel supportdecisionTablePanel;
	@Autowired
	private SupportDecisionFormPanel supportdecisionFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		supportdecisionTablePanel.setMainPanel(this);
		supportdecisionFormPanel.setCaption(I18N.message("support.decision"));
		getTabSheet().setTablePanel(supportdecisionTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		supportdecisionFormPanel.reset();
		getTabSheet().addFormPanel(supportdecisionFormPanel);
		getTabSheet().setSelectedTab(supportdecisionFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(supportdecisionFormPanel);
		initSelectedTab(supportdecisionFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == supportdecisionFormPanel) {
			supportdecisionFormPanel.assignValues(supportdecisionTablePanel.getItemSelectedId());
		} else if (selectedTab == supportdecisionTablePanel && getTabSheet().isNeedRefresh()) {
			supportdecisionTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
