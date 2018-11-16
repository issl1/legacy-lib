package com.nokor.efinance.gui.ui.panel.payment.cheque;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.payment.cashier.PaymentCashierDetailTablePanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;


/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PaymentChequesPanel.NAME)
public class PaymentChequesPanel extends TabSheet implements View, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -1067730531404247560L;

	public static final String NAME = "pending.cheques";
	
	private PaymentCashierDetailTablePanel detailTablePanel;
				
	@PostConstruct
	public void PostConstruct() {
		detailTablePanel = new PaymentCashierDetailTablePanel(true);
		addTab(detailTablePanel, I18N.message("pending.cheques"));
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
