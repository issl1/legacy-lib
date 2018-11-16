package com.nokor.efinance.gui.ui.panel.contract.repossess;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Contract re possesses panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ContractRepossessesPanel.NAME)
public class ContractRepossessesPanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -5671616382948056002L;
	
	public static final String NAME = "contract.repossesses";
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ContractRepossessTablePanel contractRepossessTablePanel;
	@Autowired
	private ContractRepossessFormPanel contractRepossessFormPanel;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		contractRepossessTablePanel.setMainPanel(this);
		contractRepossessFormPanel.setCaption(I18N.message("contract.repossess"));
		getTabSheet().setTablePanel(contractRepossessTablePanel);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		if (StringUtils.isNotEmpty(cotraId)) {
			getTabSheet().addFormPanel(contractRepossessFormPanel);
			contractRepossessFormPanel.assignValues(new Long(cotraId));
			getTabSheet().setForceSelected(true);
			getTabSheet().setSelectedTab(contractRepossessFormPanel);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		contractRepossessFormPanel.reset();
		getTabSheet().addFormPanel(contractRepossessFormPanel);
		getTabSheet().setSelectedTab(contractRepossessFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(contractRepossessFormPanel);
		initSelectedTab(contractRepossessFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == contractRepossessFormPanel) {
			contractRepossessFormPanel.assignValues(contractRepossessTablePanel.getItemSelectedId());
		} 
		contractRepossessTablePanel.refresh();
		getTabSheet().setSelectedTab(selectedTab);
	}
}
