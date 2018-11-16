package com.nokor.efinance.gui.ui.panel.contract.close;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MCloseContract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class CloseContractPanel extends AbstractControlPanel implements MCloseContract, ClickListener {

	private static final long serialVersionUID = 1665410505489320940L;
	private Contract contract;
	
	private Label lblPriceToday;
	private SimpleTable<Entity> payTable;
	private Button btnNewRequest;
	private SimpleTable<Entity> previousRequestTable;
	
	//private PreviousRequestContractPanel previousRequestContractPanel;
	
	private PreviousRequestContractPopupPanel previousRequestContractPopupPanel;
	
	public CloseContractPanel() {
		setMargin(true);
		init();
		
		Label lblPriceAsToday = getLabel("price.as.of.today");
		HorizontalLayout priceTodayLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		priceTodayLayout.addComponent(lblPriceAsToday);
		priceTodayLayout.addComponent(lblPriceToday);
		
		Panel previousRequestPanel = ComponentLayoutFactory.getPanel(previousRequestTable, false, true);
		previousRequestPanel.setCaption(I18N.message("previous.request"));
			
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(priceTodayLayout);
		mainLayout.addComponent(payTable);
		mainLayout.addComponent(btnNewRequest);
		mainLayout.addComponent(previousRequestPanel);
		//mainLayout.addComponent(previousRequestContractPanel);
		addComponent(mainLayout);
	}
	
	/**
	 * init
	 */
	private void init() {
		lblPriceToday = getLabelValue();
		payTable = getSimpleTable(getPayColumnDefinitions());
		payTable.setSizeFull();
		payTable.setPageLength(9);
		btnNewRequest = ComponentLayoutFactory.getButtonStyle("new.request", FontAwesome.PLUS, 100, "btn btn-success");
		btnNewRequest.addClickListener(this);
		
		previousRequestTable = getSimpleTable(getPreviousRequestColumnDefinitions());
		previousRequestTable.setSizeFull();
		previousRequestTable.setPageLength(5);
		
		//previousRequestContractPanel = new PreviousRequestContractPanel();
		previousRequestContractPopupPanel = new PreviousRequestContractPopupPanel();
	}
	
	/**
	 * Assign Value 
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		this.contract = contract;
		Collection collection = contract.getCollection();
		//previousRequestContractPanel.assignValue(contract);
		previousRequestContractPopupPanel.assignValue(contract);
		if (collection != null) {
			
			setPayTableIndexedContainer(collection);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getPayColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ITEMS, I18N.message("items"), String.class, Align.LEFT, 300));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setPayTableIndexedContainer(Collection collection) {
		payTable.removeAllItems();
		Container indexedContainer = payTable.getContainerDataSource();
		double totalToPay = 0d;
		double totalOutstanding = 0d;
		double outstandingInterest = 0d;
		double interestDiscount = 0d;
		double outstandingPenalties = 0d;
		double outstandingPressingFee = 0d;
		double outstandingFollowingFee = 0d;
		double outstandingRepossessionFee = 0d;
		double ownershipTranferance = 0d;
		for (int i = 0; i < 9; i++) {
			Item item = indexedContainer.addItem(i);
			if (i == 0) { 
				totalOutstanding = collection.getTiBalanceCapital() + collection.getTiBalanceInterest();
				item.getItemProperty(ITEMS).setValue(I18N.message("total.outstanding"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(totalOutstanding));
			} else if (i == 1) {
				outstandingInterest = collection.getTiBalanceInterest() != null ? collection.getTiBalanceInterest() : 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("outstanding.interest"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(outstandingInterest));
			} else if (i == 2) {
				interestDiscount = collection.getTiBalanceInterest() != null ? collection.getTiBalanceInterest() / 2 : 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("interest.discount"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(interestDiscount));
			} else if (i == 3) {
				outstandingPenalties = collection.getTiPenaltyAmount() != null ? collection.getTiPenaltyAmount() : 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("outstanding.penalties"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(outstandingPenalties));
			} else if (i == 4) {
				outstandingPressingFee = 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("outstanding.pressing.fee"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(outstandingPressingFee));
			} else if (i == 5) {
				outstandingFollowingFee = collection.getTiFollowingFeeAmount() != null ? collection.getTiFollowingFeeAmount() : 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("outstanding.following.fee"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(outstandingFollowingFee));
			} else if (i == 6) {
				outstandingRepossessionFee = collection.getTiBalanceRepossessionFee() != null ? collection.getTiBalanceRepossessionFee() : 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("outstanding.repossession.fee"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(outstandingRepossessionFee));
			} else if (i == 7) {
				ownershipTranferance = 0d;
				item.getItemProperty(ITEMS).setValue(I18N.message("ownership.tranferance"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(ownershipTranferance));
			} else if (i == 8) {
				totalToPay = (totalOutstanding - interestDiscount) + 
						outstandingPenalties + 
						outstandingPressingFee + 
						outstandingFollowingFee + 
						outstandingRepossessionFee;
				item.getItemProperty(ITEMS).setValue(I18N.message("total.to.pay"));
				item.getItemProperty(AMOUNT).setValue(AmountUtils.format(totalToPay));
			}
			
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getPreviousRequestColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(PRICE, I18N.message("price"), Amount.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		return simpleTable;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnNewRequest) {
			UI.getCurrent().addWindow(previousRequestContractPopupPanel);
		} 
	}

}
