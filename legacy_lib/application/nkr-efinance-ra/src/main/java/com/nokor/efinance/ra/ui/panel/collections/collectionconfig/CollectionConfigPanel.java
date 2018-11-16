package com.nokor.efinance.ra.ui.panel.collections.collectionconfig;

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
@VaadinView(CollectionConfigPanel.NAME)
public class CollectionConfigPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -681762982976121051L;

	public static final String NAME = "collection.config";
	
	@Autowired
	private CollectionConfigTablePanel collectionConfigTablePanel;
	@Autowired
	private CollectionConfigFormPanel collectionConfigFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		collectionConfigTablePanel.setMainPanel(this);
		collectionConfigFormPanel.setCaption( I18N.message("collection.config"));
		getTabSheet().setTablePanel(collectionConfigTablePanel);		
	}

	@Override
	public void onAddEventClick() {
		collectionConfigFormPanel.reset();
		getTabSheet().addFormPanel(collectionConfigFormPanel);
		getTabSheet().setSelectedTab(collectionConfigFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(collectionConfigFormPanel);
		initSelectedTab(collectionConfigFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == collectionConfigFormPanel) {
			collectionConfigFormPanel.assignValues(collectionConfigTablePanel.getItemSelectedId());
		} else if (selectedTab == collectionConfigTablePanel && getTabSheet().isNeedRefresh()) {
			collectionConfigTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
