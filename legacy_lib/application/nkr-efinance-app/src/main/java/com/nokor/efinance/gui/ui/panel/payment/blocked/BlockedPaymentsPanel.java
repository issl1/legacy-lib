package com.nokor.efinance.gui.ui.panel.payment.blocked;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;


/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BlockedPaymentsPanel.NAME)
public class BlockedPaymentsPanel extends TabSheet implements View, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -831491573492313993L;

	public static final String NAME = "pending.batch.payment";
	
	private TabSheet mainTab;
	
	private BlockedPaymentDetailsPanel unIdentifiedDetailsPanel;
	private BlockedPaymentDetailsPanel errorDetailsPanel;
	private BlockedPaymentDetailsPanel unMatchedDetailsPanel;
	private BlockedPaymentDetailsPanel suspendedDetailsPanel;
	private BlockedPaymentDetailsPanel overDetailsPanel;
	private BlockedPaymentDetailsPanel matchedDetailsPanel;
				
	@PostConstruct
	public void PostConstruct() {
		mainTab = new TabSheet();
		
		unIdentifiedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.UNIDENTIFIED);
		errorDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.ERROR);
		unMatchedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.UNMATCHED);
		suspendedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.SUSPENDED);
		overDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.OVER);
		matchedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.MATCHED);
		
		mainTab.addTab(unIdentifiedDetailsPanel, I18N.message("unidentified"));
		mainTab.addTab(errorDetailsPanel, I18N.message("error"));
		mainTab.addTab(unMatchedDetailsPanel, I18N.message("unmatched"));
		mainTab.addTab(suspendedDetailsPanel, I18N.message("suspended"));
		mainTab.addTab(overDetailsPanel, I18N.message("over"));
		mainTab.addTab(matchedDetailsPanel, I18N.message("matched"));
		
		addTab(ComponentLayoutFactory.setMargin(mainTab), I18N.message("pending.batch.payment"));
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
