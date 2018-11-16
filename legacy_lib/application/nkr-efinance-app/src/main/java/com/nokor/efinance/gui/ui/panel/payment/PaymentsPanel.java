package com.nokor.efinance.gui.ui.panel.payment;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PaymentsPanel.NAME)
public class PaymentsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = 5875568105689149743L;

	public static final String NAME = "payments";
	
	@Autowired
	private PaymentTablePanel paymentTablePanel;
	@Autowired
	private PaymentFormPanel paymentFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		paymentTablePanel.setMainPanel(this);
		paymentFormPanel.setCaption(I18N.message("payment"));
		getTabSheet().setTablePanel(paymentTablePanel);
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
		
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == paymentFormPanel) {
			paymentFormPanel.assignValues(paymentTablePanel.getItemSelectedId());
		} else if (selectedTab == paymentTablePanel && getTabSheet().isNeedRefresh()) {
			paymentTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	public void redirectTopaymentTablePanel() {
		initSelectedTab(paymentFormPanel);
	}

	public void refreshPaymentTablePanel() {
		paymentTablePanel.refresh();
	}
}
