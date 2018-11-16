package com.nokor.efinance.gui.ui.panel.accounting.receiptcode;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import ru.xpoft.vaadin.VaadinView;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ReceiptCodeHolderPanel.NAME)
public class ReceiptCodeHolderPanel extends AbstractTabsheetPanel implements View{

	/** */
	private static final long serialVersionUID = -6357112674471164851L;

	public static final String NAME = "receipt.codes";
	
	@Autowired
	private ReceiptCodeTablePanel receiptCodeTablePanel;
	
	@Autowired
	private ReceiptCodeFormPanel receiptCodeFormPanel;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		receiptCodeTablePanel.setMainPanel(this);
		receiptCodeFormPanel.setCaption(I18N.message("receipt.code"));
		getTabSheet().setTablePanel(receiptCodeTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == receiptCodeFormPanel) {
			receiptCodeFormPanel.assignValues(receiptCodeTablePanel.getItemSelectedId());
		} else if (selectedTab == receiptCodeTablePanel && getTabSheet().isNeedRefresh()) {
			receiptCodeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		receiptCodeFormPanel.reset();
		getTabSheet().addFormPanel(receiptCodeFormPanel);
		getTabSheet().setSelectedTab(receiptCodeFormPanel);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(receiptCodeFormPanel);
		initSelectedTab(receiptCodeFormPanel);
	}

}
