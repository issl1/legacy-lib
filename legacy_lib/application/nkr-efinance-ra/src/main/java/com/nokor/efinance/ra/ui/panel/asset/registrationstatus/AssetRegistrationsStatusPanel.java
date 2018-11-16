package com.nokor.efinance.ra.ui.panel.asset.registrationstatus;

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
 * Asset registrations status panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetRegistrationsStatusPanel.NAME)
public class AssetRegistrationsStatusPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 1240793379808526559L;

	public static final String NAME = "asset.registrations.status";
	
	@Autowired
	private AssetRegistrationStatusTablePanel assetRegistrationStatusTablePanel;
	@Autowired
	private AssetRegistrationStatusFormPanel assetRegistrationStatusFormPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetRegistrationStatusTablePanel.setMainPanel(this);
		assetRegistrationStatusFormPanel.setCaption(I18N.message("asset.registration.status"));
		getTabSheet().setTablePanel(assetRegistrationStatusTablePanel);
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
		assetRegistrationStatusFormPanel.reset();
		getTabSheet().addFormPanel(assetRegistrationStatusFormPanel);
		getTabSheet().setSelectedTab(assetRegistrationStatusFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetRegistrationStatusFormPanel);
		initSelectedTab(assetRegistrationStatusFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetRegistrationStatusFormPanel) {
			assetRegistrationStatusFormPanel.assignValues(assetRegistrationStatusTablePanel.getItemSelectedId());
		} else if (selectedTab == assetRegistrationStatusTablePanel && getTabSheet().isNeedRefresh()) {
			assetRegistrationStatusTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
