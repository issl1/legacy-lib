package com.nokor.efinance.gui.ui.panel.contract.notes.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.MContractRequest;
import com.nokor.efinance.core.contract.service.ContractRequestRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * Request table in notes 
 * @author uhout.cheng
 */
public class RequestTablePanel extends Panel implements ItemClickListener, SelectedItem, FinServicesHelper, MContractRequest {

	/** */
	private static final long serialVersionUID = 5214637844486832879L;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private Contract contract;
	private RequestPopupPanel requestPopupPanel;
	
	/**
	 * 
	 */
	public RequestTablePanel() {
		init();
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
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		simpleTable.setSizeFull();
		setContent(simpleTable);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(REQUESTTYPE, I18N.message("request"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMMENT, I18N.message("comment"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PROCESSED, I18N.message("processed"), String.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		refreshTable();
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<ContractRequest> contractRequests) {
		simpleTable.removeAllItems();
		selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (contractRequests != null && !contractRequests.isEmpty()) {
			for (ContractRequest contractRequest : contractRequests) {
				if (contractRequest != null) {
					Item item = indexedContainer.addItem(contractRequest.getId());
					item.getItemProperty(ID).setValue(contractRequest.getId());
					item.getItemProperty(CREATEDATE).setValue(contractRequest.getCreateDate());
					item.getItemProperty(CREATEUSER).setValue(contractRequest.getUserLogin());
					item.getItemProperty(REQUESTTYPE).setValue(contractRequest.getRequestType() != null ? contractRequest.getRequestType().getDescLocale() : "");
					item.getItemProperty(COMMENT).setValue(contractRequest.getComment());
					item.getItemProperty(PROCESSED).setValue(getProcessed(contractRequest));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param contractRequest
	 * @return
	 */
	private String getProcessed(ContractRequest contractRequest) {
		if (Boolean.TRUE.equals(contractRequest.isProcessed())) {
			return I18N.message("yes");
		} else {
			return I18N.message("no");
		}
	}
	
	/**
	 */
	public void refreshTable() {
		if (contract != null) {
			ContractRequestRestriction restrictions = new ContractRequestRestriction();
			restrictions.setConId(contract.getId());
			restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
			setTableIndexedContainer(CONT_SRV.list(restrictions));
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
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			displayPopup();
		}
	}
	
	/**
	 * 
	 */
	public void displayPopup() {
		requestPopupPanel = new RequestPopupPanel(contract, this);
		ContractRequest contractRequest = null;
		if (getItemSelectedId() != null) {
			contractRequest = CONT_SRV.getById(ContractRequest.class, getItemSelectedId());
		}
		requestPopupPanel.assignValues(contractRequest);
		UI.getCurrent().addWindow(requestPopupPanel);
	}

}
