package com.nokor.efinance.gui.ui.panel.report.uwrejectreport;

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
 *@author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UWRejectReportPanel.NAME)
public class UWRejectReportPanel  extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = -1929551934421408575L;
	public static final String NAME = "UW.Reject.reports";
	
	@Autowired
	private UWRejectReportTablePanel uwRejectReportTablePanel;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		uwRejectReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(uwRejectReportTablePanel);
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
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		getTabSheet().setSelectedTab(selectedTab);
	}

}
