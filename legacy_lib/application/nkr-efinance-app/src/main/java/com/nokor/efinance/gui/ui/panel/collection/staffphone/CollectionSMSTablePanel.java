package com.nokor.efinance.gui.ui.panel.collection.staffphone;

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
import com.nokor.efinance.gui.ui.panel.contract.notes.sms.SMSPopupPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;

/**
 * SMS table in collection left panel 
 * @author uhout.cheng
 */
public class CollectionSMSTablePanel extends AbstractControlPanel implements FinServicesHelper, MContractSms, ClickListener {
	
	/** */
	private static final long serialVersionUID = 4726274629670363749L;
	
	private SimpleTable<Entity> simpleTable;
	private Button btnAdd;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public CollectionSMSTablePanel() {
		setMargin(true);
		setSpacing(true);
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setWidth(100, Unit.PERCENTAGE);
		simpleTable.setPageLength(3);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		btnAdd = getButton("add", FontAwesome.PLUS);
		simpleTable = getSimpleTable(getColumnDefinitions());
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		
		addComponent(navigationPanel);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(SENDTO, I18N.message("send.to"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PHONENUMBER, I18N.message("phone.no"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MESSAGE, I18N.message("text"), String.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		simpleTable.removeAllItems();
		if (contract != null) {
			setTableIndexedContainer(getContractSMSs());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ContractSms> getContractSMSs() {
		ContractSmsRestriction restrictions = new ContractSmsRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
		return CONT_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param caption
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 */
	public void displayPopup() {
		SMSPopupPanel smsPopupPanel = SMSPopupPanel.show(contract, new SMSPopupPanel.Listener() {
			private static final long serialVersionUID = 2414729693524646564L;
			@Override
			public void onClose(SMSPopupPanel dialog) {
				setTableIndexedContainer(getContractSMSs());
			}
		});
		smsPopupPanel.assignValues(contract);
	}

	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<ContractSms> contractSms) {
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

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			displayPopup();
		}	
	}

}
