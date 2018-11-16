/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.commune.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.referential.address.commune.detail.CommuneFormPanel;
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
public class BaseCommuneHolderPanel extends AbstractTabsheetPanel implements View{
	/**	 */
	private static final long serialVersionUID = -1571773947604016321L;

	@Autowired
	private CommuneTablePanel communeTablePanel;
	@Autowired
	private CommuneFormPanel communeFormPanel;
	@PostConstruct
	public void PostConstruct() {
		super.init();
		communeTablePanel.setMainPanel(this);
		communeFormPanel.setCaption(I18N.message("commune"));
		getTabSheet().setTablePanel(communeTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddEventClick() {
		communeFormPanel.reset();
		getTabSheet().addFormPanel(communeFormPanel);
		getTabSheet().setSelectedTab(communeFormPanel);		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(communeFormPanel);
		initSelectedTab(communeFormPanel);
		
	}

	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == communeFormPanel) {
			communeFormPanel.assignValues(communeTablePanel.getItemSelectedId());
		} else if (selectedTab == communeTablePanel && getTabSheet().isNeedRefresh()) {
			communeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}
}
