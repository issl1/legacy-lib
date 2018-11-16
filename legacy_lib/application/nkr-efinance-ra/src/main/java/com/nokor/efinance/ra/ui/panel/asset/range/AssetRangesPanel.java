package com.nokor.efinance.ra.ui.panel.asset.range;

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
@VaadinView(AssetRangesPanel.NAME)
public class AssetRangesPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -2885950537826618635L;

	public static final String NAME = "ranges";
	
	@Autowired
	private AssetRangeTablePanel assetRangeTablePanel;
	@Autowired
	private AssetRangeFormPanel assetRangeFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		assetRangeTablePanel.setMainPanel(this);
		assetRangeFormPanel.setCaption(I18N.message("asset.range"));
		assetRangeFormPanel.setMainPanel(this);
		getTabSheet().setTablePanel(assetRangeTablePanel);
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
		assetRangeFormPanel.reset();
		getTabSheet().addFormPanel(assetRangeFormPanel);
		getTabSheet().setSelectedTab(assetRangeFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(assetRangeFormPanel);
		initSelectedTab(assetRangeFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == assetRangeFormPanel) {
			assetRangeFormPanel.assignValues(assetRangeTablePanel.getItemSelectedId());
		} else if (selectedTab == assetRangeTablePanel && getTabSheet().isNeedRefresh()) {
			assetRangeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
