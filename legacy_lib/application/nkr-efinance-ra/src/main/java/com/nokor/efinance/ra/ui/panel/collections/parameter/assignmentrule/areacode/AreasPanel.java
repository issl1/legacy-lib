package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.areacode;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
/**
 * Area code tab panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreasPanel extends AbstractTabsheetPanel {
	
	/** */
	private static final long serialVersionUID = -3862172769967646854L;
	
	@Autowired
	private AreaTablePanel areaTablePanel;
	@Autowired
	private AreaFormPanel areaFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		setMargin(true);
		areaTablePanel.setMainPanel(this);
		areaFormPanel.setCaption(I18N.message("area"));
		getTabSheet().setTablePanel(areaTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		areaFormPanel.reset();
		getTabSheet().addFormPanel(areaFormPanel);
		getTabSheet().setSelectedTab(areaFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(areaFormPanel);
		initSelectedTab(areaFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == areaFormPanel) {
			areaFormPanel.assignValues(areaTablePanel.getItemSelectedId());
		} else if (selectedTab == areaTablePanel && getTabSheet().isNeedRefresh()) {
			areaTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
