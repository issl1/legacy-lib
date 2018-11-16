package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UWPerformancePanel.NAME)
public class UWPerformancePanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 3391041271200057568L;
	
	public static final String NAME = "uw.performance.report";
	
	@Autowired
	private UWPerformanceTablePanel UWPerformanceTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		UWPerformanceTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(UWPerformanceTablePanel);	
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		getTabSheet().setSelectedTab(selectedTab);
	}

	@Override
	public void onAddEventClick() {
		
	}

	@Override
	public void onEditEventClick() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
