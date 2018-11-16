package com.nokor.efinance.ra.ui.panel.asset.make;

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
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetMakesPanel.NAME)
public class AssetMakesPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -2983055467054135680L;
	
	public static final String NAME = "brands";
	
	@Autowired
	private AssetMakeTablePanel assetMakeTablePanel;
	@Autowired
	private AssetMakeFormPanel assetMakeFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetMakeTablePanel.setMainPanel(this);
		assetMakeFormPanel.setCaption(I18N.message("asset.make"));
		assetMakeFormPanel.setMainPanel(this);
		getTabSheet().setTablePanel(assetMakeTablePanel);
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
		assetMakeFormPanel.reset();
		getTabSheet().addFormPanel(assetMakeFormPanel);
		getTabSheet().setSelectedTab(assetMakeFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetMakeFormPanel);
		initSelectedTab(assetMakeFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetMakeFormPanel) {
			assetMakeFormPanel.assignValues(assetMakeTablePanel.getItemSelectedId());
		} else if (selectedTab == assetMakeTablePanel && getTabSheet().isNeedRefresh()) {
			assetMakeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
