package com.nokor.efinance.ra.ui.panel.quotation;

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
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(QuotationsOldPanel.NAME)
public class QuotationsOldPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -2877873647315525313L;

	public static final String NAME = "quotations.old";
	
	@Autowired
	private QuotationTableOldPanel quotationTableOldPanel;
	@Autowired
	private QuotationFormOldPanel quotationFormOldPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		quotationTableOldPanel.setMainPanel(this);
		quotationFormOldPanel.setQuotationsPanel(this);
		quotationFormOldPanel.setCaption(I18N.message("quotation"));
		getTabSheet().setTablePanel(quotationTableOldPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		quotationFormOldPanel.reset();
		
		getTabSheet().addFormPanel(quotationFormOldPanel);
		getTabSheet().setSelectedTab(quotationFormOldPanel);
	}

	@Override
	public void onEditEventClick() {
		quotationFormOldPanel.reset();
		getTabSheet().addFormPanel(quotationFormOldPanel);
		initSelectedTab(quotationFormOldPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == quotationFormOldPanel) {
			quotationFormOldPanel.assignValues(quotationTableOldPanel.getItemSelectedId());
		} else if (selectedTab == quotationTableOldPanel && getTabSheet().isNeedRefresh()) {
			quotationTableOldPanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * Go to quotation table panel
	 */
	public void displayQuotationTablePanel() {
		quotationTableOldPanel.refresh();
		getTabSheet().setSelectedTab(quotationTableOldPanel);
	}
}
