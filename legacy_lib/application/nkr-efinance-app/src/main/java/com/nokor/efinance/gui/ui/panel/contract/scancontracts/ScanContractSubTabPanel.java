package com.nokor.efinance.gui.ui.panel.contract.scancontracts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Receiving and Booking in scan contracts
 * @author uhout.cheng
 */
public class ScanContractSubTabPanel extends VerticalLayout implements ContractEntityField, ClickListener, FinServicesHelper, 
																ItemClickListener, SelectedItem {
	
	/** */
	private static final long serialVersionUID = 4956648895898426390L;
	
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions;	
	private VerticalLayout contentLayout;
	private TextField txtApplicationId;
	private Button btnEnter;
	private List<Contract> receivedContracts;
	private List<Contract> bookedContracts;
	private boolean isReceived;
	private Item selectedItem;

	/**
	 * 
	 * @param isFocus
	 */
	public ScanContractSubTabPanel(boolean isFocus) {
		setSizeFull();
		setMargin(true);
		txtApplicationId = ComponentFactory.getTextField(false, 60, 180);
		txtApplicationId.setImmediate(true);
		if (isFocus) {
			txtApplicationId.focus();
		}
		isReceived = true;
		
		txtApplicationId.addFocusListener(new FocusListener() {
			
			/** */
			private static final long serialVersionUID = -2511340475397081825L;

			/**
			 * @see com.vaadin.event.FieldEvents.FocusListener#focus(com.vaadin.event.FieldEvents.FocusEvent)
			 */
			@Override
			public void focus(FocusEvent event) {
				btnEnter.setClickShortcut(KeyCode.ENTER, null);
			}
		});
		
		txtApplicationId.addBlurListener(new BlurListener() {
			
			/** */
			private static final long serialVersionUID = 8664544459230524619L;

			/**
			 * @see com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
			 */
			@Override
			public void blur(BlurEvent event) {
				btnEnter.removeClickShortcut();
			}
		});
		
		btnEnter = new Button(I18N.message("enter"));
		btnEnter.setImmediate(true);
		btnEnter.addClickListener(this);
		
		receivedContracts = new ArrayList<Contract>();
		bookedContracts = new ArrayList<Contract>();
		
		HorizontalLayout searchLayout = getsearchLayout();
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Contract>(this.columnDefinitions);
		pagedTable.addItemClickListener(this);
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(searchLayout);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        addComponent(contentLayout);
	}
	
	/**
	 * Search Layout for search external reference in contract
	 * @return
	 */
	private HorizontalLayout getsearchLayout() {
		String OPEN_TABLE = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TD = "<td>";
		String CLOSE_TR = "</tr>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusSearchLayout = new CustomLayout("xxx");
		String templateSearchLayout = OPEN_TABLE;
		templateSearchLayout += OPEN_TR;
		templateSearchLayout += "<td align=\"right\">";
		templateSearchLayout += "<div location =\"lblApplicationId\"/>";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"txtApplicationId\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"btnEnter\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += CLOSE_TR;
		templateSearchLayout += CLOSE_TABLE;
		
		cusSearchLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		cusSearchLayout.addComponent(txtApplicationId, "txtApplicationId");
		cusSearchLayout.addComponent(btnEnter, "btnEnter");
		cusSearchLayout.setTemplateContents(templateSearchLayout);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setWidth(100, Unit.PERCENTAGE);
		horLayout.addComponent(cusSearchLayout);
		horLayout.setComponentAlignment(cusSearchLayout, Alignment.TOP_RIGHT);
		return horLayout;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		List<Contract> lstContracts = new ArrayList<Contract>();
		if (isReceived) {
			lstContracts = getContractsByStatus(ContractWkfStatus.PEN);
		} else {
			lstContracts = getContractsByStatus(ContractWkfStatus.PEN);
		} 
		if (lstContracts != null && !lstContracts.isEmpty()) {
			for (Contract contract : lstContracts) {
				if (isReceived) {
					// Received a contract
					CONT_SRV.receiveContract(contract.getId());
					if (ContractWkfStatus.PEN.equals(contract.getWkfStatus())) {
						receivedContracts.add(contract);
					}
					setIndexedContainer(receivedContracts);
				} else {
					// Booked a contract
					CONT_SRV.bookContract(contract.getId());
					if (ContractWkfStatus.PEN.equals(contract.getWkfStatus())) {
						bookedContracts.add(contract);
					}
					setIndexedContainer(bookedContracts);
				}
			}
		} else {
			Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			if (isReceived) {
				notification.setDescription(I18N.message("could.not.receive.this.contract",
						new String[]{ txtApplicationId.getValue() }));	
			} else {
				notification.setDescription(I18N.message("could.not.book.this.contract",
						new String[]{ txtApplicationId.getValue() }));
			}
		}
		txtApplicationId.setValue("");
	}
	
	/**
	 * 
	 * @param isReceived
	 */
	public void assignValues(boolean isReceived) {
		this.isReceived = isReceived;
		txtApplicationId.setValue("");
		txtApplicationId.focus();
		if (isReceived) {
			setIndexedContainer(receivedContracts);
		} else {
			setIndexedContainer(bookedContracts);
		}
	}

	/**
	 * 
	 * @return
	 */
	private List<Contract> getContractsByStatus(EWkfStatus status) {
		List<Contract> contracts = new ArrayList<Contract>();
		if (StringUtils.isNotEmpty(txtApplicationId.getValue())) {
			BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
			restrictions.addCriterion(Restrictions.ilike(Contract.EXTERNALREFERENCE, txtApplicationId.getValue()));
			restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {status}));
			contracts = ENTITY_SRV.list(restrictions);
		}
		return contracts;
	}
	
	/**
	 * 
	 * @param contracts
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Contract> contracts) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				Item item = indexedContainer.addItem(contract.getId());
				item.getItemProperty(ID).setValue(contract.getId());
				Applicant applicant = contract.getApplicant();
				if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())) {
					Individual individual = applicant.getIndividual();
					item.getItemProperty(REFERENCE).setValue(individual.getReference());
				}
				item.getItemProperty(Contract.EXTERNALREFERENCE).setValue(contract.getExternalReference());
				item.getItemProperty(QUOTATION_DATE).setValue(contract.getQuotationDate());
				item.getItemProperty(APPROVAL_DATE).setValue(contract.getApprovalDate());
				item.getItemProperty(DEALER).setValue(contract.getDealer() != null ? contract.getDealer().getNameEn() : "");
				item.getItemProperty(ASSET).setValue(contract.getAsset() != null ? contract.getAsset().getDescEn() : "");
				item.getItemProperty(FINANCIAL_PRODUCT).setValue(contract.getFinancialProduct() != null ? 
						contract.getFinancialProduct().getDescEn() : "");
				item.getItemProperty(WKF_STATUS).setValue(contract.getWkfStatus().getDescEn());
			}	
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("applicant.id"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(Contract.EXTERNALREFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(APPROVAL_DATE, I18N.message("approval.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DEALER, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ASSET, I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FINANCIAL_PRODUCT, I18N.message("product"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(WKF_STATUS, I18N.message("status"), String.class, Align.LEFT, 140));
		return columnDefinitions;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + getItemSelectedId());
		}
	}
		
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
}
