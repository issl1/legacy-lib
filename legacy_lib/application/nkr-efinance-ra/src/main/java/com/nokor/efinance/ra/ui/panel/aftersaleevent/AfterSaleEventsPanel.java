package com.nokor.efinance.ra.ui.panel.aftersaleevent;

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
@VaadinView(AfterSaleEventsPanel.NAME)
public class AfterSaleEventsPanel extends AbstractTabsheetPanel implements View {

	public static final String NAME = "after.sale.events";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AfterSaleEventsTablePanel afterSalesEventTablePanel;
	@Autowired
	private AfterSaleEventsFormPanel afterSalesEventFormPanel;
	
	@Autowired
	private AfterSaleEventFinProductTablePanel afterSaleEventFinProductTablePanel;
	@Autowired
	private AfterSaleEventProductLineTablePanel afterSaleEventProductLineTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		afterSalesEventTablePanel.setMainPanel(this);
		afterSaleEventFinProductTablePanel.setCaption(I18N.message("financial.product"));
		afterSaleEventProductLineTablePanel.setCaption(I18N.message("product.line"));
		getTabSheet().setTablePanel(afterSalesEventTablePanel);
	}
	

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Override
	public void onAddEventClick() {
		afterSalesEventFormPanel.reset();
		getTabSheet().addFormPanel(afterSalesEventFormPanel);
		getTabSheet().setSelectedTab(afterSalesEventFormPanel);	
	}

	@Override
	public void onEditEventClick() {
		afterSaleEventFinProductTablePanel.assignValues(afterSalesEventTablePanel.getItemSelectedId());
		afterSaleEventProductLineTablePanel.assignValues(afterSalesEventTablePanel.getItemSelectedId());
		getTabSheet().addFormPanel(afterSalesEventFormPanel);
		getTabSheet().addFormPanel(afterSaleEventFinProductTablePanel);
		getTabSheet().addFormPanel(afterSaleEventProductLineTablePanel);
		initSelectedTab(afterSalesEventFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == afterSalesEventFormPanel) {
			afterSalesEventFormPanel.assignValues(afterSalesEventTablePanel.getItemSelectedId());
		} else if (selectedTab == afterSalesEventTablePanel && getTabSheet().isNeedRefresh()) {
			afterSalesEventTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}

}
