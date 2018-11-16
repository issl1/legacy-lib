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

import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.panel.earlysettlement.PaymentInfo2Panel;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(FOInstallmentPaymentReportsPanel.NAME)
public class FOInstallmentPaymentReportsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "fo.report.installment.payments";
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private FOInstallmentPaymentReportTablePanel installmentPaymentReportTablePanel;
	private PaymentInfo2Panel paymentInfo2Panel;
	private int numChangTab = 0;
	private Payment payment;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		installmentPaymentReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(installmentPaymentReportTablePanel);
		
		getTabSheet().addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				numChangTab++;						
				if (paymentInfo2Panel != null && numChangTab == 2) {
					getTabSheet().removeComponent(paymentInfo2Panel);
					numChangTab = 0;
				}						
			}
		});
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		String paymnId = event.getParameters();
		if (StringUtils.isNotEmpty(paymnId)) {;
			getTabSheet().setForceSelected(true);
		}
	}

	@Override
	public void onAddEventClick() {
		
	}

	@Override
	public void onEditEventClick() {
		payment = ENTITY_SRV.getById(Payment.class, installmentPaymentReportTablePanel.getItemSelectedId());
		paymentInfo2Panel = new PaymentInfo2Panel();
		paymentInfo2Panel.setCaption(I18N.message("payment.info"));
		paymentInfo2Panel.assignValues(payment);
		getTabSheet().addTab(paymentInfo2Panel);
		getTabSheet().setSelectedTab(paymentInfo2Panel);				
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == installmentPaymentReportTablePanel && getTabSheet().isNeedRefresh()) {
			installmentPaymentReportTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
