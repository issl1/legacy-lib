package com.nokor.ersys.vaadin.ui.security;

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
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DefaultPanel.NAME)
public class DefaultPanel extends AbstractTabsheetPanel implements View {

	/**	 */
	private static final long serialVersionUID = -420394438091198471L;

	public static final String NAME = "default";
	
	@Autowired
	protected DefaultFormPanel defaultFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		getTabSheet().setTablePanel(defaultFormPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {		
	}
	
	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {		
	}
	
}