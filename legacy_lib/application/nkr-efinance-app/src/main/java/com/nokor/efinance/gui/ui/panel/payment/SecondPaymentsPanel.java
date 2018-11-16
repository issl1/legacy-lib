package com.nokor.efinance.gui.ui.panel.payment;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author sok.vina
 *
 */
/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SecondPaymentsPanel.NAME)
public class SecondPaymentsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "second.payments";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SecondPaymentTablePanel secondPaymentTablePanel;
	@Autowired
	private PaymentFormPanel paymentFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		secondPaymentTablePanel.setMainPanel(this);
		paymentFormPanel.setCaption(I18N.message("second.payment"));
		getTabSheet().setTablePanel(secondPaymentTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		String paymnId = event.getParameters();
		if (StringUtils.isNotEmpty(paymnId)) {
			getTabSheet().addFormPanel(paymentFormPanel);
			paymentFormPanel.assignValues(new Long(paymnId));
			getTabSheet().setForceSelected(true);
			getTabSheet().setSelectedTab(paymentFormPanel);
		}
	}

	@Override
	public void onAddEventClick() {
		paymentFormPanel.reset();
		getTabSheet().addFormPanel(paymentFormPanel);
		getTabSheet().setSelectedTab(paymentFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(paymentFormPanel);
		initSelectedTab(paymentFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == paymentFormPanel) {
			paymentFormPanel.assignValues(secondPaymentTablePanel.getItemSelectedId());
		} else if (selectedTab == secondPaymentTablePanel && getTabSheet().isNeedRefresh()) {
			secondPaymentTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
