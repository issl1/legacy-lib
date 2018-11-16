package com.nokor.efinance.ra.ui.panel.collections.parameter.status;

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
 * Status panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StatusesPanel.NAME)
public class StatusesPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -4647438581720333450L;

	public static final String NAME = "status.templates";
	
	@Autowired
	private StatusTablePanel statusTablePanel;
	@Autowired
	private StatusFormPanel statusFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		statusTablePanel.setMainPanel(this);
		statusFormPanel.setCaption(I18N.message("status.template"));
		getTabSheet().setTablePanel(statusTablePanel);
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
		statusFormPanel.reset();
		getTabSheet().addFormPanel(statusFormPanel);
		getTabSheet().setSelectedTab(statusFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(statusFormPanel);
		initSelectedTab(statusFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == statusFormPanel) {
			statusFormPanel.assignValues(statusTablePanel.getItemSelectedId());
		} else if (selectedTab == statusTablePanel && getTabSheet().isNeedRefresh()) {
			statusTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}