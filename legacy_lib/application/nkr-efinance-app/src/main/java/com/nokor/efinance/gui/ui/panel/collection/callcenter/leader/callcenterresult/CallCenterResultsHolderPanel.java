package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.callcenterresult;

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
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CallCenterResultsHolderPanel.NAME)
public class CallCenterResultsHolderPanel extends AbstractTabsheetPanel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3554787070786374024L;
	
	public static final String NAME = "call.center.result";
	
	@Autowired
	private CallCenterResultsTablePanel callCenterResultsTablePanel;
	
	@Autowired
	private CallCenterResultsFormPanel callCenterResultsFormPanel;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		callCenterResultsTablePanel.setMainPanel(this);
		callCenterResultsFormPanel.setCaption(I18N.message("result"));
		getTabSheet().setTablePanel(callCenterResultsTablePanel);
	}

	@Override
	public void onAddEventClick() {
		callCenterResultsFormPanel.reset();
		getTabSheet().addFormPanel(callCenterResultsFormPanel);
		getTabSheet().setSelectedTab(callCenterResultsFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		callCenterResultsFormPanel.reset();
		getTabSheet().addFormPanel(callCenterResultsFormPanel);
		initSelectedTab(callCenterResultsFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == callCenterResultsFormPanel) {
			callCenterResultsFormPanel.assignValue(callCenterResultsTablePanel.getItemSelectedId());
		} else if (selectedTab == callCenterResultsTablePanel && getTabSheet().isNeedRefresh()) {
			callCenterResultsTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
