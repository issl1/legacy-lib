package com.nokor.efinance.ra.ui.panel.insurance.bank;

import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * 
 * @author uhout.cheng
 */
public class OrganizationBankAccountTabPanel extends AbstractControlPanel {
	
	/** */
	private static final long serialVersionUID = -2143618727101194912L;

	private TabSheet tabSheet;
    
	private OrganizationBankAccountTable orgAccountHolderTable;
	private OrganizationBankAccountTable orgBankAccountTable;
	
	private OrganizationBankAccountsPanel orgBankAccountsPanel;
	
	private Organization org;

	/**
	 * 
	 * @param orgBankAccountsPanel
	 */
	public OrganizationBankAccountTabPanel(OrganizationBankAccountsPanel orgBankAccountsPanel) {
		this.orgBankAccountsPanel = orgBankAccountsPanel;
		setSpacing(true);
		
		orgAccountHolderTable = new OrganizationBankAccountTable(EPaymentMethod.CHEQUE, this);
		orgBankAccountTable = new OrganizationBankAccountTable(EPaymentMethod.TRANSFER, this);
		
		tabSheet = new TabSheet();
		tabSheet.addTab(orgAccountHolderTable, I18N.message("account.holder"));
		tabSheet.addTab(orgBankAccountTable, I18N.message("bank.account"));
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {

			/** */
			private static final long serialVersionUID = 6565879423024531488L;

			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (tabSheet.getSelectedTab().equals(orgBankAccountTable)) {
					orgBankAccountTable.assignValues(org);
				}
			}
		});
		
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 * @param org
	 */
	public void assignValue(Organization org) {
		this.org = org;
		tabSheet.setSelectedTab(orgAccountHolderTable);
		orgAccountHolderTable.assignValues(org);
	}
	
	/**
	 * 
	 * @param organization
	 */
	protected void refreshPaymentMethod(Organization organization) {
		orgBankAccountsPanel.refreshPaymentMethod(organization);
	}

}
