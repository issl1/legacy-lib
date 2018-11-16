package com.nokor.efinance.gui.ui.panel.accounting.accounts;

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
@VaadinView(AccountsHolderPanel.NAME)
public class AccountsHolderPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -4564660973199571608L;

	public static final String NAME = "account.trees";
	
	@Autowired
	private AccountsTablePanel accountTablePanel;
	
	@Autowired
	private AccountsFormPanel accountFormPanel;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		accountTablePanel.setMainPanel(this);
		accountFormPanel.setCaption(I18N.message("payment.code"));
		getTabSheet().setTablePanel(accountTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == accountFormPanel) {
			accountFormPanel.assignValues(accountTablePanel.getItemSelectedId());
		} else if (selectedTab == accountTablePanel && getTabSheet().isNeedRefresh()) {
			accountTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		accountFormPanel.reset();
		getTabSheet().addFormPanel(accountFormPanel);
		getTabSheet().setSelectedTab(accountFormPanel);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(accountFormPanel);
		initSelectedTab(accountFormPanel);
		
	}

}
