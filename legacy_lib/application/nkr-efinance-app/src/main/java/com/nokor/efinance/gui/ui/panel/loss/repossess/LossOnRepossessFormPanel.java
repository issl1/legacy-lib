package com.nokor.efinance.gui.ui.panel.loss.repossess;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.auction.model.ContractAuctionData;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AmountField;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.FormLayout;

/**
 * Loss on Repossess Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LossOnRepossessFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 3277197191527373371L;
	
	private AmountField txtPenaltyAmount;
	private AmountField txtRepossessionFee;
	private AmountField txtCollectionFee;
	private AmountField txtSellingPrice;
	
	private Contract contract;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtPenaltyAmount = new AmountField(I18N.message("penalty.amount"));
		txtPenaltyAmount.setWidth(200, Unit.PIXELS);
		txtRepossessionFee = new AmountField(I18N.message("repossession.fee"));
		txtRepossessionFee.setWidth(200, Unit.PIXELS);
		txtCollectionFee = new AmountField(I18N.message("collection.fee"));
		txtCollectionFee.setWidth(200, Unit.PIXELS);
		txtSellingPrice = new AmountField(I18N.message("selling.price"));
		txtSellingPrice.setWidth(200, Unit.PIXELS);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtPenaltyAmount);
		formLayout.addComponent(txtRepossessionFee);
		formLayout.addComponent(txtCollectionFee);
		formLayout.addComponent(txtSellingPrice);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		// TODO PYI
		ContractAuctionData contractAuctionData = null;//contract.getContractAuctionData();
		if (contractAuctionData == null) {
			contractAuctionData = new ContractAuctionData();
			contractAuctionData.setContract(contract);
		}
		
		contractAuctionData.setPenaltyUsd(getDouble(txtPenaltyAmount));
		contractAuctionData.setRepossessionFeeUsd(getDouble(txtRepossessionFee));
		contractAuctionData.setCollectionFeeUsd(getDouble(txtCollectionFee));
		contractAuctionData.setTiAssetSellingPrice(getDouble(txtSellingPrice));
		contractAuctionData.setTeAssetSellingPrice(getDouble(txtSellingPrice));
		
		return contractAuctionData;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		ENTITY_SRV.saveOrUpdate(getEntity());
		contract = ENTITY_SRV.getById(Contract.class, contract.getId());
	}
	
	/**
	 * Assign value to form
	 * @param id
	 */
	public void assignValues (Long id) {
		reset();
		this.contract = ENTITY_SRV.getById(Contract.class, id);
		// TODO PYI
//		if (contract.getContractAuctionData() != null) {
//			ContractAuctionData auctionData = contract.getContractAuctionData();
//			txtCollectionFee.setValue(AmountUtils.format(auctionData.getCollectionFeeUsd()));
//			txtPenaltyAmount.setValue(AmountUtils.format(auctionData.getPenaltyUsd()));
//			txtRepossessionFee.setValue(AmountUtils.format(auctionData.getRepossessionFeeUsd()));
//			txtSellingPrice.setValue(AmountUtils.format(auctionData.getTiAssetSellingPrice()));
//		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.contract = null;
		txtCollectionFee.setValue(null);
		txtPenaltyAmount.setValue(null);
		txtRepossessionFee.setValue(null);
		txtSellingPrice.setValue(null);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#getDouble(com.vaadin.ui.AbstractTextField)
	 */
	@Override
	public Double getDouble(AbstractTextField textField) {
		if (textField.getValue() == null) {
			return 0d;
		}
		return super.getDouble(textField);
	}

}
