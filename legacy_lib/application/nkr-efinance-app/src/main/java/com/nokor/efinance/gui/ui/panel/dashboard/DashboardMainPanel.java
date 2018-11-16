package com.nokor.efinance.gui.ui.panel.dashboard;

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
 * Dash board Main Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DashboardMainPanel.NAME)
public class DashboardMainPanel  extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 3040853803702532013L;
	public static final String NAME = "EFINANCE_APP.dashboard";
	
	@Autowired
	private DashboardOldPanel dashboardPanel;
	@Autowired
	private DashboardMainFormPanel formPanel;
	
	/**
	 * Post Constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		dashboardPanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("overdue.contract"));
		getTabSheet().setTablePanel(dashboardPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(formPanel);
		initSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(dashboardPanel.getItemSelectedId());
		}/* else if (selectedTab == dashboardPanel && getTabSheet().isNeedRefresh()) {
			dashboardPanel.refresh();
		}*/
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
