package com.nokor.efinance.ra.ui.panel.subdealer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubDealerBankAccountTablePanel extends VerticalLayout implements FinServicesHelper, FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private Bank bank;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<DealerBankAccount> pagedTable;
	private Item selectedItem = null;
	private TextField txtBankName;
	private TextField txtaccountNumber;
	private TextField txtaccountHolder;
	private TextField txtswiftCode;
	private CheckBox cbActive;
	private long dealerId;
	private Dealer dealer;
	private Long bankAccountId;
	@PostConstruct
	public void PostConstruct() {
		
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddBankAccount = new NativeButton(I18N.message("add.bank.account"));
		btnAddBankAccount.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnAddBankAccount.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				bankAccountId = null;
				getBankAccountForm(bankAccountId);
			}
		});		
		
		navigationPanel.addButton(btnAddBankAccount);	
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<DealerBankAccount>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					bankAccountId = (Long) selectedItem.getItemProperty("id").getValue();
					getBankAccountForm(bankAccountId);
				}
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * @param dealerId
	 */
	public void assignValues(Long dealerId) {
		if (dealerId != null) {
			this.dealerId = dealerId;
			this.dealer =  ENTITY_SRV.getById(Dealer.class, new Long(dealerId));
			pagedTable.setContainerDataSource(getIndexedContainer(dealer.getDealerBankAccounts()));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<DealerBankAccount> dealerBankAccounts) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
					
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			/*for (DealerBankAccount dealerBankAccount : dealerBankAccounts) {
				Bank bank = dealerBankAccount.getBank();
				Item item = indexedContainer.addItem(bank.getId());
				item.getItemProperty(ID).setValue(bank.getId());
				item.getItemProperty("bankName").setValue(bank.getName());
				item.getItemProperty("account.number").setValue(dealerBankAccount.getAccountNumber());
				item.getItemProperty("account.holder").setValue(dealerBankAccount.getAccountHolder());
				item.getItemProperty("swiftcode").setValue(bank.getSwift());
			}*/
						
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("bankName", I18N.message("bank.name"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("account.number", I18N.message("account.number"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("account.holder", I18N.message("account.holder"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("swiftcode", I18N.message("swiftcode"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	/**
	 * 
	 * @param bankId
	 */
	public void getBankAccountForm(final Long bankId) {
		this.bankAccountId = bankId;
		final Window winBankAccount = new Window(I18N.message("add.bank.account"));
		winBankAccount.setModal(true);
		winBankAccount.setWidth(350, Unit.PIXELS);
		winBankAccount.setHeight(280, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        txtBankName = ComponentFactory.getTextField("bank.name", false, 60, 150);
        txtBankName.setRequired(true);
        txtaccountNumber = ComponentFactory.getTextField("account.number", false, 60, 150);
        txtaccountNumber.setRequired(true);
        txtaccountHolder = ComponentFactory.getTextField("account.holder", false, 60, 150);
        txtswiftCode = ComponentFactory.getTextField("swiftcode", false, 60, 150);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
		/*if (bankId != null) {
			DealerBankAccount dealerBankAccount = ENTITY_SRV.getByField(DealerBankAccount.class, "bank.id", bankId);
			txtaccountNumber.setValue(dealerBankAccount.getAccountNumber());
			txtaccountHolder.setValue(dealerBankAccount.getAccountHolder());
			txtBankName.setValue(bank.getName());
			txtswiftCode.setValue(bank.getSwift());
			cbActive.setValue(bank.isActive());
		}*/
        formLayout.addComponent(txtBankName);
        formLayout.addComponent(txtaccountNumber);
        formLayout.addComponent(txtaccountHolder);
        formLayout.addComponent(txtswiftCode);
        formLayout.addComponent(cbActive);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				
				if (!txtBankName.getValue().equals("")
								&& !txtaccountNumber.getValue().equals("")) {
					
					if (bankId == null) {
						bank = Bank.createInstance();
					}
					bank.setName(txtBankName.getValue());
					bank.setSwift(txtswiftCode.getValue());
					bank.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
					ENTITY_SRV.saveOrUpdate(bank);
					
					/*if (bankId == null) {
						DealerBankAccount dealerBankAccount = new DealerBankAccount();
						dealerBankAccount.setDealer(dealer);
						dealerBankAccount.setAccountNumber(txtaccountNumber.getValue());
						dealerBankAccount.setAccountHolder(txtaccountHolder.getValue());
						dealerBankAccount.setBank(bank);
						ENTITY_SRV.saveOrUpdate(dealerBankAccount);
					}*/
					winBankAccount.close();
					assignValues(dealerId);	
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
            }
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winBankAccount.close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		

		contentLayout.addComponent(navigationPanel);
        contentLayout.addComponent(formLayout);
        
        winBankAccount.setContent(contentLayout);
        UI.getCurrent().addWindow(winBankAccount);
	
	}

}
