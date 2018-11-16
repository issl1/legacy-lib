package com.nokor.efinance.gui.ui.panel.accounting.paymentcode;

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
@VaadinView(PaymentCodeHolderPanel.NAME)
public class PaymentCodeHolderPanel extends AbstractTabsheetPanel implements View{

	/** */
	private static final long serialVersionUID = 6623460758706008900L;

	public static final String NAME = "payment.codes";
	
	@Autowired
	private PaymentCodeTablePanel paymentCodeTablePanel;
	
	@Autowired
	private PaymentCodeFormPanel paymentCodeFormPanel;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		paymentCodeTablePanel.setMainPanel(this);
		paymentCodeFormPanel.setCaption(I18N.message("payment.code"));
		getTabSheet().setTablePanel(paymentCodeTablePanel);
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
		if (selectedTab == paymentCodeFormPanel) {
			paymentCodeFormPanel.assignValues(paymentCodeTablePanel.getItemSelectedId());
		} else if (selectedTab == paymentCodeTablePanel && getTabSheet().isNeedRefresh()) {
			paymentCodeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		paymentCodeFormPanel.reset();
		getTabSheet().addFormPanel(paymentCodeFormPanel);
		getTabSheet().setSelectedTab(paymentCodeFormPanel);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(paymentCodeFormPanel);
		initSelectedTab(paymentCodeFormPanel);
	}

}
