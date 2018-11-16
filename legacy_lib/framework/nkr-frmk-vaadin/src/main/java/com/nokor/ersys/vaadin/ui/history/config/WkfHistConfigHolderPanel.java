package com.nokor.ersys.vaadin.ui.history.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.history.config.detail.WkfHistConfigFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * Wkf Hist Config Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(WkfHistConfigHolderPanel.NAME)
public class WkfHistConfigHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = -1787157124614585101L;
	
	public static final String NAME = "hist.configs";
	
	@Autowired
	private WkfHistConfigTablePanel tablePanel;
	@Autowired
	private WkfHistConfigFormPanel formPanel;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		initSelectedTab(formPanel);
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
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
