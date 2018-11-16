package com.nokor.efinance.ra.ui.panel.collections.emailtemplate;

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
 * Email Templates Panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(EmailTemplatePanel.NAME)
public class EmailTemplatePanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 8890001928473497914L;

	public static final String NAME = "email.templates";

	@Autowired
	private EmailTemplateTablePanel emailTemplateTablePanel;
	@Autowired
	private EmailTemplateFormPanel emailTemplateFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		emailTemplateTablePanel.setMainPanel(this);
		emailTemplateFormPanel.setCaption(I18N.message("email.template"));
		getTabSheet().setTablePanel(emailTemplateTablePanel);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		emailTemplateFormPanel.reset();
		getTabSheet().addFormPanel(emailTemplateFormPanel);
		getTabSheet().setSelectedTab(emailTemplateFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		emailTemplateFormPanel.reset();
		getTabSheet().addFormPanel(emailTemplateFormPanel);
		initSelectedTab(emailTemplateFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == emailTemplateFormPanel) {
			emailTemplateFormPanel.assignValues(emailTemplateTablePanel.getItemSelectedId());
		} else if (selectedTab == emailTemplateTablePanel && getTabSheet().isNeedRefresh()) {
			emailTemplateTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
}
