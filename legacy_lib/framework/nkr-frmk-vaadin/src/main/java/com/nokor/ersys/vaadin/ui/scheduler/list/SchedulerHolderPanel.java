package com.nokor.ersys.vaadin.ui.scheduler.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.scheduler.detail.SchedulerFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SchedulerHolderPanel.NAME)
public class SchedulerHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = 8018597119229330055L;

	public static final String NAME = "task.schedulers";
	
	@Autowired
	private SchedulerListPanel schedulerListPanel;	
	@Autowired
	private SchedulerFormPanel schedulerFormPanel;	
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		
		schedulerListPanel.setMainPanel(this);
		getTabSheet().setTablePanel(schedulerListPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		schedulerFormPanel.reset();
		getTabSheet().addFormPanel(schedulerFormPanel);
		getTabSheet().setSelectedTab(schedulerFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(schedulerFormPanel);
		initSelectedTab(schedulerFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == schedulerFormPanel) {
			schedulerFormPanel.assignValues(schedulerListPanel.getItemSelectedId());
		} else if (selectedTab == schedulerListPanel && getTabSheet().isNeedRefresh()) {
			schedulerListPanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	
}
