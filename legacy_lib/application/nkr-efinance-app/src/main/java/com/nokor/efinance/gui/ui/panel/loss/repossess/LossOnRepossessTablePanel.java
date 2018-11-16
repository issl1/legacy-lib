package com.nokor.efinance.gui.ui.panel.loss.repossess;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.auction.model.ContractAuctionData;
import com.nokor.efinance.core.auction.service.AuctionService;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractAdjustment;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * Loss On Repossess Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LossOnRepossessTablePanel extends AbstractTablePanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 8888359926788516013L;
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private AuctionService auctionService;
	
	private Long totalBuyer;
	private Double sumUnpaidAccruedInterestReceivable;
	private Double sumRemainingPricipalBalance;
	private Double sumRemainingUnearnIncomeBalance;
	private Double sumRemainingVATBalanceOnLeasing;
	private Double sumRemainingInsuranceBalance;
	private Double sumRemainingVATBalanceOnInsurance;
	private Double sumRemainingServiceIncomeBalance;
	private Double sumRemainingVATBalanceOnServiceIncome;
	private Double sumPenaltyAmount;
	private Double sumRepossessionFee;
	private Double sumCollectionFee;
	private Double sumTotalAmount;
	private Double sumSellingPrice;
	private Double sumAmountRefundToLessee;
	private Double sumAmountLost;
	//private Double sumLesseeRemainingBalanceAfterRepossess;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		initValue();
		init(I18N.message("loss.on.repossess"));
		
		setCaption(I18N.message("loss.on.repossess"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		divideInHalf();
		createTableFooter();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<Contract>(searchPanel.getRestrictions());
		
		pagedDefinition.setRowRenderer(new LossOnRepossessRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(CONTRACT_STATUS, I18N.message(CONTRACT_STATUS), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("date.selling", "Date of Sells", String.class, Align.LEFT, 100 );
		pagedDefinition.addColumnDefinition("auction.status", I18N.message("auction.status"), String.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition("contract.reference", I18N.message("contract.reference"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("firstname.en", I18N.message("firstname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lastname.en",I18N.message("lastname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("buyer", I18N.message("buyer"), String.class, Align.LEFT, 150);
		
		pagedDefinition.addColumnDefinition("unpaid.accrued.interest.receivable", I18N.message("unpaid.accrued.interest.receivable"), Amount.class, Align.RIGHT, 250);
		pagedDefinition.addColumnDefinition("remaining.principal.balance", I18N.message("remaining.principal.balance"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.unearned.income.balance", I18N.message("remaining.unearned.income.balance"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.vat.balance.on.leasing", I18N.message("remaining.vat.balance.on.leasing"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.insurance.balance", I18N.message("remaining.insurance.balance"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.vat.balance.on.insurance", I18N.message("remaining.vat.balance.on.insurance"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.service.income.balance", I18N.message("remaining.service.income.balance"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("remaining.vat.balance.on.service.income", I18N.message("remaining.vat.balance.on.service.income"), Amount.class, Align.RIGHT, 220);
		pagedDefinition.addColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 130);
		pagedDefinition.addColumnDefinition("repossession.fee", I18N.message("repossession.fee"), Amount.class, Align.RIGHT, 130);
		pagedDefinition.addColumnDefinition("collection.fee", I18N.message("collection.fee"), Amount.class, Align.RIGHT, 130);
		pagedDefinition.addColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 130);
		
		pagedDefinition.addColumnDefinition("selling.price", I18N.message("selling.price"), Amount.class, Align.RIGHT, 130);
		pagedDefinition.addColumnDefinition("amount.refund.to.lessee", I18N.message("amount.refund.to.lessee"), Amount.class, Align.RIGHT, 200);
		pagedDefinition.addColumnDefinition("amount.lost", I18N.message("amount.lost"), Amount.class, Align.RIGHT, 130);
		//pagedDefinition.addColumnDefinition("lessee.remaining.balance.after.repossession", I18N.message("lessee.remaining.balance.after.repossession"), Double.class, Align.RIGHT, 250);
		
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<Contract>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * Custome Loss on Repossess Row Renderer
	 * @author bunlong.taing
	 */
	private class LossOnRepossessRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Contract contract = (Contract) entity;
			// TODO PYI
			ContractAuctionData contractAuctionData = null;//contract.getContractAuctionData();
			Applicant applicant = contract.getApplicant();
			Collection contractOtherData = contract.getCollection();
			ContractAdjustment contractAdjustment = contract.getContractAdjustment();
			
			 String newFormat = "dd/MM/yyyy";
			 SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
			 String str="";
			 if(contractAuctionData.getSellingDate()==null){
				 str=" ";
			 }else str=sdf.format(contractAuctionData.getSellingDate());
			 
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty(CONTRACT_STATUS).setValue(contract.getWkfStatus().getDesc());
			item.getItemProperty("date.selling").setValue(str);
			item.getItemProperty("auction.status").setValue(contract.getWkfStatus() != null ? contract.getWkfStatus().getDesc() : "");
			item.getItemProperty("contract.reference").setValue(contract.getReference());
			
			item.getItemProperty("unpaid.accrued.interest.receivable").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidAccruedInterestReceivableUsd()));
			item.getItemProperty("remaining.principal.balance").setValue(AmountUtils.convertToAmount(contractAdjustment.getTiAdjustmentPrincipal()));
			item.getItemProperty("remaining.unearned.income.balance").setValue(AmountUtils.convertToAmount(contractAdjustment.getTiAdjustmentInterest()));
			item.getItemProperty("remaining.vat.balance.on.leasing").setValue(AmountUtils.convertToAmount(contractAdjustment.getVatAdjustmentPrincipal()));
			item.getItemProperty("remaining.insurance.balance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidUnearnedInsuranceIncomeUsd()));
			item.getItemProperty("remaining.vat.balance.on.insurance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getVatUnpaidUnearnedInsuranceIncomeUsd()));
			item.getItemProperty("remaining.service.income.balance").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiUnpaidUnearnedServicingIncomeUsd()));
			item.getItemProperty("remaining.vat.balance.on.service.income").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getVatUnpaidUnearnedServicingIncomeUsd()));
			item.getItemProperty("penalty.amount").setValue(AmountUtils.convertToAmount(contractOtherData.getTiPenaltyAmount()));
			
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
			
			sumUnpaidAccruedInterestReceivable += MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidAccruedInterestReceivableUsd());
			sumRemainingPricipalBalance += MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentPrincipal());
			sumRemainingUnearnIncomeBalance += MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentInterest());
			sumRemainingVATBalanceOnLeasing += MyNumberUtils.getDouble(contractAdjustment.getVatAdjustmentPrincipal());
			sumRemainingInsuranceBalance += MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidUnearnedInsuranceIncomeUsd());
			sumRemainingVATBalanceOnInsurance += MyNumberUtils.getDouble(contractAdjustment.getVatUnpaidUnearnedInsuranceIncomeUsd());
			sumRemainingServiceIncomeBalance += MyNumberUtils.getDouble(contractAdjustment.getTiUnpaidUnearnedServicingIncomeUsd());
			sumRemainingVATBalanceOnServiceIncome += MyNumberUtils.getDouble(contractAdjustment.getVatUnpaidUnearnedServicingIncomeUsd());
			sumPenaltyAmount += MyNumberUtils.getDouble(contractOtherData.getTiPenaltyAmount());
			
			if (applicant != null) {
				item.getItemProperty("firstname.en").setValue(applicant.getIndividual().getFirstNameEn());
				item.getItemProperty("lastname.en").setValue(applicant.getIndividual().getLastNameEn());
			}
			
			if (contractAuctionData != null) {
				// contractAuctionData = entityService.getById(ContractAuctionData.class, contractAuctionData.getId());
				
				totalAmount += MyNumberUtils.getDouble(contractAuctionData.getRepossessionFeeUsd());
				totalAmount += MyNumberUtils.getDouble(contractAuctionData.getCollectionFeeUsd());
				
				sellingPrice += MyNumberUtils.getDouble(contractAuctionData.getTiAssetSellingPrice()); 
				
				item.getItemProperty("repossession.fee").setValue(AmountUtils.convertToAmount(contractAuctionData.getRepossessionFeeUsd()));
				item.getItemProperty("collection.fee").setValue(AmountUtils.convertToAmount(contractAuctionData.getCollectionFeeUsd()));
				item.getItemProperty("selling.price").setValue(AmountUtils.convertToAmount(contractAuctionData.getTiAssetSellingPrice()));
				item.getItemProperty("buyer").setValue(contractAuctionData.getBuyer() != null ?
						contractAuctionData.getBuyer().getDescEn() : "");
				if (contractAuctionData.getBuyer() != null) {
					totalBuyer++;
				}
				
				sumRepossessionFee += MyNumberUtils.getDouble(contractAuctionData.getRepossessionFeeUsd());
				sumCollectionFee += MyNumberUtils.getDouble(contractAuctionData.getCollectionFeeUsd());
				sumSellingPrice += sellingPrice;
			}
			
			Double amountLoss = (sellingPrice - MyNumberUtils.getDouble(contract.getContractAdjustment().getTiAdjustmentPrincipal())) < 0 ? (sellingPrice - MyNumberUtils.getDouble(contract.getContractAdjustment().getTiAdjustmentPrincipal())) : 0d;
			Double amountRefundToLessee = (sellingPrice - totalAmount) < 0 ? 0d : (sellingPrice - totalAmount);
			item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount((totalAmount)));
			item.getItemProperty("amount.refund.to.lessee").setValue(AmountUtils.convertToAmount(amountRefundToLessee));
			item.getItemProperty("amount.lost").setValue(AmountUtils.convertToAmount(amountLoss));
			//item.getItemProperty("lessee.remaining.balance.after.repossession").setValue(amountLoss);
			
			sumTotalAmount += totalAmount;
			sumAmountRefundToLessee += amountRefundToLessee;
			sumAmountLost += amountLoss;
			//sumLesseeRemainingBalanceAfterRepossess += amountLoss;
		}
		
	}
	
	/**
	 * Create table footer
	 */
	private void createTableFooter () {
		pagedTable.setFooterVisible(true);
		
		pagedTable.setColumnFooter("buyer", totalBuyer.toString());
		pagedTable.setColumnFooter("unpaid.accrued.interest.receivable", AmountUtils.format(sumUnpaidAccruedInterestReceivable));
		pagedTable.setColumnFooter("remaining.principal.balance", AmountUtils.format(sumRemainingPricipalBalance));
		pagedTable.setColumnFooter("remaining.unearned.income.balance", AmountUtils.format(sumRemainingUnearnIncomeBalance));
		pagedTable.setColumnFooter("remaining.vat.balance.on.leasing", AmountUtils.format(sumRemainingVATBalanceOnLeasing));
		pagedTable.setColumnFooter("remaining.insurance.balance", AmountUtils.format(sumRemainingInsuranceBalance));
		pagedTable.setColumnFooter("remaining.vat.balance.on.insurance", AmountUtils.format(sumRemainingVATBalanceOnInsurance));
		pagedTable.setColumnFooter("remaining.service.income.balance", AmountUtils.format(sumRemainingServiceIncomeBalance));
		pagedTable.setColumnFooter("remaining.vat.balance.on.service.income", AmountUtils.format(sumRemainingVATBalanceOnServiceIncome));
		pagedTable.setColumnFooter("penalty.amount", AmountUtils.format(sumPenaltyAmount));
		pagedTable.setColumnFooter("repossession.fee", AmountUtils.format(sumRepossessionFee));
		pagedTable.setColumnFooter("collection.fee", AmountUtils.format(sumCollectionFee));
		pagedTable.setColumnFooter("total.amount", AmountUtils.format(sumTotalAmount));
		pagedTable.setColumnFooter("selling.price", AmountUtils.format(sumSellingPrice));
		pagedTable.setColumnFooter("amount.refund.to.lessee", AmountUtils.format(sumAmountRefundToLessee));
		pagedTable.setColumnFooter("amount.lost", AmountUtils.format(sumAmountLost));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		initValue();
		super.refresh();
		createTableFooter();
	}
	
	/**
	 * Initialize value
	 */
	private void initValue () {
		totalBuyer = 0l;
		sumUnpaidAccruedInterestReceivable = 0d;
		sumRemainingPricipalBalance = 0d;
		sumRemainingUnearnIncomeBalance = 0d;
		sumRemainingVATBalanceOnLeasing = 0d;
		sumRemainingInsuranceBalance = 0d;
		sumRemainingVATBalanceOnInsurance = 0d;
		sumRemainingServiceIncomeBalance = 0d;
		sumRemainingVATBalanceOnServiceIncome = 0d;
		sumPenaltyAmount = 0d;
		sumRepossessionFee = 0d;
		sumCollectionFee = 0d;
		sumTotalAmount = 0d;
		sumSellingPrice = 0d;
		sumAmountRefundToLessee = 0d;
		sumAmountLost = 0d;
		//sumLesseeRemainingBalanceAfterRepossess = 0d;
	}
	
	/**
	 * Divide the sum value in half
	 */
	private void divideInHalf () {
		totalBuyer /= 2;
		sumUnpaidAccruedInterestReceivable /= 2;
		sumRemainingPricipalBalance /= 2;
		sumRemainingUnearnIncomeBalance /= 2;
		sumRemainingVATBalanceOnLeasing /= 2;
		sumRemainingInsuranceBalance /= 2;
		sumRemainingVATBalanceOnInsurance /= 2;
		sumRemainingServiceIncomeBalance /= 2;
		sumRemainingVATBalanceOnServiceIncome /= 2;
		sumPenaltyAmount /= 2;
		sumRepossessionFee /= 2;
		sumCollectionFee /= 2;
		sumTotalAmount /= 2;
		sumSellingPrice /= 2;
		sumAmountRefundToLessee /= 2;
		sumAmountLost /= 2;
		//sumLesseeRemainingBalanceAfterRepossess /= 2;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Contract> createSearchPanel() {
		return new LossOnRepossessSearchPanel(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Contract getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Contract.class, id);
		}
		return null;
	}

}
