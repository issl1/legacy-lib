package com.nokor.efinance.core.quotation.panel;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationsPanel extends AbstractTabsheetPanel implements View, AssetEntityField {

	private static final long serialVersionUID = -6651024937294153368L;

	public static final String NAME = "quotations";
	
	@Autowired
	private QuotationTablePanel quotationTablePanel;
	@Autowired
//	private QuotationFormPanel quotationFormPanel;
	
	/**
	 * @return the quotationFormPanel
	 */
//	public QuotationFormPanel getQuotationFormPanel() {
//		return quotationFormPanel;
//	}
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		quotationTablePanel.setMainPanel(this);
		
//		quotationFormPanel.setQuotationsPanel(this);
		getTabSheet().setTablePanel(quotationTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String quotaId = event.getParameters();
		if (StringUtils.isNotEmpty(quotaId)) {
			/*getTabSheet().addFormPanel(quotationFormPanel);
			quotationFormPanel.assignValues(new Long(quotaId), true);
			getTabSheet().setForceSelected(true);
			getTabSheet().setSelectedTab(quotationFormPanel);*/
		}
	}
	
	
	@Override
	public void onAddEventClick() {
		/*quotationFormPanel.reset();
		getTabSheet().addFormPanel(quotationFormPanel);
		getTabSheet().setSelectedTab(quotationFormPanel);*/
	}

	@Override
	public void onEditEventClick() {		
		/*getTabSheet().addFormPanel(quotationFormPanel);
		initSelectedTab(quotationFormPanel);*/
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		/*if (selectedTab == quotationFormPanel) {
			quotationFormPanel.assignValues(quotationTablePanel.getItemSelectedId(), true);
		} else if (selectedTab == quotationTablePanel && getTabSheet().isNeedRefresh()) {
			quotationTablePanel.refresh();
		}*/
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	/**
	 * Go to quotation table panel
	 */
	public void displayQuotationTablePanel() {
		quotationTablePanel.refresh();
		getTabSheet().setSelectedTab(quotationTablePanel);
	}
	
	/**
	 * Go to quotation form to add a new quotation
	 * @param quotation
	 */
	public void createQuotation(Quotation quotation) {
//		quotationFormPanel.assignValues(quotation, true);
//		getTabSheet().addFormPanel(quotationFormPanel);
//		getTabSheet().setSelectedTab(quotationFormPanel);
	}
	
}
