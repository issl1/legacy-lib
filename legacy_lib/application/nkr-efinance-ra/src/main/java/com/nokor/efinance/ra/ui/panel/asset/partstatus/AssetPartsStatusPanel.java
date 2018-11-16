package com.nokor.efinance.ra.ui.panel.asset.partstatus;

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
 * Asset parts status panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetPartsStatusPanel.NAME)
public class AssetPartsStatusPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 3661194565178092109L;

	public static final String NAME = "asset.parts.status";
	
	@Autowired
	private AssetPartStatusTablePanel assetPartStatusTablePanel;
	@Autowired
	private AssetPartStatusFormPanel assetPartStatusFormPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetPartStatusTablePanel.setMainPanel(this);
		assetPartStatusFormPanel.setCaption(I18N.message("asset.part.status"));
		getTabSheet().setTablePanel(assetPartStatusTablePanel);
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
		assetPartStatusFormPanel.reset();
		getTabSheet().addFormPanel(assetPartStatusFormPanel);
		getTabSheet().setSelectedTab(assetPartStatusFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetPartStatusFormPanel);
		initSelectedTab(assetPartStatusFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetPartStatusFormPanel) {
			assetPartStatusFormPanel.assignValues(assetPartStatusTablePanel.getItemSelectedId());
		} else if (selectedTab == assetPartStatusTablePanel && getTabSheet().isNeedRefresh()) {
			assetPartStatusTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
