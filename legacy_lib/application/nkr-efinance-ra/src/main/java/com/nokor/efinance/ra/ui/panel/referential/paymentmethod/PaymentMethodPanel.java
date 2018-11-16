package com.nokor.efinance.ra.ui.panel.referential.paymentmethod;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PaymentMethodPanel.NAME)
public class PaymentMethodPanel extends AbstractTabsheetPanel implements View{
	

	private static final long serialVersionUID = 1L;
	public static final String NAME = "payment.method";
	
	@Autowired
	private PaymentMethodTablePanel paymentMethodTablePanel;
	@Autowired
	private PaymentMethodFormPanel paymentMethodFormPanel;
	
	@PostConstruct	
	public void PostConstruct() {
		super.init();
		paymentMethodTablePanel.setMainPanel(this);
		paymentMethodFormPanel.setCaption(I18N.message("payment.method"));
		getTabSheet().setTablePanel(paymentMethodTablePanel);
	}
	
	@Override
	public void onAddEventClick() {
		paymentMethodFormPanel.reset();
		getTabSheet().addFormPanel(paymentMethodFormPanel);
		getTabSheet().setSelectedTab(paymentMethodFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(paymentMethodFormPanel);
		initSelectedTab(paymentMethodFormPanel);
	}
	
	@Override
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == paymentMethodFormPanel) {
			paymentMethodFormPanel.assignValue(paymentMethodTablePanel.getItemSelectedId());
		} else if (selectedTab == paymentMethodTablePanel && getTabSheet().isNeedRefresh()) {
			paymentMethodTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
