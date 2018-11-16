package com.nokor.efinance.ra.ui.panel.insurance.bank;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.gl.finwiz.share.domain.AP.BankBranchDTO;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.service.OrgAccountHolderRestriction;
import com.nokor.ersys.core.hr.service.OrgBankAccountRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;


/**
 * 
 * @author uhout.cheng
 */
public class OrganizationBankAccountTable extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -5460859917568859602L;

	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<DealerBankAccount> pagedTable;
	private Item selectedItem;
	private Organization org;
	
	private OrganizationBankAccountTabPanel orgBankAccountTabPanel;
	
	private EPaymentMethod paymentMethod;
	
	/**
	 * 
	 * @param paymentMethod
	 * @param orgBankAccountTabPanel
	 */
	public OrganizationBankAccountTable(EPaymentMethod paymentMethod, OrganizationBankAccountTabPanel orgBankAccountTabPanel) {
		this.paymentMethod = paymentMethod;
		this.orgBankAccountTabPanel = orgBankAccountTabPanel;
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAdd.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 2400508768559225884L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (EPaymentMethod.CHEQUE.equals(paymentMethod)) {
					UI.getCurrent().addWindow(new OrganizationAccountHolderWindowForm(org, null, OrganizationBankAccountTable.this));
				} else if (EPaymentMethod.TRANSFER.equals(paymentMethod)) {
					UI.getCurrent().addWindow(new OrganizationBankAccountWindowForm(org, null, OrganizationBankAccountTable.this));
				}
			}
		});		
		
		navigationPanel.addButton(btnAdd);		
		this.columnDefinitions = createColumnDefinitions(paymentMethod);
		pagedTable = new SimplePagedTable<DealerBankAccount>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = -6560862504035792932L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					if (EPaymentMethod.CHEQUE.equals(paymentMethod)) {
						Long accHolderId = (Long) selectedItem.getItemProperty(DealerAccountHolder.ACCOUNTHOLDERID).getValue();
						UI.getCurrent().addWindow(new OrganizationAccountHolderWindowForm(org, accHolderId, OrganizationBankAccountTable.this));
					} else if (EPaymentMethod.TRANSFER.equals(paymentMethod)) {
						Long bankAccId = (Long) selectedItem.getItemProperty(DealerBankAccount.BANKACCOUNTID).getValue();
						UI.getCurrent().addWindow(new OrganizationBankAccountWindowForm(org, bankAccId, OrganizationBankAccountTable.this));
					}
				}
			}
		});
		
		setMargin(true);
		setSpacing(true);
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @param org
	 */
	protected void assignValues(Organization org) {
		this.reset();
		if (org != null) {
			this.org = org;
			if (EPaymentMethod.CHEQUE.equals(this.paymentMethod)) {
				pagedTable.setContainerDataSource(getIndexedContainerAccountHolder(getOrgAccountHolderByDealer(org)));
			} else if (EPaymentMethod.TRANSFER.equals(this.paymentMethod)) {
				pagedTable.setContainerDataSource(getIndexedContainerBankAccount(getOrgBankAccountByDealer(org)));
			}
		} 
		orgBankAccountTabPanel.refreshPaymentMethod(org);
	}
	
	/**
	 * 
	 * @param organization
	 * @return
	 */
	private List<OrgBankAccount> getOrgBankAccountByDealer(Organization organization) {
		OrgBankAccountRestriction restrictions = new OrgBankAccountRestriction();
		restrictions.setOrganization(organization);
		restrictions.addOrder(Order.desc(OrgBankAccount.ID));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param organization
	 * @return
	 */
	private List<OrgAccountHolder> getOrgAccountHolderByDealer(Organization organization) {
		OrgAccountHolderRestriction restrictions = new OrgAccountHolderRestriction();
		restrictions.setOrganization(organization);
		restrictions.addOrder(Order.desc(OrgAccountHolder.ID));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainerBankAccount(List<OrgBankAccount> orgBankAccounts) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (orgBankAccounts != null && !orgBankAccounts.isEmpty()) {
			for (OrgBankAccount orgBankAccount : orgBankAccounts) {
				Item item = indexedContainer.addItem(orgBankAccount.getId());
				item.getItemProperty(OrgBankAccount.ID).setValue(orgBankAccount.getId());
				String accountName = StringUtils.EMPTY;
				String accountNumber = StringUtils.EMPTY;
				String bank = StringUtils.EMPTY;
				String bankBranch = StringUtils.EMPTY;
				if (orgBankAccount.getBankAccount() != null) {
					BankAccountDTO bankAccountDTO = ClientBankAccount.getBankAccountById(orgBankAccount.getBankAccount());
					if (bankAccountDTO != null) {
						accountName = getDefaultString(bankAccountDTO.getAccountName());
						accountNumber = getDefaultString(bankAccountDTO.getPayeeAccountNumber());
						BankBranchDTO bankBranchDTO = bankAccountDTO.getBankBranch();
						if (bankBranchDTO != null) {
							bankBranch = getDefaultString(bankBranchDTO.getName());
							bank = getDefaultString(bankBranchDTO.getBank() == null ? StringUtils.EMPTY : bankBranchDTO.getBank().getName());
						}
					} 
				}
				item.getItemProperty(DealerBankAccount.BANKACCOUNTID).setValue(orgBankAccount.getBankAccount());
				item.getItemProperty(DealerBankAccount.ACCOUNTNAME).setValue(accountName);
				item.getItemProperty(DealerBankAccount.PAYEEACCOUNTNUMBER).setValue(accountNumber);
				item.getItemProperty(DealerBankAccount.BANK).setValue(bank);
				item.getItemProperty(DealerBankAccount.BANKBRANCH).setValue(bankBranch);
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainerAccountHolder(List<OrgAccountHolder> orgAccountHolders) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (orgAccountHolders != null && !orgAccountHolders.isEmpty()) {
			for (OrgAccountHolder orgAccountHolder : orgAccountHolders) {
				Item item = indexedContainer.addItem(orgAccountHolder.getId());
				item.getItemProperty(OrgAccountHolder.ID).setValue(orgAccountHolder.getId());
				String payeeName = StringUtils.EMPTY;
				if (orgAccountHolder.getAccountHolder() != null) {
					AccountHolderDTO accountHolderDTO = ClientAccountHolder.getAccountHolderById(orgAccountHolder.getAccountHolder());
					payeeName = accountHolderDTO == null ? StringUtils.EMPTY : accountHolderDTO.getName();
				}
				item.getItemProperty(DealerAccountHolder.ACCOUNTHOLDERID).setValue(orgAccountHolder.getAccountHolder());
				item.getItemProperty(DealerAccountHolder.DESC).setValue(payeeName);
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions(EPaymentMethod paymentMethod) {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		if (EPaymentMethod.CHEQUE.equals(paymentMethod)) {
			columnDefinitions.add(new ColumnDefinition(DealerAccountHolder.ID, I18N.message("id"), Long.class, Align.LEFT, 100));
			columnDefinitions.add(new ColumnDefinition(DealerAccountHolder.ACCOUNTHOLDERID, I18N.message("account.holder.id"), Long.class, Align.LEFT, 100));
			columnDefinitions.add(new ColumnDefinition(DealerAccountHolder.DESC, I18N.message("payee.name"), String.class, Align.LEFT, 170));
		} else if (EPaymentMethod.TRANSFER.equals(paymentMethod)) {
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.ID, I18N.message("id"), Long.class, Align.LEFT, 100));
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.BANKACCOUNTID, I18N.message("bank.account.id"), Long.class, Align.LEFT, 100));
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.ACCOUNTNAME, I18N.message("account.name"), String.class, Align.LEFT, 150));
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.PAYEEACCOUNTNUMBER, I18N.message("payee.account.number"), String.class, Align.LEFT, 150));
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.BANK, I18N.message("bank"), String.class, Align.LEFT, 150));
			columnDefinitions.add(new ColumnDefinition(DealerBankAccount.BANKBRANCH, I18N.message("bank.branch"), String.class, Align.LEFT, 150));
		}
		return columnDefinitions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		this.pagedTable.removeAllItems();
		this.selectedItem = null;
		this.org = null;
	}
}
