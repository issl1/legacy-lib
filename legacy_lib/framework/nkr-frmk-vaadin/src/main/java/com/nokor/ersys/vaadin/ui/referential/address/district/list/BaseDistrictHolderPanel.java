/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.district.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.district.detail.DistrictFormPanel;
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
public class BaseDistrictHolderPanel extends AbstractTabsheetPanel implements View {
	/**	 */
	private static final long serialVersionUID = 8127451346343062936L;

	@Autowired
	private DistrictTablePanel districtTablePanel;
	@Autowired
	private DistrictFormPanel districtFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		districtTablePanel.setMainPanel(this);
		districtFormPanel.setCaption(I18N.message("district"));
		getTabSheet().setTablePanel(districtTablePanel);
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddEventClick() {
		districtFormPanel.reset();
		getTabSheet().addFormPanel(districtFormPanel);
		getTabSheet().setSelectedTab(districtFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(districtFormPanel);
		initSelectedTab(districtFormPanel);
		
	}

	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == districtFormPanel) {
			districtFormPanel.assignValues(districtTablePanel.getItemSelectedId());
		} else if (selectedTab == districtTablePanel && getTabSheet().isNeedRefresh()) {
			districtTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);		
	}
}
