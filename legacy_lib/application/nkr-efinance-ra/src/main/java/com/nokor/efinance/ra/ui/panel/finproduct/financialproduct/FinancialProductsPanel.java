package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(FinancialProductsPanel.NAME)
public class FinancialProductsPanel extends AbstractTabsheetPanel implements View {
	private static final long serialVersionUID = -5660641653097373781L;

	public static final String NAME = "products";
	
	@Autowired
	private FinancialProductTablePanel financialProductTablePanel;
	
	private FinancialProductFormPanel financialProductFormPanel;
	private FinancialProductServiceTablePanel finProductSrvTablePanel;
	private FinancialProductServiceTablePanel finProductDirectCostSrvTablePanel;
		
	@PostConstruct
	public void PostConstruct() {
		super.init();
		financialProductTablePanel.setMainPanel(this);
		financialProductFormPanel = new FinancialProductFormPanel(this);
		financialProductFormPanel.setCaption(I18N.message("financial.product"));
		finProductSrvTablePanel = new FinancialProductServiceTablePanel("add.service", EServiceType.list());
		finProductSrvTablePanel.setCaption(I18N.message("services"));
		finProductDirectCostSrvTablePanel = new FinancialProductServiceTablePanel("add.direct.cost", EServiceType.listDirectCosts());
		finProductDirectCostSrvTablePanel.setCaption(I18N.message("direct.costs"));
		getTabSheet().setTablePanel(financialProductTablePanel);
		
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
		financialProductFormPanel.reset();
		getTabSheet().addFormPanel(financialProductFormPanel);
		initSelectedTab(financialProductFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(financialProductFormPanel);
		getTabSheet().addFormPanel(finProductSrvTablePanel);
		getTabSheet().addFormPanel(finProductDirectCostSrvTablePanel);
		initSelectedTab(financialProductFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == financialProductFormPanel) {
			financialProductFormPanel.assignValues(financialProductTablePanel.getItemSelectedId());
			finProductSrvTablePanel.assignValues(financialProductTablePanel.getItemSelectedId());
			finProductDirectCostSrvTablePanel.assignValues(financialProductTablePanel.getItemSelectedId());
		} else if (selectedTab == financialProductTablePanel && getTabSheet().isNeedRefresh()) {
			financialProductTablePanel.refresh();
		} 
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	/**
	 * 
	 * @param financialProduct
	 */
	public void displayTabs(FinProduct financialProduct) {
		financialProductFormPanel.assignValues(financialProduct.getId());
		finProductSrvTablePanel.assignValues(financialProduct.getId());
		finProductDirectCostSrvTablePanel.assignValues(financialProduct.getId());
		getTabSheet().addFormPanel(financialProductFormPanel);
		getTabSheet().addFormPanel(finProductSrvTablePanel);
		getTabSheet().addFormPanel(finProductDirectCostSrvTablePanel);
		initSelectedTab(financialProductFormPanel);
	}
}
