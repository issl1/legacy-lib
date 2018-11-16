package com.nokor.efinance.gui.ui.panel.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ScanContractsPanel.NAME)
public class ScanContractsPanel extends VerticalLayout implements View {

	/** */
	private static final long serialVersionUID = -2714567564972238316L;

	public static final String NAME = "scan.contracts";
	
	private InboxNewPanel inboxNewPanel;
	
		
	@PostConstruct
	public void PostConstruct() {
		Panel contentPanel = new Panel();
		inboxNewPanel = new InboxNewPanel();
		contentPanel.setContent(inboxNewPanel);
		
		addComponent(contentPanel);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		inboxNewPanel.assignValues();
	}
}
