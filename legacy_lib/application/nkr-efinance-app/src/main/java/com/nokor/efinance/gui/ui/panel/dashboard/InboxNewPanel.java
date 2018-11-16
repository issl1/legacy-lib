package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * @author buntha.chea
 */
public class InboxNewPanel extends VerticalLayout implements ContractEntityField, ItemClickListener, SelectedItem, FinServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = 3097597720691234944L;
	
	private Button btnReceived;
	private SimplePagedTable<Contract> scanPagedTable;
	private SimplePagedTable<Contract> pagedTable;
	
	private Item selectedItem;
	private TextField txtApplicationId;
	private Button btnSelect;
	
	
	public InboxNewPanel() {
		setSizeFull();
		setMargin(true);
		txtApplicationId = ComponentFactory.getTextField(false, 60, 180);
		btnSelect = new Button("");
		btnSelect.setStyleName(Runo.BUTTON_LINK);
		
		btnReceived = new Button(I18N.message("received"));
		btnReceived.setIcon(FontAwesome.CHECK_CIRCLE);
		
		txtApplicationId.addFocusListener(new FocusListener() {
			/** */
			private static final long serialVersionUID = 6333845283205966781L;			
			/**
			 * @see com.vaadin.event.FieldEvents.FocusListener#focus(com.vaadin.event.FieldEvents.FocusEvent)
			 */
			@Override
			public void focus(FocusEvent event) {
				btnSelect.setClickShortcut(KeyCode.ENTER, null);
			}
		});
		
		txtApplicationId.addBlurListener(new BlurListener() {
			/** */
			private static final long serialVersionUID = -1337241393559797264L;
			
			/**
			 * @see com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
			 */
			@Override
			public void blur(BlurEvent event) {
				btnSelect.removeClickShortcut();
			}
		});
				
		HorizontalLayout searchLayout = getSearchLayout();
		
		scanPagedTable = new SimplePagedTable<>(createColumnDefinitions());
		scanPagedTable.setCaption(null);
		scanPagedTable.addItemClickListener(this);
		
		pagedTable = new SimplePagedTable<>(createColumnDefinitions());
		pagedTable.setCaption(null);
		pagedTable.addItemClickListener(this);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(searchLayout);
		contentLayout.addComponent(scanPagedTable);
        contentLayout.addComponent(scanPagedTable.createControls());
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        
        addComponent(contentLayout);
	}
	
	/**
	 * Search Layout for search applicant id
	 * @return
	 */
	private HorizontalLayout getSearchLayout() {
		btnReceived.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -5347171223537338570L;			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), I18N.message("confirm.received.applications"), I18N.message("confirm"), I18N.message("cancel"),
					new ConfirmDialog.Listener() {				
						/**
						 */
						private static final long serialVersionUID = 8385185545686540623L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								List<Long> ids = getSelectedIds(scanPagedTable);
								if (ids != null && !ids.isEmpty()) {
									CONT_SRV.receiveContracts(ids);
									assignValues();
								}
								dialog.close();
				            }
						}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
			}
		});
		
		btnSelect.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 5957274994260833712L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {								
				Contract contract = CONT_SRV.getByExternalReference(txtApplicationId.getValue());
				if (contract != null){ 
					List<ContractUserInbox> contractUserInboxs = INBOX_SRV.getContractUserInboxByContract(contract.getId());
					if (contractUserInboxs != null && contractUserInboxs.isEmpty()) {
						addScanContractItem(contract);
						txtApplicationId.setValue("");
					} else {
						displayErrorMessage();
					}
				} else {
					displayErrorMessage();
				}
			}
		});
		
		String OPEN_TABLE = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TD = "<td>";
		String CLOSE_TR = "</tr>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusBtnBookLayout = new CustomLayout("xxx");
		String templateBtnBook = OPEN_TABLE;
		templateBtnBook += OPEN_TR;
		templateBtnBook += OPEN_TD;
		templateBtnBook += "<div location =\"btnBook\"/>";
		templateBtnBook += CLOSE_TD;
		templateBtnBook += CLOSE_TR;
		templateBtnBook += CLOSE_TABLE;
		
		cusBtnBookLayout.addComponent(btnReceived, "btnBook");
		cusBtnBookLayout.setTemplateContents(templateBtnBook);
		
		CustomLayout cusSearchLayout = new CustomLayout("xxx");
		String templateSearchLayout = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" align=\"right\">";
		templateSearchLayout += OPEN_TR;
		templateSearchLayout += "<td align=\"right\">";
		templateSearchLayout += "<div location =\"lblApplicationId\"/>";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"txtApplicationId\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"btnSearch\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += CLOSE_TR;
		templateSearchLayout += CLOSE_TABLE;
		
		cusSearchLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		cusSearchLayout.addComponent(txtApplicationId, "txtApplicationId");
		cusSearchLayout.addComponent(btnSelect, "btnSearch");
		cusSearchLayout.setTemplateContents(templateSearchLayout);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setWidth(100, Unit.PERCENTAGE);
		horLayout.addComponent(cusBtnBookLayout);
		horLayout.addComponent(cusSearchLayout);
		horLayout.setComponentAlignment(cusSearchLayout, Alignment.TOP_RIGHT);
		return horLayout;
	}
		
	/**
	 */
	public void assignValues() {
		Indexed indexedContainer = scanPagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		scanPagedTable.refreshContainerDataSource();
		setIndexedContainer(CONT_SRV.getListContract(getRestrictions()));
	}
	
	/**
	 * 
	 */
	private void displayErrorMessage() {
		MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
				MessageBox.Icon.ERROR, I18N.message("application.not.found", txtApplicationId.getValue()), 
				Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.show();
	}
	
	/**
	 * @param dealer
	 * @return
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		
		restrictions.addCriterion(Restrictions.or(
				Restrictions.in(WKF_STATUS, new EWkfStatus[] {ContractWkfStatus.PEN, ContractWkfStatus.BLOCKED}),
				Restrictions.in(WKF_SUB_STATUS, new EWkfStatus[] {ContractWkfStatus.PEN_TRAN, ContractWkfStatus.BLOCKED_TRAN})));
		
		DetachedCriteria userContractSubCriteria1 = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria1.add(Restrictions.isNull("cousr.secUser"));
		userContractSubCriteria1.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").notIn(userContractSubCriteria1));
		
		DetachedCriteria userContractSubCriteria2 = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria2.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria2.createAlias("usr.defaultProfile", "prf", JoinType.INNER_JOIN);
		userContractSubCriteria2.add(Restrictions.in("prf.code", new String[] {IProfileCode.CMLEADE, IProfileCode.CMSTAFF}));
		
		userContractSubCriteria2.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").notIn(userContractSubCriteria2));
		
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
	
	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Long> getSelectedIds(SimplePagedTable<Contract> pagedTable) {
		List<Long> ids = new ArrayList<>();
		for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = pagedTable.getItem(iid);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(iid);
		    }
		}
		return ids;
	}
	
	/**
	 * @param contracts
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Contract> contracts) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				Item item = indexedContainer.addItem(contract.getId());
				CheckBox cbSelect = new CheckBox();
				cbSelect.setEnabled(ProfileUtil.isCMLeader());
				cbSelect.addValueChangeListener(new ValueChangeListener() {					
				
					private static final long serialVersionUID = -635117329532304632L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (cbSelect.getValue()) {
							addScanContractItem(contract);
						} else {
							removeScanContractItem(contract);
						}
					}
				});
				
				item.getItemProperty("select").setValue(cbSelect);
				item.getItemProperty(ID).setValue(contract.getId());
				item.getItemProperty(Contract.EXTERNALREFERENCE).setValue(ContractUtils.getApplicationID(contract));
				item.getItemProperty("type").setValue(contract.isTransfered() ? "TRANSFER" : "NEW");
				item.getItemProperty(APPLICANT + "." + NAME_EN).setValue(ContractUtils.getApplicationName(contract));
				item.getItemProperty(QUOTATION_DATE).setValue(ContractUtils.getApplicationDate(contract));
				item.getItemProperty(APPROVAL_DATE).setValue(ContractUtils.getApprovalDate(contract));
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer() != null ? contract.getDealer().getNameLocale() : "");
				item.getItemProperty(ASSET + "." + SERIE).setValue(contract.getAsset() != null ? contract.getAsset().getSerie() : "");
				item.getItemProperty(FINANCIAL_PRODUCT + "." + DESC_EN).setValue(contract.getFinancialProduct() != null ? contract.getFinancialProduct().getDescLocale() : "");
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param contract
	 */
	@SuppressWarnings("unchecked")
	private void addScanContractItem(Contract contract) {
		boolean alreadyAdded = false;
		for (Iterator i = scanPagedTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = scanPagedTable.getItem(iid);
		    String externalRef = (String) item.getItemProperty(Contract.EXTERNALREFERENCE).getValue();
		    if (txtApplicationId.getValue().equals(externalRef)) {
		    	alreadyAdded = true;
		    	break;
		    }
		}
		if (!alreadyAdded) {			
			Indexed indexedContainer = scanPagedTable.getContainerDataSource();		
			Item item = indexedContainer.addItem(contract.getId());
			CheckBox cbSelect = new CheckBox();
			cbSelect.setValue(true);
			cbSelect.setEnabled(false);
			item.getItemProperty("select").setValue(cbSelect);
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty(Contract.EXTERNALREFERENCE).setValue(ContractUtils.getApplicationID(contract));
			item.getItemProperty("type").setValue(contract.isTransfered() ? "TRANSFER" : "NEW");
			item.getItemProperty(APPLICANT + "." + NAME_EN).setValue(ContractUtils.getApplicationName(contract));
			item.getItemProperty(QUOTATION_DATE).setValue(ContractUtils.getApplicationDate(contract));
			item.getItemProperty(APPROVAL_DATE).setValue(ContractUtils.getApprovalDate(contract));
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer() != null ? contract.getDealer().getNameLocale() : "");
			item.getItemProperty(ASSET + "." + SERIE).setValue(contract.getAsset() != null ? contract.getAsset().getSerie() : "");
			item.getItemProperty(FINANCIAL_PRODUCT + "." + DESC_EN).setValue(contract.getFinancialProduct() != null ? contract.getFinancialProduct().getDescLocale() : "");
			scanPagedTable.refreshContainerDataSource();
		}
	}
	
	/**
	 * @param contract
	 */
	private void removeScanContractItem(Contract contract) {
		for (Iterator i = scanPagedTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = scanPagedTable.getItem(iid);
		    String externalRef = (String) item.getItemProperty(Contract.EXTERNALREFERENCE).getValue();
		    if (externalRef.equals(ContractUtils.getApplicationID(contract))) {
		    	Indexed indexedContainer = scanPagedTable.getContainerDataSource();
		    	indexedContainer.removeItem(iid);
		    	scanPagedTable.refreshContainerDataSource();
		    	break;
		    }
		}
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition("select", I18N.message("check"), CheckBox.class, Align.LEFT, 30));				
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(EXTERNAL_REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("type", I18N.message("type"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(APPLICANT + "." + NAME_EN, I18N.message("name"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(APPROVAL_DATE, I18N.message("approval.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ASSET + "." + SERIE, I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 150));
		
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
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
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
