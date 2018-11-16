package com.nokor.efinance.ra.ui.panel.blacklist;

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
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BlackListItemsPanel.NAME)
public class BlackListItemsPanel extends AbstractTabsheetPanel implements View {
	
	/** */
	private static final long serialVersionUID = -2368929179255096464L;
	
	public static final String NAME = "blacklist.items";
	
	@Autowired
	private BlackListItemTablePanel blackListItemTablePanel;
	@Autowired
	private BlackListItemFormPanel blackListItemFormPanel;
	
	/**
	 * @return the blackListItemTablePanel
	 */
	public BlackListItemTablePanel getBlackListItemTablePanel() {
		return blackListItemTablePanel;
	}

	@PostConstruct
	public void PostConstruct() {
		super.init();
		blackListItemTablePanel.setMainPanel(this);
		blackListItemFormPanel.setCaption(I18N.message("blacklist.item"));
		getTabSheet().setTablePanel(blackListItemTablePanel);	
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
		blackListItemFormPanel.reset();
		getTabSheet().addFormPanel(blackListItemFormPanel);
		getTabSheet().setSelectedTab(blackListItemFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(blackListItemFormPanel);
		initSelectedTab(blackListItemFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == blackListItemFormPanel) {
			blackListItemFormPanel.assignValues(blackListItemTablePanel.getItemSelectedId());
		} else if (selectedTab == blackListItemTablePanel && getTabSheet().isNeedRefresh()) {
			blackListItemTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
