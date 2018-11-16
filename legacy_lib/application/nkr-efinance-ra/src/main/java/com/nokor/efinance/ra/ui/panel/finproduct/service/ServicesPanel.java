package com.nokor.efinance.ra.ui.panel.finproduct.service;

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
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ServicesPanel.NAME)
public class ServicesPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -7330166795254236089L;

	public static final String NAME = "services";
	
	@Autowired
	private ServiceTablePanel serviceTablePanel;
	@Autowired
	private ServiceFormPanel serviceFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		serviceTablePanel.setMainPanel(this);
		serviceFormPanel.setCaption(I18N.message("service"));
		getTabSheet().setTablePanel(serviceTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		serviceFormPanel.reset();
		getTabSheet().addFormPanel(serviceFormPanel);
		getTabSheet().setSelectedTab(serviceFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(serviceFormPanel);
		initSelectedTab(serviceFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == serviceFormPanel) {
			serviceFormPanel.assignValues(serviceTablePanel.getItemSelectedId());
		} else if (selectedTab == serviceTablePanel && getTabSheet().isNeedRefresh()) {
			serviceTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
