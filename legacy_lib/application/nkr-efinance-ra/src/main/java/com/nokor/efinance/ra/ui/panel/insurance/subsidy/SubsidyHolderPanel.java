package com.nokor.efinance.ra.ui.panel.insurance.subsidy;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SubsidyHolderPanel.NAME)
public class SubsidyHolderPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -4072350938615110569L;
	
	public static final String NAME = "subsidy";

	@Autowired
	private SubsidyTablePanel subsidyTablePanel;
	
	@Autowired
	private SubsidyFormPanel subsidyFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		subsidyTablePanel.setMainPanel(this);
		subsidyFormPanel.setCaption(I18N.message("subsidy"));
		getTabSheet().setTablePanel(subsidyTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == subsidyFormPanel) {
			subsidyFormPanel.assignValues(subsidyTablePanel.getItemSelectedId());
		} else if (selectedTab == subsidyTablePanel && getTabSheet().isNeedRefresh()) {
			subsidyTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		subsidyFormPanel.reset();
		getTabSheet().addFormPanel(subsidyFormPanel);
		getTabSheet().setSelectedTab(subsidyFormPanel);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(subsidyFormPanel);
		initSelectedTab(subsidyFormPanel);
		
	}

}
