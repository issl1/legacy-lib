package com.nokor.efinance.ra.ui.panel.referential.policestation;

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
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PoliceStationPanel.NAME)
public class PoliceStationPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -1305078050015726715L;
	public static final String NAME = "police.stations";

	@Autowired
	private PoliceStationTablePanel policeStationTablePanel;
	@Autowired
	private PoliceStationFormPanel policeStationFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		policeStationTablePanel.setMainPanel(this);
		policeStationFormPanel.setCaption(I18N.message("police.station"));
		getTabSheet().setTablePanel(policeStationTablePanel);
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
		policeStationFormPanel.reset();
		getTabSheet().addFormPanel(policeStationFormPanel);
		getTabSheet().setSelectedTab(policeStationFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		policeStationFormPanel.reset();
		getTabSheet().addFormPanel(policeStationFormPanel);
		initSelectedTab(policeStationFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == policeStationFormPanel) {
			policeStationFormPanel.assignValues(policeStationTablePanel.getItemSelectedId());
		} else if (selectedTab == policeStationTablePanel && getTabSheet().isNeedRefresh()) {
			policeStationTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
}
