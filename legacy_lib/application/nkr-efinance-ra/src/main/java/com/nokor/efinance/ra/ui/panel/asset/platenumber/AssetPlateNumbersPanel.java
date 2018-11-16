package com.nokor.efinance.ra.ui.panel.asset.platenumber;

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
 * Asset Plate Number panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetPlateNumbersPanel.NAME)
public class AssetPlateNumbersPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 1240793379808526559L;

	public static final String NAME = "asset.plate.number";
	
	@Autowired
	private AssetPlateNumberTablePanel assetPlateNumberTablePanel;
	@Autowired
	private AssetPlateNumberFormPanel assetPlateNumberFormPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetPlateNumberTablePanel.setMainPanel(this);
		assetPlateNumberFormPanel.setCaption(I18N.message("asset.plate.number"));
		getTabSheet().setTablePanel(assetPlateNumberTablePanel);
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
		assetPlateNumberFormPanel.reset();
		getTabSheet().addFormPanel(assetPlateNumberFormPanel);
		getTabSheet().setSelectedTab(assetPlateNumberFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetPlateNumberFormPanel);
		initSelectedTab(assetPlateNumberFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetPlateNumberFormPanel) {
			assetPlateNumberFormPanel.assignValues(assetPlateNumberTablePanel.getItemSelectedId());
		} else if (selectedTab == assetPlateNumberTablePanel && getTabSheet().isNeedRefresh()) {
			assetPlateNumberTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
