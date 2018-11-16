package com.nokor.efinance.ra.ui.panel.insurance.compensation.reposession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

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
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CompensationReposessionHolderPanel.NAME)
public class CompensationReposessionHolderPanel extends AbstractTabsheetPanel implements View {
	
	/** */
	private static final long serialVersionUID = 4002159156924016536L;

	public static final String NAME = "compensation.reposession";

	@Autowired
	private CompensationReposessionTablePanel compensationReposessionTablePanel;
	
	@Autowired
	private CompensationReposessionFormPanel compensationReposessionFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		compensationReposessionTablePanel.setMainPanel(this);
		compensationReposessionFormPanel.setCaption(I18N.message("compensation.reposession"));
		getTabSheet().setTablePanel(compensationReposessionTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == compensationReposessionFormPanel) {
			compensationReposessionFormPanel.assignValues(compensationReposessionTablePanel.getItemSelectedId());
		} else if (selectedTab == compensationReposessionTablePanel && getTabSheet().isNeedRefresh()) {
			compensationReposessionTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		compensationReposessionFormPanel.reset();
		getTabSheet().addFormPanel(compensationReposessionFormPanel);
		getTabSheet().setSelectedTab(compensationReposessionFormPanel);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(compensationReposessionFormPanel);
		initSelectedTab(compensationReposessionFormPanel);
		
	}

}
