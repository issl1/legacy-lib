package com.nokor.ersys.vaadin.ui.security.secuser.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.security.secuser.detail.SecUserFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * UserHolderPanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SecUserHolderPanel.NAME)
public class SecUserHolderPanel extends AbstractTabsheetPanel implements View {
	
	/**	 */
	private static final long serialVersionUID = -572363915380354697L;

	public static final String NAME = "users.list";
	
	@Autowired
	protected SecUserTablePanel userTablePanel;
	@Autowired
	protected SecUserFormPanel userFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		userTablePanel.setMainPanel(this);
		userFormPanel.setCaption(I18N.message("user"));
		userFormPanel.setMainPanel(this);
		getTabSheet().setTablePanel(userTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		userFormPanel.reset();
		getTabSheet().addFormPanel(userFormPanel);
		getTabSheet().setSelectedTab(userFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(userFormPanel);
		initSelectedTab(userFormPanel);
	}
	
	public void addSubTab(Long selectedId) {
		// Allowed to override
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == userFormPanel) {
			userFormPanel.assignValues(userTablePanel.getItemSelectedId());
		} else if (selectedTab == userTablePanel && getTabSheet().isNeedRefresh()) {
			userTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
