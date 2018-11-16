package com.nokor.efinance.gui.ui.panel.quotation.followup;

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
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(QuotationsFollowUpPanel.NAME)
public class QuotationsFollowUpPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 292624950094585022L;

	public static final String NAME = "quotations.follow.up";
	
	@Autowired
	private QuotationFollowUpTablePanel quotationFollowUpTablePanel;
	@Autowired
	private QuotationFollowUpFormPanel quotationFollowUpFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		quotationFollowUpTablePanel.setMainPanel(this);
		quotationFollowUpFormPanel.setCaption(I18N.message("history.status"));
		getTabSheet().setTablePanel(quotationFollowUpTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		getTabSheet().addFormPanel(quotationFollowUpFormPanel);
		getTabSheet().setSelectedTab(quotationFollowUpFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(quotationFollowUpFormPanel);
		initSelectedTab(quotationFollowUpFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == quotationFollowUpFormPanel) {
			quotationFollowUpFormPanel.assignValues(quotationFollowUpTablePanel.getItemSelectedId());
		} else if (selectedTab == quotationFollowUpTablePanel && getTabSheet().isNeedRefresh()) {
			quotationFollowUpFormPanel.assignValues(null);
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
