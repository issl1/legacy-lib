package com.nokor.efinance.gui.ui.panel.contract;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Full contract page in CM
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ContractsPanel.NAME)
public class ContractsPanel extends AbstractTabsheetPanel implements View, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 95631790238907465L;

	public static final String NAME = "contracts";
		
	private ContractTablePanel contractTablePanel;
	private ContractFormPanel contractFormPanel;
	private ColContractHistoryFormPanel colContractHistoryFormPanel;
		
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		super.init();		
		String cotraId = event.getParameters();
		if (StringUtils.isNotEmpty(cotraId)) {
			getTabSheet().addFormPanel(getContractFormPanel());
			
			Contract contract = CONT_SRV.getById(Contract.class, new Long(cotraId));
			if (contract != null && ContractUtils.isActivated(contract)) {
				CON_OTH_SRV.calculateOtherDataContract(contract);
			}
			
			getContractFormPanel().assignValues(new Long(cotraId), true);
			getTabSheet().setForceSelected(true);
			getTabSheet().setSelectedTab(getContractFormPanel());
		} else {
			contractTablePanel = new ContractTablePanel();
			contractTablePanel.setMainPanel(this);
			getTabSheet().setTablePanel(contractTablePanel);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		if (ProfileUtil.isCollection()) {
			getTabSheet().addFormPanel(getColContractHistoryFormPanel());
			getTabSheet().setForceSelected(true);
			getTabSheet().setSelectedTab(getColContractHistoryFormPanel());
			initSelectedTab(getColContractHistoryFormPanel());
		} else {
//			getContractFormPanel().reset();
			getTabSheet().addFormPanel(getContractFormPanel());
			initSelectedTab(getContractFormPanel());
		}
	}
	
	/**
	 * @return
	 */
	private ContractFormPanel getContractFormPanel() {
		if (contractFormPanel == null) {
			contractFormPanel = new ContractFormPanel();
			contractFormPanel.setCaption(I18N.message("contract"));
		}
		return contractFormPanel;
	}
	
	/**
	 * @return
	 */
	private ColContractHistoryFormPanel getColContractHistoryFormPanel() {
		if (colContractHistoryFormPanel == null) {
			colContractHistoryFormPanel = new ColContractHistoryFormPanel(false);
			colContractHistoryFormPanel.setCaption(I18N.message("contract.histories"));
		}
		return colContractHistoryFormPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (contractTablePanel.getItemSelectedId() != null) {
			Contract contract = CONT_SRV.getById(Contract.class, contractTablePanel.getItemSelectedId());
			if (contract != null && ContractUtils.isActivated(contract)) {
				CON_OTH_SRV.calculateOtherDataContract(contract);
			}
		}
		if (!(ProfileUtil.isCallCenter() && ProfileUtil.isCollection()) && selectedTab == contractFormPanel) {
			contractFormPanel.assignValues(contractTablePanel.getItemSelectedId(), true);
		} else if ((ProfileUtil.isCallCenter() || ProfileUtil.isCollection()) && selectedTab == colContractHistoryFormPanel) { 
			colContractHistoryFormPanel.assignValues(contractTablePanel.getItemSelectedId(), true);
		} else if (selectedTab == contractTablePanel && getTabSheet().isNeedRefresh()) {	
			contractTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
