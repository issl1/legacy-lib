/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.zone.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.zone.detail.ZoneFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BaseZoneHolderPanel extends AbstractTabsheetPanel implements View {

	/**	 */
	private static final long serialVersionUID = -7028951068791266224L;
	
	@Autowired
	private ZoneTablePanel zoneTablePanel;
	@Autowired
	private ZoneFormPanel zoneFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		zoneTablePanel.setMainPanel(this);
		zoneFormPanel.setCaption(I18N.message("zone"));
		getTabSheet().setTablePanel(zoneTablePanel);
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddEventClick() {
		zoneFormPanel.reset();
		getTabSheet().addFormPanel(zoneFormPanel);
		getTabSheet().setSelectedTab(zoneFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(zoneFormPanel);
		initSelectedTab(zoneFormPanel);
		
	}

	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == zoneFormPanel) {
			zoneFormPanel.assignValues(zoneTablePanel.getItemSelectedId());
		} else if (selectedTab == zoneTablePanel && getTabSheet().isNeedRefresh()) {
			zoneTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);		
	}
}
