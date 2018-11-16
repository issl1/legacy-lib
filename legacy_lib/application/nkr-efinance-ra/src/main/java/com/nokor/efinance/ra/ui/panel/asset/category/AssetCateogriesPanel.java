package com.nokor.efinance.ra.ui.panel.asset.category;

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
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AssetCateogriesPanel.NAME)
public class AssetCateogriesPanel extends AbstractTabsheetPanel implements View {
	
	/** */
	private static final long serialVersionUID = -2963062593873628481L;

	public static final String NAME = "asset.categories";
	
	@Autowired
	private AssetCategoryTablePanel categoryTablePanel;
	@Autowired
	private AssetCategoryFormPanel categoryFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		categoryTablePanel.setMainPanel(this);
		categoryFormPanel.setCaption(I18N.message("asset.category"));
		getTabSheet().setTablePanel(categoryTablePanel);
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
		categoryFormPanel.reset();
		getTabSheet().addFormPanel(categoryFormPanel);
		getTabSheet().setSelectedTab(categoryFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(categoryFormPanel);
		initSelectedTab(categoryFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == categoryFormPanel) {
			categoryFormPanel.assignValues(categoryTablePanel.getItemSelectedId());
		} else if (selectedTab == categoryTablePanel && getTabSheet().isNeedRefresh()) {
			categoryTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
