package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.group;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
/**
 * Group tab panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GroupsPanel extends AbstractTabsheetPanel {

	/** */
	private static final long serialVersionUID = 7514446945089546868L;
	
	@Autowired
	private GroupTablePanel groupTablePanel;
	@Autowired
	private GroupFormPanel groupFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		setMargin(true);
		groupTablePanel.setMainPanel(this);
		groupFormPanel.setCaption(I18N.message("group"));
		getTabSheet().setTablePanel(groupTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		groupFormPanel.reset();
		getTabSheet().addFormPanel(groupFormPanel);
		getTabSheet().setSelectedTab(groupFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(groupFormPanel);
		initSelectedTab(groupFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == groupFormPanel) {
			groupFormPanel.assignValues(groupTablePanel.getItemSelectedId());
		} else if (selectedTab == groupTablePanel && getTabSheet().isNeedRefresh()) {
			groupTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
