package com.nokor.efinance.ra.ui.panel.collections.smstemplate;

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
@VaadinView(SMSTemplatePanel.NAME)
public class SMSTemplatePanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -1305078050015726715L;
	public static final String NAME = "sms.templates";

	@Autowired
	private SMSTemplateTablePanel smsTemplateTablePanel;
	@Autowired
	private SMSTemplateFormPanel smsTeplateFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		smsTemplateTablePanel.setMainPanel(this);
		smsTeplateFormPanel.setCaption(I18N.message("sms.template"));
		getTabSheet().setTablePanel(smsTemplateTablePanel);
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
		smsTeplateFormPanel.reset();
		getTabSheet().addFormPanel(smsTeplateFormPanel);
		getTabSheet().setSelectedTab(smsTeplateFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		smsTeplateFormPanel.reset();
		getTabSheet().addFormPanel(smsTeplateFormPanel);
		initSelectedTab(smsTeplateFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == smsTeplateFormPanel) {
			smsTeplateFormPanel.assignValues(smsTemplateTablePanel.getItemSelectedId());
		} else if (selectedTab == smsTemplateTablePanel && getTabSheet().isNeedRefresh()) {
			smsTemplateTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
}
