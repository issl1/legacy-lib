package com.nokor.efinance.ra.ui.panel.finproduct.creditcontrol;

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
@VaadinView(CreditControlsPanel.NAME)
public class CreditControlsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -1305078050015726715L;
	public static final String NAME = "credit.controls";

	@Autowired
	private CreditControlTablePanel creditControlTablePanel;
	@Autowired
	private CreditControlsFormPanel creditControlsFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		creditControlTablePanel.setMainPanel(this);
		creditControlsFormPanel.setCaption(I18N.message("credit.control"));
		getTabSheet().setTablePanel(creditControlTablePanel);
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
		creditControlsFormPanel.reset();
		getTabSheet().addFormPanel(creditControlsFormPanel);
		getTabSheet().setSelectedTab(creditControlsFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		creditControlsFormPanel.reset();
		getTabSheet().addFormPanel(creditControlsFormPanel);
		initSelectedTab(creditControlsFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == creditControlsFormPanel) {
			creditControlsFormPanel.assignValues(creditControlTablePanel.getItemSelectedId());
		} else if (selectedTab == creditControlTablePanel && getTabSheet().isNeedRefresh()) {
			creditControlTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
}
