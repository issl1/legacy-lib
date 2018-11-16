package com.nokor.efinance.gui.ui.panel.payment.cashier;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;


/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PaymentCashiersPanel.NAME)
public class PaymentCashiersPanel extends TabSheet implements View, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -174164262055459983L;

	public static final String NAME = "payment.cashier.cheque";
	
	private PaymentCashierDetailTablePanel detailTablePanel;
				
	@PostConstruct
	public void PostConstruct() {
		
		detailTablePanel = new PaymentCashierDetailTablePanel(false);
		
		addTab(detailTablePanel, I18N.message("payment.cashier.cheque"));
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
