package com.nokor.efinance.ra.ui.panel.asset.exteriorstatus;

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
 * Asset exteriors status panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetExteriorsStatusPanel.NAME)
public class AssetExteriorsStatusPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -1774109222056815656L;

	public static final String NAME = "asset.exteriors.status";
	
	@Autowired
	private AssetExteriorStatusTablePanel assetExteriorStatusTablePanel;
	@Autowired
	private AssetExteriorStatusFormPanel assetExteriorStatusFormPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetExteriorStatusTablePanel.setMainPanel(this);
		assetExteriorStatusFormPanel.setCaption(I18N.message("asset.exterior.status"));
		getTabSheet().setTablePanel(assetExteriorStatusTablePanel);
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
		assetExteriorStatusFormPanel.reset();
		getTabSheet().addFormPanel(assetExteriorStatusFormPanel);
		getTabSheet().setSelectedTab(assetExteriorStatusFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetExteriorStatusFormPanel);
		initSelectedTab(assetExteriorStatusFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetExteriorStatusFormPanel) {
			assetExteriorStatusFormPanel.assignValues(assetExteriorStatusTablePanel.getItemSelectedId());
		} else if (selectedTab == assetExteriorStatusTablePanel && getTabSheet().isNeedRefresh()) {
			assetExteriorStatusTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
