package com.nokor.efinance.gui.ui.panel.report.collectionincentive;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Collection Incentive Report panel
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CollectionIncentivePanel.NAME)
public class CollectionIncentivePanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -7397440003540163018L;
	public static final String NAME = "collection.incentive.report";
	
	@Autowired
	private CollectionIncentiveTablePanel collectionIncentiveTablePanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		collectionIncentiveTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(collectionIncentiveTablePanel);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		getTabSheet().setSelectedTab(selectedTab);
	}

	@Override
	public void onAddEventClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditEventClick() {
		// TODO Auto-generated method stub
		
	}
}
