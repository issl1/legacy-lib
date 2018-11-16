package com.nokor.ersys.finance.accounting.ui.panel.main.journal.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * Transaction Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(TransactionHolderPanel.NAME)
public class TransactionHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = 2634700304545806588L;
	
	public static final String NAME = "transaction.list";
	
	@Autowired
	private TransactionListPanel tablePanel;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
//		formPanel.setMainPanel(this);
//		formPanel.setCaption(I18N.message("detail"));
		getTabSheet().setTablePanel(tablePanel);
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
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(Component selectedTab) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
