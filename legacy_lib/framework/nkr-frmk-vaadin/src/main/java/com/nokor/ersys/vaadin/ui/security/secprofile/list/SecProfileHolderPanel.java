package com.nokor.ersys.vaadin.ui.security.secprofile.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.security.secprofile.detail.SecProfileFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * ProfileHolderPanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SecProfileHolderPanel.NAME)
public class SecProfileHolderPanel extends AbstractTabsheetPanel implements View {
	/**	 */
	private static final long serialVersionUID = 398987855158042850L;

	public static final String NAME = "profiles.list";
	
	@Autowired
	private SecProfileTablePanel profileTablePanel;
	@Autowired
	private SecProfileFormPanel profileFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		profileTablePanel.setMainPanel(this);
		profileFormPanel.setCaption(I18N.message("profile"));
		getTabSheet().setTablePanel(profileTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		profileFormPanel.reset();
		getTabSheet().addFormPanel(profileFormPanel);
		getTabSheet().setSelectedTab(profileFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(profileFormPanel);
		initSelectedTab(profileFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == profileFormPanel) {
			profileFormPanel.assignValues(profileTablePanel.getItemSelectedId());
		} else if (selectedTab == profileTablePanel && getTabSheet().isNeedRefresh()) {
			profileTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
