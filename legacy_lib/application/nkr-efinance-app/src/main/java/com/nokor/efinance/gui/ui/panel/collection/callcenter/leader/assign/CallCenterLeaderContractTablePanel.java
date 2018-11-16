package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.staff.CallCenterStaffPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Contract detail table in call center leader
 * @author uhout.cheng
 */
public class CallCenterLeaderContractTablePanel extends VerticalLayout implements FinServicesHelper, ItemClickListener, SelectedItem, MCollection {

	/** */
	private static final long serialVersionUID = -1776556795187920795L;

	private SimplePagedTable<Entity> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem;
	
	private CallCenterStaffPanel detailPanel;
	
	/**
	 * @param detailPanel
	 */
	public CallCenterLeaderContractTablePanel(CallCenterStaffPanel detailPanel) {
		this.detailPanel = detailPanel;
		
		pagedTable = new SimplePagedTable<Entity>(getColumnDefinitions());
		pagedTable.addItemClickListener(this);
		pagedTable.setWidth(600, Unit.PIXELS);
		
		VerticalLayout tableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		tableLayout.setWidth(600, Unit.PIXELS);
		tableLayout.addComponent(pagedTable);		
		tableLayout.addComponent(pagedTable.createControls());
		pagedTable.setPageLength(25);
		addComponent(tableLayout);
	}
		
	/**
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CONTRACTID, I18N.message("contract.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(DUEDAY, I18N.message("due.day"), Integer.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(ODM, I18N.message("odm"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), Amount.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantors"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(LATESTRESULT, I18N.message("latest.result"), String.class, Align.LEFT, 55));
		columnDefinitions.add(new ColumnDefinition(LATESTPAYMENTDATE, I18N.message("lpd"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(AMOUNTCOLLECTED, I18N.message("amount.collected"), Amount.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param contracts
	 * @param endDate
	 * @param isUnProcessed
	 * @return
	 */
	public IndexedContainer getIndexedContainer(List<Contract> contracts) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (contracts != null && !contracts.isEmpty()) {
			int index = 0;
			for (Contract contract : contracts) {
				String contractID = contract.getReference();
				int nbGuarantors = contract.getNumberGuarantors();
				String latestResult = "";
				int dpd = 0;
				int odm = 0;
				double apd = 0d;
				Integer dueDay = null;
				Date lastPaymentDate = null;
				double amtCollected = 0d;
				
				CallCenterHistory callCenterHistory = CALL_CTR_SRV.getLastCallCenterHistory(contract.getId());
				if (callCenterHistory != null) {
					ECallCenterResult result = callCenterHistory.getResult();
					latestResult = (result == null ? "" : result.getCode());
				}
				
				Collection collection = contract.getCollection();
				if (collection != null) {
					dpd = MyNumberUtils.getInteger(collection.getNbOverdueInDays());
					odm = MyNumberUtils.getInteger(collection.getDebtLevel());
					apd = MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue());
					dueDay = collection.getDueDay();
					lastPaymentDate = collection.getLastPaymentDate();
				}
				index = generateDataTable(indexedContainer, contractID, nbGuarantors, 
						dpd, odm, apd, dueDay, latestResult, lastPaymentDate, amtCollected, index);
				index++;
			}
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param indexedContainer
	 * @param contractID
	 * @param nbGuarantors
	 * @param dpd
	 * @param odm
	 * @param apd
	 * @param dueDay
	 * @param latestResult
	 * @param lastPaymentDate
	 * @param amtCollected
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int generateDataTable(Container indexedContainer, String contractID, int nbGuarantors, 
			int dpd, int odm, double apd, Integer dueDay, String latestResult, Date lastPaymentDate, double amtCollected, int index) {
		Item item = indexedContainer.addItem(index);
		item.getItemProperty(CONTRACTID).setValue(contractID);
		item.getItemProperty(DUEDAY).setValue(dueDay);
		item.getItemProperty(ODM).setValue(odm);
		item.getItemProperty(DPD).setValue(dpd);
		item.getItemProperty(APD).setValue(AmountUtils.convertToAmount(apd));
		item.getItemProperty(GUARANTOR).setValue(nbGuarantors);
		item.getItemProperty(LATESTRESULT).setValue(latestResult);
		item.getItemProperty(LATESTPAYMENTDATE).setValue(lastPaymentDate);
		item.getItemProperty(AMOUNTCOLLECTED).setValue(AmountUtils.convertToAmount(amtCollected));
		return index;
	}
	
	/**
	 * @param contracts
	 * @param endDate
	 * @param isUnProcessed
	 */
	private void assignValues(List<Contract> contracts) {
		pagedTable.setContainerDataSource(getIndexedContainer(contracts));
	}
	
	/**
	 * @return
	 */
	public String getContractId() {
		if (this.selectedItem != null) {
			return ((String) this.selectedItem.getItemProperty(CONTRACTID).getValue());
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

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
	}

	/**
	 * 
	 * @param contracts
	 */
	public void refresh(ContractRestriction restriction) {
		List<Contract> contracts = CONT_SRV.list(restriction);	
		assignValues(contracts);
	}
		
}
