/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.village.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.village.detail.VillageFormPanel;
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
public class BaseVillageHolderPanel extends AbstractTabsheetPanel implements View {
	/**	 */
	private static final long serialVersionUID = 6743532528854231182L;
	
	@Autowired
	private VillageTablePanel villageTablePanel;
	@Autowired
	private VillageFormPanel villageFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		villageTablePanel.setMainPanel(this);
		villageFormPanel.setCaption(I18N.message("village"));
		getTabSheet().setTablePanel(villageTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		villageFormPanel.reset();
		getTabSheet().addFormPanel(villageFormPanel);
		getTabSheet().setSelectedTab(villageFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(villageFormPanel);
		initSelectedTab(villageFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == villageFormPanel) {
			villageFormPanel.assignValues(villageTablePanel.getItemSelectedId());
		} else if (selectedTab == villageTablePanel && getTabSheet().isNeedRefresh()) {
			villageTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

}
