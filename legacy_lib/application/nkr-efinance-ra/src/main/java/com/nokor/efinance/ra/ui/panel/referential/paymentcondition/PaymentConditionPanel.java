package com.nokor.efinance.ra.ui.panel.referential.paymentcondition;

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
@VaadinView(PaymentConditionPanel.NAME)
public class PaymentConditionPanel extends AbstractTabsheetPanel implements View{
	

	private static final long serialVersionUID = 1L;
	public static final String NAME = "payment.conditions";
	
	@Autowired
	private PaymentConditionTablePanel paymentConditionTablePanel;
	@Autowired
	private PaymentConditionFormPanel paymentConditionFormPanel;
	
	@PostConstruct	
	public void PostConstruct() {
		super.init();
		paymentConditionTablePanel.setMainPanel(this);
		paymentConditionFormPanel.setCaption(I18N.message("payment.condition"));
		getTabSheet().setTablePanel(paymentConditionTablePanel);
	}
	
	@Override
	public void onAddEventClick() {
		paymentConditionFormPanel.reset();
		getTabSheet().addFormPanel(paymentConditionFormPanel);
		getTabSheet().setSelectedTab(paymentConditionFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(paymentConditionFormPanel);
		initSelectedTab(paymentConditionFormPanel);
	}
	
	@Override
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == paymentConditionFormPanel) {
			paymentConditionFormPanel.assignValue(paymentConditionTablePanel.getItemSelectedId());
		} else if (selectedTab == paymentConditionTablePanel && getTabSheet().isNeedRefresh()) {
			paymentConditionTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
