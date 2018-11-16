/*
 * Created on 29/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.setting.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.vaadin.ui.referential.setting.detail.SettingConfigFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * SettingConfig Holder Panel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BaseSettingConfigHolderPanel.NAME)
public class BaseSettingConfigHolderPanel extends AbstractTabsheetPanel implements View {

	/**	 */
	private static final long serialVersionUID = 207829674347255363L;
	
	public static final String NAME = "settings.list";
	
	@Autowired
	private SettingConfigTablePanel settingTablePanel;
	@Autowired
	private SettingConfigFormPanel settingFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		settingTablePanel.setMainPanel(this);
		settingFormPanel.setCaption(I18N.message("advanced.configuration"));
		getTabSheet().setTablePanel(settingTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == settingFormPanel) {
			settingFormPanel.assignValues(settingTablePanel.getItemSelectedId());
		} else if (selectedTab == settingTablePanel && getTabSheet().isNeedRefresh()) {
			settingTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	@Override
	public void onAddEventClick() {
		getTabSheet().addFormPanel(settingFormPanel);
		initSelectedTab(settingFormPanel);
	}
	
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(settingFormPanel);
		initSelectedTab(settingFormPanel);
		
	}
}
