package com.nokor.ersys.collab.project.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.collab.project.detail.ProjectFormPanel;
import com.nokor.ersys.collab.project.employee.ProjectAssigneeTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * Project Holder Panel
 * @author bunlong.taing
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProjectHolderPanel.NAME)
public class ProjectHolderPanel extends AbstractTabsheetPanel implements View {

	/**  */
	private static final long serialVersionUID = 7491639743794993553L;
	
	public static final String NAME = "projects";
	
	@Autowired
	private ProjectTablePanel tablePanel;
	@Autowired
	private ProjectFormPanel formPanel;
	@Autowired
	private ProjectAssigneeTablePanel assigneeTablePanel;
	
	/**
	 * Assignment Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		formPanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("detail"));
		assigneeTablePanel.setCaption(I18N.message("assignees"));
		getTabSheet().setTablePanel(tablePanel);
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
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		getTabSheet().setSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(formPanel);
		addSubTab(tablePanel.getItemSelectedId());
		initSelectedTab(formPanel);
	}
	
	/**
	 * Add Sub Tab
	 * @param selectedId
	 */
	public void addSubTab(Long selectedId) {
		assigneeTablePanel.assignValuesToControls(selectedId);
		getTabSheet().addFormPanel(assigneeTablePanel);
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

}
