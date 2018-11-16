package com.nokor.efinance.gui.ui.panel.dashboard.summaries;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ContractSummariesPanel.NAME)
public class ContractSummariesPanel extends TabSheet implements View {

	/** */
	private static final long serialVersionUID = -8880162574139944187L;

	public static final String NAME = "summaries";
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {

		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		ContractSummariedTablePanel tablePanel = new ContractSummariedTablePanel();
		ContractSummariedSearchPanel searchPanel = new ContractSummariedSearchPanel(tablePanel);
		mainLayout.addComponent(searchPanel);
		mainLayout.addComponent(tablePanel);
		
		List<Contract> contracts = new ArrayList<Contract>();
		List<ContractUserInbox> contractUserInboxs = searchPanel.getRestriction();
		for (ContractUserInbox contractUserInbox : contractUserInboxs) {
			contracts.add(contractUserInbox.getContract());
		}
		tablePanel.assignValue(contracts);
		
		this.addTab(mainLayout, I18N.message("summaries"));
	}
}
