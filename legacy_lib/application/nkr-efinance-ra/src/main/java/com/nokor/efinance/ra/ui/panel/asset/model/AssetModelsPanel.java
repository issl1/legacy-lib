package com.nokor.efinance.ra.ui.panel.asset.model;

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
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetModelsPanel.NAME)
public class AssetModelsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -2149707107396177780L;

	public static final String NAME = "models";
		
	@Autowired
	private AssetModelTablePanel assetModelTablePanel;
	@Autowired
	private AssetModelFormPanel assetModelFormPanel;
		
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetModelTablePanel.setMainPanel(this);
		assetModelFormPanel.setCaption( I18N.message("asset.model"));
		getTabSheet().setTablePanel(assetModelTablePanel);		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Override
	public void onAddEventClick() {
		assetModelFormPanel.reset();
		getTabSheet().addFormPanel(assetModelFormPanel);
		getTabSheet().setSelectedTab(assetModelFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetModelFormPanel);
		initSelectedTab(assetModelFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetModelFormPanel) {
			assetModelFormPanel.assignValues(assetModelTablePanel.getItemSelectedId());
		} else if (selectedTab == assetModelTablePanel && getTabSheet().isNeedRefresh()) {
			assetModelTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
