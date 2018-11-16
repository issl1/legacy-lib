/*
 * Created on 30/06/2015.
 */
package com.nokor.ersys.vaadin.ui.history;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.history.item.WkfHistoryItemTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * History Holder Panel.
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WkfHistoryHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = 8924520282946018229L;

	public static final String NAME = "histories";
	
	@Autowired
	protected WkfHistoryTablePanel tablePanel;
	@Autowired
	protected WkfHistoryItemTablePanel itemTablePanel;
	
	@Override
	public void enter(ViewChangeEvent event) {}

	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		itemTablePanel.setCaption(I18N.message("history.items"));
		getTabSheet().setTablePanel(tablePanel);
	}
	
	@Override
	public void onAddEventClick() {

	}

	@Override
	public void onEditEventClick() {
		itemTablePanel.assignValues(tablePanel.getItemSelectedId(), tablePanel.getCustomEntity());
		getTabSheet().addFormPanel(itemTablePanel);
		initSelectedTab(itemTablePanel);
	}

	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		} 
		getTabSheet().setSelectedTab(selectedTab);
	}
	
}
