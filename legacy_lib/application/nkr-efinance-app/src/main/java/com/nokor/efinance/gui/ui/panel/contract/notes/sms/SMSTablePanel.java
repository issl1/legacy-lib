package com.nokor.efinance.gui.ui.panel.contract.notes.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.contract.model.MContractSms;
import com.nokor.efinance.core.contract.service.ContractSmsRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * SMS table in notes 
 * @author uhout.cheng
 */
public class SMSTablePanel extends Panel implements FinServicesHelper, MContractSms {
	
	/** */
	private static final long serialVersionUID = -5665146711299806140L;
	
	private SimpleTable<Entity> simpleTable;
	private Contract contract;
	
	/**
	 * 
	 */
	public SMSTablePanel() {
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
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(SENDTO, I18N.message("send.to"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(PHONENUMBER, I18N.message("phone.no"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(MESSAGE, I18N.message("text"), String.class, Align.LEFT, 170));
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
	public void setTableIndexedContainer(List<ContractSms> contractSms) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (contractSms != null && !contractSms.isEmpty()) {
			for (ContractSms sms : contractSms) {
				if (sms != null) {
					Item item = indexedContainer.addItem(sms.getId());
					item.getItemProperty(ID).setValue(sms.getId());
					item.getItemProperty(CREATEDATE).setValue(sms.getCreateDate());
					item.getItemProperty(CREATEUSER).setValue(sms.getUserLogin());
					item.getItemProperty(SENDTO).setValue(sms.getSendTo());
					item.getItemProperty(PHONENUMBER).setValue(sms.getPhoneNumber());
					item.getItemProperty(MESSAGE).setValue(sms.getMessage());
				}
			}
		}
	}
	
	/**
	 */
	public void refreshTable() {
		if (contract != null) {
			ContractSmsRestriction restrictions = new ContractSmsRestriction();
			restrictions.setConId(contract.getId());
			restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
			setTableIndexedContainer(CONT_SRV.list(restrictions));
		}
	}
	
	/**
	 */
	public void displayPopup() {
		SMSPopupPanel smsPopupPanel = SMSPopupPanel.show(contract, new SMSPopupPanel.Listener() {
			private static final long serialVersionUID = 2414729693524646564L;
			@Override
			public void onClose(SMSPopupPanel dialog) {
				refreshTable();
			}
		});
		smsPopupPanel.assignValues(contract);
	}

}
