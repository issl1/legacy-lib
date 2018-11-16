package com.nokor.efinance.gui.ui.panel.collection.searchcontract;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.efinance.gui.ui.panel.contract.ContractFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
/**
 * Search contract panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CollectionSearchContractsPanel.NAME)
public class CollectionSearchContractsPanel extends AbstractTabsheetPanel implements View, AssetEntityField {

	/** */
	private static final long serialVersionUID = -6651024937294153368L;

	public static final String NAME = "search.contract";
	
	@Autowired
	private CollectionSearchContractTablePanel searchContractTablePanel;
	@Autowired
	private ContractFormPanel contractFormPanel;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		searchContractTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(searchContractTablePanel);
		initSelectedTab(searchContractTablePanel);
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
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		contractFormPanel.setCaption(I18N.message("contract"));
		getTabSheet().addFormPanel(contractFormPanel);
		initSelectedTab(contractFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == contractFormPanel) {
			contractFormPanel.assignValues(searchContractTablePanel.getItemSelectedId(), true);
		} else if (selectedTab == searchContractTablePanel && getTabSheet().isNeedRefresh()) {
			searchContractTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
