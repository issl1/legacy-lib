package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.list;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.gui.ui.panel.accounting.journal.entry.detail.TransactionEntryFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

import ru.xpoft.vaadin.VaadinView;

/**
 * JournalEntry Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(TransactionEntryHolderPanel.NAME)
public class TransactionEntryHolderPanel extends AbstractTabsheetPanel implements View {
	
	/***/
	private static final long serialVersionUID = -248884734610937869L;

	public static final String NAME = "transaction.entries";
	
	private TransactionEntryTablePanel tablePanel;	
	private TransactionEntryFormPanel formPanel;


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
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		initSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		super.init();
		formPanel = new TransactionEntryFormPanel();
		tablePanel = new TransactionEntryTablePanel();
		tablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(tablePanel);
	}

}
