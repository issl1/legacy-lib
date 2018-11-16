package com.nokor.efinance.gui.ui.panel.auctionresult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.auction.model.ContractAuctionData;
import com.nokor.efinance.core.auction.service.AuctionService;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.StorageLocation;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractAdjustment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.workflow.AuctionWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;


/**
 * Auction result panel for back office
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AuctionResultPanel.NAME)
public class AuctionResultPanel extends AbstractTabPanel implements View, FMEntityField, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = -5539784170746049481L;
	public static final String NAME = "auction.result";
	
	@Autowired
	private AuctionService auctionService;
	
	private TextField txtReference;
	private EntityRefComboBox<StorageLocation> cbxStorageLocation;
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions;

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Contract>(this.columnDefinitions);
        pagedTable.setCaption(I18N.message("auctions"));
		
		txtReference = ComponentFactory.getTextField("reference", false, 50, 200);
		cbxStorageLocation = new EntityRefComboBox<StorageLocation>(I18N.message("storage.location"));
		cbxStorageLocation.setRestrictions(new BaseRestrictions<StorageLocation>(StorageLocation.class));
		cbxStorageLocation.renderer();
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			/** */
			private static final long serialVersionUID = -1481397807271693022L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 2951690400645074827L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(txtReference));
		horizontalLayout.addComponent(new FormLayout(cbxStorageLocation));
		
		VerticalLayout searchLayout = new VerticalLayout();
		searchLayout.setMargin(true);
		searchLayout.setSpacing(true);
		searchLayout.addComponent(horizontalLayout);
		searchLayout.addComponent(buttonsLayout);
		
		Panel searchPanel = new Panel();
		searchPanel.setCaption(I18N.message("search"));
		searchPanel.setContent(searchLayout);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.addComponent(searchPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());

		TabSheet tabSheet = new TabSheet();
		tabSheet.addTab(contentLayout, I18N.message("auction.result"));
		
		return tabSheet;
	}

	/**
	 * @return List of ColumnDefinition
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("auction.status", I18N.message("auction.status"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("num.day.from.repossess", I18N.message("num.day.from.repossess"), Long.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("num.of.auction", I18N.message("num.of.auction"), Long.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition("contract.reference", I18N.message("contract.reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("buyer", I18N.message("buyer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("unpaid.accrued.interest.receivable", I18N.message("unpaid.accrued.interest.receivable"), Amount.class, Align.RIGHT, 250));
		columnDefinitions.add(new ColumnDefinition("remaining.principal.balance", I18N.message("remaining.principal.balance"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.unearned.income.balance", I18N.message("remaining.unearned.income.balance"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.vat.balance.on.leasing", I18N.message("remaining.vat.balance.on.leasing"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.insurance.balance", I18N.message("remaining.insurance.balance"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.vat.balance.on.insurance", I18N.message("remaining.vat.balance.on.insurance"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.service.income.balance", I18N.message("remaining.service.income.balance"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("remaining.vat.balance.on.service.income", I18N.message("remaining.vat.balance.on.service.income"), Amount.class, Align.RIGHT, 250));
		columnDefinitions.add(new ColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("repossession.fee", I18N.message("repossession.fee"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("collection.fee", I18N.message("collection.fee"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("average.estimated.price.of.motorcycle", I18N.message("average.estimated.price.of.motorcycle"), Amount.class, Align.RIGHT, 220));
		columnDefinitions.add(new ColumnDefinition("selling.price", I18N.message("selling.price"), Amount.class, Align.RIGHT, 150));
		return columnDefinitions;
	}
	
	/** */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Contract> contracts) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (Contract contract : contracts) {
			// TODO PYI
			ContractAuctionData contractAuctionData = null;//contract.getContractAuctionData();
			Collection contractOtherData = contract.getCollection();
			
			ContractAdjustment contractAdjustment = contract.getContractAdjustment();
			
			Item item = indexedContainer.addItem(contract.getId());
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty("auction.status").setValue(contract.getWkfStatus() != null ? contract.getWkfStatus().getDesc() : "");
			item.getItemProperty("num.day.from.repossess").setValue(auctionService.getDayFromRepossess(contract));
			item.getItemProperty("num.of.auction").setValue(getNumberOfAuction(contract));
			item.getItemProperty("contract.reference").setValue(contract.getReference());
			
			item.getItemProperty("unpaid.accrued.interest.receivable").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidAccruedInterestReceivableUsd()));
			item.getItemProperty("remaining.principal.balance").setValue(AmountUtils.convertToAmount(contractAdjustment.getTiAdjustmentPrincipal()));
			item.getItemProperty("remaining.unearned.income.balance").setValue(AmountUtils.convertToAmount(contractAdjustment.getTiAdjustmentInterest()));
			item.getItemProperty("remaining.vat.balance.on.leasing").setValue(AmountUtils.convertToAmount(contractAdjustment.getVatAdjustmentPrincipal()));
			item.getItemProperty("remaining.insurance.balance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidUnearnedInsuranceIncomeUsd()));
			item.getItemProperty("remaining.vat.balance.on.insurance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getVatUnpaidUnearnedInsuranceIncomeUsd()));
			item.getItemProperty("remaining.service.income.balance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidUnearnedServicingIncomeUsd()));
			item.getItemProperty("remaining.vat.balance.on.service.income").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getVatUnpaidUnearnedServicingIncomeUsd()));
			item.getItemProperty("penalty.amount").setValue(contractOtherData.getTiPenaltyAmount() == null ? AmountUtils.convertToAmount(0d) 
					: AmountUtils.convertToAmount(contractOtherData.getTiPenaltyAmount()) );
			
			Double totalAmount = 0d;
			totalAmount += MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidAccruedInterestReceivableUsd()) 
					+ MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentPrincipal())
					+ MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentInterest())
					+ MyNumberUtils.getDouble(contractAdjustment.getVatAdjustmentPrincipal())
					+ MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidUnearnedInsuranceIncomeUsd())
					+ MyNumberUtils.getDouble(contractAdjustment.getVatUnpaidUnearnedInsuranceIncomeUsd())
					+ MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidUnearnedServicingIncomeUsd())
					+ MyNumberUtils.getDouble(contractAdjustment.getVatUnpaidUnearnedServicingIncomeUsd())
					+ MyNumberUtils.getDouble(contractOtherData.getTiPenaltyAmount());
			Double sellingPrice = 0d;
			
			if (contractAuctionData != null) {
				totalAmount += MyNumberUtils.getDouble(contractAuctionData.getRepossessionFeeUsd());
				totalAmount += MyNumberUtils.getDouble(contractAuctionData.getCollectionFeeUsd());
				
				sellingPrice += MyNumberUtils.getDouble(contractAuctionData.getTiAssetSellingPrice());
				
				contractAuctionData = ENTITY_SRV.getById(ContractAuctionData.class, contractAuctionData.getId());
				item.getItemProperty("buyer").setValue(contractAuctionData.getBuyer() != null ?
						contractAuctionData.getBuyer().getDescEn() : "");
				item.getItemProperty("repossession.fee").setValue(AmountUtils.convertToAmount(contractAuctionData.getRepossessionFeeUsd()));
				item.getItemProperty("collection.fee").setValue(AmountUtils.convertToAmount(contractAuctionData.getCollectionFeeUsd()));
				Double averageEstimatedPrice = MyNumberUtils.getDouble(contractAuctionData.getTiAssetEstimationPrice1())
						+ MyNumberUtils.getDouble(contractAuctionData.getTiAssetEstimationPrice2())
						+ MyNumberUtils.getDouble(contractAuctionData.getTiAssetEstimationPrice3());
				item.getItemProperty("average.estimated.price.of.motorcycle").setValue(AmountUtils.convertToAmount(averageEstimatedPrice / 3));
				item.getItemProperty("selling.price").setValue(AmountUtils.convertToAmount(contractAuctionData.getTiAssetSellingPrice()));
			}
			item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(totalAmount));
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Get number of auction
	 * @param contract
	 * @return
	 */
	private Long getNumberOfAuction (Contract contract) {
		// TODO PYI
//		List<AuctionStatusHistory> histories = contract.getHistories();
		Long numberOfAuction = 0l;
//		if (histories == null || histories.isEmpty()) {
//			return numberOfAuction;
//		}
//		for (AuctionStatusHistory history : histories) {
//			if (history.getAuctionStatus().equals(WkfAuctionStatus.VAL)) {
//				numberOfAuction += 1;
//			}
//		}
		return numberOfAuction;
	}
	
	/** */
	private void search() {
		List<Contract> contracts = ENTITY_SRV.list(getRestrictions());
		setIndexedContainer(contracts);
	}
	
	/**
	 * @return BaseRestrictions
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("requestRepossess", Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq("auctionStatus", AuctionWkfStatus.WRE));
		
		if (cbxStorageLocation.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("storageLocation", cbxStorageLocation.getSelectedEntity()));
		}
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("reference", txtReference.getValue(), MatchMode.ANYWHERE));
		}
		
		return restrictions;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		cbxStorageLocation.setSelectedEntity(null);
		txtReference.setValue("");
	}
}
