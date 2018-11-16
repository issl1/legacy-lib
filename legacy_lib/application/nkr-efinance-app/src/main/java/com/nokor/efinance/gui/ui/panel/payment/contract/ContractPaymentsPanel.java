package com.nokor.efinance.gui.ui.panel.payment.contract;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.gui.ui.panel.payment.AddPaymentPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Contract panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ContractPaymentsPanel.NAME)
public class ContractPaymentsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -470403725943364622L;

	public static final String NAME = "contract.payment";
	
	@Autowired
	private ContractPaymentTablePanel contractPaymentTablePanel;
	
	@Autowired
	private AddPaymentPanel addPaymentPanel;
	
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		contractPaymentTablePanel.setMainPanel(this);
		addPaymentPanel.setCaption(I18N.message("payment"));
		getTabSheet().setTablePanel(contractPaymentTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
		addPaymentPanel.reset();
		getTabSheet().addFormPanel(addPaymentPanel);
		getTabSheet().setSelectedTab(addPaymentPanel);
		addPaymentPanel.assignValues(contractPaymentTablePanel.getItemSelectedId());
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		
	}
}
