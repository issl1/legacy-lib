package com.nokor.efinance.ra.ui.panel.collections.storagelocation;

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
 * Collection Status panel
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StorageLocationsPanel.NAME)
public class StorageLocationsPanel extends AbstractTabsheetPanel implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 648458290573592850L;

	public static final String NAME = "storage.locations";
	
	@Autowired
	private StorageLocationTablePanel storageLocationTablePanel;
	@Autowired
	private StorageLocationFormPanel storageLocationFormPanel;

	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		storageLocationTablePanel.setMainPanel(this);
		storageLocationFormPanel.setCaption(I18N.message("storage.location"));
		getTabSheet().setTablePanel(storageLocationTablePanel);
	}
	
	/** */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == storageLocationFormPanel) {
			storageLocationFormPanel.assignValues(storageLocationTablePanel.getItemSelectedId());
		} else if (selectedTab == storageLocationTablePanel && getTabSheet().isNeedRefresh()) {
			storageLocationTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		storageLocationFormPanel.reset();
		getTabSheet().addFormPanel(storageLocationFormPanel);
		getTabSheet().setSelectedTab(storageLocationFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(storageLocationFormPanel);
		initSelectedTab(storageLocationFormPanel);
	}

}
