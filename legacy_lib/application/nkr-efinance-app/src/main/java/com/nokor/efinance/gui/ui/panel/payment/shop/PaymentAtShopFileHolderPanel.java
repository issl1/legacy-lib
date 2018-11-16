package com.nokor.efinance.gui.ui.panel.payment.shop;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.UI;

/**
 * Files upload for payment at shop
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PaymentAtShopFileHolderPanel.NAME)
public class PaymentAtShopFileHolderPanel extends AbstractTabsheetPanel implements View {
	
	/** */
	private static final long serialVersionUID = 7237197465591379040L;

	public static final String NAME = "files.upload";
	
	@Autowired
	private PaymentAtShopTablePanel tablePanel;
	
	private PaymentAtShopPopupWindow formPanel;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		formPanel = new PaymentAtShopPopupWindow(tablePanel);
		tablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		UI.getCurrent().addWindow(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		formPanel.assignValues(tablePanel.getItemSelectedId());
		UI.getCurrent().addWindow(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
