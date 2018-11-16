package com.nokor.efinance.tm.ui.dashboard;

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
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DashboardPanel.NAME)
public class DashboardPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = 8122869268920554816L;

	public static final String NAME = "tm.dashboard";
	
	@Autowired
	private DashboardFormPanel dashboardFormPanel;
	
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		
		getTabSheet().setTablePanel(dashboardFormPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	
	@Override
	public void onAddEventClick() {
		getTabSheet().removeFormsPanel();
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(dashboardFormPanel);
		initSelectedTab(dashboardFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	
}
